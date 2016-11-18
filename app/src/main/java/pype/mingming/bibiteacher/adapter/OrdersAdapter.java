package pype.mingming.bibiteacher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.entity.Orders;
import pype.mingming.bibiteacher.entity.Post;
import pype.mingming.bibiteacher.entity.UnOrderBmob;
import pype.mingming.bibiteacher.entity.User;
import pype.mingming.bibiteacher.ui.CircleImageView;
import pype.mingming.bibiteacher.utils.LogUtils;
import pype.mingming.bibiteacher.utils.TimeUtils;

/**
 * 订单适配器
 *
 * 那么有了ListView、GridView为什么还需要RecyclerView这样的控件呢？整体上看RecyclerView架构，提供了一种插拔式的体验，高度的解耦，异常的灵活，通过设置它提供的不同LayoutManager，ItemDecoration , ItemAnimator实现令人瞠目的效果。

 你想要控制其显示的方式，请通过布局管理器LayoutManager
 你想要控制Item间的间隔（可绘制），请通过ItemDecoration
 你想要控制Item增删的动画，请通过ItemAnimator
 你想要控制点击、长按事件，请自己写（擦，这点尼玛。）
 * Created by mingming on 2016/10/9.
 */

public class OrdersAdapter extends RecyclerView.Adapter {
    List<Orders> lists;
    ItemClickListener listener;
    private Context context;
    private User user;

    private  final int WEEKDAYS = 7;
    private final  String[] WEEK = {"周日","周一","周二","周三","周四","周五","周六"};


    public OrdersAdapter(Context context){
        this.context = context;
        user = BmobUser.getCurrentUser(context, User.class);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_item, null), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        //获取当前的Item
        Orders i = lists.get(position);
        Date date = null;
        String weekDay = "";
        String createDay = i.getCreatedAt().substring(5,11); // 截取月日

        //格式化时间
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd");
        String stime = i.getCreatedAt();
        try {
            date = formatter.parse(stime);
            if(date != null){
                weekDay = DateToWeek(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        viewHolder.getWeekDay().setText(weekDay);
        viewHolder.getCreatDate().setText(createDay);
//        LogUtils.i("1111", "getPost："+i.getPost().getPostUser().getUsername());
//        LogUtils.i("1111", "getEmployer："+i.getUnOrderBmob().getEmployer().getUsername());

        if(user.getIsStudent()){ //当前用户为大学生
            //大学生应聘简历
            if(i.getUnOrderBmob() != null){
                LogUtils.i("1111", "getEmployer："+i.getUnOrderBmob().getEmployer().getUsername());
                if(i.getUnOrderBmob().getEmployer().getAvatar() != null){
                    Picasso.with(context)
                            .load(i.getUnOrderBmob().getEmployer().getAvatar().getUrl())
                            .into(viewHolder.getUserIcon());
                }
                viewHolder.getTotalAmountHired().setText("+" + i.getUnOrderBmob().getMoney());

            }else {

                if(i.getPost().getPostUser().getAvatar() != null){
                    Picasso.with(context)
                            .load(i.getPost().getPostUser().getAvatar().getUrl())
                            .into(viewHolder.getUserIcon());
                }
                viewHolder.getTotalAmountHired().setText("+" + i.getPost().getPrice());
            }

        }else {//当前用户为家长

            //大学生应聘简历
            if(i.getUnOrderBmob() != null){
               if(i.getUnOrderBmob().getEmployee().getAvatar() != null){
                   Picasso.with(context)
                           .load(i.getUnOrderBmob().getEmployee().getAvatar().getFileUrl(context))
                           .into(viewHolder.getUserIcon());

               }
                viewHolder.getTotalAmountHired().setText("-" + i.getUnOrderBmob().getMoney());


            }else {
                if(i.getEmployeeId() != null){
                    BmobQuery<User> query = new BmobQuery<User>();
                    query.getObject(context, i.getEmployeeId(), new GetListener<User>() {
                        @Override
                        public void onSuccess(User user1) {
                            if (user1.getAvatar() != null) {
                                Picasso.with(context)
                                        .load(user1.getAvatar().getFileUrl(context))
                                        .into(viewHolder.getUserIcon());
                            }
                        }

                        @Override
                        public void onFailure(int i, String s) {

                        }
                    });
                    viewHolder.getTotalAmountHired().setText("-" + i.getPost().getPrice());
                }
            }

        }

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    //数据列表list的set和get方法
    public List<Orders> getList() {
        return lists;
    }
    public void setList(List<Orders> lists) {
        this.lists = lists;
        notifyDataSetChanged();
    }

    /**
     * 日期变量转成对应的星期字符串
     * @param date
     * @return
     */
    private String DateToWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayIndex < 1 || dayIndex > WEEKDAYS) {
            return null;
        }

        return WEEK[dayIndex - 1];
    }

    public void setOnItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //recyclerView要
        private ItemClickListener listener;
        private TextView weekDay;
        private TextView createDate;
        private CircleImageView userIcon;
        private TextView totalAmountHired;

        public ViewHolder(View itemView,ItemClickListener listener) {
            super(itemView);
            this.listener = listener;
            createDate = (TextView) itemView.findViewById(R.id.order_list_item_date);
            weekDay = (TextView) itemView.findViewById(R.id.order_list_item_week_date);
            userIcon = (CircleImageView) itemView.findViewById(R.id.order_list_item_userIcon);
            totalAmountHired = (TextView) itemView.findViewById(R.id.order_list_item_money);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(listener != null){
                listener.onItemClick(v,this.getPosition());
            }
        }

        public TextView getWeekDay(){
            return this.weekDay;
        }
        public TextView getCreatDate(){
            return this.createDate;
        }
        public CircleImageView getUserIcon(){
            return this.userIcon;
        }
        public TextView getTotalAmountHired(){
            return this.totalAmountHired;
        }
    }
}
