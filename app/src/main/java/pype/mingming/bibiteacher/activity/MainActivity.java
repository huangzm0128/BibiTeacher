package pype.mingming.bibiteacher.activity;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.ValueEventListener;
import pype.mingming.bibiteacher.Constant;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.adapter.MKOrderAdapter;
import pype.mingming.bibiteacher.entity.BmobData;
import pype.mingming.bibiteacher.entity.Orders;
import pype.mingming.bibiteacher.entity.UnOrderBmob;
import pype.mingming.bibiteacher.entity.User;
import pype.mingming.bibiteacher.ui.CircleImageView;
import pype.mingming.bibiteacher.ui.ContextPagerHome;
import pype.mingming.bibiteacher.ui.ContextPagerPerson;
import pype.mingming.bibiteacher.ui.ContextPagerSpace;
import pype.mingming.bibiteacher.ui.bottomTab.TabPageIndicator;
import pype.mingming.bibiteacher.ui.bottomTab.TabPageIndicatorEx;
import pype.mingming.bibiteacher.utils.ActivityUtils;
import pype.mingming.bibiteacher.utils.ToastUtils;

public class MainActivity extends BaseActivity{
    /**
     * 时间间隔
     */
    private final int TIME_INTERVAL = 2000;
    /**
     * 记录点击退出键的时间
     */
    private long mBackPressed;


    BmobRealTimeData rtd2 = new BmobRealTimeData();
    private  CircleImageView headerIV;
    private TextView userName;
    private BmobRealTimeData data = new BmobRealTimeData();
    private User user;
    private NotificationManager notificationManager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.id_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tabpage_act_tpi)
    TabPageIndicator mTabPageIndicator;

    /**
     * 底部导航
     */
    private List<Fragment> mTabs = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;
    //默认导航滑动颜色渐变
    private boolean isGradualChange = true;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
    @Override
    protected void initDada() {
        super.initDada();
        toolbar.setTitle("Bibi家教");
        setSupportActionBar(toolbar);
        user = BmobUser.getCurrentUser(this, User.class);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        //设置侧栏Menu背景
        navView.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_menu));
        setNavContent(navView);
        //TabBottom
        initTabIndicator();
        initViewPager();
        initTable();
