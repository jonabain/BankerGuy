package com.bankerguy.bankerguy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
import java.util.List;
import java.util.Random;

public class CourseActivity extends Activity implements Button.OnClickListener{

    FirebaseDatabase database;
    FirebaseUser user;

    Random rand;

    private Button flipButton;
    private Button correctButton;
    private Button incorrectButton;
    private Button backButton;
    private TextView flashcard;
    private TextView textTitle;
    private ProgressBar progressBar;

    private Course course;
    private CourseProgress progress;
    private List<Card> cards;

    private Card currentCard;
    private boolean displayingQuestion;
    private boolean completed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        flipButton = findViewById(R.id.buttonFlip);
        correctButton = findViewById(R.id.buttonCorrect);
        incorrectButton = findViewById(R.id.buttonIncorrect);
        backButton = findViewById(R.id.buttonBack);
        flashcard = findViewById(R.id.textViewFlashcard);
        textTitle = findViewById(R.id.textTitle);
        progressBar = findViewById(R.id.progressBar);

        flipButton.setOnClickListener(this);
        correctButton.setOnClickListener(this);
        incorrectButton.setOnClickListener(this);
        backButton.setOnClickListener(this);

        rand = new Random();
        completed = false;
        cards = new ArrayList<>();
        getCourseProgress(getIntent().getExtras().getString("progressId"));
    }

    void getCourseProgress(final String key){
        database.getReference("progress/"+user.getUid()+"/"+key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progress = dataSnapshot.getValue(CourseProgress.class);
                if(progress.getCompletedCards() == null){
                    progress.setCompletedCards(new ArrayList<String>());
                }
                getCourseInfo(getIntent().getExtras().getInt("courseId"));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CourseActivity.this, "Error reading progress data.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    void getCourseInfo(final int id){
        DatabaseReference ref = database.getReference("courses");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Course c = snapshot.getValue(Course.class);
                    if(c.getId() == id){
                        course = c;
                        textTitle.setText(course.getName());
                        getAllCards();
                        return;
                    }
                }
                Toast.makeText(CourseActivity.this, "Course not found.", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CourseActivity.this, "Error reading course data.", Toast.LENGTH_LONG).show();
            }
        });
    }

    void getAllCards(){
        DatabaseReference ref = database.getReference("cards");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    cards.add(snapshot.getValue(Card.class));
                }

                progressBar.setProgress((int) (100 * (float) progress.getCompletedCards().size() / (float) course.getCards().size()));
                newCard();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CourseActivity.this, "Error reading card data.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == flipButton){
            displayingQuestion = !displayingQuestion;
            flashcard.setText(displayingQuestion ? currentCard.getQuestion() : currentCard.getAnswer());
        } else if (view == correctButton){
            if(!completed) {
                updateProgress(currentCard.getId());
            }
            newCard();
        } else if (view == incorrectButton){
            newCard();
        } else if (view == backButton){
            closeCourse();
        }
    }

    void newCard(){
        if(!completed && progress.getCompletedCards().size() == course.getCards().size()){
            courseCompleted();
            return;
        }

        int cardIndex = rand.nextInt(course.getCards().size());
        String cardId = course.getCards().get(cardIndex);

        while((!completed && progress.getCompletedCards().contains(cardId)) || (!(currentCard == null) && progress.getCompletedCards().size() < course.getCards().size() - 1 && cardId.equals(currentCard.getId()))){
            cardIndex = rand.nextInt(course.getCards().size());
            cardId = course.getCards().get(cardIndex);
        }

        for(Card card : cards){
            if(card.getId().equals(cardId)){
                currentCard = card;
                flashcard.setText(currentCard.getQuestion());
                displayingQuestion = true;
                return;
            }
        }

        Toast.makeText(this, "Could not find card " + cardId + ". Marking completed.", Toast.LENGTH_LONG).show();
        updateProgress(cardId);
        newCard();
    }

    void courseCompleted(){
        completed = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You're a BankerGuy!");
        builder.setMessage("Congrats, you've completed all the questions in this course! You can stay and continue practicing with this set of questions or leave and start a new course.");
        builder.setPositiveButton("Stay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Leave", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                closeCourse();
            }
        });
        builder.show();
    }

    @Override
    public void onPause(){
        super.onPause();
        database.getReference("progress/" + user.getUid() + "/" + progress.getId()).setValue(progress);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(completed){
            database.getReference("progress/" + user.getUid() + "/" + progress.getId()).removeValue();
        } else {
            database.getReference("progress/" + user.getUid() + "/" + progress.getId()).setValue(progress);
        }
    }

    void closeCourse(){
        if(completed){
            database.getReference("progress/" + user.getUid() + "/" + progress.getId()).removeValue();
        } else {
            database.getReference("progress/" + user.getUid() + "/" + progress.getId()).setValue(progress);
        }
        finish();
    }

    void updateProgress(String cardId){
        progress.getCompletedCards().add(cardId);
        progressBar.setProgress((int) (100 * (float) progress.getCompletedCards().size() / (float) course.getCards().size()));
    }
}
