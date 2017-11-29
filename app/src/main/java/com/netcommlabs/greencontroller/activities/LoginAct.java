package com.netcommlabs.greencontroller.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netcommlabs.greencontroller.R;

public class LoginAct extends AppCompatActivity {

    private LoginAct mContext;
    private TextView tvForgtPassEvent, tvLoginEvent, tvSignUpEvent;
    private LinearLayout llLoginFB, llLoginGoogle;
    private EditText etPhoneEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        initBase();

        initListeners();
    }

    private void initBase() {
        mContext = this;

        etPhoneEmail = (EditText) findViewById(R.id.etPhoneEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        tvForgtPassEvent = (TextView) findViewById(R.id.tvForgtPassEvent);
        tvLoginEvent = (TextView) findViewById(R.id.tvLoginEvent);
        tvSignUpEvent = (TextView) findViewById(R.id.tvSignUpEvent);

        llLoginFB = (LinearLayout) findViewById(R.id.llLoginFB);
        llLoginGoogle = (LinearLayout) findViewById(R.id.llLoginGoogle);

    }

    private void initListeners() {
        etPhoneEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etPhoneEmail.setCursorVisible(true);
                etPhoneEmail.setFocusableInTouchMode(true);
                return false;
            }
        });

        etPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etPassword.setCursorVisible(true);
                etPassword.setFocusableInTouchMode(true);
                return false;
            }
        });

        tvForgtPassEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Forgot Password", Toast.LENGTH_SHORT).show();
            }
        });

        tvLoginEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Login", Toast.LENGTH_SHORT).show();
            }
        });

        tvSignUpEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Sign Up", Toast.LENGTH_SHORT).show();
            }
        });

        llLoginFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "FB", Toast.LENGTH_SHORT).show();
            }
        });

        llLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Google", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
