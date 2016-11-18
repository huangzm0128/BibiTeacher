package pype.mingming.bibiteacher.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.entity.BmobData;
import pype.mingming.bibiteacher.entity.PluralistAdapter;
import pype.mingming.bibiteacher.entity.PluralistBmob;

/**
 * Created by mk on 2016/9/24.
 */
public class AtyRecyclerView extends Activity implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private PluralistAdapter adapter;
    private List<PluralistBmob> pluralistBmobList = new ArrayList<PluralistBmob>();
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listviewtest);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        listView = (ListView) findViewById(R.id.lv);
        BmobData bmobData = new BmobData();
        bmobData.getgetAllUniversitierChlick(this, new BmobData.getInterfacechlick() {
            @Override
            public void getData(List<PluralistBmob> list) {
                adapter = new PluralistAdapter(AtyRecyclerView.this,list);
                Log.e("AtyRecyclerView",list.size()+"");
                listView.setAdapter(adapter);
            }
        });
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
    }

    @Override
    public void onRefresh() {
        new Thread(new Runnable() {//下拉触发的函数，这里是谁1s然后加入一个数据，然后更新界面
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
        }
        }).start();
    }
    private MyHandler handler = new MyHandler();

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    swipeRefreshLayout.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }
}
