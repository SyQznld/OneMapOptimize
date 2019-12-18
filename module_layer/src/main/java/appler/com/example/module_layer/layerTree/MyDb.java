package appler.com.example.module_layer.layerTree;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * 矢量（shp）数据库基本操作
 * */
public class MyDb extends SQLiteOpenHelper {
    public MyDb(Context context, int version) {
        super(context, "db", null, version);//可以根据最后一个参数的version来完成表的自动创建和升级
    }


    //创建数据库
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE layer(" +
                "id integer primary key autoincrement, " +
                "title TEXT DEFAULT \"\"," + "color TEXT DEFAULT \"\")";
        db.execSQL(sql);
    }


    //更新数据库
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "ALTER TABLE layer ADD COLUMN mark TEXT DEFAULT \"\"";
        db.execSQL(sql);
    }
}