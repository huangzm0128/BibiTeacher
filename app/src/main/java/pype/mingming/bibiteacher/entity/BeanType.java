package pype.mingming.bibiteacher.entity;

/**
 * 枚举类
 * Created by mingming on 2016/7/24.
 */
public enum BeanType {
    /**
     * 什么也不做
     */
    DO_NOTHING(0),

    /**
     * 1.跳转到首页
     */
    GO_TO_MAIN(1),

    /**
     * 跳转到个人信息页
     */
    GO_TO_PERSON_MSG(2);

    private int type;
    private BeanType(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
