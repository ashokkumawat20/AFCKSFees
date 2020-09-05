package in.afckstechnologies.afcksfees.view;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import androidx.fragment.app.DialogFragment;
import in.afckstechnologies.afcksfees.R;
import in.afckstechnologies.afcksfees.model.BatchDAO;
import in.afckstechnologies.afcksfees.model.LocationDAO;
import in.afckstechnologies.afcksfees.utils.AppStatus;
import in.afckstechnologies.afcksfees.utils.Config;
import in.afckstechnologies.afcksfees.utils.Constant;
import in.afckstechnologies.afcksfees.utils.SmsListener;
import in.afckstechnologies.afcksfees.utils.WebClient;


/**
 * Created by Ashok Kumawat on 11/29/2017.
 */

public class BatchModifyView extends DialogFragment {

    Button saveBtn, editBtn;
    Context context;
    ImageView clear_batch;
    String newTextBatch;
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;

    private Spinner spinnerBranch;
    String studentResponse = "";
    public List<BatchDAO> batchArrayList;
    public ArrayAdapter<BatchDAO> aAdapter;
    public BatchDAO batchDAO;
    String batch_id = "";
    private JSONObject jsonSchedule;
    private JSONObject jsonLeadObj, jsonLeadObj1;
    ProgressDialog mProgressDialog;
    JSONArray jsonArray;
    boolean status;
    String message = "";
    String msg = "";
    ArrayList<LocationDAO> locationlist;
    String start_date = "", end_date = "";
    String notes = "";
    String timings = "";
    String frequency = "";
    String duration = "";
    String fees = "";
    String ref_batch = "";
    RelativeLayout footer;
    LinearLayout batchData;
    String updateBatchResponse = "", centerResponse = "", center_id = "",wa_invite_link="",meeting_link="",discount="";
    private EditText dpicker, timingsEdtTxt, frequencyEdtTxt, feesEdtTxt, durationEdtTxt, commentEdtTxt, datePickerEndBatch, refBatchEdtTxt, discountEdtTxt, meetinglinkEdtTxt, walinkEdtTxt;
    String date = "";
    Button sendData;
    static SmsListener mListener;
    ImageView clearDate;

    //Edit lead
    HashMap<String, String> edit_LeadLOCList;

    //Keys
    Set keys_Loc;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        View registerView = inflater.inflate(R.layout.dialog_update_batch_modify, null);

        context = getActivity();
        Window window = getDialog().getWindow();

        // set "origin" to top left corner, so to speak
        window.setGravity(Gravity.CENTER | Gravity.CENTER);

        // after that, setting values for x and y works "naturally"
        WindowManager.LayoutParams params = window.getAttributes();

        params.y = 50;
        window.setAttributes(params);

        commentEdtTxt = (EditText) registerView.findViewById(R.id.commentEdtTxt);
        preferences = getActivity().getSharedPreferences("Prefrence", getActivity().MODE_PRIVATE);
        prefEditor = preferences.edit();
        saveBtn = (Button) registerView.findViewById(R.id.saveBtn);

