package pype.mingming.bibiteacher.entity;

import java.util.Date;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;
import pype.mingming.bibiteacher.entity.User;

/**
 * Created by wushaohongly on 2016/7/27.
 * 一条帖子所包含的内容
 */
public class Post extends BmobObject{

    private User postUser;     //发帖人
    private String teachAddress;        //家教地址
    private String subject;         //家教科目
    private String grade;           //家教年级
    private String postTitle;        //标题
    private String postContent;        //正文内容
    private BmobDate startTime;        //开始时间
    private BmobDate endTime;          //结束时间
    private Double price;           //价格
    private Integer readNum = 0;
    private Integer likeNumber = 0;
    private String phone;
    private boolean isValue = true;
    private boolean isDelete = false;

    private PostPicture postPicture;

    public User getPostUser() {
        return postUser;
    }

    public void setPostUser(User postUser) {
        this.postUser = postUser;
    }

    public String getTeachAddress() {
        return teachAddress;
    }

    public void setTeachAddress(String postAddress) {
        this.teachAddress = postAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public BmobDate getStartTime() {
        return startTime;
    }

    public void setStartTime(BmobDate startTime) {
        this.startTime = startTime;
    }

    public BmobDate getEndTime() {
        return endTime;
    }

    public void setEndTime(BmobDate endTime) {
        this.endTime = endTime;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public PostPicture getPostPicture() {
        return postPicture;
    }

    public void setPostPicture(PostPicture postPicture) {
        this.postPicture = postPicture;
    }

    public Integer getLikeNumber() {
        return likeNumber;
    }

    public void setLikeNumber(Integer likeNumber) {
        this.likeNumber = likeNumber;
    }

    public Integer getReadNum() {
        return readNum;
    }

    public void setReadNum(Integer readNum) {
        this.readNum = readNum;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isValue() {
        return isValue;
    }

    public void setValue(boolean value) {
        isValue = value;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }
}
