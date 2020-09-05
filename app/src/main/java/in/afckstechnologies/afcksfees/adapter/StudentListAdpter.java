package in.afckstechnologies.afcksfees.adapter;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.ContactsContract;

import androidx.core.app.ActivityCompat;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import in.afckstechnologies.afcksfees.Fragments.StudentList;
import in.afckstechnologies.afcksfees.Fragments.TabsFragmentActivity;
import in.afckstechnologies.afcksfees.R;
import in.afckstechnologies.afcksfees.activity.Activity_Location_Details;
import in.afckstechnologies.afcksfees.activity.Activity_User_Profile;
import in.afckstechnologies.afcksfees.activity.SMSSendingActivity;
import in.afckstechnologies.afcksfees.model.StudentsDAO;
import in.afckstechnologies.afcksfees.utils.AppStatus;
import in.afckstechnologies.afcksfees.utils.Config;
import in.afckstechnologies.afcksfees.utils.Constant;
import in.afckstechnologies.afcksfees.utils.FeesListener;
import in.afckstechnologies.afcksfees.utils.WebClient;
import in.afckstechnologies.afcksfees.view.AttendanceDetailsView;
import in.afckstechnologies.afcksfees.view.CommentsDetailsView;
import in.afckstechnologies.afcksfees.view.FeesDetailsView;
import in.afckstechnologies.afcksfees.view.FeesReminderDetailsView;
import in.afckstechnologies.afcksfees.view.MultipleCommentAddView;
import in.afckstechnologies.afcksfees.view.StudentDiscontinueDetailsView;
import in.afckstechnologies.afcksfees.view.StudentDiscontinueEntryView;
import in.afckstechnologies.afcksfees.view.StudentFeesEntryView;
import in.afckstechnologies.afcksfees.view.StudentTransferFeesEntryView;


/**
 * Created by admin on 3/18/2017.
 */

