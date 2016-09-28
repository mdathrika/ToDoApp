package mahesh.demo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mahesh.demo.db.ToDo;

public class MainActivity extends AppCompatActivity {

    //List<String> items;
    //ArrayAdapter<String> itemsAdapter;
    List<Map<String, String>> items;
    MultiColumnAdapter multiColumnAdapter;
    ListView lstView;
    private final int NEW_TASK = 20;
    private final int EDIT_TASK = 21;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lstView = (ListView) findViewById(R.id.lvToDoList);

        items = readTasks();
        multiColumnAdapter = new MultiColumnAdapter(this, items);
        //itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , items);
        lstView.setAdapter(multiColumnAdapter);

        setUpListViewListener();
    }

    public void onClick(View v) {
        Intent intent = new Intent(this, AddTodo.class);
        //startActivity(intent);
        startActivityForResult(intent, NEW_TASK);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.share_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("****AFTER SAVING*****"+resultCode);
        if (resultCode == RESULT_OK) {

            if(requestCode == NEW_TASK) {

                ToDo task = (ToDo)(data.getExtras().getSerializable("SavedTask"));
                System.out.println("****IF**AFTER SAVING*****"+task.taskName);
                //int index = Integer.parseInt(data.getExtras().getString("index"));
                addToTasks(task);
                //items.add(task.taskName);
                multiColumnAdapter.notifyDataSetChanged();

                Toast.makeText(this, "Task Added", Toast.LENGTH_SHORT).show();
            }

            if(requestCode == EDIT_TASK) {

                ToDo task = (ToDo)(data.getExtras().getSerializable("SavedTask"));
                int index = Integer.parseInt(data.getStringExtra("index"));
                updateTasks(task, index);
                //items.add(task.taskName);
                multiColumnAdapter.notifyDataSetChanged();

                Toast.makeText(this, "Task Edited", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void setUpListViewListener() {

        final Intent intent = new Intent(this, AddTodo.class);
        final Context ctx = this;
        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                intent.putExtra("taskToEdit", items.get(pos).get("TaskId"));
                intent.putExtra("index", ""+pos);
                startActivityForResult(intent, EDIT_TASK);
            }
        });

        lstView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                items.remove(pos);
                multiColumnAdapter.notifyDataSetChanged();
                //writeFile();
                deleteTask(Integer.parseInt(items.get(pos).get("TaskId")));
                Toast.makeText(ctx, "Task Delete", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }


    private List<Map<String, String>> readTasks() {
        List<ToDo> tasks = new Select()
                .from(ToDo.class).execute();
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        for(int i=0; i<tasks.size(); i++) {
            Map<String, String> map = new HashMap<>();
            map.put("TaskName", tasks.get(i).taskName);
            map.put("Priority", tasks.get(i).priority);
            map.put("Status", tasks.get(i).status);
            map.put("TaskId", ""+tasks.get(i).taskId);
            list.add(map);
        }
        return list;
    }

    private void addToTasks(ToDo task) {
        Map<String, String> map = new HashMap<>();
        map.put("TaskName", task.taskName);
        map.put("Priority", task.priority);
        map.put("Status", task.status);
        map.put("TaskId", ""+task.taskId);
        items.add(map);
    }

    private void updateTasks(ToDo task, int index) {
        Map<String, String> map = new HashMap<>();
        map.put("TaskName", task.taskName);
        map.put("Priority", task.priority);
        map.put("Status", task.status);
        map.put("TaskId", ""+task.taskId);
        items.set(index, map);
    }

    private void deleteTask(int taskId) {
        List<ToDo> tasks = new Delete()
                .from(ToDo.class).where("taskId=?", taskId).execute();

    }
}
