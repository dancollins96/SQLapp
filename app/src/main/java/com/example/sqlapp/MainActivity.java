package com.example.sqlapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity
{
    private static final String DATABASE_PATH =
            "/data/data/com.example.sqlapp/databases/";
    private static final String DATABASE_PATH2 =
            "/data/data/com.example.sqlapp/databases"; // no / at end of path !!!
    private static final String DATABASE_NAME = "films.db";
    private static final String LOG_TAG = "FILMS_DB";
    Context ctx;

    OpenDatabase sqh;

    SQLiteDatabase sqdb;

    Button list_button;
    Button helpButton;
    Button aboutButton;
    Button list_records_button;
    Button modifyDatabase;

    TextView listRecordsTextView;

    ListView listview;
    ArrayList<String> list;
    ArrayAdapter adapter;

    String[] values = new String[] { "Bad Boys II", "Superbad",
            "Drillbit Taylor","Pineapple Express", "Funny People", "5050",
            "The Watch", "The Disaster Artist","The Interview", "Neighbors", "Blockers" };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpDatabase();
        InitDataBase();

        setupControls();
        DisplayRecords();

    }   //  protected void onCreate(Bundle savedInstanceState)

    public void InitDataBase()
    {
        // Init the SQLite Helper Class
        sqh = new OpenDatabase(this);

        // RETRIEVE A READABLE AND WRITEABLE DATABASE
        sqdb = sqh.getWritableDatabase();

    } // public void InitDataBase()// {

    public void setupControls()
    {
        listview = (ListView) findViewById(R.id.listview);
        // Construct the list and then populate this ArrayList with
        // the OS names from the Array of String called values.
        list = new ArrayList<String>();

        list.addAll(Arrays.asList(values));
        // Construct an ArrayAdapter,
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        // Link the ArrayAdapter to the listview
        listview.setAdapter(adapter);
        // Create a custom on Item Click Listener to react when the user chooses an item.
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position,
                                    long id)
            {
                // Get the chosen item store it in a string
                String item = (String) parent.getItemAtPosition(position);
                // Use the Toast message to display the chosen item.
                Toast.makeText(getApplicationContext(), "Item = " + item,
                        Toast.LENGTH_SHORT).show();
            }
        });

        list_button = findViewById(R.id.list_button);
        list_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getBaseContext(), ListActivity.class);
                startActivity(intent);
            }
        });

        helpButton = findViewById(R.id.helpButton);
        helpButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this, Help.class));
            }
        });

        modifyDatabase = findViewById(R.id.modifyDatabase);
        modifyDatabase.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this, ModifyActivity.class));
            }
        });

        list_records_button = findViewById(R.id.list_records_button);
        list_records_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listRecordsTextView.setText(sqh.ListAllRecordsString(sqdb));
            }
        });

        aboutButton = findViewById(R.id.aboutButton);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MainActivity.this, About.class));
            }
        });
    }   //   public void setupControls()


    public void DisplayRecords()
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


    public void setUpDatabase()
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
        Log.w( LOG_TAG , "Starting copying...");
        String outputFileName = DATABASE_PATH + DATABASE_NAME;
        File databaseFolder = new File( DATABASE_PATH2 );

        // databases folder exists ? No - Create it and copy !!!
        if ( !databaseFolder.exists() )
        {
            databaseFolder.mkdir();
            OutputStream out = new FileOutputStream(outputFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ( (length = in.read(buffer)) > 0 )
            {
                out.write(buffer,0,length);
            } // while ( (length = in.read(buffer)) > 0 )
            out.flush();
            out.close();
            in.close();
            Log.w(LOG_TAG, "Completed.");

        } // if ( !databaseFolder.exists() )

    } // public void CopyDataBaseFromAsset() throws IOException



}   //  public class MainActivity extends AppCompatActivity
