package com.mkroshana.libraryfeedbacksystem.AdminFrontEnd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.mkroshana.libraryfeedbacksystem.BackEnd.AnswersModel;
import com.mkroshana.libraryfeedbacksystem.R;
import com.mkroshana.libraryfeedbacksystem.UserFrontEnd.RegisterActivity;
import com.mkroshana.libraryfeedbacksystem.BackEnd.SharedPreferenceClass;
import com.mkroshana.libraryfeedbacksystem.BackEnd.UtilityService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Roshana Perera
 * Related code parts are taken from belowlinks and designed and improved upon them
 * GitHub - ajitpradhan925/todo_android_yt_api - https://github.com/ajitpradhan925/todo_android_yt_api
 * GitHub - ajitpradhan925/todo_android_yt - https://github.com/ajitpradhan925/todo_android_yt
 * YouTube - https://www.youtube.com/watch?v=5HpCkJ3DbJU&list=PLVRD5onwuyGAH2ZUquy5OkexxByzGUZ0G
 */

public class AdminDashboardActivity extends AppCompatActivity {
    SharedPreferenceClass sharedPreferenceClass;
    UtilityService utilityService;
    ArrayList<AnswersModel> arrayList;
    ProgressBar ProgressBar;
    private TextView txtBookFeed;
    private TextView txtAgeFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        AnyChartView anyChartView1 = findViewById(R.id.any_chart_view1);
        AnyChartView anyChartView2 = findViewById(R.id.any_chart_view2);
        anyChartView2.setProgressBar(findViewById(R.id.chartloadingbar));
        ProgressBar = findViewById(R.id.chartloadingbar);
        Button logout = findViewById(R.id.btnLogout);
        Button switchToFeedback = findViewById(R.id.btnFeedbackView);
        Button AdminRegUI = findViewById(R.id.btnAdminRegUI);
        TextView txtWelcome = findViewById(R.id.txtWelcome);
        TextView txtTotalFeed = findViewById(R.id.txtFeedbackRec);
        txtBookFeed = findViewById(R.id.txtFeedbackBook);
        txtAgeFeed = findViewById(R.id.txtFeedbackAge);

        utilityService = new UtilityService();
        sharedPreferenceClass = new SharedPreferenceClass(this);
        utilityService.ImmersiveFSMode(this);

