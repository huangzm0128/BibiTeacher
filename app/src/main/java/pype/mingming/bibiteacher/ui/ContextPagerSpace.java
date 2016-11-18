package pype.mingming.bibiteacher.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.List;

import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.listener.ValueEventListener;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.activity.Atyinformation_recycler;
import pype.mingming.bibiteacher.entity.BmobData;
import pype.mingming.bibiteacher.entity.PluralistAdapter;
import pype.mingming.bibiteacher.entity.PluralistBmob;

/**
 * Created by mk on 2016/7/27.
 */
public class ContextPagerSpace extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, View.OnClickListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private PluralistAdapter adapter;
    private TextView tv_getNear,tv_getAll,tv_getOther;
    private Context context = null;
    private BmobRealTimeData data = new BmobRealTimeData();
    private BmobData bmobData = new BmobData();


    public ContextPagerSpace() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();

        if (getArguments() != null) {
        }
        View view = inflater.inflate(R.layout.content_main_pagers_space, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        listView = (ListView) view.findViewById(R.id.lv);
        bmobData.getgetAllUniversitierChlick(context, new BmobData.getInterfacechlick() {
            @Override
            public void getData(List<PluralistBmob> list) {
                adapter = new PluralistAdapter(context, list);
                listView.setAdapter(adapter);
            }
        });
        init();
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
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if(listView != null && listView.getChildCount() > 0){
                    // check if the first item of the list is visible
                    boolean firstItemVisible = listView.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = listView.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                swipeRefreshLayout.setEnabled(enable);
            }});
        tv_getNear = (TextView) view.findViewById(R.id.tv_getNear);
        tv_getAll = (TextView) view.findViewById(R.id.tv_getAll);
        tv_getOther = (TextView) view.findViewById(R.id.tv_getOther);
        tv_getNear.setOnClickListener(this);
        tv_getAll.setOnClickListener(this);
        tv_getOther.setOnClickListener(this);
        return view;
    }
    public void init(){
        data.start(getContext(), new ValueEventListener() {
            @Override
            public void onConnectCompleted() {
                if(data.isConnected()){
                    data.subTableUpdate("PluralistBmob");
                }
            }

            @Override
            public void onDataChange(JSONObject jsonObject) {
                if (BmobRealTimeData.ACTION_UPDATETABLE.equals(jsonObject.optString("action"))) {
                    bmobData.getgetAllUniversitierChlick(context, new BmobData.getInterfacechlick() {
                        @Override
                        public void getData(List<PluralistBmob> list) {
                            adapter = new PluralistAdapter(context, list);
                            Log.e("onDataChange", list.size() + "这是数据同步");
                            adapter.notifyDataSetChanged();
                            listView.setAdapter(adapter);
                            onCreate(getArguments());
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        new Thread(new Runnable() {//下拉触发的函数，这里是谁1s然后加入一个数据，然后更新界面
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    onCreate(null);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        }).start();
    }
    private MyHandler handler = new MyHandler();

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(getContext(), Atyinformation_recycler.class);
        intent.putExtra("PluralistBmob",adapter.getItem(position));
        startActivity(intent);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_getNear:
                bmobData.getNearUniversitierChlick(context, new BmobData.getInterfacechlick() {
                    @Override
                    public void getData(List<PluralistBmob> list) {
                        adapter = new PluralistAdapter(context, list);
                        Log.e("AtyRecyclerView", list.size() + "");
                        listView.setAdapter(adapter);
                    }
                });
                break;
            case R.id.tv_getAll:
                bmobData.getgetAllUniversitierChlick(context, new BmobData.getInterfacechlick() {
                    @Override
                    public void getData(List<PluralistBmob> list) {
                        adapter = new PluralistAdapter(context, list);
                        Log.e("onDataChange", list.size() + "这是数据同步");
                        adapter.notifyDataSetChanged();
                        listView.setAdapter(adapter);
                    }
                });
                break;
            case R.id.tv_getOther:
                Toast.makeText(getContext(),"正在开发。。",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    swipeRefreshLayout.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                    Toast.makeText(getContext(),"刷新成功",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }
}
