package pype.mingming.bibiteacher.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.activity.HeadPageAty;
import pype.mingming.bibiteacher.activity.PostActivity;
import pype.mingming.bibiteacher.activity.PostInfoActivity;
import pype.mingming.bibiteacher.adapter.ItemClickListener;
import pype.mingming.bibiteacher.adapter.PostAdapter;
import pype.mingming.bibiteacher.data.IndexHomeGridViewDate;
import pype.mingming.bibiteacher.entity.HeadPager;
import pype.mingming.bibiteacher.entity.HeadPagersItemBean;
import pype.mingming.bibiteacher.entity.MainListBean;
import pype.mingming.bibiteacher.entity.Post;
import pype.mingming.bibiteacher.utils.LogUtils;
import pype.mingming.bibiteacher.utils.ToastUtils;

/**
 * Created by mingming on 2016/7/27.
 */
public class ContextPagerHome extends Fragment implements MyScrollview.OnScrollListener, View.OnClickListener, ItemClickListener {
    private final int HEAD_PAGERS_ITEM_0 = 0;
    private final int HEAD_PAGERS_ITEM_1 = 1;
    private final int HEAD_PAGERS_ITEM_2 = 2;
    private final int HEAD_PAGERS_ITEM_3 = 3;
    List<String> headpages; //headpage的Url
    List<String>headPageId; //

