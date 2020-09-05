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
import android.provider.ContactsContract;

import android.telephony.TelephonyManager;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

import androidx.fragment.app.DialogFragment;
import in.afckstechnologies.afcksfees.R;
import in.afckstechnologies.afcksfees.utils.AppStatus;
import in.afckstechnologies.afcksfees.utils.Config;
import in.afckstechnologies.afcksfees.utils.Constant;
import in.afckstechnologies.afcksfees.utils.SmsListener;
import in.afckstechnologies.afcksfees.utils.WebClient;


public class RegistrationView extends DialogFragment {

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
    Boolean status;
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
    String centerResponse = "";
    String phoneNumber = "", emailAddress = "";
    public static SmsListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        registerView = inflater.inflate(R.layout.dialog_registration, null);

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
        preferences = getActivity().getSharedPreferences("Prefrence", getActivity().MODE_PRIVATE);
        prefEditor = preferences.edit();
        user_name = preferences.getString("student_name", "");
        nameEdtTxt.setText(user_name);
        email_id = preferences.getString("emil_id", "");
        mobile_no = preferences.getString("st_mobile_no", "");
        last_name = preferences.getString("student_last_name", "");
        if (!email_id.equals("")) {
            emailEdtTxt.setText(email_id);
        }
        if (!mobile_no.equals("")) {
            phEdtTxt.setText(mobile_no);
            prefEditor.putString("st_mobile_no", "");
            prefEditor.commit();
        }
        if (!last_name.equals("")) {
            lastnameEdtTxt.setText(last_name);
        }
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

                String name = nameEdtTxt.getText().toString();
                String lastname = lastnameEdtTxt.getText().toString();
                String phoneno = phEdtTxt.getText().toString();
                String emailid = emailEdtTxt.getText().toString().trim();
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

