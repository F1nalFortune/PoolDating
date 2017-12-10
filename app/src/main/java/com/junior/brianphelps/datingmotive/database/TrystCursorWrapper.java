package com.junior.brianphelps.datingmotive.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.junior.brianphelps.datingmotive.Tryst;
import com.junior.brianphelps.datingmotive.database.TrystDbSchema.TrystTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by brianphelps on 12/3/17.
 */

public class TrystCursorWrapper extends CursorWrapper {
    public TrystCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Tryst getTryst() {
        String uuidString = getString(getColumnIndex(TrystTable.Cols.UUID));
        String title = getString(getColumnIndex(TrystTable.Cols.TITLE));
        long date = getLong(getColumnIndex(TrystTable.Cols.DATE));
        int isTaken = getInt(getColumnIndex(TrystTable.Cols.TAKEN));
        String friend = getString(getColumnIndex(TrystTable.Cols.FRIEND));

        Tryst tryst = new Tryst(UUID.fromString(uuidString));
        tryst.setTitle(title);
        tryst.setDate(new Date(date));
        tryst.setTaken(isTaken != 0);
        tryst.setFriend(friend);

        return tryst;
    }
}
