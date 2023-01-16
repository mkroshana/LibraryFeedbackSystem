package com.mkroshana.libraryfeedbacksystem.UserFrontEnd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mkroshana.libraryfeedbacksystem.R;
import com.mkroshana.libraryfeedbacksystem.BackEnd.SharedPreferenceClass;
import com.mkroshana.libraryfeedbacksystem.BackEnd.UtilityService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Quiz3Activity extends AppCompatActivity
{
    private Button FinishButton;
    private RadioGroup radioQ7Group;
    private RadioButton radioQ7Button;
    public String Answer07;
    SharedPreferenceClass sharedPreferenceClass;
    UtilityService utilityService;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz3);
        addListenerOnButton();
        sharedPreferenceClass = new SharedPreferenceClass(this);
        utilityService = new UtilityService();

        utilityService.ImmersiveFSMode(this);
        token = sharedPreferenceClass.getValue_string("token");
    }

    private void addListenerOnButton()
    {
        radioQ7Group = findViewById(R.id.radioQ7);
        FinishButton = findViewById(R.id.btnFinish);


        FinishButton.setOnClickListener(new View.OnClickListener()
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
    }

    /**
     * Save the selected answer to the internal device storage
     */
    public void saveResults ()
    {
        //Q07
        int selectedID7 = radioQ7Group.getCheckedRadioButtonId();
        radioQ7Button = findViewById(selectedID7);
        Answer07 = (String) radioQ7Button.getText();
        sharedPreferenceClass.setValue_string("answer07", Answer07);

        SendAnswers();
    }

    /**
     * Validate if the question is answered
     * @return validation true or false
     */
    public boolean validate()
    {
        boolean isValid;
        if ((radioQ7Group.getCheckedRadioButtonId() != -1))
        {
            isValid = true;
        }
        else
        {
            Toast.makeText(Quiz3Activity.this, "Question 7 isn't Answered", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        return isValid;
    }

    /**
     * Retrieve all the stored answers and put into a JSON request to send to the database
     * through the API
     */
    private void SendAnswers()
    {
        String url = "https://library-feedback-system.herokuapp.com/api/library/feedback";
        HashMap <String, String> body = new HashMap<>();
        body.put("answer01", sharedPreferenceClass.getValue_string("answer01"));
        body.put("answer02", sharedPreferenceClass.getValue_string("answer02"));
        body.put("answer03", sharedPreferenceClass.getValue_string("answer03"));
        body.put("answer04", sharedPreferenceClass.getValue_string("answer04"));
        body.put("answer05", sharedPreferenceClass.getValue_string("answer05"));
        body.put("answer06", sharedPreferenceClass.getValue_string("answer06"));
        body.put("answer07", sharedPreferenceClass.getValue_string("answer07"));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(body), new Response.Listener<JSONObject>()
        {
            /**
             * On a successful response shows a toast saying successfully saved
             * @param response
             */
            @Override
            public void onResponse(JSONObject response)
            {
                try {
                    if (response.getBoolean("success"))
                    {
                        Toast.makeText(Quiz3Activity.this, "Answers saved Successfully", Toast.LENGTH_SHORT).show();
                        sharedPreferenceClass.clear();
                        startActivity(new Intent(Quiz3Activity.this, RegisterActivity.class));
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            /**
             * Returns an error if a server or a network error occurs and shows it as a toast message
             * @param error
             */
            @Override
            public void onErrorResponse(VolleyError error)
            {
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null)
                {
                    try
                    {
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));

                        JSONObject obj = new JSONObject(res);
                        Toast.makeText(Quiz3Activity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                    catch (JSONException | UnsupportedEncodingException je)
                    {
                        je.printStackTrace();
                    }
                }
            }
        })
        {
            /**
             * Returns the json type requires to pass admin details to the database through the API
             * @return
             * @throws AuthFailureError
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", token);
                return headers;
            }
        };

        //Setting the retry policies
        int socketTime = 3000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTime,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        //Adding the request using volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}