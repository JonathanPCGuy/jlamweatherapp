package com.hp.jlam.practice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by lamjon on 2/13/14.
 */
public class WeatherAppStorage extends SQLiteOpenHelper
{
    // was 2, now will be 3
    // need location id to accurately get 5 & 7-day forecasts
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "weather_app_db";

    private static final String LOCATION_TABLE = "locations";

    private static final String ID_COLUMN = "id";
    private static final String LOCATION_COLUMN = "location";
    private static final String COUNTRY_COLUMN = "country";
    private static final String LOCATION_ID_COLUMN = "location_id";

    private static final String LOCATION_TABLE_CREATE =
            "CREATE TABLE " + LOCATION_TABLE + "("
                + ID_COLUMN + " INTEGER PRIMARY KEY,"
                + LOCATION_COLUMN + " TEXT,"
                + COUNTRY_COLUMN + " TEXT,"
                + LOCATION_ID_COLUMN + " INTEGER)";



    // auto-increment field, location, country

    public WeatherAppStorage(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.d("onCreate (database)", "Database being created.");
        db.execSQL(LOCATION_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // nothing to implement yet since we don't need to migrate
        // if error will drop table and start over till we get something stable
        // db versions...

        // for now always drop
        Log.d("onUpgrade (database)", "Database being upgraded.");
        db.execSQL("DROP TABLE IF EXISTS " + LOCATION_TABLE);
        onCreate(db);
    }

    // crud time

    public long addWeatherLocation(WeatherLocation weatherLocation)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(this.LOCATION_COLUMN, weatherLocation.getLocation());
        contentValues.put(this.COUNTRY_COLUMN, weatherLocation.getCountry());
        contentValues.put(this.LOCATION_ID_COLUMN, weatherLocation.getLocation_id());
        long result = db.insert(this.LOCATION_TABLE, null, contentValues);
        db.close();
        return result;
    }

    public WeatherLocation getWeatherLocation(long id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(this.LOCATION_TABLE, new String []
                { ID_COLUMN, LOCATION_COLUMN, COUNTRY_COLUMN, LOCATION_ID_COLUMN},
                ID_COLUMN + "=?",
                new String[] {String.valueOf(id)}, null, null, null, null);
        // what does the above mean

        if(cursor != null)
        {
            cursor.moveToFirst();
        }

        WeatherLocation WeatherLocation = new WeatherLocation(
                cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));

        return WeatherLocation;
    }

    public ArrayList<WeatherLocation> getAllWeatherLocations()
    {
        ArrayList<WeatherLocation> WeatherLocationList = new ArrayList<WeatherLocation>();
        // Select all query

        String selectQuery = "SELECT * FROM " + LOCATION_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // i wonder any way to NOT use raw query? SQL injection?
        // look at later
        // loop through results
        if(cursor.moveToFirst())
        {
            do
            {
                WeatherLocation weatherLocation = new WeatherLocation();

                // why get string?
                weatherLocation.setId(Integer.parseInt(cursor.getString(0)));
                weatherLocation.setLocation(cursor.getString(1));
                weatherLocation.setCountry(cursor.getString(2));
                weatherLocation.setLocation_id(Integer.parseInt(cursor.getString(3)));

                WeatherLocationList.add(weatherLocation);

            }while (cursor.moveToNext());
        }

        return WeatherLocationList;
    }

    public int getWeatherLocationCount()
    {
        String countQuery = "SELECT * FROM " + LOCATION_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return  cursor.getCount();
    }

    public int updateWeatherLocation(WeatherLocation WeatherLocation)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        // anything like Entity, rails framework, etc. to simplify this?
        ContentValues values = new ContentValues();
        values.put(LOCATION_COLUMN, WeatherLocation.getLocation());
        values.put(COUNTRY_COLUMN, WeatherLocation.getCountry());
        values.put(LOCATION_ID_COLUMN, WeatherLocation.getLocation_id());

        // update row
        return db.update(LOCATION_TABLE, values, ID_COLUMN + " = ?",
                new String[]{String.valueOf(WeatherLocation.getId())});
    }

    public void deleteWeatherLocation(WeatherLocation WeatherLocation)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(LOCATION_TABLE, ID_COLUMN + " = ?",
                new String[] {String.valueOf(WeatherLocation.getId())});
        db.close();
    }
}
