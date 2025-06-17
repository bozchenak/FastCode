package com.example.appcartochka;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class NotesDataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;


    private static final String TABLE_NAME_USERS = "allusers";

    public static final String COLUMN_FULLNAMEUSER = "fullnameuser";
    public static final String COLUMN_FULLNAMERUC = "fullnameruc";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_TERMS = "terms";
    public static final String COLUMN_AVATAR = "avatar";
    public static final String COLUMN_EXAMPLEPRACTICE = "example_practice";





    private static final String TABLE_NAME = "allnotes";


    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_NAME = "heading";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";

    public NotesDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " + COLUMN_ADDRESS + " TEXT, "
                    + COLUMN_USER_ID + " INTEGER, "
                    + COLUMN_DATE + " TEXT, "
                    + COLUMN_TIME + " TEXT);";

    private static final String TABLE_CREATE_USER =
            "CREATE TABLE " + TABLE_NAME_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FULLNAMEUSER + " TEXT, " + COLUMN_FULLNAMERUC + " TEXT, " + COLUMN_AVATAR + " TEXT, " + COLUMN_EXAMPLEPRACTICE + " TEXT, "
                    + COLUMN_EMAIL + " TEXT, "
                    + COLUMN_PASSWORD + " TEXT, " +
                    COLUMN_TERMS + " TEXT);";




    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL(TABLE_CREATE);
    db.execSQL(TABLE_CREATE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public void deleteNoteById(long noteId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            int deletedRows = db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                    new String[]{String.valueOf(noteId)});
            if (deletedRows > 0) {
                // Note deleted successfully
            } else {
                // Handle deletion failure
            }
        } catch (SQLException e) {
            // Handle the exception
        } finally {
            db.close();
        }
    }


    public long insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULLNAMEUSER, user.getFullName());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_AVATAR, user.getImage());
        values.put(COLUMN_PASSWORD, user.getPassword());


        long newRowId = -1;

        try {
            newRowId = db.insert(TABLE_NAME_USERS, null, values);
        } catch (SQLException e) {
            // Handle the exception
        } finally {
            db.close();
        }

        return newRowId;
    }

    public long insertNote(item Item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, Item.getName());
        values.put(COLUMN_DATE, Item.getDate());
        values.put(COLUMN_ADDRESS, Item.getAddress());
        values.put(COLUMN_TIME, Item.getTime());
        values.put(COLUMN_USER_ID, Item.getUser_id());

        long newRowId = -1;

        try {
            newRowId = db.insert(TABLE_NAME, null, values);
        } catch (SQLException e) {
            // Handle the exception
        } finally {
            db.close();
        }

        return newRowId;
    }


    public boolean updateNote(item Item,long noteId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, Item.getName());
        values.put(COLUMN_DATE, Item.getDate());
        values.put(COLUMN_ADDRESS, Item.getAddress());
        values.put(COLUMN_TIME, Item.getTime());

        int rowsAffected = -1;

        try {
            rowsAffected = db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                    new String[]{String.valueOf(noteId)});
        } catch (SQLException e) {
            // Handle the exception
        } finally {
            db.close();
        }

        return rowsAffected > 0;
    }

    public boolean updateUser(User user,long userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_FULLNAMEUSER, user.getFullName());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_AVATAR, user.getImage());

        int rowsAffected = -1;

        try {
            rowsAffected = db.update(TABLE_NAME_USERS, values, COLUMN_ID + " = ?",
                    new String[]{String.valueOf(userId)});
        } catch (SQLException e) {
            // Handle the exception
        } finally {
            db.close();
        }

        return rowsAffected > 0;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.query(
                    TABLE_NAME_USERS,
                    new String[]{COLUMN_ID, COLUMN_FULLNAMEUSER, COLUMN_FULLNAMERUC,COLUMN_EMAIL,COLUMN_PASSWORD,COLUMN_TERMS,COLUMN_AVATAR,COLUMN_EXAMPLEPRACTICE},
                    null,
                    null,
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                do {
//                    long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
                    @SuppressLint("Range") String nameuser = cursor.getString(cursor.getColumnIndex(COLUMN_FULLNAMEUSER));
                    @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                    @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
                    @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));

                    byte[] image = new byte[0];

                    User user = new User(email,nameuser,password,id,image);

                    users.add(user);
                } while (cursor.moveToNext());

                cursor.close();
            }
        } catch (SQLException e) {
            // Handle the exception
        } finally {
            db.close();
        }

        return users;
    }


    public List<item> getAllNotes(long userId) {
        List<item> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();


        try {
            Cursor cursor = db.query(
                    TABLE_NAME,
                    new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_ADDRESS,COLUMN_TIME,COLUMN_USER_ID,COLUMN_DATE},
                    null,
                    null,
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                do {
//                    long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                    @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                    @SuppressLint("Range") int user_id = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID));
                    @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS));
                    @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
                    @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));

                    item note = new item(id,name,user_id,address,date,time);

                    notes.add(note);
                } while (cursor.moveToNext());

                cursor.close();
            }
        } catch (SQLException e) {
            // Handle the exception
        } finally {
            db.close();
        }

        return notes;
    }

    public User getUserById(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        try {
            Cursor cursor = db.query(
                    TABLE_NAME_USERS,
                    new String[]{COLUMN_ID, COLUMN_FULLNAMEUSER, COLUMN_FULLNAMERUC,COLUMN_EMAIL,COLUMN_PASSWORD,COLUMN_TERMS,COLUMN_AVATAR,COLUMN_EXAMPLEPRACTICE},
                    COLUMN_ID + " = ?",
                    new String[]{String.valueOf(userId)},
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range") String nameuser = cursor.getString(cursor.getColumnIndex(COLUMN_FULLNAMEUSER));
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") String nameruc = cursor.getString(cursor.getColumnIndex(COLUMN_FULLNAMERUC));
                @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
                @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
                @SuppressLint("Range") String terms = cursor.getString(cursor.getColumnIndex(COLUMN_TERMS));
                @SuppressLint("Range") byte[] avatar = cursor.getBlob(cursor.getColumnIndex(COLUMN_AVATAR));
                @SuppressLint("Range") String examplepractice = cursor.getString(cursor.getColumnIndex(COLUMN_EXAMPLEPRACTICE));


                if(nameruc == null){
                    nameruc = "";
                }
                if(terms == null){
                    terms = "";
                }
                if(examplepractice == null){
                    examplepractice = "";
                }




                user = new User(email,nameuser,password,id,avatar);

                cursor.close();
            }
        } catch (SQLException e) {
            // Handle the exception
        } finally {
            db.close();
        }

        return user;
    }


    public item getNoteById(long noteId) {
        SQLiteDatabase db = this.getReadableDatabase();
        item note = null;

        try {
            Cursor cursor = db.query(
                    TABLE_NAME,
                    new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_ADDRESS,COLUMN_TIME,COLUMN_USER_ID,COLUMN_DATE},
                    COLUMN_ID + " = ?",
                    new String[]{String.valueOf(noteId)},
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") int user_id = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS));
                @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));

                 note = new item(id,name,user_id,address,date,time);

                cursor.close();
            }
        } catch (SQLException e) {
            // Handle the exception
        } finally {
            db.close();
        }

        return note;
    }
}
