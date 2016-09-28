package mahesh.demo;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mahesh.demo.db.ToDo;

public class AddTodo extends AppCompatActivity implements   TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    String dateTextView = "";

    String index = "";

    String taskId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.priority_array, R.layout.spinner_layout);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Creating adapter for spinner
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, priority);
//
//        // Drop down layout style - list view with radio button
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // attaching data adapter to spinner
//        spinner.setAdapter(dataAdapter);

        ////Status Spinner

        Spinner statusSpinner = (Spinner) findViewById(R.id.spinStatus);

        // Spinner click listener
        statusSpinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
//        List<String> status = new ArrayList<String>();
//        status.add("TO-DO");
//        status.add("DONE");

        // Creating adapter for spinner
       // ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, status);

        ArrayAdapter statusAdapter = ArrayAdapter.createFromResource(this, R.array.status_array, R.layout.spinner_layout);
        statusAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);


        // Drop down layout style - list view with radio button
        //statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        //statusSpinner.setAdapter(statusAdapter);
        Intent intent = getIntent();
        taskId = intent.getStringExtra("taskToEdit");
        index = intent.getStringExtra("index");
        ToDo taskToLoad = null;
        if(taskId != null && taskId.length() > 0)
            taskToLoad = loadTask(Integer.parseInt(taskId));

        System.out.println("TaskId & Index:: " + taskId+" " +index);
        if(taskToLoad != null) {
            populateUI(taskToLoad);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.save_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_save:
                Serializable obj = null;
                if(taskId == null) {
                    obj = saveData();
                } else {
                    obj = updateTask(taskId);
                }

                Intent data = new Intent();
                data.putExtra("SavedTask", obj);
                data.putExtra("index", index);
                setResult(RESULT_OK, data); // set result code and bundle data for response
                finish(); // closes the activity, pass data to parent
                System.out.println("**** SAVING COMPLETE RETURNING*****");
                return true;
            case R.id.action_cancel:
                finish();
                Toast.makeText(this, "Task canceled", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onTimeBtnClick(View v) {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                AddTodo.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.d("TimePicker", "Dialog was cancelled");
            }
        });
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    public void onDateBtnClick(View v) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog tpd = DatePickerDialog.newInstance(
                AddTodo.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.d("TimePicker", "Dialog was cancelled");
            }
        });
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String secondString = second < 10 ? "0"+second : ""+second;
        String time = hourString+":"+minuteString+":"+secondString;

        EditText editTimeText = (EditText) findViewById(R.id.editTimeText);
        editTimeText.setText(time);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = (++monthOfYear)+"/" +dayOfMonth+"/"+year;
        EditText editDateText = (EditText) findViewById(R.id.editDateText);
        editDateText.setText(date);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        System.out.println("Item Selected::"+item);
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

    private Serializable saveData() {
        ToDo todo = new ToDo();
        todo.taskId = (int)(Math.random()*1000);
        System.out.println("TaskId Generated::"+ todo.taskId);
        todo.taskName = ((EditText)findViewById(R.id.editTastNameText)).getText().toString();
        todo.taskDescription = ((EditText)findViewById(R.id.editDescText)).getText().toString();

        String taskDueDate = ((EditText)findViewById(R.id.editDateText)).getText().toString();
        String taskDueTime = ((EditText)findViewById(R.id.editTimeText)).getText().toString();

        try {
            Date date = new SimpleDateFormat("mm/dd/yyyy HH:mm:ss").parse(taskDueDate+" "+taskDueTime);
            todo.taskDueDate = date;
        }catch(Exception e) {
            e.printStackTrace();
            todo.taskDueDate = new Date();
        }
        todo.priority = ((Spinner)findViewById(R.id.spinner)).getSelectedItem().toString();
        todo.status = ((Spinner)findViewById(R.id.spinStatus)).getSelectedItem().toString();
        todo.save();
        return todo;
    }

    private Serializable updateTask(String taskId) {

        ToDo todo = loadTask(Integer.parseInt(taskId));
        todo.taskName = ((EditText)findViewById(R.id.editTastNameText)).getText().toString();
        todo.taskDescription = ((EditText)findViewById(R.id.editDescText)).getText().toString();

        String taskDueDate = ((EditText)findViewById(R.id.editDateText)).getText().toString();
        String taskDueTime = ((EditText)findViewById(R.id.editTimeText)).getText().toString();

        try {
            Date date = new SimpleDateFormat("mm/dd/yyyy HH:mm:ss").parse(taskDueDate+" "+taskDueTime);
            todo.taskDueDate = date;
        }catch(Exception e) {
            e.printStackTrace();
            todo.taskDueDate = new Date();
        }
        todo.priority = ((Spinner)findViewById(R.id.spinner)).getSelectedItem().toString();
        todo.status = ((Spinner)findViewById(R.id.spinStatus)).getSelectedItem().toString();
        todo.save();
        return todo;
    }

    private ToDo loadTask(int taskId) {
        List<ToDo> tasks = new Select()
                .from(ToDo.class).where("taskId=?", taskId).execute();
        if(tasks.size() > 0)
            return tasks.get(0);
        else
            return null;
    }

    private void populateUI(ToDo task) {
//        todo.taskId = (int)Math.random()*1000;
        ((EditText)findViewById(R.id.editTastNameText)).setText(task.taskName);
        ((EditText)findViewById(R.id.editDescText)).setText(task.taskDescription);
        ((EditText)findViewById(R.id.editDateText)).setText(task.taskDueDate.getMonth() +"/"+ task.taskDueDate.getDate() +"/"+ task.taskDueDate.getYear());
        ((EditText)findViewById(R.id.editTimeText)).setText(task.taskDueDate.getHours() +":"+ task.taskDueDate.getMinutes() +":"+ task.taskDueDate.getSeconds());
        ((Spinner)findViewById(R.id.spinner)).setSelection(task.priority.equalsIgnoreCase("HIGH")? 0 : (task.priority.equalsIgnoreCase("MEDIUM") ? 1 : 2));
        ((Spinner)findViewById(R.id.spinStatus)).setSelection(task.status.equalsIgnoreCase("TO-DO")? 0 : 1);

    }
}
