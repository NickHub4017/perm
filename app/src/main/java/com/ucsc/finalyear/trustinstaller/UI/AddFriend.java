package com.ucsc.finalyear.trustinstaller.UI;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ucsc.finalyear.trustinstaller.DB.DBHandle;
import com.ucsc.finalyear.trustinstaller.R;
import com.ucsc.finalyear.trustinstaller.Util.Friend;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nrv on 1/21/17.
 */
public class AddFriend extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemLongClickListener {
    ArrayList<Friend> friendList;
    DBHandle dbHandle;
     MyAdapter adapter;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfriend);
        ListView listView = (ListView) findViewById(R.id.list_view);
        dbHandle=new DBHandle(getApplicationContext());
        friendList = dbHandle.getAllFriends();
        adapter = new MyAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog = new Dialog(AddFriend.this);
                dialog.setContentView(R.layout.dialoglayout);
                dialog.findViewById(R.id.button_cancel).setOnClickListener(
                        AddFriend.this);
                dialog.findViewById(R.id.button_ok).setOnClickListener(
                        AddFriend.this);
                dialog.show();
            }
        });


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_cancel:
                dialog.dismiss();
                break;

            case R.id.button_ok:
                String name = ((EditText) dialog.findViewById(R.id.edit_box))
                        .getText().toString();
                String phone = ((EditText) dialog.findViewById(R.id.phone_box))
                        .getText().toString();
                Log.e("Data", name);
                Log.e("Data",phone);
                if (null != name && null != phone) {
                    friendList.add(new Friend(name,phone));
                    dbHandle.insertNewData(new Friend(name,phone));
                    dialog.dismiss();
                    adapter.notifyDataSetChanged();
                }

                break;
        }
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        dbHandle.deleteFriend(new Friend(friendList.get(position).getName(), friendList.get(position).getPhone()));
        friendList.remove(position);
        adapter.notifyDataSetChanged();
        return true;
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return friendList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return friendList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) convertView;
            if (null == view) {
                view = new TextView(AddFriend.this);
                view.setPadding(10, 10, 10, 10);
            }
            view.setText(friendList.get(position).getName()+"  -  "+friendList.get(position).getPhone());
            return view;
        }
    }
}
