package com.example.sqlapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class OpenDatabase extends SQLiteOpenHelper
{
    OpenDatabase sqh;
    SQLiteDatabase sqdb;

    private static final String DATABASE_NAME = "films.db";

    // TOGGLE THIS NUMBER FOR UPDATING TABLES AND DATABASE
    private static final int DATABASE_VERSION = 1;

    OpenDatabase(Context context)
    {
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
    } // OpenDatabase(Context context)

    @Override
    public void onCreate(SQLiteDatabase db)
    {


    } // public void onCreate(SQLiteDatabase db)

    @Override
    public void onUpgrade(SQLiteDatabase sqdb, int oldVersion, int newVersion)
    {


    } // public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)


    public void DisplayRecords(SQLiteDatabase sqdb)
    {
        Cursor c = sqdb.rawQuery("SELECT * FROM films", null);
        if (c != null)
        {
            if (c.moveToFirst())
            {
                do
                {
                    String id = c.getString(0);
                    String FilmTitle = c.getString(1);
                    String Year = c.getString(2);
                    String Actor = c.getString(3);
                    String Distributor = c.getString(4);
                    Log.w("FILMS_TABLE", "ID = " + id + " film title = " + FilmTitle + "Year = " + Year + "Actor = " + Actor + "Distributor = " + Distributor
                    );
                } while (c.moveToNext());
            }
        }
        c.close();
    } // public void DisplayRecords()

    public Boolean updateRecord(SQLiteDatabase sqdb, String id, String FilmTitle, String Year,
                                String Actor, String Distributor)
    {
        Boolean result = false;

        String modifyRecord = "";

        modifyRecord = "UPDATE films" +
                " SET FilmTitle = '" + FilmTitle + "'," +
                "year = '" + Year + "'," +
                "artist = '" + Actor + "'," +
                "album = '" + Distributor + "'" +
                " WHERE id = " + id + ";";

        Log.w("UPDATE","Sql = " + modifyRecord);

        sqdb.execSQL( modifyRecord );

        Log.w("UPDATED","Updated record id = " + id);

        return result;
    }   //


    public String ListAllRecordsString(SQLiteDatabase sqdb)
    {
        String str = "";

        Cursor c = sqdb.rawQuery("SELECT * FROM films", null);
        if (c != null)
        {
            if (c.moveToFirst())
            {
                do
                {
                    String id = c.getString(0);
                    str = str + id + ",";
                    String FilmTitle = c.getString(1);
                    str = str + FilmTitle + ",";
                    String Year = c.getString(2);
                    str = str + Year + ",";
                    String Actor = c.getString(3);
                    str = str + Actor + ",";
                    String Distributor = c.getString(4);
                    str = str + Distributor + "\n";
                    Log.w("FILMS_TABLE", "ID = " + id + " film title = " +
                            FilmTitle + "Year = " + Year + "Actor = " + Actor +
                            "Distributor = " + Distributor
                    );
                } while (c.moveToNext());
            }
        }
        c.close();

        return str;

    } // public void DisplayRecords()

    public ArrayList<String> ListAllRecordsArrayList(SQLiteDatabase sqdb)
    {
        ArrayList<String> list = new ArrayList<String>();

        String str = "";

        Cursor c = sqdb.rawQuery("SELECT * FROM films", null);
        if (c != null)
        {
            if (c.moveToFirst())
            {
                do
                {
                    String id = c.getString(0);
                    str = str + id + ",";
                    String FilmTitle = c.getString(1);
                    str = str + FilmTitle + ",";
                    String Year = c.getString(2);
                    str = str + Year + ",";
                    String Actor = c.getString(3);
                    str = str + Actor + ",";
                    String Distributor = c.getString(4);
                    str = str + Distributor;

                    list.add(str);

                    str = "";

                    Log.w("FILMS_TABLE", "ID = " + id + " film title = " + FilmTitle + "Year = " + Year + "Actor = " + Actor + "Distributor = " + Distributor
                    );
                } while (c.moveToNext());
            }
        }
        c.close();

        return list;

    } // public void DisplayRecords()


    public String NumberOfRecordsString(SQLiteDatabase sqdb)
    {
        String str = "";

        Cursor c = sqdb.rawQuery("SELECT COUNT(*) FROM films", null);
        if (c != null)
        {
            if (c.moveToFirst())
            {
                do
                {
                    String count = c.getString(0);
                    str = count;

                    String FilmTitle = c.getString(1);
                    str = str + FilmTitle + ",";
                    String Year = c.getString(2);
                    str = str + Year + ",";
                    String Actor = c.getString(3);
                    str = str + Actor + ",";
                    String Distributor = c.getString(4);
                    str = str + Distributor + "\n";
                    Log.w("FILMS_TABLE", "number of records = " + count);
                } while (c.moveToNext());
            }
        }
        c.close();

        return str;

    } // public void DisplayRecords()

    public String SearchByYearString(SQLiteDatabase sqdb, String searchYear)
    {
        String str = "";

        Cursor c = sqdb.rawQuery("SELECT * FROM films WHERE Year = '" + searchYear + "'", null);
        if (c != null)
        {
            if (c.moveToFirst())
            {
                do
                {
                    String id = c.getString(0);
                    str = str + id + ",";
                    String FilmTitle = c.getString(1);
                    str = str + FilmTitle + ",";
                    String Year = c.getString(2);
                    str = str + Year + ",";
                    String Actor = c.getString(3);
                    str = str + Actor + ",";
                    String Distributor = c.getString(4);
                    str = str + Distributor + "\n";
                    Log.w("FILMS_TABLE", "ID = " + id + " film title = " + FilmTitle + "Year = " + Year + "Actor = " + Actor + "Distributor = " + Distributor
                    );
                } while (c.moveToNext());
            }
            else
            {
                str = "No Records Found!";
            }
        }


        c.close();

        return str;

    } // public void DisplayRecords()




}   //  public class OpenDatabase extends SQLiteOpenHelper
