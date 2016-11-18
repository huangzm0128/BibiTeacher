package pype.mingming.bibiteacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.GetListener;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.entity.Orders;
import pype.mingming.bibiteacher.entity.User;
import pype.mingming.bibiteacher.myhomepage.TopBar;
import pype.mingming.bibiteacher.ui.CircleImageView;
import pype.mingming.bibiteacher.utils.LogUtils;
import pype.mingming.bibiteacher.utils.ToastUtils;

/**
 * 通过传过来的orderId显示订单详情
 * Created by mingming on 2016/10/9.
 */

public class OrderDetailsActivity extends BaseActivity {
    @BindView(R.id.myTopBar)
    TopBar myTopBar;
    @BindView(R.id.order_money)
    TextView orderMoney;
    @BindView(R.id.order_userIcon)
    CircleImageView orderUserIcon;
    @BindView(R.id.order_userName)
    TextView orderUserName;
    @BindView(R.id.order_otherUserName)
    TextView orderOtherUserName;
    @BindView(R.id.order_otherUserPhone)
    TextView orderOtherUserPhone;
    @BindView(R.id.order_location)
    TextView orderLocation;
    @BindView(R.id.order_time)
    TextView orderTime;
    @BindView(R.id.order_create_time)
    TextView orderCreateTime;
    @BindView(R.id.order_subject)
    TextView orderSubject;
    @BindView(R.id.order_grade)
    TextView orderGrade;

