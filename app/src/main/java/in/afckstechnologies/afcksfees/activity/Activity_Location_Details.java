package in.afckstechnologies.afcksfees.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.afckstechnologies.afcksfees.Fragments.TabsFragmentActivity;
import in.afckstechnologies.afcksfees.R;
import in.afckstechnologies.afcksfees.adapter.CenterListAdpter;
import in.afckstechnologies.afcksfees.jsonparser.JsonHelper;
import in.afckstechnologies.afcksfees.model.CenterDAO;
import in.afckstechnologies.afcksfees.utils.Config;
import in.afckstechnologies.afcksfees.utils.FeesListener;
import in.afckstechnologies.afcksfees.utils.WebClient;
import in.afckstechnologies.afcksfees.view.CommentAddView;
import in.afckstechnologies.afcksfees.view.CommentsDetailsView;
import in.afckstechnologies.afcksfees.view.MultipleCommentAddView;

public class Activity_Location_Details extends AppCompatActivity {
    LinearLayout takeChangeHome, takeChangeCourses, takeChangedayprefrence, takeTemplate, takeCreateComment, takeBookSeat, takeEditUser, takeDeleteUser;

    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    Button btnSearch;

    JSONObject jsonCenterObj, jsonobject, jsonLeadObj;
    JSONArray jsonarray;
    String centerResponse = "";

    String centerListResponse = "";
    ProgressDialog mProgressDialog;
    CenterDAO centerDAO;
    private RecyclerView mleadList;
    //
    List<CenterDAO> data;
    CenterListAdpter centerListAdpter;
    ArrayList<String> centerIdArrayList;

    Boolean status;
    String msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__location__details);
        preferences = getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        getCenterList();
        centerIdArrayList = new ArrayList<>();
        mleadList = (RecyclerView) findViewById(R.id.centerList);
        takeCreateComment = (LinearLayout) findViewById(R.id.takeCreateComment);
        takeChangeHome = (LinearLayout) findViewById(R.id.takeChangeHome);
        takeChangedayprefrence = (LinearLayout) findViewById(R.id.takeChangedayprefrence);
        takeChangeCourses = (LinearLayout) findViewById(R.id.takeChangeCourses);
        takeTemplate = (LinearLayout) findViewById(R.id.takeTemplate);
        takeBookSeat = (LinearLayout) findViewById(R.id.takeBookSeat);
        takeBookSeat = (LinearLayout) findViewById(R.id.takeBookSeat);

        takeBookSeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!preferences.getString("user_id", "").equals("")) {
                   // BookSeatView bookSeatView = new BookSeatView();
                   // bookSeatView.show(getSupportFragmentManager(), "registrationView");
                } else {

                    Toast.makeText(getApplicationContext(), "Please select student from list", Toast.LENGTH_LONG).show();
                }

            }
        });
        takeCreateComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MultipleCommentAddView commentAddView = new MultipleCommentAddView();
                commentAddView.show(getSupportFragmentManager(), "commentAddView");

            }
        });

        takeCreateComment.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                CommentsDetailsView commentAddView = new CommentsDetailsView();
                commentAddView.show(getSupportFragmentManager(), "commentAddView");
                return true;
            }
        });
        takeChangeCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!preferences.getString("user_id", "").equals("")) {
                    finish();
                    Intent intent = new Intent(Activity_Location_Details.this, TabsFragmentActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Please select student from list", Toast.LENGTH_LONG).show();
                }


            }
        });


        takeChangedayprefrence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!preferences.getString("user_id", "").equals("")) {
                    finish();
                    Intent intent = new Intent(Activity_Location_Details.this, Activity_DayPrefrence_Display.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Please select student from list", Toast.LENGTH_LONG).show();
                }


            }
        });

        takeTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!preferences.getString("user_id", "").equals("")) {
                    finish();
                    Intent intent = new Intent(Activity_Location_Details.this, TemplateDisplayActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Please select student from list", Toast.LENGTH_LONG).show();
                }
            }
        });

        takeChangeHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!preferences.getString("user_id", "").equals("")) {
                    finish();
                    Intent intent = new Intent(Activity_Location_Details.this, StudentList.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(getApplicationContext(), "Please select student from list", Toast.LENGTH_LONG).show();
                }


            }
        });

        CenterListAdpter.bindListener(new FeesListener() {
            @Override
            public void messageReceived(String messageText) {
                getCenterList();
            }
        });


    }

    public void getCenterList() {
        jsonCenterObj = new JSONObject() {
            {
                try {
                    put("user_id", preferences.getString("user_id", ""));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("json exception", "json exception" + e);
                }
            }
        };

        Thread objectThread = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                WebClient serviceAccess = new WebClient();
                Log.i("json", "json" + jsonCenterObj);
                centerResponse = serviceAccess.SendHttpPost(Config.URL_CENTER_DETAILS, jsonCenterObj);
                Log.i("resp", "centerResponse" + centerResponse);
                centerDAO = new CenterDAO();
                data = new ArrayList<>();
                if (centerResponse.compareTo("") != 0) {
                    if (isJSONValid(centerResponse)) {

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                try {

                                    JsonHelper jsonHelper = new JsonHelper();
                                    data = jsonHelper.parseCenterList(centerResponse);

                                    centerListAdpter = new CenterListAdpter(Activity_Location_Details.this, data);
                                    mleadList.setAdapter(centerListAdpter);
                                    mleadList.setLayoutManager(new LinearLayoutManager(Activity_Location_Details.this));

                                    jsonobject = new JSONObject(centerResponse);

                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Toast.makeText(Activity_Location_Details.this, "Please check your network connection", Toast.LENGTH_LONG).show();
                            }
                        });

                        return;
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_SHORT).show();
                        }
                    });

                    return;
                }
            }
        });

        objectThread.start();
    }

    protected boolean isJSONValid(String callReoprtResponse2) {
        // TODO Auto-generated method stub
        try {
            new JSONObject(callReoprtResponse2);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(callReoprtResponse2);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }


}
