package com.junior.brianphelps.datingmotive.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.junior.brianphelps.datingmotive.database.TrystDbSchema.TrystTable;

/**
 * Created by brianphelps on 12/3/17.
 */

public class TrystBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "trystBase.db";

    public TrystBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TrystTable.NAME + "(" +
            " _id integer primary key autoincrement, " +
            TrystTable.Cols.UUID + ", " +
            TrystTable.Cols.TITLE + ", " +
            TrystTable.Cols.DATE + ", " +
            TrystTable.Cols.TAKEN + ", " +
            TrystTable.Cols.FRIEND +
            ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
