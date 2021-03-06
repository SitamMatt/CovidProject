package edu.covidianie.ui.notifications;


import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
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
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.FragmentNavigator;

import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import edu.covidianie.R;
import edu.covidianie.ui.notifications.game.GameView;

public class NotificationsFragmentJava extends Fragment {

    private NotificationsViewModelJava mViewModel;
    private List<Integer> answers;
    private List<Integer> radioGroupIDs;
    private Button submit_button;
    private boolean filled;
    private TextView result;
    private AnimatedPieView pieChart;
    private GameView gameView;
    private Button start_game_button;

    private Vibrator vibrator;


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
        pieChart = new AnimatedPieView(getContext());
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
        vibrator = (Vibrator) Objects.requireNonNull(getActivity()).getSystemService(Context.VIBRATOR_SERVICE);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    void setupQuestions() {
        TextView title = getView().findViewById(R.id.not_Title_textview);
        title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        LinearLayout questions = getView().findViewById(R.id.question_list_linear_layout);
        HashMap<Integer, List<String>> answers = mViewModel.getAnswers();
        int question_counter = 0;
        AnswersFilledListener answersFilledListener = new AnswersFilledListener();
        for (String s : mViewModel.getQuestionList()) {
            TextView tv = (TextView)getLayoutInflater().inflate(R.layout.question_template, null);
            tv.setText(s);

            questions.addView(tv);
            question_counter++;
            RadioGroup answers_set = (RadioGroup)getLayoutInflater().inflate(R.layout.answers_set_template,null);
            answers_set.setId(View.generateViewId());
            answers_set.setOnCheckedChangeListener(answersFilledListener);
            radioGroupIDs.add(answers_set.getId());
            for (String answer_text : answers.get(question_counter)) {
                RadioButton answer = (RadioButton)getLayoutInflater().inflate(R.layout.answer_template,null);
//                answer.setScaleX(0.5f);
//                answer.setScaleY(0.5f);
                answer.setText(answer_text);
//                answer.setTextSize(26);
                answers_set.addView(answer);
            }
            questions.addView(answers_set);
//            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)answers_set.getLayoutParams();
//            params.setMargins(30, 0, 0, 0); //substitute parameters for left, top, right, bottom
//            answers_set.setLayoutParams(params);
        }
        questions.addView(submit_button);

    }

    int getSelectedRadioButton(int radio_group_id) {
        RadioGroup rg = getView().findViewById(radio_group_id);
        return rg.indexOfChild(getView().findViewById(rg.getCheckedRadioButtonId()));
    }

    void pieChartInvoke(long value) {
        AnimatedPieViewConfig config = new AnimatedPieViewConfig();
        int chartColor = Color.parseColor("#FF6df23d");
        if (value > 30)
            chartColor = Color.parseColor("#FF2bc23f");
        if (value > 50)
            chartColor = Color.parseColor("#FFe3c010");
        if (value > 80)
            chartColor = Color.parseColor("#FFe34510");
        config.addData(new SimplePieInfo(value, chartColor, String.valueOf(value) + "%"));
        config.addData(new SimplePieInfo(100 - value, Color.parseColor("#20888888")));
        config.drawText(false);
        config.textSize(25);
        config.canTouch(false);
        config.duration(1000);
        pieChart.applyConfig(config);
        pieChart.start();
        pieChart.setVisibility(View.VISIBLE);
    }

    class SubmitListener implements View.OnClickListener {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public void onClick(View v) {
            if (!filled)
                return;
            for (int id : radioGroupIDs) {
                answers.add(getSelectedRadioButton(id));
            }
            LinearLayout questions = getView().findViewById(R.id.question_list_linear_layout);
            if (questions.getChildCount() > 0)
                questions.removeAllViews();
            long result_percent = Math.round(mViewModel.calculate(answers) * 100);
            result.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            result.setText(getResources().getString(R.string.result_title) + "\n" + String.valueOf(result_percent) + "%");
            questions.addView(result);
            pieChartInvoke(result_percent);
            questions.addView(pieChart);
            answers.clear();
            start_game_button = new Button(getContext());
            start_game_button.setText(R.string.play_mini_game);
            start_game_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (questions.getChildCount() > 0)
                    questions.removeAllViews();
                    gameView = (GameView)getLayoutInflater().inflate(R.layout.minigame_window_template,null);
                   // gameView.setMinimumHeight(350);
                    //gameView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    gameView.setVibrator(vibrator);
                    questions.addView(gameView);
                }
            });
            questions.addView(start_game_button);
        }
    }

    class AnswersFilledListener implements RadioGroup.OnCheckedChangeListener {
        private Set<Integer> checkedRadioGroupIDs = new HashSet<>();

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            checkedRadioGroupIDs.add(group.getId());
            if (checkedRadioGroupIDs.size() == radioGroupIDs.size())
                filled = true;
        }
    }
}