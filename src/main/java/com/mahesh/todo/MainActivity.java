package com.mahesh.todo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.R.layout;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lstView;
    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lstView = (ListView) findViewById(R.id.lstItems);

        readFile();
        itemsAdapter = new ArrayAdapter<String>(this, layout.simple_list_item_1 , items);
        lstView.setAdapter(itemsAdapter);

        setUpListViewListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String name = data.getExtras().getString("editedText");
            int index = Integer.parseInt(data.getExtras().getString("index"));
            items.set(index, name);
            itemsAdapter.notifyDataSetChanged();
            writeFile();

            Toast.makeText(this, "Item Saved", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpListViewListener() {

        final Intent intent = new Intent(this, DisplayMessageActivity.class);
        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                intent.putExtra("textToEdit", items.get(pos));
                intent.putExtra("index", ""+pos);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        lstView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                writeFile();
                return true;
            }
        });
    }

    public void onAddItemClick(View v) {
        EditText txtView = (EditText) findViewById(R.id.txtAddItem);
        String text = txtView.getText().toString();
        itemsAdapter.add(text);
        writeFile();
        txtView.setText("");
    }

    private void readFile() {
        try {
            File filesDir = getFilesDir();
            File file = new File(filesDir, "todo.txt");
            items = new ArrayList<String>(FileUtils.readLines(file));
        }catch(Exception e) {
            items = new ArrayList<String>();
        }

    }

    private void writeFile() {
        try {
            File filesDir = getFilesDir();
            File file = new File(filesDir, "todo.txt");
            FileUtils.writeLines(file, items);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
