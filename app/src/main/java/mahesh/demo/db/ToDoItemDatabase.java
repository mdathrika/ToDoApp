package mahesh.demo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;

/**
 * Created by mdathrika on 9/24/16.
 */
public class ToDoItemDatabase extends android.app.Application {
    static final String DATABASE_NAME = "Tasks.db";
    static final int DATABASE_VERSION = 1;


    // These is where we need to write create table statements.
    // This is called when database is created.
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
//        Configuration dbConfiguration = new Configuration.Builder(this).setDatabaseName(DATABASE_NAME).create();
//        ActiveAndroid.initialize(dbConfiguration);
        System.out.print("************DB CREATED*******");
    }

}
