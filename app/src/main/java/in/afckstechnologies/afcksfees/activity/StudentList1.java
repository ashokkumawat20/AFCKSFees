package in.afckstechnologies.afcksfees.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.afckstechnologies.afcksfees.MailAPI.GMailSender;
import in.afckstechnologies.afcksfees.R;
import in.afckstechnologies.afcksfees.adapter.OnGoingBatchesListAdpter;
import in.afckstechnologies.afcksfees.adapter.StudentListAdpter;
import in.afckstechnologies.afcksfees.jsonparser.JsonHelper;
import in.afckstechnologies.afcksfees.model.BatchDAO;
import in.afckstechnologies.afcksfees.model.StudentListDAO;
import in.afckstechnologies.afcksfees.model.StudentsDAO;
import in.afckstechnologies.afcksfees.utils.AppStatus;
import in.afckstechnologies.afcksfees.utils.Config;
import in.afckstechnologies.afcksfees.utils.Constant;
import in.afckstechnologies.afcksfees.utils.FeesListener;
import in.afckstechnologies.afcksfees.utils.SmsListener;
import in.afckstechnologies.afcksfees.utils.VersionChecker;
import in.afckstechnologies.afcksfees.utils.WebClient;
import in.afckstechnologies.afcksfees.view.ActualBatchTimingsView;
import in.afckstechnologies.afcksfees.view.BatchModifyView;
import in.afckstechnologies.afcksfees.view.CommentAddView;
import in.afckstechnologies.afcksfees.view.FeesDetailsView;
import in.afckstechnologies.afcksfees.view.MultipleCommentAddView;
import in.afckstechnologies.afcksfees.view.RegistrationView;
import in.afckstechnologies.afcksfees.view.StudentDiscontinueEntryView;
import in.afckstechnologies.afcksfees.view.StudentFeesEntryView;
import in.afckstechnologies.afcksfees.view.StudentTransferFeesEntryView;
import in.afckstechnologies.afcksfees.view.UpdateRefBatchEntryView;


public class StudentList1 extends AppCompatActivity {
    // LogCat tag
    private static final String TAG = StudentList1.class.getSimpleName();
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    AutoCompleteTextView autoCompleteTextViewBatch;
    String studentResponse = "";
    public List<BatchDAO> batchArrayList;
    public ArrayAdapter<BatchDAO> aAdapter;
    public ArrayAdapter<StudentListDAO> studentListDAOArrayAdapter;
    public BatchDAO batchDAO;
    String batch_id = "";
    private JSONObject jsonSchedule, jsonObj, syncJsonObject;
    private JSONObject jsonLeadObj, jsonObject, jsonObj1, jsonObjectSync;
    ProgressDialog mProgressDialog;
    String studentListResponse = "";
    JSONArray jsonArray, jsonArraySync;
    List<StudentsDAO> data;
    StudentListAdpter studentListAdpter;
    private RecyclerView mstudentList;
    private FloatingActionButton fab;
    String newTextBatch, newTextStudent;
    AutoCompleteTextView autoCompleteTextViewStudent;
    ImageView add_student, clear, clear_batch, edit_batch, info_batch, dueDateChange, batchMarkedAttendance, getcode;
    public List<StudentListDAO> studentArrayList;
    String student_id = "", pendingTaskResponse = "";
    public StudentListDAO studentListDAO;
    String addStudentRespone = "", attendanceListResponse = "", justDialFeedbackResponse = "", just_dail_txt = "", syncDataesponse = "", selectedDate = "", check_list = "0";
    boolean status;
    String message = "";
    String msg = "";
    //sendind data to next activity
    Button sendData;
    ImageView batchAttendance;
    ArrayList<String> studentMailIdArrayList;
    ArrayList<String> studentMobileNoArrayList;
    ArrayList<String> nameArrayList;