                if (validate(name, lastname, phoneno, emailid, gender)) {

                    if (AppStatus.getInstance(context).isOnline()) {
                        // dismiss();
                        if (click) {
                            //Toast.makeText(context, "Thank You! your information is added Successfully", Toast.LENGTH_LONG).show();
                            sendData(name, phoneno, emailid, lastname, gender);
                            click = false;
                        } else {
                            Toast.makeText(context, "Please Wait...", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                    }


                } else {

                }

            }
        });
        return registerView;
    }


    //

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    ContentResolver cr = getActivity().getContentResolver();
                    Cursor cur = cr.query(contactData, null, null, null, null);
                    if (cur.getCount() > 0) {
                        while (cur.moveToNext()) {
                            String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                            String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                            String columns[] = name.split(" ");
                            first_name = columns[0];
                            String name2 = columns[1];
                            String columns2[] = name2.split(",");
                            last_name = columns2[0];

                            if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                                System.out.println("name : " + name + ", ID : " + id);

                                // get the phone number
                                Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                        new String[]{id}, null);
                                while (pCur.moveToNext()) {
                                    phoneNumber = pCur.getString(
                                            pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    System.out.println("phone" + phoneNumber);
                                }
                                pCur.close();


                                // get email and type

                                Cursor emailCur = cr.query(
                                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                                        null,
                                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                                        new String[]{id}, null);
                                while (emailCur.moveToNext()) {
                                    // This would allow you get several email addresses
                                    // if the email addresses were stored in an array
                                    emailAddress = emailCur.getString(
                                            emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                                    String emailType = emailCur.getString(
                                            emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));

                                    System.out.println("Email " + emailAddress + " Email Type : " + emailType);
                                }
                                emailCur.close();
                                if (phoneNumber.length() == 10) {
                                    // Toast.makeText(context, "" + current.getMobileNumber(), Toast.LENGTH_SHORT).show();
                                    mobile_no = phoneNumber;
                                    prefEditor.putString("st_mobile_no", mobile_no);
                                    prefEditor.commit();
                                }
                                if (phoneNumber.length() == 11) {
                                    //Toast.makeText(context, "" + current.getMobileNumber().substring(3, 13), Toast.LENGTH_SHORT).show();
                                    mobile_no = phoneNumber.substring(1, 11);
                                    prefEditor.putString("st_mobile_no", mobile_no);
                                    prefEditor.commit();

                                }
                                if (phoneNumber.length() == 13) {
                                    //Toast.makeText(context, "" + current.getMobileNumber().substring(3, 13), Toast.LENGTH_SHORT).show();
                                    mobile_no = phoneNumber.substring(3, 13);
                                    prefEditor.putString("st_mobile_no", mobile_no);
                                    prefEditor.commit();

                                }
                                new availableStudent().execute();


                            }
                        }
                    }
                }
                break;
        }
    }


    public boolean validate(String name, String lastname, String phoneno, String emailid, String spinnerSelected) {
        boolean isValidate = false;
        if (spinnerSelected.trim().compareTo("") == 0) {
            Toast.makeText(getActivity(), "Please select gender.", Toast.LENGTH_LONG).show();
        } else if (name.trim().compareTo("") == 0) {
            Toast.makeText(getActivity(), "Please enter  first name", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (lastname.trim().compareTo("") == 0) {
            Toast.makeText(getActivity(), "Please enter  last name", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (phoneno.trim().compareTo("") == 0) {
            Toast.makeText(getActivity(), "Please enter mobile No.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (phoneno.trim().compareTo("") == 0 || phoneno.length() != 10) {
            Toast.makeText(getActivity(), "Please enter a 10 digit valid Mobile No.", Toast.LENGTH_LONG).show();
            isValidate = false;
        } else if (!validateEmail(emailid)) {
            if (!emailid.equals("")) {
                Toast.makeText(getActivity(), "Please enter valid Email Id.", Toast.LENGTH_LONG).show();
                isValidate = false;
            } else {
                isValidate = true;
            }
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

    public void sendData(final String name, final String phoneno, final String emailid, final String lastname, final String spinnerSelected) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar cal = Calendar.getInstance();
        final String date = format.format(cal.getTime());
        //final String token = SharedPrefManager.getInstance(getActivity()).getDeviceToken();
        jsonObj = new JSONObject() {
            {
                try {
                    put("first_name", name);
                    put("last_name", lastname);
                    put("mobile_no", phoneno);
                    put("email_id", emailid);
                    put("gender", spinnerSelected);
                    put("fcm_id", "Admin");
                    put("created_at", date);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Thread objectThread = new Thread(new Runnable() {
            public void run() {
                // TODO Auto-generated method stub
                WebClient serviceAccess = new WebClient();

                Log.i("loginResponse", "jsonObj" + jsonObj);
                loginResponse = serviceAccess.SendHttpPost(Config.URL_STUDENT_REGISTRATION, jsonObj);

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
                                        message = jObject.getString("message");


                                        if (status) {
                                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                                            user_id= jObject.getString("user_id");
                                            prefEditor.putString("user_id",user_id);
                                            prefEditor.putString("student_mob_sms",phoneno);
                                            prefEditor.putString("st_first_name",name);
                                            prefEditor.putString("st_last_name",lastname);
                                            prefEditor.commit();

                                            update();
                                            dismiss();
                                        } else {
                                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
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
        /*if (preferences.getString("book_seat", "").equals("book_seat")) {
            Intent a = new Intent(context, BookSeatActivity.class);
            startActivity(a);
        } else if (preferences.getString("book_seat", "").equals("student_pre")) {
            preferences.edit().remove("st_mobile_no").commit();
            preferences.edit().remove("emil_id").commit();
            preferences.edit().remove("student_last_name").commit();
            Intent a = new Intent(context, SearchStudentEditPreActivity.class);
            startActivity(a);
           // mListener.messageReceived(mobile_no);
        }*/

       // Intent intent=new Intent(context,DisplayStudentEditPreActivity.class);
        //startActivity(intent);
        dismiss();
    }

    private void update1() {
        preferences.edit().remove("st_mobile_no").commit();
        preferences.edit().remove("emil_id").commit();
        preferences.edit().remove("student_last_name").commit();
        dismiss();
    }

    private class availableStudent extends AsyncTask<Void, Void, Void> {
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
                        put("mobile_no", mobile_no);

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
                        status = jsonObject.getBoolean("status");
                        if(status) {
                            user_id = jsonObject.getString("user_id");
                            prefEditor.putString("user_id", jsonObject.getString("user_id"));
                            prefEditor.putString("st_first_name", first_name);
                            prefEditor.putString("st_last_name", last_name);
                            prefEditor.putString("student_mob_sms",mobile_no);
                            prefEditor.commit();

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
            if (centerResponse.compareTo("") != 0) {
                if (status) {
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                        android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboard.setText(mobile_no);
                    } else {
                        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        android.content.ClipData clip = android.content.ClipData.newPlainText("myLabel", mobile_no);
                        clipboard.setPrimaryClip(clip);
                    }
                    phEdtTxt.setText("");
                    emailEdtTxt.setText("");
                    nameEdtTxt.setText("");
                    lastnameEdtTxt.setText("");
                    // Toast.makeText(context, "User Already exixts", Toast.LENGTH_LONG).show();
                    dismiss();
                  //  Intent intent=new Intent(context, DisplayStudentEditPreActivity.class);
                    //startActivity(intent);
                    // mListener.messageReceived(mobile_no);

                } else {
                    if (!emailAddress.equals("")) {
                        emailEdtTxt.setText(emailAddress);
                    }
                    if (!phoneNumber.equals("")) {
                        phEdtTxt.setText(mobile_no);

                    }
                    if (!first_name.equals("")) {

                        nameEdtTxt.setText(first_name);
                        // lastnameEdtTxt.setText(last_name);
                    }
                    if (!last_name.equals("")) {


                        lastnameEdtTxt.setText(last_name);
                    }

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
    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }

}