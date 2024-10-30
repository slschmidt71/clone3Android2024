package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;

    private ArrayList<String> elements = new ArrayList<>(Arrays.asList());
    private MyListAdapter myAdapter;
    private ListView lvTasks;

    private int i;
    private View newView;
    private TextView row, old;
    private LinearLayout parent;
    private Button addButton;

    private  AlertDialog.Builder alertDialogBuilder;

    private AlertDialog.Builder builder;
    private View view1;

    private EditText input;
    private Switch listSwitch;

    private boolean isUrgent;

    private int listAdapterCount;
    private int position;

   MyDBHelper dbHelper;
   SQLiteDatabase db = dbHelper.getWritableDatabase();



    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        dbHelper.openDB();

        // setContentView loads objects onto the screen.
        // Before this function, the screen is empty.
        setContentView(R.layout.activity_main);


        String finalData = new String();

        // start of step 3, new for lab 5
        Cursor cursor = dbHelper.getAllRecords();



        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            elements.add(finalData);

        }
        printCursor(cursor);

      //  myAdapter.notifyDataSetChanged();
        // end of step 3, new for lab 5

        input = findViewById(R.id.etTypeHere);

        EditText todo = (EditText) findViewById(R.id.etTypeHere); // new,modified

        listSwitch = findViewById(R.id.swUrgent);
        isUrgent = listSwitch.isChecked();

        Button btnAdd = findViewById(R.id.btnAdd);

        // step 4 - user presses add button
        btnAdd.setOnClickListener( click -> {

            String itemText = input.getText().toString();

            elements.add(itemText);
            myAdapter.notifyDataSetChanged();

            // start of step 4 - new code for lab 5

            long result = dbHelper.insert(-1,getValue(todo),isUrgent);

            if (result == -1)
                Toast.makeText(MainActivity.this,"An error has occurred while inserting",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(MainActivity.this,"Data inserted successfully, ID: " + result,Toast.LENGTH_SHORT).show();

            // end of step 4 - new code for lab 5
        });



        ListView myList = findViewById(R.id.lvTasks);
        myList.setAdapter( myAdapter = new MyListAdapter());

        myList.setOnItemLongClickListener( (parent, view, pos, id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Do you want to delete this?")

                    //What is the message:
                    .setMessage("The selected row is: "+pos)

                    //what the Yes button does:
                    .setPositiveButton("Yes", (click, arg) -> {
                        elements.remove(pos);

                        long result = dbHelper.delete(pos);

                        if (result == -1)
                            Toast.makeText(MainActivity.this,"An error has occurred while deleting",Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(MainActivity.this,"Data deleted successfully, ID: " + result,Toast.LENGTH_SHORT).show();

                        myAdapter.notifyDataSetChanged();
                    })
                    //What the No button does:
                    .setNegativeButton("No", (click, arg) -> { })

                    //You can add extra layout elements:
                    .setView(getLayoutInflater().inflate(R.layout.row_layout, null) )

                    //Show the dialog
                    .create().show();
            return true;
        });

    }


    private String getValue(EditText editText) {
        return editText.getText().toString().trim();
    }


    @Override
    protected void onStart() {
        super.onStart();
        dbHelper.openDB();
    }

    @Override
    protected void onStop() {
        super.onStop();
        dbHelper.closeDB();
    }

    private void printCursor(Cursor c) {
        String finalData;
        Log.d("debug","Database version number: "+db.getVersion());
        Log.d("debug","Number of columns in the cursor: "+c.getColumnCount());
        Log.d("debug", "Names of the columns in the cursor: "+ Arrays.toString(c.getColumnNames()));
        Log.d("debug","Number of results in the cursor: "+c.getCount());
        Log.d("debug","Each row of results in the cursor");
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            finalData = c.getColumnName(0)+c.getColumnName(1);
            Log.d("debug","Row of results"+finalData);
        }
        Log.d("debug"," ");
    }

    private class MyListAdapter extends BaseAdapter{

        public int getCount() { return elements.size();}

        public Object getItem(int position) { return elements.get(position); }

        public long getItemId(int position) { return (long) position; }

        public View getView(int position, View old, ViewGroup parent)
        {
            ListView myList = findViewById(R.id.lvTasks);

            LayoutInflater inflater = getLayoutInflater();
            listSwitch = findViewById(R.id.swUrgent);
            isUrgent = listSwitch.isChecked();

            //make a new row:
            View newView = inflater.inflate(R.layout.row_layout, parent, false);

            //set what the text should be for this row:
            TextView tView = newView.findViewById(R.id.textGoesHere);
            tView.setText( getItem(position).toString() );

            if (isUrgent) {
                tView.setBackgroundColor(Color.RED);
                tView.setTextColor(Color.WHITE);
            }

            //return it to be put in the table
            return newView;
        }
    }

}

