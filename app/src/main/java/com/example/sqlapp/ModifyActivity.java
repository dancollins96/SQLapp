package com.example.sqlapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class ModifyActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String DATABASE_PATH =
            "/data/data/com.example.sqlappremastered/databases/";
    private static final String DATABASE_PATH2 =
            "/data/data/com.example.sqlappremastered/databases"; // no / at end of path !!!
    private static final String DATABASE_NAME = "films";
    private static final String LOG_TAG = "FILMS_DB";
    Context ctx;

    OpenDatabase sqh;
    SQLiteDatabase sqdb;

    EditText searchEdit, filmTitleEdit, yearEdit, actorEdit, distributorEdit;
    Button selectButton, saveButton, updateButton, selectAllButton, deleteButton;
    String search, filmTitle, year, actor, distributor;

    Button displayAllRecordsButton;
    Button modifyDatabaseButton;
    EditText databaseView;
    Button aboutButton;
    Button helpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupDatabase();
        InitDataBase();

        sqdb=openOrCreateDatabase("films", Context.MODE_PRIVATE, null);
        sqdb.execSQL("CREATE TABLE IF NOT EXISTS NewFilmographyDetails(id INTEGER PRIMARY KEY AUTOINCREMENT, FilmTitle VARCHAR(255), Year VARCHAR(255), Actor VARCHAR(255), Distributor VARCHAR(255));");

        searchEdit=(EditText)findViewById(R.id.searchEdit);
        filmTitleEdit=(EditText)findViewById(R.id.filmTitleEdit);
        yearEdit=(EditText)findViewById(R.id.yearEdit);
        actorEdit=(EditText)findViewById(R.id.actorEdit);
        distributorEdit=(EditText)findViewById(R.id.distributorEdit);
        selectButton=(Button)findViewById(R.id.selectButton);
        saveButton=(Button)findViewById(R.id.saveButton);
        updateButton=(Button)findViewById(R.id.updateButton);
        selectAllButton=(Button)findViewById(R.id.selectAllButton);
        deleteButton=(Button)findViewById(R.id.deleteButton);
        selectButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        updateButton.setOnClickListener(this);
        selectAllButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

        setupControls();

    }

    public void InitDataBase()
    {
        // Init the SQLite Helper Class
        sqh = new OpenDatabase(this);

        // RETRIEVE A READABLE AND WRITEABLE DATABASE
        sqdb = sqh.getWritableDatabase();

    } // public void InitDataBase()// {


    public void setupDatabase()
    {
        ctx = this.getBaseContext();
        try
        {
            CopyDataBaseFromAsset();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    } // public void setUpDatabase()

    public void CopyDataBaseFromAsset() throws IOException
    {
        // Get the sqlite databse in the assets folder
        InputStream in = ctx.getAssets().open(DATABASE_NAME);
        Log.w(LOG_TAG, "Starting copying...");
        String outputFileName = DATABASE_PATH + DATABASE_NAME;
        File databaseFolder = new File(DATABASE_PATH2);

        // databases folder exists ? No - Create it and copy !!!
        if (!databaseFolder.exists()) {
            databaseFolder.mkdir();
            OutputStream out = new FileOutputStream(outputFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            } // while ( (length = in.read(buffer)) > 0 )
            out.flush();
            out.close();
            in.close();
            Log.w(LOG_TAG, "Completed.");

        } // if ( !databaseFolder.exists() )
    }

    public void setupControls()
    {
        displayAllRecordsButton = findViewById(R.id.displayAllRecordsButton);
        displayAllRecordsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                databaseView.setText(sqh.ListAllRecordsString(sqdb));

            }
        });

    }


    @Override
    public void onClick(View v)
    {
        if (v.getId()==R.id.saveButton)
        {
            filmTitle = filmTitleEdit.getText().toString().trim();
            year = yearEdit.getText().toString().trim();
            actor = actorEdit.getText().toString().trim();
            distributor = distributorEdit.getText().toString().trim();

            if(filmTitle.equals("")|| year.equals("")|| actor.equals("")|| distributor.equals(""))
            {
                Toast.makeText(this, "Fields cannot be empty...", Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                sqdb.execSQL("Insert Into films(FilmTitle, Year, Actor, Distributor) VALUES('" + filmTitle + "','" + year + "','" + actor + "','" + distributor + "');");
                Toast.makeText(this, "Record Saved", Toast.LENGTH_SHORT).show();
            }
        }
        else if (v.getId()==R.id.selectAllButton)
        {
            Cursor c=sqdb.rawQuery("Select * From films", null);
            if (c.getCount()==0)
            {
                Toast.makeText(this, "Database is empty", Toast.LENGTH_SHORT).show();
                return;
            }
            StringBuffer buffer =new StringBuffer();
            while (c.moveToNext())
            {
                buffer.append("Film Title: "+c.getString(1)+"\n");
                buffer.append("Year: "+c.getString(2)+"\n");
                buffer.append("Actor: "+c.getString(3)+"\n");
                buffer.append("Distributor: "+c.getString(4)+"\n");
            }
            Toast.makeText(this, buffer.toString() , Toast.LENGTH_SHORT).show();
        }
        else if (v.getId()==R.id.selectButton)
        {
            search = searchEdit.getText().toString().trim();
            if (search.equals(""))
            {
                Toast.makeText(this, "Enter Film Title", Toast.LENGTH_SHORT).show();
                return;
            }

            Cursor c = sqdb.rawQuery("Select * From films Where FilmTitle ='" + search + "'", null);
            if (c.moveToFirst())
            {
                filmTitleEdit.setText(c.getString(1));
                yearEdit.setText(c.getString(2));
                actorEdit.setText(c.getString(3));
                distributorEdit.setText(c.getString(4));
            }
            else
            {
                Toast.makeText(this, "Data not found", Toast.LENGTH_SHORT).show();
            }
        }
        else if (v.getId()==R.id.updateButton)
        {
            search=searchEdit.getText().toString().trim();
            filmTitle=filmTitleEdit.getText().toString().trim();
            year=yearEdit.getText().toString().trim();
            actor=actorEdit.getText().toString().trim();

            if (search.equals(""))
            {
                Toast.makeText(this, "Please enter Film Title to update", Toast.LENGTH_SHORT).show();
                return;
            }

            Cursor cursorupdate=sqdb.rawQuery("Select * From films Where FilmTitle='"+ search + "'", null);
            if (cursorupdate.moveToFirst())
            {
                if (filmTitle.equals("") || year.equals("") || actor.equals("") || distributor.equals("")) {
                    Toast.makeText(this, "Please enter text, fields can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    sqdb.execSQL("Update films Set FilmTitle='" + filmTitle + "', Year='" + year + "', Actor='" + actor + "', Distributor='" + distributor + "' Where filmTitle = '" + search + "'");
                    Toast.makeText(this, "Record Edited Successfully", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(this,"Data Not Found", Toast.LENGTH_SHORT).show();
            }
        }
        else if (v.getId()==R.id.deleteButton)
        {
            search=searchEdit.getText().toString().trim();
            if (search.equals(""))
            {
                Toast.makeText(this, "Please enter Film Title to delete" , Toast.LENGTH_SHORT).show();
                return;
            }
            Cursor cursordel=sqdb.rawQuery("Select * films Where FilmTitle ='" + search + "'", null);
            if (cursordel.moveToFirst())
            {
                sqdb.execSQL("Delete From films Where FilmTitle ='" + search + "'");
                Toast.makeText(this, "Record has been deleted" , Toast.LENGTH_SHORT).show();
            }

            else {
                Toast.makeText(this, "Data not found", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
