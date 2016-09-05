package com.ligen.wellwatcher;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.ligen.wellwatcher.dao.CheckInfoDao;
import com.ligen.wellwatcher.dao.UserDao;

/**
 * Created by ligen on 2016/7/1.
 */
public class ApplicationInit extends ApplicationTestCase<Application> {

    public ApplicationInit() {
        super(Application.class);
    }

    public void appInit() {
        //初始化用户
        CheckInfoDao.getDao(getContext()).addType("管理员");
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
