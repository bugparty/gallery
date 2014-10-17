package com.os1.gallery.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import java.util.Random;
import com.os1.gallery.R;
public class PasswordRecovery extends Activity {
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);
        Random ra = new Random();

        int a1 = ra.nextInt(10);
        int b1 = ra.nextInt(10);
        int c1 = ra.nextInt(10);
        int d1 = ra.nextInt(10);
        final String[] check1 = new String[1];
        final String check = ("" + a1 + b1 + c1 + d1).trim();
        final TextView tv = (TextView) findViewById(R.id.text4);
        tv.setText(check);
        Button bu1 = (Button) findViewById(R.id.confirm);
        bu1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Random ra = new Random();
                int a2 = ra.nextInt(10);
                int b2 = ra.nextInt(10);
                int c2 = ra.nextInt(10);
                int d2 = ra.nextInt(10);
                check1[0] = ("" + a2 + b2 + c2 + d2).trim();

                tv.setText(check1[0]);
            }

        });
        Button bu = (Button) findViewById(R.id.confirm);

        bu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                EditText et = (EditText) findViewById(R.id.editText);
                String edit = et.getText().toString();
                if (edit.equals(check) || edit.equals(check1[0])) {
                    Toast.makeText(PasswordRecovery.this, "正在发送，请稍后", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(PasswordRecovery.this, "重新输入", Toast.LENGTH_LONG).show();
                }
            }

        });

    }
}