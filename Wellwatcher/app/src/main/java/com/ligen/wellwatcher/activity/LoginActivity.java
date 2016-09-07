package com.ligen.wellwatcher.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ligen.wellwatcher.R;
import com.ligen.wellwatcher.adapter.ListPopupWindowAdapter;
import com.ligen.wellwatcher.dao.CheckInfoDao;
import com.ligen.wellwatcher.dao.UserDao;
import com.ligen.wellwatcher.model.User;
import com.ligen.wellwatcher.util.SharePrerenceUtil;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout mTilUsername;
    TextInputLayout mTilPassword;
    Button mBtnLogin;
    ListPopupWindow mPop;
    List<User> mUserList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        boolean firstrun = SharePrerenceUtil.getSP(this).getBoolean("firstrun", true);
        if(firstrun) {
            try {
                firstrunInit();
                SharePrerenceUtil.getSP(this).edit().putBoolean("firstrun", false).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        initViews();
        //加载注册用户
        initUser();
        //读取缓存用户
        checkCurrentUser();
    }

    private void initUser() {

        mUserList = UserDao.getDao(this).getAllUsers();
    }

    /**
     * 若缓存中有数据直接登录
     */
    private void checkCurrentUser() {
        String currentUser = SharePrerenceUtil.getCurrentUser(this);
        if (currentUser == null) {
            return;
        }
        for(User user : mUserList) {
            if (user.getName().equals(currentUser)) {
                login(user.getName(), user.getType());
                break;
            }
        }
    }

    private void initViews() {
        mTilUsername = (TextInputLayout) findViewById(R.id.til_username);
        mTilPassword = (TextInputLayout) findViewById(R.id.til_password);
        mBtnLogin = (Button) findViewById(R.id.btn_login);

        mTilUsername.getEditText().setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        findViewById(R.id.btn_signup).setOnClickListener(this);

    }

    private void login(String username, String type) {

        SharedPreferences sp = SharePrerenceUtil.getSP(this);
        sp.edit().putString("user", username).commit();
        sp.edit().putString("type", type).commit();
        Intent intent;
        if (type == null) {
            intent = new Intent(this, AdminActivity.class);
        } else {
            intent = new Intent(this, WorkerActivity.class);
        }
        startActivity(intent);
        finish();
    }

    public void loginCheck() {

        String username = mTilUsername.getEditText().getText().toString();
        String password = mTilPassword.getEditText().getText().toString();

        for(User user : mUserList) {
            if(user.getName().equals(username) && user.getPassword().equals(password)) {
                login(username, user.getType());
                return;
            }
        }
        mTilUsername.getEditText().setText("");
        mTilPassword.getEditText().setText("");
        Toast.makeText(LoginActivity.this, "用户名或密码错误!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.et_username:
                if(mPop == null || !mPop.isShowing()) {
                    initPop();
                } else {
                    mPop.dismiss();
                }
                break;
            case R.id.btn_login:
                loginCheck();
                break;

            case R.id.btn_signup:
                signup();
        }

    }

    private void initPop() {
        mPop = new ListPopupWindow(this);
        mUserList = UserDao.getDao(this).getAllUsers();
        final List<String> usernameList = new ArrayList<>();
        for (User user : mUserList) {
            usernameList.add(user.getName());
        }

        ListPopupWindowAdapter adapter = new ListPopupWindowAdapter(this, usernameList);
        mPop.setAdapter(adapter);
        mPop.setWidth(300);

        mPop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTilUsername.getEditText().setText(usernameList.get(position));
                mPop.dismiss();
            }
        });
        mPop.setAnchorView(mTilUsername.getEditText());
        mPop.show();
    }

    String signType = "管理员";
    List<String> typeList = null;

    private void signup() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("员工注册");
        final View dialogView = View.inflate(this, R.layout.dialog_signup, null);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        dialog.setCanceledOnTouchOutside(false);
        Spinner spinnerType = (Spinner) dialogView.findViewById(R.id.spinner_type);

        typeList = CheckInfoDao.getDao(this).getAllTypes();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                signType = typeList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        dialogView.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText etUsername = (EditText) dialogView.findViewById(R.id.et_username);
                EditText etPassword = (EditText) dialogView.findViewById(R.id.et_password);
                EditText etRePassword = (EditText) dialogView.findViewById(R.id.et_repeat_password);
                if (TextUtils.isEmpty(etUsername.getText().toString()) || TextUtils.isEmpty(etPassword.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!(etPassword.getText().toString().equals(etRePassword.getText().toString()))) {
                    Toast.makeText(LoginActivity.this, "密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }

                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                UserDao.getDao(LoginActivity.this).addUser(username, password, signType);
                Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();

                User newUser = new User(etUsername.getText().toString(), etPassword.getText().toString(), "员工");
                mUserList.add(newUser);
                dialog.dismiss();
            }
        });

        dialogView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    
    public void firstrunInit() throws Exception{

        XmlResourceParser parser = getResources().getXml(R.xml.initconfig);
        int eventType = parser.getEventType();
        parser.getAttributeCount();



        String currentType = null;
        String currentDevice = null;
        while(eventType != XmlPullParser.END_DOCUMENT) {

            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:

                    break;
                case XmlPullParser.START_TAG:
                    String name = parser.getName();
                    if(name.equals("type")) {
                        currentType = parser.getAttributeValue(0);
                        CheckInfoDao.getDao(this).addType(currentType);
                    }
                    if(name.equals("device")) {
                        currentDevice = parser.getAttributeValue(0);
                        CheckInfoDao.getDao(this).addDevice(currentDevice, currentType);
                    }
                    if(name.equals("checkpoint")) {
                        String checkpoint = parser.nextText();
                        CheckInfoDao.getDao(this).addCheckpoint(checkpoint, currentDevice, currentType);
                    }
                    break;


            }
            eventType = parser.next();
        }

        UserDao.getDao(this).addUser("ADMIN", "123", null);
        SharePrerenceUtil.setUrl(LoginActivity.this, "http://120.25.244.188/crawler/receive/drilling");

    }
}
