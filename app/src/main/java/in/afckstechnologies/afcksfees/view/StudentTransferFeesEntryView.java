package in.afckstechnologies.afcksfees.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.fragment.app.DialogFragment;
import in.afckstechnologies.afcksfees.MailAPI.GMailSender;
import in.afckstechnologies.afcksfees.R;
import in.afckstechnologies.afcksfees.model.BatchDAO;
import in.afckstechnologies.afcksfees.utils.AppStatus;
import in.afckstechnologies.afcksfees.utils.Config;
import in.afckstechnologies.afcksfees.utils.Constant;
import in.afckstechnologies.afcksfees.utils.FeesListener;
import in.afckstechnologies.afcksfees.utils.WebClient;


public class StudentTransferFeesEntryView extends DialogFragment {
    // LogCat tag
    private static final String TAG = StudentTransferFeesEntryView.class.getSimpleName();
    AutoCompleteTextView autoCompleteTextViewBatch;
    Button placeBtn, proposalBtn;
    private TextView nameEdtTxt;
    private EditText trfeesEdtTxt, ttrfeesEdtTxt;
    private EditText remarksEdtTxt;
    private Spinner spnrUserType;
    Context context;
    SharedPreferences preferences;
    Editor prefEditor;
    String loginResponse = "", cashBackAmountResponse = "";
    JSONObject jsonObj, jsonObjCash, jsonCompany, jsonSchedule;
    Boolean status;
    String msg = "";
    boolean click = true;
    int count = 0;
    View registerView;
    String ReceivedBy = "";
    private static FeesListener mListener;
    private static final String username = "info@afcks.com";
    private static String password = "at!@#123";
    GMailSender sender;
    String emailid = "";
    String subject = "Receipt";
    String message = "";
    String fileName = "";
    String pathName = "";

    RadioGroup radioGroup;
    int pos;
    String accountType = "Non Corporate";
    String strName = "";
    String code = "";
    LinearLayout companyLayout;
    ImageView add_comapny;
    AutoCompleteTextView SearchCompany;
    String newTextCompany = "";
    String companyResponse = "";

    public ArrayAdapter<BatchDAO> bAdapter;


    String state_id = "", company_id = "";
    Double feesAmount = 0.0;
    Double cashbackAmount = 0.0;
    TextView Totalfees;
    Double IGST = 0.0, SGST = 0.0, CGST = 0.0, Rate = 0.0, MOS = 0.0;

    String pdfAmount = "";
    String corporate_id = "";

    String comapny_name = "", company_address = "", company_state = "", company_city = "", company_gstno;

    int batch_fees;
    String expiry_date = "";
    String cashBackAmount = "";

    String casBackExpiryDate = "";


    String selectedPath1 = "NONE";
    int statusCode;

    private JSONObject jsonReqObj;
    JSONArray jsonArray, jsonarray;
    String addExpenditureResponse = "";
    private ProgressBar progressBar;
    long totalSize = 0;

    String value_image1 = "";

    ArrayList<Uri> receiptUri = new ArrayList<Uri>();

    String cashbackamount = "";
    Button availCashback;

    ProgressDialog mProgressDialog;
    private JSONObject jsonLeadObj;

    String availCashbackResponse = "";
    String newTextBatch;
    static String sms_user = "";
    static String sms_pass = "";
    String flag = "", studentResponse = "", batch_id = "", tfees = "", ttfees;
    public List<BatchDAO> batchArrayList;
    BatchDAO batchDAO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        registerView = inflater.inflate(R.layout.dialog_student_transfer_fees, null);

        context = getActivity();
        Window window = getDialog().getWindow();

        // set "origin" to top left corner, so to speak
        window.setGravity(Gravity.CENTER | Gravity.CENTER);

        // after that, setting values for x and y works "naturally"
        WindowManager.LayoutParams params = window.getAttributes();

