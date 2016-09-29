package mahesh.demo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mahesh.demo.db.ToDo;

/**
 * Created by mdathrika on 9/26/16.
 */
public class MultiColumnAdapter extends ArrayAdapter<ToDo> {

    public List<ToDo> list;
    Activity activity;

    TextView taskName;
    TextView priority;
    TextView dueDate;
    ImageView status;

    public MultiColumnAdapter(Context context, List<ToDo> list) {
        super(context, 0, list);
        this.list = list;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView=LayoutInflater.from(getContext()).inflate(R.layout.multi_column_list, parent, false);
        }

        taskName=(TextView) convertView.findViewById(R.id.taskName);
        priority=(TextView) convertView.findViewById(R.id.priority);
        status=(ImageView) convertView.findViewById(R.id.status);
        dueDate = (TextView) convertView.findViewById(R.id.taskDueDate);

        ToDo map = getItem(position);

        taskName.setText(map.taskName);
        priority.setText(map.priority);
        int color = map.priority.equalsIgnoreCase("HIGH") ? Color.RED : (map.priority.equalsIgnoreCase("MEDIUM") ? 0xFF4B8FEF  : 0xFFC7720F);
        priority.setTextColor(color);


        if(map.status.equalsIgnoreCase("DONE"))
            status.setImageResource(R.drawable.check);
        else
            status.setImageResource(R.drawable.dots_horizontal);

        dueDate.setText(map.taskDueDate.toString());

        return convertView;


    }
}
