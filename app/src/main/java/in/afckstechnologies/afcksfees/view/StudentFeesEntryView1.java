package in.afckstechnologies.afcksfees.view;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.fragment.app.DialogFragment;
import in.afckstechnologies.afcksfees.MailAPI.GMailSender1;
import in.afckstechnologies.afcksfees.R;
import in.afckstechnologies.afcksfees.model.BankingdetailsDAO;
import in.afckstechnologies.afcksfees.model.LocationDAO;
import in.afckstechnologies.afcksfees.model.RequestChangeUsersNameDAO;
import in.afckstechnologies.afcksfees.utils.AppStatus;
import in.afckstechnologies.afcksfees.utils.Config;
import in.afckstechnologies.afcksfees.utils.Constant;
import in.afckstechnologies.afcksfees.utils.FeesListener;
import in.afckstechnologies.afcksfees.utils.WebClient;


public class StudentFeesEntryView1 extends DialogFragment {
    // LogCat tag
    private static final String TAG = StudentFeesEntryView1.class.getSimpleName();
    Button placeBtn;
    private TextView nameEdtTxt, mobileEdtTxt;
    private EditText feesEdtTxt, duefeesEdtTxt, refundEdtTxt, ChequeNoEdtTxt, RefNOEdtTxt, oldBatchNoEdtTxt, PaytmNoEdtTxt, UPINoEdtTxt, CASHNoEdtTxt, cashbackNOEdtTxt;
    private Spinner spinnerFeesMode, spinnerBranch, spinnerSubfeesMode;
    Context context;
    SharedPreferences preferences;
    Editor prefEditor;
    String loginResponse = "", bankingDetailsResponse = "", centerResponse = "";
    JSONObject jsonObj, jsonObjCash, jsonLeadObj1;
    Boolean status;
    String msg = "";
    boolean click = true;
    int count = 0;
    View registerView;
    String ReceivedBy = "";
    String ReceivedBy_id = "";
    private static FeesListener mListener;
    private static final String username = "info@afcks.com";
    private static String password = "at!@#123";
    GMailSender1 sender;
    String emailid = "";
    // String subject = "Receipt";
    String subject = "";
    String message = "";
    String strName = "";
    String code = "";
    LinearLayout companyLayout, eft, cheque, paytm, upi, refund, hideForDueFees, oldStudent, CashDeposit, cashback;
    ImageView add_comapny;
    Double feesAmount = 0.0;
    Double cashbackAmount = 0.0;
    String corporate_id = "";
    int batch_fees;
    String expiry_date = "";
    JSONArray jsonArray, jsonarray;
    Button availCashback;
    ProgressDialog mProgressDialog;
    private JSONObject jsonLeadObj;
    static String sms_user = "";
    static String sms_pass = "";
    ArrayList<RequestChangeUsersNameDAO> userslist;
    String spinnerSelected = "", technologySelected = "NEFT";
    String reverseFlag = "";
    String dueFees = "", sendingmail = "", smsSent = "", dueFees1 = "";
    ImageView noncorporateButton, corporateButton, mos, moscorporate;
    String remarks = "";
    private static EditText datePickerPayDate, datePickerChequeDate, datePickerDateofReceipt, datePickerDepositDate, datePickerPaymentDate, datePickerNextPayDate, datePickerUPIPaymentDate, datePickerCashbackDate;
    String pay_date = "", cheque_date = "", date_of_receipt = "", center_id = "", case_deposit_date = "", paytm_date = "", upi_date = "", next_pay_date = "", next_cashback_date = "";
    ArrayList<LocationDAO> locationlist;
    String refundTxt = "", check_no = "", cash_trans_no = "", bank_name = "", issuer_name = "", ref_no = "", old_batch_no = "", paytm_no = "", upi_no = "", next_pay_comments = "", next_cashback_comments = "", cash_no = "";
    int unableflag = 0;
    Double total_amount = 0.0;
    ArrayList<BankingdetailsDAO> bankingdetailsDAOArrayList;
    String bd_id = "", adj_amount = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        registerView = inflater.inflate(R.layout.dialog_student_fees, null);

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
        sender = new GMailSender1(username, password);
        preferences = getActivity().getSharedPreferences("Prefrence", getActivity().MODE_PRIVATE);
        prefEditor = preferences.edit();
        eft = (LinearLayout) registerView.findViewById(R.id.eft);
        cheque = (LinearLayout) registerView.findViewById(R.id.cheque);
        paytm = (LinearLayout) registerView.findViewById(R.id.paytm);
        upi = (LinearLayout) registerView.findViewById(R.id.upi);
        refund = (LinearLayout) registerView.findViewById(R.id.refund);
        hideForDueFees = (LinearLayout) registerView.findViewById(R.id.hideForDueFees);
        CashDeposit = (LinearLayout) registerView.findViewById(R.id.CashDeposit);
        oldStudent = (LinearLayout) registerView.findViewById(R.id.oldStudent);
        cashback = (LinearLayout) registerView.findViewById(R.id.cashback);
        nameEdtTxt = (TextView) registerView.findViewById(R.id.nameEdtTxt);
        mobileEdtTxt = (TextView) registerView.findViewById(R.id.mobileEdtTxt);
        feesEdtTxt = (EditText) registerView.findViewById(R.id.feesEdtTxt);
        CASHNoEdtTxt = (EditText) registerView.findViewById(R.id.CASHNoEdtTxt);
        cashbackNOEdtTxt = (EditText) registerView.findViewById(R.id.cashbackNOEdtTxt);
        spinnerBranch = (Spinner) registerView.findViewById(R.id.spinnerBranch);
        companyLayout = (LinearLayout) registerView.findViewById(R.id.companyLayout);
        add_comapny = (ImageView) registerView.findViewById(R.id.add_comapny);
        availCashback = (Button) registerView.findViewById(R.id.availCashback);
        spinnerFeesMode = (Spinner) registerView.findViewById(R.id.spinnerFeesMode);
        spinnerSubfeesMode = (Spinner) registerView.findViewById(R.id.spinnerSubfeesMode);
        duefeesEdtTxt = (EditText) registerView.findViewById(R.id.duefeesEdtTxt);
        duefeesEdtTxt.setEnabled(false);
        duefeesEdtTxt.setClickable(false);
        datePickerPayDate = (EditText) registerView.findViewById(R.id.datePickerPayDate);
        datePickerChequeDate = (EditText) registerView.findViewById(R.id.datePickerChequeDate);
        datePickerDateofReceipt = (EditText) registerView.findViewById(R.id.datePickerDateofReceipt);
        datePickerDepositDate = (EditText) registerView.findViewById(R.id.datePickerDepositDate);
        datePickerPaymentDate = (EditText) registerView.findViewById(R.id.datePickerPaymentDate);
        datePickerNextPayDate = (EditText) registerView.findViewById(R.id.datePickerNextPayDate);
        datePickerUPIPaymentDate = (EditText) registerView.findViewById(R.id.datePickerUPIPaymentDate);
        datePickerCashbackDate = (EditText) registerView.findViewById(R.id.datePickerCashbackDate);
        refundEdtTxt = (EditText) registerView.findViewById(R.id.refundEdtTxt);
        ChequeNoEdtTxt = (EditText) registerView.findViewById(R.id.ChequeNoEdtTxt);
        RefNOEdtTxt = (EditText) registerView.findViewById(R.id.RefNOEdtTxt);
        oldBatchNoEdtTxt = (EditText) registerView.findViewById(R.id.oldBatchNoEdtTxt);
        PaytmNoEdtTxt = (EditText) registerView.findViewById(R.id.PaytmNoEdtTxt);
        UPINoEdtTxt = (EditText) registerView.findViewById(R.id.UPINoEdtTxt);
        noncorporateButton = (ImageView) registerView.findViewById(R.id.noncorporateButton);
        corporateButton = (ImageView) registerView.findViewById(R.id.corporateButton);
        mos = (ImageView) registerView.findViewById(R.id.mos);
        moscorporate = (ImageView) registerView.findViewById(R.id.moscorporate);
        if (!preferences.getString("fees_amount", "").equals("null")) {
            total_amount = Double.parseDouble(preferences.getString("fees_amount", ""));
        }
        sms_user = preferences.getString("sms_username", "");
        sms_pass = preferences.getString("sms_password", "");

        nameEdtTxt.setText(preferences.getString("student_name", ""));
        emailid = preferences.getString("mail_id", "");
        corporate_id = preferences.getString("corporate", "");
        expiry_date = preferences.getString("expiry_date", "");
        batch_fees = Integer.parseInt(preferences.getString("batch_fees", ""));

