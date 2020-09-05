package in.afckstechnologies.afcksfees.utils;


import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;


public class WebServiceUtility {
    String result = "";
    Context context;


    public WebServiceUtility(Context context) {
        this.context = context;
    }

    public String getString(String url,JSONObject jsonObjSend,final VolleyCallback callback) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObjSend,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.getBoolean("status")) {
                                //updating the status in sqlite
                                //  db.updateNameStatus(id, MainActivity.NAME_SYNCED_WITH_SERVER);
                                if (!response.isNull("dataList")) {
                                    result=response.toString();
                                    try {
                                        callback.onSuccess(result);
                                    } catch (OperationApplicationException e) {
                                        e.printStackTrace();
                                    } catch (RemoteException e) {
                                        e.printStackTrace();
                                    }
                                }
                                //sending the broadcast to refresh the list
                                // context.sendBroadcast(new Intent(MainActivity.DATA_SAVED_BROADCAST));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onRequestError(error);
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
        return result;
    }

    public interface VolleyCallback {
        void onSuccess(String result) throws OperationApplicationException, RemoteException;
        void onRequestError(VolleyError errorMessage);
        //void onJsonInvoke(String url, final VolleyCallback callback);
    }
}
