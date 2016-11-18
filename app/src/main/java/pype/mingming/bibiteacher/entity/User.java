package pype.mingming.bibiteacher.entity;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;

/**
 * two,日后和需求再改动User...
 * Created by mingming on 2016/6/28.
 */
public class User extends BmobUser {
    /**
     * 性别 true代表男 false代表女
     */
    private Boolean sex = true;
    /**
     * 性别 true代表学生 false代表家长
     */
    private Boolean isStudent;

    /**
     * 用户头像
     */
    private BmobFile avatar;

    /**
     * 个性签名
     */
    private String signature;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 出生日期
     */
    private String birthDay;

    /**
     * 地址
     */
    private String address;

    /**
     * 支付宝账号
     */
    private String alipayAccount;

    /**
     * 被举报数
     */
    private Integer badReport;


    //用户照片墙图片
    private BmobFile imageWall1;
    private BmobFile imageWall2;
    private BmobFile imageWall3;
    //
    private BmobFile imageWall;

    public BmobFile getImageWall() {
        return imageWall;
    }

    public void setImageWall(BmobFile imageWall) {
        this.imageWall = imageWall;
    }

    //背景
    public String background;

    public BmobFile getImageWall3() {
        return imageWall3;
    }

    public void setImageWall3(BmobFile imageWall3) {
        this.imageWall3 = imageWall3;
    }

    public BmobFile getImageWall2() {
        return imageWall2;
    }

    public void setImageWall2(BmobFile imageWall2) {
        this.imageWall2 = imageWall2;
    }

    public BmobFile getImageWall1() {
        return imageWall1;
    }

    public void setImageWall1(BmobFile imageWall1) {
        this.imageWall1 = imageWall1;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAlipayAccount() {
        return alipayAccount;
    }

    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }

    public Integer getBadReport() {
        return badReport;
    }

    public void setBadReport(Integer badReport) {
        this.badReport = badReport;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setIsStudent(Boolean isStudent) {
        this.isStudent = isStudent;
    }
    public Boolean getIsStudent() {
        return isStudent;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }





    public BmobFile getAvatar() {
        return avatar;
    }

    public void setAvatar(BmobFile avatar) {
        this.avatar = avatar;
    }
}
