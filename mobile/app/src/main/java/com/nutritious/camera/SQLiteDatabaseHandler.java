package com.nutritious.camera;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UsersDB";
    private static final String TABLE_NAME = "Users";
    private static final String KEY_NAME = "name";
    private static final String KEY_AGE = "age";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_HEIGHT = "height";
    private static final String KEY_SEX = "sex";
    private static final String[] COLUMNS = {KEY_NAME, KEY_AGE, KEY_WEIGHT, KEY_SEX};

    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATION_TABLE = "CREATE TABLE Users ( "
                + "name TEXT, " + "age INTEGER, " + "weight DOUBLE, " + "height DOUBLE, " + "sex CHAR )";

        db.execSQL(CREATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // you can implement here migration process
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    public void removeUser(User user) {
        // Get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    public User getUser() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, // a. table
                new String[] { "*" }, // b. column names
                null, // c. selections
                null, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        User user = null;
        if (cursor != null)
            if (cursor.moveToFirst()) {
                user = new User();
                user.setName(cursor.getString(0));
                user.setAge(Integer.parseInt(cursor.getString(1)));
                user.setWeight(Double.parseDouble((cursor.getString(2))));
                user.setHeight(Double.parseDouble(cursor.getString(3)));
                user.setSex(cursor.getString(4).charAt(0));
            }

        return user;
    }

    public void addUser(User user) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getName());
        values.put(KEY_AGE, user.getAge());
        values.put(KEY_WEIGHT, user.getWeight());
        values.put(KEY_HEIGHT, user.getHeight());
        values.put(KEY_SEX, Character.toString(user.getSex()));

        SQLiteDatabase db = this.getWritableDatabase();

        if (getUser() != null) {
            // insert
            db.update(TABLE_NAME, values, null, null);
            db.close();
        } else {
            // insert
            db.insert(TABLE_NAME,null, values);
            db.close();
        }
    }
}