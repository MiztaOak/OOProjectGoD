package com.god.kahit.databaseService;

import android.content.Context;
import android.util.Log;

import com.god.kahit.model.Category;
import com.god.kahit.model.IQuestionDataLoader;
import com.god.kahit.model.Question;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;
import static com.google.firebase.firestore.FirebaseFirestore.getInstance;


/**
 * A helper class for the Firebase database
 */
public class QuestionDataLoaderRealtime implements IQuestionDataLoader {
    private FirebaseDatabase db;
    private DatabaseReference databaseReference;
    private Map<Category,List<Question>>  questions;

    public QuestionDataLoaderRealtime(Context context){
        FirebaseApp.initializeApp(context);
        db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference("questions");
        questions = new HashMap<>();
        loadData();
    }

    private void loadData(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                questions = new HashMap<>();

                for(DataSnapshot categoryData: dataSnapshot.getChildren()){
                    Category current = Category.getCategoryByString(categoryData.getKey());
                    List<Question> questionOfCurrent = new ArrayList<>();
                    for(DataSnapshot questionData: categoryData.getChildren()){
                        questionOfCurrent.add(getQuestion(questionData,current));
                    }
                    questions.put(current,questionOfCurrent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value", databaseError.toException());
            }
        });
    }

    /**
     * Method that returns a list of questions based on a given category
     * @param category the category that decides from which table the questions are pulled from
     * @return the list with all of the questions
     */
    @Override
    public  List<Question> getQuestionList(final Category category) {
        if(questions.isEmpty()){
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(questions.get(category) != null){
            return questions.get(category);
        }
        else{
            return new ArrayList<>();
        }
    }

    /**
     * Method that uses the values of a given DocumentSnapshot to create a single question
     * @param document the document that is pointing at the data
     * @param category the category of the question
     * @return the created question
     */
    private Question getQuestion(DataSnapshot document, Category category){
       String question = (String) document.child("question").getValue();
       String answer = (String) document.child("answer").getValue();
       int time = ((Long)document.child("time").getValue()).intValue();
       List<String> alts = (List<String>) document.child("alts").getValue();
       alts.add(answer);

       return new Question(category,question,answer,alts,time);
    }
}
