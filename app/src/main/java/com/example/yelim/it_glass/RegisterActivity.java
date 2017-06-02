package com.example.yelim.it_glass;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    private EditText inputID;
    private Button idConfirmButton;
    private RadioButton rbMan;
    private RadioButton rbWoman;
    private RadioButton rbNone;
    private Spinner spAge;
    private TextView tvSpAge;
    private Spinner spWeight;
    private TextView tvSpWeight;

    private Context mContext;
    final DatabaseManager dbManager = new DatabaseManager(RegisterActivity.this, DatabaseManager.DB_NAME + ".db", null, 1);

    private String[] spAgeItems = {"안알랴줌", "20대", "30대", "40대", "50대", "60대 이상"};
    private String[] spWeightItems = {"안알랴줌", "~50kg", "50~60kg", "60~70kg", "70~80kg", "80kg~"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputID = (EditText) findViewById(R.id.inputID);
        idConfirmButton = (Button) findViewById(R.id.idConfirmButton);
        rbMan = (RadioButton) findViewById(R.id.rbMan);
        rbWoman = (RadioButton) findViewById(R.id.rbWoman);
        rbNone = (RadioButton) findViewById(R.id.rbNone);
        rbNone.setChecked(true);
        spAge = (Spinner) findViewById(R.id.spAge);
        spAge.setOnItemSelectedListener(spAgeListener());
        tvSpAge = (TextView) findViewById(R.id.tvSpAge);
        spWeight = (Spinner) findViewById(R.id.spWeight);
        spWeight.setOnItemSelectedListener(spWeightListener());
        tvSpWeight = (TextView) findViewById(R.id.tvSpWeight);
        mContext = this;

        ArrayAdapter<String> ageAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spAgeItems);
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAge.setAdapter(ageAdapter);

        ArrayAdapter<String> weightAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spWeightItems);
        weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWeight.setAdapter(weightAdapter);

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
                                    .setMessage("이미 사용중인 이름이에요. 다시 입력해주세요.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            idConfirmButton.setEnabled(true);
                                        }
                                    })
                                    .show();
                        }

                        //inputID of user is not in Server
                        else {
                            //Server로 ID 전송 및 저장
                            ServerDatabaseManager.setLocalUserID(inputID.getText().toString());
                            ServerDatabaseManager.saveUserID(inputID.getText().toString());

                            //DB에 ID 및 개인 정보 저장
                            String[] record = new String[6];                                //record 크기 할당(맞나..?)
                            for(int i=0; i<record.length; i++) record[i] = new String();    //record 초기화
                            record[0] = inputID.getText().toString();
                            record[1] = "true";
                            saveSetting2DB(record);
                            dbManager.insertToDatabase(Database.UserTable._TABLENAME, record);
                            String[] temp = Record.parsingDate(ServerDatabaseManager.getTime());
                            record[0] = temp[0] + "" + temp[1] + "" + temp[2];
                            record[1] = 0 + "";
                            dbManager.insertToDatabase(Database.DrinkRecordTable._TABLENAME, record);
                            Log.d("DATABASE", "---------user_registered--------");
                            dbManager.getDrinkOnOff();
                            dbManager.getAvgDrink();


                            //(확인창 띄우고) MainActivity 진입
                            new AlertDialog.Builder(mContext)
                                    .setTitle("Welcome!")
                                    .setMessage("안녕하세요, " + ServerDatabaseManager.getLocalUserID() + " 님!")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            intent.putExtra("avg_drink", "none");
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

    private void saveSetting2DB(String[] record) {
        if(rbMan.isChecked()) {
            record[2] = "man";
            record[5] = "300";
        }
        else if(rbWoman.isChecked()) {
            record[2] = "woman";
            record[5] = "260";
        }
        else {
            record[2] = "none";
            record[5] = "285";
        }

        record[3] = ((String) spAge.getSelectedItem());
        record[4] = ((String) spWeight.getSelectedItem());
    }

    private AdapterView.OnItemSelectedListener spAgeListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)parent.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
    }

    private AdapterView.OnItemSelectedListener spWeightListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)parent.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
    }
}
