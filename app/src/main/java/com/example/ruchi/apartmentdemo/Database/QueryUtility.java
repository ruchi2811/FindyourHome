package com.example.ruchi.apartmentdemo.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.example.ruchi.apartmentdemo.Activity.OwnerDashboardActivity;
import com.example.ruchi.apartmentdemo.R;

import java.io.ByteArrayOutputStream;

public class QueryUtility extends SQLiteOpenHelper{

    private final static String TENANTS_TABLE = "tenants";
    private final static String FLATS_TABLE = "flats";
    private final static String SESSION_TABLE = "session";

    private final static String DATABASE_NAME = "apartment";
    private final static int DATABASE_VERSION = 1;
    private Context context;

    String CREATE_FLAT_TABLE_QUERY = "CREATE TABLE " + FLATS_TABLE + " (_id int PRIMARY KEY, name text, address text, city text, image blob)";
    String CREATE_SESSION_TABLE_QUERY = "CREATE TABLE " + SESSION_TABLE + " (userID text, password text)";
    String CREATE_TENANT_TABLE_QUERY = "CREATE TABLE " + TENANTS_TABLE + " (_id text PRIMARY KEY, name text, flat text, FOREIGNKEY flat references flats(name), contact text, email text, password text, rent int, charges int, image blob)";


    private static QueryUtility instance;

    public static synchronized QueryUtility getInstance(Context context) {
        if (instance == null) {
            instance = new QueryUtility(context.getApplicationContext());
        }
        return instance;
    }

    public QueryUtility(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FLAT_TABLE_QUERY);
        db.execSQL(CREATE_TENANT_TABLE_QUERY);
        db.execSQL(CREATE_SESSION_TABLE_QUERY);
        db.beginTransaction();

        ContentValues sessionValues = new ContentValues();
        sessionValues.put("userID", "null");
        sessionValues.put("password", "null");
        db.insert(SESSION_TABLE, null, sessionValues);

        String[] flatNames = {"Sunshine Colony", "City Avenue", "Ganapathy Nagar", "Green Villa"};
        String[] flatCities = {"Anand","Ahmedabad", "Vadodara", "Surat"};
        String[] flatAddress = {"Ganesha Residency", "Prestige Manor", "Green City", "Shreepad Antillia"};
        String[] tenantNames = {"Ananya Shah", "Govind GS", "Shivam S", "Raval K"};
        String[] tenantFlats = {"Sunshine Colony", "City Avenue", "Ganapathy Nagar", "Green Villa"};
        String[] tenantIds = {"ananya", "govind", "siva", "raval"};
        String[] tenantEmails = {"ananya@tenant.com", "govind@tenant.com", "shivam@tenant.com", "raval03@tenant.com"};

