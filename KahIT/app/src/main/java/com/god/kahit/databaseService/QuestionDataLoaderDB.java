package com.god.kahit.databaseService;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.god.kahit.model.Category;
import com.god.kahit.model.IQuestionDataLoader;
import com.god.kahit.model.Question;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that acts as a adapter between the rest of the model and the database implementation
 */
public class QuestionDataLoaderDB implements IQuestionDataLoader {
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDB;

    public QuestionDataLoaderDB(Context context) {
        mDBHelper = new DatabaseHelper(context);
        try {
            mDBHelper.updateDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mDB = mDBHelper.getReadableDatabase();
    }

    /**
     * Method that loads all questions from a given table from the database
     *
     * @param category the category that decides from which table the questions are pulled from
     * @return the list with all of the questions
     */
    public List<Question> getQuestionList(Category category) {
        Cursor resultSet = mDB.rawQuery("SELECT * from " + category.toString(), null); //Gets all data from the given table
        resultSet.moveToFirst();
        List<Question> questions = new ArrayList<>();

        do {
            questions.add(getQuestion(resultSet, category));
        } while (resultSet.move(1)); //move moves relative to the current position and returns false if it cant reach that pos
        resultSet.close();
        return questions;
    }

    /**
     * Method that uses the values of a given cursor to create a single question
     *
     * @param resultSet the cursor that is pointing at the data
     * @param category  the category of the question
     * @return the created question
     */
    private Question getQuestion(Cursor resultSet, Category category) {
        List<String> alts = new ArrayList<>();
        alts.add(resultSet.getString(3));
        alts.add(resultSet.getString(4));
        alts.add(resultSet.getString(5));
        alts.add(resultSet.getString(6));
        return new Question(category, resultSet.getString(1), resultSet.getString(3), alts, resultSet.getInt(2));
    }
}
