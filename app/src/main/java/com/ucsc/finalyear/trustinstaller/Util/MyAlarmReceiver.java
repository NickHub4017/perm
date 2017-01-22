package com.ucsc.finalyear.trustinstaller.Util;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ucsc.finalyear.trustinstaller.Service.DataGet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nrv on 1/21/17.
 */
public class MyAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "com.codepath.example.servicesdemo.alarm";
    RequestQueue queue = Volley.newRequestQueue(DataGet.con);
    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(DataGet.con,"GOT",Toast.LENGTH_LONG).show();
        getData();

    }

    public void getData(){
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final List pkgAppsList = DataGet.pm.queryIntentActivities(mainIntent, 0);
        //Toast.makeText(getApplicationContext(),  " + PackageManager.PERMISSION_GRANTED + " " + PackageManager.PERMISSION_DENIED, Toast.LENGTH_LONG).show();
        String all="";
        JSONArray jsonArray=new JSONArray();
        for (Object obj : pkgAppsList) {
            ResolveInfo resolveInfo = (ResolveInfo) obj;
            PackageInfo packageInfo = null;
            Log.e("-----------", resolveInfo.activityInfo.packageName);
            List<String> permls=getGrantedPermissions(resolveInfo.activityInfo.packageName);

            for (int i=0;i<permls.size();i++){
                JSONObject jo=new JSONObject();
                try {
                    jo.put("uname","abc");
                    jo.put("app",resolveInfo.activityInfo.packageName);
                    jo.put("uname",permls.get(i));

                    jo.put("status",DataGet.pm.checkPermission(permls.get(i), resolveInfo.activityInfo.packageName) == PackageManager.PERMISSION_GRANTED);
                    jsonArray.put(jo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }

        RequestQueue queue = DataGet.queue;
        String url = "http://138.197.26.183:9000";
        final JSONObject regjson=new JSONObject();
        try {
            regjson.put("data",jsonArray);
            regjson.put("name","Name is");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ABCD","ddddd  "+e.getMessage());
        }
        /*
        @param regjson
        */
        Log.e("Data",regjson.toString());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("name", "USER");
                params.put("domain", regjson.toString());

                return params;
            }
        };
        queue.add(postRequest);


    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    List<String> getGrantedPermissions(final String appPackage) {
        List<String> granted = new ArrayList<String>();
        try {
            PackageInfo pi = DataGet.pm.getPackageInfo(appPackage, PackageManager.GET_PERMISSIONS);
            for (int i = 0; i < pi.requestedPermissions.length; i++) {
                if ((pi.requestedPermissionsFlags[i] & PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0) {
                    granted.add(pi.requestedPermissions[i]);
                }
                else{
                    Log.e("WRONG",appPackage);
                }
            }
        } catch (Exception e) {
        }
        for (String p: granted){
            Log.e("Log", p);
        }
        return granted;
    }

}
