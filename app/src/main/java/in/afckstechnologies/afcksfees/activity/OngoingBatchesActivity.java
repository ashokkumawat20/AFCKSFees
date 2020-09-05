package in.afckstechnologies.afcksfees.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.afckstechnologies.afcksfees.R;
import in.afckstechnologies.afcksfees.adapter.OnGoingBatchesListAdpter;
import in.afckstechnologies.afcksfees.jsonparser.JsonHelper;
import in.afckstechnologies.afcksfees.model.FALocationDAO;
import in.afckstechnologies.afcksfees.model.FAStatusDAO;
import in.afckstechnologies.afcksfees.model.FItemNameDAO;
import in.afckstechnologies.afcksfees.model.OnGoingBatchDAO;
import in.afckstechnologies.afcksfees.utils.Config;
import in.afckstechnologies.afcksfees.utils.WebClient;

public class OngoingBatchesActivity extends AppCompatActivity {
    Spinner displayTrainerName, displayCourseName, displayLocationName;
    ProgressDialog mProgressDialog;
    private JSONObject jsonLeadObj, jsonLeadObj1;
    String itemNameResponse = "", fLocationResponse = "", fStatusResponse = "", addResponse = "", onGoingBatchResponse = "";
    JSONArray jsonArray;

    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    ArrayList<FItemNameDAO> itemnamelist;
    ArrayList<FALocationDAO> flocationlist;
    ArrayList<FAStatusDAO> fstatuslist;

    String trainer_id = "0", branch_name = "0", course_name = "0";

    boolean status;
    String message = "";
    String itemName = "", locationName = "";
    LinearLayout layTrainerName;
    List<OnGoingBatchDAO> data;
    OnGoingBatchesListAdpter onGoingBatchesListAdpter;
    private RecyclerView onGoingBatchList;
    TextView Date, frequency, timings, branch;
    private boolean mAscendingOrder[] = {true, true, true, true};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_batches);
        displayTrainerName = (Spinner) findViewById(R.id.displayTrainerName);
        displayLocationName = (Spinner) findViewById(R.id.displayLocationName);
        displayCourseName = (Spinner) findViewById(R.id.displayCourseName);
        layTrainerName = (LinearLayout) findViewById(R.id.layTrainerName);
        Date = (TextView) findViewById(R.id.Date);
        frequency = (TextView) findViewById(R.id.frequency);
        timings = (TextView) findViewById(R.id.timings);
        branch = (TextView) findViewById(R.id.branch);
        preferences = getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        onGoingBatchList = (RecyclerView) findViewById(R.id.onGoingBatchList);
        if (preferences.getString("trainer_user_id", "").equals("AK")) {
            layTrainerName.setVisibility(View.VISIBLE);
        }
        if (preferences.getString("trainer_user_id", "").equals("RS")) {
            layTrainerName.setVisibility(View.VISIBLE);
        }
        new initLocationNameSpinner().execute();
        new initCourseSpinner().execute();
        new initTrainerNameSpinner().execute();

        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAscendingOrder[0]) {
                    // Show items descending
                    mAscendingOrder[0] = false;
                    onGoingBatchesListAdpter.sortByNameDesc();

                } else {
                    // Show items ascending
                    mAscendingOrder[0] = true;
                    onGoingBatchesListAdpter.sortByNameAsc();

                }

                mAscendingOrder[1] = true;
                mAscendingOrder[2] = true;
            }
        });
        frequency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
