package in.afckstechnologies.afcksfees.Fragments;


import android.accounts.Account;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.SyncStatusObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;

import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import android.widget.LinearLayout;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.afckstechnologies.afcksfees.MailAPI.GMailSender1;
import in.afckstechnologies.afcksfees.R;
import in.afckstechnologies.afcksfees.activity.Activity_User_Profile;
import in.afckstechnologies.afcksfees.activity.BankingDetailsActivity;
import in.afckstechnologies.afcksfees.activity.OngoingBatchesActivity;
import in.afckstechnologies.afcksfees.activity.SendingStudentNameForCertificateActivity;
import in.afckstechnologies.afcksfees.activity.TemplateDisplayActivity;
import in.afckstechnologies.afcksfees.activity.TemplateDisplayActivity1;
import in.afckstechnologies.afcksfees.activity.TrainerVerfiyActivity;
import in.afckstechnologies.afcksfees.activity.UpcomingClassDetailsActivity;
import in.afckstechnologies.afcksfees.activity.WriteSendSmsActivity;
import in.afckstechnologies.afcksfees.adapter.OnGoingBatchesListAdpter;
import in.afckstechnologies.afcksfees.adapter.StudentListAdpter;
import in.afckstechnologies.afcksfees.common.accounts.GenericAccountService;
import in.afckstechnologies.afcksfees.jsonparser.JsonHelper;
import in.afckstechnologies.afcksfees.model.BatchDAO;
import in.afckstechnologies.afcksfees.model.BatchesForStudentsDAO;
import in.afckstechnologies.afcksfees.model.StudentListDAO;
import in.afckstechnologies.afcksfees.model.StudentsDAO;
import in.afckstechnologies.afcksfees.provider.StudentContract;
import in.afckstechnologies.afcksfees.provider.StudentProvider;
import in.afckstechnologies.afcksfees.utils.AppStatus;
import in.afckstechnologies.afcksfees.utils.Config;
import in.afckstechnologies.afcksfees.utils.Constant;
import in.afckstechnologies.afcksfees.utils.FeesListener;
import in.afckstechnologies.afcksfees.utils.SmsListener;
import in.afckstechnologies.afcksfees.utils.SyncUtils;
import in.afckstechnologies.afcksfees.utils.Utils;
import in.afckstechnologies.afcksfees.utils.VersionChecker;
import in.afckstechnologies.afcksfees.utils.WebClient;
import in.afckstechnologies.afcksfees.view.ActualBatchTimingsView;
import in.afckstechnologies.afcksfees.view.BatchModifyView;
import in.afckstechnologies.afcksfees.view.CertificateDisplayDetailsView;
import in.afckstechnologies.afcksfees.view.CommentAddView;
import in.afckstechnologies.afcksfees.view.FeesDetailsView;
import in.afckstechnologies.afcksfees.view.MultipleCommentAddView;
import in.afckstechnologies.afcksfees.view.RegistrationDemoBatchView;
import in.afckstechnologies.afcksfees.view.RegistrationView;
import in.afckstechnologies.afcksfees.view.StudentDiscontinueEntryView;
import in.afckstechnologies.afcksfees.view.StudentFeesEntryView;
import in.afckstechnologies.afcksfees.view.StudentTransferFeesEntryView;
import in.afckstechnologies.afcksfees.view.UpdateRefBatchEntryView;


