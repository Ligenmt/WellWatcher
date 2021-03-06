package com.ligen.wellwatcher;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.ligen.wellwatcher.dao.CheckInfoDao;
import com.ligen.wellwatcher.dao.CheckRecordDao;
import com.ligen.wellwatcher.dao.UserDao;
import com.ligen.wellwatcher.model.CheckRecord;
import com.ligen.wellwatcher.model.Checkpoint;
import com.ligen.wellwatcher.model.DeviceRecord;
import com.ligen.wellwatcher.model.User;
import com.ligen.wellwatcher.util.SharePrerenceUtil;

import java.util.Date;
import java.util.List;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testAddUser() {
//        UserDao.getDao(getContext()).addUser("大力", "123", "员工");
//        UserDao.getDao(getContext()).addUser("技安", "123", "员工");
//        UserDao.getDao(getContext()).addUser("大熊", "123", "管理员");
        UserDao.getDao(getContext()).addUser("ADMIN", "123", null);
    }

    public void testGetUsers() {
        List<User> allUsers = UserDao.getDao(getContext()).getAllUsers();
        for (User user : allUsers) {
            System.out.println(user.getName());
        }
    }

    public void testAddType() {
        CheckInfoDao.getDao(getContext()).addType("泥浆工");
        CheckInfoDao.getDao(getContext()).addType("井架工");
        CheckInfoDao.getDao(getContext()).addType("司钻");
        CheckInfoDao.getDao(getContext()).addType("内钳工");
        CheckInfoDao.getDao(getContext()).addType("外钳工");
        CheckInfoDao.getDao(getContext()).addType("场地工");
    }

    public void testRemoveType() {
        CheckInfoDao.getDao(getContext()).removeType("管理员");
    }

    public void testGetTypes() {
        List<User> allUsers = UserDao.getDao(getContext()).getAllUsers();
        for (User user : allUsers) {
            System.out.println(user.getName());
        }
    }

    public void testAddDevice() {
        CheckInfoDao.getDao(getContext()).addDevice("泥浆值班房", "泥浆工");
    }

    public void testgetDevices() {

        List<String> devices = CheckInfoDao.getDao(getContext()).getDevicesByType("泥浆工");
        for (String device : devices) {
            System.out.println(device);
        }
    }

    public void testAddCheckpoint() {
        CheckInfoDao.getDao(getContext()).addCheckpoint("旋转粘度计", "泥浆值班房");
        CheckInfoDao.getDao(getContext()).addCheckpoint("用水", "泥浆值班房");
        CheckInfoDao.getDao(getContext()).addCheckpoint("接地", "泥浆值班房");
    }

    public void testGetCheckpoint() {
        List<Checkpoint> list = CheckInfoDao.getDao(getContext()).getCheckpoint("泥浆值班房");
        for (Checkpoint checkpoint : list) {
            System.out.println(checkpoint.getCheckpoint());
        }
    }

    public void testRemoveCheckpoint() {
        CheckInfoDao.getDao(getContext()).removeCheckpoint("device1");
    }

    public void testAddCheckRecord() {
        CheckRecordDao.getDao(getContext()).saveCheckRecord("大熊", "泥浆值班房", "旋转粘度计", 1);
    }

    public void testGetCheckRecord() {
        List<CheckRecord> checkRecords = CheckRecordDao.getDao(getContext()).getRecordByUsernameAndDevice("大熊", "泥浆值班房");
        for (CheckRecord checkRecord : checkRecords) {
            System.out.println(checkRecord.getCheckpoint());
        }
    }

    public void testAddCheckDevice() {
        CheckRecordDao.getDao(getContext()).saveCheckDevice("大熊", "泥浆值班房", 1, new Date());
    }

    public void testGetCheckDevice() {
        List<DeviceRecord> drs = CheckRecordDao.getDao(getContext()).getAllRecordDevicesByUsername("大熊");
        for (DeviceRecord dr : drs) {
            System.out.println(dr.getDevicename());
            System.out.println(dr.getUsername());
        }
    }

    public void testRemoveCheckDevice() {
        CheckRecordDao.getDao(getContext()).removeCheckDevicesByUsername("大熊");
        CheckRecordDao.getDao(getContext()).removeCheckRecordByUsername("大熊");

    }

    public void testReload() {
        SharePrerenceUtil.clearUser(getContext());
    }

    public void testInit() {

        //初始化用户
        CheckInfoDao.getDao(getContext()).addType("泥浆工");
        CheckInfoDao.getDao(getContext()).addType("井架工");
        CheckInfoDao.getDao(getContext()).addType("司钻");
        CheckInfoDao.getDao(getContext()).addType("内钳工");
        CheckInfoDao.getDao(getContext()).addType("外钳工");
        CheckInfoDao.getDao(getContext()).addType("场地工");
        //初始化用户
        UserDao.getDao(getContext()).addUser("ADMIN", "123", null);
        UserDao.getDao(getContext()).addUser("大力", "123", "泥浆工");
        UserDao.getDao(getContext()).addUser("技安", "123", "泥浆工");
        UserDao.getDao(getContext()).addUser("大熊", "123", "管理员");

        CheckInfoDao.getDao(getContext()).addDevice("泥浆值班房", "泥浆工");
        CheckInfoDao.getDao(getContext()).addCheckpoint("旋转粘度计", "泥浆值班房");
        CheckInfoDao.getDao(getContext()).addCheckpoint("用水", "泥浆值班房");
        CheckInfoDao.getDao(getContext()).addCheckpoint("接地", "泥浆值班房");

        CheckInfoDao.getDao(getContext()).addDevice("药品罐", "泥浆工");
        CheckInfoDao.getDao(getContext()).addCheckpoint("罐面清洁", "药品罐");
        CheckInfoDao.getDao(getContext()).addCheckpoint("闸门", "药品罐");
        CheckInfoDao.getDao(getContext()).addCheckpoint("搅拌器接线", "药品罐");

        CheckInfoDao.getDao(getContext()).addDevice("除砂器", "泥浆工");
        CheckInfoDao.getDao(getContext()).addCheckpoint("固定牢靠", "除砂器");
        CheckInfoDao.getDao(getContext()).addCheckpoint("设备润滑", "除砂器");
        CheckInfoDao.getDao(getContext()).addCheckpoint("护罩齐全", "除砂器");
        CheckInfoDao.getDao(getContext()).addCheckpoint("筛布筛框", "除砂器");
        CheckInfoDao.getDao(getContext()).addCheckpoint("电机", "除砂器");
        CheckInfoDao.getDao(getContext()).addCheckpoint("漏斗", "除砂器");
        CheckInfoDao.getDao(getContext()).addCheckpoint("管线", "除砂器");

        CheckInfoDao.getDao(getContext()).addDevice("砂泵", "泥浆工");
        CheckInfoDao.getDao(getContext()).addCheckpoint("罐面清洁", "砂泵");
        CheckInfoDao.getDao(getContext()).addCheckpoint("闸门", "砂泵");
        CheckInfoDao.getDao(getContext()).addCheckpoint("搅拌器接线", "砂泵");

        CheckInfoDao.getDao(getContext()).addDevice("搅拌器", "泥浆工");
        CheckInfoDao.getDao(getContext()).addCheckpoint("固定牢靠", "搅拌器");
        CheckInfoDao.getDao(getContext()).addCheckpoint("润滑清洁", "搅拌器");
        CheckInfoDao.getDao(getContext()).addCheckpoint("护罩齐全", "搅拌器");
        CheckInfoDao.getDao(getContext()).addCheckpoint("电机", "搅拌器");
        CheckInfoDao.getDao(getContext()).addCheckpoint("搅拌器叶轮", "搅拌器");

        CheckInfoDao.getDao(getContext()).addDevice("离心机", "泥浆工");
        CheckInfoDao.getDao(getContext()).addCheckpoint("润滑清洁", "离心机");
        CheckInfoDao.getDao(getContext()).addCheckpoint("电机", "离心机");
        CheckInfoDao.getDao(getContext()).addCheckpoint("供液泵", "离心机");
        CheckInfoDao.getDao(getContext()).addCheckpoint("管线", "离心机");

        CheckInfoDao.getDao(getContext()).addDevice("泥浆循环罐", "泥浆工");
        CheckInfoDao.getDao(getContext()).addCheckpoint("梯子牢靠", "泥浆循环罐");
        CheckInfoDao.getDao(getContext()).addCheckpoint("护栏", "泥浆循环罐");
        CheckInfoDao.getDao(getContext()).addCheckpoint("罐面", "泥浆循环罐");
        CheckInfoDao.getDao(getContext()).addCheckpoint("泥浆量", "泥浆循环罐");
        CheckInfoDao.getDao(getContext()).addCheckpoint("液面报警器", "泥浆循环罐");

        CheckInfoDao.getDao(getContext()).addDevice("泥浆储备罐", "泥浆工");
        CheckInfoDao.getDao(getContext()).addCheckpoint("梯子牢靠", "泥浆储备罐");
        CheckInfoDao.getDao(getContext()).addCheckpoint("护栏", "泥浆储备罐");
        CheckInfoDao.getDao(getContext()).addCheckpoint("罐面", "泥浆储备罐");
        CheckInfoDao.getDao(getContext()).addCheckpoint("储备浆", "泥浆储备罐");
        CheckInfoDao.getDao(getContext()).addCheckpoint("管线", "泥浆储备罐");
        CheckInfoDao.getDao(getContext()).addCheckpoint("电源开关", "泥浆储备罐");
        CheckInfoDao.getDao(getContext()).addCheckpoint("药品罐", "泥浆储备罐");

        CheckInfoDao.getDao(getContext()).addDevice("工业水罐", "泥浆工");
        CheckInfoDao.getDao(getContext()).addCheckpoint("梯子牢靠", "工业水罐");

        CheckInfoDao.getDao(getContext()).addDevice("混合漏斗", "泥浆工");
        CheckInfoDao.getDao(getContext()).addCheckpoint("漏斗完好", "混合漏斗");
        CheckInfoDao.getDao(getContext()).addCheckpoint("整洁干净", "混合漏斗");

        CheckInfoDao.getDao(getContext()).addDevice("材料爬犁", "泥浆工");
        CheckInfoDao.getDao(getContext()).addCheckpoint("处理剂", "材料爬犁");

        CheckInfoDao.getDao(getContext()).addDevice("坐岗房", "泥浆工");
        CheckInfoDao.getDao(getContext()).addCheckpoint("记录齐全", "坐岗房");
        CheckInfoDao.getDao(getContext()).addCheckpoint("量具齐全", "坐岗房");
        CheckInfoDao.getDao(getContext()).addCheckpoint("卫生整洁", "坐岗房");

        CheckInfoDao.getDao(getContext()).addDevice("泥浆材料房", "泥浆工");
        CheckInfoDao.getDao(getContext()).addCheckpoint("处理剂", "泥浆材料房");
        CheckInfoDao.getDao(getContext()).addCheckpoint("门上通风", "泥浆材料房");
        CheckInfoDao.getDao(getContext()).addCheckpoint("整洁卫生", "泥浆材料房");

//        CheckInfoDao.getDao(getContext()).addDevice("值班房");
//        CheckInfoDao.getDao(getContext()).addCheckpoint("处理剂", "值班房");
//        CheckInfoDao.getDao(getContext()).addCheckpoint("门上通风", "值班房");
//        CheckInfoDao.getDao(getContext()).addCheckpoint("整洁卫生", "值班房");
    }
}