//        //header
        //获取nav_header_main.xml的属性
        View headerView = navView.getHeaderView(0);
        headerIV = (CircleImageView) headerView.findViewById(R.id.header_imageView);
        userName = (TextView) headerView.findViewById(R.id.header_user_name);
        if(isLogin()){
            //刷新用户
            updateUserHeader();
        }else {
            userName.setText("立即登录");
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateUserHeader();
    }

    private void setNavContent(NavigationView nav) {
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressWarnings("StatementWithEmptyBody")
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.nav_message) {
                    user = BmobUser.getCurrentUser(context,User.class);
                    if (user==null){
                        Toast.makeText(MainActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
                    }else {
                        if(user.getIsStudent() != null){
                            Intent i = new Intent(MainActivity.this,AtyMkOrderNew.class);
                            i.putExtra("user",user);
                            startActivity(i);
                        }else {
                            new AlertDialog.Builder(MainActivity.this).setTitle("系统提示")
                                    .setMessage("请完善个人信息").setPositiveButton("完善个人信息", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(MainActivity.this, CompleteInformation.class);
                                    startActivity(intent);
                                }
                            }).setNegativeButton("我知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.i("info", "我知道了");
                                }
                            }).show();
                        }
                    }

                }else if (id == R.id.nav_home) {

                }  else if (id == R.id.nav_order_from) {
                    user = BmobUser.getCurrentUser(context,User.class);
                    if (user==null){
                        Toast.makeText(MainActivity.this,"请登录...",Toast.LENGTH_SHORT).show();
                    }else {
                        if (user.getMobilePhoneNumber()==null||user.getIsStudent()==null){
                            new AlertDialog.Builder(MainActivity.this).setTitle("系统提示")
                                    .setMessage("请完善个人信息").setPositiveButton("完善个人信息", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(MainActivity.this, CompleteInformation.class);
                                    startActivity(intent);
                                }
                            }).setNegativeButton("我知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.i("info", "我知道了");
                                }
                            }).show();
                        }else {
                            if(user.getIsStudent()){
                                switchActivity(AtyTutorRegi.class,false);
                            }else {
                                Toast.makeText(MainActivity.this,"学生才能投简历...",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else if (id == R.id.nav_person) {
                    if (isLogin()){
                        switchActivity(UserActivity.class,false);
                    }else {
                        switchActivity(LoginActivity.class,false);
                    }

                } else if (id == R.id.nav_post) {
                    switchActivity(PostActivity.class,false);

                } else if (id == R.id.nav_theme) {
                    ToastUtils.showToast(MainActivity.this, "正在完善，敬请期待",Toast.LENGTH_SHORT);
                } else if (id == R.id.nav_setting) {
                    switchActivity(SettingActivity.class, false);
                }else if (id == R.id.nav_exit){
                    ToastUtils.clearToast();
                    ActivityUtils.finishAll();
                }


                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    //以下是BottomTab

    private void initViewPager() {
        ContextPagerHome home = new ContextPagerHome();
        ContextPagerSpace space = new ContextPagerSpace();
        ContextPagerPerson person = new ContextPagerPerson();
        mTabs.add(home);
        mTabs.add(space);
        mTabs.add(person);
        //缓存viewPager
        mViewPager.setOffscreenPageLimit(mTabs.size());
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return mTabs.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mTabs.get(arg0);
            }
        };
        mViewPager.setAdapter(mAdapter);
        //设置是否渐变
        mTabPageIndicator.setIsGradualChange(isGradualChange);
    }

    private void initTabIndicator() {
        //设置底部导航图标与ViewPager关联
        mTabPageIndicator.setViewPager(mViewPager);
//        //设置指示点的在哪个图标显示
//        mTabPageIndicator.setIndicateDisplay(1, true);
    }

    //以上是bottomTab


    @Override
    protected void setListener() {
        super.setListener();
        //BottomTab
        mTabPageIndicator.setOnTabSelectedListener(new TabPageIndicatorEx.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                //点击图标的同时切换ViewPager
                mViewPager.setCurrentItem(index, false);
            }
        });


        //抽屉菜单中用户头像的监听事件，待完善，只是用来测试界面跳转
        headerIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLogin()){
                    switchActivity(UserActivity.class, false);
                }else {
                    //转到登录页面
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent, 1);
                }

            }
        });

        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLogin()){
                    switchActivity(UserActivity.class, false);
                }else {
                    //转到登录页面
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent, 1);
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (System.currentTimeMillis() - mBackPressed > TIME_INTERVAL) { //2s内点击两次BackPressed即退出程序
                mBackPressed = System.currentTimeMillis();
                ToastUtils.showToast(this, "再按一次退出", Toast.LENGTH_SHORT);
            } else {
                ToastUtils.clearToast();
                ActivityUtils.finishAll();
            }
        }
    }


    public boolean isLogin() {
        user = BmobUser.getCurrentUser(this, User.class);
        if (user != null) {
            return true;
        }
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    boolean isLogin = data.getBooleanExtra("isLogin", false);
                    if(isLogin){
                        updateUserHeader();
                    }

                }
                break;
            default:
                break;
        }
    }

    /**
     * 刷新用户头像和用户名
     */
    public void updateUserHeader() {
        user = BmobUser.getCurrentUser(this, User.class);
        if (user != null) {
            if (user.getAvatar() != null) {
                String imageUrl = user.getAvatar().getFileUrl(this);

                Picasso.with(this)
                        .load(imageUrl)
                        .centerCrop()
                        .resize(96, 96)
                        .into(headerIV);
            }
            if (user.getUsername() != null) {
                userName.setText(user.getUsername());
            }
        }else {
            headerIV.setImageResource(R.mipmap.default_user_avatar);
            userName.setText("立即登录");
        }
    }
    public void initTable(){
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
                    JSONObject data = jsonObject.optJSONObject("data");
                    String employerName = data.optString("employerName");
                    String employeeName = data.optString("employeeName");
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.this);
                    mBuilder.setContentTitle("有人招聘您了")//设置通知栏标题
                            .setContentText("请到消息栏查看。。") //设置通知栏显示内容
                            //  .setNumber(number) //设置通知集合的数量
                            .setTicker("您有一条消息") //通知首次出现在通知栏，带上升动画效果的
                            .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                            .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                            .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                            .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                            .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                            .setDefaults(Notification.DEFAULT_SOUND)
                            //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                            .setSmallIcon(R.mipmap.ic_bibiteacher_night);//设置通知小ICON

                    if (user!=null){
                        if (user.getUsername().equals(employerName)||user.getUsername().equals(employeeName)){
                            final Intent intent = new Intent(MainActivity.this,AtyMkOrderNew.class);
                            intent.putExtra("user",user);
                            mNotificationManager.notify(1, mBuilder.build());
                            Snackbar.make(getCurrentFocus(), "您有条信息更新！是否查看", Snackbar.LENGTH_SHORT).setAction("YES", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(intent);
                                }
                            }).show();
                        }
                    }

                }
            }
        });

        rtd2.start(this, new ValueEventListener() {
            @Override
            public void onConnectCompleted() {
                Log.i("info", "连接成功:"+rtd2.isConnected());
                if(rtd2.isConnected()){
                    rtd2.subTableUpdate("Orders");
                }
            }

            @Override
            public void onDataChange(JSONObject jsonObject) {
                Log.i("info", "("+jsonObject.optString("action")+")"+"数据："+jsonObject);
                if (BmobRealTimeData.ACTION_UPDATETABLE.equals(jsonObject.optString("action"))){
                    JSONObject data = jsonObject.optJSONObject("data");
                    String id = data.optString("employeeId");
                    if (isLogin()){

                        if (id.equals(user.getObjectId())){

                            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            NotificationCompat.Builder nbuilder;

                            Intent resultItent = new Intent(context, OrdersActivity.class);
                            resultItent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
                            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultItent, PendingIntent.FLAG_UPDATE_CURRENT);
                            nbuilder = new NotificationCompat.Builder(context)
                            /*设置大位图*/
                                    .setLargeIcon(bitmap)
                            /*设置小图标-*/
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle("您有一条哔哔信息哟")
                                    .setWhen(System.currentTimeMillis())
                            /*设置发出通知时在status bar进行提醒*/
                                    .setTicker("")
                                    .setAutoCancel(true)
                            /*setOngoing(boolean)设为true,notification将无法通过左右滑动的方式清除
                            * 可用于添加常驻通知，必须调用cancle方法来清除
                            */
                                    .setOngoing(true)
                                    .setContentText("点我查看吧")
                            /*设置通知数量的显示类似于QQ那种，用于同志的合并*/
                                    .setContentIntent(pendingIntent);
                            manager.notify(10,nbuilder.build());

                        }
                    }
                }
            }
        });
    }


}