        Bitmap avatar = BitmapFactory.decodeResource(context.getResources(), R.drawable.model);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        avatar.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] avatarBlob = byteArrayOutputStream.toByteArray();
        Bitmap flatImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.flat);
        byteArrayOutputStream = new ByteArrayOutputStream();
        flatImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] flatImageBlob = byteArrayOutputStream.toByteArray();

        for (int i = 0; i < 1; i++) {
            ContentValues tenantValues = new ContentValues();
            tenantValues.put("_id", tenantIds[i]);
            tenantValues.put("name", tenantNames[i]);
            tenantValues.put("flat", tenantFlats[i]);
            tenantValues.put("contact", "987654321");
            tenantValues.put("email", tenantEmails[i]);
            tenantValues.put("password", tenantIds[i]);
            tenantValues.put("rent", 13000);
            tenantValues.put("charges", 3500);
            tenantValues.put("image", avatarBlob);
            db.insert(TENANTS_TABLE, null, tenantValues);

            ContentValues flatValues = new ContentValues();
            flatValues.put("_id", i);
            flatValues.put("name", flatNames[i]);
            flatValues.put("city", flatCities[i]);
            flatValues.put("address", flatAddress[i]);
            flatValues.put("image", flatImageBlob);
            db.insert(FLATS_TABLE, null, flatValues);
        }
        Toast.makeText(context, "Created new Database", Toast.LENGTH_SHORT).show();
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String getSession() {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * from session;", null);
        if(cursor.moveToLast()) {
            if(cursor.getString(cursor.getColumnIndex("userID")).equals("null")) {
                return null;
            } else {
                return cursor.getString(cursor.getColumnIndex("userID"));
            }
        }
        return null;
    }

    public void setSessionTable(String userID, String password) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        db.delete("session", null, null);
        ContentValues sessionValues = new ContentValues();
        sessionValues.put("userID", userID);
        sessionValues.put("password", password);
        db.insert(SESSION_TABLE, null, sessionValues);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }


    public Cursor getTenantData(String tenantID) {
        Cursor cursor;
        if(tenantID == null) {
            cursor = getReadableDatabase().rawQuery("SELECT * from tenants", null);
        } else {
            cursor = getReadableDatabase().rawQuery("SELECT * from tenants WHERE _id = '" + tenantID + "';", null);
        }
        return cursor;
    }

    public Cursor getFlatData(String flatName) {
        Cursor cursor;
        if (flatName == null) {
            cursor = getReadableDatabase().rawQuery("SELECT * from flats", null);
        } else {
            cursor = getReadableDatabase().rawQuery("SELECT * from flats WHERE name = '" + flatName + "';", null);
        }
        return cursor;
    }

    public Cursor getFlatData(int flatId) {
        Cursor cursor;
        if (flatId == 0) {
            cursor = getReadableDatabase().rawQuery("SELECT * from flats", null);
        } else {
            cursor = getReadableDatabase().rawQuery("SELECT * from flats WHERE _id = '" + flatId + "';", null);
        }
        return cursor;
    }


    public void insertTenant(String tenantId, String tenantName, String tenantFlatSelection, String tenantContact, String tenantEmail, String tenantPassword, int tenantRent, int tenantCharges, byte[] avatarBlob) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues tenantValues = new ContentValues();
        tenantValues.put("_id", tenantId);
        tenantValues.put("name", tenantName);
        tenantValues.put("flat", tenantFlatSelection);
        tenantValues.put("contact", tenantContact);
        tenantValues.put("email", tenantEmail);
        tenantValues.put("password", tenantPassword);
        tenantValues.put("rent", tenantRent);
        tenantValues.put("charges", tenantCharges);
        tenantValues.put("image", avatarBlob);
        db.beginTransaction();
        db.insert(TENANTS_TABLE, null,tenantValues);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public void insertFlat(int flatId, String flatName, String flatAddress,String flatCity, byte[] avatarBlob) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues flatValues = new ContentValues();
        flatValues.put("_id", flatId);
        flatValues.put("name", flatName);
        flatValues.put("address", flatAddress);
        flatValues.put("city", flatCity);
        flatValues.put("image", avatarBlob);
        db.beginTransaction();
        db.insert(FLATS_TABLE, null, flatValues);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public void deleteFlat(int flatId, String flatName, String flatAddress,String flatCity, byte[] avatarBlob) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues flatValues = new ContentValues();
        flatValues.put("_id", flatId);
        flatValues.put("name", flatName);
        flatValues.put("address", flatAddress);
        flatValues.put("city", flatCity);
        flatValues.put("image", avatarBlob);
        db.beginTransaction();
        db.delete(FLATS_TABLE, String.valueOf(flatId), null);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }
    public static QueryUtility getInstance() {
        return instance;
    }

    public void deleteOneFlat(int _id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FLATS_TABLE ,"_id",
                new String[]{String.valueOf(_id)});
        db.close();
    }

    public void addNewContact(String contact, String id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT contact FROM tenants WHERE _id = " + "'" + id + "'", null);
        cursor.moveToLast();
        contact = contact + "\n" + cursor.getString(cursor.getColumnIndexOrThrow("contact"));
        db.beginTransaction();
        contentValues.put("contact", contact);
        String selection = "_id = ?";
        String[] selectionArgs = { "" + id };
        db.update(TENANTS_TABLE, contentValues, selection,
                selectionArgs);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }



    public boolean allowPassage(String id, String pass) {
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT password FROM " + TENANTS_TABLE + " WHERE _id = '" + id + "';", null);
            cursor.moveToLast();
            String truePassword = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            return pass.equals(truePassword);
        } catch(Exception e) {
            return false;
        }
    }
}