        footer = (RelativeLayout) registerView.findViewById(R.id.footer);
        batchData = (LinearLayout) registerView.findViewById(R.id.batchData);
        clear_batch = (ImageView) registerView.findViewById(R.id.clear_batch);
        sendData = (Button) registerView.findViewById(R.id.sendData);
        batchArrayList = new ArrayList<BatchDAO>();
        spinnerBranch = (Spinner) registerView.findViewById(R.id.spinnerBranch);
        dpicker = (EditText) registerView.findViewById(R.id.datePickerCal);
        datePickerEndBatch = (EditText) registerView.findViewById(R.id.datePickerEndBatch);
        timingsEdtTxt = (EditText) registerView.findViewById(R.id.timingsEdtTxt);
        frequencyEdtTxt = (EditText) registerView.findViewById(R.id.frequencyEdtTxt);
        feesEdtTxt = (EditText) registerView.findViewById(R.id.feesEdtTxt);
        durationEdtTxt = (EditText) registerView.findViewById(R.id.durationEdtTxt);
        commentEdtTxt = (EditText) registerView.findViewById(R.id.commentEdtTxt);
        refBatchEdtTxt = (EditText) registerView.findViewById(R.id.refBatchEdtTxt);
        discountEdtTxt = (EditText) registerView.findViewById(R.id.discountEdtTxt);
        meetinglinkEdtTxt = (EditText) registerView.findViewById(R.id.meetinglinkEdtTxt);
        walinkEdtTxt = (EditText) registerView.findViewById(R.id.walinkEdtTxt);
        clearDate = (ImageView) registerView.findViewById(R.id.clearDate);
        newTextBatch = preferences.getString("batch_mofiy", "");
        getBatchSelect(newTextBatch);
        dpicker.setOnClickListener(new View.OnClickListener() {
            private int mYear, mMonth, mDay;
            private SimpleDateFormat dateFormatter;

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);

                final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        mcurrentDate.set(selectedyear, selectedmonth, selectedday);
                        dpicker.setText(dateFormatter.format(mcurrentDate.getTime()));
                        date = format.format(mcurrentDate.getTime());
                        start_date = date;
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
            }
        });

        datePickerEndBatch.setOnClickListener(new View.OnClickListener() {
            private int mYear, mMonth, mDay;
            private SimpleDateFormat dateFormatter;

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);

                final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        mcurrentDate.set(selectedyear, selectedmonth, selectedday);
                        datePickerEndBatch.setText(dateFormatter.format(mcurrentDate.getTime()));
                        date = format.format(mcurrentDate.getTime());
                        end_date = date;
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
            }
        });
        clearDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerEndBatch.setText("");
                end_date = "";
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

                    timings = timingsEdtTxt.getText().toString().trim();
                    frequency = frequencyEdtTxt.getText().toString().trim();
                    fees = feesEdtTxt.getText().toString().trim();
                    duration = durationEdtTxt.getText().toString().trim();
                    notes = commentEdtTxt.getText().toString().trim();
                    ref_batch = refBatchEdtTxt.getText().toString().trim();
                    discount = discountEdtTxt.getText().toString().trim();
                    meeting_link = meetinglinkEdtTxt.getText().toString().trim();
                    wa_invite_link = walinkEdtTxt.getText().toString().trim();

                    if (validate(start_date, timings, frequency, fees, duration, ref_batch)) {
                        new initBatchUpdate().execute();

                    }

                } else {

                    Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }


            }
        });


        return registerView;
    }

    public void getBatchSelect(final String channelPartnerSelect) {

        jsonSchedule = new JSONObject() {
            {
                try {
                    put("Prefixtext", channelPartnerSelect);
                    put("user_id", preferences.getString("trainer_user_id", ""));
                    //put("user_id", "RS");

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
              //  String baseURL = "http://sales.pibm.net:86/service.svc/CallListService/GetChannelPartner";
                Log.i("json", "json" + jsonSchedule);
                //SEND RESPONSE
                // studentResponse = serviceAccess.SendHttpPost(baseURL, jsonSchedule);
                studentResponse = serviceAccess.SendHttpPost(Config.URL_GETALLBATCHMODIFYBYPREFIX, jsonSchedule);
                Log.i("resp", "loginResponse" + studentResponse);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray callArrayList = new JSONArray(studentResponse);
                            batchArrayList.clear();
                            // user_id="";
                            for (int i = 0; i < callArrayList.length(); i++) {
                                batchDAO = new BatchDAO();
                                JSONObject json_data = callArrayList.getJSONObject(i);
                                batchArrayList.add(new BatchDAO(json_data.getString("id"), json_data.getString("course_id"), json_data.getString("branch_id"), json_data.getString("batch_code"), json_data.getString("start_date"), json_data.getString("timings"), json_data.getString("Notes"), json_data.getString("frequency"), json_data.getString("fees"), json_data.getString("duration"), json_data.getString("course_name"), json_data.getString("branch_name"), json_data.getString("batchtype"), json_data.getString("completion_status"), json_data.getString("batch_end_date"), json_data.getString("email_id"), json_data.getString("mobile_no"), json_data.getString("faculty_Name"), json_data.getString("ref_batch")));
                                start_date = json_data.getString("start_date");
                                batch_id = json_data.getString("id");
                                dpicker.setText(formateDateFromstring("yyyy-MM-dd", "dd-MMM-yyyy", json_data.getString("start_date")));
                                timingsEdtTxt.setText(json_data.getString("timings"));
                                frequencyEdtTxt.setText(json_data.getString("frequency"));
                                feesEdtTxt.setText(json_data.getString("fees"));
                                durationEdtTxt.setText(json_data.getString("duration"));
                                commentEdtTxt.setText(json_data.getString("Notes"));
                                refBatchEdtTxt.setText(json_data.getString("ref_batch"));
                                discountEdtTxt.setText(json_data.getString("discount"));
                                meetinglinkEdtTxt.setText(json_data.getString("meeting_link"));
                                walinkEdtTxt.setText(json_data.getString("wa_invite_link"));
                                if (!json_data.getString("batch_end_date").equals("null") && !json_data.getString("batch_end_date").equals("0000-00-00")) {
                                    end_date = json_data.getString("batch_end_date");
                                    datePickerEndBatch.setText(formateDateFromstring("yyyy-MM-dd", "dd-MMM-yyyy", json_data.getString("batch_end_date")));
                                }

                                edit_LeadLOCList = new HashMap<String, String>();
                                edit_LeadLOCList.put(json_data.getString("branch_id"), json_data.getString("branch_name"));
                                keys_Loc = edit_LeadLOCList.keySet();
                                new initBranchSpinner().execute();
                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                });


            }
        });

        objectThread.start();

    }

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            //LOGE(TAG, "ParseException - dateFormat");
        }

        return outputDate;

    }

    public boolean validate(String start_date, String timings, String frequency, String fees, String duration, String ref_batch) {

        boolean isValidate = false;
        if (start_date.equals("")) {
            Toast.makeText(getActivity(), "Please select Batch Starting Date.", Toast.LENGTH_LONG).show();
        } else if (timings.equals("")) {
            Toast.makeText(getActivity(), "Please enter batch timings.", Toast.LENGTH_LONG).show();
        } else if (frequency.equals("")) {
            Toast.makeText(getActivity(), "Please enter  Frequency", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (fees.trim().equals("")) {
            Toast.makeText(getActivity(), "Please enter  Fees", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (duration.trim().equals("")) {
            Toast.makeText(getActivity(), "Please enter  Duration", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (ref_batch.trim().equals("")) {
            Toast.makeText(getActivity(), "Please enter  Reference Batch No.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else {
            isValidate = true;
        }
        return isValidate;
    }

    private class initBatchUpdate extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getActivity());
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Updating Batch...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {
                        put("id", batch_id);
                        put("start_date", start_date);
                        put("timings", timings);
                        put("frequency", frequency);
                        put("fees", fees);
                        put("duration", duration);
                        put("notes", notes);
                        put("end_date", end_date);
                        put("branch_id", center_id);
                        put("ref_batch", ref_batch);
                        put("discount", discount);
                        put("meeting_link",meeting_link);
                        put("wa_invite_link", wa_invite_link);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();

            //  String baseURL = "http://192.168.1.13:8088/lms/api/lead/showlead";
            Log.i("json", "json" + jsonLeadObj);
            updateBatchResponse = serviceAccess.SendHttpPost(Config.URL_UPDATEBATCHDETAILS, jsonLeadObj);
            Log.i("resp", "addTemplateResponse" + updateBatchResponse);

            if (updateBatchResponse.compareTo("") != 0) {
                if (isJSONValid(updateBatchResponse)) {

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {
                                JSONObject jsonObject = new JSONObject(updateBatchResponse);
                                msg = jsonObject.getString("message");
                                status = jsonObject.getBoolean("status");

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
            if (updateBatchResponse.compareTo("") != 0) {

                if (status) {
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();

                    // finish();
                    dismiss();
                    mProgressDialog.dismiss();
                } else {

                    Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                    mProgressDialog.dismiss();
                    dismiss();
                }
                mProgressDialog.dismiss();
                dismiss();
            } else {
                // Close the progressdialog
                mProgressDialog.dismiss();
                dismiss();
            }
        }
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

    public static void bindListener(SmsListener listener) {
        mListener = listener;
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

    //
    private class initBranchSpinner extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getActivity());
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
                        //  put("user_id", "" + preferences.getInt("user_id", 0));
                        // put("branch_id", flag);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();

            //  String baseURL = "http://192.168.1.13:8088/lms/api/lead/showlead";
            Log.i("json", "json" + jsonLeadObj);
            centerResponse = serviceAccess.SendHttpPost(Config.URL_DISPLAY_CENTER, jsonLeadObj);
            Log.i("resp", "leadListResponse" + centerResponse);

            if (centerResponse.compareTo("") != 0) {
                if (isJSONValid(centerResponse)) {

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {

                                locationlist = new ArrayList<>();
                                locationlist.add(new LocationDAO("0", "Select Branch"));

                                JSONArray LeadSourceJsonObj = new JSONArray(centerResponse);
                                for (int i = 0; i < LeadSourceJsonObj.length(); i++) {
                                    JSONObject json_data = LeadSourceJsonObj.getJSONObject(i);
                                    locationlist.add(new LocationDAO(json_data.getString("id"), json_data.getString("branch_name")));

                                }

                                jsonArray = new JSONArray(centerResponse);

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
            if (centerResponse.compareTo("") != 0) {

                String key_loc = "";
                String value_loc = "";
                if (!keys_Loc.isEmpty()) {
                    for (Iterator i = keys_Loc.iterator(); i.hasNext(); ) {
                        key_loc = (String) i.next();
                        value_loc = (String) edit_LeadLOCList.get(key_loc);
                        Log.d("keys ", "" + key_loc + " = " + value_loc);
                    }

                }
                ArrayAdapter<LocationDAO> adapter = new ArrayAdapter<LocationDAO>(getActivity(), android.R.layout.simple_spinner_dropdown_item, locationlist);
                // MyAdapter adapter = new MyAdapter(StudentsListActivity.this,R.layout.spinner_item,locationlist);
                spinnerBranch.setAdapter(adapter);
                selectSpinnerItemByValue(spinnerBranch, value_loc);
                spinnerBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#1c5fab"));
                        LocationDAO LeadSource = (LocationDAO) parent.getSelectedItem();
                        //  Toast.makeText(getApplicationContext(), "Source ID: " + LeadSource.getId() + ",  Source Name : " + LeadSource.getBranch_name(), Toast.LENGTH_SHORT).show();
                        center_id = LeadSource.getId();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }


                });
                mProgressDialog.dismiss();
            } else {
                // Close the progressdialog
                mProgressDialog.dismiss();
            }
        }
    }

    private void selectSpinnerItemByValue(Spinner spinner, String value) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

}