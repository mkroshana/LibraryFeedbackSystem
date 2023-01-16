package com.mkroshana.libraryfeedbacksystem.UserFrontEnd;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mkroshana.libraryfeedbacksystem.AdminFrontEnd.AdminLoginActivity;
import com.mkroshana.libraryfeedbacksystem.R;
import com.mkroshana.libraryfeedbacksystem.BackEnd.SharedPreferenceClass;
import com.mkroshana.libraryfeedbacksystem.BackEnd.UtilityService;

public class Quiz1Activity extends AppCompatActivity
{
    private Button NextButton, ExitButton;
    private RadioGroup radioQ1Group, radioQ2Group, radioQ3Group;
    private RadioButton radioQ1Button, radioQ2Button, radioQ3Button;
    public String Answer01, Answer02, Answer03;
    SharedPreferenceClass sharedPreferenceClass;
    UtilityService utilityService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz1);
        addListenerOnButton();
        sharedPreferenceClass = new SharedPreferenceClass(this);
        utilityService = new UtilityService();

        utilityService.ImmersiveFSMode(this);
    }

    private void addListenerOnButton()
    {
        radioQ1Group = findViewById(R.id.radioQ1);
        radioQ2Group = findViewById(R.id.radioQ2);
        radioQ3Group = findViewById(R.id.radioQ3);
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
            /**
             * Exit when feedback in progress
             * Asks confirmation using an android alert dialog
             * @param v
             */
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(Quiz1Activity.this);
                builder.setTitle("You won't be able give feedback again after you exit !");
                builder.setMessage("Are you sure want exit ?");

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        sharedPreferenceClass.clear();
                        startActivity(new Intent(Quiz1Activity.this, RegisterActivity.class));
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    /**
     * Save all the selected answers to the internal device storage
     */
    public void saveResults ()
    {
        //Q01
        int selectedID1 = radioQ1Group.getCheckedRadioButtonId();
        radioQ1Button = findViewById(selectedID1);
        Answer01 = (String) radioQ1Button.getText();
        sharedPreferenceClass.setValue_string("answer01", Answer01);

        //Q02
        int selectedID2 = radioQ2Group.getCheckedRadioButtonId();
        radioQ2Button = findViewById(selectedID2);
        Answer02 = (String) radioQ2Button.getText();
        sharedPreferenceClass.setValue_string("answer02", Answer02);

        //Q03
        int selectedID3 = radioQ3Group.getCheckedRadioButtonId();
        radioQ3Button = findViewById(selectedID3);
        Answer03 = (String) radioQ3Button.getText();
        sharedPreferenceClass.setValue_string("answer03", Answer03);

        startActivity(new Intent(Quiz1Activity.this, Quiz2Activity.class));
    }

    /**
     * Validate if every question is answered
     * @return validation true or false
     */
    public boolean validate()
    {
        boolean isValid;
        if ((radioQ1Group.getCheckedRadioButtonId() != -1))
        {
            if ((radioQ2Group.getCheckedRadioButtonId() != -1))
            {
                if ((radioQ3Group.getCheckedRadioButtonId() != -1))
                {
                    isValid = true;
                }
                else
                {
                    Toast.makeText(Quiz1Activity.this, "Question 3 isn't Answered", Toast.LENGTH_SHORT).show();
                    isValid = false;
                }
            }
            else
            {
                Toast.makeText(Quiz1Activity.this, "Question 2 isn't Answered", Toast.LENGTH_SHORT).show();
                isValid = false;
            }
        }
        else
        {
            Toast.makeText(Quiz1Activity.this, "Question 1 isn't Answered", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        return isValid;
    }
}