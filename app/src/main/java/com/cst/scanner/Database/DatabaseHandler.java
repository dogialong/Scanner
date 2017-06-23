package com.cst.scanner.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cst.scanner.Model.FileObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by longdg on 05/05/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String NAME_DB = "IMAGE_SCANNED";
    private static final String NAME_TABLE = "image";

    private static final String KEY_ID = "id";
    private static final String KEY_PATH ="path";
    private static final String KEY_NAME = "name";
    private static final String KEY_DATE = "date";
    private static final String KEY_STATUS = "status";
    private static final String KEY_ARRAY_IMAGE = "image";


    public DatabaseHandler(Context context) {
        super(context, NAME_DB, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + NAME_TABLE + " (id integer primary key autoincrement, path text, name text, date text, status text, image text);";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + NAME_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void addImage(FileObject object) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PATH,object.getPathFile());
        values.put(KEY_NAME,object.getNameFile());
        values.put(KEY_DATE,object.getDateFile());
        values.put(KEY_STATUS,object.getStatus());
        values.put(KEY_ARRAY_IMAGE,object.getImage());
        db.insert(NAME_TABLE,null,values);
        Log.d("Database", "addImage: " +  db.getPath());
        db.close();
    }

    public FileObject getObject(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(NAME_TABLE, new String[]{KEY_PATH,KEY_NAME,KEY_DATE,KEY_STATUS},KEY_NAME + "=?",
                new String[]{name},null,null,null,null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        FileObject object = new FileObject(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
        return object;
    }
    public List<FileObject> getListObject() {
        List<FileObject> fileObjects = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query =  ("Select * from " + NAME_TABLE);
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()) {
            do {
                FileObject object  = new FileObject(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5));
                object.setId(cursor.getInt(0));
                fileObjects.add(object);
            }
            while (cursor.moveToNext());
        }
        return fileObjects;
    }

    public int updateObject (FileObject object) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID,object.getId());
        values.put(KEY_NAME,object.getNameFile());
        values.put(KEY_PATH,object.getPathFile());
        values.put(KEY_DATE,object.getDateFile());
        values.put(KEY_STATUS,object.getStatus());
        return db.update(NAME_TABLE,values,KEY_ID + "=?" , new String[] {String.valueOf(object.getId())});

    }

    public void deleteObject(FileObject contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NAME_TABLE, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getId()) });
        db.close();
    }

}
