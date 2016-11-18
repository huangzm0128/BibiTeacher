package pype.mingming.bibiteacher.entity;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by mk on 2016/7/26.
 */
public class UserDate {
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPersonality() {
        return personality;
    }

    public void setPersonality(String personality) {
        this.personality = personality;
    }

    public String getPostNum() {
        return postNum;
    }

    public void setPostNum(String postNum) {
        this.postNum = postNum;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
    public void setData(String data){
        this.data = data;
    }
    public String getData(){
        return data;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public String getAddress(){
        return address;
    }
    public String getImageWallPath() {
        return imageWallPath;
    }

    public void setImageWallPath(String imageWallPath) {
        this.imageWallPath = imageWallPath;
    }

    public String getUserHeadPath() {
        return userHeadPath;
    }

    public void setUserHeadPath(String userHeadPath) {
        this.userHeadPath = userHeadPath;
    }
    public BmobFile getBrackground() {
        return background;
    }

    public void setBrackground(BmobFile brackground) {
        this.background = background;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String email) {
        this.realName = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String userName;
    private String age;
    private String data;
    private Boolean sex;
    private String school;
    private String subject;
    private String postNum;
    private String address;
    private String personality;//个性签名
    private String imageWallPath;//图片墙地址
    private String userHeadPath;//头像图片地址
    private BmobFile background;//背景
    private String phone;//手机
    private String realName;//真实姓名


}