        params.y = 50;
        window.setAttributes(params);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.
                Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // Add your mail Id and Password
        sender = new GMailSender(username, password);
        nameEdtTxt = (TextView) registerView.findViewById(R.id.nameEdtTxt);
        trfeesEdtTxt = (EditText) registerView.findViewById(R.id.trfeesEdtTxt);
        ttrfeesEdtTxt = (EditText) registerView.findViewById(R.id.ttrfeesEdtTxt);
        remarksEdtTxt = (EditText) registerView.findViewById(R.id.remarksEdtTxt);
        companyLayout = (LinearLayout) registerView.findViewById(R.id.companyLayout);
        add_comapny = (ImageView) registerView.findViewById(R.id.add_comapny);
        availCashback = (Button) registerView.findViewById(R.id.availCashback);
        proposalBtn = (Button) registerView.findViewById(R.id.proposalBtn);
        preferences = getActivity().getSharedPreferences("Prefrence", getActivity().MODE_PRIVATE);
        prefEditor = preferences.edit();
        batchArrayList = new ArrayList<BatchDAO>();
        sms_user = preferences.getString("sms_username", "");
        sms_pass = preferences.getString("sms_password", "");
        cashbackamount = preferences.getString("cashBackAmount", "");
        if (!cashbackamount.equals("")) {
            //  feesEdtTxt.setText(cashbackamount);
            availCashback.setVisibility(View.VISIBLE);
            availCashback.append("Rs. " + cashbackamount);
        }

        nameEdtTxt.setText(preferences.getString("student_name", ""));
        emailid = preferences.getString("mail_id", "");
        corporate_id = preferences.getString("corporate", "");
        expiry_date = preferences.getString("expiry_date", "");

        batch_fees = Integer.parseInt(preferences.getString("batch_fees", ""));



