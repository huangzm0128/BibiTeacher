package pype.mingming.bibiteacher.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.listener.ValueEventListener;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.adapter.MKOrderAdapter;
import pype.mingming.bibiteacher.entity.BmobData;
import pype.mingming.bibiteacher.entity.UnOrderBmob;
import pype.mingming.bibiteacher.entity.User;
import pype.mingming.bibiteacher.myhomepage.TopBar;

/**
 * Created by mk on 2016/10/13.
 */
public class AtyMkOrderNew extends Activity implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private BmobData bmobData = new BmobData();
    private User user;
    private ListView listView;
    private MKOrderAdapter adapter;
    private LinearLayout linearLayout;
    private TopBar mkorderlist_topBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MyHandler handler = new MyHandler();
    private BmobRealTimeData data = new BmobRealTimeData();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_mkorderlist);
        user = (User) getIntent().getSerializableExtra("user");
        init();
        listView = (ListView) findViewById(R.id.mkOrderlist);
        adapter = new MKOrderAdapter(AtyMkOrderNew.this,new ArrayList<UnOrderBmob>(),user);
        linearLayout = (LinearLayout) findViewById(R.id.showNews);
        mkorderlist_topBar = (TopBar) findViewById(R.id.mkorderlist_topBar);
        mkorderlist_topBar.getLeftImageButtonn().setImageResource(R.drawable.back_white_top);
        mkorderlist_topBar.getLeftImageButtonn().setVisibility(View.VISIBLE);
        mkorderlist_topBar.getRightButton().setVisibility(View.GONE);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        //设置刷新时动画的颜色，可以设置4个
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        // 这句话是为了，第一次进入页面的时候显示加载进度条
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        swipeRefreshLayout.setOnRefreshListener(this);

        bmobData.getorderInterfaceCherck(this, user, new BmobData.orderInterface() {
            @Override
            public void getData(List<UnOrderBmob> odBmobList) {
                if (odBmobList.size()==0){
                    adapter = new MKOrderAdapter(AtyMkOrderNew.this,odBmobList,user);
                    linearLayout.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.INVISIBLE);
                }else {
                    linearLayout.setVisibility(View.INVISIBLE);
                    listView.setVisibility(View.VISIBLE);
                    adapter = new MKOrderAdapter(AtyMkOrderNew.this,odBmobList,user);
                    listView.setAdapter(adapter);
                }

            }
        });
        mkorderlist_topBar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftButtonOnClick() {
                onBackPressed();
            }
            @Override
            public void rightButtonOnClick() {

            }
        });
        listView.setOnItemClickListener(this);
    }
    public void init(){
        data.start(this, new ValueEventListener() {
            @Override
            public void onConnectCompleted() {
                if(data.isConnected()){
                    data.subTableUpdate("UnOrderBmob");
                }
            }
            @Override
            public void onDataChange(JSONObject jsonObject) {
                if (BmobRealTimeData.ACTION_UPDATETABLE.equals(jsonObject.optString("action"))) {
                    bmobData.getorderInterfaceCherck(AtyMkOrderNew.this, user, new BmobData.orderInterface() {
                        @Override
                        public void getData(List<UnOrderBmob> odBmobList) {
                            if (odBmobList.size()==0){
                                linearLayout.setVisibility(View.VISIBLE);
                                listView.setVisibility(View.INVISIBLE);
                            }else {
                                linearLayout.setVisibility(View.INVISIBLE);
                                listView.setVisibility(View.VISIBLE);
                                adapter = new MKOrderAdapter(AtyMkOrderNew.this,odBmobList,user);
                                listView.setAdapter(adapter);
                            }

                        }
                    });
                }
            }
        });
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i  = new Intent(AtyMkOrderNew.this,AtyMkOrderMore.class);
        i.putExtra("UnOrderBmob",adapter.getItem(position));
        i.putExtra("user",user);
        startActivity(i);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        new Thread(new Runnable() {//下拉触发的函数，这里是谁1s然后加入一个数据，然后更新界面
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    bmobData.getorderInterfaceCherck(AtyMkOrderNew.this, user, new BmobData.orderInterface() {
                        @Override
                        public void getData(List<UnOrderBmob> odBmobList) {
                            if (odBmobList.size()==0){
                                
                            }else {
                                linearLayout.setVisibility(View.INVISIBLE);
                                listView.setVisibility(View.VISIBLE);
                                adapter = new MKOrderAdapter(AtyMkOrderNew.this,odBmobList,user);
                                listView.setAdapter(adapter);
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        }).start();
    }
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    swipeRefreshLayout.setRefreshing(false);
                    //adapter.notifyDataSetChanged();
                    Toast.makeText(AtyMkOrderNew.this,"刷新成功",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }
}
