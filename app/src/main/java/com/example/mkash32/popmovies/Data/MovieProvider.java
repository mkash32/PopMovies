package com.example.mkash32.popmovies.Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class MovieProvider extends ContentProvider {

    private MovieDBHelper dbHelper;
    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static final int ALL_MOVIES = 100;
    private static final int MOVIE_GIVEN_ID = 101;
    private static final int FAVORITES = 102;

    public MovieProvider() {
    }

    private static UriMatcher buildUriMatcher(){

        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority =  MovieDBContract.CONTENT_AUTHORITY;

        //adding the uris to the matcher
        matcher.addURI(authority,MovieDBContract.PATH_MOVIE, ALL_MOVIES);
        matcher.addURI(authority,MovieDBContract.PATH_MOVIE+"/#",MOVIE_GIVEN_ID);

        matcher.addURI(authority,MovieDBContract.PATH_FAVORITES,FAVORITES);

        return matcher;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {

        int match = uriMatcher.match(uri);

        switch (match)
        {
            case ALL_MOVIES:
                return MovieDBContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_GIVEN_ID:
                return MovieDBContract.MovieEntry.CONTENT_ITEM_TYPE;
            case FAVORITES:
                return MovieDBContract.FavoritesEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String tableName = null;
        if(uriMatcher.match(uri) == MOVIE_GIVEN_ID)
            tableName = MovieDBContract.MovieEntry.TABLE_NAME;
        else
            tableName = MovieDBContract.FavoritesEntry.TABLE_NAME;

        long _id = db.insertOrThrow(tableName, null, values);
        getContext().getContentResolver().notifyChange(uri,null);
        return MovieDBContract.MovieEntry.buildMovieUri(_id);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor = null;
        switch (uriMatcher.match(uri))
        {
            case ALL_MOVIES:
                cursor = getAllMovies(selection);
                break;
            case MOVIE_GIVEN_ID:
                cursor = getMovieById(uri);
                break;
            case FAVORITES:
                cursor = getFavorites();
                break;
        }

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        int match = uriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MovieDBContract.MovieEntry.TABLE_NAME);
        int successfulStore = 0;

        db.beginTransaction();
        try {

            for (int i = 0; i < values.length; i++) {
                try {
                    long _id = db.insertOrThrow(MovieDBContract.MovieEntry.TABLE_NAME,null,values[i]);
                    if(_id != -1)
                        successfulStore++;
                }catch (SQLiteConstraintException e)
                {
                    //don't log any error if there is a conflict with the UNIQUE constraint
                }

            }
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return successfulStore;
    }

    //helper methods
    private Cursor getMovieById(Uri uri){
        String movieId = MovieDBContract.getMovieIdFromUri(uri);
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MovieDBContract.MovieEntry.TABLE_NAME);

        Cursor c = queryBuilder.query(dbHelper.getReadableDatabase(),null,MovieDBContract.MovieEntry.COLUMN_ID+" = "+movieId,null,null,null,null);
        return c;
    }

    private Cursor getAllMovies(String selection){

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MovieDBContract.MovieEntry.TABLE_NAME);

        Cursor c = queryBuilder.query(dbHelper.getReadableDatabase(),null,selection,null,null,null,null);
        return c;
    }

    private Cursor getFavorites(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " +MovieDBContract.MovieEntry.TABLE_NAME+ " m"
                +" INNER JOIN " + MovieDBContract.FavoritesEntry.TABLE_NAME+ " f"
                +" ON m."+MovieDBContract.MovieEntry.COLUMN_ID+"=f."+MovieDBContract.FavoritesEntry.COLUMN_ID;
        Cursor c = db.rawQuery(query,null);
        return c;
    }
}

