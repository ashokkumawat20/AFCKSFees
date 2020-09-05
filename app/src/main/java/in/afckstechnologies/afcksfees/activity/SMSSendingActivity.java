package in.afckstechnologies.afcksfees.activity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import in.afckstechnologies.afcksfees.R;
import in.afckstechnologies.afcksfees.utils.AppStatus;
import in.afckstechnologies.afcksfees.utils.Constant;

public class SMSSendingActivity extends AppCompatActivity {
    Button sendSMSBtn;
    EditText mobileNoEdtTxt, smsEdtTxt;
    String sms_type = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smssending);
        Intent intent = getIntent();
        sendSMSBtn = (Button) findViewById(R.id.sendSMSBtn);
        mobileNoEdtTxt = (EditText) findViewById(R.id.mobileNoEdtTxt);
        mobileNoEdtTxt.setText(intent.getStringExtra("mobile_no"));
        sms_type = intent.getStringExtra("sms_type");
        smsEdtTxt = (EditText) findViewById(R.id.smsEdtTxt);
        sendSMSBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    String phoneNo = mobileNoEdtTxt.getText().toString().trim();
                    String msg = smsEdtTxt.getText().toString().trim();

                    if (validate(phoneNo, msg)) {
                        if (sms_type.equals("Mobile_SMS")) {
                            sendSMS(phoneNo, msg);
                        } else if (sms_type.equals("AFCKST")) {
                            Toast.makeText(getApplicationContext(), "coming soon....", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {

                    Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage().toString(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public boolean validate(String phoneno, String msg) {
        boolean isValidate = false;
        if (phoneno.trim().compareTo("") == 0) {
            Toast.makeText(getApplicationContext(), "Please enter mobile No.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (phoneno.trim().compareTo("") == 0 || phoneno.length() != 10) {
            Toast.makeText(getApplicationContext(), "Please enter a 10 digit valid Mobile No.", Toast.LENGTH_LONG).show();
            isValidate = false;
        }
        if (msg.trim().compareTo("") == 0) {
            Toast.makeText(getApplicationContext(), "Please enter message", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else {
            isValidate = true;
        }
        return isValidate;
    }
}
