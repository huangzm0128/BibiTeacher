package pype.mingming.bibiteacher.entity;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * 订单基类
 * Created by mingming on 2016/10/9.
 */

public class Orders extends BmobObject {

    private String employeeId;     //应聘方ID
    private Post post;
    private UnOrderBmob unOrderBmob;
    private boolean isRead = false; // 新建订单是默认为用户未读

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public UnOrderBmob getUnOrderBmob() {
        return unOrderBmob;
    }

    public void setUnOrderBmob(UnOrderBmob unOrderBmob) {
        this.unOrderBmob = unOrderBmob;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(boolean read) {
        isRead = read;
    }
}
