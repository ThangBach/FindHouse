package com.example.thangbach.findhouse.Dialog;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.thangbach.findhouse.R;
import com.example.thangbach.findhouse.SERVICE.UserIMP;
import com.example.thangbach.findhouse.VIEW.FindActivity;
import com.example.thangbach.findhouse.VIEW.LoginActivity;
import com.example.thangbach.findhouse.VIEW.SignUpActivity;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by ThangBach on 11/15/2016.
 */

public class LoginDialog extends DialogFragment {

    Button  btn_dangnhap,
            btn_dangky;

    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.dialog_login,container,false);

        UserIMP.myData= FirebaseDatabase.getInstance().getReference();


        anhXa(view);

        btn_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        btn_dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return view;
    }
    public void anhXa(View view){
        btn_dangky=(Button)view.findViewById(R.id.dialog_login_btn_dangky);
        btn_dangnhap=(Button)view.findViewById(R.id.dialog_login_btn_dangnhap);
    }
}
