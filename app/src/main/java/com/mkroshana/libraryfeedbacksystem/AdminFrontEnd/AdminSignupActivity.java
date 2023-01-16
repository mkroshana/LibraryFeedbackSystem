package com.mkroshana.libraryfeedbacksystem.AdminFrontEnd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.google.android.material.snackbar.Snackbar;
import com.mkroshana.libraryfeedbacksystem.BackEnd.UtilityService;
import com.mkroshana.libraryfeedbacksystem.R;
import com.mkroshana.libraryfeedbacksystem.UserFrontEnd.Quiz1Activity;
import com.mkroshana.libraryfeedbacksystem.UserFrontEnd.RegisterActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class AdminSignupActivity extends AppCompatActivity
{

    UtilityService utilityService;
    private String Username, Email, Password;
    ProgressBar ProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signup);

        EditText AdminRegName = findViewById(R.id.txtRegUsername);
        EditText AdminRegEmail = findViewById(R.id.txtRegEmail);
        EditText AdminRegPassword = findViewById(R.id.txtRegPassword);
        Button AdminRegButton = findViewById(R.id.btnAdminReg);
        ProgressBar = findViewById(R.id.progressBar);
        utilityService = new UtilityService();

        utilityService.ImmersiveFSMode(this);

        AdminRegButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                utilityService.hideKeyboard(v, AdminSignupActivity.this);
                Username = AdminRegName.getText().toString();
                Email = AdminRegEmail.getText().toString();
                Password = AdminRegPassword.getText().toString();

                if (ValidateRegisterAdmin(v))
                {
                    RegisterAdmin(v);
                }
            }
        });
    }

    /**
     * Method for creating an admin account using the API and JSON requests
     * @param v
     */
    private void RegisterAdmin(View v)
    {
        ProgressBar.setVisibility(View.VISIBLE);
        final HashMap<String, String> params = new HashMap<>();
        params.put("username", Username);
        params.put("email", Email);
        params.put("password", Password);

        String APIKey = "https://library-feedback-system.herokuapp.com/api/library/auth/admin/register";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, APIKey, new JSONObject(params), new Response.Listener<JSONObject>()
        {
            /**
             * On a successful register response shows a toast saying successfully registered
             * @param response
             */
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    if (response.getBoolean("success"))
                    {
                        startActivity(new Intent(AdminSignupActivity.this, AdminDashboardActivity.class));
                        Toast.makeText(AdminSignupActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                    }
                    ProgressBar.setVisibility(View.GONE);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    ProgressBar.setVisibility(View.GONE);
                }
            }
        },new Response.ErrorListener()
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
                        Toast.makeText(AdminSignupActivity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
                        ProgressBar.setVisibility(View.GONE);
                    }
                    catch (JSONException | UnsupportedEncodingException je)
                    {
                        je.printStackTrace();
                        ProgressBar.setVisibility(View.GONE);
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

                return params;
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

    /**
     * Validate if the Admin account details are entered or not
     * @param view
     * @return validation true or false
     */
    public boolean ValidateRegisterAdmin(View view)
    {
        boolean isValid;
        if (!TextUtils.isEmpty(Username))
        {
            if (!TextUtils.isEmpty(Email))
            {
                if (!TextUtils.isEmpty(Password))
                {
                    isValid = true;
                }
                else
                {
                    Snackbar.make(view, "Please enter a Password", Snackbar.LENGTH_SHORT).show();
                    //utilityService.showSnackBar(view, "Please enter your Library Number");
                    isValid = false;
                }
            }
            else
            {
                Snackbar.make(view, "Please enter a Email Address", Snackbar.LENGTH_SHORT).show();
                //utilityService.showSnackBar(view, "Please enter your Library Number");
                isValid = false;
            }
        }
        else
        {
            Snackbar.make(view, "Please enter your Name", Snackbar.LENGTH_SHORT).show();
            //utilityService.showSnackBar(view, "Please enter your Name");
            isValid = false;

        }
        return isValid;
    }

}