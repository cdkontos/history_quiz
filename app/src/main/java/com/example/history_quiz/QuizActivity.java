package com.example.history_quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener{

    TextView questionNum, question;
    Button ansA, ansB, ansC, ansD;
    int quizSize = 5;
    int score = 0;
    int totalQuestion = QuestionAnswer.question.length;
    int currentQuestionIndex = 1;
    int realQuestionIndex = 0;
    ArrayList<Integer> indexList = new ArrayList<>();
    String selectedAnswer = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionNum = findViewById(R.id.question_num);
        question = findViewById(R.id.question);
        ansA = findViewById(R.id.ans_A);
        ansB = findViewById(R.id.ans_B);
        ansC = findViewById(R.id.ans_C);
        ansD = findViewById(R.id.ans_D);

        ansA.setOnClickListener(this);
        ansB.setOnClickListener(this);
        ansC.setOnClickListener(this);
        ansD.setOnClickListener(this);

        questionNum.setText("Question " + currentQuestionIndex);

        loadNewQuestion();

    }

    @Override
    public void onClick(View v) {
        Button clickedButton = (Button) v;
        if(clickedButton.getId() == R.id.ans_A || clickedButton.getId() == R.id.ans_B || clickedButton.getId() == R.id.ans_C || clickedButton.getId() == R.id.ans_D)
        {
            selectedAnswer = clickedButton.getText().toString();
            if(selectedAnswer.equals(QuestionAnswer.answer[realQuestionIndex]))
            {
                score++;
            }
            currentQuestionIndex++;
            loadNewQuestion();
        }

    }

    void loadNewQuestion()
    {
        if(indexList.size()==quizSize)
        {
            finishQuiz();
            return;
        }
        realQuestionIndex = randomIndex();
        if(!indexList.contains(realQuestionIndex))
            indexList.add(realQuestionIndex);
        else
        {
            while(indexList.contains(randomIndex()));
        }
        question.setText(QuestionAnswer.question[realQuestionIndex]);
        questionNum.setText(String.valueOf(currentQuestionIndex));
        ansA.setText(QuestionAnswer.choices[realQuestionIndex][0]);
        ansB.setText(QuestionAnswer.choices[realQuestionIndex][1]);
        ansC.setText(QuestionAnswer.choices[realQuestionIndex][2]);
        ansD.setText(QuestionAnswer.choices[realQuestionIndex][3]);

    }
    int randomIndex()
    {
        realQuestionIndex = ThreadLocalRandom.current().nextInt(0,totalQuestion);
        return realQuestionIndex;
    }

    void finishQuiz()
    {
        String passStatus;
        if(score >quizSize*0.60)
        {
            passStatus = "Passed";
        }
        else
            passStatus = "Failed";

        new AlertDialog.Builder(this)
                .setTitle(passStatus)
                .setMessage("Score is " + score + " out of " + quizSize)
                .setPositiveButton("Restart",(dialogInterface, i) -> restartQuiz())
                .setCancelable(false)
                .show();
    }
    void restartQuiz()
    {
        score = 0;
        currentQuestionIndex = 1;
        indexList.clear();
        startActivity(new Intent(QuizActivity.this, MainActivity.class));

    }

}