        if (preferences.getString("trainer_user_id", "").equals("RS")) {
            spinnerFeesMode.setVisibility(View.VISIBLE);
            spinnerSelected = "";
        } else {
            spinnerFeesMode.setVisibility(View.GONE);
            spinnerSelected = "Cash";
        }

        if (corporate_id.equals("1")) {
            noncorporateButton.setVisibility(View.VISIBLE);
            corporateButton.setVisibility(View.GONE);
            mos.setVisibility(View.GONE);
            moscorporate.setVisibility(View.GONE);
        }
        if (corporate_id.equals("2")) {
            noncorporateButton.setVisibility(View.GONE);
            corporateButton.setVisibility(View.VISIBLE);
            mos.setVisibility(View.GONE);
            moscorporate.setVisibility(View.GONE);
        }
        if (corporate_id.equals("3")) {
            noncorporateButton.setVisibility(View.VISIBLE);
            corporateButton.setVisibility(View.GONE);
            mos.setVisibility(View.GONE);
            moscorporate.setVisibility(View.GONE);
        }
        if (corporate_id.equals("4")) {
            noncorporateButton.setVisibility(View.VISIBLE);
            corporateButton.setVisibility(View.GONE);
            mos.setVisibility(View.GONE);
            moscorporate.setVisibility(View.GONE);
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");//yyyy-MM-dd hh:mm:ss
        Calendar cal = Calendar.getInstance();
        pay_date = format.format(cal.getTime());
        cheque_date = format.format(cal.getTime());
        date_of_receipt = format.format(cal.getTime());
        case_deposit_date = format.format(cal.getTime());
        paytm_date = format.format(cal.getTime());
        String newDate = formateDateFromstring("yyyy-MM-dd", "dd-MMM-yyyy", pay_date);
        datePickerPayDate.setText(newDate);
        datePickerChequeDate.setText(newDate);
        datePickerDateofReceipt.setText(newDate);
        datePickerDepositDate.setText(newDate);
        datePickerPaymentDate.setText(newDate);
        if (AppStatus.getInstance(getActivity()).isOnline()) {
            new initSubFeesModeSpinner().execute();
        } else {

            Toast.makeText(getContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
        }

        datePickerPayDate.setOnClickListener(new OnClickListener() {
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

        datePickerChequeDate.setOnClickListener(new OnClickListener() {
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
                        datePickerChequeDate.setText(dateFormatter.format(mcurrentDate.getTime()));
                        cheque_date = format.format(mcurrentDate.getTime());
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
            }
        });


        datePickerDateofReceipt.setOnClickListener(new OnClickListener() {
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
                        datePickerDateofReceipt.setText(dateFormatter.format(mcurrentDate.getTime()));
                        date_of_receipt = format.format(mcurrentDate.getTime());
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
            }
        });

        datePickerDepositDate.setOnClickListener(new OnClickListener() {
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
                        datePickerDepositDate.setText(dateFormatter.format(mcurrentDate.getTime()));
                        case_deposit_date = format.format(mcurrentDate.getTime());
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
            }
        });

        datePickerPaymentDate.setOnClickListener(new OnClickListener() {
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
                        datePickerPaymentDate.setText(dateFormatter.format(mcurrentDate.getTime()));
                        paytm_date = format.format(mcurrentDate.getTime());
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
            }
        });
        datePickerUPIPaymentDate.setOnClickListener(new OnClickListener() {
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
                        datePickerUPIPaymentDate.setText(dateFormatter.format(mcurrentDate.getTime()));
                        upi_date = format.format(mcurrentDate.getTime());
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
            }
        });
        datePickerNextPayDate.setOnClickListener(new OnClickListener() {
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
                        datePickerNextPayDate.setText(dateFormatter.format(mcurrentDate.getTime()));
                        next_pay_date = format.format(mcurrentDate.getTime());
                        next_pay_comments = ", Next payment on " + dateFormatter.format(mcurrentDate.getTime());
                        unableflag = 0;
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
            }
        });

        datePickerCashbackDate.setOnClickListener(new OnClickListener() {
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
                        datePickerCashbackDate.setText(dateFormatter.format(mcurrentDate.getTime()));
                        next_cashback_date = format.format(mcurrentDate.getTime());
                        next_cashback_comments = ", Next payment on " + dateFormatter.format(mcurrentDate.getTime());
                        unableflag = 0;
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
            }
        });


        placeBtn = (Button) registerView.findViewById(R.id.placeBtn);
        CheckBox not = (CheckBox) registerView.findViewById(R.id.chkSelected);
        CheckBox notm = (CheckBox) registerView.findViewById(R.id.chkSelectedm);
        CheckBox chkSelectedReverse = (CheckBox) registerView.findViewById(R.id.chkSelectedReverse);
        if (preferences.getString("current_send_sms", "").equals("sendsms")) {
            not.setChecked(true);
            prefEditor.putString("current_send_sms", "sendsms");
            prefEditor.commit();

        } else if (preferences.getString("current_send_sms", "").equals("notsendsms")) {
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
        chkSelectedReverse.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    reverseFlag = "1";
                    String value = feesEdtTxt.getText().toString().trim();
                    feesEdtTxt.setText("-" + value);
                    dueFees = "";
                    unableflag = 0;
                    duefeesEdtTxt.setVisibility(View.GONE);
                    datePickerNextPayDate.setVisibility(View.GONE);

                } else {
                    reverseFlag = "";
                    String value = feesEdtTxt.getText().toString().trim().replace("-", "");
                    feesEdtTxt.setText("" + value);
                    unableflag = 1;
                    duefeesEdtTxt.setVisibility(View.VISIBLE);
                    datePickerNextPayDate.setVisibility(View.VISIBLE);
                }
            }
        });


        feesEdtTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("") && !s.toString().equals("-")) {
                    feesAmount = Double.parseDouble((s.toString().replace("--", "-")));
                    // getGSTDetails((feesAmount + cashbackAmount));
                    duefeesEdtTxt.setText("" + (total_amount - feesAmount));
                    String am = "" + (total_amount - feesAmount);
                    if (!am.equals("0.0")) {
                        dueFees1 = "Fees Due Rs " + (total_amount - feesAmount);
                        dueFees = "" + (total_amount - feesAmount);
                        if (!am.contains("-")) {
                            unableflag = 1;
                            datePickerNextPayDate.setVisibility(View.VISIBLE);
                        } else {
                            unableflag = 0;
                            datePickerNextPayDate.setVisibility(View.GONE);
                        }
                    } else {
                        dueFees1 = "No Fees Due.";
                        dueFees = "";
                        unableflag = 0;
                        datePickerNextPayDate.setVisibility(View.GONE);
                    }
                } else {
                    duefeesEdtTxt.setText("");
                    dueFees = "";
                    unableflag = 0;
                    datePickerNextPayDate.setVisibility(View.GONE);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        /*InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (Character.isWhitespace(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };*/

        //  RefNOEdtTxt.setFilters(new InputFilter[]{filter});

        userslist = new ArrayList<>();
        userslist.add(new RequestChangeUsersNameDAO("0", "Select Fee Type", "0", "0"));
        userslist.add(new RequestChangeUsersNameDAO("0", "Cash", "0", "0"));
        userslist.add(new RequestChangeUsersNameDAO("0", "Cheque/EFT/Paytm/UPI/Cash Deposit/Cash Voucher", "0", "0"));
        userslist.add(new RequestChangeUsersNameDAO("0", "Refund", "0", "0"));
        userslist.add(new RequestChangeUsersNameDAO("0", "Old Student", "0", "0"));

        // Spinner spinnerCustom = (Spinner) findViewById(R.id.spinnerBranch);
        ArrayAdapter<RequestChangeUsersNameDAO> adapter = new ArrayAdapter<RequestChangeUsersNameDAO>(getActivity(), R.layout.multiline_spinner_dropdown_item, userslist);
        // MyAdapter adapter = new MyAdapter(StudentsListActivity.this,R.layout.spinner_item,locationlist);
        spinnerFeesMode.setAdapter(adapter);
        spinnerFeesMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#1c5fab"));
                RequestChangeUsersNameDAO LeadSource = (RequestChangeUsersNameDAO) parent.getSelectedItem();
                // Toast.makeText(getApplicationContext(), "Source ID: " + LeadSource.getId() + ",  Source Name : " + LeadSource.getLocation_name(), Toast.LENGTH_SHORT).show();
                spinnerSelected = LeadSource.getUser_name();
                if (spinnerSelected.equals("Refund")) {
                    feesEdtTxt.setText("");
                    bd_id = "";
                    adj_amount = "";
                    mobileEdtTxt.setVisibility(View.GONE);
                    feesEdtTxt.setText("-" + feesEdtTxt.getText().toString());
                    refund.setVisibility(View.VISIBLE);
                    hideForDueFees.setVisibility(View.VISIBLE);
                    eft.setVisibility(View.GONE);
                    cheque.setVisibility(View.GONE);
                    paytm.setVisibility(View.GONE);
                    upi.setVisibility(View.GONE);
                    cashback.setVisibility(View.GONE);
                    oldStudent.setVisibility(View.GONE);
                    CashDeposit.setVisibility(View.GONE);
                    duefeesEdtTxt.setVisibility(View.GONE);
                    spinnerSubfeesMode.setVisibility(View.GONE);

                } else if (spinnerSelected.equals("Cash")) {
                    feesEdtTxt.setText("");
                    bd_id = "";
                    adj_amount = "";
                    mobileEdtTxt.setVisibility(View.GONE);
                    eft.setVisibility(View.GONE);
                    cheque.setVisibility(View.GONE);
                    paytm.setVisibility(View.GONE);
                    upi.setVisibility(View.GONE);
                    refund.setVisibility(View.GONE);
                    cashback.setVisibility(View.GONE);
                    oldStudent.setVisibility(View.GONE);
                    CashDeposit.setVisibility(View.GONE);
                    spinnerSubfeesMode.setVisibility(View.GONE);
                    hideForDueFees.setVisibility(View.VISIBLE);
                    duefeesEdtTxt.setVisibility(View.VISIBLE);

                    feesEdtTxt.setText(feesEdtTxt.getText().toString().replace("-", ""));

                } else if (spinnerSelected.equals("Cheque/EFT/Paytm/UPI/Cash Deposit/Cash Voucher")) {
                    eft.setVisibility(View.GONE);
                    cheque.setVisibility(View.GONE);
                    paytm.setVisibility(View.GONE);
                    upi.setVisibility(View.GONE);
                    cashback.setVisibility(View.GONE);
                    refund.setVisibility(View.GONE);
                    oldStudent.setVisibility(View.GONE);
                    duefeesEdtTxt.setVisibility(View.GONE);
                    hideForDueFees.setVisibility(View.GONE);
                    spinnerSubfeesMode.setVisibility(View.VISIBLE);
                    mobileEdtTxt.setVisibility(View.VISIBLE);
                    mobileEdtTxt.setText(preferences.getString("mobile_no", ""));
                    feesEdtTxt.setText(feesEdtTxt.getText().toString().replace("-", ""));


                } else if (spinnerSelected.equals("Old Student")) {
                    feesEdtTxt.setText("");
                    bd_id = "";
                    adj_amount = "";
                    mobileEdtTxt.setVisibility(View.GONE);
                    oldStudent.setVisibility(View.VISIBLE);
                    upi.setVisibility(View.GONE);
                    paytm.setVisibility(View.GONE);
                    cheque.setVisibility(View.GONE);
                    eft.setVisibility(View.GONE);
                    refund.setVisibility(View.GONE);
                    cashback.setVisibility(View.GONE);
                    CashDeposit.setVisibility(View.GONE);
                    spinnerSubfeesMode.setVisibility(View.GONE);
                    hideForDueFees.setVisibility(View.VISIBLE);
                    duefeesEdtTxt.setVisibility(View.VISIBLE);
                    feesEdtTxt.setText(feesEdtTxt.getText().toString().replace("-", ""));
                } else {
                    hideForDueFees.setVisibility(View.VISIBLE);
                    feesEdtTxt.setText(feesEdtTxt.getText().toString().replace("-", ""));
                }

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
                String fees = feesEdtTxt.getText().toString();

                dueFees = duefeesEdtTxt.getText().toString();
                // Toast.makeText(getActivity(), accountType,Toast.LENGTH_SHORT).show();
                if (AppStatus.getInstance(context).isOnline()) {
                    if (validate(spinnerSelected)) {
                        if (spinnerSelected.equals("Cash")) {
                            remarks = dueFees1 + next_pay_comments;
                            if (validatecash(spinnerSelected, fees, dueFees, unableflag)) {
                                // dismiss();
                                if (click) {
                                    //Toast.makeText(context, "Thank You! your information is added Successfully", Toast.LENGTH_LONG).show();
                                    sendData(name, fees, remarks, spinnerSelected);
                                    click = false;
                                } else {
                                    Toast.makeText(context, "Please Wait...", Toast.LENGTH_SHORT).show();
                                }


                            }
                        } else if (spinnerSelected.equals("Refund")) {
                            //  Toast.makeText(context, "Refund", Toast.LENGTH_LONG).show();
                            refundTxt = refundEdtTxt.getText().toString().trim();
                            remarks = "Fees of Rs  " + fees.replace("-", "") + " received on " + formateDateFromstring("yyyy-MM-dd", "dd-MMM-yyyy", date_of_receipt) + " Reason: " + refundTxt;
                            if (validaterefund(spinnerSelected, fees, date_of_receipt, refundTxt)) {
                                // dismiss();
                                if (click) {
                                    //Toast.makeText(context, "Thank You! your information is added Successfully", Toast.LENGTH_LONG).show();
                                    sendData(name, fees, remarks, spinnerSelected);
                                    click = false;
                                } else {
                                    Toast.makeText(context, "Please Wait...", Toast.LENGTH_SHORT).show();
                                }


                            }

                        } else if (spinnerSelected.equals("Cheque")) {
                            //  Toast.makeText(context, "Refund", Toast.LENGTH_LONG).show();
                            check_no = ChequeNoEdtTxt.getText().toString().trim();
                            remarks = check_no + " of " + bank_name + " on " + formateDateFromstring("yyyy-MM-dd", "dd-MMM-yyyy", cheque_date) + " from Account of " + issuer_name + ", " + dueFees1 + next_pay_comments;
                            if (validateCheque(spinnerSelected, fees, dueFees, check_no, cheque_date, unableflag)) {
                                // dismiss();

                                if (click) {
                                    //Toast.makeText(context, "Thank You! your information is added Successfully", Toast.LENGTH_LONG).show();
                                    if (!adj_amount.equals("")) {
                                        if (Double.parseDouble(adj_amount) >= feesAmount) {
                                            sendData(name, fees, remarks, spinnerSelected);
                                            click = false;
                                        } else {
                                            Toast.makeText(context, "Do not allow greater then value of Adjust amount of RS." + adj_amount, Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        sendData(name, fees, remarks, spinnerSelected);
                                        click = false;
                                    }
                                } else {
                                    Toast.makeText(context, "Please Wait...", Toast.LENGTH_SHORT).show();
                                }


                            }

                        } else if (spinnerSelected.equals("EFT")) {
                            //  Toast.makeText(context, "Refund", Toast.LENGTH_LONG).show();
                            ref_no = RefNOEdtTxt.getText().toString().trim();
                            remarks = "Ref No: " + ref_no + " on " + formateDateFromstring("yyyy-MM-dd", "dd-MMM-yyyy", pay_date) + " using " + technologySelected + ", " + dueFees1 + next_pay_comments;
                            if (validateEFT(spinnerSelected, fees, dueFees, ref_no, pay_date, unableflag)) {
                                // dismiss();
                                if (click) {
                                    //Toast.makeText(context, "Thank You! your information is added Successfully", Toast.LENGTH_LONG).show();
                                    if (!adj_amount.equals("")) {
                                        if (Double.parseDouble(adj_amount) >= feesAmount) {
                                            sendData(name, fees, remarks, spinnerSelected);
                                            click = false;
                                        } else {
                                            Toast.makeText(context, "Do not allow greater then value of Adjust amount of RS." + adj_amount, Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        sendData(name, fees, remarks, spinnerSelected);
                                        click = false;
                                    }


                                } else {
                                    Toast.makeText(context, "Please Wait...", Toast.LENGTH_SHORT).show();
                                }


                            }

                        } else if (spinnerSelected.equals("Cash Voucher")) {
                            //  Toast.makeText(context, "Refund", Toast.LENGTH_LONG).show();
                            cash_no = cashbackNOEdtTxt.getText().toString().trim();
                            remarks = "Cash Voucher No: " + cash_no + " on " + formateDateFromstring("yyyy-MM-dd", "dd-MMM-yyyy", next_cashback_date) + " using " + spinnerSelected + ", " + dueFees1 + next_cashback_comments;
                            if (validateCashBack(spinnerSelected, fees, dueFees, cash_no, next_cashback_date, unableflag)) {
                                // dismiss();
                                if (click) {
                                    //Toast.makeText(context, "Thank You! your information is added Successfully", Toast.LENGTH_LONG).show();
                                    if (!adj_amount.equals("")) {
                                        if (Double.parseDouble(adj_amount) >= feesAmount) {
                                            sendData(name, fees, remarks, spinnerSelected);
                                            click = false;
                                        } else {
                                            Toast.makeText(context, "Do not allow greater then value of Adjust amount of RS." + adj_amount, Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        sendData(name, fees, remarks, spinnerSelected);
                                        click = false;
                                    }


                                } else {
                                    Toast.makeText(context, "Please Wait...", Toast.LENGTH_SHORT).show();
                                }


                            }

                        }
                        if (spinnerSelected.equals("Old Student")) {
                            old_batch_no = oldBatchNoEdtTxt.getText().toString().trim();
                            remarks = "Old Student of BatchNo: " + old_batch_no + ", " + dueFees + next_pay_comments;
                            if (validateoldStudent(spinnerSelected, fees, dueFees, old_batch_no, unableflag)) {
                                // dismiss();
                                if (click) {
                                    //Toast.makeText(context, "Thank You! your information is added Successfully", Toast.LENGTH_LONG).show();
                                    sendData(name, fees, remarks, spinnerSelected);
                                    click = false;
                                } else {
                                    Toast.makeText(context, "Please Wait...", Toast.LENGTH_SHORT).show();
                                }


                            }
                        }

                        if (spinnerSelected.equals("Cash Deposit")) {
                            cash_trans_no = CASHNoEdtTxt.getText().toString().trim();
                            remarks = "Cash Deposited in " + center_id + " on " + formateDateFromstring("yyyy-MM-dd", "dd-MMM-yyyy", case_deposit_date) + ", " + dueFees + next_pay_comments;
                            if (validatecaseDeposit(spinnerSelected, fees, dueFees, cash_trans_no, case_deposit_date, center_id, unableflag)) {
                                // dismiss();
                                if (click) {
                                    //Toast.makeText(context, "Thank You! your information is added Successfully", Toast.LENGTH_LONG).show();

                                    if (!adj_amount.equals("")) {
                                        if (Double.parseDouble(adj_amount) >= feesAmount) {
                                            sendData(name, fees, remarks, spinnerSelected);
                                            click = false;
                                        } else {
                                            Toast.makeText(context, "Do not allow greater then value of Adjust amount of RS." + adj_amount, Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        sendData(name, fees, remarks, spinnerSelected);
                                        click = false;
                                    }
                                } else {
                                    Toast.makeText(context, "Please Wait...", Toast.LENGTH_SHORT).show();
                                }


                            }
                        }

                        if (spinnerSelected.equals("Paytm")) {
                            paytm_no = PaytmNoEdtTxt.getText().toString().trim();
                            remarks = "Paytm: " + paytm_no + " on " + formateDateFromstring("yyyy-MM-dd", "dd-MMM-yyyy", paytm_date) + ", " + dueFees + next_pay_comments;
                            if (validatePaytm(spinnerSelected, fees, dueFees, paytm_no, paytm_date, unableflag)) {
                                // dismiss();
                                if (click) {
                                    //Toast.makeText(context, "Thank You! your information is added Successfully", Toast.LENGTH_LONG).show();

                                    if (!adj_amount.equals("")) {
                                        if (Double.parseDouble(adj_amount) >= feesAmount) {
                                            sendData(name, fees, remarks, spinnerSelected);
                                            click = false;
                                        } else {
                                            Toast.makeText(context, "Do not allow greater then value of Adjust amount of RS." + adj_amount, Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        sendData(name, fees, remarks, spinnerSelected);
                                        click = false;
                                    }
                                } else {
                                    Toast.makeText(context, "Please Wait...", Toast.LENGTH_SHORT).show();
                                }


                            }
                        }

                        if (spinnerSelected.equals("UPI")) {
                            upi_no = UPINoEdtTxt.getText().toString().trim();
                            remarks = "UPI: " + upi_no + " on " + formateDateFromstring("yyyy-MM-dd", "dd-MMM-yyyy", paytm_date) + ", " + dueFees + next_pay_comments;
                            if (validateUPI(spinnerSelected, fees, dueFees, upi_no, upi_date, unableflag)) {
                                // dismiss();
                                if (click) {
                                    //Toast.makeText(context, "Thank You! your information is added Successfully", Toast.LENGTH_LONG).show();

                                    if (!adj_amount.equals("")) {
                                        if (Double.parseDouble(adj_amount) >= feesAmount) {
                                            sendData(name, fees, remarks, spinnerSelected);
                                            click = false;
                                        } else {
                                            Toast.makeText(context, "Do not allow greater then value of Adjust amount of RS." + adj_amount, Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        sendData(name, fees, remarks, spinnerSelected);
                                        click = false;
                                    }
                                } else {
                                    Toast.makeText(context, "Please Wait...", Toast.LENGTH_SHORT).show();
                                }


                            }
                        }
                    }
                } else {

                    Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }

            }
        });
        return registerView;
    }

    public boolean validate(String selected) {
        boolean isValidate = false;
        if (selected.equals("Select Fee Type")) {
            Toast.makeText(getActivity(), "Please select Fee Mode.", Toast.LENGTH_LONG).show();
        } else {
            isValidate = true;
        }
        return isValidate;
    }

    public boolean validatecash(String selected, String fees, String dueFees, int unableflag) {
        boolean isValidate = false;
        if (selected.equals("Select Fee Type")) {
            Toast.makeText(getActivity(), "Please select Fee Mode.", Toast.LENGTH_LONG).show();
        } else if (fees.trim().compareTo("") == 0) {
            Toast.makeText(getActivity(), "Please enter  Fees", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (dueFees.equals("")) {
            Toast.makeText(getActivity(), "Please enter  Fees Due", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (unableflag == 1) {
            if (next_pay_date.equals("")) {
                Toast.makeText(getActivity(), "Please select next payment date!", Toast.LENGTH_LONG).show();
                isValidate = false;
            }

        } else {
            isValidate = true;
        }
        return isValidate;
    }

    public boolean validaterefund(String selected, String fees, String date_of_receipt, String refundTxt) {
        boolean isValidate = false;
        if (selected.equals("Select Fee Type")) {
            Toast.makeText(getActivity(), "Please select Fee Mode.", Toast.LENGTH_LONG).show();
        } else if (fees.trim().compareTo("-") == 0) {
            Toast.makeText(getActivity(), "Please enter  Fees", Toast.LENGTH_LONG).show();
            isValidate = false;
        } else if (date_of_receipt.equals("")) {
            Toast.makeText(getActivity(), "Please select  date of receipt", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (refundTxt.equals("")) {
            Toast.makeText(getActivity(), "Please Enter  Reason for Refund", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else {
            isValidate = true;
        }
        return isValidate;
    }

    public boolean validateCheque(String selected, String fees, String dueFees, String check_no, String cheque_date, int unableflag) {
        boolean isValidate = false;
        if (selected.equals("Select Fee Type")) {
            Toast.makeText(getActivity(), "Please select Fee Mode.", Toast.LENGTH_LONG).show();
        } else if (fees.trim().compareTo("") == 0) {
            Toast.makeText(getActivity(), "Please enter  Fees", Toast.LENGTH_LONG).show();
            isValidate = false;
        } else if (dueFees.equals("")) {
            Toast.makeText(getActivity(), "Please enter  Fees Due", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (unableflag == 1) {
            if (next_pay_date.equals("")) {
                Toast.makeText(getActivity(), "Please select next payment date!", Toast.LENGTH_LONG).show();
                isValidate = false;
            }

        } else if (check_no.equals("")) {
            Toast.makeText(getActivity(), "Please enter check no.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (cheque_date.equals("")) {
            Toast.makeText(getActivity(), "Please select  cheque date", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else {
            isValidate = true;
        }
        return isValidate;
    }

    public boolean validateEFT(String selected, String fees, String dueFees, String ref_no, String pay_date, int unableflag) {
        boolean isValidate = false;
        if (selected.equals("Select Fee Type")) {
            Toast.makeText(getActivity(), "Please select Fee Mode.", Toast.LENGTH_LONG).show();
        } else if (fees.trim().compareTo("") == 0) {
            Toast.makeText(getActivity(), "Please enter  Fees", Toast.LENGTH_LONG).show();
            isValidate = false;
        } else if (dueFees.equals("")) {
            Toast.makeText(getActivity(), "Please enter  Fees Due", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (unableflag == 1) {
            if (next_pay_date.equals("")) {
                Toast.makeText(getActivity(), "Please select next payment date!", Toast.LENGTH_LONG).show();
                isValidate = false;
            }

        } else if (ref_no.equals("")) {
            Toast.makeText(getActivity(), "Please enter reference no.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (pay_date.equals("")) {
            Toast.makeText(getActivity(), "Please select  payment date", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else {
            isValidate = true;
        }
        return isValidate;
    }

    public boolean validateCashBack(String selected, String fees, String dueFees, String ref_no, String pay_date, int unableflag) {
        boolean isValidate = false;
        if (selected.equals("Select Fee Type")) {
            Toast.makeText(getActivity(), "Please select Fee Mode.", Toast.LENGTH_LONG).show();
        } else if (fees.trim().compareTo("") == 0) {
            Toast.makeText(getActivity(), "Please enter  Fees", Toast.LENGTH_LONG).show();
            isValidate = false;
        } else if (dueFees.equals("")) {
            Toast.makeText(getActivity(), "Please enter  Fees Due", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (unableflag == 1) {
            if (next_pay_date.equals("")) {
                Toast.makeText(getActivity(), "Please select next payment date!", Toast.LENGTH_LONG).show();
                isValidate = false;
            }

        } else if (ref_no.equals("")) {
            Toast.makeText(getActivity(), "Please enter reference no.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (pay_date.equals("")) {
            Toast.makeText(getActivity(), "Please select  payment date", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else {
            isValidate = true;
        }
        return isValidate;
    }

    public boolean validateoldStudent(String selected, String fees, String dueFees, String old_batch_no, int unableflag) {
        boolean isValidate = false;
        if (selected.equals("Select Fee Type")) {
            Toast.makeText(getActivity(), "Please select Fee Mode.", Toast.LENGTH_LONG).show();
        } else if (fees.trim().compareTo("") == 0) {
            Toast.makeText(getActivity(), "Please enter  Fees", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (dueFees.equals("")) {
            Toast.makeText(getActivity(), "Please enter  Fees Due", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (unableflag == 1) {
            if (next_pay_date.equals("")) {
                Toast.makeText(getActivity(), "Please select next payment date!", Toast.LENGTH_LONG).show();
                isValidate = false;
            }

        } else if (old_batch_no.equals("")) {
            Toast.makeText(getActivity(), "Please enter old batch no.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else {
            isValidate = true;
        }
        return isValidate;
    }

    public boolean validatecaseDeposit(String selected, String fees, String dueFees, String cash_trans_no, String case_deposit_date, String center_id, int unableflag) {
        boolean isValidate = false;
        if (selected.equals("Select Fee Type")) {
            Toast.makeText(getActivity(), "Please select Fee Mode.", Toast.LENGTH_LONG).show();
        } else if (fees.trim().compareTo("") == 0) {
            Toast.makeText(getActivity(), "Please enter  Fees", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (dueFees.equals("")) {
            Toast.makeText(getActivity(), "Please enter  Fees Due", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (cash_trans_no.equals("")) {
            Toast.makeText(getActivity(), "Please enter  Trans no.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (unableflag == 1) {
            if (next_pay_date.equals("")) {
                Toast.makeText(getActivity(), "Please select next payment date!", Toast.LENGTH_LONG).show();
                isValidate = false;
            }

        } else if (case_deposit_date.equals("")) {
            Toast.makeText(getActivity(), "Please select case deposit date", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (center_id.equals("")) {
            Toast.makeText(getActivity(), "Please select branch.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else {
            isValidate = true;
        }
        return isValidate;
    }

    public boolean validatePaytm(String selected, String fees, String dueFees, String paytm_no, String paytm_date, int unableflag) {
        boolean isValidate = false;
        if (selected.equals("Select Fee Type")) {
            Toast.makeText(getActivity(), "Please select Fee Mode.", Toast.LENGTH_LONG).show();
        } else if (fees.trim().compareTo("") == 0) {
            Toast.makeText(getActivity(), "Please enter  Fees", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (dueFees.equals("")) {
            Toast.makeText(getActivity(), "Please enter  Fees Due", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (unableflag == 1) {
            if (next_pay_date.equals("")) {
                Toast.makeText(getActivity(), "Please select next payment date!", Toast.LENGTH_LONG).show();
                isValidate = false;
            }

        } else if (paytm_no.equals("")) {
            Toast.makeText(getActivity(), "Please enter Wallet Txn ID", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (paytm_date.equals("")) {
            Toast.makeText(getActivity(), "Please select Payment Date.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else {
            isValidate = true;
        }
        return isValidate;
    }

    public boolean validateUPI(String selected, String fees, String dueFees, String upi_no, String upi_date, int unableflag) {
        boolean isValidate = false;
        if (selected.equals("Select Fee Type")) {
            Toast.makeText(getActivity(), "Please select Fee Mode.", Toast.LENGTH_LONG).show();
        } else if (fees.trim().compareTo("") == 0) {
            Toast.makeText(getActivity(), "Please enter  Fees", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (dueFees.equals("")) {
            Toast.makeText(getActivity(), "Please enter  Fees Due", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (unableflag == 1) {
            if (next_pay_date.equals("")) {
                Toast.makeText(getActivity(), "Please select next payment date!", Toast.LENGTH_LONG).show();
                isValidate = false;
            }

        } else if (upi_no.equals("")) {
            Toast.makeText(getActivity(), "Please enter UPI Txn ID", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (upi_date.equals("")) {
            Toast.makeText(getActivity(), "Please select Payment Date.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else {
            isValidate = true;
        }
        return isValidate;
    }

    public void sendData(final String name, final String fees, final String remarks, final String spinnerSelected) {
        if (spinnerSelected.equals("Cash")) {
            ReceivedBy = preferences.getString("trainer_user_name", "");
            ReceivedBy_id = preferences.getString("trainer_user_id", "");
            String str = name;
            String[] splited = str.split(" ");
            strName = splited[0];

        } else if (spinnerSelected.equals("Cheque")) {
            ReceivedBy = "Raza Saghar ";
            ReceivedBy_id = "RS";
            String str = name;
            String[] splited = str.split(" ");
            String strName = splited[0];
        } else if (spinnerSelected.equals("Refund")) {
            ReceivedBy = "Raza Saghar ";
            ReceivedBy_id = "RS";
            String str = name;
            String[] splited = str.split(" ");
            String strName = splited[0];


        } else if (spinnerSelected.equals("EFT")) {
            ReceivedBy = "Raza Saghar ";
            ReceivedBy_id = "RS";
            String str = name;
            String[] splited = str.split(" ");
            String strName = splited[0];

        } else if (spinnerSelected.equals("Old Student")) {
            ReceivedBy = "Discount ";
            ReceivedBy_id = "Discount ";
        } else if (spinnerSelected.equals("Discount")) {
            ReceivedBy = "Discount ";
            ReceivedBy_id = "Discount ";
        } else if (spinnerSelected.equals("Cash Voucher")) {
            ReceivedBy = "Discount ";
            ReceivedBy_id = "Discount ";
        } else if (spinnerSelected.equals("Cash Deposit")) {
            ReceivedBy = "Raza Saghar";
            ReceivedBy_id = "RS";
            String str = name;
            String[] splited = str.split(" ");
            String strName = splited[0];


        } else if (spinnerSelected.equals("Paytm")) {
            ReceivedBy = "Raza Saghar";
            ReceivedBy_id = "RS";
            String str = name;
            String[] splited = str.split(" ");
            String strName = splited[0];


        } else if (spinnerSelected.equals("UPI")) {
            ReceivedBy = "Raza Saghar";
            ReceivedBy_id = "RS";
            String str = name;
            String[] splited = str.split(" ");
            String strName = splited[0];
        } else if (spinnerSelected.equals("Paytm Self")) {
            ReceivedBy = preferences.getString("trainer_user_name", "");
            ReceivedBy_id = preferences.getString("trainer_user_id", "");
            String str = name;
            String[] splited = str.split(" ");
            String strName = splited[0];


        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar cal = Calendar.getInstance();
        final String date = format.format(cal.getTime());
        jsonObjCash = new JSONObject() {
            {
                try {
                    put("batch_no", preferences.getString("batch_id", ""));
                    put("user_id", preferences.getString("id_fees", ""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        if (spinnerSelected.equals("Refund")) {
            jsonObj = new JSONObject() {
                {
                    try {
                        put("ReceivedBy", ReceivedBy);
                        put("UserName", preferences.getString("trainer_user_name", ""));
                        put("ReceivedBy_id", ReceivedBy_id);
                        put("UserName_id", preferences.getString("trainer_user_id", ""));
                        put("BatchNo", preferences.getString("batch_id", ""));
                        put("MobileNo", preferences.getString("mobile_no", ""));
                        put("user_id", preferences.getString("id_fees", ""));
                        put("Fees", feesAmount);
                        put("Note", remarks);
                        put("FeeMode", spinnerSelected);
                        put("students_name", preferences.getString("student_name", ""));
                        put("DateTimeOfEntry", date);
                        put("next_pay_date", "");
                        put("dueFees", "");
                        put("bd_id", "");


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

        } else {
            jsonObj = new JSONObject() {
                {
                    try {
                        put("ReceivedBy_id", ReceivedBy_id);
                        put("UserName_id", preferences.getString("trainer_user_id", ""));
                        put("ReceivedBy", ReceivedBy);
                        put("BatchNo", preferences.getString("batch_id", ""));
                        put("MobileNo", preferences.getString("mobile_no", ""));
                        put("UserName", preferences.getString("trainer_user_name", ""));
                        put("user_id", preferences.getString("id_fees", ""));
                        put("Fees", feesAmount);
                        put("Note", remarks);
                        put("FeeMode", spinnerSelected);
                        put("DateTimeOfEntry", date);
                        put("next_pay_date", next_pay_date);
                        put("dueFees", dueFees);
                        put("students_name", preferences.getString("student_name", ""));
                        put("bd_id", bd_id);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        }

        Thread objectThread = new Thread(new Runnable() {
            public void run() {
                // TODO Auto-generated method stub
                WebClient serviceAccess = new WebClient();

                Log.i("jsonObj", "jsonObj" + jsonObj);
                loginResponse = serviceAccess.SendHttpPost(Config.URL_ADD_STUDENT_FEESDETAILS, jsonObj);
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
                                        code = jObject.getString("fees_id");

                                        if (status) {
                                            if (preferences.getString("current_send_sms", "").equals("sendsms")) {
                                                smsSent = "Yes";
                                            } else {
                                                smsSent = "No";
                                            }
                                            subject = "FeesDetails: R" + preferences.getString("batch_id", "");
                                            sendingmail = "Batch Name : " + preferences.getString("batch_id", "") + System.getProperty("line.separator")
                                                    + "Student Name : " + preferences.getString("student_name", "") + System.getProperty("line.separator")
                                                    + "Fees  Paid : " + feesAmount + System.getProperty("line.separator")
                                                    + "Fees  Due : " + dueFees1 + System.getProperty("line.separator")
                                                    + "Fees Note : " + remarks + System.getProperty("line.separator")
                                                    + "Received By : " + ReceivedBy + System.getProperty("line.separator")
                                                    + "Entered By : " + preferences.getString("trainer_user_name", "") + System.getProperty("line.separator")
                                                    + "SMS Sent : " + smsSent + System.getProperty("line.separator")
                                                    + "FeeMode : " + spinnerSelected + System.getProperty("line.separator")
                                                    + "Date Time : " + date + System.getProperty("line.separator");
                                            if (spinnerSelected.equals("Cash")) {
                                                String str = name.replace("Mr. ", "").replace("Mr. ", "").replace("Ms. ", "");
                                                String[] splited = str.split(" ");
                                                String strName = splited[0];
                                                message = "Hi " + strName + System.getProperty("line.separator") + System.getProperty("line.separator")
                                                        + "Received Rs. " + (feesAmount + cashbackAmount) + " for " + preferences.getString("course_name", "") + " Batch No. " + preferences.getString("batch_id", "") + " in Cash." + System.getProperty("line.separator") + System.getProperty("line.separator")
                                                        + "Note:" + dueFees1 + System.getProperty("line.separator") + System.getProperty("line.separator")
                                                        + "Regards" + System.getProperty("line.separator")
                                                        + preferences.getString("trainer_name", "") + System.getProperty("line.separator")
                                                        + preferences.getString("phone_no", "");
                                                Log.d("message---->", message);

                                                if (preferences.getString("current_send_sms", "").equals("sendsms")) {
                                                    String result = sendSms1(preferences.getString("mobile_no", ""), message);
                                                    Log.d("sent sms---->", result);
                                                }


                                            } else if (spinnerSelected.equals("Cheque")) {
                                                String str = name.replace("Mr. ", "").replace("Mr. ", "").replace("Ms. ", "");
                                                String[] splited = str.split(" ");
                                                String strName = splited[0];
                                                message = "Hi " + strName + System.getProperty("line.separator") + System.getProperty("line.separator")
                                                        + "Received Rs. " + (feesAmount + cashbackAmount) + " recorded for " + preferences.getString("course_name", "") + " Batch No. " + preferences.getString("batch_id", "") + " which is paid via Cheque. Receipt is Subject to Realization of Cheque." + System.getProperty("line.separator") + System.getProperty("line.separator")
                                                        + "Note:" + dueFees1 + System.getProperty("line.separator") + System.getProperty("line.separator")
                                                        + "Regards" + System.getProperty("line.separator")
                                                        + preferences.getString("trainer_name", "") + System.getProperty("line.separator")
                                                        + preferences.getString("phone_no", "");
                                                if (preferences.getString("current_send_sms", "").equals("sendsms")) {
                                                    String result = sendSms1(preferences.getString("mobile_no", ""), message);
                                                    Log.d("sent sms---->", result);
                                                }


                                            } else if (spinnerSelected.equals("EFT")) {
                                                String str = name.replace("Mr. ", "").replace("Mr. ", "").replace("Ms. ", "");
                                                String[] splited = str.split(" ");
                                                String strName = splited[0];
                                                message = "Hi " + strName + System.getProperty("line.separator") + System.getProperty("line.separator")
                                                        + "Received Rs. " + (feesAmount + cashbackAmount) + " recorded for " + preferences.getString("course_name", "") + " Batch No. " + preferences.getString("batch_id", "") + " which is paid via EFT. Receipt is Subject to Realization of EFT." + System.getProperty("line.separator") + System.getProperty("line.separator")
                                                        + "Note:" + dueFees1 + System.getProperty("line.separator") + System.getProperty("line.separator")
                                                        + "Regards" + System.getProperty("line.separator")
                                                        + preferences.getString("trainer_name", "") + System.getProperty("line.separator")
                                                        + preferences.getString("phone_no", "");
                                                if (preferences.getString("current_send_sms", "").equals("sendsms")) {
                                                    String result = sendSms1(preferences.getString("mobile_no", ""), message);
                                                    Log.d("sent sms---->", result);
                                                }


                                            } else if (spinnerSelected.equals("Refund")) {
                                                String str = name.replace("Mr. ", "").replace("Mr. ", "").replace("Ms. ", "");
                                                String[] splited = str.split(" ");
                                                String strName = splited[0];
                                                message = "Hi " + strName + System.getProperty("line.separator") + System.getProperty("line.separator")
                                                        + "Received Rs. " + (feesAmount + cashbackAmount) + " refunded for  " + preferences.getString("course_name", "") + " Batch No. " + preferences.getString("batch_id", "") + " in EFT." + System.getProperty("line.separator") + System.getProperty("line.separator")
                                                        + "Note:" + dueFees1 + System.getProperty("line.separator") + System.getProperty("line.separator")
                                                        + "Regards" + System.getProperty("line.separator")
                                                        + preferences.getString("trainer_name", "") + System.getProperty("line.separator")
                                                        + preferences.getString("phone_no", "");
                                                if (preferences.getString("current_send_sms", "").equals("sendsms")) {
                                                    String result = sendSms1(preferences.getString("mobile_no", ""), message);
                                                    Log.d("sent sms---->", result);
                                                }


                                            } else if (spinnerSelected.equals("Cash Deposit")) {
                                                String str = name.replace("Mr. ", "").replace("Mr. ", "").replace("Ms. ", "");
                                                String[] splited = str.split(" ");
                                                String strName = splited[0];
                                                message = "Hi " + strName + System.getProperty("line.separator") + System.getProperty("line.separator")
                                                        + "Received Rs. " + (feesAmount + cashbackAmount) + " recorded for  " + preferences.getString("course_name", "") + " Batch No. " + preferences.getString("batch_id", "") + " which is paid via Cash Deposit." + System.getProperty("line.separator") + System.getProperty("line.separator")
                                                        + "Note:" + dueFees1 + System.getProperty("line.separator") + System.getProperty("line.separator")
                                                        + "Regards" + System.getProperty("line.separator")
                                                        + preferences.getString("trainer_name", "") + System.getProperty("line.separator")
                                                        + preferences.getString("phone_no", "");
                                                if (preferences.getString("current_send_sms", "").equals("sendsms")) {
                                                    String result = sendSms1(preferences.getString("mobile_no", ""), message);
                                                    Log.d("sent sms---->", result);
                                                }


                                            } else if (spinnerSelected.equals("Cash Voucher")) {
                                                String str = name.replace("Mr. ", "").replace("Mr. ", "").replace("Ms. ", "");
                                                String[] splited = str.split(" ");
                                                String strName = splited[0];
                                                message = "Hi " + strName + System.getProperty("line.separator") + System.getProperty("line.separator")
                                                        + "Received Rs. " + (feesAmount + cashbackAmount) + " recorded for  " + preferences.getString("course_name", "") + " Batch No. " + preferences.getString("batch_id", "") + " which is paid via Cash Voucher No." + cash_no + " on " + formateDateFromstring("yyyy-MM-dd", "dd-MMM-yyyy", next_cashback_date) + System.getProperty("line.separator") + System.getProperty("line.separator")
                                                        + "Note:" + dueFees1 + System.getProperty("line.separator") + System.getProperty("line.separator")
                                                        + "Regards" + System.getProperty("line.separator")
                                                        + preferences.getString("trainer_name", "") + System.getProperty("line.separator")
                                                        + preferences.getString("phone_no", "");
                                                if (preferences.getString("current_send_sms", "").equals("sendsms")) {
                                                    String result = sendSms1(preferences.getString("mobile_no", ""), message);
                                                    Log.d("sent sms---->", result);
                                                }


                                            } else if (spinnerSelected.equals("Paytm")) {
                                                String str = name.replace("Mr. ", "").replace("Mr. ", "").replace("Ms. ", "");
                                                String[] splited = str.split(" ");
                                                String strName = splited[0];
                                                message = "Hi " + strName + System.getProperty("line.separator") + System.getProperty("line.separator")
                                                        + "Received Rs. " + (feesAmount + cashbackAmount) + " for " + preferences.getString("course_name", "") + " Batch No. " + preferences.getString("batch_id", "") + " in Paytm." + System.getProperty("line.separator") + System.getProperty("line.separator")
                                                        + "Note:" + dueFees1 + System.getProperty("line.separator") + System.getProperty("line.separator")
                                                        + "Regards" + System.getProperty("line.separator")
                                                        + preferences.getString("trainer_name", "") + System.getProperty("line.separator")
                                                        + preferences.getString("phone_no", "");
                                                if (preferences.getString("current_send_sms", "").equals("sendsms")) {
                                                    String result = sendSms1(preferences.getString("mobile_no", ""), message);
                                                    Log.d("sent sms---->", result);
                                                }


                                            } else if (spinnerSelected.equals("UPI")) {
                                                String str = name.replace("Mr. ", "").replace("Mr. ", "").replace("Ms. ", "");
                                                String[] splited = str.split(" ");
                                                String strName = splited[0];
                                                message = "Hi " + strName + System.getProperty("line.separator") + System.getProperty("line.separator")
                                                        + "Received Rs. " + (feesAmount + cashbackAmount) + " for " + preferences.getString("course_name", "") + " Batch No. " + preferences.getString("batch_id", "") + " in UPI." + System.getProperty("line.separator") + System.getProperty("line.separator")
                                                        + "Note:" + dueFees1 + System.getProperty("line.separator") + System.getProperty("line.separator")
                                                        + "Regards" + System.getProperty("line.separator")
                                                        + preferences.getString("trainer_name", "") + System.getProperty("line.separator")
                                                        + preferences.getString("phone_no", "");
                                                if (preferences.getString("current_send_sms", "").equals("sendsms")) {
                                                    String result = sendSms1(preferences.getString("mobile_no", ""), message);
                                                    Log.d("sent sms---->", result);
                                                }
                                            } else if (spinnerSelected.equals("Paytm Self")) {
                                                String str = name.replace("Mr. ", "").replace("Mr. ", "").replace("Ms. ", "");
                                                String[] splited = str.split(" ");
                                                String strName = splited[0];
                                                message = "Hi " + strName + System.getProperty("line.separator") + System.getProperty("line.separator")
                                                        + "Received Rs. " + (feesAmount + cashbackAmount) + " for " + preferences.getString("course_name", "") + " Batch No. " + preferences.getString("batch_id", "") + " in Paytm." + System.getProperty("line.separator") + System.getProperty("line.separator")
                                                        + "Note:" + dueFees1 + System.getProperty("line.separator") + System.getProperty("line.separator")
                                                        + "Regards" + System.getProperty("line.separator")
                                                        + preferences.getString("trainer_name", "") + System.getProperty("line.separator")
                                                        + preferences.getString("phone_no", "");
                                                if (preferences.getString("current_send_sms", "").equals("sendsms")) {
                                                    String result = sendSms1(preferences.getString("mobile_no", ""), message);
                                                    Log.d("sent sms---->", result);
                                                }


                                            }

                                            Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                                            new MyAsyncClass().execute();
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

    class MyAsyncClass extends AsyncTask<Void, Void, Void> {


        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {
                // Toast.makeText(getActivity(), "mail id is"+preferences.getString("sendingmailid", ""), Toast.LENGTH_SHORT).show();
                // Add subject, Body, your mail Id, and receiver mail Id.
                sender.sendMail(subject, sendingmail, username, preferences.getString("sendingmailid", ""));
                if (!preferences.getString("sendingmailid", "").equals("mohammed.raza@afcks.com")) {
                    sender.sendMail(subject, sendingmail, username, "mohammed.raza@afcks.com");
                }

            } catch (Exception ex) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.cancel();

            // Toast.makeText(getActivity(), "Email send", Toast.LENGTH_LONG).show();
            //  new UploadFileToServer().execute();

        }


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


                ArrayAdapter<LocationDAO> adapter = new ArrayAdapter<LocationDAO>(getActivity(), android.R.layout.simple_spinner_dropdown_item, locationlist);
                // MyAdapter adapter = new MyAdapter(StudentsListActivity.this,R.layout.spinner_item,locationlist);
                spinnerBranch.setAdapter(adapter);
                spinnerBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#1c5fab"));
                        LocationDAO LeadSource = (LocationDAO) parent.getSelectedItem();
                        //  Toast.makeText(getApplicationContext(), "Source ID: " + LeadSource.getId() + ",  Source Name : " + LeadSource.getBranch_name(), Toast.LENGTH_SHORT).show();
                        center_id = LeadSource.getBranch_name();

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

    //
    private class initSubFeesModeSpinner extends AsyncTask<Void, Void, Void> {
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

            jsonLeadObj1 = new JSONObject() {
                {
                    try {


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();
            Log.i("json", "json" + jsonLeadObj1);
            bankingDetailsResponse = serviceAccess.SendHttpPost(Config.URL_GETALLBANKRDETAILS, jsonLeadObj1);
            Log.i("resp", "bankingDetailsResponse" + bankingDetailsResponse);

            if (bankingDetailsResponse.compareTo("") != 0) {
                if (isJSONValid(bankingDetailsResponse)) {

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {

                                bankingdetailsDAOArrayList = new ArrayList<>();
                                bankingdetailsDAOArrayList.add(new BankingdetailsDAO("0", "Select Banking Receipt", "", "", "", "", "", ""));
                                JSONArray LeadSourceJsonObj = new JSONArray(bankingDetailsResponse);
                                for (int i = 0; i < LeadSourceJsonObj.length(); i++) {
                                    JSONObject json_data = LeadSourceJsonObj.getJSONObject(i);
                                    bankingdetailsDAOArrayList.add(new BankingdetailsDAO(json_data.getString("id"), json_data.getString("fees"), json_data.getString("trans_ref"), json_data.getString("trans_date"), json_data.getString("trans_type"), json_data.getString("rec_amount"), json_data.getString("adj_amount"), json_data.getString("entered_by")));
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
            if (bankingDetailsResponse.compareTo("") != 0) {

                ArrayAdapter<BankingdetailsDAO> adapter = new ArrayAdapter<BankingdetailsDAO>(getActivity(), R.layout.multiline_spinner_dropdown_item, bankingdetailsDAOArrayList) {
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v = super.getView(position, convertView, parent);

                        ((TextView) v).setTextSize(16);
                        ((TextView) v).setTextColor(getResources().getColorStateList(R.color.white)
                        );

                        return v;
                    }

                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        View v = super.getDropDownView(position, convertView, parent);
                        BankingdetailsDAO item = bankingdetailsDAOArrayList.get(position);
                        String test = item.getId();
                        Log.d("test ", test);
                        ((TextView) v).setTextColor(getResources().getColorStateList(R.color.white));
                        //((TextView) v).setTypeface(fontStyle);
                        ((TextView) v).setGravity(Gravity.CENTER);

                        return v;
                    }

                };

                spinnerSubfeesMode.setAdapter(adapter);
                spinnerSubfeesMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#1c5fab"));
                        BankingdetailsDAO LeadSource = (BankingdetailsDAO) parent.getSelectedItem();
                        // Toast.makeText(getActivity(), "Source ID: " + LeadSource.getId() + ",  Source Name : " + LeadSource.getFees(), Toast.LENGTH_SHORT).show();

                        if (LeadSource.getTrans_type().equals("EFT")) {
                            bd_id = LeadSource.getId();
                            eft.setVisibility(View.VISIBLE);
                            cashback.setVisibility(View.GONE);
                            cheque.setVisibility(View.GONE);
                            paytm.setVisibility(View.GONE);
                            upi.setVisibility(View.GONE);
                            refund.setVisibility(View.GONE);
                            oldStudent.setVisibility(View.GONE);
                            duefeesEdtTxt.setVisibility(View.VISIBLE);
                            hideForDueFees.setVisibility(View.VISIBLE);
                            feesEdtTxt.setText(LeadSource.getAdj_amount());
                            adj_amount = LeadSource.getAdj_amount();
                            datePickerPayDate.setText(formateDateFromstring("yyyy-MM-dd", "dd-MMM-yyyy", LeadSource.getTrans_date()));
                            pay_date = LeadSource.getTrans_date();
                            spinnerSelected = LeadSource.getTrans_type();
                            RefNOEdtTxt.setText(LeadSource.getTrans_ref());
                            feesEdtTxt.setText(feesEdtTxt.getText().toString().replace("-", ""));

                            RefNOEdtTxt.setEnabled(false);
                            RefNOEdtTxt.setClickable(false);
                            datePickerPayDate.setEnabled(false);
                            datePickerPayDate.setClickable(false);

                        }
                        if (LeadSource.getTrans_type().equals("Cheque")) {
                            bd_id = LeadSource.getId();
                            cheque.setVisibility(View.VISIBLE);
                            cashback.setVisibility(View.GONE);
                            eft.setVisibility(View.GONE);
                            paytm.setVisibility(View.GONE);
                            refund.setVisibility(View.GONE);
                            upi.setVisibility(View.GONE);
                            oldStudent.setVisibility(View.GONE);
                            CashDeposit.setVisibility(View.GONE);
                            hideForDueFees.setVisibility(View.VISIBLE);
                            duefeesEdtTxt.setVisibility(View.VISIBLE);
                            feesEdtTxt.setText(LeadSource.getAdj_amount());
                            adj_amount = LeadSource.getAdj_amount();
                            spinnerSelected = LeadSource.getTrans_type();
                            ChequeNoEdtTxt.setText(LeadSource.getTrans_ref());
                            datePickerChequeDate.setText(formateDateFromstring("yyyy-MM-dd", "dd-MMM-yyyy", LeadSource.getTrans_date()));
                            cheque_date = LeadSource.getTrans_date();

                            feesEdtTxt.setText(feesEdtTxt.getText().toString().replace("-", ""));
                            ChequeNoEdtTxt.setEnabled(false);
                            ChequeNoEdtTxt.setClickable(false);
                            datePickerChequeDate.setEnabled(false);
                            datePickerChequeDate.setClickable(false);

                        }
                        if (LeadSource.getTrans_type().equals("Paytm")) {
                            bd_id = LeadSource.getId();
                            paytm.setVisibility(View.VISIBLE);
                            cashback.setVisibility(View.GONE);
                            upi.setVisibility(View.GONE);
                            cheque.setVisibility(View.GONE);
                            eft.setVisibility(View.GONE);
                            refund.setVisibility(View.GONE);
                            oldStudent.setVisibility(View.GONE);
                            CashDeposit.setVisibility(View.GONE);
                            hideForDueFees.setVisibility(View.VISIBLE);
                            duefeesEdtTxt.setVisibility(View.VISIBLE);
                            feesEdtTxt.setText(LeadSource.getAdj_amount());
                            adj_amount = LeadSource.getAdj_amount();
                            spinnerSelected = LeadSource.getTrans_type();
                            PaytmNoEdtTxt.setText(LeadSource.getTrans_ref());
                            datePickerPaymentDate.setText(formateDateFromstring("yyyy-MM-dd", "dd-MMM-yyyy", LeadSource.getTrans_date()));
                            paytm_date = LeadSource.getTrans_date();
                            feesEdtTxt.setText(feesEdtTxt.getText().toString().replace("-", ""));
                            PaytmNoEdtTxt.setEnabled(false);
                            PaytmNoEdtTxt.setClickable(false);
                            datePickerPaymentDate.setEnabled(false);
                            datePickerPaymentDate.setClickable(false);
                        }
                        if (LeadSource.getTrans_type().equals("UPI")) {
                            bd_id = LeadSource.getId();
                            upi.setVisibility(View.VISIBLE);
                            cashback.setVisibility(View.GONE);
                            paytm.setVisibility(View.GONE);
                            cheque.setVisibility(View.GONE);
                            eft.setVisibility(View.GONE);
                            refund.setVisibility(View.GONE);
                            oldStudent.setVisibility(View.GONE);
                            CashDeposit.setVisibility(View.GONE);
                            hideForDueFees.setVisibility(View.VISIBLE);
                            duefeesEdtTxt.setVisibility(View.VISIBLE);
                            feesEdtTxt.setText(LeadSource.getAdj_amount());
                            adj_amount = LeadSource.getAdj_amount();
                            spinnerSelected = LeadSource.getTrans_type();
                            UPINoEdtTxt.setText(LeadSource.getTrans_ref());
                            datePickerUPIPaymentDate.setText(formateDateFromstring("yyyy-MM-dd", "dd-MMM-yyyy", LeadSource.getTrans_date()));
                            upi_date = LeadSource.getTrans_date();
                            feesEdtTxt.setText(feesEdtTxt.getText().toString().replace("-", ""));
                            UPINoEdtTxt.setEnabled(false);
                            UPINoEdtTxt.setClickable(false);
                            datePickerUPIPaymentDate.setEnabled(false);
                            datePickerUPIPaymentDate.setClickable(false);

                        }
                        if (LeadSource.getTrans_type().equals("Cash Deposit")) {
                            bd_id = LeadSource.getId();
                            CashDeposit.setVisibility(View.VISIBLE);
                            upi.setVisibility(View.GONE);
                            cashback.setVisibility(View.GONE);
                            oldStudent.setVisibility(View.GONE);
                            paytm.setVisibility(View.GONE);
                            cheque.setVisibility(View.GONE);
                            eft.setVisibility(View.GONE);
                            refund.setVisibility(View.GONE);
                            hideForDueFees.setVisibility(View.VISIBLE);
                            duefeesEdtTxt.setVisibility(View.VISIBLE);
                            feesEdtTxt.setText(LeadSource.getAdj_amount());
                            adj_amount = LeadSource.getAdj_amount();
                            spinnerSelected = LeadSource.getTrans_type();
                            CASHNoEdtTxt.setText(LeadSource.getTrans_ref());
                            datePickerDepositDate.setText(formateDateFromstring("yyyy-MM-dd", "dd-MMM-yyyy", LeadSource.getTrans_date()));
                            case_deposit_date = LeadSource.getTrans_date();
                            feesEdtTxt.setText(feesEdtTxt.getText().toString().replace("-", ""));
                            CASHNoEdtTxt.setEnabled(false);
                            CASHNoEdtTxt.setClickable(false);
                            datePickerDepositDate.setEnabled(false);
                            datePickerDepositDate.setClickable(false);
                            new initBranchSpinner().execute();
                        }

                        if (LeadSource.getTrans_type().equals("Cash Voucher")) {
                            bd_id = LeadSource.getId();
                            cashback.setVisibility(View.VISIBLE);
                            eft.setVisibility(View.GONE);
                            cheque.setVisibility(View.GONE);
                            paytm.setVisibility(View.GONE);
                            upi.setVisibility(View.GONE);
                            refund.setVisibility(View.GONE);
                            oldStudent.setVisibility(View.GONE);
                            duefeesEdtTxt.setVisibility(View.VISIBLE);
                            hideForDueFees.setVisibility(View.VISIBLE);
                            feesEdtTxt.setText(LeadSource.getAdj_amount());
                            adj_amount = LeadSource.getAdj_amount();
                            datePickerCashbackDate.setText(formateDateFromstring("yyyy-MM-dd", "dd-MMM-yyyy", LeadSource.getTrans_date()));
                            next_cashback_date = LeadSource.getTrans_date();
                            spinnerSelected = LeadSource.getTrans_type();
                            cashbackNOEdtTxt.setText(LeadSource.getTrans_ref());
                            feesEdtTxt.setText(feesEdtTxt.getText().toString().replace("-", ""));

                            cashbackNOEdtTxt.setEnabled(false);
                            cashbackNOEdtTxt.setClickable(false);
                            datePickerCashbackDate.setEnabled(false);
                            datePickerCashbackDate.setClickable(false);

                        }

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
}