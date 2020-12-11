package edu.covidianie.ui.notifications;

import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.covidianie.R;

public class NofiticationsViewModelJava extends ViewModel {

    List<List<Double>> result_value = new ArrayList<>();

    List<String> getQuestionList()
    {
        String questions_data = "Treść pytania pierwszego;Treść pytania drugiego;Treść pytania trzeciego";
        String[] questions = questions_data.split(";");
        return Arrays.asList(questions);
    }
    HashMap<Integer, List<String>> getAnswers()
    {
        String answers_data = "Odpowiedź 1a;Odpowiedź 1b;Odpowiedź 1c\n" +
                                "Odpowiedź 2a;Odpowiedź 2b;Odpowiedź 2c\n" +
                                "Odpowiedź 3a;Odpowiedź 3b;Odpowiedź 3c";
        String[] answers = answers_data.split("\n");
        int counter = 1;
        HashMap<Integer,List<String>> result = new HashMap<Integer, List<String>>();
        for(String row : answers){
            result.put(counter,
                    Arrays.asList(row.split(";")));
            counter ++;
        }
        getAnswersValue();
        return result;
    }
    void getAnswersValue(){
        String value_data = "0.0;0.5;1.0\n" +
                            "0.5;1.0;0.0\n" +
                            "1.0;0.0;0.5\n";

        for(String value_row : value_data.split("\n")){
            List<Double> values = new ArrayList<>();
            for(String value_str : value_row.split(";")){
                values.add(Double.parseDouble(value_str));
            }
            result_value.add(values);
        }

    }
    Double calculate(List<Integer> answers_results)
    {
        int counter = 0;
        double sum = 0.0;
        for(int i : answers_results){
            sum += result_value.get(counter).get(i);
            counter++;
        }
        return sum/counter;
    }



    // TODO: Implement the ViewModel
}