package in.afckstechnologies.afcksfees.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import in.afckstechnologies.afcksfees.R;
import in.afckstechnologies.afcksfees.model.CenterDAO;
import in.afckstechnologies.afcksfees.utils.Config;
import in.afckstechnologies.afcksfees.utils.FeesListener;
import in.afckstechnologies.afcksfees.utils.WebClient;


/**
 * Created by admin on 3/18/2017.
 */

public class CenterListAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<CenterDAO> data;
    CenterDAO current;
    int currentPos = 0;
    String id, id1;
    String centerId;
    int ID;

    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;

    ProgressDialog mProgressDialog;
    private JSONObject jsonLeadObj, jsonLeadObjSms;
    JSONArray jsonArray;
    String centerListResponse = "", templateSMSResponse = "";
    boolean status, status1;
    String message = "", message1 = "";
    String msg = "";
    String txt_msg = "";
    private static FeesListener mListener;
    int temp_sms_status = 0;

    // create constructor to innitilize context and data sent from MainActivity
    public CenterListAdpter(Context context, List<CenterDAO> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        preferences = context.getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_new_center_details, parent, false);
        CenterListAdpter.MyHolder holder = new CenterListAdpter.MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final int pos = position;
        // Get current position of item in recyclerview to bind data and assign values from list
        final CenterListAdpter.MyHolder myHolder = (CenterListAdpter.MyHolder) holder;
        current = data.get(position);
        myHolder.view_company_q.setText(current.getBranch_name());
        myHolder.view_company_q.setTag(position);
        myHolder.view_Address.setText(current.getAddress());
        myHolder.view_Address.setTag(position);
        myHolder.sendingSms.setTag(position);
        myHolder.sentSms.setTag(position);


        myHolder.chkSelected.setTag(position);
        myHolder.chkSelected.setChecked(data.get(position).isSelected());
        myHolder.chkSelected.setTag(data.get(position));
        if (current.getIsselected().equals("selected")) {
            myHolder.chkSelected.setChecked(true);
            Config.VALUE.add(data.get(pos).getId());
            // myHolder.removeCenter.setVisibility(View.VISIBLE);
        }

        myHolder.chkSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                CenterDAO contact = (CenterDAO) cb.getTag();

                contact.setSelected(cb.isChecked());
                data.get(pos).setSelected(cb.isChecked());

                // Toast.makeText(v.getContext(), "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked(), Toast.LENGTH_LONG).show();

                // Toast.makeText(v.getContext(), "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked()+data.get(pos).getId(), Toast.LENGTH_LONG).show();
                if (cb.isChecked()) {
                    //Toast.makeText(v.getContext(), "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked() + data.get(pos).getId(), Toast.LENGTH_LONG).show();
                    Config.VALUE.add(data.get(pos).getId());
                    id = data.get(pos).getId();
                    myHolder.chkSelected.setChecked(true);
                    new submitData().execute();
                } else if (!cb.isChecked()) {
                    // Toast.makeText(v.getContext(), "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked() + data.get(pos).getId(), Toast.LENGTH_LONG).show();
                    id1 = data.get(pos).getId();
                    myHolder.chkSelected.setChecked(false);
                    new deleteSale().execute();
                    Config.VALUE.remove(data.get(pos).getId());

                }


            }
        });
        if (current.isSelected()) {
            myHolder.sendingSms.setVisibility(View.GONE);
            myHolder.sentSms.setVisibility(View.VISIBLE);
        } else {
            myHolder.sendingSms.setVisibility(View.VISIBLE);
            myHolder.sentSms.setVisibility(View.GONE);
        }
        myHolder.sendingSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ID = (Integer) v.getTag();
                current = data.get(ID);
                //Toast.makeText(context, "id is " + current.getId(), Toast.LENGTH_LONG).show();
                id = current.getId();
                current.setSelected(true);
                myHolder.sendingSms.setVisibility(View.GONE);
                myHolder.sentSms.setVisibility(View.VISIBLE);

                temp_sms_status = 1;
                new submitData().execute();
            }
        });


    }


    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {

        TextView view_company_q;
        TextView view_Address;
        public CheckBox chkSelected;
        ImageView removeCenter, sendingSms, sentSms;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            view_company_q = (TextView) itemView.findViewById(R.id.view_company_q);
            view_Address = (TextView) itemView.findViewById(R.id.view_Address);
            sendingSms = (ImageView) itemView.findViewById(R.id.sendingSms);
            sentSms = (ImageView) itemView.findViewById(R.id.sentSms);
            chkSelected = (CheckBox) itemView.findViewById(R.id.chkSelected);


        }

    }

    // method to access in activity after updating selection
    public List<CenterDAO> getSservicelist() {
        return data;
    }


    //

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

            jsonLeadObj = new JSONObject() {
                {
                    try {
                        put("branch_id", id);
                        put("user_id", preferences.getString("user_id", ""));

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            jsonLeadObjSms = new JSONObject() {
                {
                    try {
                        put("location_map_id", id);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();


            Log.i("json", "json" + jsonLeadObj);
            centerListResponse = serviceAccess.SendHttpPost(Config.URL_SEND_LOCATION_DETAILS, jsonLeadObj);
            Log.i("resp", "centerListResponse" + centerListResponse);

            Log.i("json", "json" + jsonLeadObj);
            templateSMSResponse = serviceAccess.SendHttpPost(Config.URL_GETTEMPLATETEXTLOCATIONID, jsonLeadObjSms);
            Log.i("resp", "templateSMSResponse" + templateSMSResponse);
            if (isJSONValid(templateSMSResponse)) {


                try {

                    JSONObject jsonObject = new JSONObject(templateSMSResponse);
                    status1 = jsonObject.getBoolean("status");
                    message1 = jsonObject.getString("message");
                    txt_msg = jsonObject.getString("TemplateText");

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }

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
                prefEditor.putString("center_flag", "1");
                prefEditor.commit();
                // mListener.messageReceived(message);
                if (status1) {
                    if (temp_sms_status == 1) {
                        temp_sms_status = 0;
                        sendSms(txt_msg);
                    }

                }
                // Close the progressdialog
                mProgressDialog.dismiss();
            } else {
                // Close the progressdialog
                mProgressDialog.dismiss();

            }
        }
    }

    private void sendSms(String txt_msg) {
        //Toast.makeText(context, txt_msg, Toast.LENGTH_SHORT).show();
        sendSMS(preferences.getString("student_mob_sms", ""), "Hi " + preferences.getString("st_first_name", "") + "," + "\n\n" + txt_msg);

    }

    //
    private class deleteSale extends AsyncTask<Void, Void, Void> {
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
                        put("user_id", preferences.getString("user_id", ""));
                        put("branch_id", id1);


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };

            WebClient serviceAccess = new WebClient();


            Log.i("json", "json" + jsonLeadObj);
            centerListResponse = serviceAccess.SendHttpPost(Config.URL_DELETE_CENTER, jsonLeadObj);
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
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();

            }
            // Close the progressdialog
            mProgressDialog.dismiss();
        }
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

    public void removeAt(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, data.size());
    }

    public static void bindListener(FeesListener listener) {
        mListener = listener;
    }

    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            // smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            ArrayList<String> parts = smsManager.divideMessage(msg);
            smsManager.sendMultipartTextMessage(phoneNo, null, parts, null, null);

            Toast.makeText(context, "Message Sent", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage().toString(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
}