// Change the order of items based on price
                if (mAscendingOrder[1]) {
                    // Show items descending
                    mAscendingOrder[1] = false;
                    onGoingBatchesListAdpter.sortByFrequencyDesc();

                } else {
                    // Show items ascending
                    mAscendingOrder[1] = true;
                    onGoingBatchesListAdpter.sortByFrequencyAsc();

                }

                mAscendingOrder[0] = false;
                mAscendingOrder[2] = true;
            }
        });

        timings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Change the order of items based on quantity
                if (mAscendingOrder[2]) {
                    // Show items descending
                    mAscendingOrder[2] = false;
                    onGoingBatchesListAdpter.sortByTimingsDesc();

                } else {
                    // Show items ascending
                    mAscendingOrder[2] = true;
                    onGoingBatchesListAdpter.sortByTimingsAsc();

                }

                mAscendingOrder[0] = false;
                mAscendingOrder[1] = true;
            }
        });
        branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Change the order of items based on quantity
                if (mAscendingOrder[3]) {
                    // Show items descending
                    mAscendingOrder[3] = false;
                    onGoingBatchesListAdpter.sortByBranchDesc();

                } else {
                    // Show items ascending
                    mAscendingOrder[3] = true;
                    onGoingBatchesListAdpter.sortByBranchAsc();

                }

                mAscendingOrder[0] = false;
                mAscendingOrder[1] = true;
            }
        });
    }

    private class initLocationNameSpinner extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            //   mProgressDialog = new ProgressDialog(FixedAssetsActivity.this);
            // Set progressdialog title
            //   mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            // mProgressDialog.setMessage("Loading...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            //  mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {
                        put("user_id", preferences.getString("trainer_user_id", ""));

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();

            //  String baseURL = "http://192.168.1.13:8088/lms/api/lead/showlead";
            Log.i("json", "json" + jsonLeadObj);
            fLocationResponse = serviceAccess.SendHttpPost(Config.URL_GETALLTRAINERONGOINGBRANCH, jsonLeadObj);
            Log.i("resp", "leadListResponse" + fLocationResponse);

            if (fLocationResponse.compareTo("") != 0) {
                if (isJSONValid(fLocationResponse)) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {

                                flocationlist = new ArrayList<>();
                                flocationlist.add(new FALocationDAO("0", "ALL"));
                                JSONArray LeadSourceJsonObj = new JSONArray(fLocationResponse);
                                for (int i = 0; i < LeadSourceJsonObj.length(); i++) {
                                    JSONObject json_data = LeadSourceJsonObj.getJSONObject(i);
                                    flocationlist.add(new FALocationDAO(json_data.getString("branch_name"), json_data.getString("branch_name")));

                                }

                                jsonArray = new JSONArray(fLocationResponse);

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
                            Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_LONG).show();
                        }
                    });

                    return null;
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_LONG).show();
                    }
                });

                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (fLocationResponse.compareTo("") != 0) {

                // Spinner spinnerCustom = (Spinner) findViewById(R.id.spinnerBranch);
                ArrayAdapter<FALocationDAO> adapter = new ArrayAdapter<FALocationDAO>(OngoingBatchesActivity.this, android.R.layout.simple_spinner_dropdown_item, flocationlist);
                // MyAdapter adapter = new MyAdapter(StudentsListActivity.this,R.layout.spinner_item,locationlist);
                displayLocationName.setAdapter(adapter);
                displayLocationName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#1c5fab"));
                        FALocationDAO LeadSource = (FALocationDAO) parent.getSelectedItem();
                        // Toast.makeText(getApplicationContext(), "Source ID: " + LeadSource.getId() + ",  Source Name : " + LeadSource.getLocation_name(), Toast.LENGTH_SHORT).show();
                        branch_name = LeadSource.getId();

                        if (!branch_name.equals("0")) {
                            new getOngoingBatchesList().execute();
                        }

                        if (trainer_id.equals("0") && course_name.equals("0") && branch_name.equals("0")) {
                            new getOngoingBatchesList().execute();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }


                });
                // mProgressDialog.dismiss();
            } else {
                // Close the progressdialog
                // mProgressDialog.dismiss();
            }
        }
    }

    private class initCourseSpinner extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            //   mProgressDialog = new ProgressDialog(FixedAssetsActivity.this);
            // Set progressdialog title
            //   mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            // mProgressDialog.setMessage("Loading...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            //  mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {
                        put("user_id", preferences.getString("trainer_user_id", ""));

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();

            //  String baseURL = "http://192.168.1.13:8088/lms/api/lead/showlead";
            Log.i("json", "json" + jsonLeadObj);
            fStatusResponse = serviceAccess.SendHttpPost(Config.URL_GETALLTRAINERONGOINGBATCHES, jsonLeadObj);
            Log.i("resp", "leadListResponse" + fStatusResponse);

            if (fStatusResponse.compareTo("") != 0) {
                if (isJSONValid(fStatusResponse)) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {

                                fstatuslist = new ArrayList<>();
                                fstatuslist.add(new FAStatusDAO("0", "ALL"));
                                JSONArray LeadSourceJsonObj = new JSONArray(fStatusResponse);
                                for (int i = 0; i < LeadSourceJsonObj.length(); i++) {
                                    JSONObject json_data = LeadSourceJsonObj.getJSONObject(i);
                                    fstatuslist.add(new FAStatusDAO(json_data.getString("course_name"), json_data.getString("course_name")));

                                }

                                jsonArray = new JSONArray(fStatusResponse);

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
                            Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_LONG).show();
                        }
                    });

                    return null;
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_LONG).show();
                    }
                });

                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (fStatusResponse.compareTo("") != 0) {

                // Spinner spinnerCustom = (Spinner) findViewById(R.id.spinnerBranch);
                ArrayAdapter<FAStatusDAO> adapter = new ArrayAdapter<FAStatusDAO>(OngoingBatchesActivity.this, android.R.layout.simple_spinner_dropdown_item, fstatuslist);
                // MyAdapter adapter = new MyAdapter(StudentsListActivity.this,R.layout.spinner_item,locationlist);
                displayCourseName.setAdapter(adapter);
                displayCourseName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#1c5fab"));
                        FAStatusDAO LeadSource = (FAStatusDAO) parent.getSelectedItem();
                        //   Toast.makeText(getApplicationContext(), "Source ID: " + LeadSource.getId() + ",  Source Name : " + LeadSource.getStatus(), Toast.LENGTH_SHORT).show();
                        course_name = LeadSource.getId();

                        if (!course_name.equals("0")) {
                            new getOngoingBatchesList().execute();
                        }


                        if (course_name.equals("0") && trainer_id.equals("0") && branch_name.equals("0")) {

                            new getOngoingBatchesList().execute();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }


                });
                //  mProgressDialog.dismiss();
            } else {
                // Close the progressdialog
                //  mProgressDialog.dismiss();
            }
        }
    }

    private class initTrainerNameSpinner extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            //  mProgressDialog = new ProgressDialog(OngoingBatchesActivity.this);
            // Set progressdialog title
            //   mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            //   mProgressDialog.setMessage("Loading...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            // mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();

            //  String baseURL = "http://192.168.1.13:8088/lms/api/lead/showlead";
            Log.i("json", "json" + jsonLeadObj);
            itemNameResponse = serviceAccess.SendHttpPost(Config.URL_GETALLONGOINGTRAINER, jsonLeadObj);
            Log.i("resp", "leadListResponse" + itemNameResponse);

            if (itemNameResponse.compareTo("") != 0) {
                if (isJSONValid(itemNameResponse)) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {

                                itemnamelist = new ArrayList<>();
                                itemnamelist.add(new FItemNameDAO("0", "ALL"));
                                JSONArray LeadSourceJsonObj = new JSONArray(itemNameResponse);
                                for (int i = 0; i < LeadSourceJsonObj.length(); i++) {
                                    JSONObject json_data = LeadSourceJsonObj.getJSONObject(i);
                                    itemnamelist.add(new FItemNameDAO(json_data.getString("trainer_id"), json_data.getString("trainer_id")));

                                }

                                jsonArray = new JSONArray(itemNameResponse);

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
                            Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_LONG).show();
                        }
                    });

                    return null;
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_LONG).show();
                    }
                });

                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (itemNameResponse.compareTo("") != 0) {

                // Spinner spinnerCustom = (Spinner) findViewById(R.id.spinnerBranch);
                ArrayAdapter<FItemNameDAO> adapter = new ArrayAdapter<FItemNameDAO>(OngoingBatchesActivity.this, android.R.layout.simple_spinner_dropdown_item, itemnamelist);
                // MyAdapter adapter = new MyAdapter(StudentsListActivity.this,R.layout.spinner_item,locationlist);
                displayTrainerName.setAdapter(adapter);
                displayTrainerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#1c5fab"));
                        FItemNameDAO LeadSource = (FItemNameDAO) parent.getSelectedItem();
                        // Toast.makeText(getApplicationContext(), "Source ID: " + LeadSource.getId() + ",  Source Name : " + LeadSource.getItemName(), Toast.LENGTH_SHORT).show();
                        trainer_id = LeadSource.getId();

                        if (!trainer_id.equals("0")) {
                            new getOngoingBatchesList().execute();
                        }
                        if (branch_name.equals("0") && trainer_id.equals("0") && course_name.equals("0")) {
                            new getOngoingBatchesList().execute();

                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }


                });
                //mProgressDialog.dismiss();
            } else {
                // Close the progressdialog
                // mProgressDialog.dismiss();
            }
        }
    }

    private class getOngoingBatchesList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            // mProgressDialog = new ProgressDialog(OngoingBatchesActivity.this);
            // Set progressdialog title
            //  mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            //  mProgressDialog.setMessage("Loading...");
            //   mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            //  mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {
                        put("user_id", preferences.getString("trainer_user_id", ""));
                        put("trainer_id", trainer_id);
                        put("branch_name", branch_name);
                        put("course_name", course_name);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();

            //  String baseURL = "http://192.168.1.13:8088/srujanlms_new/api/Leadraw/showleadraw";
            Log.i("json", "json" + jsonLeadObj);
            onGoingBatchResponse = serviceAccess.SendHttpPost(Config.URL_GETALLONGOINGBATCHES, jsonLeadObj);
            Log.i("resp", "batchesListResponse" + onGoingBatchResponse);
            if (onGoingBatchResponse.compareTo("") != 0) {
                if (isJSONValid(onGoingBatchResponse)) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {

                                data = new ArrayList<>();
                                data.clear();
                                JsonHelper jsonHelper = new JsonHelper();
                                data = jsonHelper.parseOnGoingBatchesList(onGoingBatchResponse);
                                jsonArray = new JSONArray(onGoingBatchResponse);

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
                            Toast.makeText(getApplicationContext(), "Please check your webservice", Toast.LENGTH_LONG).show();
                        }
                    });

                    return null;
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_LONG).show();
                    }
                });

                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (data.size() > 0) {

                onGoingBatchesListAdpter = new OnGoingBatchesListAdpter(OngoingBatchesActivity.this, data);
                onGoingBatchList.setAdapter(onGoingBatchesListAdpter);
                onGoingBatchList.setLayoutManager(new LinearLayoutManager(OngoingBatchesActivity.this));
                onGoingBatchesListAdpter.notifyDataSetChanged();

            } else {
                // Close the progressdialog


                data.clear(); // this list which you hava passed in Adapter for your listview
                onGoingBatchesListAdpter = new OnGoingBatchesListAdpter(OngoingBatchesActivity.this, data);
                onGoingBatchList.setAdapter(onGoingBatchesListAdpter);
                onGoingBatchList.setLayoutManager(new LinearLayoutManager(OngoingBatchesActivity.this));
                onGoingBatchesListAdpter.notifyDataSetChanged();

            }

        }
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
