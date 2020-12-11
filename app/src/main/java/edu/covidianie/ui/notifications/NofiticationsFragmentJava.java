package edu.covidianie.ui.notifications;

import android.arch.lifecycle.ViewModelProvider;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.covidianie.R;

public class NofiticationsFragmentJava extends Fragment {

    private NofiticationsViewModelJava mViewModel;
    private List<Integer> answers;
    private List<Integer> radioGroupIDs;
    private Button submit_button;
    private boolean filled;


    public static NofiticationsFragmentJava newInstance() {
        return new NofiticationsFragmentJava();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        answers = new ArrayList<>();
        radioGroupIDs = new ArrayList<>();
        filled = false;
        submit_button = new Button(getContext());
        submit_button.setText(R.string.submit);
        return inflater.inflate(R.layout.nofitications_fragment, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(NofiticationsViewModelJava.class);
        // TODO: Use the ViewModel
        mViewModel.setAppResources(getResources());
        setupQuestions();
        submit_button.setOnClickListener(new SubmitListener());

    }

    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    void setupQuestions() {
        LinearLayout questions = getView().findViewById(R.id.question_list_linear_layout);
        HashMap<Integer, List<String>> answers = mViewModel.getAnswers();
        int question_counter = 0;
        AnswersFilledListener answersFilledListener = new AnswersFilledListener();
        for (String s : mViewModel.getQuestionList()) {
            TextView tv = new TextView(getContext());
            tv.setText(s);
            questions.addView(tv);
            question_counter ++;
            RadioGroup answers_set = new RadioGroup(getContext());
            answers_set.setId(View.generateViewId());
            answers_set.setOnCheckedChangeListener(answersFilledListener);
            radioGroupIDs.add(answers_set.getId());
            for(String answer_text : answers.get(question_counter)){
                RadioButton answer = new RadioButton(getContext());
                answer.setText(answer_text);
                answers_set.addView(answer);
            }
            questions.addView(answers_set);
        }
        questions.addView(submit_button);
    }
    int getSelectedRadioButton(int radio_group_id){
        RadioGroup rg = getView().findViewById(radio_group_id);
        return rg.indexOfChild(getView().findViewById(rg.getCheckedRadioButtonId()));
    }
    class SubmitListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if(!filled)
                return;
            for(int id : radioGroupIDs){
                answers.add(getSelectedRadioButton(id));
            }
            submit_button.setText(mViewModel.calculate(answers).toString());
            answers.clear();
        }
    }
    class AnswersFilledListener implements RadioGroup.OnCheckedChangeListener{
        private Set<Integer>  checkedRadioGroupIDs = new HashSet<>();
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            checkedRadioGroupIDs.add(group.getId());
            if(checkedRadioGroupIDs.size() == radioGroupIDs.size())
                filled = true;
        }
    }
}