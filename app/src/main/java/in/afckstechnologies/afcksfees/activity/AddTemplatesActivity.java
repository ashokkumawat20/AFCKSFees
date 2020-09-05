package in.afckstechnologies.afcksfees.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import in.afckstechnologies.afcksfees.R;
import in.afckstechnologies.afcksfees.utils.AppStatus;
import in.afckstechnologies.afcksfees.utils.Config;
import in.afckstechnologies.afcksfees.utils.Constant;
import in.afckstechnologies.afcksfees.utils.FeesListener;
import in.afckstechnologies.afcksfees.utils.WebClient;

public class AddTemplatesActivity extends AppCompatActivity {
    String title = "";
    String templateText = "";
    EditText add_title, msgData;
    Button saveTemplate;
    ProgressDialog mProgressDialog;
    private JSONObject jsonLeadObj, jsonLeadObj1;

    String addTemplateResponse = "";

    String msg = "";
    boolean status;
    private static FeesListener mListener;
    Button first_name,course_name,start_date,notes,timings,frequency,duration,branch_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_templates);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        add_title = (EditText) findViewById(R.id.add_title);
        msgData = (EditText) findViewById(R.id.msgData);
        add_title.setText(title);
        first_name=(Button)findViewById(R.id.first_name);
        course_name=(Button)findViewById(R.id.course_name);
        start_date=(Button)findViewById(R.id.start_date);
        notes=(Button)findViewById(R.id.notes);
        timings=(Button)findViewById(R.id.timings);
        frequency=(Button)findViewById(R.id.frequency);
        duration=(Button)findViewById(R.id.duration);
        branch_name=(Button)findViewById(R.id.branch_name);
        first_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgData.append("[first_name]");
            }
        });
        course_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgData.append("[course_name]");
            }
        });
        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgData.append("[start_date]");
            }
        });
        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgData.append("[notes]");
            }
        });
        timings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgData.append("[timings]");
            }
        });
        frequency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgData.append("[frequency]");
            }
        });
        duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgData.append("[duration]");
            }
        });
        branch_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgData.append("[branch_name]");
            }
        });

        saveTemplate = (Button) findViewById(R.id.saveTemplate);
        saveTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    templateText = msgData.getText().toString().trim();
                    title = add_title.getText().toString().trim();
                    if (!templateText.equals("")) {
                        new initAddTemplate().execute();
                    } else {

                        Toast.makeText(getApplicationContext(), "Please enter value for template message", Toast.LENGTH_SHORT).show();

                    }
                } else {

                    Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private class initAddTemplate extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(AddTemplatesActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {
                        put("ID", title);
                        put("Template_Text", templateText);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();

            //  String baseURL = "http://192.168.1.13:8088/lms/api/lead/showlead";
            Log.i("json", "json" + jsonLeadObj);
            addTemplateResponse = serviceAccess.SendHttpPost(Config.URL_ADDTEMPLATE, jsonLeadObj);
            Log.i("resp", "addTemplateResponse" + addTemplateResponse);

            if (addTemplateResponse.compareTo("") != 0) {
                if (isJSONValid(addTemplateResponse)) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {
                                JSONObject jsonObject = new JSONObject(addTemplateResponse);
                                msg = jsonObject.getString("message");
                                status = jsonObject.getBoolean("status");

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
            if (addTemplateResponse.compareTo("") != 0) {

                if (status) {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddTemplatesActivity.this);
                    alertDialog.setTitle("Add Template");
                    alertDialog.setMessage("Do you want add more Template ");


                    // alertDialog.setIcon(R.drawable.msg_img);
                    alertDialog.setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();

                                }
                            });

                    alertDialog.setNegativeButton("NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Intent intent = new Intent(AddTemplatesActivity.this, StudentList.class);
                                    //   startActivity(intent);
                                    mListener.messageReceived(msg);
                                    finish();

                                }
                            });

                    alertDialog.show();
                    mProgressDialog.dismiss();
                } else {

                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
                mProgressDialog.dismiss();
            } else {
                // Close the progressdialog
                mProgressDialog.dismiss();
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

    public static void bindListener(FeesListener listener) {
        mListener = listener;
    }

}
