package com.example.mkash32.popmovies.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mkash32 on 10/1/16.
 */
public class MovieDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie.db";

    private static final int DATABASE_VERSION = 1;

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_MOVIES_TABLE = "CREATE TABLE " + MovieDBContract.MovieEntry.TABLE_NAME + "("
                + MovieDBContract.MovieEntry.COLUMN_ID + " INTEGER PRIMARY KEY, "
                + MovieDBContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + MovieDBContract.MovieEntry.COLUMN_IMAGE + " TEXT NOT NULL, "
                + MovieDBContract.MovieEntry.COLUMN_POSTER + " TEXT NOT NULL, "
                + MovieDBContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT, "
                + MovieDBContract.MovieEntry.COLUMN_OVERVIEW + " TEXT, "
                + MovieDBContract.MovieEntry.COLUMN_RUNTIME + " INTEGER, "
                + MovieDBContract.MovieEntry.COLUMN_VOTE + " FLOAT, "
                + MovieDBContract.MovieEntry.COLUMN_POPULARITY + " FLOAT, "
                + MovieDBContract.MovieEntry.COLUMN_SORTPOP + " INTEGER "
                +")";
        sqLiteDatabase.execSQL(CREATE_MOVIES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
