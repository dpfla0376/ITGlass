package com.example.yelim.it_glass;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private EditText inputID;
    private Button idConfirmButton;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputID = (EditText) findViewById(R.id.inputID);
        idConfirmButton = (Button) findViewById(R.id.idConfrimButton);
        mContext = this;

        idConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //inputID of user is already in Server
                if(ServerManager.hasID(inputID.getText().toString())) {
                    //중복 경고창 띄우고 EditText 내용 지우고 재입력 받기
                    inputID.setText("");
                    new AlertDialog.Builder(mContext)
                            .setTitle("Warning!")
                            .setMessage("Already in use. Please do it again.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }

                //inputID of user is not in Server
                else {
                    //Server로 ID 전송 및 저장
                    ServerManager.saveID(inputID.getText().toString());

                    //DB에 ID 정보보 저장
                   Cursor c = LogoActivity.db.rawQuery("select * from where ", null);
                    if(c.getCount() == 0) {

                    }
                    else {

                    }

                    //(확인창 띄우고) MainActivity 진입
                    new AlertDialog.Builder(mContext)
                            .setTitle("Warning!")
                            .setMessage("Already in use. Please do it again.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }
            }
        });
    }

}
