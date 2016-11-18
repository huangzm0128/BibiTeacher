package pype.mingming.bibiteacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.adapter.ItemClickListener;
import pype.mingming.bibiteacher.adapter.OrdersAdapter;
import pype.mingming.bibiteacher.entity.Orders;
import pype.mingming.bibiteacher.entity.User;
import pype.mingming.bibiteacher.myhomepage.TopBar;
import pype.mingming.bibiteacher.ui.SpaceItemDecoration;
import pype.mingming.bibiteacher.utils.CustomProgressDialog;

/**
 * Created by mingming on 2016/10/11.
 */

public class OrdersActivity extends BaseActivity implements ItemClickListener {
    @BindView(R.id.myTopBar)
    TopBar myTopBar;
    @BindView(R.id.error_rl)
    RelativeLayout errorRl;
    @BindView(R.id.order_item_recyclerview)
    RecyclerView orderItemRecyclerview;

    final int MESSAGE_OK = 1;
    @BindView(R.id.error_tv)
    TextView errorTv;
    private OrdersAdapter adapter;
    private User user;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_OK:
                    Log.i("info", "更新");
//                    progress_dialog.setVisibility(View.GONE);
                    errorRl.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_list;
    }

    @Override
    protected void initDada() {
        super.initDada();
        myTopBar.getLeftImageButtonn().setImageResource(R.drawable.back_white_top);
        myTopBar.getLeftImageButtonn().setVisibility(View.VISIBLE);
        myTopBar.getRightButton().setVisibility(View.GONE);
        errorRl.setVisibility(View.GONE);
        adapter = new OrdersAdapter(context);
        orderItemRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        orderItemRecyclerview.addItemDecoration(new SpaceItemDecoration(5));
        user = BmobUser.getCurrentUser(context, User.class);
        BmobQuery();
    }

    @Override
    protected void setListener() {
        super.setListener();
        myTopBar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftButtonOnClick() {
                onBackPressed();
            }

            @Override
            public void rightButtonOnClick() {

            }
        });

        adapter.setOnItemClickListener(this);

        errorRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobQuery();
            }
        });

    }

    @Override
    public void onItemClick(View view, int postion) {
        Log.i("info", "点击");

        String ordersId = adapter.getList().get(postion).getObjectId();
        Log.i("info", ordersId);
        Intent intent = new Intent(OrdersActivity.this, OrderDetailsActivity.class);
        intent.putExtra("ordersId", ordersId);
        startActivity(intent);

    }

    private void BmobQuery() {
        //bmob查询
        BmobQuery<Orders> query = new BmobQuery<Orders>();
        query.include("unOrderBmob.employee,unOrderBmob.employer,post.postUser");
        if(user.getIsStudent() != null){
            final CustomProgressDialog dialog = new CustomProgressDialog(OrdersActivity.this, "网络君正在奔跑..", R.drawable.myprogressframe);
            dialog.show();

            query.order("-createdAt");  //时间倒序查询
            query.findObjects(context, new FindListener<Orders>() {

                @Override
                public void onSuccess(List<Orders> list) {
                    dialog.dismiss();
                    Log.i("info", "查询成功");
                    if (list.size() != 0) {
                        List<Orders> myList = new ArrayList<Orders>();
                        for(int i=0; i<list.size(); i++){
                            if(list.get(i).getUnOrderBmob() != null &&
                                    list.get(i).getUnOrderBmob().getEmployee().getObjectId().equals(user.getObjectId()) ||
                                    list.get(i).getUnOrderBmob() != null &&
                                            list.get(i).getUnOrderBmob().getEmployer().getObjectId().equals(user.getObjectId()) ||
                                    list.get(i).getPost()!= null &&
                                    list.get(i).getPost().getPostUser().getObjectId().equals(user.getObjectId()) ||
                                    list.get(i).getEmployeeId() != null && list.get(i).getEmployeeId().equals(user.getObjectId())){
                                myList.add(list.get(i));
                            }
                        }
                        if(myList.size() == 0){
                            errorTv.setText("你的订单空空的");
                            errorRl.setVisibility(View.VISIBLE);
                            errorRl.setEnabled(false);
                        }else {
                            adapter.setList(myList);   //查询结果添加到adapter
                            orderItemRecyclerview.setAdapter(adapter);    //设置数据源
                            Message message = new Message();
                            message.what = MESSAGE_OK;
                            handler.sendMessage(message);
                        }
                    } else {
                        errorTv.setText("你的订单空空的");
                        errorRl.setVisibility(View.VISIBLE);
                        errorRl.setEnabled(false);
                    }
                }

                @Override
                public void onError(int i, String s) {
                    dialog.dismiss();
                    errorRl.setEnabled(true);
                    errorRl.setVisibility(View.VISIBLE);
                }
            });
        }

    }

}
