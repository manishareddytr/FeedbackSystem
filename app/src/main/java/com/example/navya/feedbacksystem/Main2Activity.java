package com.example.navya.feedbacksystem;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {
    EditText name,sub,fb;
    String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button submit = (Button) findViewById(R.id.button2);

        name = (EditText) findViewById(R.id.editText);
        sub = (EditText) findViewById(R.id.editText2);
        fb = (EditText) findViewById(R.id.editText3);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((sub.getText().toString()).equals("") || (fb.getText().toString()).equals("")) {
                    Toast.makeText(Main2Activity.this,"Subject and Feedback are mandatory.", Toast.LENGTH_LONG).show();
                } else {
                    data = "http://123.176.47.87:3002/saveFeedback?student_name=" + name.getText().toString() + "&subject=" + sub.getText().toString() + "&feedback=" + fb.getText().toString();

                    //Toast.makeText(Main2Activity.this,data,Toast.LENGTH_LONG).show();
                    WebView webView = (WebView) findViewById(R.id.webView);
                    webView.loadUrl(data);
                }
            }
        });
    }
}