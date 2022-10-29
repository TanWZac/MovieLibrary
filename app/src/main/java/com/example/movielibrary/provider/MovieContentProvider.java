package com.example.movielibrary.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MovieContentProvider extends ContentProvider {

    public static final String CONTENT_AUTHORITY = "fit2081.app.Wen";

    public static final Uri CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final int MULTIPLE_MOVIE = 1;
    private static final int PARTICULAR_MOVIE = 2;
    private static final int BIG_BUDGET = 3;

    MovieDatabase db;
    private static final UriMatcher sUriMatcher = createUriMatcher();

    private static UriMatcher createUriMatcher() {

        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, Movie.TABLE_NAME, MULTIPLE_MOVIE);

        uriMatcher.addURI(authority, Movie.TABLE_NAME + "/#", PARTICULAR_MOVIE);

        uriMatcher.addURI(authority, Movie.TABLE_NAME + "/*", BIG_BUDGET);

        return uriMatcher;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deletionCount;

        deletionCount = db.getOpenHelper().getWritableDatabase()
                .delete(Movie.TABLE_NAME, selection, selectionArgs);

        return deletionCount;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long rowId = db.getOpenHelper().getWritableDatabase().insert(Movie.TABLE_NAME, 0, values);

        return ContentUris.withAppendedId(CONTENT_URI, rowId);
    }

    @Override
    public boolean onCreate() {
        db = MovieDatabase.getDatabase(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(Movie.TABLE_NAME);
        String query = builder.buildQuery(projection, selection, null, null, sortOrder, null);

        final Cursor cursor = db.getOpenHelper().getReadableDatabase().query(query, selectionArgs);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int updateCount;
        updateCount = db.getOpenHelper().getWritableDatabase()
                .update(Movie.TABLE_NAME, 0, values, selection, selectionArgs);

        return updateCount;
    }
}