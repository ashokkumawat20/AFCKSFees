package in.afckstechnologies.afcksfees.view;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import in.afckstechnologies.afcksfees.R;
import in.afckstechnologies.afcksfees.model.RequestChangeUsersNameDAO;
import in.afckstechnologies.afcksfees.utils.AppStatus;
import in.afckstechnologies.afcksfees.utils.Config;
import in.afckstechnologies.afcksfees.utils.Constant;
import in.afckstechnologies.afcksfees.utils.SmsListener;
import in.afckstechnologies.afcksfees.utils.WebClient;


public class FeesTypeViewForAdmin extends DialogFragment {

    Button saveBtn;
    private EditText feesEdtTxt, RefNOEdtTxt, datePickerPayDate, studentNameEdtTxt, batchNoEdtTxt;
    private Spinner spinnerFeesMode;
    ArrayList<RequestChangeUsersNameDAO> userslist;
    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    String loginResponse = "";

    String message;
    int user_id;
    boolean click = true;
    String studentcommentListResponse = "";
    JSONObject jsonObj;
    Boolean status;
    int count = 0;
    View registerView;
    private JSONObject jsonLeadObj;
    ProgressDialog mProgressDialog;
    JSONArray jsonArray;
    public static SmsListener mListener;
    String pay_date = "", fees = "", feesMode = "", t_ref = "", studentName = "", centerListResponse = "", flag = "";
    String lastFiveDigits = "";     //substring containing last 5 characters

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        View registerView = inflater.inflate(R.layout.dialog_fee_type_admin_add, null);

        context = getActivity();
        Window window = getDialog().getWindow();

        // set "origin" to top left corner, so to speak
        window.setGravity(Gravity.CENTER | Gravity.CENTER);

        // after that, setting values for x and y works "naturally"
        WindowManager.LayoutParams params = window.getAttributes();

        params.y = 50;
        window.setAttributes(params);
        spinnerFeesMode = (Spinner) registerView.findViewById(R.id.spinnerFeesMode);
        preferences = getActivity().getSharedPreferences("Prefrence", getActivity().MODE_PRIVATE);

        saveBtn = (Button) registerView.findViewById(R.id.saveBtn);
        spinnerFeesMode = (Spinner) registerView.findViewById(R.id.spinnerFeesMode);

        userslist = new ArrayList<>();
        userslist.add(new RequestChangeUsersNameDAO("0", "Select Fee Type", "0", "0"));
        userslist.add(new RequestChangeUsersNameDAO("0", "Cheque", "0", "0"));
        userslist.add(new RequestChangeUsersNameDAO("0", "EFT", "0", "0"));
        userslist.add(new RequestChangeUsersNameDAO("0", "UPI", "0", "0"));
        userslist.add(new RequestChangeUsersNameDAO("0", "Cash Deposit", "0", "0"));
        userslist.add(new RequestChangeUsersNameDAO("0", "Paytm", "0", "0"));
        userslist.add(new RequestChangeUsersNameDAO("0", "Cash Voucher", "0", "0"));
        ArrayAdapter<RequestChangeUsersNameDAO> adapter1 = new ArrayAdapter<RequestChangeUsersNameDAO>(getActivity(), android.R.layout.simple_spinner_dropdown_item, userslist);
        spinnerFeesMode.setAdapter(adapter1);
        spinnerFeesMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#1c5fab"));
                RequestChangeUsersNameDAO LeadSource = (RequestChangeUsersNameDAO) parent.getSelectedItem();
                // Toast.makeText(getApplicationContext(), "Source ID: " + LeadSource.getId() + ",  Source Name : " + LeadSource.getLocation_name(), Toast.LENGTH_SHORT).show();
                feesMode = LeadSource.getUser_name();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

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
                    // Toast.makeText(getActivity(), "PLease enter your information to get us connected with you.", Toast.LENGTH_LONG).show();
                    return true; // pretend we've processed it
                } else
                    return false; // pass on to be processed as normal
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (AppStatus.getInstance(context).isOnline()) {
                    if (validate(feesMode)) {

                      new  updateStatus().execute();
                    }

                } else {

                    Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }


            }
        });


        return registerView;
    }

    public boolean validate(String feesMode) {
        boolean isValidate = false;
        if (feesMode.equals("Select Fee Type")) {
            Toast.makeText(getActivity(), "Please select fees type.", Toast.LENGTH_LONG).show();
            isValidate = false;

        }  else {
            isValidate = true;
        }
        return isValidate;
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
                        update();

                        // Toast.makeText(getActivity(), "Your information is valuable for us and won't be misused.", Toast.LENGTH_LONG).show();
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


    //
    private class updateStatus extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(context);
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Updating...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {
                        put("status", "1");
                        put("trans_type", feesMode);
                        put("id", preferences.getString("bid", ""));


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };

            WebClient serviceAccess = new WebClient();


            Log.i("json", "json" + jsonLeadObj);
            centerListResponse = serviceAccess.SendHttpPost(Config.URL_UPDATEBANKINGSTATUS, jsonLeadObj);
            Log.i("resp", "leadListResponse" + centerListResponse);


            if (centerListResponse.compareTo("") != 0) {
                if (isJSONValid(centerListResponse)) {

                    try {

                        JSONObject jObject = new JSONObject(centerListResponse);
                        status = jObject.getBoolean("status");
                        message = jObject.getString("message");
                        jsonArray = new JSONArray(centerListResponse);

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
            // Close the progressdialog
            mProgressDialog.dismiss();
            if (status) {
                //  removeAt(ID);
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                // Close the progressdialog
                mListener.messageReceived(message);
                dismiss();
            } else {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();


            }

        }
    }

    //
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


}