package pype.mingming.bibiteacher.entity;

import java.util.ArrayList;

/**
 * 首页中标题栏下的位置的图片滚动焦点图Bean
 * Created by mingming on 2016/7/24.
 */
public class HeadPagersItemBean extends MainItemBean{
    /**
     * 图片地址
     */
    private ArrayList<String> imgUrl;

    /**
     * 图片意图
     */
    private ArrayList<BeanType>imgIntent;

    /**
     * 意图目标
     */
    private ArrayList<String> intentMsg;

    public HeadPagersItemBean(){
        this.imgUrl = new ArrayList<String>();
        this.imgIntent = new ArrayList<BeanType>();
        this.intentMsg = new ArrayList<String>();
    }
    public HeadPagersItemBean(ArrayList<String> imgUrl, ArrayList<BeanType>imgIntent, ArrayList<String> intentMsg){
        this.imgUrl = imgUrl;
        this.imgIntent = imgIntent;
        this.intentMsg = intentMsg;
    }

    /**
     * 返回指定下标的图片的URl
     * @param i 下标
     * @return 图片的URl
     */
    public String getImgUrl(int i){
        String result = "";
        if(imgUrl != null){
            result = imgUrl.get(i);
        }
        return result;
    }

    /**
     *返回imgUrl里图片URL的数量
     * @return
     */
    public int getImgUrlSize(){
        int s = 0;
        if(imgUrl != null){
            s = imgUrl.size();
        }
        return s;
    }

    /**
     *返回imgUrl
     * @return
     */
    public ArrayList<String> getImgUrl(){ return imgUrl;}

    public void setImgUrl(ArrayList<String> imgUrl){this.imgUrl = imgUrl;}

    public BeanType getImgIntent(int i){
        BeanType result = BeanType.DO_NOTHING;
        if(imgIntent != null){
            result = imgIntent.get(i);
        }
        return result;
    }

    public void setIntentMsg(ArrayList<String> intentMsg) {
        this.intentMsg = intentMsg;
    }

    /**
     * 添加加图片URl到imgUrl
     * @param url 图片的Url
     */
    public void addImgUrl(String url){
        if(imgUrl==null)
            return ;
        this.imgUrl.add(url);
    }
    public void addIntentMsg(String msg){
        if(intentMsg==null)
            return ;
        this.intentMsg.add(msg);
    }
    public void addImgIntent(BeanType type){
        if(imgIntent==null)
            return ;
        this.imgIntent.add(type);
    }

    public int getImgIntentSize(){return imgIntent.size();}
    public int getIntentMsgSize(){return intentMsg.size();}

}
