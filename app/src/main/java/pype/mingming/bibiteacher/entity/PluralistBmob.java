package pype.mingming.bibiteacher.entity;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * Created by mk on 2016/9/24.
 */
public class PluralistBmob extends BmobObject {
    private User user;
    //兼职者的地址
    private BmobGeoPoint pBGaddress;
    //兼职地址
    private String pAddress;
    //联系方式
    private String pPhone;
    //兼职者的用户名
    private String pUserName;
    //性别
    private String pSex;
    //年龄
    private String pAge;
    //家教龄
    private int pTeacherAge;
    //家教荣誉评价
    private int prbNum;
    //就读高校
    private String pUniversity;
    //头像
    private BmobFile pUserHeadImage;
    //擅长学科
    private String pSubject;
    //自我推荐
    private String pIntroduce;
    //完工结算
    private String pClosing;
    //兼职时段
    private String pTime;
    //有效期
    private String pDay;
    //有空周天
    private String pWeek;

    public String getpWeek() {
        return pWeek;
    }

    public void setpWeek(String pWeek) {
        this.pWeek = pWeek;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getpPhone() {
        return pPhone;
    }

    public void setpPhone(String pPhone) {
        this.pPhone = pPhone;
    }

    public void setpAddress(String pAddress) {
        this.pAddress = pAddress;
    }

    public BmobGeoPoint getpBGaddress() {
        return pBGaddress;
    }

    public void setpBGaddress(BmobGeoPoint pBGaddress) {
        this.pBGaddress = pBGaddress;
    }

    public String getpClosing() {
        return pClosing;
    }

    public void setpClosing(String pClosing) {
        this.pClosing = pClosing;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public String getpDay() {
        return pDay;
    }

    public void setpDay(String pDay) {
        this.pDay = pDay;
    }

    public String getpAddress() {
        return pAddress;
    }
    public String getpUserName() {
        return pUserName;
    }

    public void setpUserName(String pUserName) {
        this.pUserName = pUserName;
    }

    public String getpSex() {
        return pSex;
    }

    public void setpSex(String pSex) {
        this.pSex = pSex;
    }

    public int getpTeacherAge() {
        return pTeacherAge;
    }

    public void setpTeacherAge(int pTeacherAge) {
        this.pTeacherAge = pTeacherAge;
    }

    public String getpAge() {
        return pAge;
    }

    public void setpAge(String pAge) {
        this.pAge = pAge;
    }

    public int getPrbNum() {
        return prbNum;
    }

    public void setPrbNum(int prbNum) {
        this.prbNum = prbNum;
    }

    public BmobFile getpUserHeadImage() {
        return pUserHeadImage;
    }

    public void setpUserHeadImage(BmobFile pUserHeadImage) {
        this.pUserHeadImage = pUserHeadImage;
    }

    public String getpUniversity() {
        return pUniversity;
    }

    public void setpUniversity(String pUniversity) {
        this.pUniversity = pUniversity;
    }

    public String getpSubject() {
        return pSubject;
    }

    public void setpSubject(String pSubject) {
        this.pSubject = pSubject;
    }

    public String getpIntroduce() {
        return pIntroduce;
    }

    public void setpIntroduce(String pIntroduce) {
        this.pIntroduce = pIntroduce;
    }
}
