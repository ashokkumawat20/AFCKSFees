package in.afckstechnologies.afcksfees.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import in.afckstechnologies.afcksfees.adapter.StudentCertificateEntryDetailsListAdpter;
import in.afckstechnologies.afcksfees.common.logger.Log;
import in.afckstechnologies.afcksfees.model.CenterDAO;
import in.afckstechnologies.afcksfees.model.StudentCertificateDetailsDAO;
import in.afckstechnologies.afcksfees.model.StudentsDAO;
import in.afckstechnologies.afcksfees.utils.AppStatus;
import in.afckstechnologies.afcksfees.utils.Config;
import in.afckstechnologies.afcksfees.utils.Constant;
import in.afckstechnologies.afcksfees.utils.WebClient;

public class SendingStudentNameForCertificateActivity extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    ArrayList<String> mobileNoArrayList;
    ArrayList<String> studentNameArrayList;
    ArrayList<String> studentMailIdArrayList;
    ArrayList<String> studentUserIdArrayList;
    ArrayList<String> nameArrayList;
    String cousre_name="",batch_type="",batch_id="",addICertificateResponse="",message="";
    private ArrayList<StudentCertificateDetailsDAO> certificateDetailsDAOArrayList = new ArrayList<StudentCertificateDetailsDAO>();
    private StudentCertificateDetailsDAO certificateDetailsDAO;
    StudentCertificateEntryDetailsListAdpter studentCertificateEntryDetailsListAdpter;
    TextView titleTxt;
    private RecyclerView mstudentList;
    Button submit;
    ImageView closeFeesDetails;
    JSONArray jsonArraySync,jsonArray;
    JSONObject jsonObjectSync,syncJsonObject;
    ProgressDialog mProgressDialog;
    boolean status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sending_student_name_for_certificate);
        preferences = getSharedPreferences("Prefrence", MODE_PRIVATE);
        prefEditor = preferences.edit();
        titleTxt=(TextView)findViewById(R.id.titleTxt);
        submit=(Button)findViewById(R.id.submit);
        closeFeesDetails=(ImageView)findViewById(R.id.closeFeesDetails);
        titleTxt.setText("Display Name on Certificate("+preferences.getString("batch_id","")+")");
        mstudentList = (RecyclerView)findViewById(R.id.feesdetailsList);
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        mobileNoArrayList = (ArrayList<String>) args.getSerializable("ARRAYLIST");
        studentNameArrayList = (ArrayList<String>) args.getSerializable("ARRAYLIST1");
        studentMailIdArrayList = (ArrayList<String>) args.getSerializable("ARRAYLIST2");
        studentUserIdArrayList = (ArrayList<String>) args.getSerializable("ARRAYLIST3");
        nameArrayList = (ArrayList<String>) args.getSerializable("ARRAYLIST4");
        cousre_name = intent.getStringExtra("course_name");
        batch_id = intent.getStringExtra("batch_id");
        batch_type = intent.getStringExtra("batch_type");
        for (int i = 0; i < mobileNoArrayList.size(); i++) {

            certificateDetailsDAO = new StudentCertificateDetailsDAO();
            certificateDetailsDAO.setStudent_Name(studentNameArrayList.get(i));
            certificateDetailsDAO.setStudent_certificate_name(nameArrayList.get(i));
            certificateDetailsDAOArrayList.add(certificateDetailsDAO);
        }

        Log.d("Size",""+certificateDetailsDAOArrayList.size());
        new displayCertificateDetailsList().execute();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(SendingStudentNameForCertificateActivity.this);
                    builder.setMessage("Are you sure ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    new addCertificateDataDetails().execute();
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                }
                            });

                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Sending Data for Certificate");
                    alert.show();

                }
                else {

                    Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }

            }
        });
        closeFeesDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private class displayCertificateDetailsList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (certificateDetailsDAOArrayList.size()>0) {
                studentCertificateEntryDetailsListAdpter = new StudentCertificateEntryDetailsListAdpter(SendingStudentNameForCertificateActivity.this, certificateDetailsDAOArrayList);
                mstudentList.setAdapter(studentCertificateEntryDetailsListAdpter);
                mstudentList.setLayoutManager(new LinearLayoutManager(SendingStudentNameForCertificateActivity.this));
                studentCertificateEntryDetailsListAdpter.notifyDataSetChanged();
                mstudentList.setItemViewCacheSize(certificateDetailsDAOArrayList.size());

            } else {
                // Close the progressdialog

            }
        }
    }

    private class addCertificateDataDetails extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(SendingStudentNameForCertificateActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Uploading...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonArraySync = new JSONArray();
            for (int i = 0; i < certificateDetailsDAOArrayList.size(); i++) {
                View view = mstudentList.getChildAt(i);
                EditText nameEditText = (EditText) view.findViewById(R.id.sno);
                String name = nameEditText.getText().toString();
                Log.d("name",name);
                jsonObjectSync = new JSONObject();
                try {
                    jsonObjectSync.put("st_id", studentUserIdArrayList.get(i));
                    jsonObjectSync.put("st_name", name);
                    jsonObjectSync.put("batch_id", batch_id);
                    jsonObjectSync.put("course_Name", cousre_name);
                    jsonObjectSync.put("mobile_No", mobileNoArrayList.get(i));
                    jsonObjectSync.put("email_id", studentMailIdArrayList.get(i));
                    jsonObjectSync.put("batch_type", batch_type);
                    jsonArraySync.put(jsonObjectSync);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            try {
                syncJsonObject = new JSONObject();
                syncJsonObject.put("certificateData", jsonArraySync);
                Log.d("certificateData", "" + syncJsonObject);
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }



            WebClient serviceAccess = new WebClient();
            addICertificateResponse = serviceAccess.SendHttpPost(Config.URL_ADDCERTIFICATEDETAILSSYNCDATA, syncJsonObject);
            Log.i("resp", "addICertificateResponse" + addICertificateResponse);


            if (addICertificateResponse.compareTo("") != 0) {
                if (isJSONValid(addICertificateResponse)) {

                    try {

                        JSONObject jObject = new JSONObject(addICertificateResponse);
                        status = jObject.getBoolean("status");
                        message = jObject.getString("message");
                        if (status) {


                        }
                        jsonArray = new JSONArray(addICertificateResponse);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                } else {

                    Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_LONG).show();

                }
            } else {

                Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            // Close the progressdialog
            mProgressDialog.dismiss();
            if (status) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                finish();


            } else {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();


            }

        }
    }
    protected boolean isJSONValid(String addImageLocationResponse) {
        // TODO Auto-generated method stub
        try {
            new JSONObject(addImageLocationResponse);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(addImageLocationResponse);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
}
