package in.afckstechnologies.afcksfees.view;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.List;
import java.util.Locale;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.afckstechnologies.afcksfees.R;
import in.afckstechnologies.afcksfees.activity.StudentList;
import in.afckstechnologies.afcksfees.adapter.StudentFeesDetailsListAdpter;
import in.afckstechnologies.afcksfees.jsonparser.JsonHelper;
import in.afckstechnologies.afcksfees.model.StudentsFeesDetailsDAO;
import in.afckstechnologies.afcksfees.utils.Config;
import in.afckstechnologies.afcksfees.utils.FeesListener;
import in.afckstechnologies.afcksfees.utils.WebClient;


public class FeesDetailsView extends DialogFragment {


    Context context;
    SharedPreferences preferences;
    Editor prefEditor;
    String studentfeesListResponse = "";
    JSONObject jsonObj;
    Boolean status;
    int count = 0;
    View registerView;
    private JSONObject jsonLeadObj, jsonLeadObjSaveDate;
    ProgressDialog mProgressDialog;
    JSONArray jsonArray;
    StudentFeesDetailsListAdpter studentFeesDetailsListAdpter;
    private RecyclerView mstudentList;
    List<StudentsFeesDetailsDAO> data;
    ImageView closeFeesDetails;
    LinearLayout feesDetails;
    TextView titleTxt, titleDateTxt;
    LinearLayout clickForModifydate;
    EditText nextDueAmount, nextDueDateDate;
    Button updateDueDate;
    String next_due_date = "", saveDateResponse = "", message = "";
    String dueAmount = "";
    private static FeesListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        registerView = inflater.inflate(R.layout.dialog_fees_details, null);

        context = getActivity();
        Window window = getDialog().getWindow();

        // set "origin" to top left corner, so to speak
        window.setGravity(Gravity.CENTER | Gravity.CENTER);

        // after that, setting values for x and y works "naturally"
        WindowManager.LayoutParams params = window.getAttributes();

