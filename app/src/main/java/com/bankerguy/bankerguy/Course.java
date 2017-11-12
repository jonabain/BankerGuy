package com.bankerguy.bankerguy;

import java.util.List;

/**
 * Created by nevin on 11/12/2017.
 */

public class Course {

    private enum CourseType {
        FLASHCARDS
    }

    private int courseId;
    private String courseName;
    private CourseType courseType;
    private List<Card> cards; //private List<Integer> cards;

}
