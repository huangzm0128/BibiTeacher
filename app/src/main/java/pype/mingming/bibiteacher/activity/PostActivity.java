package pype.mingming.bibiteacher.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.adapter.ItemClickListener;
import pype.mingming.bibiteacher.adapter.PostAdapter;
import pype.mingming.bibiteacher.entity.Applicants;
import pype.mingming.bibiteacher.entity.Post;
import pype.mingming.bibiteacher.entity.User;
import pype.mingming.bibiteacher.myhomepage.TopBar;
import pype.mingming.bibiteacher.ui.SpaceItemDecoration;
import pype.mingming.bibiteacher.utils.ToastUtils;


/**
 * Created by wushaohongly on 2016/7/25.
 * 模拟发帖信息显示
 * ........
 */
public class PostActivity extends BaseActivity implements ItemClickListener, View.OnClickListener {

    final int MESSAGE_OK = 1;
    final int MESSAGE_NOT = 2;
    User user;
    String string = "";

    @BindView(R.id.sfl)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.teach_item_recyclerview)
    RecyclerView rv;
    @BindView(R.id.progress_dialog)
    RelativeLayout progress_dialog;

    @BindView(R.id.error_rl)
    RelativeLayout error_rl;

    @BindView(R.id.error_iv)
    ImageView error_iv;
    @BindView(R.id.error_tv)
    TextView error_tv;

    //发帖悬浮按钮
    @BindView(R.id.floatingAB)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.post_list_topBar)
    TopBar topBar;

    private PostAdapter adapter;

    private AnimationDrawable mAnimation;
    @BindView(R.id.loadingIv)
    ImageView mImageView;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MESSAGE_OK:
                    Log.i("info", "更新");
                    progress_dialog.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                    floatingActionButton.setVisibility(View.VISIBLE);
                    break;

                case MESSAGE_NOT:
                    progress_dialog.setVisibility(View.GONE);
                    error_tv.setText("暂无招募，点击刷新");
                    error_rl.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.post_list_layout;
    }

    @Override
    protected void initDada() {
        super.initDada();

        initToolbar();
        setListener();
        Intent intent = getIntent();
        if (intent.getStringExtra("subject") != null){
            string = intent.getStringExtra("subject");
        }

        floatingActionButton.setOnClickListener(this);
        error_iv.setOnClickListener(this);
        error_tv.setOnClickListener(this);
        error_rl.setVisibility(View.GONE);
        floatingActionButton.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.GONE);
        initView();
        progress_dialog.setVisibility(View.VISIBLE);

        adapter =  new PostAdapter();

        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnItemClickListener(this);
        rv.addItemDecoration(new SpaceItemDecoration(5));

        BmobQuery(string);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {
                BmobQuery(string);
            }
        });

        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (((LinearLayoutManager) rv.getLayoutManager()).findLastVisibleItemPosition() == rv.getLayoutManager().getItemCount() - 1) {
                    Log.i("info", "滑到底部咯");
                } else {

                }
            }
        });
    }

    private void BmobQuery(String sub){
        //bmob查询
        BmobQuery<Post> query = new BmobQuery<Post>();
        query.order("-createdAt");  //时间倒序查询
        if (sub != ""){
            query.addWhereEqualTo("subject", sub);
        }
        query.addWhereEqualTo("isValue", true);
        query.addWhereEqualTo("isDelete", false);
        query.findObjects(context, new FindListener<Post>() {

            @Override
            public void onSuccess(List<Post> list) {
                Log.i("info", "查询成功");
                if (list.size() != 0){
                    if (adapter.getList() != null){
                        if(adapter.getList().size() == list.size()){
                            ToastUtils.showToast(context, "已加载到最新", 0);
                            return;
                        }
                    }
                    adapter.addALL(list);   //查询结果添加到adapter
                    rv.setAdapter(adapter);    //设置数据源
                    Message message = new Message();
                    message.what = MESSAGE_OK;
                    handler.sendMessage(message);
                }else {
                    Message message = new Message();
                    message.what = MESSAGE_NOT;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onError(int i, String s) {
                progress_dialog.setVisibility(View.GONE);
                error_rl.setVisibility(View.VISIBLE);
            }
        });

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemClick(View v, int postion) {

        String postId = adapter.getList().get(postion).getObjectId();
        Intent intent = new Intent(PostActivity.this, PostInfoActivity.class);
        intent.putExtra("postId", postId);
        startActivity(intent);
    }

    /**
     * 设置标题栏
     */
    private void initToolbar() {
        topBar.getLeftImageButtonn().setImageResource(R.drawable.back_white_top);
        topBar.getLeftImageButtonn().setVisibility(View.VISIBLE);
        topBar.getRightImageButtom().setVisibility(View.GONE);
    }

    @Override
    protected void setListener() {
        super.setListener();
        topBar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftButtonOnClick() {
                onBackPressed();
            }

            @Override
            public void rightButtonOnClick() {}
        });
    }

    private void initView() {

        // 通过ImageView对象拿到背景显示的AnimationDrawable
        mAnimation = (AnimationDrawable) mImageView.getBackground();
        // 为了防止在onCreate方法中只显示第一帧的解决方案之一
        mImageView.post(new Runnable() {
            @Override
            public void run() {
                mAnimation.start();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.floatingAB:
                if (isLogin()){
                    if (user.getIsStudent() == null){
                        new AlertDialog.Builder(PostActivity.this).setTitle("系统提示")
                                .setMessage("资料不完善哟").setPositiveButton("去完善", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(PostActivity.this, CompleteInformation.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton("我知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                    }else {
                        if(user.getIsStudent()){
                            new AlertDialog.Builder(PostActivity.this).setTitle("系统提示")
                                    .setMessage("学生身份无法发布招募哟").setPositiveButton("去提交份简历呗", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.i("info", "简历");
                                    Intent intent = new Intent(PostActivity.this, AtyTutorRegi.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }).setNegativeButton("我知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                        } else {
                            Intent intent = new Intent(PostActivity.this, SendPostActivity.class);
                            startActivityForResult(intent, 1);
                        }
                    }
                }else {
                    ToastUtils.showToast(context, "请登录..", 1);
                }
                break;
            case R.id.error_iv:
            case R.id.error_tv:
                error_rl.setVisibility(View.GONE);
                error_tv.setText("服务走丢了，点击刷新");
                progress_dialog.setVisibility(View.VISIBLE);
                BmobQuery(string);
                break;
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
    protected void onRestart() {
        super.onRestart();
    }

    //发帖结束回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 1:
                BmobQuery(string);
                break;
        }
    }
}
