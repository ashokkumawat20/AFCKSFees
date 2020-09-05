package in.afckstechnologies.afcksfees.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;

import android.telephony.SmsManager;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import in.afckstechnologies.afcksfees.MailAPI.GMailSender1;
import in.afckstechnologies.afcksfees.R;
import in.afckstechnologies.afcksfees.activity.UpdateTemplateContactsActivity;
import in.afckstechnologies.afcksfees.model.TemplatesContactDAO;
import in.afckstechnologies.afcksfees.utils.Config;
import in.afckstechnologies.afcksfees.utils.WebClient;


/**
 * Created by admin on 3/18/2017.
 */

public class TemplateListAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<TemplatesContactDAO> data;
    TemplatesContactDAO current;
    int currentPos = 0;
    String id, id1;
    String centerId;
    int ID;

    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    String sms_type = "";
    private static String username = ""; //"info@afcks.com";
    private static String password = "";//"at!@#123";
    GMailSender1 sender;

    String subject = "";

    String emailid = "";

    ProgressDialog mProgressDialog;
    private JSONObject jsonLeadObj;
    JSONArray jsonArray;
    String centerListResponse = "";
    boolean status;
    String message = "";
    String message_body = "";
    String msg = "";
    static String sms_user = "";
    static String sms_pass = "";

    // create constructor to innitilize context and data sent from MainActivity
    public TemplateListAdpter(Context context, List<TemplatesContactDAO> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        preferences = context.getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();

        sms_user = preferences.getString("sms_username", "");
        sms_pass = preferences.getString("sms_password", "");

        username = preferences.getString("mail_username", "");
        password = preferences.getString("mail_password", "");
        sender = new GMailSender1(username, password);
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_contacts_template_details, parent, false);
        final TemplateListAdpter.MyHolder holder = new TemplateListAdpter.MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final int pos = position;


        // Get current position of item in recyclerview to bind data and assign values from list
        final TemplateListAdpter.MyHolder myHolder = (TemplateListAdpter.MyHolder) holder;
        current = data.get(position);
        myHolder.view_subject.setText(current.getSubject());
        myHolder.view_subject.setTag(position);

        myHolder.modifyButton.setTag(position);
        myHolder.copyButton.setTag(position);
        myHolder.deleteButton.setTag(position);
        myHolder.layoutColor.setTag(position);

        myHolder.view_subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);

                if (preferences.getString("temp_type_sms", "").equals("1")) {
                    //  Toast.makeText(context, "Hello", Toast.LENGTH_LONG).show();
                    myHolder.layoutColor.setBackgroundColor(Color.parseColor("#70AD47"));
                    current.setSelected(true);
                    final CharSequence[] items = {" AFCKST ", " Mobile SMS ", " WhatsApp ", " SMS+WhatsApp ", " AFCKST+WhatsApp ", " Mail "};
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
                                        case 2:
                                            // Your code when 2nd  option seletced
                                            sms_type = "Whatsapp_SMS";
                                            break;
                                        case 3:
                                            // Your code when 2nd  option seletced
                                            sms_type = "SMSWhatsapp_SMS";
                                            break;
                                        case 4:
                                            // Your code when 2nd  option seletced
                                            sms_type = "AFCKSTWhatsapp_SMS";
                                            break;
                                        case 5:
                                            // Your code when 2nd  option seletced
                                            sms_type = "mail";
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
                                        if (sms_type.equals("Mobile_SMS")) {
                                            sendSMS(preferences.getString("student_mob_sms", ""), "Hi " + preferences.getString("st_first_name", "") + "," + "\n\n" + current.getTemplate_Text());


                                        } else if (sms_type.equals("AFCKST")) {
                                            Log.d("msg", preferences.getString("student_mob_sms", "") + "  " + message);
                                            System.out.println("message--->" + message);
                                            String result = sendSms1(preferences.getString("student_mob_sms", ""), "Hi " + preferences.getString("st_first_name", "") + "," + "\n\n" + current.getTemplate_Text());
                                            System.out.println("result :" + result);

                                        } else if (sms_type.equals("Whatsapp_SMS")) {
                                            int sdk = android.os.Build.VERSION.SDK_INT;
                                            if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                                                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                                clipboard.setText("Hi " + preferences.getString("st_first_name", "") + "," + "\n\n" + current.getTemplate_Text());
                                            } else {
                                                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                                android.content.ClipData clip = android.content.ClipData.newPlainText("myLabel", "Hi " + preferences.getString("st_first_name", "") + "," + "\n\n" + current.getTemplate_Text());
                                                clipboard.setPrimaryClip(clip);
                                            }


                                           /* Log.i("call ph no", "" + preferences.getString("student_mob_sms", ""));
                                            try {
                                                Uri uri = Uri.parse("smsto:" + preferences.getString("student_mob_sms", ""));
                                                Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
                                                sendIntent.setPackage("com.whatsapp");
                                                context.startActivity(sendIntent);
                                            } catch (Exception e) {
                                                Toast.makeText(context, "Error" + e.toString(), Toast.LENGTH_SHORT).show();
                                            }*/

                                            PackageManager packageManager = context.getPackageManager();
                                            Intent i = new Intent(Intent.ACTION_VIEW);
                                            try {
                                                String url = "https://api.whatsapp.com/send?phone=" + "91" + preferences.getString("student_mob_sms", "") + "&text=" + URLEncoder.encode("Hi " + preferences.getString("st_first_name", "") + "," + "\n\n" + current.getTemplate_Text(), "UTF-8");
                                                i.setPackage("com.whatsapp");
                                                i.setData(Uri.parse(url));
                                                if (i.resolveActivity(packageManager) != null) {
                                                    context.startActivity(i);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


                                        } else if (sms_type.equals("SMSWhatsapp_SMS")) {
                                            sendSMS(preferences.getString("student_mob_sms", ""), "Hi " + preferences.getString("st_first_name", "") + "," + "\n\n" + current.getTemplate_Text());
                                            int sdk = android.os.Build.VERSION.SDK_INT;
                                            if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                                                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                                clipboard.setText("Hi " + preferences.getString("st_first_name", "") + "," + "\n\n" + current.getTemplate_Text());
                                            } else {
                                                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                                android.content.ClipData clip = android.content.ClipData.newPlainText("myLabel", "Hi " + preferences.getString("st_first_name", "") + "," + "\n\n" + current.getTemplate_Text());
                                                clipboard.setPrimaryClip(clip);
                                            }


                                           /* Log.i("call ph no", "" + preferences.getString("student_mob_sms", ""));
                                            try {
                                                Uri uri = Uri.parse("smsto:" + preferences.getString("student_mob_sms", ""));
                                                Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
                                                sendIntent.setPackage("com.whatsapp");
                                                context.startActivity(sendIntent);
                                            } catch (Exception e) {
                                                Toast.makeText(context, "Error" + e.toString(), Toast.LENGTH_SHORT).show();
                                            }*/
                                            PackageManager packageManager = context.getPackageManager();
                                            Intent i = new Intent(Intent.ACTION_VIEW);
                                            try {
                                                String url = "https://api.whatsapp.com/send?phone=" + "91" + preferences.getString("student_mob_sms", "") + "&text=" + URLEncoder.encode("Hi " + preferences.getString("st_first_name", "") + "," + "\n\n" + current.getTemplate_Text(), "UTF-8");
                                                i.setPackage("com.whatsapp");
                                                i.setData(Uri.parse(url));
                                                if (i.resolveActivity(packageManager) != null) {
                                                    context.startActivity(i);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


                                        } else if (sms_type.equals("AFCKSTWhatsapp_SMS")) {
                                            String result = sendSms1(preferences.getString("student_mob_sms", ""), "Hi " + preferences.getString("st_first_name", "") + "," + "\n\n" + current.getTemplate_Text());
                                            System.out.println("result :" + result);
                                            int sdk = android.os.Build.VERSION.SDK_INT;
                                            if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                                                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                                clipboard.setText("Hi " + preferences.getString("st_first_name", "") + "," + "\n\n" + current.getTemplate_Text());
                                            } else {
                                                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                                android.content.ClipData clip = android.content.ClipData.newPlainText("myLabel", "Hi " + preferences.getString("st_first_name", "") + "," + "\n\n" + current.getTemplate_Text());
                                                clipboard.setPrimaryClip(clip);
                                            }


                                          /*  Log.i("call ph no", "" + preferences.getString("student_mob_sms", ""));
                                            try {
                                                Uri uri = Uri.parse("smsto:" + preferences.getString("student_mob_sms", ""));
                                                Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
                                                sendIntent.setPackage("com.whatsapp");
                                                context.startActivity(sendIntent);
                                            } catch (Exception e) {
                                                Toast.makeText(context, "Error" + e.toString(), Toast.LENGTH_SHORT).show();
                                            }*/

                                            PackageManager packageManager = context.getPackageManager();
                                            Intent i = new Intent(Intent.ACTION_VIEW);
                                            try {
                                                String url = "https://api.whatsapp.com/send?phone=" + "91" + preferences.getString("student_mob_sms", "") + "&text=" + URLEncoder.encode("Hi " + preferences.getString("st_first_name", "") + "," + "\n\n" + current.getTemplate_Text(), "UTF-8");
                                                i.setPackage("com.whatsapp");
                                                i.setData(Uri.parse(url));
                                                if (i.resolveActivity(packageManager) != null) {
                                                    context.startActivity(i);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


                                        } else if (sms_type.equals("mail")) {
                                            subject = current.getSubject();
                                            message_body = "Hi " + preferences.getString("st_first_name", "") + "," + "\n\n" + current.getTemplate_Text();
                                            emailid = preferences.getString("student_mail_id", "");
                                            if (!emailid.equals("")) {
                                                new MyAsyncClass().execute();
                                            } else {
                                                Toast.makeText(context, "Does not exist mail id", Toast.LENGTH_SHORT).show();
                                            }


                                        }

                                    } else {

                                        Toast.makeText(context, "Please select SMS Mode!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Your code when user clicked on Cancel
                                    myHolder.layoutColor.setBackgroundColor(Color.parseColor("#556199"));
                                    current.setSelected(false);
                                }
                            }).create();
                    dialog.show();
                }

            }
        });

        if (current.isSelected() == true) {
            myHolder.layoutColor.setBackgroundColor(Color.parseColor("#70AD47"));
        } else {
            myHolder.layoutColor.setBackgroundColor(Color.parseColor("#556199"));
        }
        myHolder.modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                Intent intent = new Intent(context, UpdateTemplateContactsActivity.class);
                prefEditor.putString("contact_ID", current.getID());
                prefEditor.putString("contact_Subject", current.getSubject());
                prefEditor.putString("contact_Tag", current.getTag());
                prefEditor.putString("contact_MsgData", current.getTemplate_Text());
                prefEditor.commit();
                context.startActivity(intent);
            }
        });
        myHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                id = current.getID();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to delete Template ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                try {

                                    new deleteSale().execute();

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
                alert.setTitle("Deleting Template");
                alert.show();

            }
        });
        myHolder.copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                Toast.makeText(context, "Copy", Toast.LENGTH_SHORT).show();
                String textToCopy = current.getTemplate_Text();
                int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(textToCopy);
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("myLabel", textToCopy);
                    clipboard.setPrimaryClip(clip);
                }
            }
        });

    }


    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {

        TextView view_subject;
        ImageView modifyButton, deleteButton, copyButton;
        LinearLayout layoutColor;
        private SparseBooleanArray selectedItems = new SparseBooleanArray();

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            view_subject = (TextView) itemView.findViewById(R.id.view_subject);
            modifyButton = (ImageView) itemView.findViewById(R.id.modifyButton);
            deleteButton = (ImageView) itemView.findViewById(R.id.deleteButton);
            copyButton = (ImageView) itemView.findViewById(R.id.copyButton);
            layoutColor = (LinearLayout) itemView.findViewById(R.id.lead_Layout);


        }


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

                        put("template_id", id);


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };

            WebClient serviceAccess = new WebClient();


            Log.i("json", "json" + jsonLeadObj);
            centerListResponse = serviceAccess.SendHttpPost(Config.URL_DELETECONTACTTEMPLATE, jsonLeadObj);
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
                removeAt(ID);
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

    public void removeAt(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, data.size());
    }


    class MyAsyncClass extends AsyncTask<Void, Void, Void> {


        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Please wait...");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {
                // Add subject, Body, your mail Id, and receiver mail Id.
                sender.sendMail(subject, message_body, username, emailid);
            } catch (Exception ex) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.cancel();

            Toast.makeText(context, "Email send", Toast.LENGTH_LONG).show();
        }


    }
}