        params.y = 50;
        window.setAttributes(params);
        preferences = getActivity().getSharedPreferences("Prefrence", getActivity().MODE_PRIVATE);
        prefEditor = preferences.edit();
        mstudentList = (RecyclerView) registerView.findViewById(R.id.feesdetailsList);
        closeFeesDetails = (ImageView) registerView.findViewById(R.id.closeFeesDetails);
        feesDetails = (LinearLayout) registerView.findViewById(R.id.feesDetails);
        titleTxt = (TextView) registerView.findViewById(R.id.titleTxt);
        titleDateTxt = (TextView) registerView.findViewById(R.id.titleDateTxt);
        clickForModifydate = (LinearLayout) registerView.findViewById(R.id.clickForModifydate);
        nextDueAmount = (EditText) registerView.findViewById(R.id.nextDueAmount);
        nextDueDateDate = (EditText) registerView.findViewById(R.id.nextDueDateDate);
        updateDueDate = (Button) registerView.findViewById(R.id.updateDueDate);
        if (!preferences.getString("check_fees", "").equals("0")) {
            feesDetails.setVisibility(View.VISIBLE);
        }
        if (preferences.getString("da", "").equals("0")) {
            titleTxt.setText("Due Amount Rs.0");
            titleDateTxt.setVisibility(View.GONE);
        }
        if (preferences.getString("da", "").equals("null")) {
            titleTxt.setText("Due Amount Rs.0");
            titleDateTxt.setVisibility(View.GONE);
        } else {
            titleTxt.setText("Due Amount Rs." + preferences.getString("da", ""));
            titleDateTxt.setText("Due Date." + preferences.getString("ndd", ""));
        }
        new getStudentFeesDetailsList().execute();
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);

        closeFeesDetails.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        getDialog().setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                    //Hide your keyboard here!!!
                    //Toast.makeText(getActivity(), "PLease enter your information to get us connected with you.", Toast.LENGTH_LONG).show();
                    return true; // pretend we've processed it
                } else
                    return false; // pass on to be processed as normal
            }
        });


        clickForModifydate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(getActivity(), "Hello", Toast.LENGTH_LONG).show();
                if (preferences.getString("trainer_user_id", "").equals("RS")) {
                    nextDueAmount.setVisibility(View.VISIBLE);
                    nextDueAmount.setText(preferences.getString("da", ""));
                }
                nextDueDateDate.setVisibility(View.VISIBLE);
                updateDueDate.setVisibility(View.VISIBLE);
                feesDetails.setVisibility(View.GONE);

                nextDueDateDate.setText(preferences.getString("ndd", ""));

                next_due_date= formateDateFromstring("dd-MMM-yyyy", "yyyy-MM-dd", preferences.getString("ndd", ""));

            }
        });

        nextDueDateDate.setOnClickListener(new View.OnClickListener() {
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
                        nextDueDateDate.setText(dateFormatter.format(mcurrentDate.getTime()));
                        next_due_date = format.format(mcurrentDate.getTime());
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
            }
        });

        updateDueDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(getActivity(), next_due_date, Toast.LENGTH_LONG).show();
                new updateData().execute();
            }
        });
        return registerView;
    }

    private class updateData extends AsyncTask<Void, Void, Void> {
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

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... params) {

            if (preferences.getString("trainer_user_id", "").equals("RS")) {
                dueAmount = nextDueAmount.getText().toString().trim();
            } else {
                dueAmount = preferences.getString("da", "");
            }

            jsonLeadObjSaveDate = new JSONObject() {
                {
                    try {
                        put("ndd", next_due_date);
                        put("BID", preferences.getString("batchId", ""));
                        put("UID", preferences.getString("id_fees", ""));
                        put("da", dueAmount);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };


            WebClient serviceAccess = new WebClient();
            saveDateResponse = serviceAccess.SendHttpPost(Config.URL_UPDATEDUEDATEREMINDERUSINGSTOREPROC, jsonLeadObjSaveDate);
            Log.i("resp", "saveOrderResponse" + saveDateResponse);
            if (isJSONValid(saveDateResponse)) {


                try {

                    JSONObject jsonObject = new JSONObject(saveDateResponse);
                    status = jsonObject.getBoolean("status");
                    message = jsonObject.getString("message");

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } else {

                Toast.makeText(getActivity(), "Please check your network connection.", Toast.LENGTH_LONG).show();

            }
            return null;

        }

        @Override
        protected void onPostExecute(Void args) {

            if (status) {
                // mListener.messageReceived(message1);
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                // Close the progressdialog
                mProgressDialog.dismiss();
                dismiss();
                mListener.messageReceived(message);
            } else {
                // Close the progressdialog
                mProgressDialog.dismiss();
            }
        }
    }

    //
    private class getStudentFeesDetailsList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(context);
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
                        put("user_id", preferences.getString("id_fees", ""));
                        put("BatchNo", preferences.getString("batchId", ""));

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();

            Log.i("json", "json" + jsonLeadObj);
            studentfeesListResponse = serviceAccess.SendHttpPost(Config.URL_GETALL_FEES_DETAILSINBATCH, jsonLeadObj);
            Log.i("resp", "studentfeesListResponse" + studentfeesListResponse);
            if (studentfeesListResponse.compareTo("") != 0) {
                if (isJSONValid(studentfeesListResponse)) {

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {

                                data = new ArrayList<>();
                                JsonHelper jsonHelper = new JsonHelper();
                                data = jsonHelper.parseStudentFessDetailsList(studentfeesListResponse);
                                jsonArray = new JSONArray(studentfeesListResponse);

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
            if (studentfeesListResponse.compareTo("") != 0) {
                studentFeesDetailsListAdpter = new StudentFeesDetailsListAdpter(getActivity(), data);
                mstudentList.setAdapter(studentFeesDetailsListAdpter);
                mstudentList.setLayoutManager(new LinearLayoutManager(getActivity()));
                studentFeesDetailsListAdpter.notifyDataSetChanged();
                mProgressDialog.dismiss();
            } else {
                // Close the progressdialog
                mProgressDialog.dismiss();
            }
        }
    }

    private void update() {
        Intent intent = new Intent(context, StudentList.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        getDialog().setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {

                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                    //This is the filter
                    if (event.getAction() != KeyEvent.ACTION_DOWN) {
                        //Toast.makeText(getActivity(), "Your information is valuable for us and won't be misused.", Toast.LENGTH_SHORT).show();
                        count++;
                        if (count >= 1) {
                            // update();
                            dismiss();
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

    public static void bindListener(FeesListener listener) {
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
}