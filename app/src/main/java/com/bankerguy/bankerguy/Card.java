package com.bankerguy.bankerguy;

import java.util.List;

/**
 * Created by nevin on 11/12/2017.
 */

public class Card {

    private enum CardType {
        FLASHCARD, MULTIPLE_CHOICE, FILL_IN_THE_BLANK
    }

    private String id;
    private CardType type;
    private String question;
    private String answer;

    public Card(){

    }

    Card(String id, String type, String question, String answer){
        this.id = id;
        this.question = question;
        this.answer = answer;
        for(CardType cardType : CardType.values()){
            if(type.equalsIgnoreCase(cardType.toString())){
                this.type = cardType;
                break;
            }
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
