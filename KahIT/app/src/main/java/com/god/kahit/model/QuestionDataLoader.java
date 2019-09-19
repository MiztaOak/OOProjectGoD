package com.god.kahit.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class QuestionDataLoader {
    private Scanner in;
    QuestionDataLoader(String fileName){
        try{
            in = new Scanner(new FileReader(fileName));
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    Question getQuestion(Category category){
        String s = "";
        if(in.hasNext()){
            s = in.next();
        }else{
            in.close();
        }
        int index = 0;
        List<String> data = new ArrayList<>();
        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i) == ';'){
                data.add(s.substring(index,i));
                index = i+2;
            }
        }
        String q = data.get(0),a = data.get(2);
        int t = Integer.parseInt(data.get(0));
        List<String> alts = data.subList(2,data.size()-1);

        Question question = new Question(category,q,a,alts,t);
        return question;
    }
}
