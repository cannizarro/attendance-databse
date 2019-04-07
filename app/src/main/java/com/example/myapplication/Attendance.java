package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Attendance extends AppCompatActivity {

    ArrayList<String> students=new ArrayList<>();
    ArrayList<Integer> pOrA=new ArrayList<>();
    ListView list;
    SQLiteDatabase attendanceManager;
    int number;
    String className;

    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        list=findViewById(R.id.list);

        Intent intent=getIntent();
        number=intent.getIntExtra("number",0);
        className=intent.getStringExtra("class");

        attendanceManager=this.openOrCreateDatabase("attendance",MODE_PRIVATE,null);

        arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,students);
        list.setAdapter(arrayAdapter);

        updateListView();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Attendance.this, "Absent !", Toast.LENGTH_SHORT).show();

                //absent
                if(pOrA.get(position) == 1)
                {
                    pOrA.set(position,0);
                    view.setBackgroundColor(Color.RED);
                }
                else
                {
                    pOrA.set(position,1);
                    view.setBackgroundColor(Color.WHITE);
                }
            }
        });

    }

    public void save(View v)
    {
        Toast.makeText(this, "Submitted!", Toast.LENGTH_SHORT).show();
        for(int i=0; i<number; i++)
        {
            // 0  means absent and 1 means present
            if(pOrA.get(i) == 0)
            {
                attendanceManager.execSQL("UPDATE leave SET days = days+1 WHERE rollNo=" + Integer.toString(i));
            }
        }
    }


    public void updateListView() {
        Cursor c = attendanceManager.rawQuery("SELECT * FROM student", null);
        Cursor c1=attendanceManager.rawQuery("Select * from leave",null);

        if (c.moveToFirst() && c1.moveToFirst()) {
            students.clear();
            pOrA.clear();
            for(int i=1;i<=number;i++)
            {
                students.add(Integer.toString(i));
                pOrA.add(1);
            }
            arrayAdapter.notifyDataSetChanged();
        }
        else
        {
            for(int i=1;i<=number;i++)
            {
                attendanceManager.execSQL("INSERT INTO student VALUES ('ANONYMOUS'," + Integer.toString(i) + ",'" + className + "')");
                attendanceManager.execSQL("INSERT INTO leave VALUES (" + Integer.toString(i) +",0)");
                students.add(Integer.toString(i));
                pOrA.add(1);
            }
        }
    }

    public void detained(View v)
    {
        Intent intent=new Intent(getApplicationContext(),Deatained.class);
        intent.putExtra("class",className);
        startActivity(intent);
    }


}
