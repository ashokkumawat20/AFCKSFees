package in.afckstechnologies.afcksfees.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;


public class Utils {

    private static ProgressDialog progress = null;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void hideSoftKeyboardOnRequestFocus(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public static void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    public static void showLoadingDialog(Context context) {
        //if (progress == null) {
        progress = new ProgressDialog(context);
        progress.setMessage("Please wait Syncing Data...");
        progress.setCancelable(true); // disable dismiss by tapping outside of the dialog
        progress.show();
        // }

    }

    public static void dismissLoadingDialog() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }

    public static boolean isValidEmail(EditText editText) {
        boolean valid = true;
        if (editText.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(editText.getText()).matches()) {
            valid = false;
        } else {
            editText.setError(null);
        }
        return valid;
    }

    public static boolean isValidateEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isUserNameValid(EditText editText) {
        boolean valid = true;
        if (editText.getText().toString().isEmpty() || editText.getText().length() < 3) {
            valid = false;
        } else {
            editText.setError(null);
        }
        return valid;
    }





    public static String getAddressFromLatLong(Double lat, Double lng) {
        String addressFromLatLong = null;

        String address = String.format(Locale.ENGLISH, "http://maps.googleapis.com/maps/api/geocode/json?latlng=%1$f,%2$f&sensor=true&language=" + Locale.getDefault().getCountry(), lat, lng);
        HttpGet httpGet = new HttpGet(address);

        HttpClient client = new DefaultHttpClient();

        HttpResponse response;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            response = client.execute(httpGet);

            HttpEntity entity = response.getEntity();

            InputStream stream = entity.getContent();

            int b;

            while ((b = stream.read()) != -1) {

                stringBuilder.append((char) b);

            }


            JSONObject jsonObject = new JSONObject(stringBuilder.toString());

            System.out.println("jsonObject-- ioutil->" + jsonObject.toString());
            JSONArray results = jsonObject.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject jsonObject1 = results.getJSONObject(0);

                addressFromLatLong = jsonObject1.getString("formatted_address");

            }

        } catch (Exception ex) {
        }


        return addressFromLatLong;

    }

    public static JSONObject getLocationInfo(String address) {

        HttpGet httpGet = null;
        try {
            httpGet = new HttpGet("http://maps.google."
                    + "com/maps/api/geocode/json?address=" + URLEncoder.encode(address, "UTF-8")
                    + "ka&sensor=false");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {

        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static String getLatLong(JSONObject jsonObject) {

        Double lon = new Double(0);
        Double lat = new Double(0);
        String latlong = null;

        try {

            lon = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

            latlong = lat + "," + lon;


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return latlong;

    }


    public static String convertArrayListToStringWithComma(ArrayList<String> arrayList) {

        //The string builder used to construct the string
        StringBuilder commaSepValueBuilder = new StringBuilder();

        //Looping through the list
        for (int i = 0; i < arrayList.size(); i++) {
            //append the value into the builder
            commaSepValueBuilder.append(arrayList.get(i));

            //if the value is not the last element of the list
            //then append the comma(,) as well
            if (i != arrayList.size() - 1) {
                commaSepValueBuilder.append(",");
            }
        }

        return String.valueOf(commaSepValueBuilder);
    }

    public static String convertArrayListToStringWithHash(ArrayList<String> arrayList) {

        //The string builder used to construct the string
        StringBuilder commaSepValueBuilder = new StringBuilder();

        //Looping through the list
        for (int i = 0; i < arrayList.size(); i++) {
            //append the value into the builder
            commaSepValueBuilder.append(arrayList.get(i));

            //if the value is not the last element of the list
            //then append the comma(,) as well
            if (i != arrayList.size() - 1) {
                commaSepValueBuilder.append("#");
            }
        }

        return String.valueOf(commaSepValueBuilder);
    }


    public static ArrayList<String> convertStringToArrayList(String stringValue) {
        ArrayList<String> myList = new ArrayList<String>(Arrays.asList(stringValue.split("#")));
        return myList;
    }

    public static ArrayList<String> convertStringwithCommaToArrayList(String stringValue) {
        ArrayList<String> myList = new ArrayList<String>(Arrays.asList(stringValue.split(",")));
        return myList;
    }

    public static void openMap(Context context, Double latitude, Double longitude) {

        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        context.startActivity(intent);
    }
}
