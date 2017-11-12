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
import java.util.List;

public class AddCourseActivity extends Activity {

    private Spinner nameSpinner;

    private DatabaseReference courseListRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        nameSpinner = (Spinner) findViewById(R.id.spinnerName);

        courseListRef = FirebaseDatabase.getInstance().getReference("Courses");
        courseListRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                final List<String> courses = new ArrayList<String>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String name = snapshot.getValue(String.class);
                    courses.add(name);
                }

                ArrayAdapter<String> namesAdapter = new ArrayAdapter<String>(AddCourseActivity.this, android.R.layout.simple_spinner_item, courses);
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
