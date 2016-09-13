package com.mahesh.todo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class DisplayMessageActivity extends AppCompatActivity {

    String index = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent intent = getIntent();
        String message = intent.getStringExtra("textToEdit");
        index = intent.getStringExtra("index");
        EditText editTxt = (EditText) findViewById(R.id.editText);
        editTxt.setText(message);

    }

    public void onSaveClick(View v) {
        EditText txtView = (EditText) findViewById(R.id.editText);

        Intent data = new Intent();
        // Pass relevant data back as a result
        data.putExtra("editedText", txtView.getText().toString());
        data.putExtra("index", index); // ints work too
        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }
}
