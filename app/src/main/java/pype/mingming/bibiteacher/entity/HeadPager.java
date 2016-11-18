package pype.mingming.bibiteacher.entity;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 页码 id  （0-3）
 图片 picture
 top标题 topTitle
 内容标题 contentTitle
 内容图片 contentPicture
 内容文本 content

 //待定这几列

 日后想点击首届焦点图直接启动一个Activity，里面就一个Web控件（现在没有网页端所以无法实现）
 * Created by mingming on 2016/10/14.
 */

public class HeadPager extends BmobObject {
    private Integer id;
    private BmobFile picture;
    private String topTitle;
    private String contentTitle;
    private BmobFile contentPicture;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BmobFile getContentPicture() {
        return contentPicture;
    }

    public void setContentPicture(BmobFile contentPicture) {
        this.contentPicture = contentPicture;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BmobFile getPicture() {
        return picture;
    }

    public void setPicture(BmobFile picture) {
        this.picture = picture;
    }

    public String getTopTitle() {
        return topTitle;
    }

    public void setTopTitle(String topTitle) {
        this.topTitle = topTitle;
    }
}
