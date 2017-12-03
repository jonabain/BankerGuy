package com.bankerguy.bankerguy;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by nevin on 11/12/2017.
 */

public class CourseProgress {

    private String id;
    private int courseId;
    private long dueDate; // Stored in Milliseconds
    private List<String> completedCards;

    public CourseProgress(){

    }

    public CourseProgress(int courseId, long dueDateMillis, String id){
        this.id = id;
        this.courseId = courseId;
        this.dueDate = dueDateMillis;
        completedCards = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public long getDueDate() {
        return dueDate;
    }

    public String getDueDateString() {
        Calendar cal = Calendar.getInstance();
        Date date = new Date(getDueDate());
        cal.setTime(date);
        return (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE) + "-" + cal.get(Calendar.YEAR);
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public List<String> getCompletedCards() {
        return completedCards;
    }

    public void setCompletedCards(List<String> completedCards) {
        this.completedCards = completedCards;
    }
}
