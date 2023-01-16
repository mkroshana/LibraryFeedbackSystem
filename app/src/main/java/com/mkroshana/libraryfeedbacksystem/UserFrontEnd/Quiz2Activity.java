package com.mkroshana.libraryfeedbacksystem.UserFrontEnd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mkroshana.libraryfeedbacksystem.R;
import com.mkroshana.libraryfeedbacksystem.BackEnd.SharedPreferenceClass;
import com.mkroshana.libraryfeedbacksystem.BackEnd.UtilityService;

public class Quiz2Activity extends AppCompatActivity
{
    private Button NextButton, ExitButton;
    private RadioGroup radioQ4Group, radioQ5Group, radioQ6Group;
    private RadioButton radioQ4Button, radioQ5Button, radioQ6Button;
    public String Answer04, Answer05, Answer06;
    SharedPreferenceClass sharedPreferenceClass;
    UtilityService utilityService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz2);
        addListenerOnButton();
        sharedPreferenceClass = new SharedPreferenceClass(this);
        utilityService = new UtilityService();

        utilityService.ImmersiveFSMode(this);
    }

    private void addListenerOnButton()
    {
        radioQ4Group = findViewById(R.id.radioQ4);
        radioQ5Group = findViewById(R.id.radioQ5);
        radioQ6Group = findViewById(R.id.radioQ6);
        NextButton = findViewById(R.id.btnNext);
        ExitButton = findViewById(R.id.btnExit);

        NextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (validate())
                {
                    saveResults();
                }
            }
        });

        ExitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sharedPreferenceClass.clear();
                startActivity(new Intent(Quiz2Activity.this, Quiz1Activity.class));
            }
        });
    }

    /**
     * Save all the selected answers to the internal device storage
     */
    public void saveResults ()
    {
        //Q04
        int selectedID4 = radioQ4Group.getCheckedRadioButtonId();
        radioQ4Button = findViewById(selectedID4);
        Answer04 = (String) radioQ4Button.getText();
        sharedPreferenceClass.setValue_string("answer04", Answer04);

        //Q05
        int selectedID5 = radioQ5Group.getCheckedRadioButtonId();
        radioQ5Button = findViewById(selectedID5);
        Answer05 = (String) radioQ5Button.getText();
        sharedPreferenceClass.setValue_string("answer05", Answer05);

        //Q06
        int selectedID6 = radioQ6Group.getCheckedRadioButtonId();
        radioQ6Button = findViewById(selectedID6);
        Answer06 = (String) radioQ6Button.getText();
        sharedPreferenceClass.setValue_string("answer06", Answer06);

        startActivity(new Intent(Quiz2Activity.this, Quiz3Activity.class));
    }

    /**
     * Validate if every question is answered
     * @return validation true or false
     */
    public boolean validate()
    {
        boolean isValid;
        if ((radioQ4Group.getCheckedRadioButtonId() != -1))
        {
            if ((radioQ5Group.getCheckedRadioButtonId() != -1))
            {
                if ((radioQ6Group.getCheckedRadioButtonId() != -1))
                {
                    isValid = true;
                }
                else
                {
                    Toast.makeText(Quiz2Activity.this, "Question 6 isn't Answered", Toast.LENGTH_SHORT).show();
                    isValid = false;
                }
            }
            else
            {
                Toast.makeText(Quiz2Activity.this, "Question 5 isn't Answered", Toast.LENGTH_SHORT).show();
                isValid = false;
            }
        }
        else
        {
            Toast.makeText(Quiz2Activity.this, "Question 4 isn't Answered", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        return isValid;
    }
}