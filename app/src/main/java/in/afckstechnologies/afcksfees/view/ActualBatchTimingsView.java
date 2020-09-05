package in.afckstechnologies.afcksfees.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.fragment.app.DialogFragment;
import in.afckstechnologies.afcksfees.R;
import in.afckstechnologies.afcksfees.model.BatchDAO;
import in.afckstechnologies.afcksfees.utils.AppStatus;
import in.afckstechnologies.afcksfees.utils.Config;
import in.afckstechnologies.afcksfees.utils.Constant;
import in.afckstechnologies.afcksfees.utils.SmsListener;
import in.afckstechnologies.afcksfees.utils.WebClient;


/**
 * Created by Ashok Kumawat on 11/29/2017.
 */

public class ActualBatchTimingsView extends DialogFragment {

    Button saveBtn, editBtn;
    Context context;
    ImageView clear_batch;
    String newTextBatch;
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;


    String studentResponse = "";
    public List<BatchDAO> batchArrayList;
    public ArrayAdapter<BatchDAO> aAdapter;


    private JSONObject jsonSchedule;
    private JSONObject jsonLeadObj, jsonObj;
    ProgressDialog mProgressDialog;

    JSONArray jsonArray;


    boolean status;
    String message = "";
    String msg = "";


    static String start_date = "", end_date = "NULL";

    String start_time = "";
    String end_time = "";
    static String datefalg = "";
    CheckBox chkAll, chkJDSelected;
    RelativeLayout footer;
    LinearLayout batchData, nextClassLay;
    String insertClassTimingsResponse = "", verifyTimingsResponse = "";
    private EditText cstimingsEdtTxt, cetimingsEdtTxt;
    private static EditText datePickerClassDate, datePickerNextClassDate;
    static String date = "", next_date = "";
    Button sendData;
    static SmsListener mListener;
    ImageView clearDate, clearStarttime, clearEndtime, clearNextClassDate;
    String batch_end_flag = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        View registerView = inflater.inflate(R.layout.dialog_actual_batch_timings, null);

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
        saveBtn = (Button) registerView.findViewById(R.id.saveBtn);

        footer = (RelativeLayout) registerView.findViewById(R.id.footer);
        batchData = (LinearLayout) registerView.findViewById(R.id.batchData);
        nextClassLay = (LinearLayout) registerView.findViewById(R.id.nextClassLay);
        clear_batch = (ImageView) registerView.findViewById(R.id.clear_batch);
        clearNextClassDate = (ImageView) registerView.findViewById(R.id.clearNextClassDate);
        sendData = (Button) registerView.findViewById(R.id.sendData);
        batchArrayList = new ArrayList<BatchDAO>();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");//yyyy-MM-dd hh:mm:ss
        Calendar cal = Calendar.getInstance();
        start_date = format.format(cal.getTime());
        datePickerClassDate = (EditText) registerView.findViewById(R.id.datePickerClassDate);
        datePickerNextClassDate = (EditText) registerView.findViewById(R.id.datePickerNextClassDate);
        cstimingsEdtTxt = (EditText) registerView.findViewById(R.id.cstimingsEdtTxt);
        cetimingsEdtTxt = (EditText) registerView.findViewById(R.id.cetimingsEdtTxt);

        clearDate = (ImageView) registerView.findViewById(R.id.clearDate);
        clearStarttime = (ImageView) registerView.findViewById(R.id.clearStarttime);
        clearEndtime = (ImageView) registerView.findViewById(R.id.clearEndtime);
        String newDate = formateDateFromstring("yyyy-MM-dd", "dd-MMM-yyyy", start_date);
        datePickerClassDate.setText(newDate);
       /* datePickerClassDate.setOnClickListener(new View.OnClickListener() {
            private int mYear, mMonth, mDay;
            private SimpleDateFormat dateFormatter;

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                datefalg = "startDate";

                // Initialize a new date picker dialog fragment
                DialogFragment dFragment = new DatePickerFragment();

                // Show the date picker dialog fragment
                dFragment.show(getFragmentManager(), "Date Picker");
            }

        });*/


