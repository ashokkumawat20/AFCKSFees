package in.afckstechnologies.afcksfees.view;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
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


public class BankingDetailsAddView extends DialogFragment {

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
    String pay_date = "", fees = "", feesMode = "", t_ref = "", studentName = "", batchno = "", flag = "";
    String lastFiveDigits = "";     //substring containing last 5 characters

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        View registerView = inflater.inflate(R.layout.dialog_banking_details_add, null);

        context = getActivity();
        Window window = getDialog().getWindow();

        // set "origin" to top left corner, so to speak
        window.setGravity(Gravity.CENTER | Gravity.CENTER);

        // after that, setting values for x and y works "naturally"
        WindowManager.LayoutParams params = window.getAttributes();

        params.y = 50;
        window.setAttributes(params);

        feesEdtTxt = (EditText) registerView.findViewById(R.id.feesEdtTxt);
        RefNOEdtTxt = (EditText) registerView.findViewById(R.id.RefNOEdtTxt);
        studentNameEdtTxt = (EditText) registerView.findViewById(R.id.studentNameEdtTxt);
        batchNoEdtTxt = (EditText) registerView.findViewById(R.id.batchNoEdtTxt);
        datePickerPayDate = (EditText) registerView.findViewById(R.id.datePickerPayDate);
        spinnerFeesMode = (Spinner) registerView.findViewById(R.id.spinnerFeesMode);
        preferences = getActivity().getSharedPreferences("Prefrence", getActivity().MODE_PRIVATE);
        prefEditor = preferences.edit();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");//yyyy-MM-dd hh:mm:ss
        Calendar cal = Calendar.getInstance();
        pay_date = format.format(cal.getTime());
        String newDate = formateDateFromstring("yyyy-MM-dd", "dd-MMM-yyyy", pay_date);
        datePickerPayDate.setText(newDate);
        datePickerPayDate.setOnClickListener(new View.OnClickListener() {
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
                        datePickerPayDate.setText(dateFormatter.format(mcurrentDate.getTime()));
                        pay_date = format.format(mcurrentDate.getTime());
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
            }
        });
        saveBtn = (Button) registerView.findViewById(R.id.saveBtn);


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

                fees = feesEdtTxt.getText().toString().trim();
                t_ref = RefNOEdtTxt.getText().toString().trim();

                studentName = studentNameEdtTxt.getText().toString().trim();
                batchno = batchNoEdtTxt.getText().toString().trim();
                if (AppStatus.getInstance(context).isOnline()) {
                    if (validate(studentName, batchno, fees, t_ref)) {
                        if (click) {
                            sendData(fees);
                            click = false;
                        } else {
                            Toast.makeText(context, "Please Wait...", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {

                    Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }


            }
        });


        return registerView;
    }

    public boolean validate(String studentName, String batchno, String fees, String t_ref) {
        boolean isValidate = false;
        if (studentName.equals("")) {
            Toast.makeText(getActivity(), "Please enter Student Name.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (batchno.equals("")) {
            Toast.makeText(getActivity(), "Please enter Batch No.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (fees.equals("")) {
            Toast.makeText(getActivity(), "Please enter fees.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (t_ref.equals("")) {
            Toast.makeText(getActivity(), "Please enter reference no.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else {
            isValidate = true;
        }
        return isValidate;
    }


    public void sendData(final String comment) {
        if (t_ref.length() > 5) {
            lastFiveDigits = t_ref.substring(t_ref.length() - 5);
        } else {
            lastFiveDigits = t_ref;
        }
        if (preferences.getString("trainer_user_id", "").equals("RS")) {
            flag = "1";
        } else {
            flag = "0";
        }
        jsonObj = new JSONObject() {
            {
                try {
                    put("Entered_by", preferences.getString("trainer_user_id", ""));
                    put("Trans_ref", studentName + " " + lastFiveDigits);
                    put("Trans_type", "null");
                    put("Trans_date", pay_date);
                    put("Rec_amount", fees);
                    put("batch_id", batchno);
                    put("student_name", studentName);
                    put("status", flag);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Thread objectThread = new Thread(new Runnable() {
            public void run() {
                // TODO Auto-generated method stub
                WebClient serviceAccess = new WebClient();

                Log.i("jsonObj", "jsonObj" + jsonObj);
                loginResponse = serviceAccess.SendHttpPost(Config.URL_ADDBANKRECEIPT, jsonObj);
                Log.i("loginResponse", "loginResponse" + loginResponse);
                final Handler handler = new Handler(Looper.getMainLooper());
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() { // This thread runs in the UI
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void run() {
                                if (loginResponse.compareTo("") == 0) {
                                    Toast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();
                                } else {

                                    try {
                                        JSONObject jObject = new JSONObject(loginResponse);
                                        status = jObject.getBoolean("status");
                                        message = jObject.getString("message");
                                        if (status) {
                                            Calendar c = Calendar.getInstance();
                                            SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa");
                                            String datetime = dateformat.format(c.getTime());
                                            prefEditor.putString("recipetDateEntry", "Last Updated " + datetime);
                                            prefEditor.commit();
                                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                            mListener.messageReceived(message);
                                            dismiss();

                                        } else {
                                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                            dismiss();
                                        }
                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                };

                new Thread(runnable).start();
            }
        });
        objectThread.start();
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

    //
    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            //LOGE(TAG, "ParseException - dateFormat");
        }

        return outputDate;

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