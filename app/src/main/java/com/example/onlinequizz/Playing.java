package com.example.onlinequizz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinequizz.Common.Common;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import static com.example.onlinequizz.Common.Common.questionList;

public class Playing extends AppCompatActivity implements View.OnClickListener {

    CountDownTimer mCountDown;

    int index=0, score=0, thisQuestion=0, totalQuestion, correctAnswer;
    final static long INTERVAL = 1000;
    final static long TIMEOUT = 7000;
    int progressValue = 0;

    ProgressBar progressBar;
    ImageView question_image;
    Button btnA, btnB, btnC, btnD;
    TextView txtScore, txtQuestionNum, question_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);


        totalQuestion = questionList.size();
        question_image = findViewById(R.id.question_image);
        txtScore = findViewById(R.id.txtScore);
        txtQuestionNum = findViewById(R.id.txtTotalQuestion);
        question_text = findViewById(R.id.question_text);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(totalQuestion);

        btnA = findViewById(R.id.btnAnswerA);
        btnB = findViewById(R.id.btnAnswerB);
        btnC = findViewById(R.id.btnAnswerC);
        btnD = findViewById(R.id.btnAnswerD);

        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);

       QueShow(index);
    }

    @Override
    public void onClick(View view) {
        if(index < totalQuestion){
            
            Button clickedButton = (Button) view;
            if(clickedButton.getText().equals(questionList.get(index).getCorrectAnswer())){
                score+=10;
                correctAnswer++;
                question_text.setText("Answer is correct");
                index++;
                QueShow(index);
            }
            else {

                index++;
                QueShow(index);

            }

            txtScore.setText(String.format("%d",score));
            
        }
    }

    private void QueShow(int index){

        if(index < totalQuestion){
            thisQuestion++;
            progressBar.setProgress(thisQuestion - 1);
            txtQuestionNum.setText(String.format("%d / %d",thisQuestion, totalQuestion));

            if(questionList.get(index).getIsImageQuestion().equals("true")){

                Picasso.get().load(questionList.get(index).getQuestion()).into(question_image);
                Toast.makeText(Playing.this,"Картинка", Toast.LENGTH_SHORT).show();
                question_image.setVisibility(View.VISIBLE);
                question_text.setVisibility(View.INVISIBLE);

            }
            else {
                question_text.setText(Common.questionList.get(index).getQuestion());
                question_image.setVisibility(View.INVISIBLE);
                question_text.setVisibility(View.VISIBLE);
            }

            btnA.setText(Common.questionList.get(index).getAnswerA());
            btnB.setText(Common.questionList.get(index).getAnswerB());
            btnC.setText(Common.questionList.get(index).getAnswerC());
            btnD.setText(Common.questionList.get(index).getAnswerD());

        }

        else {

            Intent intent = new Intent(this,Done.class);
            Bundle dataSend = new Bundle();
            dataSend.putInt("SCORE", score);
            dataSend.putInt("TOTAL", totalQuestion);
            dataSend.putInt("CORRECT", correctAnswer);
            intent.putExtras(dataSend);
            startActivity(intent);
            finish();

        }
    }
}