    private String ordersId;
    private User user;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_details;
    }

    @Override
    protected void initDada() {
        super.initDada();
        myTopBar.getLeftImageButtonn().setImageResource(R.drawable.back_white_top);
        myTopBar.getLeftImageButtonn().setVisibility(View.VISIBLE);
        myTopBar.getRightButton().setVisibility(View.GONE);

        Intent intent = getIntent();
        ordersId = intent.getStringExtra("ordersId"); //传过来的OrderId
        user = BmobUser.getCurrentUser(context, User.class);

        BmobQuery<Orders> query = new BmobQuery<Orders>();
        //查询Order表并把unOrderBmob表和UnOrderBmob里的employee的avatar和username列一起查询出来，
        // Post表和Post表里的postUser中的employee的avatar和username列起查询出来
        query.include("unOrderBmob.employee[avatar|username|objectId],unOrderBmob.employer[avatar|username|objectId],post.postUser[avatar|username|objectId]");
        query.getObject(this, ordersId, new GetListener<Orders>() {

            @Override
            public void onSuccess(Orders object) {
                getOrder(object);
            }

            @Override
            public void onFailure(int code, String arg0) {
                ToastUtils.showToast(context, "获取失败", Toast.LENGTH_SHORT);
                LogUtils.i("1111", code + arg0);
            }

        });

    }

    @Override
    protected void setListener() {
        super.setListener();
        myTopBar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftButtonOnClick() {
                //导航栏的左菜单监听事件
                onBackPressed();
            }

            @Override
            public void rightButtonOnClick() {
            }
        });
    }

    /**
     * 获取订单数据并显示
     *
     * @param object
     */
    private void getOrder(Orders object) {

        LogUtils.i("1111", "查询成功");
        //大学生应聘家教
        if (object.getUnOrderBmob()!= null &&
                user.getObjectId().equals(object.getUnOrderBmob().getEmployer().getObjectId())) { //当前用户为雇主(来至大学生应聘简历)
            LogUtils.i("1111", "当前用户为雇主(来至大学生应聘简历)");
            //获得金额的信息
            orderMoney.setText("-" + object.getUnOrderBmob().getMoney());
            if (object.getUnOrderBmob().getEmployee().getAvatar() != null) {
                Picasso.with(context)
                        .load(object.getUnOrderBmob().getEmployee().getAvatar().getFileUrl(context))
                        .into(orderUserIcon);
            }
            orderUserName.setText(object.getUnOrderBmob().getEmployee().getUsername());
            orderOtherUserName.setText(object.getUnOrderBmob().getEmployee().getUsername());
            orderOtherUserPhone.setText(object.getUnOrderBmob().getPhone());
            orderLocation.setText(object.getUnOrderBmob().getAddress());
            orderSubject.setText(object.getUnOrderBmob().getSubject().substring(0, 2));
            orderGrade.setText(object.getUnOrderBmob().getSubject().substring(5));
            orderTime.setText(object.getUnOrderBmob().getTime());
            //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
            orderCreateTime.setText(object.getCreatedAt());


        } else if (object.getUnOrderBmob() != null &&
                user.getObjectId().equals(object.getUnOrderBmob().getEmployee().getObjectId())) {//当前用户为雇员(来至大学生应聘简历)

            orderMoney.setText("+" + object.getUnOrderBmob().getMoney());
            if (object.getUnOrderBmob().getEmployer().getAvatar() != null) {
                Picasso.with(context)
                        .load(object.getUnOrderBmob().getEmployer().getAvatar().getFileUrl(context))
                        .into(orderUserIcon);
            }
            orderUserName.setText(object.getUnOrderBmob().getEmployer().getUsername());
            orderOtherUserName.setText(object.getUnOrderBmob().getEmployer().getUsername());
            orderOtherUserPhone.setText(object.getUnOrderBmob().getPhone());
            orderLocation.setText(object.getUnOrderBmob().getAddress());
            orderSubject.setText(object.getUnOrderBmob().getSubject().substring(0, 2));
            orderGrade.setText(object.getUnOrderBmob().getSubject().substring(3));
            orderTime.setText(object.getUnOrderBmob().getTime());
            //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
            orderCreateTime.setText(object.getCreatedAt());

        } else if (object.getPost()!= null &&
                user.getObjectId().equals(object.getPost().getPostUser().getObjectId())) {//当前用户为雇主(来至家教帖子)

            LogUtils.i("1111", "当前用户为雇主(来至家教发帖子)");
            //获得金额的信息
            orderMoney.setText("-" + object.getPost().getPrice());
            BmobQuery<User> query = new BmobQuery<User>();
            query.getObject(context, object.getEmployeeId(), new GetListener<User>() {
                @Override
                public void onSuccess(User user1) {
                    if (user1.getAvatar() != null) {
                        Picasso.with(context)
                                .load(user1.getAvatar().getFileUrl(context))
                                .into(orderUserIcon);
                    }
                    orderUserName.setText(user1.getUsername());
                    orderOtherUserName.setText(user1.getUsername());
                }

                @Override
                public void onFailure(int i, String s) {

                }
            });
            orderOtherUserPhone.setText(object.getPost().getPhone());
            orderLocation.setText(object.getPost().getTeachAddress());
            orderSubject.setText(object.getPost().getSubject());
            orderGrade.setText(object.getPost().getGrade());
            orderTime.setText(object.getPost().getStartTime().getDate().toString().substring(5, 16) +
                    " --- " + object.getPost().getEndTime().getDate().toString().substring(5, 16)); // 截取月日时分(5,16)
            //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
            orderCreateTime.setText(object.getCreatedAt());


        } else if (user.getObjectId().equals(object.getEmployeeId())) {//如果当前用户为雇员
            LogUtils.i("1111", "当前用户为雇员(来至家教帖子)");

            //获得金额的信息
            orderMoney.setText("+" + object.getPost().getPrice());

            if(object.getPost().getPostUser().getAvatar() != null){
                Picasso.with(context)
                        .load(object.getPost().getPostUser().getAvatar().getFileUrl(context))
                        .into(orderUserIcon);
            }
            orderUserName.setText(object.getPost().getPostUser().getUsername());
            orderOtherUserName.setText(object.getPost().getPostUser().getUsername());
            orderOtherUserPhone.setText(object.getPost().getPhone());
            orderLocation.setText(object.getPost().getTeachAddress());
            orderSubject.setText(object.getPost().getSubject());
            orderGrade.setText(object.getPost().getGrade());
            orderTime.setText(object.getPost().getStartTime().getDate().toString().substring(5, 16) +
                    " --- " + object.getPost().getEndTime().getDate().toString().substring(5, 16)); // 截取月日时分(5,16)
            //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
            orderCreateTime.setText(object.getCreatedAt());

        }
    }
}


