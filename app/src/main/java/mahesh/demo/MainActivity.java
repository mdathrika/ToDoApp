package mahesh.demo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import mahesh.demo.db.ToDo;

public class MainActivity extends AppCompatActivity {

    List<ToDo> items;
    MultiColumnAdapter multiColumnAdapter;
    ListView lstView;
    private final int NEW_TASK = 20;
    private final int EDIT_TASK = 21;

    private List<ToDo> toDelete = new ArrayList<ToDo>();
    private List<String> indexes = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lstView = (ListView) findViewById(R.id.lvToDoList);

        items = readTasks();
        multiColumnAdapter = new MultiColumnAdapter(this, items);
        lstView.setAdapter(multiColumnAdapter);

        setUpListViewListener();
    }

    public void onClick(View v) {
        Intent intent = new Intent(this, AddTodo.class);
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
        if (resultCode == RESULT_OK) {

            if(requestCode == NEW_TASK) {

                ToDo task = (ToDo)(data.getExtras().getSerializable("SavedTask"));
                addToTasks(task);
                multiColumnAdapter.notifyDataSetChanged();

                Toast.makeText(this, "Task Added", Toast.LENGTH_SHORT).show();
            }

            if(requestCode == EDIT_TASK) {

                ToDo task = (ToDo)(data.getExtras().getSerializable("SavedTask"));
                int index = Integer.parseInt(data.getStringExtra("index"));
                updateTasks(task, index);
                multiColumnAdapter.notifyDataSetChanged();

                Toast.makeText(this, "Task Edited", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_share:

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");

                intent.putExtra(Intent.EXTRA_TEXT, "Task");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(Intent.createChooser(intent,"Share using"));
                } else {
                    startActivity(intent);
                }
                return true;
            case R.id.action_delete:
                for(int i=0; i< toDelete.size();i++) {
                    deleteTask(toDelete.get(i).taskId);
                    items.remove(Integer.parseInt(indexes.get(i)));
                }

                multiColumnAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Task Deleted", Toast.LENGTH_SHORT).show();

                toDelete = new ArrayList<ToDo>();
                indexes = new ArrayList<String>();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpListViewListener() {

        final Intent intent = new Intent(this, AddTodo.class);
        final Context ctx = this;
        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                intent.putExtra("taskToEdit", items.get(pos).taskId);
                intent.putExtra("index", ""+pos);
                System.out.println("*****ITEM PRESSED*****"+pos + " : " +id);
                startActivityForResult(intent, EDIT_TASK);
            }
        });

        lstView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {

                //deleteTask(items.get(pos).taskId);

                //items.remove(pos);

                indexes.add(""+pos);
                toDelete.add(items.get(pos));

                //multiColumnAdapter.notifyDataSetChanged();

                //Toast.makeText(ctx, "Task Delete", Toast.LENGTH_SHORT).show();

                return true;
            }
        });

    }


    private List<ToDo> readTasks() {
        List<ToDo> tasks = new Select()
                .from(ToDo.class).execute();
        return tasks;
    }

    private void addToTasks(ToDo task) {
        items.add(task);
    }

    private void updateTasks(ToDo task, int index) {
        items.set(index, task);
    }

    private void deleteTask(int taskId) {
        List<ToDo> tasks = new Delete()
                .from(ToDo.class).where("taskId=?", taskId).execute();

    }
}
