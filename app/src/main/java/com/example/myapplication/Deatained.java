package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class Deatained extends AppCompatActivity {

    EditText total;
    ListView students;
    ArrayList<String> studentList=new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    int totalClasses;
    SQLiteDatabase attendanceManager;
    String className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deatained);

        total=findViewById(R.id.total);
        students=findViewById(R.id.detainedStudents);

        Intent intent=getIntent();
        className=intent.getStringExtra("class");

        attendanceManager=this.openOrCreateDatabase("attendance",MODE_PRIVATE,null);
        arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,studentList);
        students.setAdapter(arrayAdapter);
    }

    public void detained(View v)
    {
        totalClasses=Integer.parseInt(total.getText().toString());
        int maxAbs= (int) Math.floor(0.25*totalClasses);
        Log.i("maxAbs",Integer.toString(maxAbs));
        Cursor c = attendanceManager.rawQuery("SELECT * FROM student,leave,class WHERE leave.days > " + Integer.toString(maxAbs) + " AND student.cName=class.cName  AND student.rollNo = leave.rollNo", null);
        int rollIndex = c.getColumnIndex("rollNo");
        if(c.moveToFirst()) {
            do {
                studentList.add(c.getString(rollIndex));
            } while (c.moveToNext());

            arrayAdapter.notifyDataSetChanged();
        }
    }
}
