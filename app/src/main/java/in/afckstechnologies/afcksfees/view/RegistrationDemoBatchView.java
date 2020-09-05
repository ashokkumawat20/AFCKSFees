package in.afckstechnologies.afcksfees.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

import in.afckstechnologies.afcksfees.R;
import in.afckstechnologies.afcksfees.activity.Activity_User_Profile;
import in.afckstechnologies.afcksfees.model.BatchesDAO;
import in.afckstechnologies.afcksfees.model.CourseDetailsDAO;
import in.afckstechnologies.afcksfees.utils.AppStatus;
import in.afckstechnologies.afcksfees.utils.Config;
import in.afckstechnologies.afcksfees.utils.Constant;
import in.afckstechnologies.afcksfees.utils.SmsListener;
import in.afckstechnologies.afcksfees.utils.WebClient;


public class RegistrationDemoBatchView extends DialogFragment {

    Button placeBtn;
    private EditText nameEdtTxt;
    private EditText lastnameEdtTxt;
    private EditText phEdtTxt;
    private EditText emailEdtTxt;
    private EditText AddEdtTxt;
    private TextView dateEdtTxt, titleTxt, header;
    private TelephonyManager telephonyManager;
    private String deviceId = "";
    private Spinner spnrUserType;
    private Calendar myCalendar;
    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    DatePickerDialog dp = null;
    String loginResponse = "";
    JSONObject jsonObj;
    String device_id;
    Boolean status, status1;
    String message;
    String user_id;
    boolean click = true;
    int count = 0;

    //8-3-2017
    RadioGroup radioGroup;
    private RadioButton radioButton;
    View registerView;
    int pos;
    int pos1;

    String gender = "";
    String user_name = "";
    String first_name = "";
    String last_name = "";
    String email_id = "";
    String mobile_no = "";
    ImageView readContact;
    final static int PICK_CONTACT = 0;
    ProgressDialog mProgressDialog;

    JSONObject jsonCenterObj, jsonobject, jsonLeadObj;
    JSONArray jsonarray;
    String centerResponse = "", courseResponse = "", onComingBatchResponse = "", course_id = "", batch_code = "", course_name = "", bankDetailsResponse = "", f_name = "", l_name = "", msg = "", addStudentRespone = "";
    String phoneNumber = "", emailAddress = "";
    public static SmsListener mListener;
    Spinner displayCourseName;

    ArrayList<CourseDetailsDAO> courseDetailsDAOArrayList;
    JSONArray jsonArray;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private RadioButton male, female;
    static String sms_user = "";
    static String sms_pass = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        registerView = inflater.inflate(R.layout.dialog_demo_registration, null);

        context = getActivity();
        Window window = getDialog().getWindow();

        // set "origin" to top left corner, so to speak
        window.setGravity(Gravity.CENTER | Gravity.CENTER);

        // after that, setting values for x and y works "naturally"
        WindowManager.LayoutParams params = window.getAttributes();

        params.y = 50;
        window.setAttributes(params);

        nameEdtTxt = (EditText) registerView.findViewById(R.id.nameEdtTxt);
        lastnameEdtTxt = (EditText) registerView.findViewById(R.id.lastNameEdtTxt);
        phEdtTxt = (EditText) registerView.findViewById(R.id.phEdtTxt);
        emailEdtTxt = (EditText) registerView.findViewById(R.id.emailEdtTxt);
        titleTxt = (TextView) registerView.findViewById(R.id.titleTxt);
        header = (TextView) registerView.findViewById(R.id.header);
        readContact = (ImageView) registerView.findViewById(R.id.readContact);
        displayCourseName = (Spinner) registerView.findViewById(R.id.displayCourseName);

        male = (RadioButton) registerView.findViewById(R.id.male);
        female = (RadioButton) registerView.findViewById(R.id.female);
        courseDetailsDAOArrayList = new ArrayList<>();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.
                Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        preferences = getActivity().getSharedPreferences("Prefrence", getActivity().MODE_PRIVATE);
        prefEditor = preferences.edit();
        if (!preferences.getString("dbatch_id", "").equals("")) {
            displayCourseName.setVisibility(View.GONE);
            batch_code=preferences.getString("dbatch_id", "");
        } else {
            displayCourseName.setVisibility(View.VISIBLE);
            batch_code="";
        }
        sms_user = preferences.getString("sms_username", "");
        sms_pass = preferences.getString("sms_password", "");
        // device_id = getDeviceId();
        placeBtn = (Button) registerView.findViewById(R.id.placeBtn);
//8-3-2017
        readContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

