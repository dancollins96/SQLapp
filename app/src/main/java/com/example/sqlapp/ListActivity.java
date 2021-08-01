package com.example.sqlapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity
{
    EditText searchEditText;
    Button list_records_button;
    Button search_records_button;
    TextView listRecordsTextView;

    OpenDatabase sqh;
    SQLiteDatabase sqdb;

    ListView listview;

    ArrayList<String> list;
    ArrayAdapter adapter;

    String[] values = new String[] { "Android", "iPhone",
            "WindowsMobile","Blackberry", "WebOS", "Ubuntu",
            "Windows7", "Max OS X","Linux", "OS/2", "Ubuntu" };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        InitDataBase();

        setupControls();

        //listTextView.setText( sqh.ListAllRecordsString( sqdb ) );

        listRecordsTextView.setText( "number of records = " + sqh.NumberOfRecordsString( sqdb ) );

    }   //   protected void onCreate(Bundle savedInstanceState)

    public void InitDataBase()
    {
        // Init the SQLite Helper Class
        sqh = new OpenDatabase(this);

        // RETRIEVE A READABLE AND WRITEABLE DATABASE
        sqdb = sqh.getWritableDatabase();

    } // public void InitDataBase() {

    public void setupControls()
    {
        searchEditText = findViewById(R.id.searchEditText);

        listRecordsTextView = findViewById(R.id.listRecordsTextView);

        list_records_button = findViewById(R.id.list_records_button);
        list_records_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                listRecordsTextView.setText( sqh.ListAllRecordsString(sqdb));
            }
        });

        search_records_button = findViewById(R.id.search_records_button);
        search_records_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                listRecordsTextView.setText( sqh.SearchByYearString(sqdb, searchEditText.getText().toString())   );
            }
        });


        listview = findViewById(R.id.listview);

        // Construct the list and then populate this ArrayList with
        // the OS names from the Array of String called values.
        list = new ArrayList<String>();

        list.addAll( sqh.ListAllRecordsArrayList( sqdb )  );

        for (int i = 0; i < values.length; ++i)
        {
            list.add(values[i]);
        }

        // Construct an ArrayAdapter,
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);

        // Link the ArrayAdapter to the listview
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l)
            {
                // Get the chosen item store it in a string
                String item = (String) parent.getItemAtPosition(position);

                // Use the Toast message to display the choosen item.
                Toast.makeText(getApplicationContext(), "Item = " + item,
                      Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getBaseContext(), ModifyActivity.class);
                intent.putExtra("id", item);
                startActivity(intent);

            }
        });

    }   //  public void setupControls()


}   //  public class ListActivity extends AppCompatActivity
