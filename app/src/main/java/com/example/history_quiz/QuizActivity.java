package com.example.history_quiz;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener{

    TextView questionNum, question;
    Button ansA, ansB, ansC, ansD;
    int quizSize = 20;
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
        System.out.println(indexList.size());
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
            while(indexList.contains(realQuestionIndex = randomIndex()));
            indexList.add(realQuestionIndex);
        }
        System.out.println("realquestionindex " + realQuestionIndex);
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
        new AlertDialog.Builder(this)
                .setTitle("Congratulations!")
                .setMessage("Score is " + score + " out of " + quizSize)
                .setPositiveButton("Finish",(dialogInterface, i) -> restartQuiz())
                .setCancelable(false)
                .show();
    }
    void restartQuiz()
    {
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        DatabaseManager databaseManager = DatabaseManager.getInstance(getApplicationContext());
        MyDBHandler database = databaseManager.getDBHandler();
        int oldScore = database.getPlayerScore(username);
        if(oldScore<score)
            database.updatePlayerScore(username,score);
        score = 0;
        currentQuestionIndex = 1;
        indexList.clear();
        startActivity(new Intent(QuizActivity.this, MainActivity.class));

    }

}