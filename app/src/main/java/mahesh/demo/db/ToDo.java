package mahesh.demo.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by mdathrika on 9/24/16.
 */
@Table(name = "ToDo")
public class ToDo extends Model implements Serializable {

    @Column(name = "TaskId")
    public int taskId;

    @Column(name = "TaskName")
    public String taskName;

    @Column(name = "TaskDesc")
    public String taskDescription;

    @Column(name = "TaskDueDate")
    public Date taskDueDate;

    @Column(name = "Priority")
    public String priority;

    @Column(name = "Status")
    public String status;

    public ToDo() {
        super();
    }

    public ToDo(int id, String name, String desc, Date due, String priority, String status){
        super();
        this.taskId = id;
        this.taskName = name;
        this.taskDescription = desc;
        this.taskDueDate = due;
        this.priority = priority;
        this.status = status;
    }
}
