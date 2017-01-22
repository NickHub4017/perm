package com.ucsc.finalyear.trustinstaller.UI;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ucsc.finalyear.trustinstaller.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nrv on 1/21/17.
 */
public class Register extends AppCompatActivity {
    EditText nameText;
    EditText phoneText;
    Button regbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_ui);
        nameText=(EditText)findViewById(R.id.nameedittext);
        phoneText=(EditText)findViewById(R.id.phoneeditText);
        regbtn=(Button)findViewById(R.id.regbutton);

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "138.197.26.183:9000";
                JSONObject regjson=new JSONObject();
                try {
                    regjson.put("name",nameText.getText().toString());
                    regjson.put("phone",phoneText.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, regjson, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.get("status").equals("ok")){
                                Toast.makeText(getApplicationContext(),"Register Done",Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Register Fail",Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(getApplicationContext(),"Register Fail",Toast.LENGTH_LONG).show();

                    }
                });

                queue.add(jsObjRequest);

            }
        });

    }

    public void getData(){
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final List pkgAppsList = getPackageManager().queryIntentActivities(mainIntent, 0);
        Toast.makeText(getApplicationContext(), " " + PackageManager.PERMISSION_GRANTED + " " + PackageManager.PERMISSION_DENIED, Toast.LENGTH_LONG).show();
        String all="";
        for (Object obj : pkgAppsList) {
            ResolveInfo resolveInfo = (ResolveInfo) obj;
            PackageInfo packageInfo = null;
            Log.e("-----------", resolveInfo.activityInfo.packageName);
            List<String> permls=getGrantedPermissions(resolveInfo.activityInfo.packageName);
            JSONArray jsonArray=new JSONArray();
            for (int i=0;i<permls.size();i++){
                JSONObject jo=new JSONObject();
                try {
                    jo.put("uname","abc");
                    jo.put("app",resolveInfo.activityInfo.packageName);
                    jo.put("uname",permls.get(i));
                    jsonArray.put(jo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = "192.168.1.5:8080";
            JSONObject regjson=new JSONObject();
            try {
                regjson.put("data",jsonArray);
                regjson.put("name","Name is");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {

                    try {
                        if (response.get("status").equals("ok")){
                            Toast.makeText(getApplicationContext(),"Submit data Done",Toast.LENGTH_LONG).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Submit data Fail",Toast.LENGTH_LONG).show();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"Submit data Fail+++++",Toast.LENGTH_LONG).show();
                }
            });

            queue.add(jsObjRequest);


        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    List<String> getGrantedPermissions(final String appPackage) {
        List<String> granted = new ArrayList<String>();
        try {
            PackageInfo pi = getPackageManager().getPackageInfo(appPackage, PackageManager.GET_PERMISSIONS);
            for (int i = 0; i < pi.requestedPermissions.length; i++) {
                if ((pi.requestedPermissionsFlags[i] & PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0) {
                    granted.add(pi.requestedPermissions[i]);
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