public class StudentList extends Fragment {
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    AutoCompleteTextView autoCompleteTextViewBatch;
    String studentResponse = "";
    public List<BatchesForStudentsDAO> batchArrayList;
    public ArrayAdapter<BatchesForStudentsDAO> aAdapter;
    public ArrayAdapter<StudentListDAO> studentListDAOArrayAdapter;
    public BatchDAO batchDAO;
    String batch_id = "";
    private JSONObject jsonSchedule, jsonObj, syncJsonObject, jsonCertiObj;
    private JSONObject jsonLeadObj, jsonObject, jsonObj1, jsonObjectSync,jsonObj2;
    ProgressDialog mProgressDialog;
    String studentListResponse = "";
    JSONArray jsonArray, jsonArraySync;
    List<StudentsDAO> data;
    StudentListAdpter studentListAdpter;
    private RecyclerView mstudentList;
    private FloatingActionButton fab,fab1;
    String newTextBatch, newTextStudent;
    AutoCompleteTextView autoCompleteTextViewStudent;
    ImageView add_student, clear, clear_batch, edit_batch, info_batch, dueDateChange, batchMarkedAttendance, getcode, sendRequestForCerti, getEmailIds;
    public List<StudentListDAO> studentArrayList;
    String student_id = "", pendingTaskResponse = "";
    public StudentListDAO studentListDAO;
    String addStudentRespone = "", attendanceListResponse = "", justDialFeedbackResponse = "", just_dail_txt = "", syncDataesponse = "", selectedDate = "", check_list = "0";
    boolean status, statusv,statusv1;
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
    String fees = "", batch_code = "", getFeeStatusResponse = "";
    RelativeLayout footer;
    String flag = "";
    private static final String username = "info@afcks.com";
    private static String password = "at!@#123";
    String subject = "";
    String mailids = "";
    GMailSender1 sender;
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
    RadioButton normal, corporate, demo;
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
    ImageView settingimg, walinkh, walinks;
    ImageView btnClosePopup;
    View v;
    LinearLayout studentSHS;
    boolean stcorporate = false;
    String attendance_mark = "";
    String certificateResponse = "", certi_status = "", batch_type = "", wa_link = "", m_link = "";
    //database helper object
    private StudentProvider.AFCKSDatabase db;
    /**
     * Handle to a SyncObserver. The ProgressBar element is visible until the SyncObserver reports
     * that the sync is complete.
     *
     * <p>This allows us to delete our SyncObserver once the application is no longer in the
     * foreground.
     */
    private Object mSyncObserverHandle;
    /**
     * Options menu used to populate ActionBar.
     */
    private Menu mOptionsMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.floating_main_layout, container, false);
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        toolbar.getOverflowIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        // Create account, if needed
        SyncUtils.CreateSyncAccount(activity);
        db = new StudentProvider.AFCKSDatabase(getActivity());
        preferences = getActivity().getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        prefEditor.putBoolean("0", true);
        prefEditor.commit();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.
                Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // Add your mail Id and Password
        sender = new GMailSender1(username, password);
        prefEditor.putString("mail_a_flag", "enter_student");
        prefEditor.commit();
        clear = (ImageView) v.findViewById(R.id.clear);
        clear_batch = (ImageView) v.findViewById(R.id.clear_batch);
        add_student = (ImageView) v.findViewById(R.id.add_student);
        edit_batch = (ImageView) v.findViewById(R.id.edit_batch);
        info_batch = (ImageView) v.findViewById(R.id.info_batch);
        sendRequestForCerti = (ImageView) v.findViewById(R.id.sendRequestForCerti);
        mstudentList = (RecyclerView) v.findViewById(R.id.studentsList);
        batchAttendance = (ImageView) v.findViewById(R.id.batchAttendance);
        dueDateChange = (ImageView) v.findViewById(R.id.dueDateChange);
        settingimg = (ImageView) v.findViewById(R.id.settingimg);
        getcode = (ImageView) v.findViewById(R.id.getcode);
        walinkh = (ImageView) v.findViewById(R.id.walinkh);
        walinks = (ImageView) v.findViewById(R.id.walinks);
        getEmailIds = (ImageView) v.findViewById(R.id.getEmailIds);
        studentSHS = (LinearLayout) v.findViewById(R.id.studentSHS);
        batchMarkedAttendance = (ImageView) v.findViewById(R.id.batchMarkedAttendance);
        normal = (RadioButton) v.findViewById(R.id.normal);
        corporate = (RadioButton) v.findViewById(R.id.corporate);
        demo = (RadioButton) v.findViewById(R.id.demo);
        receipt = (TextView) v.findViewById(R.id.receipt);
        notestxt = (TextView) v.findViewById(R.id.notes);
        studentArrayList = new ArrayList<StudentListDAO>();
        batchArrayList = new ArrayList<BatchesForStudentsDAO>();
        data = new ArrayList<>();
        autoCompleteTextViewStudent = (AutoCompleteTextView) v.findViewById(R.id.SearchStudent);
        footer = (RelativeLayout) v.findViewById(R.id.footer);
        autoCompleteTextViewBatch = (AutoCompleteTextView) v.findViewById(R.id.SearchBatch);
        Intent intent1 = getActivity().getIntent();
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
            Intent intent = getActivity().getIntent();
            autoCompleteTextViewBatch.setText(intent.getStringExtra("batch_id"));
            batch_id = intent.getStringExtra("batch_id");
            new getStudentList().execute();
        }


        if (preferences.getString("prebatchcall", "").equals("2")) {
            prefEditor.putString("prebatchcall", "0");
            prefEditor.commit();
            Intent intent = getActivity().getIntent();
            autoCompleteTextViewBatch.setText(intent.getStringExtra("batch_id"));
            batch_id = intent.getStringExtra("batch_id");
            Status_id = 0;
            normal.setChecked(false);
            corporate.setChecked(true);
            new getStudentList().execute();
        }

       /* if (preferences.getString("trainer_user_id", "").equals("RS")) {
            receipt.setVisibility(View.VISIBLE);
        }
        if (preferences.getString("trainer_user_id", "").equals("AK")) {
            receipt.setVisibility(View.VISIBLE);
        }*/
        radioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub

                // Method 1 For Getting Index of RadioButton
                pos = radioGroup.indexOfChild(v.findViewById(checkedId));
                switch (pos) {
                    case 1:
                        Status_id = 0;
                        new getStudentList().execute();
                        // getFeesOptionStatus();
                        //   Toast.makeText(getActivity(), "1",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:

                        Status_id = 2;
                        // Intent intent = new Intent(StudentList.this, BankingDetailsActivity.class);
                        // startActivity(intent);
                        // Toast.makeText(getActivity(), "2", Toast.LENGTH_SHORT).show();

                        new getStudentList().execute();

                        break;

                    default:
                        //The default selection is RadioButton 1
                        Status_id = 1;
                        //  getFeesOptionStatus();
                        new getStudentList().execute();
                        // Toast.makeText(getActivity(), "3",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BankingDetailsActivity.class);
                startActivity(intent);
            }
        });
        notestxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!batch_id.equals("")) {
                    Intent intent = new Intent(getActivity(), UpcomingClassDetailsActivity.class);
                    intent.putExtra("batch_code", batch_id);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Please enter Batch code ", Toast.LENGTH_LONG).show();
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
                    walinks.setVisibility(View.VISIBLE);
                    walinkh.setVisibility(View.GONE);
                    clear_batch.setVisibility(View.VISIBLE);
                    edit_batch.setVisibility(View.VISIBLE);
                    newTextBatch = s.toString();
                    getBatchSelect(autoCompleteTextViewBatch.getText().toString());

                } else {
                    clear_batch.setVisibility(View.GONE);
                    edit_batch.setVisibility(View.GONE);
                    info_batch.setVisibility(View.VISIBLE);
                    getcode.setVisibility(View.VISIBLE);
                    walinks.setVisibility(View.GONE);
                    walinkh.setVisibility(View.VISIBLE);
                    newTextBatch = "";
                }
            }
        });
        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                prefEditor.putString("student_name", newTextStudent);
                prefEditor.commit();
                RegistrationView registrationView = new RegistrationView();
                registrationView.show(getActivity().getSupportFragmentManager(), "registrationView");

            }
        });
        fab1 = (FloatingActionButton) v.findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                prefEditor.putString("dbatch_id", batch_id);
                prefEditor.commit();
                RegistrationDemoBatchView registrationDemoBatchView = new RegistrationDemoBatchView();
                registrationDemoBatchView.show(getActivity().getSupportFragmentManager(), "registrationView");

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
                completion_status = "";
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
                studentSHS.setVisibility(View.GONE);
                batch_id = "";
                completion_status = "";
                normal.setText("E");
                corporate.setText("L");
                demo.setText("D");
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
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                        View mView = getLayoutInflater().inflate(R.layout.dialog_app_updates, null);
                        CheckBox mCheckBox = mView.findViewById(R.id.checkBox);
                        CheckBox mCheckBoxCor = mView.findViewById(R.id.checkBoxCorporate);
                        mCheckBox.setChecked(getDialogStatus());
                        mBuilder.setTitle("Add Student");
                        mBuilder.setMessage("If below is checked Student will get fees reminder ");
                        mBuilder.setView(mView);
                        mBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if (!student_id.equals("")) {

                                    if (getDialogStatus()) {
                                        if (stcorporate) {
                                            FeesApplicable = "2";
                                        } else {
                                            FeesApplicable = "1";
                                        }
                                    } else {
                                        FeesApplicable = "0";
                                    }
                                    new submitData().execute();
                                    dialogInterface.cancel();
                                } else {
                                    Toast.makeText(getActivity(), "Please select Student", Toast.LENGTH_LONG).show();
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
                        mCheckBoxCor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                if (compoundButton.isChecked()) {
                                    stcorporate = true;
                                } else {
                                    stcorporate = false;
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
                        Toast.makeText(getActivity(), "Please Update Gender...", Toast.LENGTH_LONG).show();
                        prefEditor.putString("user_id", student_id);
                        prefEditor.putString("edit_u_p_flag", "fu");
                        prefEditor.commit();
                        Intent intent = new Intent(getActivity(), Activity_User_Profile.class);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(getActivity(), "Please enter Batch code ", Toast.LENGTH_LONG).show();
                }
            }
        });


        /** Getting reference to checkbox available in the main.xml layout */
        chkAll = (CheckBox) v.findViewById(R.id.chkAllSelected);
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

        sendData = (Button) v.findViewById(R.id.sendData);
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
                    Intent intent = new Intent(getActivity(), WriteSendSmsActivity.class);
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
                    Toast.makeText(getActivity(), "Please select student from list", Toast.LENGTH_LONG).show();
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
                        actualBatchTimingsView.show(getActivity().getSupportFragmentManager(), "actualBatchTimingsView");

                    } else {
                        Toast.makeText(getActivity(), "Please select batch ", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getActivity(), "Please select student from list", Toast.LENGTH_LONG).show();
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Do you want to update Today Attendance ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        try {
                                            try {
                                                check_list = "0";
                                                new SendingMailsTask().execute(m_count + "", studentMobileNoArrayList.get(m_count));
                                            } catch (Exception ex) {
                                                Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG).show();
                                            }

                                        } catch (Exception ex) {
                                            Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getActivity(), "Please select batch ", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getActivity(), "Please select student from list", Toast.LENGTH_LONG).show();
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

                        DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setMessage("Do you want to Update Due Date " + selectedDate + " ?")
                                                .setCancelable(false)
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {

                                                        accessAlertDilog();


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
                        Toast.makeText(getActivity(), "Please select batch ", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(getActivity(), "Please select student from list", Toast.LENGTH_LONG).show();
                }

            }
        });

        sendRequestForCerti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                String data1 = "";
                String data2 = "";
                String data3 = "";
                String data4 = "";
                String data5 = "";
                studentNameArrayList = new ArrayList<>();
                studentUserIdArrayList = new ArrayList<>();
                mobileNoArrayList = new ArrayList<>();
                studentMailIdArrayList = new ArrayList<>();
                nameArrayList = new ArrayList<>();
                List<StudentsDAO> stList = ((StudentListAdpter) studentListAdpter).getSservicelist();
                for (int i = 0; i < stList.size(); i++) {
                    StudentsDAO serviceListDAO = stList.get(i);
                    if (serviceListDAO.isSelected()) {
                        data1 = serviceListDAO.getStudents_Name();
                        data2 = serviceListDAO.getUser_id();
                        data3 = serviceListDAO.getMobile_no();
                        data4 = serviceListDAO.getEmail_id();
                        data5 = serviceListDAO.getStudent_certificate_name();
                        studentNameArrayList.add(data1);
                        studentUserIdArrayList.add(data2);
                        mobileNoArrayList.add(data3);
                        studentMailIdArrayList.add(data4);
                        nameArrayList.add(data5);

                    }


                }
                temp_size = studentNameArrayList.size();
                if (temp_size > 0) {
                    //Toast.makeText(getActivity(),"printing students",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getActivity(), SendingStudentNameForCertificateActivity.class);
                    Bundle args = new Bundle();
                    args.putSerializable("ARRAYLIST", (Serializable) mobileNoArrayList);
                    args.putSerializable("ARRAYLIST1", (Serializable) studentNameArrayList);
                    args.putSerializable("ARRAYLIST2", (Serializable) studentMailIdArrayList);
                    args.putSerializable("ARRAYLIST3", (Serializable) studentUserIdArrayList);
                    args.putSerializable("ARRAYLIST4", (Serializable) nameArrayList);
                    intent.putExtra("BUNDLE", args);
                    intent.putExtra("course_name", course_name);
                    intent.putExtra("batch_id", batch_id);
                    intent.putExtra("batch_type", batch_type);
                    startActivity(intent);

                } else {
                    if (certi_status.equals("1")) {
                        // Toast.makeText(getActivity(),"Display students",Toast.LENGTH_SHORT).show();
                        CertificateDisplayDetailsView certificateDisplayDetailsView = new CertificateDisplayDetailsView();
                        certificateDisplayDetailsView.show(getActivity().getSupportFragmentManager(), "certificateDisplayDetailsView");
                    } else {
                        Toast.makeText(getActivity(), "Please select students", Toast.LENGTH_SHORT).show();
                    }
                }


            }


        });

        getEmailIds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data1 = "";
                studentMailIdArrayList = new ArrayList<>();
                List<StudentsDAO> stList = ((StudentListAdpter) studentListAdpter).getSservicelist();
                for (int i = 0; i < stList.size(); i++) {
                    StudentsDAO serviceListDAO = stList.get(i);
                    if (serviceListDAO.isSelected()) {
                        data1 = serviceListDAO.getEmail_id();
                        studentMailIdArrayList.add(data1);
                    }
                }
                if (studentMailIdArrayList.size() > 0) {
                    if (!batch_id.equals("")) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Do you want to Get Students Mail Ids ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        mailids = Utils.convertArrayListToStringWithComma(studentMailIdArrayList);
                                        //Toast.makeText(getActivity(),sendingmail,Toast.LENGTH_SHORT).show();
                                        subject = "Total students " + studentMailIdArrayList.size() + " for " + batch_id + ".";
                                        new MyAsyncClass().execute();


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
                        alert.setTitle("Getting Students Mail Ids");
                        alert.show();

                    } else {
                        Toast.makeText(getActivity(), "Please select batch ", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(getActivity(), "Please select student from list", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getActivity(), "Please select batch ", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(), "Please select batch ", Toast.LENGTH_SHORT).show();
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
                    batchModifyView.show(getActivity().getSupportFragmentManager(), "batchModifyView");
                } else {
                    Toast.makeText(getActivity(), "Please select batch ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        info_batch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //StatsBatchStudentsView statsBatchStudentsView = new StatsBatchStudentsView();
                //statsBatchStudentsView.show(getSupportFragmentManager(), "statsBatchStudentsView");
                Intent intent = new Intent(getActivity(), OngoingBatchesActivity.class);
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
                normal.setText("E");
                corporate.setText("L");
                demo.setText("D");
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

                    if (messageText.equals("1")) {
                        batchAttendance.setVisibility(View.GONE);
                        batchMarkedAttendance.setVisibility(View.GONE);
                        sendRequestForCerti.setVisibility(View.VISIBLE);
                    } else {
                        batchAttendance.setVisibility(View.GONE);
                        batchMarkedAttendance.setVisibility(View.VISIBLE);
                    }
                    new SendingMailsTask().execute(m_count + "", studentMobileNoArrayList.get(m_count));
                } catch (Exception ex) {
                    Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

        settingimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initiatePopupWindow();
            }
        });

        getcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppStatus.getInstance(getActivity()).isOnline()) {
                    new getVerifyCodeWithUpdate().execute();
                   /* if (!wa_link.equals("")) {
                        Intent intentWhatsapp = new Intent(Intent.ACTION_VIEW);
                        intentWhatsapp.setData(Uri.parse(wa_link));
                        intentWhatsapp.setPackage("com.whatsapp");
                        startActivity(intentWhatsapp);
                    } else {
                        Toast.makeText(getActivity(), "Whatsapp Group link not available", Toast.LENGTH_SHORT).show();

                    }*/

                } else {

                    Toast.makeText(getActivity(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });

        walinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppStatus.getInstance(getActivity()).isOnline()) {

                    if (!wa_link.equals("")) {
                        try {
                            Intent intentWhatsapp = new Intent(Intent.ACTION_VIEW);
                            intentWhatsapp.setData(Uri.parse(wa_link));
                            if (preferences.getString("trainer_user_id", "").equals("AT")) {
                                intentWhatsapp.setPackage("com.whatsapp.w4b");
                            } else if (preferences.getString("trainer_user_id", "").equals("AK")) {
                                intentWhatsapp.setPackage("com.whatsapp.w4b");
                            } else {
                                intentWhatsapp.setPackage("com.whatsapp");
                            }

                            startActivity(intentWhatsapp);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Whatsapp Group link not available", Toast.LENGTH_SHORT).show();

                    }

                } else {

                    Toast.makeText(getActivity(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (AppStatus.getInstance(getActivity()).isOnline()) {
            //  deviceId = getDeviceId();
            deviceId = getIMEINumber(getActivity());
            verifyMobileDeviceId();
        }
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (AppStatus.getInstance(getActivity()).isOnline()) {
            getUserRoles();
            new smspassAvailable().execute();
            getFeesOptionStatus();

        }


    }

    @Override
    public void onResume() {
        super.onResume();
        mSyncStatusObserver.onStatusChanged(0);

        // Watch for sync state changes
        final int mask = ContentResolver.SYNC_OBSERVER_TYPE_PENDING |
                ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE;
        mSyncObserverHandle = ContentResolver.addStatusChangeListener(mask, mSyncStatusObserver);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSyncObserverHandle != null) {
            ContentResolver.removeStatusChangeListener(mSyncObserverHandle);
            mSyncObserverHandle = null;
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

    /**
     * Create the ActionBar.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        mOptionsMenu = menu;
        // mOptionsMenu.add("asas");
        inflater.inflate(R.menu.main, menu);
    }

    /**
     * Respond to user gestures on the ActionBar.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Refresh")) {
            if (AppStatus.getInstance(getActivity()).isOnline()) {
                Utils.showLoadingDialog(getActivity());
                SyncUtils.TriggerRefresh();

            } else {

                Toast.makeText(getActivity(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
            }
        } else if (item.getTitle().equals("Get Students Mail Ids")) {
            if (!batch_id.equals("")) {

                if (AppStatus.getInstance(getActivity()).isOnline()) {
                    if (preferences.getString("trainer_user_id", "").equals("RS")) {
                        sendingEmail();
                    } else {
                        getEmailsCount();
                    }
                } else {

                    Toast.makeText(getActivity(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }


            } else {
                Toast.makeText(getActivity(), "Please select batch ", Toast.LENGTH_SHORT).show();
            }
        }
       else if (item.getTitle().equals("Template")) {
            prefEditor.putString("temp_type_sms", "0");
            prefEditor.commit();
           Intent intent=new Intent(getActivity(), TemplateDisplayActivity1.class);
           startActivity(intent);
        }
       /* switch (item.getItemId()) {
            // If the user clicks the "Refresh" button.
            case R.id.menu_refresh:
                if (AppStatus.getInstance(getActivity()).isOnline()) {
                    Utils.showLoadingDialog(getActivity());
                    SyncUtils.TriggerRefresh();

                } else {

                    Toast.makeText(getActivity(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }
            case R.id.menu_getMailids:
                if (!batch_id.equals("")) {

                    if (AppStatus.getInstance(getActivity()).isOnline()) {
                        if (preferences.getString("trainer_user_id", "").equals("RS")) {
                            sendingEmail();
                        } else {
                            getEmailsCount();
                        }
                    } else {

                        Toast.makeText(getActivity(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(getActivity(), "Please select batch ", Toast.LENGTH_SHORT).show();
                }
                return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    /**
     * Set the state of the Refresh button. If a sync is active, turn on the ProgressBar widget.
     * Otherwise, turn it off.
     *
     * @param refreshing True if an active sync is occuring, false otherwise
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setRefreshActionButtonState(boolean refreshing) {
        if (mOptionsMenu == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return;
        }

        final MenuItem refreshItem = mOptionsMenu.findItem(R.id.menu_refresh);
        if (refreshItem != null) {
            if (refreshing) {
                refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);

            } else {
                refreshItem.setActionView(null);
                Utils.dismissLoadingDialog();
            }
        }
    }

    /**
     * Crfate a new anonymous SyncStatusObserver. It's attached to the app's ContentResolver in
     * onResume(), and removed in onPause(). If status changes, it sets the state of the Refresh
     * button. If a sync is active or pending, the Refresh button is replaced by an indeterminate
     * ProgressBar; otherwise, the button itself is displayed.
     */
    private SyncStatusObserver mSyncStatusObserver = new SyncStatusObserver() {
        /** Callback invoked with the sync adapter status changes. */
        @Override
        public void onStatusChanged(int which) {
            getActivity().runOnUiThread(new Runnable() {
                /**
                 * The SyncAdapter runs on a background thread. To update the UI, onStatusChanged()
                 * runs on the UI thread.
                 */
                @Override
                public void run() {
                    // Create a handle to the account that was created by
                    // SyncService.CreateSyncAccount(). This will be used to query the system to
                    // see how the sync status has changed.
                    Account account = GenericAccountService.GetAccount(SyncUtils.ACCOUNT_TYPE);
                    if (account == null) {
                        // GetAccount() returned an invalid value. This shouldn't happen, but
                        // we'll set the status to "not refreshing".
                        setRefreshActionButtonState(false);
                        return;
                    }

                    // Test the ContentResolver to see if the sync adapter is active or pending.
                    // Set the state of the refresh button accordingly.
                    boolean syncActive = ContentResolver.isSyncActive(
                            account, StudentContract.CONTENT_AUTHORITY);
                    boolean syncPending = ContentResolver.isSyncPending(
                            account, StudentContract.CONTENT_AUTHORITY);
                    setRefreshActionButtonState(syncActive || syncPending);
                }
            });
        }
    };

    //
    public void getBatchSelect(final String channelPartnerSelect) {
       /* String value = "";
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
*/

        Thread objectThread = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                WebClient serviceAccess = new WebClient();
/*

                Log.i("json", "json" + jsonSchedule);
                //SEND RESPONSE

                //  justDialFeedbackResponse = serviceAccess.SendHttpPost(Config.URL_GETJUSTDIALFEEDBACKID, jsonSchedule);
                //  Log.i("resp", "justDialFeedbackResponse" + justDialFeedbackResponse);
                studentResponse = serviceAccess.SendHttpPost(Config.URL_GETALLBATCHTRAINERBYPREFIX, jsonSchedule);
                Log.i("resp", "loginResponse" + studentResponse);
*/
                Cursor c = db.getSerachBatches(channelPartnerSelect, preferences.getString("trainer_user_id", ""));
                assert c != null;
                Log.d("Count", "" + c.getCount());
                batchArrayList.clear();
                if (c.moveToFirst()) {
                    do {
                        status = true;
                        batchArrayList.add(new BatchesForStudentsDAO(c.getString(c.getColumnIndex("id")), c.getString(c.getColumnIndex("course_id")), c.getString(c.getColumnIndex("branch_id")), c.getString(c.getColumnIndex("batch_code")), c.getString(c.getColumnIndex("start_date")), c.getString(c.getColumnIndex("timings")), c.getString(c.getColumnIndex("Notes")), c.getString(c.getColumnIndex("frequency")), c.getString(c.getColumnIndex("fees")), c.getString(c.getColumnIndex("duration")), c.getString(c.getColumnIndex("course_name")), c.getString(c.getColumnIndex("branch_name")), c.getString(c.getColumnIndex("batchtype")), c.getString(c.getColumnIndex("completion_status")), c.getString(c.getColumnIndex("batch_end_date")), c.getString(c.getColumnIndex("trainer_mail_id")), c.getString(c.getColumnIndex("trainer_mobile_no")), c.getString(c.getColumnIndex("attendance_marked")), c.getString(c.getColumnIndex("ref_batch")), c.getString(c.getColumnIndex("faculty_Name")), c.getString(c.getColumnIndex("Code")), c.getString(c.getColumnIndex("meeting_link")), c.getString(c.getColumnIndex("wa_invite_link"))));

                    } while (c.moveToNext());
                }


                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        aAdapter = new ArrayAdapter<BatchesForStudentsDAO>(getActivity(), R.layout.item, batchArrayList);
                        autoCompleteTextViewBatch.setAdapter(aAdapter);
                        completion_status = "";
                        autoCompleteTextViewBatch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                                // String s = parent.getItemAtPosition(i).toString();
                                BatchesForStudentsDAO student = (BatchesForStudentsDAO) parent.getAdapter().getItem(i);
                                String date_after = formateDateFromstring("yyyy-MM-dd", "dd-MMM-yyyy", student.getStart_date());
                                //  Toast.makeText(getApplicationContext(), "Source ID: " +date_after+"   "+ LeadSource.getId() + ",  Source Name : " + LeadSource.getBatch_code(), Toast.LENGTH_SHORT).show();
                                start_date = date_after;
                                course_name = student.getCourse_name();
                                notes = student.getNotes();
                                timings = student.getTimings();
                                duration = student.getDuration();
                                fees = student.getFees();
                                batch_type = student.getBatchtype();
                                frequency = student.getFrequency();
                                branch_name = student.getBranch_name();
                                batch_id = student.getId();
                                batch_code = student.getBatch_code();
                                completion_status = student.getCompletion_status();
                                attendance_mark = student.getAttendance_marked();
                                wa_link = student.getWa_invite_link();
                                m_link = student.getMeeting_link();
                                Log.d("completion_status->", completion_status);
                                Log.d("trainer mail id->", student.getTrainer_mail_id());
                                Log.d("attendance_mark->", attendance_mark);
                                Log.d("Id---->", student.getId() + "" + student.getCourse_name());
                                prefEditor.putString("wa_link", wa_link);
                                prefEditor.putString("batch_id", batch_id);
                                prefEditor.putString("course_name", student.getCourse_name());
                                prefEditor.putString("batch_code", student.getBatch_code());
                                prefEditor.putString("batch_fees", student.getFees());
                                prefEditor.putString("sendingmailid", student.getTrainer_mail_id());
                                prefEditor.putString("trainer_mobile_no", student.getTrainer_mobile_no());
                                prefEditor.putString("trainer_name", student.getFaculty_Name());
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

                                studentSHS.setVisibility(View.VISIBLE);
                                if (completion_status.equals("1")) {
                                    batchAttendance.setVisibility(View.GONE);
                                    batchMarkedAttendance.setVisibility(View.GONE);
                                    sendRequestForCerti.setVisibility(View.VISIBLE);
                                    new checkEntryCertificate().execute();
                                }
                                if (completion_status.equals("0")) {
                                    if (student.getAttendance_marked().equals("1")) {
                                        batchAttendance.setVisibility(View.GONE);
                                        batchMarkedAttendance.setVisibility(View.VISIBLE);
                                        sendRequestForCerti.setVisibility(View.GONE);


                                    }
                                    if (student.getAttendance_marked().equals("0")) {
                                        batchMarkedAttendance.setVisibility(View.GONE);
                                        batchAttendance.setVisibility(View.VISIBLE);
                                        sendRequestForCerti.setVisibility(View.GONE);

                                    }

                                }


                                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                                if (student.getRef_batch().equals("null") || student.getRef_batch().equals("")) {
                                    // Toast.makeText(getApplicationContext(), student.getRef_batch(), Toast.LENGTH_SHORT).show();
                                    UpdateRefBatchEntryView updateRefBatchEntryView = new UpdateRefBatchEntryView();
                                    updateRefBatchEntryView.show(getActivity().getSupportFragmentManager(), "updateRefBatchEntryView");

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

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {

                                data.clear();
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
            if (Status_id != 2) {
                footer.setVisibility(View.VISIBLE);
            } else {
                footer.setVisibility(View.GONE);
            }
            if (data.size() > 0) {

                if (Status_id == 1) {
                    normal.setText("E " + data.size());
                } else if (Status_id == 0) {
                    corporate.setText("L " + data.size());
                } else if (Status_id == 2) {
                    demo.setText("D " + data.size());
                }
                studentListAdpter = new StudentListAdpter(getActivity(), data);
                mstudentList.setAdapter(studentListAdpter);
                mstudentList.setLayoutManager(new LinearLayoutManager(getActivity()));
                studentListAdpter.notifyDataSetChanged();
                // mstudentList.setHasFixedSize(true);
                // setUpItemTouchHelper();
                //setUpAnimationDecoratorHelper();
                //  mProgressDialog.dismiss();
            } else {
                studentListAdpter = new StudentListAdpter(getActivity(), data);
                mstudentList.setAdapter(studentListAdpter);
                mstudentList.setLayoutManager(new LinearLayoutManager(getActivity()));
                studentListAdpter.notifyDataSetChanged();
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
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        studentListDAOArrayAdapter = new ArrayAdapter<StudentListDAO>(getActivity(), R.layout.item, studentArrayList);
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
                                new getVerifyStudentByIdBatch().execute();
                                gender = student.getLast_Name();
                                name = student.getFirst_Name();
                                mobile_no = student.getMobile_No();

                                Log.d("Id---->", student_id);
                                prefEditor.putString("student_id", student_id);
                                prefEditor.commit();
                                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
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

  /*  public String getDeviceId() {
        try {
            telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = telephonyManager.getDeviceId();

        } catch (Exception e) {
        }
        return deviceId;
    }*/

    public static String getIMEINumber(@NonNull final Context context) throws SecurityException, NullPointerException {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            assert tm != null;
            imei = tm.getImei();
            //this change is for Android 10 as per security concern it will not provide the imei number.
            if (imei == null) {
                imei = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        } else {
            assert tm != null;
            if (tm.getDeviceId() != null && !tm.getDeviceId().equals("000000000000000")) {
                imei = tm.getDeviceId();
            } else {
                imei = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }

        return imei;
    }

    public void verifyMobileDeviceId() {


        jsonObj = new JSONObject() {
            {
                try {
                    put("pDeviceID", deviceId);
                    put("role_id", "4");

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

                                            prefEditor.putString("trainer_user_id", "");
                                            prefEditor.commit();
                                            Intent intent = new Intent(getActivity(), TrainerVerfiyActivity.class);
                                            startActivity(intent);
                                            getActivity().finish();
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

    private void storeDialogStatus(boolean isChecked) {
        SharedPreferences mSharedPreferences = getActivity().getSharedPreferences("CheckItem", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putBoolean("item", isChecked);
        mEditor.apply();
    }

    private boolean getDialogStatus() {
        SharedPreferences mSharedPreferences = getActivity().getSharedPreferences("CheckItem", Context.MODE_PRIVATE);
        return mSharedPreferences.getBoolean("item", false);
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
                                    + "https://afckstechnologies.in/fb_text/" + studentFeedBackCode + studentFeedBackCode + System.getProperty("line.separator") + System.getProperty("line.separator") + "WhatsApp Group Link: " + preferences.getString("wa_link", "");
                            String result = sendSms1(mobile_no, msg);
                            Log.d("sent sms---->", result);

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
            if (status) {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                stcorporate = false;
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

    class SendingMailsTask extends AsyncTask<String, Void, String> {

        String sResponse = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = ProgressDialog.show(getActivity(), "Marking Attendance " + (count++) + " / " + studentMobileNoArrayList.size(), "Please wait...", true);
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
                        SyncUtils.TriggerRefresh();
                        new getStudentList().execute();
                    }

                } else {
                    getActivity().finish();
                }

            } catch (Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }

        }
    }

    private void accessAlertDilog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do you want to give Access Notes ,Count is " + studentMobileNoArrayList.size() + " ?")
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
                                jsonObjectSync.put("notes_id", "1");
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
                                                                Toast.makeText(getActivity(), "Due Dates updated successfully", Toast.LENGTH_SHORT).show();
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
                        jsonArraySync = new JSONArray();
                        for (int i = 0; i < studentMobileNoArrayList.size(); i++) {
                            jsonObjectSync = new JSONObject();
                            try {
                                jsonObjectSync.put("batch_id", batch_id);
                                jsonObjectSync.put("user_id", studentMobileNoArrayList.get(i));
                                jsonObjectSync.put("next_due_date", selectedDate);
                                jsonObjectSync.put("notes_id", "0");
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
                                                                Toast.makeText(getActivity(), "Due Dates updated successfully", Toast.LENGTH_SHORT).show();
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
                });

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Updating Access Notes");
        alert.show();
    }

    private class getVerifyCode extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getActivity());
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
                    Toast.makeText(getActivity(), "Please check your webservice", Toast.LENGTH_LONG).show();
                }
            } else {

                Toast.makeText(getActivity(), "Please check your network connection.", Toast.LENGTH_LONG).show();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {


            if (status) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

                Toast.makeText(getActivity(), "Please enter your mobile no in website to login.", Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
            // Close the progressdialog
            mProgressDialog.dismiss();

        }
    }

    private class getVerifyStudentByIdBatch extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getActivity());
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Verify...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            jsonObj = new JSONObject() {
                {
                    try {
                        put("student_user_id", student_id);
                        put("batch_id", batch_id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            WebClient serviceAccess = new WebClient();
            Log.i("json", "json" + jsonObj);
            pendingTaskResponse = serviceAccess.SendHttpPost(Config.URL_GETVERIFYSTUDENTINBATCH, jsonObj);
            Log.i("resp", "pendingTaskResponse" + pendingTaskResponse);


            if (pendingTaskResponse.compareTo("") != 0) {
                if (isJSONValid(pendingTaskResponse)) {


                    try {

                        jsonObject = new JSONObject(pendingTaskResponse);
                        msg = jsonObject.getString("message");
                        status = jsonObject.getBoolean("status");


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
            mProgressDialog.dismiss();

            if (status) {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            } else {
                if (!student_id.equals("")) {
                    add_student.setVisibility(View.VISIBLE);
                }

            }


        }
    }

    private class updateBatchCodeData extends AsyncTask<Void, Void, Void> {
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


                    Toast.makeText(getActivity(), "Please check your webservice", Toast.LENGTH_LONG).show();


                }
            } else {

                Toast.makeText(getActivity(), "Please check your network connection.", Toast.LENGTH_LONG).show();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (status) {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                // Close the progressdialog
                mProgressDialog.dismiss();
                //   getBatchSelect(batch_id);
            } else {
                // Close the progressdialog
                mProgressDialog.dismiss();

            }
        }
    }

    private class getVerifyCodeWithUpdate extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getActivity());
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
                    Toast.makeText(getActivity(), "Please check your webservice", Toast.LENGTH_LONG).show();
                }
            } else {

                Toast.makeText(getActivity(), "Please check your network connection.", Toast.LENGTH_LONG).show();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {


            if (status) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                    // Toast.makeText(getActivity(), "Please check your webservice", Toast.LENGTH_LONG).show();
                }
            } else {

                //  Toast.makeText(getActivity(), "Please check your network connection.", Toast.LENGTH_LONG).show();

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


        PackageManager packageManager = getActivity().getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //  String currentVersion = packageInfo.versionName;
        String currentVersion = packageInfo.versionName;

        new ForceUpdateAsync(currentVersion, getActivity()).execute();

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
                    if (AppStatus.getInstance(getActivity()).isOnline()) {

                        // AppUpdater appUpdater = new AppUpdater((Activity) context);
                        //  appUpdater.start();
                    } else {

                        Toast.makeText(getActivity(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
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

    private class checkEntryCertificate extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            jsonCertiObj = new JSONObject() {
                {
                    try {
                        put("batch_id", batch_id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            WebClient serviceAccess = new WebClient();
            Log.i("json", "json" + jsonObj);
            certificateResponse = serviceAccess.SendHttpPost(Config.URL_CHECKCERTIFICATEENTRY, jsonCertiObj);
            Log.i("resp", "certificateResponse" + certificateResponse);


            if (certificateResponse.compareTo("") != 0) {
                if (isJSONValid(certificateResponse)) {


                    try {

                        JSONObject jObject = new JSONObject(certificateResponse);
                        certi_status = jObject.getString("certi_status");

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
            //  mProgressDialog.dismiss();

        }
    }

    //sending mails
    public void getEmailsCount() {


        jsonObj1 = new JSONObject() {
            {
                try {
                    put("batch_id", batch_id);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Thread objectThread = new Thread(new Runnable() {
            public void run() {
                // TODO Auto-generated method stub
                WebClient serviceAccess = new WebClient();
                getRoleusersResponse = serviceAccess.SendHttpPost(Config.URL_GETEMAILSCOUNT, jsonObj1);
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
                                            sendingEmail();
                                        } else {
                                            Toast.makeText(getActivity(), "Only 3 times  get mail ids in a week of same Batch", Toast.LENGTH_SHORT).show();
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

    private void sendingEmail() {

        String data1 = "";
        studentMailIdArrayList = new ArrayList<>();
        List<StudentsDAO> stList = ((StudentListAdpter) studentListAdpter).getSservicelist();
        for (int i = 0; i < stList.size(); i++) {
            StudentsDAO serviceListDAO = stList.get(i);
            if (serviceListDAO.isSelected()) {
                data1 = serviceListDAO.getEmail_id();
                studentMailIdArrayList.add(data1);
            }
        }
        if (studentMailIdArrayList.size() > 0) {


            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Do you want to Get Students Mail Ids ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mailids = Utils.convertArrayListToStringWithComma(studentMailIdArrayList);
                            //Toast.makeText(getActivity(),sendingmail,Toast.LENGTH_SHORT).show();
                            subject = "Total students " + studentMailIdArrayList.size() + " for " + batch_id + ".";
                            new MyAsyncClass().execute();


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
            alert.setTitle("Getting Students Mail Ids");
            alert.show();


        } else {
            Toast.makeText(getActivity(), "Please select student from list", Toast.LENGTH_LONG).show();
        }
    }

    class MyAsyncClass extends AsyncTask<Void, Void, Void> {


        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Sending Mail...");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {
                // Toast.makeText(getActivity(), "mail id is"+preferences.getString("sendingmailid", ""), Toast.LENGTH_SHORT).show();
                // Add subject, Body, your mail Id, and receiver mail Id.

                if (preferences.getString("trainer_user_id", "").equals("RS")) {
                    sender.sendMail(subject, mailids, username, "mohammed.raza@afcks.com");
                } else {
                    sender.sendMail(subject, mailids, username, preferences.getString("sendingmailid", ""));
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

    private void getFeesOptionStatus() {
        jsonObj2 = new JSONObject() {
            {
                try {
                    put("trainer_user_id", preferences.getString("trainer_user_id", ""));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Thread objectThread = new Thread(new Runnable() {
            public void run() {
                // TODO Auto-generated method stub
                WebClient serviceAccess = new WebClient();
                getFeeStatusResponse = serviceAccess.SendHttpPost(Config.URL_GETAVAILABLESUPERADMIN, jsonObj2);
                Log.i("getFeeStatusResponse", getFeeStatusResponse);
                final Handler handler = new Handler(Looper.getMainLooper());
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() { // This thread runs in the UI
                            @Override
                            public void run() {
                                if (getFeeStatusResponse.compareTo("") == 0) {

                                } else {

                                    try {
                                        JSONObject jObject = new JSONObject(getFeeStatusResponse);
                                        statusv1 = jObject.getBoolean("status");

                                        if (statusv1) {
                                            prefEditor.putString("s_delete", "1");
                                            prefEditor.commit();

                                        } else {
                                            prefEditor.putString("s_delete", "0");
                                            prefEditor.commit();
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
}