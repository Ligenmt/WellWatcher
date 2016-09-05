package com.ligen.wellwatcher.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ligen.wellwatcher.R;
import com.ligen.wellwatcher.adapter.AdminDeviceFragmentAdapter;
import com.ligen.wellwatcher.dao.CheckInfoDao;
import com.ligen.wellwatcher.dao.UserDao;
import com.ligen.wellwatcher.fragment.AdminDeviceFragment;
import com.ligen.wellwatcher.model.User;
import com.ligen.wellwatcher.util.SharePrerenceUtil;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "AdminActivity";
    public static String currentUser;
    private DrawerLayout mDl;
    private CheckInfoDao dao;

    List<String> mTitles;
    List<Fragment> mFragments;
    AdminDeviceFragmentAdapter adapter;

    ViewPager mViewPager;
    TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        currentUser = sp.getString("user", null);

        Log.i(TAG, "成功登录：" + currentUser);
        //侧滑菜单
        mDl = (DrawerLayout) findViewById(R.id.draw_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView nv = (NavigationView) findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(this);

        dao = CheckInfoDao.getDao(this);
        //创建Fragment
        initFragment();
    }

    private void initFragment() {
        mTitles = new ArrayList<>();
        mFragments = new ArrayList<>();

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        List<String> types = CheckInfoDao.getDao(this).getAllTypes();

        for (String type : types) {
            TabLayout.Tab tab = mTabLayout.newTab().setText(type);
            mTabLayout.addTab(tab);
            mTitles.add(type);
            List<String> devices = dao.getDevicesByType(type);
            Fragment fragment = new AdminDeviceFragment(type, devices);
            mFragments.add(fragment);
        }


        mViewPager = (ViewPager) findViewById(R.id.vp);
        adapter = new AdminDeviceFragmentAdapter(getSupportFragmentManager(), mTitles, mFragments);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_min, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            //登出
            case R.id.action_menu:
                mDl.openDrawer(Gravity.LEFT);
                break;
            }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_device:
                Intent intent = new Intent(this, AddDeviceActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.action_delete_device:
                deleteDevice();
                break;
            case R.id.action_add_type:
                addType();
                break;
            case R.id.action_delete_type:
                deleteType();
                break;
            case R.id.action_delete_user:
                deleteUser();
                break;
            case R.id.action_password:
                changePassword();
                break;
            case R.id.action_logout:
                logout();
                break;
            case R.id.action_nfcwrite:
                startActivity(new Intent(this, WriteTextActivity.class));
                break;
            case R.id.action_reset:
                resetData();
                break;
            case R.id.action_network:
                setUploadUrl();
                break;

        }
        mDl.closeDrawers();
        return true;
    }

    /**
     * 设置上传地址
     */
    private void setUploadUrl() {

        View view = View.inflate(this, R.layout.dialog_upload_url, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置上传地址")
                .setView(view);
        final AlertDialog dialog = builder.create();

        final EditText et = (EditText) view.findViewById(R.id.et_upload_url);
        et.setText(SharePrerenceUtil.getUrl(this));
        view.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(et.getText().toString())) {
                    Toast.makeText(AdminActivity.this, "不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharePrerenceUtil.setUrl(AdminActivity.this, et.getText().toString());
                Toast.makeText(AdminActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public void resetData() {
        //重置放在WorkerActivity中
    }

    String mDeviceToDelete;  //删除巡检地用
    /**
     * 移除device
     */
    public void deleteDevice() {
        String type = mTabLayout.getTabAt(mViewPager.getCurrentItem()).getText().toString();
        List<String> deviceList = CheckInfoDao.getDao(this).getDevicesByType(type);
        if(deviceList.size() == 0) {
            Toast.makeText(AdminActivity.this, type + "当前无巡检地！", Toast.LENGTH_SHORT).show();
            return;
        }

        final String[] devices = new String[deviceList.size()];
        for(int i=0; i<deviceList.size(); i++) {
            devices[i] = deviceList.get(i);
        }
        mDeviceToDelete = devices[0];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("移除巡检点")
                .setSingleChoiceItems(devices, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDeviceToDelete = devices[which];
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                CheckInfoDao.getDao(AdminActivity.this).removeDevice(mDeviceToDelete);
                Toast.makeText(AdminActivity.this, mDeviceToDelete + "已移除!", Toast.LENGTH_SHORT).show();
                initFragment();
                dialog.dismiss();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * 新增工种
     */
    public void addType() {

        View view = View.inflate(this, R.layout.dialog_add_type, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("新增工种")
                .setView(view);
        final AlertDialog dialog = builder.create();

        final EditText et = (EditText) view.findViewById(R.id.et_add);
        view.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(et.getText().toString())) {
                    Toast.makeText(AdminActivity.this, "不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                CheckInfoDao.getDao(AdminActivity.this).addType(et.getText().toString());
                initFragment();
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    String mTypeToDelete;  //删除工种用
    /**
     * 移除工种
     */
    public void deleteType() {

        List<String> allTypesList = CheckInfoDao.getDao(this).getAllTypes();
        if(allTypesList.size() == 0) {
            Toast.makeText(AdminActivity.this, "还未添加工种!", Toast.LENGTH_SHORT).show();
            return;
        }

        final String[] allTypes = new String[allTypesList.size()];
        for(int i=0; i<allTypesList.size(); i++) {
            allTypes[i] = allTypesList.get(i);
        }

        mTypeToDelete = allTypes[0];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("移除工种")
                .setSingleChoiceItems(allTypes, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTypeToDelete = allTypes[which];
            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CheckInfoDao.getDao(AdminActivity.this).removeType(mTypeToDelete);
                Toast.makeText(AdminActivity.this, mTypeToDelete + "已移除!", Toast.LENGTH_SHORT).show();
                initFragment();
                dialog.dismiss();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    String mUserToDelete;  //删除用户用
    public void deleteUser() {
        List<User> userList = UserDao.getDao(this).getAllUsers();
        if(userList.size() == 0) {
            Toast.makeText(AdminActivity.this, "目前没有用户!", Toast.LENGTH_SHORT).show();
            return;
        }

        final String[] allUsers = new String[userList.size()];
        for(int i=0; i<userList.size(); i++) {
            allUsers[i] = userList.get(i).getName();
        }
        mUserToDelete = allUsers[0];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("移除用户")
                .setSingleChoiceItems(allUsers, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mUserToDelete = allUsers[which];
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (mUserToDelete.equals("ADMIN")) {
                    Toast.makeText(AdminActivity.this, "管理员不能移除！", Toast.LENGTH_SHORT).show();
                    return;
                }

                UserDao.getDao(AdminActivity.this).removeUser(mUserToDelete);
                Toast.makeText(AdminActivity.this, mUserToDelete + "已移除!", Toast.LENGTH_SHORT).show();
                initFragment();
                dialog.dismiss();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void logout() {
        //清除缓存中用户，登出
        SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        sp.edit().putString("user", null).putString("type", null).commit();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void changePassword() {

        final View view = View.inflate(this, R.layout.dialog_change_password, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view)
                .setTitle("设置密码");

        final AlertDialog dialog = builder.create();
        view.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String oldPassword = UserDao.getDao(AdminActivity.this).getUser("ADMIN").getPassword();

                EditText etOld = (EditText) view.findViewById(R.id.et_old_password);
                EditText etNew = (EditText) view.findViewById(R.id.et_new_password);
                EditText etRepeat = (EditText) view.findViewById(R.id.et_repeat_password);
                if (TextUtils.isEmpty(etOld.getText().toString()) || TextUtils.isEmpty(etNew.getText().toString()) || TextUtils.isEmpty(etRepeat.getText().toString())) {
                    Toast.makeText(AdminActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!oldPassword.equals(etOld.getText().toString())) {
                    Toast.makeText(AdminActivity.this, "旧密码不正确", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!(etNew.getText().toString().equals(etRepeat.getText().toString()))) {
                    Toast.makeText(AdminActivity.this, "密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                UserDao.getDao(AdminActivity.this).changePassword("ADMIN", etNew.getText().toString());
                Toast.makeText(AdminActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();


    }
}
