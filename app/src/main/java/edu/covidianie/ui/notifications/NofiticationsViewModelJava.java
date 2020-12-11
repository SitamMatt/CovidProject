package edu.covidianie.ui.notifications;

import android.arch.lifecycle.ViewModel;
import android.content.res.Resources;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import edu.covidianie.R;

public class NofiticationsViewModelJava extends ViewModel {

    List<List<Double>> result_value = new ArrayList<>();
    private Resources appResources;

    List<String> getQuestionList()
    {
        String questions_data = "Treść pytania pierwszego;Treść pytania drugiego;Treść pytania trzeciego";
        String[] questions = questions_data.split(";");
        return Arrays.asList(questions);
    }
    HashMap<Integer, List<String>> getAnswers()
    {
        result_value.clear();
        HashMap<Integer,List<String>> result = new HashMap<Integer, List<String>>();
        String answers_data = "Odpowiedź 1a;Odpowiedź 1b;Odpowiedź 1c\n" +
                                "Odpowiedź 2a;Odpowiedź 2b;Odpowiedź 2c\n" +
                                "Odpowiedź 3a;Odpowiedź 3b;Odpowiedź 3c";
        List<List<String>> records = new ArrayList<>();
        InputStream ins = appResources.openRawResource(R.raw.data);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(ins, Charset.forName("UTF-8")));
        String line = "";
        try {
            int key = 1;
            while( (line = reader.readLine()) != null){
                String[] data_set = line.split(";");
                List<String> answers = new ArrayList<>(Arrays.asList(data_set).subList(0, data_set.length / 2));
                result.put(key, answers);
                key++;
                List<Double> values_set = new ArrayList<>();
                for(int i=data_set.length/2;i<data_set.length;i++){
                    values_set.add(Double.parseDouble(data_set[i]));
                }
                result_value.add(values_set);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
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
    private List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<String>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(";");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }

    public void setAppResources(Resources appResources) {
        this.appResources = appResources;
    }
}