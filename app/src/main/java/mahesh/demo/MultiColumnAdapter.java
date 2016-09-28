package mahesh.demo;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mdathrika on 9/26/16.
 */
public class MultiColumnAdapter extends BaseAdapter {

    public List<Map<String, String>> list;
    Activity activity;

    TextView taskName;
    TextView priority;
    ImageView status;

    public MultiColumnAdapter(Activity activity, List<Map<String, String>> list){
        super();
        this.activity=activity;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=activity.getLayoutInflater();

        if(convertView == null){

            convertView=inflater.inflate(R.layout.multi_column_list, null);

            taskName=(TextView) convertView.findViewById(R.id.taskName);
            priority=(TextView) convertView.findViewById(R.id.priority);
            status=(ImageView) convertView.findViewById(R.id.status);

        }

        Map<String, String> map=list.get(position);
        taskName.setText(map.get("TaskName"));
        priority.setText(map.get("Priority"));
        int color = map.get("Priority").equalsIgnoreCase("HIGH") ? Color.RED : (map.get("Priority").equalsIgnoreCase("MEDIUM") ? 0xFF4B8FEF  : 0xFFC7720F);
        priority.setTextColor(color);


        if(map.get("Status").equalsIgnoreCase("DONE"))
            status.setImageResource(R.drawable.check);
        else
            status.setImageResource(R.drawable.dots_horizontal);

        return convertView;


    }
}
