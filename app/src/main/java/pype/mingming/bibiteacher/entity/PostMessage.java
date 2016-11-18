package pype.mingming.bibiteacher.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by wushaohongly on 2016/10/4.
 */

public class PostMessage extends BmobObject {

    private Post post;
    private User user;
    private String context;

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

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
