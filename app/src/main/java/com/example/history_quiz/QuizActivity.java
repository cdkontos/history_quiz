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

/**
 * This is the QuizActivity class for the layout {@link R.layout#activity_quiz} History Quiz app.
 * The different quiz questions and answers will be shown to the user randomized
 * and their score will be calculated.
 *
 * @author Christos Kontos
 * @author Leonidas Christoforou
 */
public class QuizActivity extends AppCompatActivity implements View.OnClickListener{
    TextView questionNum, question;
    Button ansA, ansB, ansC, ansD;
    int quizSize = 20;
    int score = 0;
    /**
     * The total number of questions in the app.
     */
    int totalQuestion = QuestionAnswer.question.length;
    /**
     * The number of the current question in the user's quiz session.
     */
    int currentQuestionIndex = 1;
    /**
     * This is used to save the real index of the question in the app question list.
     */
    int realQuestionIndex = 0;
    /**
     * This array list saves the indexes of the questions that have already been shown to the user.
     */
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

    /**
     * This method is used to load the new question after at the start of the quiz or after the user
     * answers the previous one shown to them. After the question number has reached the set number
     * the {@link #finishPopUp()} method is used to end the quiz.
     * The questions and answers shown are randomized using the {@link #randomIndex()} method
     * while checking whether the questions have been shown before or not.
     * @author Christos Kontos
     */
    void loadNewQuestion()
    {
        if(indexList.size()==quizSize)
        {
            finishPopUp();
            return;
        }
        realQuestionIndex = randomIndex();
        /*
        Checks if the randomIndex already exists in the indexList and adds it to it or keeps
        generating randomIndex until it finds one that doesn't exist in the indexList.
         */
        if(!indexList.contains(realQuestionIndex))
            indexList.add(realQuestionIndex);
        else
        {
            while(indexList.contains(realQuestionIndex = randomIndex()));
            indexList.add(realQuestionIndex);
        }
        question.setText(QuestionAnswer.question[realQuestionIndex]);
        questionNum.setText(String.valueOf(currentQuestionIndex));
        ansA.setText(QuestionAnswer.choices[realQuestionIndex][0]);
        ansB.setText(QuestionAnswer.choices[realQuestionIndex][1]);
        ansC.setText(QuestionAnswer.choices[realQuestionIndex][2]);
        ansD.setText(QuestionAnswer.choices[realQuestionIndex][3]);

    }

    /**
     * Generates a random int and returns it.
     * @return realQuestionIndex a random int generated between 0 and the {@link #totalQuestion} number.
     * @author Christos Kontos
     */
    int randomIndex()
    {
        realQuestionIndex = ThreadLocalRandom.current().nextInt(0,totalQuestion);
        return realQuestionIndex;
    }

    /**
     * This method shows the user a pop up with his achieved score and a button to call the method
     * {@link #finishQuiz()} to finish the quiz and go back to the home screen.
     * @author Christos Kontos
     */
    void finishPopUp()
    {
        new AlertDialog.Builder(this)
                .setTitle("Congratulations!")
                .setMessage("Score is " + score + " out of " + quizSize)
                .setPositiveButton("Finish",(dialogInterface, i) -> finishQuiz())
                .setCancelable(false)
                .show();
    }

    /**
     * This method searches the database for the username of the user and their old score and
     * if their current score is higher than before updates it using
     * the {@link MyDBHandler#updatePlayerScore(String, int)} method of the {@link MyDBHandler} class.
     * After it resets the variables for the score and the question indexing and returns the user to
     * the home screen.
     * @author Christos Kontos
     */
    void finishQuiz()
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