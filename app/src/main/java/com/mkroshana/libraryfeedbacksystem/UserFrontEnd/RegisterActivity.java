package com.mkroshana.libraryfeedbacksystem.UserFrontEnd;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import com.mkroshana.libraryfeedbacksystem.AdminFrontEnd.AdminLoginActivity;
import com.mkroshana.libraryfeedbacksystem.BackEnd.SharedPreferenceClass;
import com.mkroshana.libraryfeedbacksystem.BackEnd.UtilityService;
import com.mkroshana.libraryfeedbacksystem.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity
{
    private Button RegisterButton;
    private EditText Name_ET, LibraryNumber_ET;
    private ImageButton ImageLogout;
    ProgressBar ProgressBar;
    private String Name, LibraryNumber;
    UtilityService utilityService;
    SharedPreferenceClass sharedPreferenceClass;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ImageLogout = findViewById(R.id.imgLogout);
        RegisterButton = findViewById(R.id.btnRegister);
        Name_ET = findViewById(R.id.txtName);
        LibraryNumber_ET = findViewById(R.id.txtLibraryNumber);
        ProgressBar = findViewById(R.id.progressBar);
        utilityService = new UtilityService();
        sharedPreferenceClass = new SharedPreferenceClass(this);

        utilityService.ImmersiveFSMode(this);

        ImageLogout.setOnClickListener(new View.OnClickListener()
        {
            /**
             * Logout from the admin account when in the user registration so the avarage user can't
             * sees the analytics
             * @param v
             */
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("Warning !");
                builder.setMessage("Are you sure want Logout from here ??");

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
                        startActivity(new Intent(RegisterActivity.this, AdminLoginActivity.class));
                        sharedPreferenceClass.adminClear();
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        RegisterButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                utilityService.hideKeyboard(v, RegisterActivity.this);
                Name = Name_ET.getText().toString();
                LibraryNumber = LibraryNumber_ET.getText().toString();

                if (ValidateRegisterUser(v))
                {
                    RegisterUser(v);
                }
            }
        });
    }

    /**
     * Registering user using the API and JSON requests
     * @param v
     */
    private void RegisterUser(View v)
    {
        ProgressBar.setVisibility(View.VISIBLE);
        final HashMap <String, String> params = new HashMap<>();
        params.put("name", Name);
        params.put("librarynumber", LibraryNumber);

        String APIKey = "https://library-feedback-system.herokuapp.com/api/library/auth/user";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                APIKey, new JSONObject(params), new Response.Listener<JSONObject>()
        {
            /**
             * On a successful register response proceeds to next step
             * @param response
             */
            @Override
            public void onResponse(JSONObject response)
            {
                try {
                    if (response.getBoolean("success"))
                    {
                        String token = response.getString("token");
                        sharedPreferenceClass.setValue_string("token", token);
                        startActivity(new Intent(RegisterActivity.this, Quiz1Activity.class));
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
                        Toast.makeText(RegisterActivity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
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
     * Validate if the register details are entered or not
     * @param view
     * @return validation true or false
     */
    public boolean ValidateRegisterUser(View view)
    {
        boolean isValid;
        if (!TextUtils.isEmpty(Name))
        {
            if (!TextUtils.isEmpty(LibraryNumber))
            {
                isValid = true;
            }
            else
            {
                Snackbar.make(view, "Please enter your Library Number", Snackbar.LENGTH_SHORT).show();
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