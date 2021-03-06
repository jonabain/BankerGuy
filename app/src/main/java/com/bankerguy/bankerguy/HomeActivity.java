package com.bankerguy.bankerguy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends Activity implements AppCompatCallback, AdapterView.OnItemClickListener {

    private AppCompatDelegate delegate;

    private FirebaseDatabase database;
    private FirebaseUser user;
    private DatabaseReference progressRef;
    private ValueEventListener progressListener;

    private ListView enrolledList;
    private Map<Integer, Course> coursesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        delegate = AppCompatDelegate.create(this, this);
        delegate.installViewFactory();

        super.onCreate(savedInstanceState);
        delegate.onCreate(savedInstanceState);

        delegate.setContentView(R.layout.activity_home);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        delegate.setSupportActionBar(toolbar);

        enrolledList = findViewById(R.id.listEnrolled);
        enrolledList.setOnItemClickListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Toast.makeText(HomeActivity.this, "Please Log In.", Toast.LENGTH_SHORT).show();
            finish();
        }
        database = FirebaseDatabase.getInstance();

        DatabaseReference coursesRef = database.getReference("courses");
        coursesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> coursesData = dataSnapshot.getChildren();
                Map<Integer, Course> courses = new HashMap<>();

                for(DataSnapshot snapshot : coursesData){
                    Course course = snapshot.getValue(Course.class);
                    courses.put(course.getId(), course);
                }

                setCoursesList(courses);
                loadProgress();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, "Error reading courses list from database: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*@Override
    public void onResume(){
        super.onResume();
        loadProgress();
    }*/

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long arg){
        CourseProgress selected = (CourseProgress)(adapter.getAdapter().getItem(position));
        if(selected.getCompletedCards() == null){
            selected.setCompletedCards(new ArrayList<String>());
        }

        Intent toCourse = new Intent(this, CourseActivity.class);
        toCourse.putExtra("courseId", selected.getCourseId());
        toCourse.putStringArrayListExtra("completed", new ArrayList<>(selected.getCompletedCards()));
        toCourse.putExtra("progressId", selected.getId());
        startActivity(toCourse);
    }

    @Override
    public void onSupportActionModeStarted(ActionMode mode){

    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode){

    }

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.action_logout){
            logout();
            return true;
        } else if (id == R.id.action_add){
            goToAdd();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToAdd(){
        this.startActivity(new Intent(this, AddCourseActivity.class));
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        progressRef.removeEventListener(progressListener);
    }

    public void logout(){
        finish();
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void onBackPressed(){
        logout();
    }

    public void setCoursesList(Map<Integer, Course> courses){
        coursesList = courses;
    }

    public void loadProgress(){
        progressRef = database.getReference("progress/" + user.getUid());
        progressListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<CourseProgress> courses = new ArrayList<>();
                Iterable<DataSnapshot> progress = dataSnapshot.getChildren();

                for(DataSnapshot snapshot : progress){
                    courses.add(snapshot.getValue(CourseProgress.class));
                }

                generateList(courses);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if(FirebaseAuth.getInstance().getCurrentUser() != null){
                    Toast.makeText(HomeActivity.this, "Error reading list of enrolled courses from database: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        progressRef.addValueEventListener(progressListener);
    }

    public void generateList(List<CourseProgress> courses){
        enrolledList.setAdapter(new CourseProgressListAdapter(this, courses.toArray(new CourseProgress[courses.size()])));
    }

    public class CourseProgressListAdapter extends ArrayAdapter<CourseProgress> {
        Context context;

        public CourseProgressListAdapter(Context context, CourseProgress[] courses){
            super(context, -1, -1, courses);
            this.context = context;
        }

        public CourseProgressListAdapter(Context context, int resource, int textViewResourceId, CourseProgress[] courses){
            super(context, resource, textViewResourceId, courses);
            this.context = context;
        }

        @SuppressLint("ResourceType")
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            DisplayMetrics dm = context.getResources().getDisplayMetrics();

            LinearLayout listLayout = new LinearLayout(context);
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, (int)((50 * dm.density) + 0.5));
            listLayout.setLayoutParams(layoutParams);
            listLayout.setId(/*R.id.listLayoutProgress*/1337);

            LinearLayout.LayoutParams leftParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
            leftParams.weight = 1;
            leftParams.gravity = Gravity.LEFT;
            leftParams.setMargins(8, 16, 0, 16);

            LinearLayout.LayoutParams rightParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
            rightParams.weight = 1;
            leftParams.gravity = Gravity.RIGHT;
            leftParams.setMargins(0, 16, 8, 16);

            TextView textName = new TextView(context);
            textName.setId(/*R.id.textProgressName*/1338);
            textName.setLayoutParams(leftParams);
            listLayout.addView(textName);

            TextView textDate = new TextView(context);
            textDate.setId(/*R.id.textProgressDate*/1339);
            textName.setLayoutParams(rightParams);
            listLayout.addView(textDate);

            textName.setText(coursesList.get(super.getItem(position).getCourseId()).getName());
            textDate.setText(super.getItem(position).getDueDateString());

            return listLayout;
        }
    }

}