        datePickerClassDate.setOnClickListener(new View.OnClickListener() {
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
                        datePickerClassDate.setText(dateFormatter.format(mcurrentDate.getTime()));
                        start_date = format.format(mcurrentDate.getTime());
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
            }
        });
        /*datePickerNextClassDate.setOnClickListener(new View.OnClickListener() {
            private int mYear, mMonth, mDay;
            private SimpleDateFormat dateFormatter;

            @Override
            public void onClick(View v) {
                datefalg = "nextDate";
                // Initialize a new date picker dialog fragment
                DialogFragment dFragment = new DatePickerFragment1();

                // Show the date picker dialog fragment
                dFragment.show(getFragmentManager(), "Date Picker");
            }

        });*/

        datePickerNextClassDate.setOnClickListener(new View.OnClickListener() {
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
                        datePickerNextClassDate.setText(dateFormatter.format(mcurrentDate.getTime()));
                        next_date = format.format(mcurrentDate.getTime());
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
            }
        });
        cstimingsEdtTxt.setOnClickListener(new View.OnClickListener() {
            private int mHours, mMinutes;

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Calendar mcurrentTime = Calendar.getInstance();
                mHours = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                mMinutes = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePickerDialog = new TimePickerDialog(getActivity(), new OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // TODO Auto-generated method stub
                        String output = String.format("%02d:%02d", hourOfDay, minute);
                        Log.d("output", output);
                        cstimingsEdtTxt.setText("" + hourOfDay + ":" + minute);
                    }
                }, mHours, mMinutes, false);
                mTimePickerDialog.setTitle("Select Time");
                mTimePickerDialog.show();
            }
        });
        cetimingsEdtTxt.setOnClickListener(new View.OnClickListener() {
            private int mHours, mMinutes;

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Calendar mcurrentTime = Calendar.getInstance();
                mHours = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                mMinutes = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePickerDialog = new TimePickerDialog(getActivity(), new OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // TODO Auto-generated method stub
                        String output = String.format("%02d:%02d", hourOfDay, minute);
                        Log.d("output", output);
                        cetimingsEdtTxt.setText("" + hourOfDay + ":" + minute);
                    }
                }, mHours, mMinutes, false);
                mTimePickerDialog.setTitle("Select Time");
                mTimePickerDialog.show();
            }
        });
        clearDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerClassDate.setText("");
                start_date = "";

            }
        });
        clearStarttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cstimingsEdtTxt.setText("");
                start_time = "";

            }
        });
        clearEndtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cetimingsEdtTxt.setText("");
                end_time = "";

            }
        });

        /** Getting reference to checkbox available in the main.xml layout */
        chkAll = (CheckBox) registerView.findViewById(R.id.chkSelected);
        /** Setting a click listener for the checkbox **/
        chkAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckBox cb = (CheckBox) v;
                if (cb.isChecked()) {
                    nextClassLay.setVisibility(View.GONE);
                    next_date = "0000-00-00";
                    // Toast.makeText(v.getContext(), "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked(), Toast.LENGTH_LONG).show();
                } else {
                    nextClassLay.setVisibility(View.VISIBLE);
                    next_date = "";
                    datePickerNextClassDate.setText("");
                    // Toast.makeText(v.getContext(), "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked(), Toast.LENGTH_LONG).show();
                }
            }
        });


        /** Getting reference to checkbox available in the main.xml layout */
        chkJDSelected = (CheckBox) registerView.findViewById(R.id.chkJDSelected);
        /** Setting a click listener for the checkbox **/
        chkJDSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckBox cb = (CheckBox) v;
                if (cb.isChecked()) {
                    batch_end_flag = "1";
                   // Toast.makeText(v.getContext(), "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked(), Toast.LENGTH_LONG).show();
                } else {
                    batch_end_flag = "0";
                   // Toast.makeText(v.getContext(), "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked(), Toast.LENGTH_LONG).show();
                }
            }
        });
        clearNextClassDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerNextClassDate.setText("");
                next_date = "";
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

                    start_time = cstimingsEdtTxt.getText().toString().trim();
                    end_time = cetimingsEdtTxt.getText().toString().trim();

                    if (validate(start_date, start_time, end_time, next_date)) {
                        verifyTimings();


                    }

                } else {

                    Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }


            }
        });


        return registerView;
    }


    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            /*
                We should use THEME_HOLO_LIGHT, THEME_HOLO_DARK or THEME_TRADITIONAL only.

                The THEME_DEVICE_DEFAULT_LIGHT and THEME_DEVICE_DEFAULT_DARK does not work
                perfectly. This two theme set disable color of disabled dates but users can
                select the disabled dates also.

                Other three themes act perfectly after defined enabled date range of date picker.
                Those theme completely hide the disable dates from date picker object.
             */
            DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_HOLO_LIGHT, this, year, month, day);

            /*
                add(int field, int value)
                    Adds the given amount to a Calendar field.
             */
            // Add 3 days to Calendar
            // calendar.add(Calendar.DATE, 3);

            /*
                getTimeInMillis()
                    Returns the time represented by this Calendar,
                    recomputing the time from its fields if necessary.

                getDatePicker()
                Gets the DatePicker contained in this dialog.

                setMinDate(long minDate)
                    Sets the minimal date supported by this NumberPicker
                    in milliseconds since January 1, 1970 00:00:00 in getDefault() time zone.

                setMaxDate(long maxDate)
                    Sets the maximal date supported by this DatePicker in milliseconds
                    since January 1, 1970 00:00:00 in getDefault() time zone.
             */

            // Set the Calendar new date as maximum date of date picker
            dpd.getDatePicker().setMaxDate(calendar.getTimeInMillis());

            // Subtract 6 days from Calendar updated date
            calendar.add(Calendar.DATE, -6);

            // Set the Calendar new date as minimum date of date picker
            //  dpd.getDatePicker().setMinDate(calendar.getTimeInMillis());

            // So, now date picker selectable date range is 7 days only

            // Return the DatePickerDialog
            return dpd;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the chosen date

            // Create a Date variable/object with user chosen date
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(0);
            cal.set(year, month, day, 0, 0, 0);
            Date chosenDate = cal.getTime();

            // Format the date using style and locale
            DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
            String formattedDate = dateFormatter.format(chosenDate);
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            // Display the chosen date to app interface
            //  tv.setText(formattedDate);
            datePickerClassDate.setText(formattedDate);
            date = format.format(chosenDate);
            start_date = date;
        }
    }

    public static class DatePickerFragment1 extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            /*
                We should use THEME_HOLO_LIGHT, THEME_HOLO_DARK or THEME_TRADITIONAL only.

                The THEME_DEVICE_DEFAULT_LIGHT and THEME_DEVICE_DEFAULT_DARK does not work
                perfectly. This two theme set disable color of disabled dates but users can
                select the disabled dates also.

                Other three themes act perfectly after defined enabled date range of date picker.
                Those theme completely hide the disable dates from date picker object.
             */
            DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_HOLO_LIGHT, this, year, month, day);

            /*
                add(int field, int value)
                    Adds the given amount to a Calendar field.
             */
            // Add 3 days to Calendar
            // calendar.add(Calendar.DATE, 3);

            /*
                getTimeInMillis()
                    Returns the time represented by this Calendar,
                    recomputing the time from its fields if necessary.

                getDatePicker()
                Gets the DatePicker contained in this dialog.

                setMinDate(long minDate)
                    Sets the minimal date supported by this NumberPicker
                    in milliseconds since January 1, 1970 00:00:00 in getDefault() time zone.

                setMaxDate(long maxDate)
                    Sets the maximal date supported by this DatePicker in milliseconds
                    since January 1, 1970 00:00:00 in getDefault() time zone.
             */

            // Set the Calendar new date as maximum date of date picker
            dpd.getDatePicker().setMinDate(calendar.getTimeInMillis());

            // Subtract 6 days from Calendar updated date
            calendar.add(Calendar.DATE, -6);

            // Set the Calendar new date as minimum date of date picker
            //  dpd.getDatePicker().setMinDate(calendar.getTimeInMillis());

            // So, now date picker selectable date range is 7 days only

            // Return the DatePickerDialog
            return dpd;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the chosen date

            // Create a Date variable/object with user chosen date
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(0);
            cal.set(year, month, day, 0, 0, 0);
            Date chosenDate = cal.getTime();

            // Format the date using style and locale
            DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
            String formattedDate = dateFormatter.format(chosenDate);
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            // Display the chosen date to app interface
            //  tv.setText(formattedDate);
            if (datefalg.equals("startDate")) {
                datePickerClassDate.setText(formattedDate);
                date = format.format(chosenDate);
                start_date = date;
            }
            if (datefalg.equals("nextDate")) {
                datePickerNextClassDate.setText(formattedDate);
                date = format.format(chosenDate);
                next_date = date;
            }

        }
    }

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

    public boolean validate(String start_date, String start_time, String end_time, String next_date) {

        boolean isValidate = false;
        if (start_date.equals("")) {
            Toast.makeText(getActivity(), "Please select class starting date.", Toast.LENGTH_LONG).show();
        } else if (start_time.equals("")) {
            Toast.makeText(getActivity(), "Please select class start timing.", Toast.LENGTH_LONG).show();
        } else if (end_time.equals("")) {
            Toast.makeText(getActivity(), "Please select class end timing.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (next_date.equals("")) {
            Toast.makeText(getActivity(), "Please select next class date.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else {
            isValidate = true;
        }
        return isValidate;
    }

    private class initClassDetailsUpdate extends AsyncTask<Void, Void, Void> {
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
                        put("batch_id", preferences.getString("batch_class", ""));
                        put("batch_date", start_date);
                        put("start_time", start_time);
                        put("end_time", end_time);
                        put("next_date", next_date);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();

            //  String baseURL = "http://192.168.1.13:8088/lms/api/lead/showlead";
            Log.i("json", "json" + jsonLeadObj);
            insertClassTimingsResponse = serviceAccess.SendHttpPost(Config.URL_ADDACTUALBATCHTIMINGS, jsonLeadObj);
            Log.i("resp", "addTemplateResponse" + insertClassTimingsResponse);

            if (insertClassTimingsResponse.compareTo("") != 0) {
                if (isJSONValid(insertClassTimingsResponse)) {

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {
                                JSONObject jsonObject = new JSONObject(insertClassTimingsResponse);
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
                            Toast.makeText(getActivity(), "Already Marked Attendance", Toast.LENGTH_LONG).show();
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
            if (!insertClassTimingsResponse.equals("")) {

                if (status) {
                    // Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                    mProgressDialog.dismiss();
                    prefEditor.putString("check_mark_attendance_date", start_date);
                    prefEditor.commit();
                    dismiss();
                    mListener.messageReceived(batch_end_flag);
                } else {

                    //   Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
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

    public void verifyTimings() {


        jsonObj = new JSONObject() {
            {
                try {
                    put("start_time", start_time);
                    put("end_time", end_time);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Thread objectThread = new Thread(new Runnable() {
            public void run() {
                // TODO Auto-generated method stub
                WebClient serviceAccess = new WebClient();
                verifyTimingsResponse = serviceAccess.SendHttpPost(Config.URL_GETAVAILABLEISTIMEINRANGE, jsonObj);
                Log.i("verifyTimingsResponse", verifyTimingsResponse);
                final Handler handler = new Handler(Looper.getMainLooper());
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() { // This thread runs in the UI
                            @Override
                            public void run() {
                                if (verifyTimingsResponse.compareTo("") == 0) {

                                } else {

                                    try {
                                        JSONObject jObject = new JSONObject(verifyTimingsResponse);
                                        status = jObject.getBoolean("status");

                                        if (status) {
                                            new initClassDetailsUpdate().execute();
                                        } else {
                                            Toast.makeText(getActivity(), "Please select current timings ", Toast.LENGTH_SHORT).show();

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


}