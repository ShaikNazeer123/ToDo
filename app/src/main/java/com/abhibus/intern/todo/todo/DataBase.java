package com.abhibus.intern.todo.todo;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by shaik nazeer on 6/19/2017.
 */
//Test commit
public class DataBase extends SQLiteOpenHelper {
    private  static final String dbName = "db";
    private  static final int version = 1;
    String[] columns={"Id","TaskName","StartTime","EndTime","Date","EndDate","notified","AddedOn","Finished"};
    public DataBase(Context context){
        super(context,dbName,null,version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Task (Id integer primary key autoincrement,TaskName text,StartTime text,EndTime text,Date text,EndDate text,notified integer,AddedOn text,Finished text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop Table if exists Task");
        onCreate(db);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean insertData(String tn, String st, String et, String date, String EndDate){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TaskName",tn);
        contentValues.put("StartTime",st);
        contentValues.put("EndTime",et);
        contentValues.put("Date",date);
        contentValues.put("EndDate",EndDate);
        contentValues.put("notified",0);
        String currentDateandTime = new SimpleDateFormat("dd-MMM-yy").format(new Date());
        if(currentDateandTime.charAt(0)=='0'){
            currentDateandTime = currentDateandTime.substring(0, 0) + currentDateandTime.substring(0 + 1);
            if(currentDateandTime.charAt(2)=='0'){
                currentDateandTime = currentDateandTime.substring(0, 2) + currentDateandTime.substring(2 + 1);;
            }
        }
        else if(currentDateandTime.charAt(3)=='0'){
            currentDateandTime = currentDateandTime.substring(0, 3) + currentDateandTime.substring(3 + 1);;
        }
        contentValues.put("AddedOn",currentDateandTime);
        contentValues.put("Finished",0);
        long result = db.insert("Task",null,contentValues);
        if(result==-1)
            return false;
        else
            return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Cursor getTasks(int i){
        Cursor cursor;
        if(i==0) {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor= db.rawQuery("Select * from Task ORDER BY Date", null);
        }
        else if(i==2){ //tomorrow

            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE,1);
            String t = new SimpleDateFormat("dd-MM-yyyy").format(c.getTime());
            if(t.charAt(0)=='0'){
                t = t.substring(0, 0) + t.substring(0 + 1);
                if(t.charAt(2)=='0'){
                    t = t.substring(0, 2) + t.substring(2 + 1);;
                }
            }
            else if(t.charAt(3)=='0'){
                t = t.substring(0, 3) + t.substring(3 + 1);;
            }
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.query("Task",columns,"Date=?",new String[]{t},null,null,null);
        }
        else{//Today
            Log.e("Today"," Called");
            String t = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            if(t.charAt(0)=='0'){
                t = t.substring(0, 0) + t.substring(0 + 1);
                if(t.charAt(2)=='0'){
                    t = t.substring(0, 2) + t.substring(2 + 1);;
                }
            }
            else if(t.charAt(3)=='0'){
                t = t.substring(0, 3) + t.substring(3 + 1);;
            }
            Log.e(t,"jkg");
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.query("Task",columns,"Date=?",new String[]{t},null,null,null);
        }

        if(cursor!=null)
            cursor.moveToFirst();
        return cursor;
    }

    public Cursor getTasks(String d){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query("Task",columns,"Date=?",new String[]{d},null,null,null);
        if(cursor!=null)
            cursor.moveToFirst();
        return cursor;
    }

    public void delete(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Task","TaskName =?",new String[]{name});
        Log.e("delete ",name);
        db.close();
    }

    public void update(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String updateQuery = "UPDATE Task SET notified = 1 WHERE Id = " + id;
        Cursor c = db.rawQuery(updateQuery,null);
        c.moveToFirst();
        c.close();
        db.close();
    }



    public void update(String name,int flag){
        SQLiteDatabase db = this.getWritableDatabase();
        String updateQuery = "UPDATE Task SET Finished = "+flag+" WHERE TaskName = '" + name+"'";
        Cursor c = db.rawQuery(updateQuery,null);
        c.moveToFirst();
        c.close();
        db.close();
    }

    public void update(String oName,String name,String sdate,String edate,String stime,String etime){
        SQLiteDatabase db = this.getWritableDatabase();
        String updateQuery = "UPDATE Task SET TaskName = '"+name+ "', StartTime = '"+stime+"' , EndTime = '"+etime+"' , Date = '"+sdate+"' , EndDate = '"+edate+"' WHERE TaskName = '"+oName+"'";
        Log.e("Update ","Rc");
        Cursor c = db.rawQuery(updateQuery,null);
        c.moveToFirst();
        c.close();
        db.close();
    }

    public Cursor getRow(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM Task WHERE TaskName = '"+name+"'";
        Cursor c = db.rawQuery(query,null);
        return c;
    }

    public Cursor getDistinct(){
        Cursor c;
        SQLiteDatabase db = this.getWritableDatabase();
        c = db.query(true,"Task",new String[]{"Date"},null,null,"Date",null,null,null);
        return c;
    }
}