    @BindView(R.id.home_sfl)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.head_pagers_item)
    ListView listView;
    @BindView(R.id.index_home_viewpager)
    WrapContentHeightViewPager viewPager;
    @BindView(R.id.index_home_rb1)//radiogroup 1组以及2个radiobutton
     RadioButton rb1;
    @BindView(R.id.index_home_rb2)
    RadioButton rb2;

    @BindView(R.id.myScroll)
    MyScrollview myScrollview;

    //导航栏
    @BindView(R.id.post_all_1)
    TextView all_tv_1;
    @BindView(R.id.post_all_2)
    TextView all_tv_2;
    @BindView(R.id.post_near_1)
    TextView near_tv_1;
    @BindView(R.id.post_near_2)
    TextView near_tv_2;
    @BindView(R.id.post_price_1)
    TextView price_tv_1;
    @BindView(R.id.post_price_2)
    TextView price_tv_2;

    private int searchLayoutTop;
    private boolean isPrice = false;

    @BindView(R.id.dingbu_navbar)
    LinearLayout dingbu_ll;
    @BindView(R.id.xuanfu_navbar)
    LinearLayout xuanfu_ll;

    //首页家教列表
    @BindView(R.id.teach_home_recyclerview)
    RecyclerView teach_list;
    boolean isV = false;        //顶部导航栏显示状态

    private PostAdapter adapter;

    private GridView gridView1;
    private GridView gridView2;

    //标题下的焦点图滑动
    private MainListBean bean;
    private MyFocusPictureAdapter myFPAdapter;


    public ContextPagerHome() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.comtent_main_pagers_home, container, false);
        ButterKnife.bind(this, view);

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {
                bmobQuery(); //获取headpage的url
                initGridView();
                initViewPager();
                initTeachList();
                initHeadPagers();
            }
        });

        bmobQuery(); //获取headpage的url
        initGridView();
        initViewPager();
        initTeachList();
        initHeadPagers();
        return view;
    }



    //加载首页中标题栏下的位置的图片滚动焦点图的URL
    private void initHeadPagers(){

        HeadPagersItemBean headPagersItemBean = new HeadPagersItemBean();
        if(headpages != null){
            for(int i=0; i<headpages.size(); i++){
                headPagersItemBean.addImgUrl(headpages.get(i));
            }
        }

        bean = new MainListBean();
        bean.addBean(headPagersItemBean);
        bean.addType(MainListBean.HeadPagersItem);
        //设置ListView的适配器
        listView.setAdapter(myFPAdapter = new MyFocusPictureAdapter());
    }

    private void bmobQuery() {
        headpages = new ArrayList<>();
        headPageId = new ArrayList<>();
        //bmob查询
        BmobQuery<HeadPager> query = new BmobQuery<HeadPager>();
        query.order("-createdAt");  //时间倒序查询
        query.findObjects(getContext(), new FindListener<HeadPager>() {

            @Override
            public void onSuccess(List<HeadPager> list) {
                for (int i=0; i<list.size(); i++){
                    headpages.add(list.get(i).getPicture().getUrl());
                    headPageId.add(list.get(i).getObjectId());
                }
                initHeadPagers();
                LogUtils.i("0000", "添加headpages的Url成功");
            }

            @Override
            public void onError(int i, String s) {
                LogUtils.i("0000", "错误" + s);
            }
        });

    }


    private void initGridView(){
        gridView1=(GridView) LayoutInflater.from(getActivity()).inflate(R.layout.index_home_gridview, null);
        gridView1.setAdapter(new GridViewAdapter(getActivity(), 0));
        gridView2=(GridView) LayoutInflater.from(getActivity()).inflate(R.layout.index_home_gridview, null);
        gridView2.setAdapter(new GridViewAdapter(getActivity(), 1));
    }

    private class MyFocusPictureAdapter extends BaseAdapter {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            switch (bean.getTypes(position)) {
                case MainListBean.HeadPagersItem:
                    //将滚动图赋值给listView的适配器的convertView
                    convertView = new HeadPagersItem(getContext(),
                            (HeadPagersItemBean) bean.getBeans(position), new HeadPagersItem.OnPageClickListener() {
                        @Override
                        public void onPageClick(View view, int index) {
                            switch (index){
                                case HEAD_PAGERS_ITEM_0:
                                    Intent intent0 = new Intent(getActivity(), HeadPageAty.class);
                                    intent0.putExtra("id", headPageId.get(HEAD_PAGERS_ITEM_0));
                                    startActivity(intent0);
                                    break;
                                case HEAD_PAGERS_ITEM_1:
                                    Intent intent1 = new Intent(getActivity(), HeadPageAty.class);
                                    intent1.putExtra("id", headPageId.get(HEAD_PAGERS_ITEM_1));
                                    startActivity(intent1);
                                    break;
                                case HEAD_PAGERS_ITEM_2:
                                    Intent intent2 = new Intent(getActivity(), HeadPageAty.class);
                                    intent2.putExtra("id", headPageId.get(HEAD_PAGERS_ITEM_2));
                                    startActivity(intent2);
                                    break;
                                case HEAD_PAGERS_ITEM_3:
                                    Intent intent3 = new Intent(getActivity(), HeadPageAty.class);
                                    intent3.putExtra("id",headPageId.get(HEAD_PAGERS_ITEM_3));
                                    startActivity(intent3);
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                    break;
                default:
                    break;
            }
            return convertView;
        }

        @Override
        public int getCount() {
            return bean.getBeansSize();
        }

        @Override
        public Object getItem(int position) {
            return bean.getBeans(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

    }

    /**
     * gridview 的适配器
     */
    public class GridViewAdapter extends BaseAdapter{

        //我的数据在data包下的IndexHomeGridViewDate中定义好了
        private LayoutInflater inflater;
        private int page;

        public GridViewAdapter(Context context, int page) {
            super();
            this.inflater = LayoutInflater.from(context);
            this.page=page;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if(page!=-1){
                return 8;
            }else{
                return IndexHomeGridViewDate.navSort.length;
            }
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {

            ViewHolder vh = null;
            if(convertView==null){
                vh=new ViewHolder();
                convertView=inflater.inflate(R.layout.index_home_grid_item, null);
                vh.iv_navsort = (ImageView) convertView.findViewById(R.id.index_home_iv_navsort);
                vh.tv_navsort = (TextView) convertView.findViewById(R.id.index_home_tv_navsort);
                vh.linearLayout = (LinearLayout) convertView.findViewById(R.id.index_home_ll);
                convertView.setTag(vh);
            }
            else{
                vh=(ViewHolder) convertView.getTag();
            }
            vh.iv_navsort.setImageResource(IndexHomeGridViewDate.navSortImages[position+8*page]);
            vh.tv_navsort.setText(IndexHomeGridViewDate.navSort[position+8*page]);

            //item点击事件
            setOnclick(vh.linearLayout, position, page);
            return convertView;
        }
    }

    /**
     * gridview 适配器的holder类
     */
    class ViewHolder{
        ImageView iv_navsort;
        TextView tv_navsort;
        LinearLayout linearLayout;
    }

    /**
     *  初始化viewpager
     */
    private void initViewPager(){
        List<View> list=new ArrayList<View>();  //以下实现动态添加2组gridview
        list.add(gridView1);
        list.add(gridView2);
        viewPager.setAdapter(new MyViewPagerAdapter(list));
        //设置下标点的数量
        //viewPager .setOffscreenPageLimit(2);   //meiyong
        rb1.setChecked(true);//设置默认  下面的点选中的是第一个
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {  //实现划到那个页面，那个页面下面的点会被选中
                // TODO Auto-generated method stub
                if(position==0){
                    rb1.setChecked(true);
                }else if(position==1){
                    rb2.setChecked(true);
                }
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }
            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
            }
        });

        //解决viewPager和SwipeRefreshLayout的滑动冲突
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                    swipeRefreshLayout.setEnabled(false);
                    break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                    swipeRefreshLayout.setEnabled(true);
                    break;
                    }
                return false;
            }
        });
    }

    //自定义viewpager的适配器
    private class MyViewPagerAdapter extends PagerAdapter {

        List<View> list;
        public MyViewPagerAdapter(List<View> list) {
            // TODO Auto-generated constructor stub

            this.list=list;
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        //  判断  当前的view 是否是  Object 对象
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0==arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            container.addView(list.get(position));

            return list.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub

            container.removeView(list.get(position));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // TODO Auto-generated method stub
            //return titles.get(position);
            return "1";  //暂时没用的
        }
    }

    private void setOnclick(LinearLayout ll, int position, int page){
        if(page == 0){
            switch (position){
                case 0:
                    ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), PostActivity.class);
                            intent.putExtra("subject", "语文");
                            startActivity(intent);
                        }
                    });
                    break;
                case 1:
                    ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), PostActivity.class);
                            intent.putExtra("subject", "数学");
                            startActivity(intent);
                        }
                    });
                    break;
                case 2:
                    ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), PostActivity.class);
                            intent.putExtra("subject", "英语");
                            startActivity(intent);
                        }
                    });
                    break;
                case 3:
                    ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), PostActivity.class);
                            intent.putExtra("subject", "物理");
                            startActivity(intent);
                        }
                    });
                    break;
                case 4:
                    ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), PostActivity.class);
                            intent.putExtra("subject", "化学");
                            startActivity(intent);
                        }
                    });
                    break;
                case 5:
                    ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), PostActivity.class);
                            intent.putExtra("subject", "生物");
                            startActivity(intent);
                        }
                    });
                    break;
                case 6:
                    ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), PostActivity.class);
                            intent.putExtra("subject", "历史");
                            startActivity(intent);
                        }
                    });
                    break;
                case 7:
                    ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), PostActivity.class);
                            intent.putExtra("subject", "政治");
                            startActivity(intent);
                        }
                    });
                    break;
                default:
                    break;
            }
        }else if(page == 1){
            switch (position){
                case 0:
                    ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            ToastUtils.showToast(getContext(), "历史", Toast.LENGTH_SHORT);
                            ToastUtils.showToast(getContext(), "正在完善，敬请期待",Toast.LENGTH_SHORT);
                        }
                    });
                    break;
                case 1:
                    ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            ToastUtils.showToast(getContext(), "地理", Toast.LENGTH_SHORT);
                            ToastUtils.showToast(getContext(), "正在完善，敬请期待",Toast.LENGTH_SHORT);
                        }
                    });
                    break;
                case 2:
                    ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            ToastUtils.showToast(getContext(), "理综", Toast.LENGTH_SHORT);
                            ToastUtils.showToast(getContext(), "正在完善，敬请期待",Toast.LENGTH_SHORT);
                        }
                    });
                    break;
                case 3:
                    ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            ToastUtils.showToast(getContext(), "文综", Toast.LENGTH_SHORT);
                            ToastUtils.showToast(getContext(), "正在完善，敬请期待",Toast.LENGTH_SHORT);
                        }
                    });
                    break;
                case 4:
                    ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            ToastUtils.showToast(getContext(), "绘画", Toast.LENGTH_SHORT);
                            ToastUtils.showToast(getContext(), "正在完善，敬请期待",Toast.LENGTH_SHORT);
                        }
                    });
                    break;
                case 5:
                    ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            ToastUtils.showToast(getContext(), "器乐", Toast.LENGTH_SHORT);
                            ToastUtils.showToast(getContext(), "正在完善，敬请期待",Toast.LENGTH_SHORT);
                        }
                    });
                    break;
                case 6:
                    ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            ToastUtils.showToast(getContext(), "舞蹈", Toast.LENGTH_SHORT);
                            ToastUtils.showToast(getContext(), "正在完善，敬请期待",Toast.LENGTH_SHORT);
                        }
                    });
                    break;
                case 7:
                    ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            ToastUtils.showToast(getContext(), "更多", Toast.LENGTH_SHORT);
                            ToastUtils.showToast(getContext(), "正在完善，敬请期待",Toast.LENGTH_SHORT);
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    }

    //初始化teach列表
    public void initTeachList(){
        adapter = new PostAdapter();

        teach_list.setLayoutManager(new LinearLayoutManager(getContext()));
        teach_list.addItemDecoration(new SpaceItemDecoration(5));
        adapter.setOnItemClickListener(this);

        all_tv_1.setOnClickListener(this);
        all_tv_2.setOnClickListener(this);
        near_tv_1.setOnClickListener(this);
        near_tv_2.setOnClickListener(this);
        price_tv_1.setOnClickListener(this);
        price_tv_2.setOnClickListener(this);
        myScrollview.setOnScrollListener(this);

        BmobQuery("-createdAt");

    }

    //recycleview item点击
    @Override
    public void onItemClick(View view, int postion) {

        String postId = adapter.getList().get(postion).getObjectId();
        Intent intent = new Intent(getActivity(), PostInfoActivity.class);
        intent.putExtra("postId", postId);
        startActivity(intent);
    }

    private void BmobQuery(String strOrder){
        //bmob查询
        BmobQuery<Post> query = new BmobQuery<Post>();
        query.order(strOrder);  //时间倒序查询
        query.addWhereEqualTo("isValue", true);
        query.addWhereEqualTo("isDelete", false);
        query.findObjects(getContext(), new FindListener<Post>() {

            @Override
            public void onSuccess(List<Post> list) {
                if (list.size() != 0){
                    adapter.addALL(list);   //查询结果添加到adapter
                    teach_list.setAdapter(adapter);    //设置数据源
                }
                Message message = new Message();
                message.what = 1;
                myhandler.sendMessage(message);
            }

            @Override
            public void onError(int i, String s) {
                Message message = new Message();
                message.what = 2;
                myhandler.sendMessage(message);
            }
        });

    }

    Handler myhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    swipeRefreshLayout.setRefreshing(false);
                    break;
                case 2:
                    swipeRefreshLayout.setRefreshing(false);
                    ToastUtils.showToast(getContext(), "加载跑丢了", 0);
                    break;
            }
        }
    };

    @Override
    public void onScroll(int scrollY) {
        searchLayoutTop = xuanfu_ll.getTop();//获取searchLayout的顶部位置
        if (scrollY > searchLayoutTop){
            dingbu_ll.setVisibility(View.VISIBLE);
            isV = true;
        }else if (scrollY < searchLayoutTop){
            dingbu_ll.setVisibility(View.GONE);
            isV = false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.post_all_1:
            case R.id.post_all_2:
                BmobQuery("-createdAt");
                break;
            case R.id.post_near_1:
            case R.id.post_near_2:
                ToastUtils.showToast(getContext(), "程序员加班开发ing", 0);
                break;
            case R.id.post_price_1:
            case R.id.post_price_2:
                String string = "price";
                if (isPrice){
                    isPrice = false;
                }else {
                    isPrice = true;
                    string  = "-"+string;
                }
                BmobQuery(string);
                break;
        }
    }
}
