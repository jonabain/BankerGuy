package com.bankerguy.bankerguy;

/**
 * Created by nevin on 11/12/2017.
 */

public class Card {

    private enum CardType {
        FLASHCARD, MULTIPLE_CHOICE, FILL_IN_THE_BLANK
    }

    private int cardId;
    private CardType cardType;
    private String question;
    private String answer;

}
