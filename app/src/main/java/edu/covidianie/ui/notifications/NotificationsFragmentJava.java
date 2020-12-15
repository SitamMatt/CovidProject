package edu.covidianie.ui.notifications;


import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.covidianie.R;

public class NotificationsFragmentJava extends Fragment {

    private NotificationsViewModelJava mViewModel;
    private List<Integer> answers;
    private List<Integer> radioGroupIDs;
    private Button submit_button;
    private boolean filled;
    private TextView result;


    public static NotificationsFragmentJava newInstance() {
        return new NotificationsFragmentJava();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        answers = new ArrayList<>();
        radioGroupIDs = new ArrayList<>();
        filled = false;
        submit_button = new Button(getContext());
        submit_button.setText(R.string.submit);
        result = new TextView(getContext());
        result.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(NotificationsViewModelJava.class);
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
            LinearLayout questions = getView().findViewById(R.id.question_list_linear_layout);
            if(questions.getChildCount() > 0)
                questions.removeAllViews();
            String result_percent = String.valueOf(Math.round(mViewModel.calculate(answers) * 100));
            result.setText(getResources().getString(R.string.result_title) + ' ' + result_percent + '%');
            questions.addView(result);
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