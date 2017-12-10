package com.junior.brianphelps.datingmotive;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.junior.brianphelps.datingmotive.database.TrystBaseHelper;
import com.junior.brianphelps.datingmotive.database.TrystCursorWrapper;
import com.junior.brianphelps.datingmotive.database.TrystDbSchema;
import com.junior.brianphelps.datingmotive.database.TrystDbSchema.TrystTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by brianphelps on 12/1/17.
 */

public class TrystList {
    private static TrystList sTrystList;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static TrystList get(Context context) {
        if (sTrystList == null) {
            sTrystList = new TrystList(context);
        }
        return sTrystList;
    }

    private TrystList(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new TrystBaseHelper(mContext)
                .getWritableDatabase();
    }

    public void addTryst(Tryst t) {
        ContentValues values = getContentValues(t);

        mDatabase.insert(TrystTable.NAME, null, values);
    }

    public List<Tryst> getTrysts() {
        List<Tryst> trysts = new ArrayList<>();

        TrystCursorWrapper cursor = queryTrysts(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                trysts.add(cursor.getTryst());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return trysts;
    }

    public Tryst getTryst(UUID id) {
        TrystCursorWrapper cursor = queryTrysts(
                TrystTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getTryst();
        } finally {
            cursor.close();
        }
    }

    public File getPhotoFile(Tryst tryst) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, tryst.getPhotoFilename());
    }

    public void updateTryst(Tryst tryst) {
        String uuidString = tryst.getId().toString();
        ContentValues values = getContentValues(tryst);

        mDatabase.update(TrystTable.NAME, values,
                TrystTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    private TrystCursorWrapper queryTrysts(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                TrystTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new TrystCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Tryst tryst) {
        ContentValues values = new ContentValues();
        values.put(TrystTable.Cols.UUID, tryst.getId().toString());
        values.put(TrystTable.Cols.TITLE, tryst.getTitle());
        values.put(TrystTable.Cols.DATE, tryst.getDate().getTime());
        values.put(TrystTable.Cols.TAKEN, tryst.isTaken() ? 1 : 0);
        values.put(TrystTable.Cols.FRIEND, tryst.getFriend());

        return values;
    }
}