        String adminUsername = sharedPreferenceClass.getValue_string("adminUsername");
        txtWelcome.setText("Welcome, " + adminUsername);

        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sharedPreferenceClass.adminClear();
                startActivity(new Intent(AdminDashboardActivity.this, AdminLoginActivity.class));
                finish();
            }
        });

        switchToFeedback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(AdminDashboardActivity.this, RegisterActivity.class));
                finish();
            }
        });

        AdminRegUI.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(AdminDashboardActivity.this, AdminSignupActivity.class));

            }
        });

        arrayList = new ArrayList<>();
        String url = "https://library-feedback-system.herokuapp.com/api/library/feedback";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            /**
             * on a successful response display charts and the data on the dashboard
             * @param response
             */
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    if (response.getBoolean("success"))
                    {
                        JSONArray jaAns = response.getJSONArray("msg");
                        int length = jaAns.length();
                        int Q1A1 = 0, Q1A2 = 0, Q1A3 = 0, Q1A4 = 0, Q1A5 = 0;
                        for(int i=0; i<length; i++)
                        {
                            JSONObject jObj = jaAns.getJSONObject(i);
                            if (jObj.getString("answer05").equals("Comic Book or Graphic Novel"))
                            {
                                Q1A1++;
                            }
                            if (jObj.getString("answer05").equals("Detective and Mystery"))
                            {
                                Q1A2++;
                            }
                            if (jObj.getString("answer05").equals("Fantasy"))
                            {
                                Q1A3++;
                            }
                            if (jObj.getString("answer05").equals("Science Fiction"))
                            {
                                Q1A4++;
                            }
                            if (jObj.getString("answer05").equals("Horror"))
                            {
                                Q1A5++;
                            }
                        }

                        int Q6A1 = 0, Q6A2 = 0, Q6A3 = 0, Q6A4 = 0, Q6A5 = 0;
                        for(int i=0; i<length; i++)
                        {
                            JSONObject jObj = jaAns.getJSONObject(i);
                            if (jObj.getString("answer06").equals("Under 18"))
                            {
                                Q6A1++;
                            }
                            if (jObj.getString("answer06").equals("18-24"))
                            {
                                Q6A2++;
                            }
                            if (jObj.getString("answer06").equals("25-44"))
                            {
                                Q6A3++;
                            }
                            if (jObj.getString("answer06").equals("45-64"))
                            {
                                Q6A4++;
                            }
                            if (jObj.getString("answer06").equals("Above 64"))
                            {
                                Q6A5++;
                            }
                        }

                        txtTotalFeed.setText(String.valueOf(length));

                        FavouriteBookType(Q1A1, Q1A2, Q1A3, Q1A4, Q1A5);
                        MostVisitedAge(Q6A1, Q6A2, Q6A3, Q6A4, Q6A5);
                        drawChart1(anyChartView1, Q1A1, Q1A2, Q1A3, Q1A4, Q1A5);
                        drawChart2(anyChartView2, Q6A1, Q6A2, Q6A3, Q6A4, Q6A5);
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
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
                        Toast.makeText(AdminDashboardActivity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                    catch (JSONException | UnsupportedEncodingException je)
                    {
                        je.printStackTrace();
                    }
                }
            }
        });

        //Setting the retry policies
        int socketTime = 3000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTime, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        //Adding the request using volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Takes the book category parameters to calculate the favourite book category
     * Stores the favourite book category on a string and change the related label
     * @param A1
     * @param A2
     * @param A3
     * @param A4
     * @param A5
     */
    public void FavouriteBookType (int A1, int A2, int A3, int A4, int A5)
    {
        int MaxBook = A1;
        String MaxBookName = "Comic Book or Graphic Novel";
        if (A2 > MaxBook)
        {
            MaxBook = A2;
            MaxBookName = "Detective and Mystery";
        }
        if (A3 > MaxBook)
        {
            MaxBook = A3;
            MaxBookName = "Fantasy";
        }
        if (A4 > MaxBook)
        {
            MaxBook = A4;
            MaxBookName = "Science Fiction";
        }
        if (A5 > MaxBook)
        {
            MaxBook = A5;
            MaxBookName = "Horror";
        }
        txtBookFeed.setText(MaxBookName);
    }

    /**
     * Takes the age parameters to calculate the most visited age
     * Stores the age category on a string and change the related label
     * @param A1
     * @param A2
     * @param A3
     * @param A4
     * @param A5
     */
    public void MostVisitedAge (int A1, int A2, int A3, int A4, int A5)
    {
        int MaxAge = A1;
        String MaxAgeCategory = "Under 18";
        if (A2 > MaxAge)
        {
            MaxAge = A2;
            MaxAgeCategory = "18-24";
        }
        if (A3 > MaxAge)
        {
            MaxAge = A3;
            MaxAgeCategory = "25-44";
        }
        if (A4 > MaxAge)
        {
            MaxAge = A4;
            MaxAgeCategory = "45-64";
        }
        if (A5 > MaxAge)
        {
            MaxAge = A5;
            MaxAgeCategory = "Above 64";
        }
        txtAgeFeed.setText(MaxAgeCategory);
    }

    /**
     * Call the external chart API.
     * Takes the chart structure and values needed to generate the first chart
     * On click event is created to display the enhanced view of the values
     * @param anyChartView
     * @param A1
     * @param A2
     * @param A3
     * @param A4
     * @param A5
     */
    public void drawChart1(AnyChartView anyChartView, int A1, int A2, int A3, int A4, int A5)
    {
        APIlib.getInstance().setActiveAnyChartView(anyChartView);
        Pie pie1 = AnyChart.pie();

        pie1.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(AdminDashboardActivity.this, event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Comic Book or Graphic Novel", A1));
        data.add(new ValueDataEntry("Detective and Mystery", A2));
        data.add(new ValueDataEntry("Fantasy", A3));
        data.add(new ValueDataEntry("Science Fiction", A4));
        data.add(new ValueDataEntry("Horror", A5));

        pie1.data(data);

        pie1.title("Most interested book categories");
        pie1.labels().position("outside");
        pie1.legend().position("center-bottom").itemsLayout(LegendLayout.HORIZONTAL).align(Align.CENTER);

        anyChartView.setChart(pie1);
    }

    /**
     * Call the external chart API.
     * Takes the chart structure and values needed to generate the second chart
     * on click event is created to display the enhanced view of the values
     * @param anyChartView
     * @param A1
     * @param A2
     * @param A3
     * @param A4
     * @param A5
     */
    public void drawChart2(AnyChartView anyChartView, int A1, int A2, int A3, int A4, int A5)
    {
        APIlib.getInstance().setActiveAnyChartView(anyChartView);
        Pie pie = AnyChart.pie();

        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(AdminDashboardActivity.this, event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Under 18", A1));
        data.add(new ValueDataEntry("18-24", A2));
        data.add(new ValueDataEntry("25-44", A3));
        data.add(new ValueDataEntry("45-64", A4));
        data.add(new ValueDataEntry("Above 64", A5));

        pie.data(data);

        pie.title("Age Categories");
        pie.labels().position("outside");
        pie.legend().position("center-bottom").itemsLayout(LegendLayout.HORIZONTAL).align(Align.CENTER);

        anyChartView.setChart(pie);
    }
}