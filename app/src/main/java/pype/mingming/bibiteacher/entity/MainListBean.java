package pype.mingming.bibiteacher.entity;

import java.util.ArrayList;

/**
 * 首页列表
 * Created by mingming on 2016/7/24.
 */
public class MainListBean {
    /**
     * 轮播图
     */
    public static final int HeadPagersItem = 101;

    /**
     * item的类型
     */
    private ArrayList<Integer> types;
    /**
     * item的属性
     */
    private ArrayList<MainItemBean> beans;
    public ArrayList<Integer> getTypes() {
        return types;
    }
    public Integer getTypes(int i) {
        return types.get(i);
    }
    public int getTypesSize() {
        return types.size();
    }
    public void setTypes(ArrayList<Integer> types) {
        this.types = types;
    }
    public ArrayList<MainItemBean> getBeans() {
        return beans;
    }
    public Object getBeans(int i) {
        return beans.get(i);
    }
    public int getBeansSize() {
        return beans.size();
    }
    public void setBeans(ArrayList<MainItemBean> beans) {
        this.beans = beans;
    }
    public void addBean(MainItemBean o){
        beans.add(o);
    }
    public void addType(int i){
        types.add(i);
    }
    public MainListBean() {
        super();
        this.types = new ArrayList<Integer>();
        this.beans = new ArrayList<MainItemBean>();
    }
    public MainListBean(ArrayList<Integer> types, ArrayList<MainItemBean> beans) {
        super();
        this.types = types;
        this.beans = beans;
    }
}