        Totalfees = (TextView) registerView.findViewById(R.id.Totalfees);
        autoCompleteTextViewBatch = (AutoCompleteTextView) registerView.findViewById(R.id.SearchBatch);
        autoCompleteTextViewBatch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newTextBatch = s.toString();
                getBatchSelect(newTextBatch);


            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        SearchCompany = (AutoCompleteTextView) registerView.findViewById(R.id.SearchCompany);
        SearchCompany.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newTextCompany = s.toString();
                // getCompanySelect(newTextCompany);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        placeBtn = (Button) registerView.findViewById(R.id.placeBtn);
        CheckBox not = (CheckBox) registerView.findViewById(R.id.chkSelected);
        CheckBox notm = (CheckBox) registerView.findViewById(R.id.chkSelectedm);
        if (preferences.getString("send_sms", "").equals("sendsms")) {
            not.setChecked(true);
            prefEditor.putString("current_send_sms", "sendsms");
            prefEditor.commit();
        } else if (preferences.getString("send_sms", "").equals("notsendsms")) {
            not.setChecked(false);
            prefEditor.putString("current_send_sms", "notsendsms");
            prefEditor.commit();
        }
        if (preferences.getString("send_mail", "").equals("sendmail")) {
            notm.setChecked(true);
            prefEditor.putString("current_send_mail", "sendmail");
            prefEditor.commit();
        } else if (preferences.getString("send_mail", "").equals("notsendmail")) {
            notm.setChecked(false);
            prefEditor.putString("current_send_mail", "notsendmail");
            prefEditor.commit();
        }
        not.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {

                    Log.d("True", "Value");
                    prefEditor.putString("current_send_sms", "sendsms");
                    prefEditor.commit();

                } else {
                    Log.d("False", "Value");
                    prefEditor.putString("current_send_sms", "notsendsms");
                    prefEditor.commit();
                }
            }
        });
        notm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {

                    Log.d("True", "Value");
                    prefEditor.putString("current_send_mail", "sendmail");
                    prefEditor.commit();

                } else {
                    Log.d("False", "Value");
                    prefEditor.putString("current_send_mail", "notsendmail");
                    prefEditor.commit();
                }
            }
        });




        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item);
        dataAdapter.add("Select Fee Type");
        dataAdapter.add("Cash");
        dataAdapter.add("Refund");
        dataAdapter.add("Cheque");
        dataAdapter.add("EFT");
        dataAdapter.add("Old Student");
        dataAdapter.add("Discount");
        dataAdapter.add("Cash Deposit");
        dataAdapter.add("Paytm AFCKS");
        dataAdapter.add("Paytm Self");
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnrUserType = (Spinner) registerView.findViewById(R.id.spnrType);
        spnrUserType.setBackgroundColor(Color.parseColor("#234e5e"));
        spnrUserType.setAdapter(dataAdapter);


        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);

        getDialog().setOnKeyListener(new OnKeyListener() {

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

        placeBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                String name = nameEdtTxt.getText().toString();
                tfees = trfeesEdtTxt.getText().toString();
                ttfees = ttrfeesEdtTxt.getText().toString();
                String remarks = remarksEdtTxt.getText().toString();
                String spinnerSelectedFeeStatus = "";
                //  String spinnerSelected = spnrUserType.getSelectedItem().toString();

                // Toast.makeText(getActivity(), accountType,Toast.LENGTH_SHORT).show();
                if (AppStatus.getInstance(context).isOnline()) {
                    if (validate(batch_id, tfees, ttfees, remarks)) {
                        // dismiss();
                        if (Integer.parseInt(tfees) >= Integer.parseInt(ttfees)) {
                            if (click) {
                                //Toast.makeText(context, "Thank You! your information is added Successfully", Toast.LENGTH_LONG).show();
                                sendData(name, spinnerSelectedFeeStatus, remarks);
                                click = false;
                            } else {
                                Toast.makeText(context, "Please Wait...", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Do not allow transfer amount more then total amount", Toast.LENGTH_SHORT).show();
                        }


                    }
                } else {

                    Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }

            }
        });

        proposalBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEdtTxt.getText().toString();
                tfees = trfeesEdtTxt.getText().toString();
                ttfees = ttrfeesEdtTxt.getText().toString();
                String remarks = remarksEdtTxt.getText().toString();
                String spinnerSelectedFeeStatus = "";
                //  String spinnerSelected = spnrUserType.getSelectedItem().toString();

                // Toast.makeText(getActivity(), accountType,Toast.LENGTH_SHORT).show();
                if (AppStatus.getInstance(context).isOnline()) {
                    if (validate(batch_id, tfees, ttfees, remarks)) {
                        // dismiss();
                        if (Integer.parseInt(tfees) >= Integer.parseInt(ttfees)) {
                            if (click) {
                              //  Toast.makeText(context, "Thank You! your information is added Successfully", Toast.LENGTH_LONG).show();
                                if (batch_id.equals("RSAE099")) {
                                    batch_id = " temporary Batch *" + batch_id + "*.";
                                } else {
                                    batch_id = " Batch *" + batch_id + "*.";
                                }
                                String sms = "Hi " + preferences.getString("trainer_name", "") + " ," + System.getProperty("line.separator") + System.getProperty("line.separator")
                                        + "*Proposal for transfer*" + System.getProperty("line.separator")+ System.getProperty("line.separator")
                                        + "*" + preferences.getString("student_name", "") + "*" + " need to be transferred from your Batch no " + "*" + preferences.getString("out_batch", "") + "*" + " to " + batch_id + System.getProperty("line.separator") + System.getProperty("line.separator")
                                        + "You have received *Rs " + tfees + "*" + " and I proposal transfer of *Rs " + ttfees + "* to other Batch, Reason: *" + remarks + "*.";

                                PackageManager packageManager = context.getPackageManager();
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                try {
                                    String url = "https://api.whatsapp.com/send?phone=" + "91" + preferences.getString("trainer_mobile_no", "") + "&text=" + URLEncoder.encode(sms, "UTF-8");
                                    i.setPackage("com.whatsapp");
                                    i.setData(Uri.parse(url));
                                    if (i.resolveActivity(packageManager) != null) {
                                        context.startActivity(i);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                click = false;
                            } else {
                                Toast.makeText(context, "Please Wait...", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Do not allow transfer amount more then total amount", Toast.LENGTH_SHORT).show();
                        }


                    }
                } else {

                    Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return registerView;
    }


    public boolean validate(String batch_id, String tfees, String ttfees, String remarks) {
        boolean isValidate = false;
        if (batch_id.equals("")) {
            Toast.makeText(getActivity(), "Please select Batch.", Toast.LENGTH_LONG).show();
        } else if (tfees.equals("")) {
            Toast.makeText(getActivity(), "Please enter  Total Fees", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (ttfees.equals("")) {
            Toast.makeText(getActivity(), "Please enter  Transfer Fees", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (remarks.equals("")) {
            Toast.makeText(getActivity(), "Please enter  Remarks", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else {
            isValidate = true;
        }
        return isValidate;
    }


    public void sendData(final String name, final String spinnerSelectedFeeStatus, final String remarks) {


        jsonObj = new JSONObject() {
            {
                try {
                    put("OutBatch", preferences.getString("out_batch", ""));
                    put("InBatch", batch_id);
                    put("TotalRec", tfees);
                    put("TransferAmt", ttfees);
                    put("TUserID", preferences.getString("user_id", ""));
                    put("Note", remarks);
                    put("MobileNo", preferences.getString("mobile_no", ""));
                    put("PayStatus", spinnerSelectedFeeStatus);
                    put("UserName", preferences.getString("trainer_user_name", ""));
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
                loginResponse = serviceAccess.SendHttpPost(Config.URL_ADDSTUDENTINBATCHTRANSFERFROMOLDBATCH, jsonObj);
                Log.i("loginResponse", "loginResponse" + loginResponse);
                final Handler handler = new Handler(Looper.getMainLooper());
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() { // This thread runs in the UI
                            @Override
                            public void run() {
                                if (loginResponse.compareTo("") == 0) {
                                    Toast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();
                                } else {

                                    try {

                                        JSONObject jObject = new JSONObject(loginResponse);
                                        status = jObject.getBoolean("status");
                                        msg = jObject.getString("message");
                                        if (status) {
                                            Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                                            mListener.messageReceived(msg);
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

    public static void bindListener(FeesListener listener) {
        mListener = listener;
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



    public void getBatchSelect(final String channelPartnerSelect) {
        String value = "";
        String value1 = "";
        if (channelPartnerSelect.startsWith("$")) {
            value1 = "1";
            value = channelPartnerSelect.substring(1);
        } else {
            value1 = "2";
            value = channelPartnerSelect;

        }

        final String finalValue = value;
        flag = value1;
        jsonSchedule = new JSONObject() {
            {
                try {
                    put("Prefixtext", finalValue);

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
                String baseURL = "http://sales.pibm.net:86/service.svc/CallListService/GetChannelPartner";
                Log.i("json", "json" + jsonSchedule);
                //SEND RESPONSE
                // studentResponse = serviceAccess.SendHttpPost(baseURL, jsonSchedule);
                studentResponse = serviceAccess.SendHttpPost(Config.URL_GETALLSTUDENTTRANSFERBATCHTRAINERBYPREFIX, jsonSchedule);
                Log.i("resp", "loginResponse" + studentResponse);


                try {
                    JSONArray callArrayList = new JSONArray(studentResponse);
                    batchArrayList.clear();
                    // user_id="";
                    for (int i = 0; i < callArrayList.length(); i++) {
                        batchDAO = new BatchDAO();
                        JSONObject json_data = callArrayList.getJSONObject(i);
                        batchArrayList.add(new BatchDAO(json_data.getString("id"), json_data.getString("course_id"), json_data.getString("branch_id"), json_data.getString("batch_code"), json_data.getString("start_date"), json_data.getString("timings"), json_data.getString("Notes"), json_data.getString("frequency"), json_data.getString("fees"), json_data.getString("duration"), json_data.getString("course_name"), json_data.getString("branch_name"), json_data.getString("batchtype"), json_data.getString("completion_status"), json_data.getString("batch_end_date"), json_data.getString("email_id"), json_data.getString("mobile_no"), json_data.getString("faculty_Name"), json_data.getString("ref_batch")));

                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        bAdapter = new ArrayAdapter<BatchDAO>(context, R.layout.item, batchArrayList);
                        autoCompleteTextViewBatch.setAdapter(bAdapter);

                        autoCompleteTextViewBatch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                                // String s = parent.getItemAtPosition(i).toString();
                                BatchDAO student = (BatchDAO) parent.getAdapter().getItem(i);
                                String date_after = formateDateFromstring("yyyy-MM-dd", "dd-MMM-yyyy", student.getStart_date());
                                //  Toast.makeText(getApplicationContext(), "Source ID: " +date_after+"   "+ LeadSource.getId() + ",  Source Name : " + LeadSource.getBatch_code(), Toast.LENGTH_SHORT).show();

                                batch_id = student.getId();
                                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                            }
                        });
                        bAdapter.notifyDataSetChanged();

                    }
                });


            }
        });

        objectThread.start();

    }

}