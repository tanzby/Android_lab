package pub.tanzby.lab8;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tan on 2017/12/5.
 */

public class mydb extends SQLiteOpenHelper
{
    private static final String DB_NAME = "Contacts.db";
    private static final String TABLE_NAME = "Contacts";
    private static final int DB_VERSION = 1;

    public mydb(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public mydb(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "create table " + TABLE_NAME
                + " (_id integer primary key , "
                + "name text , "
                + "birth text , "
                + "gift text);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO
    }

    public void dbOpe(String name, String birthday, String gift, Integer type)
    {
        SQLiteDatabase db = getWritableDatabase();
        String []exc_sql = new String[]{
                String.format("INSERT INTO %s (name,birth,gift) VALUES ('%s','%s', '%s');",
                        TABLE_NAME,name,birthday,gift),
                String.format("UPDATE %s SET birth = '%s', gift = '%s' WHERE name='%s';",
                        TABLE_NAME,birthday,gift,name),
                String.format("DELETE FROM '%s' WHERE name='%s';",TABLE_NAME,name)
        };
        db.execSQL(exc_sql[type]);
    }

    public List<contactItem> selectAll()
    {
        SQLiteDatabase db = getReadableDatabase();
        List<contactItem> newList = new ArrayList<>();
        String select_all = "SELECT * FROM "+TABLE_NAME;
        Cursor cursor = db.rawQuery(select_all,null);
        while (cursor.moveToNext()) {

            newList.add(new contactItem(cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3)));

        }
        cursor.close();
        db.close();
        return newList;
    }
}
