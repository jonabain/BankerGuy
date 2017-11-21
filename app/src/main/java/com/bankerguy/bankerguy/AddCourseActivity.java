package com.bankerguy.bankerguy;
//pull check
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class AddCourseActivity extends Activity {

    private Spinner nameSpinner;

    private DatabaseReference courseListRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        nameSpinner = (Spinner) findViewById(R.id.spinnerName);

        courseListRef = FirebaseDatabase.getInstance().getReference("courses");

        courseListRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                Iterable<DataSnapshot> coursesData = dataSnapshot.getChildren();
                Map<Integer, Course> courses = new Hashtable<>();

                for(DataSnapshot snapshot : coursesData){
                    courses.put(Integer.parseInt(snapshot.getKey()), snapshot.getValue(Course.class));
                }

                List<String> courseNames = new ArrayList<>();

                for(Hashtable.Entry<Integer, Course> entry : courses.entrySet()){
                    courseNames.add(entry.getValue().getName());
                }

                ArrayAdapter<String> namesAdapter = new ArrayAdapter<String>(AddCourseActivity.this, android.R.layout.simple_spinner_item, courseNames);
                namesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                nameSpinner.setAdapter(namesAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error){
                Toast.makeText(AddCourseActivity.this, "Error reading course names from database: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