                startActivityForResult(intent, PICK_CONTACT);
            }
        });
        radioGroup = (RadioGroup) registerView.findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub

                // Method 1 For Getting Index of RadioButton
                pos = radioGroup.indexOfChild(registerView.findViewById(checkedId));
                switch (pos) {
                    case 1:

                        gender = "Female";
                        // Toast.makeText(getActivity(), "You have Clicked RadioButton 1"+gender,Toast.LENGTH_SHORT).show();
                        break;
                    case 2:

                        gender = "Male";
                        // Toast.makeText(getActivity(), gender, Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        //The default selection is RadioButton 1
                        gender = "Female";
                        // Toast.makeText(getActivity(), gender,Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        //

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        dataAdapter.add("Select gender");
        dataAdapter.add("He");
        dataAdapter.add("She");
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        spnrUserType = (Spinner) registerView.findViewById(R.id.spnrType);
        spnrUserType.setBackgroundColor(Color.parseColor("#234e5e"));
        spnrUserType.setAdapter(dataAdapter);


        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                    //Hide your keyboard here!!!
                    Toast.makeText(getActivity(), "PLease enter your information to get us connected with you.", Toast.LENGTH_LONG).show();
                    return true; // pretend we've processed it
                } else
                    return false; // pass on to be processed as normal
            }
        });

        placeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                f_name = nameEdtTxt.getText().toString();
                l_name = lastnameEdtTxt.getText().toString();
                phoneNumber = phEdtTxt.getText().toString();
                email_id = emailEdtTxt.getText().toString().trim();
                String spinnerSelected = spnrUserType.getSelectedItem().toString();
                String textToCopy = phEdtTxt.getText().toString().trim();
                int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(textToCopy);
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("myLabel", textToCopy);
                    clipboard.setPrimaryClip(clip);
                }

                if (validate(course_id, phoneNumber, f_name, l_name, email_id, gender)) {

                    if (AppStatus.getInstance(context).isOnline()) {
                        // dismiss();
                        new submitData().execute();
                    } else {

                        Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                    }


                } else {

                }

            }
        });
        if (AppStatus.getInstance(context).isOnline()) {
            new initCourseSpinner().execute();
        } else {

            Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
        }
        phEdtTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE, AUTO_COMPLETE_DELAY);
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(phEdtTxt.getText())) {
                        if (phEdtTxt.getText().toString().length() == 10) {
                            phoneNumber = phEdtTxt.getText().toString();
                            // Toast.makeText(getActivity(), phoneNumber, Toast.LENGTH_SHORT).show();
                            new availableStudent().execute();
                        } else {
                            phoneNumber = "";
                        }
                    } else {

                        phoneNumber = "";
                        nameEdtTxt.setText("");
                        lastnameEdtTxt.setText("");
                        phEdtTxt.setText("");
                        emailEdtTxt.setText("");
                        male.setChecked(false);
                        female.setChecked(false);
                    }
                }
                return false;
            }
        });
        return registerView;
    }


    //
    public boolean validate(String courseId, String phoneno, String name, String lastname, String emailid, String spinnerSelected) {
        boolean isValidate = false;
        if (courseId.equals("") && batch_code.equals("")) {
            Toast.makeText(getActivity(), "Please select Course.", Toast.LENGTH_LONG).show();
        } else if (phoneno.equals("")) {
            Toast.makeText(getActivity(), "Please enter mobile No.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (phoneno.equals("") || phoneno.length() != 10) {
            Toast.makeText(getActivity(), "Please enter a 10 digit valid Mobile No.", Toast.LENGTH_LONG).show();
            isValidate = false;
        } else if (name.trim().compareTo("") == 0) {
            Toast.makeText(getActivity(), "Please enter  first name", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (lastname.trim().compareTo("") == 0) {
            Toast.makeText(getActivity(), "Please enter  last name", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (emailid.trim().compareTo("") == 0) {
            Toast.makeText(getActivity(), "Please enter  Email Id", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (!validateEmail(emailid)) {
            if (!emailid.equals("")) {
                Toast.makeText(getActivity(), "Please enter valid Email Id.", Toast.LENGTH_LONG).show();
                isValidate = false;
            } else {
                isValidate = true;
            }
        } else if (spinnerSelected.trim().compareTo("") == 0) {
            Toast.makeText(getActivity(), "Please select gender.", Toast.LENGTH_LONG).show();
        } else {
            isValidate = true;
        }
        return isValidate;
    }

    /**
     * email validation
     */
    private final static Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(

            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$");

    public boolean validateEmail(String email) {
        if (!email.contains("@")) {
            return false;
        }
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }


    @Override
    public void onResume() {
        super.onResume();

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {

                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                    //This is the filter
                    if (event.getAction() != KeyEvent.ACTION_DOWN) {
                        // Toast.makeText(getActivity(), "Your information is valuable for us and won't be misused.", Toast.LENGTH_SHORT).show();
                        count++;
                        if (count >= 1) {
                            update1();
                        }
                        return true;
                    } else {
                        //Hide your keyboard here!!!!!!
                        return true; // pretend we've processed it
                    }
                } else
                    return false; // pass on to be processed as normal
            }
        });
    }

    private void update() {
        dismiss();
    }

    private void update1() {
        dismiss();
    }

    private class availableStudent extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            //  mProgressDialog = new ProgressDialog(context);
            // Set progressdialog title
            //    mProgressDialog.setTitle("Please Wait...");
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
                        put("mobile_no", phoneNumber);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();
            Log.i("json", "json" + jsonLeadObj);
            centerResponse = serviceAccess.SendHttpPost(Config.URL_AVAILABLESTUDENT, jsonLeadObj);
            Log.i("resp", "centerResponse" + centerResponse);

            if (centerResponse.compareTo("") != 0) {
                if (isJSONValid(centerResponse)) {


                    try {


                        JSONObject jsonObject = new JSONObject(centerResponse);
                        status1 = jsonObject.getBoolean("status");
                        if (status1) {
                            user_id = jsonObject.getString("user_id");

                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                } else {

                    Toast.makeText(context, "Please check your network connection", Toast.LENGTH_LONG).show();

                }
            } else {

                Toast.makeText(context, "Please check your network connection.", Toast.LENGTH_LONG).show();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            //  mProgressDialog.dismiss();
            if (status1) {
                // Toast.makeText(context, "Hello", Toast.LENGTH_LONG).show();

                new getData().execute();
            } else {


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

    public static void bindListener(SmsListener listener) {
        mListener = listener;
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


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();
            Log.i("json", "json" + jsonLeadObj);
            courseResponse = serviceAccess.SendHttpPost(Config.URL_GETALLCOURSES, jsonLeadObj);
            Log.i("resp", courseResponse);

            if (courseResponse.compareTo("") != 0) {
                if (isJSONValid(courseResponse)) {

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {


                                courseDetailsDAOArrayList.add(new CourseDetailsDAO("0", "Select Course", "", ""));
                                JSONArray LeadSourceJsonObj = new JSONArray(courseResponse);
                                for (int i = 0; i < LeadSourceJsonObj.length(); i++) {
                                    JSONObject json_data = LeadSourceJsonObj.getJSONObject(i);
                                    courseDetailsDAOArrayList.add(new CourseDetailsDAO(json_data.getString("id"), json_data.getString("course_name"), json_data.getString("course_type_id"), json_data.getString("course_code")));

                                }

                                jsonArray = new JSONArray(courseResponse);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Please check your network connection", Toast.LENGTH_LONG).show();
                        }
                    });

                    return null;
                }
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Please check your network connection.", Toast.LENGTH_LONG).show();
                    }
                });

                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (courseResponse.compareTo("") != 0) {

                // Spinner spinnerCustom = (Spinner) findViewById(R.id.spinnerBranch);
                ArrayAdapter<CourseDetailsDAO> adapter = new ArrayAdapter<CourseDetailsDAO>(getActivity(), R.layout.multiline_spinner_dropdown_item, courseDetailsDAOArrayList);
                // MyAdapter adapter = new MyAdapter(StudentsListActivity.this,R.layout.spinner_item,locationlist);
                displayCourseName.setAdapter(adapter);
                displayCourseName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#1c5fab"));
                        CourseDetailsDAO LeadSource = (CourseDetailsDAO) parent.getSelectedItem();
                        //   Toast.makeText(getApplicationContext(), "Source ID: " + LeadSource.getId() + ",  Source Name : " + LeadSource.getStatus(), Toast.LENGTH_SHORT).show();
                        course_id = LeadSource.getId();

                        if (!course_id.equals("0")) {
                        } else {
                            batch_code = "";
                            course_id = "";
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


    private class getData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getActivity());
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            //    mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {
                        put("user_id", user_id);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();


            Log.i("json", "json" + jsonLeadObj);
            bankDetailsResponse = serviceAccess.SendHttpPost(Config.URL_GET_USER_DETAILS, jsonLeadObj);
            Log.i("resp", "leadListResponse" + bankDetailsResponse);


            if (bankDetailsResponse.compareTo("") != 0) {
                if (isJSONValid(bankDetailsResponse)) {

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {

                                JSONObject jsonObject = new JSONObject(bankDetailsResponse);
                                f_name = jsonObject.getString("first_name");
                                l_name = jsonObject.getString("last_name");
                                email_id = jsonObject.getString("email_id");
                                mobile_no = jsonObject.getString("mobile_no");
                                gender = jsonObject.getString("gender");
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Please check your webservice", Toast.LENGTH_LONG).show();
                        }
                    });

                    return null;
                }
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Please check your network connection.", Toast.LENGTH_LONG).show();
                    }
                });

                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            // Close the progressdialog
            mProgressDialog.dismiss();
            nameEdtTxt.setText(f_name);
            lastnameEdtTxt.setText(l_name);
            //   phEdtTxt.setText(mobile_no);
            emailEdtTxt.setText(email_id);
            if (gender.equals("Male")) {
                male.setChecked(true);
            }
            if (gender.equals("Female")) {
                female.setChecked(true);
            }

        }
    }

    private class submitData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getActivity());
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Registering...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {

                        put("first_name", f_name);
                        put("last_name", l_name);
                        put("mobile_no", phoneNumber);
                        put("email_id", email_id);
                        put("gender", gender);
                        put("BatchID", batch_code);
                        put("CourseID", course_id);
                        put("know_from", "Trainer");
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();
            Log.i("json", "json" + jsonLeadObj);
            if (!batch_code.equals("")) {
                addStudentRespone = serviceAccess.SendHttpPost(Config.URL_ADDSTUDENTINBATCHDEMOBYTRAINER, jsonLeadObj);
            } else {
                addStudentRespone = serviceAccess.SendHttpPost(Config.URL_ADDSTUDENTINNOBATCHDEMOBYTRAINER, jsonLeadObj);
            }
            Log.i("resp", "addStudentRespone" + addStudentRespone);


            if (addStudentRespone.compareTo("") != 0) {
                if (isJSONValid(addStudentRespone)) {


                    try {

                        JSONObject jsonObject = new JSONObject(addStudentRespone);
                        status = jsonObject.getBoolean("status");
                        msg = jsonObject.getString("message");
                        if (status) {
                            String msg = "Thank you for your interest in " + course_name + " Demo Batch." + System.getProperty("line.separator") + System.getProperty("line.separator") +
                                    " We would be updating you details one day before batch Start.";
                            if (!batch_code.equals("")) {
                                String result = sendSms1(phoneNumber, msg);
                                Log.d("sent sms---->", result);
                            }

                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                } else {


                    Toast.makeText(getActivity(), "Please check your webservice", Toast.LENGTH_LONG).show();


                }
            } else {

                Toast.makeText(getActivity(), "Please check your network connection.", Toast.LENGTH_LONG).show();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            // Close the progressdialog
            mProgressDialog.dismiss();
            if (status) {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                dismiss();

            } else {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static String sendSms1(String tempMobileNumber, String message) {
        String sResult = null;
        try {
// Construct data
            //String phonenumbers = "9657816221";
            String data = "user=" + URLEncoder.encode(sms_user, "UTF-8");
            data += "&password=" + URLEncoder.encode(sms_pass, "UTF-8");
            data += "&message=" + URLEncoder.encode(message, "UTF-8");
            data += "&sender=" + URLEncoder.encode("AFCKST", "UTF-8");
            data += "&mobile=" + URLEncoder.encode(tempMobileNumber, "UTF-8");
            data += "&type=" + URLEncoder.encode("3", "UTF-8");
// Send data
            URL url = new URL("http://login.bulksmsgateway.in/sendmessage.php?" + data);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
// Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            String sResult1 = "";
            while ((line = rd.readLine()) != null) {
// Process line...
                sResult1 = sResult1 + line + " ";
            }
            wr.close();
            rd.close();
            return sResult1;
        } catch (Exception e) {
            System.out.println("Error SMS " + e);
            return "Error " + e;
        }
    }
}