package pype.mingming.bibiteacher.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by wushaohongly on 2016/10/9.
 */

public class Applicants extends BmobObject {

    private Post post;
    private User user;

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
