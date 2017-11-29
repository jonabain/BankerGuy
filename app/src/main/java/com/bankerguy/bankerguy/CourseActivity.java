package com.bankerguy.bankerguy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CourseActivity extends Activity implements Button.OnClickListener{

    private Button flipButton;
    private Button correctButton;
    private Button incorrectButton;
    private TextView flashcard;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        flipButton = findViewById(R.id.buttonFlip);
        correctButton = findViewById(R.id.buttonCorrect);
        incorrectButton = findViewById(R.id.buttonIncorrect);
        flashcard = findViewById(R.id.textViewFlashcard);
        progressBar = findViewById(R.id.progressBar);

        flipButton.setOnClickListener(this);
        correctButton.setOnClickListener(this);
        incorrectButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        if (view == flipButton){
            DatabaseReference myLookupRef = database.getReference("test");
            myLookupRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Double currentValue = dataSnapshot.getValue(double.class);
                    flashcard.setText(currentValue.toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        if (view == correctButton){
            flashcard.setText("CORRECT");
            progressBar.setProgress(50);
        }
        if (view == incorrectButton){
            flashcard.setText("INCORRECT");
            progressBar.setProgress(25);
        }
    }
}
