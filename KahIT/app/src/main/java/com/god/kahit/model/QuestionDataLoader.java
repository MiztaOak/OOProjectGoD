package com.god.kahit.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class QuestionDataLoader {
    private Scanner in;

    QuestionDataLoader(){
        try{
            in = new Scanner(new FileReader("questionDataBase.txt"));
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    Question getQuestion(){
        String s;
        if(in.hasNext()){
            s = in.next();
        }else{
            in.close();
        }
        String q,a;
        int t;
        List<String> alts = new ArrayList<>();
    }
}