public class StudentListAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    List<StudentsDAO> data;
    StudentsDAO current;
    int currentPos = 0;
    String id, id1;
    String centerId;
    int ID;
    int number = 1;
    int posr;

    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    String sms_type = "", corporate_type = "", addStudentRespone = "", name = "", mobile_no = "";

    ProgressDialog mProgressDialog;
    private JSONObject jsonLeadObj, jsonLeadObj1;
    JSONArray jsonArray, jsonarray;
    String centerListResponse = "", feesstatusResponse = "", notes_id = "";
    boolean status;
    String message = "";
    String msg = "";
    int flag = 1;
    static String sms_user = "";
    static String sms_pass = "";
    String attendenceStudentId = "";
    String attendenceBatchId = "";
    String attendencePresent = "";
    boolean undoOn; // is undo on, you can turn it on from the toolbar menu
    private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec
    List<StudentsDAO> itemsPendingRemoval = new ArrayList<>();

    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<StudentsDAO, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be

    private static FeesListener mListener;

    String user_id = "";
    String batch_id = "";
    String fees_status = "";
    boolean f_status;
    String discontinue_reason = "";
    String UserID = "";
    String BatchID = "", attendanceListResponse;
    String m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11;
    boolean stcorporate = false;
    String FeesApplicable = "", studentFeedBackCode = "";

    // create constructor to innitilize context and data sent from MainActivity
    public StudentListAdpter(Context context, List<StudentsDAO> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        preferences = context.getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        sms_user = preferences.getString("sms_username", "");
        sms_pass = preferences.getString("sms_password", "");
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_student_details, parent, false);
        MyHolder holder = new MyHolder(view);

        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final int pos = position;
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder = (MyHolder) holder;
        current = data.get(position);

        myHolder.notes1.setTag(position);

        if (current.getNotes().equals("null") || current.getNotes().equals("")) {
            myHolder.notes1.setVisibility(View.GONE);
        } else {
            myHolder.notes1.setVisibility(View.VISIBLE);
            myHolder.notes.setText(current.getNotes());
            myHolder.notes.setTag(position);
        }


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            if (current.getEmail_id().equals("null") || current.getEmail_id().equals("")) {
                // myHolder.name.setBackgroundColor(Color.parseColor("FF0000"));
                myHolder.name.setText(Html.fromHtml("<a><font color='yellow'> " + current.getStudents_Name() + "</font> </a>", Html.FROM_HTML_MODE_COMPACT));
                myHolder.name.setTag(position);
            } else {
                myHolder.name.setText(Html.fromHtml("<a><font color='white'> " + current.getStudents_Name() + "</font> </a>", Html.FROM_HTML_MODE_COMPACT));
                myHolder.name.setTag(position);
            }
        } else {


            if (current.getEmail_id().equals("null") || current.getEmail_id().equals("")) {
                // myHolder.name.setBackgroundColor(Color.parseColor("FF0000"));
                myHolder.name.setText(Html.fromHtml("<a><font color='yellow'> " + current.getStudents_Name() + "</font> </a>"));
                myHolder.name.setTag(position);
            } else {
                myHolder.name.setText(Html.fromHtml("<a><font color='white'> " + current.getStudents_Name() + "</font> </a>"));
                myHolder.name.setTag(position);
            }
        }

        myHolder.callingButton.setTag(position);
        myHolder.messageButton.setTag(position);

        myHolder.numbers.setTag(position);
        myHolder.whatsappeButton.setTag(position);
        myHolder.numbers.setText(current.getNumbers());
        myHolder.feesButton.setTag(position);
        myHolder.guestFees.setTag(position);
        myHolder.layoutBackChange.setTag(position);
        myHolder.nonClickButton.setTag(position);
        myHolder.transferStudents.setTag(position);
        myHolder.notTransferStudents.setTag(position);
        myHolder.takeViewInfo1.setTag(position);

        if (preferences.getString("trainer_user_id", "").equals("RS") || preferences.getString("trainer_user_id", "").equals("AK")) {
            myHolder.notTransferStudents.setVisibility(View.GONE);
            myHolder.transferStudents.setVisibility(View.VISIBLE);

        } else {
            myHolder.transferStudents.setVisibility(View.GONE);
            myHolder.notTransferStudents.setVisibility(View.VISIBLE);

        }


        if (!current.getDue_amount().equals("null") && !current.getDue_amount().equals("0")) {
            myHolder.totalFees.setText("Rs." + current.getDue_amount());
            myHolder.totalFees.setTag(position);
            myHolder.dueDate.setText(current.getNext_due_date());
            myHolder.dueDate.setTag(position);

        } else {
            myHolder.totalFees.setText("Rs.0");
            myHolder.totalFees.setTag(position);
            myHolder.dueDate.setText("");
            myHolder.dueDate.setTag(position);
        }
        if (current.getCorporate().equals("1")) {
            myHolder.guestFees.setVisibility(View.GONE);
            myHolder.totalFees.setVisibility(View.VISIBLE);
            myHolder.layoutBackChange.setBackgroundColor(Color.parseColor("#556199"));
            myHolder.lbtn1.setBackgroundColor(Color.parseColor("#556199"));

        } else if (current.getCorporate().equals("2")) {
            myHolder.guestFees.setVisibility(View.GONE);
            myHolder.totalFees.setVisibility(View.VISIBLE);

            myHolder.layoutBackChange.setBackgroundColor(Color.parseColor("#556199"));
            myHolder.lbtn1.setBackgroundColor(Color.parseColor("#556199"));
        } else if (current.getCorporate().equals("3")) {
            myHolder.guestFees.setVisibility(View.GONE);
            myHolder.totalFees.setVisibility(View.VISIBLE);

            myHolder.layoutBackChange.setBackgroundColor(Color.parseColor("#800000"));
            myHolder.lbtn1.setBackgroundColor(Color.parseColor("#800000"));
        } else if (current.getCorporate().equals("5")) {
            myHolder.guestFees.setVisibility(View.GONE);
            myHolder.totalFees.setVisibility(View.VISIBLE);

            myHolder.layoutBackChange.setBackgroundColor(Color.parseColor("#fda4a4"));
            myHolder.lbtn1.setBackgroundColor(Color.parseColor("#fda4a4"));
        } else {
            myHolder.totalFees.setVisibility(View.GONE);
            myHolder.guestFees.setVisibility(View.VISIBLE);

            myHolder.layoutBackChange.setBackgroundColor(Color.parseColor("#006400"));
            myHolder.lbtn1.setBackgroundColor(Color.parseColor("#006400"));
        }
        myHolder.chkSelected.setTag(position);
        myHolder.chkSelected.setChecked(data.get(position).isSelected());
        myHolder.chkSelected.setTag(data.get(position));

        myHolder.locationButton.setTag(position);
        myHolder.coursesButton.setTag(position);

        myHolder.normalButton.setTag(position);
        myHolder.corporateButton.setTag(position);
        myHolder.mos.setTag(position);
        myHolder.moscorporate.setTag(position);
        myHolder.noncorporateButton.setTag(position);
        myHolder.takeViewInfo.setTag(position);
        myHolder.takeViewDustbean.setTag(position);
        myHolder.takeViewContinue.setTag(position);
        myHolder.clickForShowBtn.setTag(position);
        myHolder.lbtn1.setTag(position);
        myHolder.takeNotViewInfo.setTag(position);
        myHolder.takeEditNotNotes.setTag(position);
        myHolder.takeEditNotes.setTag(position);
        myHolder.takeComment.setTag(position);
        myHolder.messageCount.setTag(position);
        myHolder.hidemessageCount.setTag(position);
        myHolder.previousAttendance.setText(current.getPrevious_attendance());
        myHolder.previousAttendance.setTag(position);
        myHolder.middleHide.setTag(position);
        myHolder.addButton.setTag(position);
        myHolder.takeDeleteStudent.setTag(position);

        if (current.getNotes_id().equals("1")) {
            myHolder.takeEditNotNotes.setVisibility(View.GONE);
            myHolder.takeEditNotes.setVisibility(View.VISIBLE);

        }
        if (current.getNotes_id().equals("0")) {
            myHolder.takeEditNotes.setVisibility(View.GONE);
            myHolder.takeEditNotNotes.setVisibility(View.VISIBLE);

        }

        if (current.getDiscontinued().equals("0")) {
            myHolder.takeViewInfo.setVisibility(View.GONE);
            myHolder.takeNotViewInfo.setVisibility(View.VISIBLE);

        }
        if (!current.getDiscontinued().equals("0")) {
            myHolder.takeNotViewInfo.setVisibility(View.GONE);
            myHolder.takeViewInfo.setVisibility(View.VISIBLE);

        }
        if (current.getStatus().equals("0")) {

            myHolder.takeViewContinue.setVisibility(View.VISIBLE);
            myHolder.takeViewDustbean.setVisibility(View.GONE);
        }
        if (current.getStatus().equals("1")) {
            myHolder.takeViewDustbean.setVisibility(View.VISIBLE);
            myHolder.takeViewContinue.setVisibility(View.GONE);
        }


        if (current.getCorporate().equals("0")) {
            myHolder.normalButton.setVisibility(View.VISIBLE);
            myHolder.noncorporateButton.setVisibility(View.GONE);
            myHolder.corporateButton.setVisibility(View.GONE);
            myHolder.mos.setVisibility(View.GONE);
            myHolder.moscorporate.setVisibility(View.GONE);
        }
        if (current.getCorporate().equals("1")) {

            myHolder.normalButton.setVisibility(View.GONE);
            myHolder.noncorporateButton.setVisibility(View.VISIBLE);
            myHolder.corporateButton.setVisibility(View.GONE);
            myHolder.mos.setVisibility(View.GONE);
            myHolder.moscorporate.setVisibility(View.GONE);
            myHolder.nonClickButton.setVisibility(View.GONE);
        } else if (current.getCorporate().equals("2")) {
            myHolder.normalButton.setVisibility(View.GONE);
            myHolder.noncorporateButton.setVisibility(View.GONE);
            myHolder.corporateButton.setVisibility(View.VISIBLE);
            myHolder.mos.setVisibility(View.GONE);
            myHolder.moscorporate.setVisibility(View.GONE);
            myHolder.nonClickButton.setVisibility(View.GONE);
        } else {
            myHolder.normalButton.setVisibility(View.GONE);
            myHolder.noncorporateButton.setVisibility(View.GONE);
            myHolder.corporateButton.setVisibility(View.GONE);
            myHolder.mos.setVisibility(View.GONE);
            myHolder.moscorporate.setVisibility(View.GONE);
            myHolder.nonClickButton.setVisibility(View.VISIBLE);
        }
       /* if (current.getCorporate().equals("3")) {
            myHolder.normalButton.setVisibility(View.GONE);
            myHolder.noncorporateButton.setVisibility(View.GONE);
            myHolder.corporateButton.setVisibility(View.GONE);
            myHolder.mos.setVisibility(View.VISIBLE);
            myHolder.moscorporate.setVisibility(View.GONE);
        }
        if (current.getCorporate().equals("4")) {
            myHolder.normalButton.setVisibility(View.GONE);
            myHolder.noncorporateButton.setVisibility(View.GONE);
            myHolder.corporateButton.setVisibility(View.GONE);
            myHolder.mos.setVisibility(View.GONE);
            myHolder.moscorporate.setVisibility(View.VISIBLE);
        }*/
        if (current.getReminder_count().equals("0")) {
            myHolder.messageCount.setVisibility(View.GONE);
            myHolder.hidemessageCount.setVisibility(View.VISIBLE);
        } else {
            myHolder.hidemessageCount.setVisibility(View.GONE);
            myHolder.messageCount.setVisibility(View.VISIBLE);
            myHolder.messageCount.setText(current.getReminder_count());

        }

        if (preferences.getBoolean(current.getUser_id(), false)) {
            myHolder.chkSelected.setChecked(true);
            current.setSelected(true);
        } else {
            myHolder.chkSelected.setChecked(false);
            current.setSelected(false);
        }

        if (current.getStudent_batch_cat().equals("5")) {
            myHolder.middleHide.setVisibility(View.GONE);
            myHolder.normalButton.setVisibility(View.GONE);
            myHolder.noncorporateButton.setVisibility(View.GONE);
            myHolder.corporateButton.setVisibility(View.GONE);
            myHolder.mos.setVisibility(View.GONE);
            myHolder.moscorporate.setVisibility(View.GONE);
            myHolder.nonClickButton.setVisibility(View.GONE);
            myHolder.chkSelected.setVisibility(View.GONE);
            myHolder.takeEditNotes.setVisibility(View.GONE);
            myHolder.takeViewDustbean.setVisibility(View.GONE);
            myHolder.transferStudents.setVisibility(View.GONE);
            myHolder.addButton.setVisibility(View.VISIBLE);
            myHolder.takeViewInfo1.setVisibility(View.VISIBLE);
            if (preferences.getString("s_delete", "").equals("1")) {
                myHolder.takeDeleteStudent.setVisibility(View.VISIBLE);
            }

        } else {
            myHolder.takeViewInfo1.setVisibility(View.GONE);
        }

        myHolder.takeDeleteStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                id = current.getSbd_id();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to delete " + current.getFirst_name() + " ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                new deleteStudent().execute();

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
                alert.setTitle("Deleting Student");
                alert.show();

            }
        });
        myHolder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                batch_id = current.getBatchid();
                user_id = current.getUser_id();
                name = current.getFirst_name();
                mobile_no = current.getMobile_no();
                //   Toast.makeText(context, current.getUser_id(), Toast.LENGTH_LONG).show();
                if (!current.getGender().equals("")) {

//Dialog code
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View mView = inflater.inflate(R.layout.dialog_app_updates, null);
                    CheckBox mCheckBox = mView.findViewById(R.id.checkBox);
                    CheckBox mCheckBoxCor = mView.findViewById(R.id.checkBoxCorporate);
                    mCheckBox.setChecked(getDialogStatus());
                    mBuilder.setTitle("Add Student");
                    mBuilder.setMessage("If below is checked Student will get fees reminder ");
                    mBuilder.setView(mView);
                    mBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if (getDialogStatus()) {
                                if (stcorporate) {
                                    FeesApplicable = "2";
                                } else {
                                    FeesApplicable = "1";
                                }
                            } else {
                                FeesApplicable = "0";
                            }
                            new updateDemoStudent().execute();
                            dialogInterface.cancel();

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
                    Toast.makeText(context, "Please Update Gender...", Toast.LENGTH_LONG).show();
                    prefEditor.putString("user_id", current.getUser_id());
                    prefEditor.putString("edit_u_p_flag", "fu");
                    prefEditor.commit();
                    Intent intent = new Intent(context, Activity_User_Profile.class);
                    context.startActivity(intent);
                }


            }
        });
        myHolder.chkSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                StudentsDAO contact = (StudentsDAO) cb.getTag();
                contact.setSelected(cb.isChecked());
                data.get(pos).setSelected(cb.isChecked());
                if (cb.isChecked()) {
                    prefEditor.putString("cscb", "1");
                    prefEditor.putBoolean(contact.getUser_id(), true);
                    prefEditor.commit();
                } else {
                    prefEditor.remove(contact.getUser_id());
                    prefEditor.commit();
                }
                // Toast.makeText(v.getContext(), "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked(), Toast.LENGTH_LONG).show();

            }
        });
        myHolder.callingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                Log.i("call ph no", "" + current.getMobile_no());
                callIntent.setData(Uri.parse("tel:" + current.getMobile_no()));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                context.startActivity(callIntent);


            }
        });
        myHolder.messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                final CharSequence[] items = {" AFCKST ", " Mobile SMS "};
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("Select The SMS Mode")
                        .setCancelable(false)
                        .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {


                                switch (item) {
                                    case 0:
                                        // Your code when first option seletced
                                        sms_type = "AFCKST";
                                        break;
                                    case 1:
                                        // Your code when 2nd  option seletced
                                        sms_type = "Mobile_SMS";
                                        break;


                                }
                            }
                        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //  Your code when user clicked on OK
                                //  You can write the code  to save the selected item here
                                Log.d("sms_type", sms_type);
                                if (!sms_type.equals("")) {
                                    //sms sending
                                    Intent intent = new Intent(context, SMSSendingActivity.class);
                                    intent.putExtra("mobile_no", current.getMobile_no());
                                    intent.putExtra("sms_type", sms_type);
                                    context.startActivity(intent);
                                } else {

                                    Toast.makeText(context, "Please select SMS Mode!", Toast.LENGTH_LONG).show();
                                }
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //  Your code when user clicked on Cancel
                            }
                        }).create();
                dialog.show();

            }
        });

        myHolder.whatsappeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                String temp_message = "";
                String msgData1 = preferences.getString("template_txt_copy", "");
                boolean fristName = msgData1.contains("[first_name]");
                boolean courseName = msgData1.contains("[course_name]");
                boolean startDate = msgData1.contains("[start_date]");
                boolean note = msgData1.contains("[notes]");
                boolean timings1 = msgData1.toString().contains("[timings]");
                boolean branch = msgData1.contains("[branch_name]");
                boolean frequency1 = msgData1.contains("[frequency]");
                boolean duration1 = msgData1.contains("[duration]");

                if (fristName) {
                    m1 = msgData1.replace("[first_name]", current.getFirst_name());
                    temp_message = m1;
                }
                if (courseName) {
                    m2 = temp_message.replace("[course_name]", preferences.getString("template_course_name_copy", ""));
                    temp_message = m2;
                }
                if (startDate) {
                    m3 = temp_message.replace("[start_date]", preferences.getString("template_start_date_copy", ""));
                    temp_message = m3;
                }
                if (note) {
                    m4 = temp_message.replace("[notes]", preferences.getString("template_notes_copy", ""));
                    temp_message = m4;
                }
                if (branch) {
                    m5 = temp_message.replace("[branch_name]", preferences.getString("template_branch_name_copy", ""));
                    temp_message = m5;
                }
                if (frequency1) {
                    m6 = temp_message.replace("[frequency]", preferences.getString("template_frequency_copy", ""));
                    temp_message = m6;
                }
                if (duration1) {
                    m7 = temp_message.replace("[duration]", preferences.getString("template_duration_copy", ""));
                    temp_message = m7;
                }
                if (timings1) {
                    m8 = temp_message.replace("[timings]", preferences.getString("template_timings_copy", ""));
                    temp_message = m8;
                }
                PackageManager packageManager = context.getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);

                try {
                    String url = "https://api.whatsapp.com/send?phone=" + "91" + current.getMobile_no() + "&text=" + URLEncoder.encode(temp_message, "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        context.startActivity(i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        myHolder.feesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                id = current.getSbd_id();
                prefEditor.putString("id", current.getSbd_id());
                prefEditor.commit();
                if (!current.getEmail_id().equals("")) {
                    if (!current.getCorporate().equals("0")) {
                        prefEditor.putString("student_name", current.getStudents_Name());
                        prefEditor.putString("mobile_no", current.getMobile_no());
                        prefEditor.putString("id_fees", current.getUser_id());
                        prefEditor.putString("mail_id", current.getEmail_id());
                        prefEditor.putString("gender", current.getGender());
                        prefEditor.putString("corporate", current.getCorporate());
                        prefEditor.putString("st_id_cashback", current.getUser_id());
                        prefEditor.commit();
                        user_id = current.getUser_id();
                        batch_id = current.getBatchid();
                        //   new getcashback().execute();
                        //StudentFeesEntryView studentFeesEntryView = new StudentFeesEntryView();
                        //studentFeesEntryView.show(((FragmentActivity) context).getSupportFragmentManager(), "studentFeesEntryView");
                    } else {

                        // Create custom dialog object
                        final Dialog dialog = new Dialog(context);
                        // Include dialog.xml file
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_choise_option);
                        final RadioGroup radioGroup;
                        dialog.show();
                        radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup);
                        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                // TODO Auto-generated method stub

                                // Method 1 For Getting Index of RadioButton
                                posr = radioGroup.indexOfChild(dialog.findViewById(checkedId));
                                switch (posr) {
                                    case 1:

                                        corporate_type = "2";
                                        // Toast.makeText(context, corporate_type,Toast.LENGTH_SHORT).show();
                                        break;
                                    case 2:

                                        corporate_type = "3";
                                        //Toast.makeText(context, corporate_type, Toast.LENGTH_SHORT).show();
                                        break;
                                    case 3:

                                        corporate_type = "4";
                                        //Toast.makeText(context, corporate_type,Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        //The default selection is RadioButton 1
                                        corporate_type = "1";
                                        //Toast.makeText(context, corporate_type,Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        });


                        Button declineButton = (Button) dialog.findViewById(R.id.placeBtn);
                        // if decline button is clicked, close the custom dialog
                        declineButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Close dialog
                                if (!corporate_type.equals("")) {
                                    //sms sending
                                    dialog.dismiss();
                                    if (corporate_type == "2" || corporate_type == "4") {
                                        prefEditor.putString("corporate_type", corporate_type);
                                        prefEditor.commit();
                                        // StudentCorporateRegView studentCorporateRegView = new StudentCorporateRegView();
                                        // studentCorporateRegView.show(((FragmentActivity) context).getSupportFragmentManager(), "studentCorporateRegView");
                                    } else {
                                        //  Toast.makeText(context, "MOS", Toast.LENGTH_LONG).show();
                                        new updateCorporate().execute();
                                    }

                                } else {

                                    Toast.makeText(context, "Please select Option!", Toast.LENGTH_LONG).show();
                                }

                            }

                        });

                    }


                } else {
                    Toast.makeText(context, "Please Update Email Id...", Toast.LENGTH_LONG).show();

                }

            }
        });

        myHolder.totalFees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                id = current.getSbd_id();
                prefEditor.putString("id", current.getSbd_id());
                prefEditor.commit();
                String gender = current.getStudents_Name().trim().substring(0, 3);
                if (!current.getEmail_id().equals("")) {
                    if (!gender.equals("Na.")) {
                        if (!current.getCorporate().equals("0")) {
                            if (AppStatus.getInstance(context).isOnline()) {
                                prefEditor.putString("student_name", current.getStudents_Name());
                                prefEditor.putString("mobile_no", current.getMobile_no());
                                prefEditor.putString("id_fees", current.getUser_id());
                                prefEditor.putString("mail_id", current.getEmail_id());
                                prefEditor.putString("gender", current.getGender());
                                prefEditor.putString("corporate", current.getCorporate());
                                prefEditor.putString("fees_amount", current.getDue_amount());
                                prefEditor.putString("discount", current.getDiscount());
                                prefEditor.putString("st_id_cashback", current.getUser_id());
                                prefEditor.putString("sendingmailid", current.getTrainer_email_id());
                                prefEditor.commit();
                                user_id = current.getUser_id();
                                batch_id = current.getBatchid();
                                if (!current.getDue_amount().equals("null") && !current.getDue_amount().equals("0")) {

                                    StudentFeesEntryView studentFeesEntryView = new StudentFeesEntryView();
                                    studentFeesEntryView.show(((FragmentActivity) context).getSupportFragmentManager(), "studentFeesEntryView");
                                }
                            } else {

                                Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                            }
                            //  new getcashback().execute();
                            //  new initCallbackCount().execute();
                            //StudentFeesEntryView studentFeesEntryView = new StudentFeesEntryView();
                            //studentFeesEntryView.show(((FragmentActivity) context).getSupportFragmentManager(), "studentFeesEntryView");
                        } else {

                            // Create custom dialog object
                            final Dialog dialog = new Dialog(context);
                            // Include dialog.xml file
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.dialog_choise_option);
                            final RadioGroup radioGroup;
                            dialog.show();
                            radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup);
                            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                                @Override
                                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                    // TODO Auto-generated method stub

                                    // Method 1 For Getting Index of RadioButton
                                    posr = radioGroup.indexOfChild(dialog.findViewById(checkedId));
                                    switch (posr) {
                                        case 1:

                                            corporate_type = "2";
                                            Toast.makeText(context, corporate_type, Toast.LENGTH_SHORT).show();
                                            break;

                                        default:
                                            //The default selection is RadioButton 1
                                            corporate_type = "1";
                                            Toast.makeText(context, corporate_type, Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                }
                            });


                            Button declineButton = (Button) dialog.findViewById(R.id.placeBtn);
                            // if decline button is clicked, close the custom dialog
                            declineButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Close dialog
                                    if (!corporate_type.equals("")) {
                                        //sms sending
                                        dialog.dismiss();
                                        new updateCorporate().execute();
                                       /* if (corporate_type == "2" || corporate_type == "4") {
                                            prefEditor.putString("corporate_type", corporate_type);
                                            prefEditor.commit();
                                            StudentCorporateRegView studentCorporateRegView = new StudentCorporateRegView();
                                            studentCorporateRegView.show(((FragmentActivity) context).getSupportFragmentManager(), "studentCorporateRegView");
                                        } else {
                                            //  Toast.makeText(context, "MOS", Toast.LENGTH_LONG).show();
                                            new updateCorporate().execute();
                                        }
*/
                                    } else {

                                        Toast.makeText(context, "Please select Option!", Toast.LENGTH_LONG).show();
                                    }

                                }

                            });

                        }
                    } else {
                        Toast.makeText(context, "Please Update Gender...", Toast.LENGTH_LONG).show();
                        prefEditor.putString("user_id", current.getUser_id());
                        prefEditor.putString("edit_u_p_flag", "fu");
                        prefEditor.commit();
                        Intent intent = new Intent(context, Activity_User_Profile.class);
                        context.startActivity(intent);
                    }

                } else {
                    Toast.makeText(context, "Please Update Email Id...", Toast.LENGTH_LONG).show();

                }

            }
        });

        myHolder.totalFees.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                prefEditor.putString("id_fees", current.getUser_id());
                prefEditor.putString("batchId", current.getBatchid());
                prefEditor.putString("check_fees", current.getTotalFees());
                prefEditor.putString("da", current.getDue_amount());
                prefEditor.putString("ndd", current.getNext_due_date());
                prefEditor.commit();
                FeesDetailsView feesDetailsView = new FeesDetailsView();
                feesDetailsView.show(((FragmentActivity) context).getSupportFragmentManager(), "feesDetailsView");
              /*  if (!current.getTotalFees().equals("0")) {
                    // Toast.makeText(context, "batch id/user_id" + current.getBatchid() + "===" + current.getUser_id(), Toast.LENGTH_SHORT).show();
                    prefEditor.putString("id_fees", current.getUser_id());
                    prefEditor.putString("batchId", current.getBatchid());
                    prefEditor.putString("check_fees", current.getTotalFees());
                    prefEditor.commit();
                    FeesDetailsView feesDetailsView = new FeesDetailsView();
                    feesDetailsView.show(((FragmentActivity) context).getSupportFragmentManager(), "feesDetailsView");
                } else {
                    Toast.makeText(context, "Not available fees details ", Toast.LENGTH_SHORT).show();
                }*/
                return true;
            }
        });
        myHolder.locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                prefEditor.putString("user_id", current.getUser_id());
                prefEditor.putString("temp_type_sms", "1");
                prefEditor.commit();
                Intent intent = new Intent(context, Activity_Location_Details.class);
                context.startActivity(intent);

            }
        });
        myHolder.coursesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                prefEditor.putString("user_id", current.getUser_id());
                prefEditor.putString("temp_type_sms", "1");
                prefEditor.commit();
                Intent intent = new Intent(context, TabsFragmentActivity.class);
                context.startActivity(intent);

            }
        });


        myHolder.takeEditNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                id = current.getSbd_id();
                //  new deleteNotes().execute();
            }
        });
        myHolder.takeEditNotNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                id = current.getSbd_id();
                //  new addNotes().execute();
            }
        });


        myHolder.previousAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);

                prefEditor.putString("id_student_p", current.getUser_id());
                prefEditor.putString("batchId", current.getBatchid());
                prefEditor.putString("a_student_name", current.getStudents_Name());
                prefEditor.commit();
                AttendanceDetailsView attendanceDetailsView = new AttendanceDetailsView();
                attendanceDetailsView.show(((FragmentActivity) context).getSupportFragmentManager(), "attendanceDetailsView");

            }
        });
        myHolder.takeViewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                prefEditor.putString("a_student_name", current.getStudents_Name());
                prefEditor.putString("D_UserID", current.getUser_id());
                prefEditor.commit();
                StudentDiscontinueDetailsView studentDiscontinueDetailsView = new StudentDiscontinueDetailsView();
                studentDiscontinueDetailsView.show(((FragmentActivity) context).getSupportFragmentManager(), "studentDiscontinueDetailsView");

            }
        });
        myHolder.takeViewInfo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                prefEditor.putString("a_student_name", current.getStudents_Name());
                prefEditor.putString("D_UserID", current.getUser_id());
                prefEditor.commit();
                StudentDiscontinueDetailsView studentDiscontinueDetailsView = new StudentDiscontinueDetailsView();
                studentDiscontinueDetailsView.show(((FragmentActivity) context).getSupportFragmentManager(), "studentDiscontinueDetailsView");

            }
        });
        myHolder.takeViewDustbean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                BatchID = current.getBatchid();
                UserID = current.getUser_id();
                prefEditor.putString("D_BatchID", current.getBatchid());
                prefEditor.putString("D_UserID", current.getUser_id());
                prefEditor.commit();
                StudentDiscontinueEntryView studentDiscontinueEntryView = new StudentDiscontinueEntryView();
                studentDiscontinueEntryView.show(((FragmentActivity) context).getSupportFragmentManager(), "studentDiscontinueEntryView");

            }
        });
        myHolder.takeViewContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                batch_id = current.getBatchid();
                user_id = current.getUser_id();
                prefEditor.putBoolean(current.getUser_id(), true);
                prefEditor.commit();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to give access for notes ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                try {
                                    notes_id = "1";
                                    new continueStudent().execute();
                                } catch (Exception ex) {
                                    Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
                                }
                                dialog.cancel();
                            }
                        })
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                                try {
                                    notes_id = "0";
                                    new continueStudent().execute();
                                } catch (Exception ex) {
                                    Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
                                }

                            }
                        });

                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Regarding  Notes");
                alert.show();


            }
        });


        /*myHolder.notes1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                prefEditor.putString("user_id", current.getUser_id());
                prefEditor.commit();
                CommentAddView commentAddView = new CommentAddView();
                commentAddView.show(((FragmentActivity) context).getSupportFragmentManager(), "commentAddView");

            }
        });*/

        myHolder.clickForShowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                // if (!current.getStudent_batch_cat().equals("5")) {
                if (flag == 1) {
                    flag = 2;
                    myHolder.lbtn1.setVisibility(View.VISIBLE);
                } else {
                    flag = 1;
                    myHolder.lbtn1.setVisibility(View.GONE);

                }
                //  }

            }
        });

        myHolder.clickForShowBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                prefEditor.putString("user_id", current.getUser_id());
                prefEditor.putString("edit_u_p_flag", "fu");
                prefEditor.commit();
                Intent intent = new Intent(context, Activity_User_Profile.class);
                context.startActivity(intent);
                return true;
            }
        });
        myHolder.lbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myHolder.lbtn1.setVisibility(View.GONE);

            }
        });
        myHolder.transferStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                // Toast.makeText(context,""+current.getBatchid(),Toast.LENGTH_SHORT).show();

                prefEditor.putString("user_id", current.getUser_id());
                prefEditor.putString("out_batch", current.getBatchid());
                prefEditor.putString("mobile_no", current.getMobile_no());
                prefEditor.putString("student_name", current.getStudents_Name());
                prefEditor.commit();
                StudentTransferFeesEntryView studentTransferFeesEntryView = new StudentTransferFeesEntryView();
                studentTransferFeesEntryView.show(((FragmentActivity) context).getSupportFragmentManager(), "studentTransferFeesEntryView");

            }
        });

        //modify cat.
        myHolder.normalButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                prefEditor.putString("id", current.getSbd_id());
                prefEditor.commit();
                //  Toast.makeText(context,"n"+current.getCorporate(),Toast.LENGTH_SHORT).show();
                // Create custom dialog object
                final Dialog dialog = new Dialog(context);
                // Include dialog.xml file
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_choise_option);
                final RadioGroup radioGroup;
                final RadioButton normal, corporate;

                dialog.show();
                radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup);
                normal = (RadioButton) dialog.findViewById(R.id.normal);
                corporate = (RadioButton) dialog.findViewById(R.id.corporate);

                if (current.getCorporate().equals("1")) {
                    normal.setChecked(true);
                    corporate_type = "1";
                }
                if (current.getCorporate().equals("2")) {
                    corporate.setChecked(true);
                    corporate_type = "2";
                }

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        // TODO Auto-generated method stub

                        // Method 1 For Getting Index of RadioButton
                        posr = radioGroup.indexOfChild(dialog.findViewById(checkedId));
                        switch (posr) {
                            case 1:

                                corporate_type = "2";
                                Toast.makeText(context, corporate_type, Toast.LENGTH_SHORT).show();
                                break;

                            default:
                                //The default selection is RadioButton 1
                                corporate_type = "1";
                                Toast.makeText(context, corporate_type, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });


                Button declineButton = (Button) dialog.findViewById(R.id.placeBtn);
                // if decline button is clicked, close the custom dialog
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Close dialog
                        if (!corporate_type.equals("")) {
                            //sms sending
                            dialog.dismiss();
                            new updateCorporate().execute();
                           /* if (corporate_type == "2" || corporate_type == "4") {
                                prefEditor.putString("corporate_type", corporate_type);
                                prefEditor.commit();
                                StudentCorporateRegView studentCorporateRegView = new StudentCorporateRegView();
                                studentCorporateRegView.show(((FragmentActivity) context).getSupportFragmentManager(), "studentCorporateRegView");
                            } else {
                                //  Toast.makeText(context, "MOS", Toast.LENGTH_LONG).show();
                                new updateCorporate().execute();
                            }*/

                        } else {

                            Toast.makeText(context, "Please select Option!", Toast.LENGTH_LONG).show();
                        }

                    }

                });
                return true;
            }
        });

        myHolder.noncorporateButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                prefEditor.putString("id", current.getSbd_id());
                prefEditor.commit();
                //  Toast.makeText(context,"nc"+current.getCorporate(),Toast.LENGTH_SHORT).show();
                // Create custom dialog object
              /*  final Dialog dialog = new Dialog(context);
                // Include dialog.xml file
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_choise_option);
                final RadioGroup radioGroup;
                final RadioButton normal,corporate;

                dialog.show();
                radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup);
                normal=(RadioButton)dialog.findViewById(R.id.normal);
                corporate=(RadioButton)dialog.findViewById(R.id.corporate);

                if(current.getCorporate().equals("1"))
                {
                    normal.setChecked(true);
                    corporate_type = "1";
                }
                if(current.getCorporate().equals("2"))
                {
                    corporate.setChecked(true);
                    corporate_type = "2";
                }

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        // TODO Auto-generated method stub

                        // Method 1 For Getting Index of RadioButton
                        posr = radioGroup.indexOfChild(dialog.findViewById(checkedId));
                        switch (posr) {
                            case 1:

                                corporate_type = "2";
                                // Toast.makeText(context, corporate_type,Toast.LENGTH_SHORT).show();
                                break;

                            default:
                                //The default selection is RadioButton 1
                                corporate_type = "1";
                                //Toast.makeText(context, corporate_type,Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });


                Button declineButton = (Button) dialog.findViewById(R.id.placeBtn);
                // if decline button is clicked, close the custom dialog
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Close dialog
                        if (!corporate_type.equals("")) {
                            //sms sending
                            dialog.dismiss();
                            new updateCorporate().execute();
                           *//* if (corporate_type == "2" || corporate_type == "4") {
                                prefEditor.putString("corporate_type", corporate_type);
                                prefEditor.commit();
                                StudentCorporateRegView studentCorporateRegView = new StudentCorporateRegView();
                                studentCorporateRegView.show(((FragmentActivity) context).getSupportFragmentManager(), "studentCorporateRegView");
                            } else {
                                //  Toast.makeText(context, "MOS", Toast.LENGTH_LONG).show();
                                new updateCorporate().execute();
                            }*//*

                        } else {

                            Toast.makeText(context, "Please select Option!", Toast.LENGTH_LONG).show();
                        }

                    }

                });*/

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to make Corporate Student ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                try {
                                    corporate_type = "2";
                                    new updateCorporate().execute();

                                } catch (Exception ex) {
                                    Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
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
                alert.setTitle("Corporate Student");
                alert.show();


                return true;
            }
        });

        myHolder.corporateButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                prefEditor.putString("id", current.getSbd_id());
                prefEditor.commit();
           /*   //  Toast.makeText(context,"c"+current.getCorporate(),Toast.LENGTH_SHORT).show();
                // Create custom dialog object
                final Dialog dialog = new Dialog(context);
                // Include dialog.xml file
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_choise_option);
                final RadioGroup radioGroup;
                final RadioButton normal,corporate;

                dialog.show();
                radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup);
                normal=(RadioButton)dialog.findViewById(R.id.normal);
                corporate=(RadioButton)dialog.findViewById(R.id.corporate);

                if(current.getCorporate().equals("1"))
                {
                    normal.setChecked(true);
                    corporate_type = "1";
                }
                if(current.getCorporate().equals("2"))
                {
                    corporate.setChecked(true);
                    corporate_type = "2";
                }

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        // TODO Auto-generated method stub

                        // Method 1 For Getting Index of RadioButton
                        posr = radioGroup.indexOfChild(dialog.findViewById(checkedId));
                        switch (posr) {
                            case 1:

                                corporate_type = "2";
                                // Toast.makeText(context, corporate_type,Toast.LENGTH_SHORT).show();
                                break;

                            default:
                                //The default selection is RadioButton 1
                                corporate_type = "1";
                                //Toast.makeText(context, corporate_type,Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });


                Button declineButton = (Button) dialog.findViewById(R.id.placeBtn);
                // if decline button is clicked, close the custom dialog
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Close dialog
                        if (!corporate_type.equals("")) {
                            //sms sending
                            dialog.dismiss();
                            new updateCorporate().execute();
                          *//*  if (corporate_type == "2" || corporate_type == "4") {
                                prefEditor.putString("corporate_type", corporate_type);
                                prefEditor.commit();
                                StudentCorporateRegView studentCorporateRegView = new StudentCorporateRegView();
                                studentCorporateRegView.show(((FragmentActivity) context).getSupportFragmentManager(), "studentCorporateRegView");
                            } else {
                                //  Toast.makeText(context, "MOS", Toast.LENGTH_LONG).show();
                                new updateCorporate().execute();
                            }*//*

                        } else {

                            Toast.makeText(context, "Please select Option!", Toast.LENGTH_LONG).show();
                        }

                    }

                });*/

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to make Normal Student ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                try {
                                    corporate_type = "1";
                                    new updateCorporate().execute();

                                } catch (Exception ex) {
                                    Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
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
                alert.setTitle("Normal Student");
                alert.show();
                return true;
            }
        });

        myHolder.mos.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                prefEditor.putString("id", current.getSbd_id());
                prefEditor.commit();
                // Toast.makeText(context,"m"+current.getCorporate(),Toast.LENGTH_SHORT).show();
                // Create custom dialog object
                final Dialog dialog = new Dialog(context);
                // Include dialog.xml file
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_choise_option);
                final RadioGroup radioGroup;
                final RadioButton normal, corporate;

                dialog.show();
                radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup);
                normal = (RadioButton) dialog.findViewById(R.id.normal);
                corporate = (RadioButton) dialog.findViewById(R.id.corporate);

                if (current.getCorporate().equals("1")) {
                    normal.setChecked(true);
                    corporate_type = "1";
                }
                if (current.getCorporate().equals("2")) {
                    corporate.setChecked(true);
                    corporate_type = "2";
                }

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        // TODO Auto-generated method stub

                        // Method 1 For Getting Index of RadioButton
                        posr = radioGroup.indexOfChild(dialog.findViewById(checkedId));
                        switch (posr) {
                            case 1:

                                corporate_type = "2";
                                // Toast.makeText(context, corporate_type,Toast.LENGTH_SHORT).show();
                                break;

                            default:
                                //The default selection is RadioButton 1
                                corporate_type = "1";
                                //Toast.makeText(context, corporate_type,Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });


                Button declineButton = (Button) dialog.findViewById(R.id.placeBtn);
                // if decline button is clicked, close the custom dialog
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Close dialog
                        if (!corporate_type.equals("")) {
                            //sms sending
                            dialog.dismiss();
                            new updateCorporate().execute();
                            /*if (corporate_type == "2" || corporate_type == "4") {
                                prefEditor.putString("corporate_type", corporate_type);
                                prefEditor.commit();
                                StudentCorporateRegView studentCorporateRegView = new StudentCorporateRegView();
                                studentCorporateRegView.show(((FragmentActivity) context).getSupportFragmentManager(), "studentCorporateRegView");
                            } else {
                                //  Toast.makeText(context, "MOS", Toast.LENGTH_LONG).show();
                                new updateCorporate().execute();
                            }*/

                        } else {

                            Toast.makeText(context, "Please select Option!", Toast.LENGTH_LONG).show();
                        }

                    }

                });
                return true;
            }
        });
        myHolder.moscorporate.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                prefEditor.putString("id", current.getSbd_id());
                prefEditor.commit();
                //  Toast.makeText(context,"mc"+current.getCorporate(),Toast.LENGTH_SHORT).show();
                // Create custom dialog object
                final Dialog dialog = new Dialog(context);
                // Include dialog.xml file
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_choise_option);
                final RadioGroup radioGroup;
                final RadioButton normal, corporate;

                dialog.show();
                radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup);
                normal = (RadioButton) dialog.findViewById(R.id.normal);
                corporate = (RadioButton) dialog.findViewById(R.id.corporate);

                if (current.getCorporate().equals("1")) {
                    normal.setChecked(true);
                    corporate_type = "1";
                }
                if (current.getCorporate().equals("2")) {
                    corporate.setChecked(true);
                    corporate_type = "2";
                }

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        // TODO Auto-generated method stub

                        // Method 1 For Getting Index of RadioButton
                        posr = radioGroup.indexOfChild(dialog.findViewById(checkedId));
                        switch (posr) {
                            case 1:

                                corporate_type = "2";
                                // Toast.makeText(context, corporate_type,Toast.LENGTH_SHORT).show();
                                break;

                            default:
                                //The default selection is RadioButton 1
                                corporate_type = "1";
                                //Toast.makeText(context, corporate_type,Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });


                Button declineButton = (Button) dialog.findViewById(R.id.placeBtn);
                // if decline button is clicked, close the custom dialog
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Close dialog
                        if (!corporate_type.equals("")) {
                            //sms sending
                            dialog.dismiss();
                            new updateCorporate().execute();
                           /* if (corporate_type == "2" || corporate_type == "4") {
                                prefEditor.putString("corporate_type", corporate_type);
                                prefEditor.commit();
                                StudentCorporateRegView studentCorporateRegView = new StudentCorporateRegView();
                                studentCorporateRegView.show(((FragmentActivity) context).getSupportFragmentManager(), "studentCorporateRegView");
                            } else {
                                //  Toast.makeText(context, "MOS", Toast.LENGTH_LONG).show();
                                new updateCorporate().execute();
                            }*/

                        } else {

                            Toast.makeText(context, "Please select Option!", Toast.LENGTH_LONG).show();
                        }

                    }

                });
                return true;
            }
        });

        myHolder.notTransferStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Can not be transfer this student", Toast.LENGTH_SHORT).show();
            }
        });
        myHolder.nonClickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                prefEditor.putString("id", current.getSbd_id());
                prefEditor.commit();
                //Toast.makeText(context, "Can not be change status for this student", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to make Corporate Student ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                try {
                                    corporate_type = "2";
                                    new updateCorporate().execute();

                                } catch (Exception ex) {
                                    Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
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
                alert.setTitle("Corporate Student");
                alert.show();

            }
        });

        myHolder.guestFees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Fees not applicable for this student", Toast.LENGTH_SHORT).show();
            }
        });

        myHolder.takeComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                prefEditor.putString("user_id", current.getUser_id());
                prefEditor.putString("da", current.getStudent_Name());
                prefEditor.commit();
                MultipleCommentAddView commentAddView = new MultipleCommentAddView();
                commentAddView.show(((FragmentActivity) context).getSupportFragmentManager(), "commentAddView");
            }
        });
        myHolder.takeComment.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                if (current.getNotes().equals("null") || current.getNotes().equals("")) {
                    Toast.makeText(context, "No comments.", Toast.LENGTH_SHORT).show();
                } else {
                    prefEditor.putString("user_id", current.getUser_id());
                    prefEditor.putString("da", current.getStudents_Name());
                    prefEditor.commit();
                    CommentsDetailsView commentAddView = new CommentsDetailsView();
                    commentAddView.show(((FragmentActivity) context).getSupportFragmentManager(), "commentAddView");
                }

                return true;
            }
        });
        myHolder.notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);

                prefEditor.putString("user_id", current.getUser_id());
                prefEditor.putString("da", current.getStudents_Name());
                prefEditor.commit();
                CommentsDetailsView commentAddView = new CommentsDetailsView();
                commentAddView.show(((FragmentActivity) context).getSupportFragmentManager(), "commentAddView");
            }
        });
        myHolder.messageCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                prefEditor.putString("user_id", current.getUser_id());
                prefEditor.putString("da", current.getStudents_Name());
                prefEditor.putString("r_b_id", current.getBatchid());
                prefEditor.commit();
                FeesReminderDetailsView feesReminderDetailsView = new FeesReminderDetailsView();
                feesReminderDetailsView.show(((FragmentActivity) context).getSupportFragmentManager(), "feesReminderDetailsView");
            }
        });


    }


    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    public void setUndoOn(boolean undoOn) {
        this.undoOn = undoOn;
    }

    public boolean isUndoOn() {
        return undoOn;
    }

    public void pendingRemoval(int position) {
        current = data.get(position);
        if (!itemsPendingRemoval.contains(current)) {
            itemsPendingRemoval.add(current);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {

                    remove(data.indexOf(current));

                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(current, pendingRemovalRunnable);
        }
    }

    public void remove(int position) {
        current = data.get(position);
        id = current.getSbd_id();
        ID = position;
        //  Toast.makeText(context, "Remove id" + id, Toast.LENGTH_LONG).show();

        if (itemsPendingRemoval.contains(current)) {
            itemsPendingRemoval.remove(current);
        }
        if (data.contains(current)) {
            data.remove(position);
            notifyItemRemoved(position);
        }
        new deleteDisContinue().execute();
    }

    public boolean isPendingRemoval(int position) {
        current = data.get(position);
        return itemsPendingRemoval.contains(current);
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView name, mobile_no, numbers, mailid, totalFees, notes, guestFees, messageCount, hidemessageCount, dueDate;
        TextView mail_id, previousAttendance;
        ImageView normalButton, noncorporateButton, corporateButton, mos, moscorporate, aButton, pButton, callingButton, messageButton, whatsappeButton, feesButton, locationButton, coursesButton, takeEditNotes, takeEditNotNotes, takeViewInfo, takeViewDustbean, takeViewContinue, takeNotViewInfo, transferStudents, nonClickButton, notTransferStudents, takeComment, addButton, takeDeleteStudent, takeViewInfo1;
        public CheckBox chkSelected;
        LinearLayout notes1, clickForShowBtn, lbtn1, layoutBackChange, middleHide;


        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            // mail_id = (TextView) itemView.findViewById(R.id.mail_id);
            callingButton = (ImageView) itemView.findViewById(R.id.callingButton);
            guestFees = (TextView) itemView.findViewById(R.id.guestFees);

            messageButton = (ImageView) itemView.findViewById(R.id.messageButton);
            notes = (TextView) itemView.findViewById(R.id.notes);
            numbers = (TextView) itemView.findViewById(R.id.numbers);
            previousAttendance = (TextView) itemView.findViewById(R.id.previousAttendance);
            totalFees = (TextView) itemView.findViewById(R.id.totalFees);
            messageCount = (TextView) itemView.findViewById(R.id.messageCount);
            hidemessageCount = (TextView) itemView.findViewById(R.id.hidemessageCount);
            dueDate = (TextView) itemView.findViewById(R.id.dueDate);
            whatsappeButton = (ImageView) itemView.findViewById(R.id.whatsappeButton);
            feesButton = (ImageView) itemView.findViewById(R.id.feesButton);
            chkSelected = (CheckBox) itemView.findViewById(R.id.chkSelected);
            normalButton = (ImageView) itemView.findViewById(R.id.normalButton);
            noncorporateButton = (ImageView) itemView.findViewById(R.id.noncorporateButton);
            corporateButton = (ImageView) itemView.findViewById(R.id.corporateButton);
            mos = (ImageView) itemView.findViewById(R.id.mos);
            moscorporate = (ImageView) itemView.findViewById(R.id.moscorporate);
            addButton = (ImageView) itemView.findViewById(R.id.addButton);
            locationButton = (ImageView) itemView.findViewById(R.id.locationButton);
            coursesButton = (ImageView) itemView.findViewById(R.id.coursesButton);
            takeEditNotes = (ImageView) itemView.findViewById(R.id.takeEditNotes);
            takeEditNotNotes = (ImageView) itemView.findViewById(R.id.takeEditNotNotes);
            transferStudents = (ImageView) itemView.findViewById(R.id.transferStudents);
            takeViewInfo = (ImageView) itemView.findViewById(R.id.takeViewInfo);
            takeViewInfo1 = (ImageView) itemView.findViewById(R.id.takeViewInfo1);
            takeViewDustbean = (ImageView) itemView.findViewById(R.id.takeViewDustbean);
            takeViewContinue = (ImageView) itemView.findViewById(R.id.takeViewContinue);
            notes1 = (LinearLayout) itemView.findViewById(R.id.notes1);
            clickForShowBtn = (LinearLayout) itemView.findViewById(R.id.clickForShowBtn);
            lbtn1 = (LinearLayout) itemView.findViewById(R.id.lbtn1);
            takeNotViewInfo = (ImageView) itemView.findViewById(R.id.takeNotViewInfo);
            nonClickButton = (ImageView) itemView.findViewById(R.id.nonClickButton);
            takeComment = (ImageView) itemView.findViewById(R.id.takeComment);
            notTransferStudents = (ImageView) itemView.findViewById(R.id.notTransferStudents);
            takeDeleteStudent = (ImageView) itemView.findViewById(R.id.takeDeleteStudent);
            layoutBackChange = (LinearLayout) itemView.findViewById(R.id.layoutBackChange);
            middleHide = (LinearLayout) itemView.findViewById(R.id.middleHide);
        }

    }

    // method to access in activity after updating selection
    public List<StudentsDAO> getSservicelist() {
        return data;
    }

    private class submitData extends AsyncTask<Void, Void, Void> {
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
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            final String date = format.format(cal.getTime());
            jsonLeadObj = new JSONObject() {
                {
                    try {
                        put("batch_id", attendenceBatchId);
                        put("user_id", attendenceStudentId);
                        put("batch_date", date);
                        put("present", "1");

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();


            Log.i("json", "json" + jsonLeadObj);
            centerListResponse = serviceAccess.SendHttpPost(Config.URL_TAKEATTENDENCEBYBTACH, jsonLeadObj);
            Log.i("resp", "centerListResponse" + centerListResponse);


            if (centerListResponse.compareTo("") != 0) {
                if (isJSONValid(centerListResponse)) {


                    try {

                        JSONObject jsonObject = new JSONObject(centerListResponse);
                        status = jsonObject.getBoolean("status");
                        msg = jsonObject.getString("message");
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                } else {


                    Toast.makeText(context, "Please check your webservice", Toast.LENGTH_LONG).show();


                }
            } else {

                Toast.makeText(context, "Please check your network connection.", Toast.LENGTH_LONG).show();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (status) {

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Calendar cal = Calendar.getInstance();
                final String date = format.format(cal.getTime());
                prefEditor.putString("a_student_id" + attendenceStudentId, attendenceStudentId);
                prefEditor.putString("a_batchId" + attendenceBatchId, attendenceBatchId);
                prefEditor.putString("attendence" + attendenceStudentId, attendencePresent);
                prefEditor.putString("date" + date, date);
                prefEditor.commit();
                swap(ID);

                // Close the progressdialog
                mProgressDialog.dismiss();
            } else {
                // Close the progressdialog
                mProgressDialog.dismiss();

            }
        }
    }

    public void swap(int position) {
        notifyItemChanged(position);

    }

    //
    private class deleteDisContinue extends AsyncTask<Void, Void, Void> {
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
                        put("BatchID", BatchID);
                        put("UserID", UserID);
                        put("discontinue_reason", discontinue_reason);


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };

            WebClient serviceAccess = new WebClient();
            Log.i("json", "json" + jsonLeadObj);
            centerListResponse = serviceAccess.SendHttpPost(Config.URL_DISCONTINUEBATCHSTUDENT, jsonLeadObj);
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
            if (status) {
                //  removeAt(ID);
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                // Close the progressdialog
                mListener.messageReceived(message);
                mProgressDialog.dismiss();


            } else {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                // Close the progressdialog
                mProgressDialog.dismiss();

            }
            // Close the progressdialog
            mProgressDialog.dismiss();
        }
    }
    //


    private class updateCorporate extends AsyncTask<Void, Void, Void> {
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
                        put("student_batch_cat", corporate_type);
                        put("id", preferences.getString("id", ""));


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };

            WebClient serviceAccess = new WebClient();


            Log.i("json", "json" + jsonLeadObj);
            centerListResponse = serviceAccess.SendHttpPost(Config.URL_UPDATEMOSSTUDENT, jsonLeadObj);
            Log.i("resp", "leadListResponse" + centerListResponse);


            if (centerListResponse.compareTo("") != 0) {
                if (isJSONValid(centerListResponse)) {

                    try {

                        JSONObject jObject = new JSONObject(centerListResponse);
                        status = jObject.getBoolean("status");
                        message = jObject.getString("message");

                        jsonarray = new JSONArray(centerListResponse);

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
            if (status) {
                //  removeAt(ID);
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                // Close the progressdialog
                mListener.messageReceived(message);
                mProgressDialog.dismiss();


            } else {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                // Close the progressdialog
                mProgressDialog.dismiss();

            }
            // Close the progressdialog
            mProgressDialog.dismiss();
        }
    }

    private class getcashback extends AsyncTask<Void, Void, Void> {
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
                        put("user_id", user_id);
                        put("batch_no", batch_id);


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };

            WebClient serviceAccess = new WebClient();


            Log.i("json", "json" + jsonLeadObj);
            centerListResponse = serviceAccess.SendHttpPost(Config.URL_GETSTUDENTSCASHBACK, jsonLeadObj);
            Log.i("resp", "leadListResponse" + centerListResponse);
            prefEditor.putString("cashBackAmount", "");
            prefEditor.commit();

            if (centerListResponse.compareTo("") != 0) {
                if (isJSONValid(centerListResponse)) {

                    try {

                        JSONArray leadJsonObj = new JSONArray(centerListResponse);
                        for (int i = 0; i < leadJsonObj.length(); i++) {
                            JSONObject json_data = leadJsonObj.getJSONObject(i);
                            prefEditor.putString("cashBackAmount", json_data.getString("CashbackAmount"));
                            prefEditor.putString("cashBackid", json_data.getString("id"));
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


            // StudentFeesEntryView studentFeesEntryView = new StudentFeesEntryView();
            //  studentFeesEntryView.show(((FragmentActivity) context).getSupportFragmentManager(), "studentFeesEntryView");


            // Close the progressdialog
            mProgressDialog.dismiss();
            new initCallbackCount().execute();
        }
    }

    private class initCallbackCount extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
          /*  mProgressDialog = new ProgressDialog(context);
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();*/
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj1 = new JSONObject() {
                {
                    try {
                        put("user_id", user_id);
                        put("batch_no", batch_id);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();

            //  String baseURL = "http://192.168.1.13:8088/lms/api/lead/showlead";
            Log.i("json", "json" + jsonLeadObj);
            feesstatusResponse = serviceAccess.SendHttpPost(Config.URL_GETFULLFEESSTATUS, jsonLeadObj1);
            Log.i("resp", "feesstatusResponse" + feesstatusResponse);

            if (feesstatusResponse.compareTo("") != 0) {
                if (isJSONValid(feesstatusResponse)) {


                    try {


                        JSONObject jsonObject = new JSONObject(feesstatusResponse);
                        fees_status = jsonObject.getString("feesstatus");
                        f_status = jsonObject.getBoolean("status");


                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                } else {

                    Toast.makeText(context, "Please check your network connection", Toast.LENGTH_LONG).show();

                    return null;
                }
            } else {

                Toast.makeText(context, "Please check your network connection.", Toast.LENGTH_LONG).show();

                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (f_status) {
                //mProgressDialog.dismiss();


                Toast.makeText(context, "Full Fees paid.", Toast.LENGTH_LONG).show();


            } else {
                StudentFeesEntryView studentFeesEntryView = new StudentFeesEntryView();
                studentFeesEntryView.show(((FragmentActivity) context).getSupportFragmentManager(), "studentFeesEntryView");

            }
        }
    }

    //
    private class deleteStudent extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(context);
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Deleting...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {
                        put("id", id);


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };

            WebClient serviceAccess = new WebClient();


            Log.i("json", "json" + jsonLeadObj);
            centerListResponse = serviceAccess.SendHttpPost(Config.URL_DELETESTUDENTBYADMIN, jsonLeadObj);
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
                mListener.messageReceived(message);

            } else {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();


            }

        }
    }

    //
    private class addNotes extends AsyncTask<Void, Void, Void> {
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
                        put("notes_id", "1");
                        put("id", id);


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };

            WebClient serviceAccess = new WebClient();


            Log.i("json", "json" + jsonLeadObj);
            centerListResponse = serviceAccess.SendHttpPost(Config.URL_DISCONTINUENOTESSTUDENT, jsonLeadObj);
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
            if (status) {
                //  removeAt(ID);
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                // Close the progressdialog
                mProgressDialog.dismiss();
                mListener.messageReceived(message);


            } else {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                // Close the progressdialog
                mProgressDialog.dismiss();

            }
            // Close the progressdialog
            mProgressDialog.dismiss();
        }
    }
    //

    private class continueStudent extends AsyncTask<Void, Void, Void> {
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
                        put("BatchID", batch_id);
                        put("UserID", user_id);
                        put("FeesApplicable", "");
                        put("notes_id", notes_id);


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
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                } else {


                    Toast.makeText(context, "Please check your webservice", Toast.LENGTH_LONG).show();


                }
            } else {

                Toast.makeText(context, "Please check your network connection.", Toast.LENGTH_LONG).show();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (status) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                mListener.messageReceived(message);
                // Close the progressdialog
                mProgressDialog.dismiss();

            } else {
                // Close the progressdialog
                mProgressDialog.dismiss();

            }
        }
    }

    private class updateDemoStudent extends AsyncTask<Void, Void, Void> {
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
                        put("BatchID", batch_id);
                        put("UserID", user_id);
                        put("FeesApplicable", (4 - Integer.parseInt(FeesApplicable)));


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();


            Log.i("json", "json" + jsonLeadObj);
            addStudentRespone = serviceAccess.SendHttpPost(Config.URL_ADDSTUDENTFROMDEMOINBATCH, jsonLeadObj);
            Log.i("resp", "addStudentRespone" + addStudentRespone);


            if (addStudentRespone.compareTo("") != 0) {
                if (isJSONValid(addStudentRespone)) {


                    try {

                        JSONObject jsonObject = new JSONObject(addStudentRespone);
                        status = jsonObject.getBoolean("status");
                        msg = jsonObject.getString("message");
                        studentFeedBackCode = jsonObject.getString("feedback_student_code");
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                } else {


                    Toast.makeText(context, "Please check your webservice", Toast.LENGTH_LONG).show();


                }
            } else {

                Toast.makeText(context, "Please check your network connection.", Toast.LENGTH_LONG).show();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (status) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                String msg = "Hi " + name + System.getProperty("line.separator") + System.getProperty("line.separator")
                        + "Congratulations on Joining Course for " + preferences.getString("course_name", "") + "!" + System.getProperty("line.separator") + System.getProperty("line.separator")
                        + "Please save the below link where you can give feedback regarding your ongoing batch no " + batch_id + "." + System.getProperty("line.separator") + System.getProperty("line.separator")
                        + "https://afckstechnologies.in/fb_text/" + studentFeedBackCode + System.getProperty("line.separator") + System.getProperty("line.separator") + "WhatsApp Group Link: " + preferences.getString("wa_link", "");
                String result = sendSms1(mobile_no, msg);
                Log.d("sent sms---->", result);
                mListener.messageReceived(message);
                // Close the progressdialog
                mProgressDialog.dismiss();

            } else {
                // Close the progressdialog
                mProgressDialog.dismiss();

            }
        }
    }

    public static void bindListener(FeesListener listener) {
        mListener = listener;
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

    public String getContactDetails(String phoneNumber1) {
        String searchNumber = phoneNumber1;
        String phoneNumber = "", emailAddress = "", name = "";
        StringBuffer sb = new StringBuffer();
        // Cursor c =  getContentResolver().query(contactData, null, null, null, null);
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(searchNumber));
        Cursor c = context.getContentResolver().query(uri, null, null, null, null);
        if (c.moveToFirst()) {


            name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
            //http://stackoverflow.com/questions/866769/how-to-call-android-contacts-list   our upvoted answer

            String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

            if (hasPhone.equalsIgnoreCase("1"))
                hasPhone = "true";
            else
                hasPhone = "false";

            if (Boolean.parseBoolean(hasPhone)) {
                Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                while (phones.moveToNext()) {
                    phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                phones.close();
            }

            // Find Email Addresses
            Cursor emails = context.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId, null, null);
            while (emails.moveToNext()) {
                emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            }
            emails.close();

            //mainActivity.onBackPressed();
            // Toast.makeText(mainactivity, "go go go", Toast.LENGTH_SHORT).show();

            //  tvname.setText("Name: "+name);
            //tvphone.setText("Phone: "+phoneNumber);
            //tvmail.setText("Email: "+emailAddress);

            sb.append("\nUser Name:--- " + name + " \nCall Type:--- "
                    + " \nMobile Number:--- " + phoneNumber
                    + " \nEmail Id:--- " + emailAddress);
            sb.append("\n----------------------------------");


// add elements to al, including duplicates


            Log.d("curs", name + " num" + phoneNumber + " " + "mail" + emailAddress);
        }
        c.close();
        return name;
    }

/////

    private void storeDialogStatus(boolean isChecked) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences("CheckItem", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putBoolean("item", isChecked);
        mEditor.apply();
    }

    private boolean getDialogStatus() {
        SharedPreferences mSharedPreferences = context.getSharedPreferences("CheckItem", Context.MODE_PRIVATE);
        return mSharedPreferences.getBoolean("item", false);
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

}
