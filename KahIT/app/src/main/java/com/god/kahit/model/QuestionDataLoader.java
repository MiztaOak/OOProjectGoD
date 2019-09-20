package com.god.kahit.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.god.kahit.database.DatabaseHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that acts as a adapter between the rest of the model and the database implementation
 */
class QuestionDataLoader {
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDB;

    QuestionDataLoader(Context context){
        mDBHelper = new DatabaseHelper(context);
        try {
            mDBHelper.updateDataBase();
        }catch (IOException e){
            e.printStackTrace();
        }
        mDB = mDBHelper.getReadableDatabase();
    }

    /**
     * Method that loads all questions from a given table from the database
     * @param category the category that decides from which table the questions are pulled from
     * @return the list with all of the questions
     */
    List<Question> getQuestion(Category category){
        Cursor resultSet = mDB.rawQuery("SELECT * from test",null);
        resultSet.moveToFirst();
        List<Question> questions = new ArrayList<>();

        do {
            List<String> alts = new ArrayList<>();
            alts.add(resultSet.getString(3));
            alts.add(resultSet.getString(4));
            alts.add(resultSet.getString(5));
            alts.add(resultSet.getString(6));
            Question q = new Question(category, resultSet.getString(1), resultSet.getString(3), alts, resultSet.getInt(2));
            questions.add(q);
        }while (resultSet.move(1)); //move moves relative to the current position and returns false if it cant reach that pos
        resultSet.close();
        return questions;
    }
}
