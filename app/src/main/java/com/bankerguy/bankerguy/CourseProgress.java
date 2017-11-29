package com.bankerguy.bankerguy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by nevin on 11/12/2017.
 */

public class CourseProgress {

    private int courseId;
    private long dueDate; // Stored in Milliseconds
    private List<Integer> completedCards;

    public CourseProgress(){

    }

    public CourseProgress(int courseId, long dueDateMillis){
        this.courseId = courseId;
        this.dueDate = dueDateMillis;
        completedCards = new ArrayList<>();
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

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public List<Integer> getCompletedCards() {
        return completedCards;
    }

    public void setCompletedCards(List<Integer> completedCards) {
        this.completedCards = completedCards;
    }
}
