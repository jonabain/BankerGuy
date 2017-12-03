package com.bankerguy.bankerguy;

import java.util.List;

/**
 * Created by nevin on 11/12/2017.
 */

public class Course {

    private enum CourseType {
        FLASHCARDS
    }

    private int id;
    private String name;
    private CourseType type;
    private List<String> cards;

    public Course(){

    }

    Course(int id, List<String> cards, String name, String type){
        this.id = id;
        this.cards = cards;
        this.name = name;
        for(CourseType courseType : CourseType.values()){
            if(type.equalsIgnoreCase(courseType.toString())){
                this.type = courseType;
                break;
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CourseType getType() {
        return type;
    }

    public void setType(CourseType type) {
        this.type = type;
    }

    public List<String> getCards() {
        return cards;
    }

    public void setCards(List<String> cards) {
        this.cards = cards;
    }

    public String toString(){
        return name;
    }
}
