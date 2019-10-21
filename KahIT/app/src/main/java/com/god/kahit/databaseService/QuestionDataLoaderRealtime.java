package com.god.kahit.databaseService;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.god.kahit.R;
import com.god.kahit.model.Category;
import com.god.kahit.model.IQuestionDataLoader;
import com.god.kahit.model.Question;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

/**
 * A helper class for the Firebase realtime database, that loads the question data from the database and
 * and stores it inside of a map similar to the one found in QuizGame
 *
 * used by: Repository
 *
 * @author Johan Ek
 */
public class QuestionDataLoaderRealtime implements IQuestionDataLoader {
    private FirebaseDatabase db;
    private DatabaseReference databaseReference;
    private Map<Category,List<Question>>  questions;

    private Toast errorToast;
    private Toast succesToast;

    @SuppressLint("ShowToast")
    public QuestionDataLoaderRealtime(Context context){
        FirebaseApp.initializeApp(context);
        db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference("questions");


        errorToast = Toast.makeText(context, context.getString(R.string.databaseError),Toast.LENGTH_LONG);
        succesToast = Toast.makeText(context, context.getString(R.string.databaseConnect),Toast.LENGTH_LONG);

        questions = new HashMap<>();
        loadData();
    }

    /**
     * Method that attaches a onDataChange listener to fetch the data from the database
     */
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
                errorToast.show();
            }
        });

        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean connected = dataSnapshot.getValue(Boolean.class);
                if(connected){
                    succesToast.show();
                }else{
                    errorToast.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
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
            errorToast.show();
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
       int time = ((Long) Objects.requireNonNull(document.child("time").getValue())).intValue();
       List<String> alts = new ArrayList<>();
       if(document.child("alts").getValue() instanceof List){
           for(Object alt: (List<?>) Objects.requireNonNull(document.child("alts").getValue())){
               if(alt instanceof String){
                   alts.add((String) alt);
               }
           }

       }
       Objects.requireNonNull(alts).add(answer);
       return new Question(category,question,answer,alts,time);
    }
}
