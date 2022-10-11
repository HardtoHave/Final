package com.example.afinal.db;

import android.provider.BaseColumns;

public class DBContract {
    public static final String DB_CREATE=
            "CREATE TABLE "+ToDoNote.TABLE_NAME
            +"("+ToDoNote.COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +ToDoNote.COLUMN_TIME+" TEXT, "
            +ToDoNote.COLUMN_DEADLINE+ " TEXT, "
            +ToDoNote.COLUMN_STATE+ " TEXT, "
            +ToDoNote.COLUMN_SCHEDULED+" TEXT, "
            +ToDoNote.COLUMN_CAPTION+" TEXT)";
    public static class ToDoNote implements BaseColumns{
        public static final String TABLE_NAME="TimeManagement";
        public static final String COLUMN_ID="_id";
        public static final String COLUMN_DEADLINE="deadline";
        public static final String COLUMN_SCHEDULED="scheduled";
        public static final String COLUMN_STATE="state";
        public static final String COLUMN_TIME="time";
        public static final String COLUMN_CAPTION="caption";
    }
}
