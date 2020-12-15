package edu.covidianie.ui.notifications;

import android.arch.lifecycle.ViewModel;
import android.content.res.Resources;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
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
    static double ERROR_OF_MEASUREMENT = 0.85;
    List<List<Double>> result_value = new ArrayList<>();
    private Resources appResources;

    List<String> getQuestionList()
    {
        InputStream ins = appResources.openRawResource(R.raw.questions_data);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(ins, Charset.forName("UTF-8")));
        ArrayList<String> questions = new ArrayList<>();
        String line;
        try{
            while( (line = reader.readLine()) != null) {
                questions.add(line);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return questions;
    }
    HashMap<Integer, List<String>> getAnswers()
    {
        result_value.clear();
        HashMap<Integer,List<String>> result = new HashMap<Integer, List<String>>();
        InputStream ins = appResources.openRawResource(R.raw.answers_data);
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
    Double calculate(@NotNull List<Integer> answers_results)
    {
        int counter = 0;
        double sum = 0.0;
        for(int i : answers_results){
            sum += result_value.get(counter).get(i);
            counter++;
            //comment
        }
        return (sum/counter) * ERROR_OF_MEASUREMENT;
    }

    public void setAppResources(Resources appResources) {
        this.appResources = appResources;
    }
}