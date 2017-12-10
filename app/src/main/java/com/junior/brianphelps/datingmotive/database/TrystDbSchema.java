package com.junior.brianphelps.datingmotive.database;

/**
 * Created by brianphelps on 12/3/17.
 */

public class TrystDbSchema {
    public static final class TrystTable {
        public static final String NAME = "trysts";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String TAKEN = "taken";
            public static final String FRIEND = "friend";
        }
    }
}
