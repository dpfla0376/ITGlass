package com.example.yelim.it_glass;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private EditText inputID;
    private Button idConfirmButton;

    private Context mContext;
    final DatabaseManager dbManager = new DatabaseManager(RegisterActivity.this, DatabaseManager.DB_NAME + ".db", null, 1);
    //ServerDatabaseManager serverDBM = new ServerDatabaseManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputID = (EditText) findViewById(R.id.inputID);
        idConfirmButton = (Button) findViewById(R.id.idConfirmButton);
        mContext = this;

        idConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idConfirmButton.setEnabled(false);
                //inputID of user is already in Server
                ServerDatabaseManager.hasID(inputID.getText().toString());
                Callback callback = new Callback() {
                    @Override
                    public void callBackMethod() {

                    }

                    @Override
                    public void callBackMethod(boolean value) {
                        if(value) {
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
                            ServerDatabaseManager.setLocalUserID(inputID.getText().toString());
                            ServerDatabaseManager.saveUserID(inputID.getText().toString());

                            //DB에 ID 정보 저장
                            String[] record = new String[1];                                //record 크기 할당(맞나..?)
                            for(int i=0; i<record.length; i++) record[i] = new String();    //record 초기화
                            record[0] = inputID.getText().toString();
                            dbManager.insertToDatabase(Database.UserTable._TABLENAME, record);
                            Log.d("DATABASE", "---------user_registered--------");


                            //(확인창 띄우고) MainActivity 진입
                            new AlertDialog.Builder(mContext)
                                    .setTitle("Welcome!")
                                    .setMessage("Hello, " + record[0] + "!")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    })
                                    .show();
                        }
                    }
                };
                ServerDatabaseManager.sethasIDCallback(callback);

            }
        });
    }

}
