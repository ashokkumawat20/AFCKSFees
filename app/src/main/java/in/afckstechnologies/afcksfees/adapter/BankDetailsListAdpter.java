package in.afckstechnologies.afcksfees.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import in.afckstechnologies.afcksfees.R;
import in.afckstechnologies.afcksfees.model.BankingdetailsDAO;
import in.afckstechnologies.afcksfees.model.StudentsDAO;
import in.afckstechnologies.afcksfees.utils.Config;
import in.afckstechnologies.afcksfees.utils.SmsListener;
import in.afckstechnologies.afcksfees.utils.WebClient;
import in.afckstechnologies.afcksfees.view.FeesTypeViewForAdmin;


public class BankDetailsListAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<BankingdetailsDAO> data;
    BankingdetailsDAO current;
    int currentPos = 0;
    String id, bid;
    String centerId;
    int ID;
    int number = 1;
    int postition1;
    ProgressDialog mProgressDialog;
    private JSONObject jsonLeadObj;
    JSONArray jsonArray;
    String centerListResponse = "";
    boolean status;
    String message = "";
    String msg = "", flag = "";

    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    public static SmsListener mListener;

    // create constructor to innitilize context and data sent from MainActivity
    public BankDetailsListAdpter(Context context, List<BankingdetailsDAO> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        preferences = context.getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_banking_details, parent, false);
        MyHolder holder = new MyHolder(view);

        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final int pos = position;
        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder = (MyHolder) holder;
        current = data.get(position);
        myHolder.sno.setText(current.getNumbers());
        myHolder.sno.setTag(position);
        myHolder.trans_date.setText(formateDateFromstring("yyyy-MM-dd", "dd-MMM-yyyy", current.getTrans_date()));
        myHolder.trans_date.setTag(position);
        myHolder.trans_type.setText(current.getTrans_type());
        myHolder.trans_type.setTag(position);
        myHolder.trans_ref.setText(current.getTrans_ref());
        myHolder.trans_ref.setTag(position);
        myHolder.rec_amount.setText(current.getRec_amount());
        myHolder.rec_amount.setTag(position);
        myHolder.adj_amount.setText(current.getAdj_amount());
        myHolder.adj_amount.setTag(position);
        myHolder.deleteRaw.setTag(position);
        myHolder.chkSelected.setTag(position);
        myHolder.chkSelected.setTag(data.get(position));
        if (current.getStatus().equals("1")) {
            myHolder.chkSelected.setChecked(true);
        } else {
            myHolder.chkSelected.setChecked(false);

        }

        if (current.getStatus().equals("1")) {

            myHolder.layoutBackChange.setBackgroundColor(Color.parseColor("#556199"));
            myHolder.chkSelected.setVisibility(View.GONE);

        } else {
            myHolder.chkSelected.setVisibility(View.VISIBLE);
            myHolder.layoutBackChange.setBackgroundColor(Color.parseColor("#800000"));

        }
        if (preferences.getString("s_delete", "").equals("1")) {
            myHolder.deleteRaw.setVisibility(View.VISIBLE);
            if (current.getStatus().equals("1")) {
                myHolder.chkSelected.setVisibility(View.GONE);

            } else {
                myHolder.chkSelected.setVisibility(View.VISIBLE);
            }
        } else {
            myHolder.deleteRaw.setVisibility(View.GONE);
            myHolder.chkSelected.setVisibility(View.GONE);
        }
        myHolder.chkSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                BankingdetailsDAO contact = (BankingdetailsDAO) cb.getTag();

                if (cb.isChecked()) {
                    prefEditor.putString("bid",contact.getId());
                    prefEditor.commit();
                    FeesTypeViewForAdmin feesTypeViewForAdmin = new FeesTypeViewForAdmin();
                    feesTypeViewForAdmin.show(((FragmentActivity) context).getSupportFragmentManager(), "feesTypeViewForAdmin");

                }
                // Toast.makeText(v.getContext(), "Clicked on Checkbox: " + contact.getId() + " is " + cb.isChecked(), Toast.LENGTH_LONG).show();

            }
        });
        myHolder.deleteRaw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                id = current.getId();

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to delete ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                                new deleteSale().execute();
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
                alert.setTitle("Deleting Bank details");
                alert.show();
            }
        });

    }


    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView sno, trans_date, trans_type, trans_ref, rec_amount, adj_amount;
        ImageView deleteRaw;
        LinearLayout layoutBackChange;
        public CheckBox chkSelected;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            sno = (TextView) itemView.findViewById(R.id.sno);
            trans_date = (TextView) itemView.findViewById(R.id.trans_date);
            trans_type = (TextView) itemView.findViewById(R.id.trans_type);
            trans_ref = (TextView) itemView.findViewById(R.id.trans_ref);
            rec_amount = (TextView) itemView.findViewById(R.id.rec_amount);
            adj_amount = (TextView) itemView.findViewById(R.id.adj_amount);
            deleteRaw = (ImageView) itemView.findViewById(R.id.deleteRaw);
            layoutBackChange = (LinearLayout) itemView.findViewById(R.id.layoutBackChange);
            chkSelected = (CheckBox) itemView.findViewById(R.id.chkSelected);
        }

    }

    // method to access in activity after updating selection
    public List<BankingdetailsDAO> getSservicelist() {
        return data;
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
                        put("id", id);


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };

            WebClient serviceAccess = new WebClient();


            Log.i("json", "json" + jsonLeadObj);
            centerListResponse = serviceAccess.SendHttpPost(Config.URL_DELETEBANKDETAILS, jsonLeadObj);
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
                // Close the progressdialog
                mListener.messageReceived(message);

                mProgressDialog.dismiss();


            } else {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                // Close the progressdialog
                mListener.messageReceived(message);
                mProgressDialog.dismiss();

            }
            // Close the progressdialog
            mProgressDialog.dismiss();
        }
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


    public static void bindListener(SmsListener listener) {
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


}
