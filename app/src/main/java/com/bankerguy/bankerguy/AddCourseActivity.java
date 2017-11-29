package com.bankerguy.bankerguy;
//pull check
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Console;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class AddCourseActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    private Spinner nameSpinner;
    private EditText editDate;
    private Button buttonAdd;
    private Button buttonCancel;

    FirebaseDatabase database;
    FirebaseUser user;

    int chosenCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        nameSpinner = (Spinner) findViewById(R.id.spinnerName);
        editDate = (EditText) findViewById(R.id.editDate);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonCancel = (Button) findViewById(R.id.buttonBack);

        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        chosenCourse = Integer.MAX_VALUE;

        if(user == null){
            Toast.makeText(AddCourseActivity.this, "Please Log In.", Toast.LENGTH_SHORT).show();
            finish();
        }

        DatabaseReference courseListRef = database.getReference("courses");
        courseListRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                Iterable<DataSnapshot> coursesData = dataSnapshot.getChildren();
                List<Course> courses = new ArrayList<>();

                for(DataSnapshot snapshot : coursesData){
                    courses.add(snapshot.getValue(Course.class));
                }

                ArrayAdapter<Course> namesAdapter = new ArrayAdapter<Course>(AddCourseActivity.this, android.R.layout.simple_spinner_item, courses);
                namesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                nameSpinner.setAdapter(namesAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error){
                Toast.makeText(AddCourseActivity.this, "Error reading course names from database: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        buttonAdd.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
        nameSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonAdd){
            Calendar date = parseDate(editDate.getText().toString());
            if(date == null){
                return;
            }
            if(chosenCourse == Integer.MAX_VALUE){
                Toast.makeText(AddCourseActivity.this, "Please choose a course.", Toast.LENGTH_SHORT).show();
                return;
            }

            CourseProgress progress = new CourseProgress(chosenCourse, date.getTimeInMillis());

            DatabaseReference userRef = database.getReference("progress/"+user.getUid()).push();
            userRef.setValue(progress);

            finish();
        } else if (v == buttonCancel){
            finish();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.spinnerName){
            chosenCourse = ((Course) parent.getItemAtPosition(position)).getId();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        chosenCourse = Integer.MAX_VALUE;
    }

    Calendar parseDate(String date){
        int m, d, y;
        if(date.length() != 10 || date.charAt(2) != '/' || date.charAt(5) != '/'){
            Toast.makeText(AddCourseActivity.this, "Please enter date in MM/DD/YYYY format.", Toast.LENGTH_SHORT).show();
            return null;
        }

        m = Integer.parseInt(date.substring(0, 2)) - 1;
        d = Integer.parseInt(date.substring(3, 5));
        y = Integer.parseInt(date.substring(6));

        Calendar cal = Calendar.getInstance();
        cal.set(y, m, d, 0, 0, 0);

        Calendar today = Calendar.getInstance();
        today.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE), 0, 0, 0);

        Log.println(Log.INFO, "timestamps", "Cal: " + cal.toString() + ", " + cal.getTimeInMillis() + "\nToday: " + today.toString() + ", " + today.getTimeInMillis());

        if(cal.getTimeInMillis() <= today.getTimeInMillis()) {
            Toast.makeText(AddCourseActivity.this, "Please enter a future date.", Toast.LENGTH_SHORT).show();
            return null;
        }

        return cal;
    }
}