    ArrayList<String> studentNameArrayList;
    ArrayList<String> studentUserIdArrayList;
    ArrayList<String> studentBatchIdArrayList;
    ArrayList<String> mobileNoArrayList;
    String start_date = "";
    String course_name = "";
    String notes = "";
    String timings = "";
    String frequency = "";
    String duration = "";
    String branch_name = "";
    String fees = "", batch_code = "";
    RelativeLayout footer;
    String flag = "";
    private static final String username = "info@afcks.com";
    private static String password = "at!@#123";
    GMailSender sender;
    String completion_status = "";
    int temp_size;
    CheckBox chkAll;
    //
    int Status_id = 1;
    RadioGroup radioGroup;
    int pos;
    int pa = 0;
    String verifycode = "";
    String gender = "";
    RadioButton normal, corporate;
    TextView receipt, notestxt;
    String FeesApplicable = "";
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    String verifyMobileDeviceIdResponse = "";
    String deviceId = "";
    TelephonyManager telephonyManager;
    //update apk
    private String latestVersion = "", smspassResponse = "", getRoleusersResponse = "";
    int m_count = 0;
    int count = 1;
    private ProgressDialog dialog;
    String studentFeedBackCode, name, mobile_no;
    static String sms_user = "";
    static String sms_pass = "";
    ImageView settingimg;
    ImageView btnClosePopup;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floating_main_layout);
        preferences = getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        prefEditor.putBoolean("0", true);
        prefEditor.commit();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.
                Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // Add your mail Id and Password
        sender = new GMailSender(username, password);
        prefEditor.putString("mail_a_flag", "enter_student");
        prefEditor.commit();
        clear = (ImageView) findViewById(R.id.clear);
        clear_batch = (ImageView) findViewById(R.id.clear_batch);
        add_student = (ImageView) findViewById(R.id.add_student);
        edit_batch = (ImageView) findViewById(R.id.edit_batch);
        info_batch = (ImageView) findViewById(R.id.info_batch);

        mstudentList = (RecyclerView) findViewById(R.id.studentsList);
        batchAttendance = (ImageView) findViewById(R.id.batchAttendance);
        dueDateChange = (ImageView) findViewById(R.id.dueDateChange);
        settingimg = (ImageView) findViewById(R.id.settingimg);
        getcode = (ImageView) findViewById(R.id.getcode);
        batchMarkedAttendance = (ImageView) findViewById(R.id.batchMarkedAttendance);
        normal = (RadioButton) findViewById(R.id.normal);
        corporate = (RadioButton) findViewById(R.id.corporate);
        receipt = (TextView) findViewById(R.id.receipt);
        notestxt = (TextView) findViewById(R.id.notes);
        studentArrayList = new ArrayList<StudentListDAO>();
        batchArrayList = new ArrayList<BatchDAO>();
        data = new ArrayList<>();
        autoCompleteTextViewStudent = (AutoCompleteTextView) findViewById(R.id.SearchStudent);
        footer = (RelativeLayout) findViewById(R.id.footer);
        autoCompleteTextViewBatch = (AutoCompleteTextView) findViewById(R.id.SearchBatch);
        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
          //  deviceId = getDeviceId();
            verifyMobileDeviceId();
        }
        Intent intent1 = getIntent();
        String action = intent1.getAction();
        String type = intent1.getType();
        sms_user = preferences.getString("sms_username", "");
        sms_pass = preferences.getString("sms_password", "");
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent1); // Handle text being sent
            } else if (type.startsWith("image/")) {
                // handleSendImage(intent); // Handle single image being sent
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                // handleSendMultipleImages(intent); // Handle multiple images being sent
            }
        } else {
            // Handle other intents, such as being started from the home screen
        }
        if (preferences.getString("prebatchcall", "").equals("1")) {
            prefEditor.putString("prebatchcall", "0");
            prefEditor.commit();
            Intent intent = getIntent();
            autoCompleteTextViewBatch.setText(intent.getStringExtra("batch_id"));
            batch_id = intent.getStringExtra("batch_id");
            new getStudentList().execute();
        }


        if (preferences.getString("prebatchcall", "").equals("2")) {
            prefEditor.putString("prebatchcall", "0");
            prefEditor.commit();
            Intent intent = getIntent();
            autoCompleteTextViewBatch.setText(intent.getStringExtra("batch_id"));
            batch_id = intent.getStringExtra("batch_id");
            Status_id = 0;
            normal.setChecked(false);
            corporate.setChecked(true);
            new getStudentList().execute();
        }

        if (preferences.getString("trainer_user_id", "").equals("RS")) {
            receipt.setVisibility(View.VISIBLE);
        }
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub

                // Method 1 For Getting Index of RadioButton
                pos = radioGroup.indexOfChild(findViewById(checkedId));
                switch (pos) {
                    case 1:
                        Status_id = 0;
                        new getStudentList().execute();
                        // Toast.makeText(getApplicationContext(), "1"+Status_id,Toast.LENGTH_SHORT).show();
                        break;
                    case 2:

                        //Status_id = 2;
                        // Intent intent = new Intent(StudentList.this, BankingDetailsActivity.class);
                        // startActivity(intent);
                        // Toast.makeText(getApplicationContext(), "2"+Status_id, Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        //The default selection is RadioButton 1
                        Status_id = 1;
                        new getStudentList().execute();
                        //  Toast.makeText(getApplicationContext(), "3"+Status_id,Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentList1.this, BankingDetailsActivity.class);
                startActivity(intent);
            }
        });

        notestxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!batch_id.equals("")) {
                    Intent intent = new Intent(StudentList1.this, UpcomingClassDetailsActivity.class);
                    intent.putExtra("batch_code", batch_id);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter Batch code ", Toast.LENGTH_LONG).show();
                }

            }
        });
        autoCompleteTextViewBatch.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        autoCompleteTextViewBatch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                // getBatchSelect(newTextBatch);

                if (!TextUtils.isEmpty(autoCompleteTextViewBatch.getText())) {
                    info_batch.setVisibility(View.GONE);
                    getcode.setVisibility(View.GONE);
                    clear_batch.setVisibility(View.VISIBLE);
                    edit_batch.setVisibility(View.VISIBLE);
                    newTextBatch = s.toString();
                    getBatchSelect(autoCompleteTextViewBatch.getText().toString());

                } else {
                    clear_batch.setVisibility(View.GONE);
                    edit_batch.setVisibility(View.GONE);
                    info_batch.setVisibility(View.VISIBLE);
                    getcode.setVisibility(View.VISIBLE);
                    newTextBatch = "";
                }
            }
        });


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                prefEditor.putString("student_name", newTextStudent);
                prefEditor.commit();
                RegistrationView registrationView = new RegistrationView();
                registrationView.show(getSupportFragmentManager(), "registrationView");

            }
        });


        autoCompleteTextViewStudent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteTextViewStudent.getText())) {
                        clear.setVisibility(View.VISIBLE);
                        getchannelPartnerSelect(autoCompleteTextViewStudent.getText().toString());

                    } else {
                        clear.setVisibility(View.GONE);
                        add_student.setVisibility(View.GONE);
                        student_id = "";
                    }
                }
                return false;
            }
        });


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentArrayList.clear();
                autoCompleteTextViewStudent.setText("");
                clear.setVisibility(View.GONE);
                add_student.setVisibility(View.GONE);


            }
        });

        clear_batch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextViewBatch.setText("");
                clear_batch.setVisibility(View.GONE);
                edit_batch.setVisibility(View.GONE);
                info_batch.setVisibility(View.VISIBLE);
                getcode.setVisibility(View.VISIBLE);

                batch_id = "";
                normal.setText("Existing");
                corporate.setText("Discontinued");
                footer.setVisibility(View.GONE);
                if (data.size() > 0) {
                    data.clear(); // this list which you hava passed in Adapter for your listview
                    studentListAdpter.notifyDataSetChanged(); // notify to listview for refresh
                }


            }
        });
        add_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!batch_id.equals("")) {
                    if (!gender.equals("")) {

//Dialog code
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(StudentList1.this);
                        View mView = getLayoutInflater().inflate(R.layout.dialog_app_updates, null);
                        CheckBox mCheckBox = mView.findViewById(R.id.checkBox);
                        mCheckBox.setChecked(getDialogStatus());
                        mBuilder.setTitle("Add Guest Student");
                        mBuilder.setMessage("If below is checked Student will get fees reminder ");
                        mBuilder.setView(mView);
                        mBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if (!student_id.equals("")) {

                                    if (getDialogStatus()) {
                                        FeesApplicable = "1";
                                    } else {
                                        FeesApplicable = "0";
                                    }
                                    new submitData().execute();
                                    dialogInterface.cancel();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Please select Student", Toast.LENGTH_LONG).show();
                                    dialogInterface.cancel();
                                }

                            }
                        });
                        mBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                            }
                        });
                        AlertDialog mDialog = mBuilder.create();
                        mDialog.show();
                        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                if (compoundButton.isChecked()) {
                                    storeDialogStatus(true);
                                } else {
                                    storeDialogStatus(false);
                                }
                            }
                        });
                        mDialog.show();
                        /*if(getDialogStatus()){
                            mDialog.hide();
                        }else{
                            mDialog.show();
                        }
*/

                    } else {
                        Toast.makeText(getApplicationContext(), "Please Update Gender...", Toast.LENGTH_LONG).show();
                        prefEditor.putString("user_id", student_id);
                        prefEditor.putString("edit_u_p_flag", "fu");
                        prefEditor.commit();
                        Intent intent = new Intent(StudentList1.this, Activity_User_Profile.class);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter Batch code ", Toast.LENGTH_LONG).show();
                }
            }
        });


        /** Getting reference to checkbox available in the main.xml layout */
        chkAll = (CheckBox) findViewById(R.id.chkAllSelected);
        /** Setting a click listener for the checkbox **/
        if (preferences.getString("allcheck", "").equals("1")) {
            chkAll.setChecked(true);
        } else {
            chkAll.setChecked(false);
        }
        chkAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckBox cb = (CheckBox) v;
                //Toast.makeText(v.getContext(), "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked(), Toast.LENGTH_LONG).show();
                if (cb.isChecked()) {
                    prefEditor.putString("cscb", "1");
                    prefEditor.commit();
                    List<StudentsDAO> list = ((StudentListAdpter) studentListAdpter).getSservicelist();
                    for (StudentsDAO workout : list) {
                        workout.setSelected(true);
                        prefEditor.putBoolean(workout.getUser_id(), true);
                        prefEditor.commit();
                    }
                    prefEditor.putString("allcheck", "1");
                    prefEditor.commit();
                    ((StudentListAdpter) mstudentList.getAdapter()).notifyDataSetChanged();
                } else {
                    prefEditor.putString("cscb", "0");
                    prefEditor.commit();
                    List<StudentsDAO> list = ((StudentListAdpter) studentListAdpter).getSservicelist();
                    for (StudentsDAO workout : list) {
                        workout.setSelected(false);
                        prefEditor.remove(workout.getUser_id());
                        prefEditor.commit();
                    }
                    prefEditor.putString("allcheck", "0");
                    prefEditor.commit();
                    ((StudentListAdpter) mstudentList.getAdapter()).notifyDataSetChanged();
                }
            }
        });

        sendData = (Button) findViewById(R.id.sendData);
        sendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String data1 = "";
                String data2 = "";
                String data3 = "";
                studentMobileNoArrayList = new ArrayList<>();
                nameArrayList = new ArrayList<>();
                studentMailIdArrayList = new ArrayList<>();
                List<StudentsDAO> stList = ((StudentListAdpter) studentListAdpter).getSservicelist();

                for (int i = 0; i < stList.size(); i++) {
                    StudentsDAO serviceListDAO = stList.get(i);
                    if (serviceListDAO.isSelected() == true) {
                        data1 = serviceListDAO.getMobile_no().toString();
                        data2 = serviceListDAO.getFirst_name().toString();
                        data3 = serviceListDAO.getEmail_id().toString();
                        studentMobileNoArrayList.add(data1);
                        nameArrayList.add(data2);
                        studentMailIdArrayList.add(data3);
                    } else {
                        System.out.println("not selected");
                    }
                }

                if (studentMobileNoArrayList.size() > 0) {
                    Intent intent = new Intent(StudentList1.this, WriteSendSmsActivity.class);
                    Bundle args = new Bundle();
                    args.putSerializable("ARRAYLIST", (Serializable) studentMobileNoArrayList);
                    args.putSerializable("ARRAYLIST1", (Serializable) nameArrayList);
                    args.putSerializable("ARRAYLIST2", (Serializable) studentMailIdArrayList);
                    intent.putExtra("BUNDLE", args);
                    intent.putExtra("start_date", start_date);
                    intent.putExtra("course_name", course_name);
                    intent.putExtra("notes", notes);
                    intent.putExtra("timings", timings);
                    intent.putExtra("frequency", frequency);
                    intent.putExtra("duration", duration);
                    intent.putExtra("branch_name", branch_name);
                    intent.putExtra("fees", fees);
                    startActivity(intent);


                } else {
                    Toast.makeText(getApplicationContext(), "Please select student from list", Toast.LENGTH_LONG).show();
                }


            }
        });


        batchAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String data1 = "";
                String data2 = "";
                String data3 = "";
                boolean data4;

                studentMobileNoArrayList = new ArrayList<>();
                nameArrayList = new ArrayList<>();
                studentNameArrayList = new ArrayList<>();
                mobileNoArrayList = new ArrayList<>();
                studentUserIdArrayList = new ArrayList<>();
                studentBatchIdArrayList = new ArrayList<>();
                List<StudentsDAO> stList = ((StudentListAdpter) studentListAdpter).getSservicelist();

                for (int i = 0; i < stList.size(); i++) {
                    StudentsDAO serviceListDAO = stList.get(i);

                    data1 = serviceListDAO.getUser_id().toString();
                    data2 = serviceListDAO.getBatchid().toString();
                    data3 = serviceListDAO.getStudents_Name().toString();
                    data4 = serviceListDAO.isSelected();
                    studentMobileNoArrayList.add(data1);
                    nameArrayList.add(data2);
                    mobileNoArrayList.add("" + data4);
                    studentNameArrayList.add(data3);
                    if (serviceListDAO.isSelected()) {
                        check_list = "1";
                    }

                }

                temp_size = studentMobileNoArrayList.size();
                if (check_list.equals("1")) {

                    if (!batch_id.equals("")) {
                        check_list = "0";
                        prefEditor.putString("batch_class", batch_id);
                        prefEditor.commit();
                        ActualBatchTimingsView actualBatchTimingsView = new ActualBatchTimingsView();
                        actualBatchTimingsView.show(getSupportFragmentManager(), "actualBatchTimingsView");

                    } else {
                        Toast.makeText(getApplication(), "Please select batch ", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please select student from list", Toast.LENGTH_LONG).show();
                }

            }
        });
        batchMarkedAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data1 = "";
                String data2 = "";
                String data3 = "";
                boolean data4;

                studentMobileNoArrayList = new ArrayList<>();
                nameArrayList = new ArrayList<>();
                studentNameArrayList = new ArrayList<>();
                mobileNoArrayList = new ArrayList<>();
                studentUserIdArrayList = new ArrayList<>();
                studentBatchIdArrayList = new ArrayList<>();
                List<StudentsDAO> stList = ((StudentListAdpter) studentListAdpter).getSservicelist();

                for (int i = 0; i < stList.size(); i++) {
                    StudentsDAO serviceListDAO = stList.get(i);

                    data1 = serviceListDAO.getUser_id().toString();
                    data2 = serviceListDAO.getBatchid().toString();
                    data3 = serviceListDAO.getStudents_Name().toString();
                    data4 = serviceListDAO.isSelected();
                    studentMobileNoArrayList.add(data1);
                    nameArrayList.add(data2);
                    mobileNoArrayList.add("" + data4);
                    studentNameArrayList.add(data3);
                    if (serviceListDAO.isSelected()) {
                        check_list = "1";
                    }

                }

                temp_size = studentMobileNoArrayList.size();
                if (check_list.equals("1")) {

                    if (!batch_id.equals("")) {

                        //Toast.makeText(getApplication(), "selected ", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(StudentList1.this);
                        builder.setMessage("Do you want to update Today Attendance ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        try {
                                            try {
                                                check_list = "0";
                                                new SendingMailsTask().execute(m_count + "", studentMobileNoArrayList.get(m_count));
                                            } catch (Exception ex) {
                                                Toast.makeText(StudentList1.this, ex.toString(), Toast.LENGTH_LONG).show();
                                            }

                                        } catch (Exception ex) {
                                            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                                        }
                                        dialog.cancel();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Action for 'NO' Button
                                        dialog.cancel();
                                    }
                                });

                        //Creating dialog box
                        AlertDialog alert = builder.create();
                        //Setting the title manually
                        alert.setTitle("Update Attendance");
                        alert.show();


                    } else {
                        Toast.makeText(getApplication(), "Please select batch ", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please select student from list", Toast.LENGTH_LONG).show();
                }
            }
        });

        dueDateChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data1 = "";

                studentMobileNoArrayList = new ArrayList<>();

                List<StudentsDAO> stList = ((StudentListAdpter) studentListAdpter).getSservicelist();

                for (int i = 0; i < stList.size(); i++) {
                    StudentsDAO serviceListDAO = stList.get(i);
                    if (serviceListDAO.isSelected()) {
                        data1 = serviceListDAO.getUser_id();
                        studentMobileNoArrayList.add(data1);
                    }


                }

                if (studentMobileNoArrayList.size() > 0) {
                    if (!batch_id.equals("")) {

                        final Calendar c = Calendar.getInstance();

                        DatePickerDialog dpd = new DatePickerDialog(StudentList1.this,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                        AlertDialog.Builder builder = new AlertDialog.Builder(StudentList1.this);
                                        builder.setMessage("Do you want to Update Due Date " + selectedDate + " ?")
                                                .setCancelable(false)
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {

                                                        jsonArraySync = new JSONArray();
                                                        for (int i = 0; i < studentMobileNoArrayList.size(); i++) {
                                                            jsonObjectSync = new JSONObject();
                                                            try {
                                                                jsonObjectSync.put("batch_id", batch_id);
                                                                jsonObjectSync.put("user_id", studentMobileNoArrayList.get(i));
                                                                jsonObjectSync.put("next_due_date", selectedDate);
                                                                jsonArraySync.put(jsonObjectSync);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                        }

                                                        try {
                                                            syncJsonObject = new JSONObject();
                                                            syncJsonObject.put("dueDates", jsonArraySync);
                                                            Log.d("dueDates", "" + syncJsonObject);


                                                            Thread objectThread = new Thread(new Runnable() {
                                                                public void run() {
                                                                    // TODO Auto-generated method stub
                                                                    WebClient serviceAccess = new WebClient();
                                                                    Log.i("json", "json" + jsonObj);
                                                                    syncDataesponse = serviceAccess.SendHttpPost(Config.URL_USERDUEDATESYNCDATA, syncJsonObject);
                                                                    Log.i("resp", "syncDataesponse" + syncDataesponse);
                                                                    final Handler handler = new Handler(Looper.getMainLooper());
                                                                    Runnable runnable = new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            handler.post(new Runnable() { // This thread runs in the UI
                                                                                @Override
                                                                                public void run() {
                                                                                    if (syncDataesponse.compareTo("") == 0) {

                                                                                    } else {

                                                                                        try {
                                                                                            JSONObject jObject = new JSONObject(syncDataesponse);
                                                                                            status = jObject.getBoolean("status");
                                                                                            if (status) {
                                                                                                Toast.makeText(getApplicationContext(), "Due Dates updated successfully", Toast.LENGTH_SHORT).show();
                                                                                                for (int i = 0; i < studentMobileNoArrayList.size(); i++) {
                                                                                                    prefEditor.remove(studentMobileNoArrayList.get(i));
                                                                                                    prefEditor.commit();
                                                                                                    new getStudentList().execute();// notify to listview for refresh
                                                                                                }

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


                                                        } catch (JSONException e1) {
                                                            // TODO Auto-generated catch block
                                                            e1.printStackTrace();
                                                        }


                                                    }
                                                })
                                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        //  Action for 'NO' Button
                                                        dialog.cancel();
                                                    }
                                                });

                                        //Creating dialog box
                                        AlertDialog alert = builder.create();
                                        //Setting the title manually
                                        alert.setTitle("Updating Due Date");
                                        alert.show();

                                    }
                                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                        dpd.show();


                    } else {
                        Toast.makeText(getApplication(), "Please select batch ", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Please select student from list", Toast.LENGTH_LONG).show();
                }

            }
        });
        batchAttendance.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //  Toast.makeText(getApplicationContext(), "Batch code inserted successfully !", Toast.LENGTH_LONG).show();
                if (!batch_id.equals("")) {

                    new getVerifyCode().execute();
                } else {
                    Toast.makeText(getApplication(), "Please select batch ", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });
        batchMarkedAttendance.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //  Toast.makeText(getApplicationContext(), "Batch code inserted successfully !", Toast.LENGTH_LONG).show();
                if (!batch_id.equals("")) {

                    new getVerifyCode().execute();
                } else {
                    Toast.makeText(getApplication(), "Please select batch ", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });

        //
        edit_batch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!batch_id.equals("")) {
                    prefEditor.putString("batch_mofiy", batch_id);
                    prefEditor.commit();
                    BatchModifyView batchModifyView = new BatchModifyView();
                    batchModifyView.show(getSupportFragmentManager(), "batchModifyView");
                } else {
                    Toast.makeText(getApplication(), "Please select batch ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        info_batch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //StatsBatchStudentsView statsBatchStudentsView = new StatsBatchStudentsView();
                //statsBatchStudentsView.show(getSupportFragmentManager(), "statsBatchStudentsView");
                Intent intent = new Intent(StudentList1.this, OngoingBatchesActivity.class);
                startActivity(intent);

            }
        });
        StudentFeesEntryView.bindListener(new FeesListener() {
            @Override
            public void messageReceived(String messageText) {
                new getStudentList().execute();
            }
        });

        Activity_User_Profile.bindListener(new FeesListener() {
            @Override
            public void messageReceived(String messageText) {
                gender = messageText;
                new getStudentList().execute();
            }
        });
        StudentListAdpter.bindListener(new FeesListener() {
            @Override
            public void messageReceived(String messageText) {
                new getStudentList().execute();
            }
        });

        MultipleCommentAddView.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                // Toast.makeText(getApplicationContext(), messageText, Toast.LENGTH_SHORT).show();
                List<StudentsDAO> list = ((StudentListAdpter) studentListAdpter).getSservicelist();
                for (StudentsDAO workout : list) {
                    if (preferences.getString("user_id", "").equals(workout.getUser_id())) {
                        workout.setNotes(messageText);
                    }

                }
                ((StudentListAdpter) mstudentList.getAdapter()).notifyDataSetChanged();
            }
        });
        StudentDiscontinueEntryView.bindListener(new FeesListener() {
            @Override
            public void messageReceived(String messageText) {
                new getStudentList().execute();
            }
        });
        RegistrationView.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                //Toast.makeText(getApplicationContext(), messageText, Toast.LENGTH_LONG).show();
                autoCompleteTextViewStudent.setText(messageText);
                getchannelPartnerSelect(messageText);

            }
        });

        CommentAddView.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                new getStudentList().execute();
            }
        });
        StudentTransferFeesEntryView.bindListener(new FeesListener() {
            @Override
            public void messageReceived(String messageText) {
                new getStudentList().execute();
            }
        });
        FeesDetailsView.bindListener(new FeesListener() {
            @Override
            public void messageReceived(String messageText) {
                new getStudentList().execute();
            }
        });

        OnGoingBatchesListAdpter.bindListener(new FeesListener() {
            @Override
            public void messageReceived(String messageText) {
                autoCompleteTextViewBatch.setText(messageText);
                batch_id = messageText;
                new getStudentList().execute();
            }
        });

        ActualBatchTimingsView.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                try {
                    batchAttendance.setVisibility(View.GONE);
                    batchMarkedAttendance.setVisibility(View.VISIBLE);
                    new SendingMailsTask().execute(m_count + "", studentMobileNoArrayList.get(m_count));
                } catch (Exception ex) {
                    Toast.makeText(StudentList1.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

        settingimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiatePopupWindow();
            }
        });

        getcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    new getVerifyCodeWithUpdate().execute();


                } else {

                    Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private PopupWindow pwindo;

    private void initiatePopupWindow() {
        try {
// We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) StudentList1.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.screen_popup, (ViewGroup) findViewById(R.id.popup_element));
            pwindo = new PopupWindow(layout, 350, 400, true);
            pwindo.showAtLocation(layout, Gravity.TOP | Gravity.RIGHT, 15, 55);
            btnClosePopup = (ImageView) layout.findViewById(R.id.close);
            btnClosePopup.setOnClickListener(cancel_button_click_listener);

            CheckBox not = (CheckBox) layout.findViewById(R.id.chkSelected);
            CheckBox notm = (CheckBox) layout.findViewById(R.id.chkSelectedm);
            if (preferences.getString("current_send_sms", "").equals("sendsms")) {
                not.setChecked(true);
                prefEditor.putString("current_send_sms", "sendsms");
                prefEditor.commit();

            } else if (preferences.getString("current_send_sms", "").equals("notsendsms")) {
                not.setChecked(false);
                prefEditor.putString("current_send_sms", "notsendsms");
                prefEditor.commit();
            }
            not.setOnClickListener(new View.OnClickListener() {

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
            notm.setOnClickListener(new View.OnClickListener() {

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener cancel_button_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            pwindo.dismiss();

        }
    };

    /**
     * Called when the activity is about to become visible.
     */
    @Override
    protected void onStart() {
        super.onStart();

        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
            getUserRoles();
            new smspassAvailable().execute();

        }


    }

    /**
     * Called when the activity has become visible.
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
            // getUserRoles();
            // new smspassAvailable().execute();
        }


    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
            autoCompleteTextViewBatch.setText(sharedText);
            batch_id = sharedText;
            prefEditor.putString("batch_id", batch_id);
            prefEditor.commit();
            new getStudentList().execute();
        }

    }


    private class smspassAvailable extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            //  mProgressDialog = new ProgressDialog(TrainerVerfiyActivity.this);
            // Set progressdialog title
            // mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            // mProgressDialog.setMessage("Loading...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            //   mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            jsonObj = new JSONObject() {
                {
                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            WebClient serviceAccess = new WebClient();
            Log.i("json", "json" + jsonObj);
            smspassResponse = serviceAccess.SendHttpPost(Config.URL_GETUSERNAMEPASSSMS, jsonObj);
            Log.i("resp", "smspassResponse" + smspassResponse);


            if (smspassResponse.compareTo("") != 0) {
                if (isJSONValid(smspassResponse)) {


                    try {

                        JSONArray introJsonArray = new JSONArray(smspassResponse);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Please check your webservice", Toast.LENGTH_LONG).show();
                }
            } else {

                Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_LONG).show();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

            try {
                JSONArray introJsonArray = new JSONArray(smspassResponse);
                for (int i = 0; i < introJsonArray.length(); i++) {
                    JSONObject introJsonObject = introJsonArray.getJSONObject(i);
                    if (introJsonObject.getString("type").equals("sms")) {
                        prefEditor.putString("sms_username", introJsonObject.getString("sms_id"));
                        prefEditor.putString("sms_password", introJsonObject.getString("password"));
                        prefEditor.commit();
                    }

                    if (introJsonObject.getString("type").equals("AFCKS_email")) {
                        prefEditor.putString("mail_username", introJsonObject.getString("sms_id"));
                        prefEditor.putString("mail_password", introJsonObject.getString("password"));
                        prefEditor.commit();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            // Close the progressdialog
            //  mProgressDialog.dismiss();

        }
    }

    public void getUserRoles() {


        jsonObj1 = new JSONObject() {
            {
                try {
                    put("user_id", preferences.getString("trainer_user_id", ""));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Thread objectThread = new Thread(new Runnable() {
            public void run() {
                // TODO Auto-generated method stub
                WebClient serviceAccess = new WebClient();
                getRoleusersResponse = serviceAccess.SendHttpPost(Config.URL_GETAVAILABLEUSERROLES, jsonObj1);
                Log.i("getRoleusersResponse", getRoleusersResponse);
                final Handler handler = new Handler(Looper.getMainLooper());
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() { // This thread runs in the UI
                            @Override
                            public void run() {
                                if (getRoleusersResponse.compareTo("") == 0) {

                                } else {

                                    try {
                                        JSONObject jObject = new JSONObject(getRoleusersResponse);
                                        status = jObject.getBoolean("status");

                                        if (status) {
                                            forceUpdate();
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

    public void forceUpdate() {
        //  int playStoreVersionCode = FirebaseRemoteConfig.getInstance().getString("android_latest_version_code");
        VersionChecker versionChecker = new VersionChecker();
        try {
            latestVersion = versionChecker.execute().get();
            /*if (latestVersion.length() > 0) {
                latestVersion = latestVersion.substring(50, 58);
                latestVersion = latestVersion.trim();
            }*/


            Log.d("versoncode", "" + latestVersion);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        PackageManager packageManager = this.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //  String currentVersion = packageInfo.versionName;
        String currentVersion = packageInfo.versionName;

        new ForceUpdateAsync(currentVersion, StudentList1.this).execute();

    }

    public class ForceUpdateAsync extends AsyncTask<String, String, JSONObject> {


        private String currentVersion;
        private Context context;

        public ForceUpdateAsync(String currentVersion, Context context) {
            this.currentVersion = currentVersion;
            this.context = context;
        }

        @Override
        protected JSONObject doInBackground(String... params) {


            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (latestVersion != null) {
                if (!latestVersion.equals("")) {
                    if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                        // Toast.makeText(context,"update is available.",Toast.LENGTH_LONG).show();

                        if (!((Activity) context).isFinishing()) {
                            showForceUpdateDialog();
                        }


                    }
                } else {
                    if (AppStatus.getInstance(getApplicationContext()).isOnline()) {

                        // AppUpdater appUpdater = new AppUpdater((Activity) context);
                        //  appUpdater.start();
                    } else {

                        Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                    }

                }
            }
            super.onPostExecute(jsonObject);
        }

        public void showForceUpdateDialog() {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AppTheme));

            alertDialogBuilder.setTitle(context.getString(R.string.youAreNotUpdatedTitle));
            alertDialogBuilder.setMessage(context.getString(R.string.youAreNotUpdatedMessage) + " " + latestVersion + context.getString(R.string.youAreNotUpdatedMessage1));
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
                    dialog.cancel();
                }
            });
            alertDialogBuilder.show();
        }
    }

    private void storeDialogStatus(boolean isChecked) {
        SharedPreferences mSharedPreferences = getSharedPreferences("CheckItem", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putBoolean("item", isChecked);
        mEditor.apply();
    }

    private boolean getDialogStatus() {
        SharedPreferences mSharedPreferences = getSharedPreferences("CheckItem", MODE_PRIVATE);
        return mSharedPreferences.getBoolean("item", false);
    }

    public void getchannelPartnerSelect(final String channelPartnerSelect) {

        jsonSchedule = new JSONObject() {
            {
                try {
                    put("Prefixtext", channelPartnerSelect);

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
                Log.i("json", "json" + jsonSchedule);
                //SEND RESPONSE
                studentResponse = serviceAccess.SendHttpPost(Config.URL_GETALLSTUDENTSBYID1, jsonSchedule);
                Log.i("resp", "loginResponse" + studentResponse);


                try {
                    JSONArray callArrayList = new JSONArray(studentResponse);
                    studentArrayList.clear();
                    // user_id="";
                    for (int i = 0; i < callArrayList.length(); i++) {
                        studentListDAO = new StudentListDAO();
                        JSONObject cityJsonObject = callArrayList.getJSONObject(i);
                        studentArrayList.add(new StudentListDAO(cityJsonObject.getString("Details"), cityJsonObject.getString("id"), cityJsonObject.getString("first_name"), cityJsonObject.getString("gender"), cityJsonObject.getString("mobile_no"), ""));

                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        studentListDAOArrayAdapter = new ArrayAdapter<StudentListDAO>(getApplicationContext(), R.layout.item, studentArrayList);
                        autoCompleteTextViewStudent.setAdapter(studentListDAOArrayAdapter);
                        if (studentArrayList.size() < 40)
                            autoCompleteTextViewStudent.setThreshold(1);
                        else autoCompleteTextViewStudent.setThreshold(2);
                        studentListDAOArrayAdapter.notifyDataSetChanged();
                        autoCompleteTextViewStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                                // String s = parent.getItemAtPosition(i).toString();
                                StudentListDAO student = (StudentListDAO) parent.getAdapter().getItem(i);
                                student_id = student.getId();
                                if (!student_id.equals("")) {

                                    add_student.setVisibility(View.VISIBLE);
                                }

                                gender = student.getLast_Name();
                                name = student.getFirst_Name();
                                mobile_no = student.getMobile_No();
                                Log.d("Id---->", student_id);
                                prefEditor.putString("student_id", student_id);
                                prefEditor.commit();
                                InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                            }
                        });
                        studentListDAOArrayAdapter.notifyDataSetChanged();

                    }
                });


            }
        });

        objectThread.start();

    }

   /* public String getDeviceId() {

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        deviceId = telephonyManager.getDeviceId();
        return deviceId;
    }*/

    public void verifyMobileDeviceId() {


        jsonObj = new JSONObject() {
            {
                try {
                    put("pDeviceID", deviceId);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Thread objectThread = new Thread(new Runnable() {
            public void run() {
                // TODO Auto-generated method stub
                WebClient serviceAccess = new WebClient();
                verifyMobileDeviceIdResponse = serviceAccess.SendHttpPost(Config.GETAVAILABLETMOBILEDEVICEID, jsonObj);
                Log.i("loginResponse", "verifyMobileDeviceIdResponse" + verifyMobileDeviceIdResponse);
                final Handler handler = new Handler(Looper.getMainLooper());
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() { // This thread runs in the UI
                            @Override
                            public void run() {
                                if (verifyMobileDeviceIdResponse.compareTo("") == 0) {

                                } else {

                                    try {
                                        JSONObject jObject = new JSONObject(verifyMobileDeviceIdResponse);
                                        status = jObject.getBoolean("status");

                                        if (status) {


                                        } else {

                                            finish();
                                            prefEditor.putString("trainer_user_id", "");
                                            prefEditor.commit();
                                            Intent intent = new Intent(StudentList1.this, TrainerVerfiyActivity.class);
                                            startActivity(intent);
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

                Log.i("json", "json" + jsonSchedule);
                //SEND RESPONSE

                //  justDialFeedbackResponse = serviceAccess.SendHttpPost(Config.URL_GETJUSTDIALFEEDBACKID, jsonSchedule);
                //  Log.i("resp", "justDialFeedbackResponse" + justDialFeedbackResponse);
                studentResponse = serviceAccess.SendHttpPost(Config.URL_GETALLBATCHTRAINERBYPREFIX, jsonSchedule);
                Log.i("resp", "loginResponse" + studentResponse);


                try {

                    // JSONObject jsonObject = new JSONObject(justDialFeedbackResponse);
                    //  just_dail_txt = jsonObject.getString("JustDialFeedback");
                    JSONArray callArrayList = new JSONArray(studentResponse);
                    batchArrayList.clear();
                    // user_id="";
                    for (int i = 0; i < callArrayList.length(); i++) {
                        batchDAO = new BatchDAO();
                        JSONObject json_data = callArrayList.getJSONObject(i);
                        batchArrayList.add(new BatchDAO(json_data.getString("id"), json_data.getString("course_id"), json_data.getString("branch_id"), json_data.getString("batch_code"), json_data.getString("start_date"), json_data.getString("timings"), json_data.getString("Notes"), json_data.getString("frequency"), json_data.getString("fees"), json_data.getString("duration"), json_data.getString("course_name"), json_data.getString("branch_name"), json_data.getString("batchtype"), json_data.getString("completion_status"), json_data.getString("batch_end_date"), json_data.getString("email_id"), json_data.getString("mobile_no"), json_data.getString("faculty_Name"), json_data.getString("attendance_marked"), json_data.getString("ref_batch")));

                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        aAdapter = new ArrayAdapter<BatchDAO>(getApplicationContext(), R.layout.item, batchArrayList);
                        autoCompleteTextViewBatch.setAdapter(aAdapter);

                        autoCompleteTextViewBatch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                                // String s = parent.getItemAtPosition(i).toString();
                                BatchDAO student = (BatchDAO) parent.getAdapter().getItem(i);
                                String date_after = formateDateFromstring("yyyy-MM-dd", "dd-MMM-yyyy", student.getStart_date());
                                //  Toast.makeText(getApplicationContext(), "Source ID: " +date_after+"   "+ LeadSource.getId() + ",  Source Name : " + LeadSource.getBatch_code(), Toast.LENGTH_SHORT).show();
                                start_date = date_after;
                                course_name = student.getCourse_name();
                                notes = student.getNotes();
                                timings = student.getTimings();
                                duration = student.getDuration();
                                fees = student.getFees();
                                frequency = student.getFrequency();
                                branch_name = student.getBranch_name();
                                batch_id = student.getId();
                                batch_code = student.getBatch_code();
                                completion_status = student.getCompletion_status();
                                Log.d("completion_status->", completion_status);
                                Log.d("trainer mail id->", student.getTrainer_mail_id());

                                Log.d("Id---->", student.getId() + "" + student.getCourse_name());
                                prefEditor.putString("batch_id", batch_id);
                                prefEditor.putString("course_name", student.getCourse_name());
                                prefEditor.putString("batch_code", student.getBatch_code());
                                prefEditor.putString("batch_fees", student.getFees());
                                prefEditor.putString("sendingmailid", student.getTrainer_mail_id());
                                prefEditor.putString("trainer_mobile_no", student.getTrainer_mobile_no());
                                prefEditor.putString("trainer_name", student.getTrainer_name());
                                //
                                prefEditor.putString("template_start_date_copy", start_date);
                                prefEditor.putString("template_course_name_copy", course_name);
                                prefEditor.putString("template_notes_copy", notes);
                                prefEditor.putString("template_timings_copy", timings);
                                prefEditor.putString("template_duration_copy", duration);
                                prefEditor.putString("template_fees_copy", fees);
                                prefEditor.putString("template_frequency_copy", frequency);
                                prefEditor.putString("template_branch_name_copy", branch_name);

                                //

                                prefEditor.commit();
                                footer.setVisibility(View.VISIBLE);

                                if (completion_status.equals("1")) {
                                    batchAttendance.setVisibility(View.GONE);
                                    batchMarkedAttendance.setVisibility(View.GONE);

                                }
                                if (completion_status.equals("0")) {
                                    if (student.getAttendance_marked().equals("1")) {
                                        batchAttendance.setVisibility(View.GONE);
                                        batchMarkedAttendance.setVisibility(View.VISIBLE);

                                    }
                                    if (student.getAttendance_marked().equals("0")) {
                                        batchMarkedAttendance.setVisibility(View.GONE);
                                        batchAttendance.setVisibility(View.VISIBLE);

                                    }

                                }


                                InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                                if (student.getRef_batch().equals("null") || student.getRef_batch().equals("")) {
                                   // Toast.makeText(getApplicationContext(), student.getRef_batch(), Toast.LENGTH_SHORT).show();
                                    UpdateRefBatchEntryView updateRefBatchEntryView = new UpdateRefBatchEntryView();
                                    updateRefBatchEntryView.show(getSupportFragmentManager(), "updateRefBatchEntryView");

                                }

                                new getStudentList().execute();
                            }
                        });
                        aAdapter.notifyDataSetChanged();

                    }
                });


            }
        });

        objectThread.start();

    }

    //
    private class getStudentList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            //  mProgressDialog = new ProgressDialog(StudentList.this);
            // Set progressdialog title
            //  mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            //    mProgressDialog.setMessage("Loading...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            //  mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {
                        //put("course_id", course_id);
                        put("Status", Status_id);
                        put("batch_id", batch_id);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();

            //  String baseURL = "http://192.168.1.13:8088/srujanlms_new/api/Leadraw/showleadraw";
            Log.i("json", "json" + jsonLeadObj);
            studentListResponse = serviceAccess.SendHttpPost(Config.URL_DISPLAY_STUDENTS, jsonLeadObj);
            Log.i("resp", "batchesListResponse" + studentListResponse);
            if (studentListResponse.compareTo("") != 0) {
                if (isJSONValid(studentListResponse)) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {


                                JsonHelper jsonHelper = new JsonHelper();
                                data = jsonHelper.parseStudentList(studentListResponse);
                                jsonArray = new JSONArray(studentListResponse);

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
                            Toast.makeText(getApplicationContext(), "Please check your webservice", Toast.LENGTH_LONG).show();
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
            if (data.size() > 0) {
                if (Status_id == 1) {
                    normal.setText("Existing " + data.size());
                } else {
                    corporate.setText("Discontinued " + data.size());
                }
                studentListAdpter = new StudentListAdpter(StudentList1.this, data);
                mstudentList.setAdapter(studentListAdpter);
                mstudentList.setLayoutManager(new LinearLayoutManager(StudentList1.this));
                studentListAdpter.notifyDataSetChanged();
                // mstudentList.setHasFixedSize(true);
                // setUpItemTouchHelper();
                //setUpAnimationDecoratorHelper();
                //  mProgressDialog.dismiss();
            } else {
                // Close the progressdialog
                //   mProgressDialog.dismiss();
            }
        }
    }

//

    private class submitData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(StudentList1.this);
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
                        put("BatchID", batch_id);
                        put("UserID", student_id);
                        put("FeesApplicable", FeesApplicable);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();


            Log.i("json", "json" + jsonLeadObj);
            addStudentRespone = serviceAccess.SendHttpPost(Config.URL_ADD_STUDENT_INBATCH, jsonLeadObj);
            Log.i("resp", "addStudentRespone" + addStudentRespone);


            if (addStudentRespone.compareTo("") != 0) {
                if (isJSONValid(addStudentRespone)) {


                    try {

                        JSONObject jsonObject = new JSONObject(addStudentRespone);
                        status = jsonObject.getBoolean("status");
                        msg = jsonObject.getString("message");
                        if (status) {
                            studentFeedBackCode = jsonObject.getString("feedback_student_code");
                            String msg = "Hi " + name + System.getProperty("line.separator") + System.getProperty("line.separator")
                                    + "Congratulations on Joining Course for " + preferences.getString("course_name", "") + "!" + System.getProperty("line.separator") + System.getProperty("line.separator")
                                    + "Please save the below link where you can give feedback regarding your ongoing batch no " + batch_id + "." + System.getProperty("line.separator") + System.getProperty("line.separator")
                                    + "https://afckstechnologies.in/fb_text/" + studentFeedBackCode;
                            String result = sendSms1(mobile_no, msg);
                            Log.d("sent sms---->", result);

                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                } else {


                    Toast.makeText(getApplicationContext(), "Please check your webservice", Toast.LENGTH_LONG).show();


                }
            } else {

                Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_LONG).show();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (status) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                autoCompleteTextViewStudent.setText("");
                add_student.setVisibility(View.GONE);
                // Close the progressdialog
                mProgressDialog.dismiss();
                new getStudentList().execute();
            } else {
                // Close the progressdialog
                mProgressDialog.dismiss();

            }
        }
    }
//


    private class getVerifyCode extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(StudentList1.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            jsonObj = new JSONObject() {
                {
                    try {
                        put("user_id", preferences.getString("trainer_user_id", ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            WebClient serviceAccess = new WebClient();
            Log.i("json", "json" + jsonObj);
            pendingTaskResponse = serviceAccess.SendHttpPost(Config.URL_GETVERIFYCODEFORWEB, jsonObj);
            Log.i("resp", "pendingTaskResponse" + pendingTaskResponse);


            if (pendingTaskResponse.compareTo("") != 0) {
                if (isJSONValid(pendingTaskResponse)) {


                    try {

                        jsonObject = new JSONObject(pendingTaskResponse);
                        msg = jsonObject.getString("message");
                        status = jsonObject.getBoolean("status");
                        verifycode = jsonObject.getString("id");

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Please check your webservice", Toast.LENGTH_LONG).show();
                }
            } else {

                Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_LONG).show();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {


            if (status) {


                AlertDialog.Builder builder = new AlertDialog.Builder(StudentList1.this);
                builder.setMessage("Verify Code is " + verifycode)
                        .setCancelable(false)
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                            }
                        });


                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually

                alert.show();
                mProgressDialog.dismiss();
                new updateBatchCodeData().execute();
            } else {

                Toast.makeText(getApplicationContext(), "Please enter your mobile no in website to login.", Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
            // Close the progressdialog
            mProgressDialog.dismiss();

        }
    }

    private class updateBatchCodeData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(StudentList1.this);
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
                        put("current_batch", batch_id);
                        put("id", preferences.getString("trainer_user_id", ""));


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();


            Log.i("json", "json" + jsonLeadObj);
            addStudentRespone = serviceAccess.SendHttpPost(Config.URL_UPDATEBTACHCODE, jsonLeadObj);
            Log.i("resp", "addStudentRespone" + addStudentRespone);


            if (addStudentRespone.compareTo("") != 0) {
                if (isJSONValid(addStudentRespone)) {


                    try {

                        JSONObject jsonObject = new JSONObject(addStudentRespone);
                        status = jsonObject.getBoolean("status");
                        msg = jsonObject.getString("message");
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                } else {


                    Toast.makeText(getApplicationContext(), "Please check your webservice", Toast.LENGTH_LONG).show();


                }
            } else {

                Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_LONG).show();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (status) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                // Close the progressdialog
                mProgressDialog.dismiss();
                //   getBatchSelect(batch_id);
            } else {
                // Close the progressdialog
                mProgressDialog.dismiss();

            }
        }
    }
    //end ..........

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

    //taking studenyts attendance


    class SendingMailsTask extends AsyncTask<String, Void, String> {

        String sResponse = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = ProgressDialog.show(StudentList1.this, "Marking Attendance " + (count++) + " / " + studentMobileNoArrayList.size(), "Please wait...", true);
            dialog.show();
        }


        @Override
        protected String doInBackground(final String... params) {
            try {
                jsonLeadObj = new JSONObject() {
                    {
                        try {
                            if (mobileNoArrayList.get(Integer.parseInt(params[0])).equals("true")) {
                                pa = 1;
                            } else {
                                pa = 0;
                            }
                            prefEditor.remove("cscb");
                            prefEditor.remove("allcheck");
                            prefEditor.remove(studentMobileNoArrayList.get(Integer.parseInt(params[0])));
                            prefEditor.commit();
                            put("batch_id", nameArrayList.get(Integer.parseInt(params[0])));
                            put("user_id", studentMobileNoArrayList.get(Integer.parseInt(params[0])));
                            put("batch_date", preferences.getString("check_mark_attendance_date", ""));
                            put("present", pa);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("json exception", "json exception" + e);
                        }
                    }
                };
                WebClient serviceAccess = new WebClient();
                Log.i("json", "json" + jsonLeadObj);
                sResponse = serviceAccess.SendHttpPost(Config.URL_TAKEATTENDENCEBYBTACH, jsonLeadObj);
                Log.i("resp", "attendanceListResponse" + attendanceListResponse);

            } catch (Exception ex) {
                sResponse = "null";
            }


            return sResponse;
        }

        @Override
        protected void onPostExecute(String sResponse) {
            try {
                if (dialog.isShowing())
                    dialog.dismiss();

                if (sResponse != null) {
                    //  Toast.makeText(getApplicationContext(), sResponse + " Photo uploaded successfully", Toast.LENGTH_SHORT).show();
                    m_count++;
                    if (m_count < studentMobileNoArrayList.size()) {


                        new SendingMailsTask().execute(m_count + "", studentMobileNoArrayList.get(m_count));

                    } else {
                        dialog.dismiss();
                        chkAll.setChecked(false);
                        studentMobileNoArrayList.clear();
                        nameArrayList.clear();
                        studentMobileNoArrayList.clear();
                        m_count = 0;
                        count = 1;
                        new getStudentList().execute();
                    }

                } else {
                    finish();
                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }

        }
    }

    //
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
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

    private class getVerifyCodeWithUpdate extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(StudentList1.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            jsonObj = new JSONObject() {
                {
                    try {
                        put("user_id", preferences.getString("trainer_user_id", ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            WebClient serviceAccess = new WebClient();
            Log.i("json", "json" + jsonObj);
            pendingTaskResponse = serviceAccess.SendHttpPost(Config.URL_GETVERIFYCODEFORWEB, jsonObj);
            Log.i("resp", "pendingTaskResponse" + pendingTaskResponse);


            if (pendingTaskResponse.compareTo("") != 0) {
                if (isJSONValid(pendingTaskResponse)) {


                    try {

                        jsonObject = new JSONObject(pendingTaskResponse);
                        msg = jsonObject.getString("message");
                        status = jsonObject.getBoolean("status");
                        verifycode = jsonObject.getString("id");

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Please check your webservice", Toast.LENGTH_LONG).show();
                }
            } else {

                Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_LONG).show();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {


            if (status) {


                AlertDialog.Builder builder = new AlertDialog.Builder(StudentList1.this);
                builder.setMessage("Verify Code is " + verifycode)
                        .setCancelable(false)
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                            }
                        });


                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually

                alert.show();
                mProgressDialog.dismiss();

            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(StudentList1.this);
                builder.setMessage("To get verify code please enter your mobile number on your portal.")
                        .setCancelable(false)
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                            }
                        });


                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually

                alert.show();
                mProgressDialog.dismiss();
            }
            // Close the progressdialog
            mProgressDialog.dismiss();

        }
    }

}
