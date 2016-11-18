package pype.mingming.bibiteacher.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.*;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.adapter.PostMassageAdpter;
import pype.mingming.bibiteacher.entity.Applicants;
import pype.mingming.bibiteacher.entity.Post;
import pype.mingming.bibiteacher.entity.PostMessage;
import pype.mingming.bibiteacher.entity.User;
import pype.mingming.bibiteacher.myhomepage.TopBar;
import pype.mingming.bibiteacher.ui.S_ListView;
import pype.mingming.bibiteacher.utils.TimeUtils;
import pype.mingming.bibiteacher.utils.ToastUtils;

/**
 * Created by wushaohongly on 2016/9/22.
 */

public class PostInfoActivity extends BaseActivity implements View.OnClickListener{

    final int MESSAGE_OK = 1;
    final int POSTMASSAGE_OK = 2;
    final int Baoming_OK = 3;

    Post p;
    Date date = null;
    String postId;
    User user;
    String iconURL = "";
    String baomingStr = "";
    String baoming_number = "0人已报名";

    boolean isBaoming = false;  //标示当前用户是否报名了该招募

    @BindView(R.id.post_info_topBar)
    TopBar topBar;

    @BindView(R.id.info_view)
    RelativeLayout rl;
    @BindView(R.id.progress_dialog)
    RelativeLayout progress_dialog;
    @BindView(R.id.error_rl)
    RelativeLayout error_rl;
    @BindView(R.id.error_iv)
    ImageView error_iv;
    @BindView(R.id.error_tv)
    TextView error_tv;

    private AnimationDrawable mAnimation;
    @BindView(R.id.loadingIv)
    ImageView mImageView;

    @BindView(R.id.post_info_title)
    TextView title_tv;
    @BindView(R.id.post_info_priceNum)
    TextView priceNum_tv;
    @BindView(R.id.post_info_time)
    TextView time_tv;
    @BindView(R.id.post_info_read)
    TextView read_tv;

    @BindView(R.id.post_info_icon)
    ImageView icon;
    @BindView(R.id.post_info_username)
    TextView username_tv;
    @BindView(R.id.post_info_subject)
    TextView subject_tv;
    @BindView(R.id.post_info_grade)
    TextView grade;

    @BindView(R.id.post_info_stime)
    TextView setime;

    @BindView(R.id.post_info_address)
    TextView address_tv;
    @BindView(R.id.post_info_address_rl)
    RelativeLayout address_rl;
    @BindView(R.id.post_info_context)
    TextView context_tv;

    @BindView(R.id.baoming_table_rl)
    RelativeLayout baoming_rl;
    @BindView(R.id.baoming_text)
    TextView baoming_text;

    @BindView(R.id.post_info_hint)
    TextView hint_tv;

    @BindView(R.id.post_info_liuyan_list)
    S_ListView liuyan_lv;

    @BindView(R.id.baoming_bt)
    LinearLayout baoming_bt;
    @BindView(R.id.liuyan_bt)
    LinearLayout liuyan_bt;
    @BindView(R.id.fenxiang_bt)
    LinearLayout fenxiang_bt;

    @BindView(R.id.post_info_mean_ll)
    LinearLayout mean_ll;
    @BindView(R.id.post_info_liuyan_ll)
    LinearLayout liuyan_ll;

    @BindView(R.id.post_info_liuyan_send)
    Button liuyan_send;
    @BindView(R.id.post_info_liuyan_quxiao)
    Button liuyan_quxiao;
    @BindView(R.id.post_info_liuyan_et)
    EditText liuyan_et;

    @BindView(R.id.baoming_table_tv)
    TextView baoming_list;

    private PostMassageAdpter postMassageAdpter;

    @Override
    protected int getLayoutId() {
        return R.layout.post_info_layout;
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case MESSAGE_OK:
                    title_tv.setText(p.getPostTitle());
                    priceNum_tv.setText(p.getPrice().toString());

                    SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
                    String strTime = p.getCreatedAt();
                    try {
                        date =  formatter.parse(strTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    long time = new Date(System.currentTimeMillis()).getTime();
                    if(date != null){
                        time = date.getTime();
                    }

                    time_tv.setText(TimeUtils.dataLongToSNS(time) +" 发布");
                    read_tv.setText(p.getReadNum().toString() +" 浏览");
                    username_tv.setText(p.getPostUser().getUsername());

                    if(p.getPostUser().getAvatar() != null){
                        iconURL = p.getPostUser().getAvatar().getFileUrl(context);
                        Picasso.with(context)
                                .load(iconURL)
                                .centerCrop()
                                .resize(150, 150)
                                .into(icon);
                    }

                    subject_tv.setText(p.getSubject());
                    grade.setText(p.getGrade());

                    //格式化时间
                    time = new Date(System.currentTimeMillis()).getTime();
                    String stime = p.getStartTime().getDate();
                    String etime = p.getEndTime().getDate();

                    try {
                        date =  formatter.parse(stime);
                        if(date != null){
                            time = date.getTime();
                        }
                        stime = TimeUtils.dateLongToString(time, "MM/dd HH:mm");

                        date =  formatter.parse(etime);
                        if(date != null){
                            time = date.getTime();
                        }
                        etime = TimeUtils.dateLongToString(time, "MM/dd HH:mm");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    setime.setText(stime +" 至 "+ etime);
                    address_tv.setText(p.getTeachAddress());
                    context_tv.setText(p.getPostContent());

                    progress_dialog.setVisibility(View.GONE);
                    rl.setVisibility(View.VISIBLE);
                    break;

                case POSTMASSAGE_OK:
                    hint_tv.setVisibility(View.GONE);
                    break;

                case Baoming_OK:
                    baomingQuery();
                    break;
                case 4:
                    baoming_list.setText(baomingStr);
                    baoming_text.setText(baoming_number);
                    break;

                case 5:
                    hideSoftKeyBoard(liuyan_et);
                    break;
            }
        }
    };

    @Override
    protected void initDada() {
        super.initDada();

        rl.setVisibility(View.GONE);
        initToolbar();
        initView();
        error_iv.setOnClickListener(this);
        error_tv.setOnClickListener(this);
        error_rl.setVisibility(View.GONE);
        progress_dialog.setVisibility(View.VISIBLE);

        postMassageAdpter = new PostMassageAdpter(this);

        icon.setOnClickListener(this);
        username_tv.setOnClickListener(this);
        address_rl.setOnClickListener(this);

        baoming_bt.setOnClickListener(this);
        fenxiang_bt.setOnClickListener(this);
        liuyan_bt.setOnClickListener(this);

        liuyan_send.setOnClickListener(this);
        liuyan_quxiao.setOnClickListener(this);

        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        baoming_list.setText("");

        QueryIsBM();
        Query(postId);
        baomingQuery();
    }

    public void Query(final String postId){
        if(postId != null){

            //查询帖子详细
            BmobQuery<Post> bmobQuery = new BmobQuery<Post>();
            bmobQuery.include("postUser");
            bmobQuery.getObject(this, postId, new GetListener<Post>() {
                @Override
                public void onSuccess(final Post post) {
                    p = post;
                    Message message = new Message();
                    message.what = MESSAGE_OK;
                    handler.sendMessage(message);

                    //更新浏览量
                    Post p = new Post();
                    p.setReadNum(post.getReadNum() + 1);
                    p.update(context, postId, new UpdateListener() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFailure(int i, String s) {

                        }
                    });
                }

                @Override
                public void onFailure(int i, String s) {
                    progress_dialog.setVisibility(View.GONE);
                    error_rl.setVisibility(View.VISIBLE);
                }
            });

            liuyanQuery();
        }
    }

    /**
     * 设置标题栏
     */
    private void initToolbar() {
        topBar.getLeftImageButtonn().setImageResource(R.drawable.back_white_top);
        topBar.getLeftImageButtonn().setVisibility(View.VISIBLE);
        topBar.getRightButton().setVisibility(View.GONE);
    }

    private void setView(Post p){
        title_tv.setText(p.getPostTitle());
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
            case R.id.error_iv:
            case R.id.error_tv:
                error_rl.setVisibility(View.GONE);
                progress_dialog.setVisibility(View.VISIBLE);
                Query(postId);
                break;
            case R.id.post_info_icon:
            case R.id.post_info_username:

                Intent intent = new Intent(PostInfoActivity.this, VisitorActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", p.getPostUser());
                intent.putExtras(bundle);
                startActivity(intent);

                break;
            case R.id.post_info_address_rl:
                ToastUtils.showToast(context, "地图", 1);
                break;
            case R.id.baoming_bt:
                if (isLogin()){
                    if (!isBaoming){
                        String phone = "";
                        String realName = "";

                        phone = user.getMobilePhoneNumber();
                        realName = user.getRealName();

                        Log.i("info", ""+phone+realName);

                        if(user.getIsStudent()){
                            if (user.getIsStudent().equals(null)){
                                new AlertDialog.Builder(PostInfoActivity.this).setTitle("系统提示")
                                        .setMessage("请完善个人信息").setPositiveButton("完善个人信息", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(PostInfoActivity.this, CompleteInformation.class);
                                        startActivity(intent);
                                    }
                                }).setNegativeButton("我知道了", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.i("info", "我知道了");
                                    }
                                }).show();
                            } else {
                                new AlertDialog.Builder(PostInfoActivity.this).setTitle("系统提示")
                                        .setMessage("确定报名？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Applicants applicants = new Applicants();
                                        applicants.setPost(p);
                                        applicants.setUser(user);
                                        applicants.save(context, new SaveListener() {
                                            @Override
                                            public void onSuccess() {
                                                Message message = new Message();
                                                message.what = Baoming_OK;
                                                handler.sendMessage(message);
                                                isBaoming = true;
                                                ToastUtils.showToast(context, "报名成功", 1);
                                            }

                                            @Override
                                            public void onFailure(int i, String s) {
                                                ToastUtils.showToast(context, "报名失败", 1);
                                            }
                                        });

                                    }
                                }).setNegativeButton("考虑一下", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.i("info", "考虑一下");
                                    }
                                }).show();
                            }
                        }else {
                            new AlertDialog.Builder(PostInfoActivity.this).setTitle("系统提示")
                                    .setMessage("家长不能报名哟").setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                        }
                    }else {
                        ToastUtils.showToast(context, "请勿从复报名", 0);
                    }
                } else {
                    ToastUtils.showToast(context, "请先登录..", 0);
                }
                break;
            case R.id.liuyan_bt:
                if (isLogin()){
                    mean_ll.setVisibility(View.GONE);
                    liuyan_et.setText("");
                    liuyan_ll.setVisibility(View.VISIBLE);
                }else {
                    ToastUtils.showToast(context, "请先登录..", 0);
                }
                break;
            case R.id.fenxiang_bt:
                ToastUtils.showToast(context, "分享", 1);
                break;
            case R.id.post_info_liuyan_send:
                String str = liuyan_et.getText().toString();
                if (!str.equals("") && str != null){
                    PostMessage postMessage = new PostMessage();
                    postMessage.setPost(p);
                    postMessage.setContext(str);
                    postMessage.setUser(user);
                    postMessage.save(this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            Message message = new Message();
                            message.what = 5;
                            handler.sendMessage(message);
                            liuyan_et.setText("");
                            liuyanQuery();
                        }

                        @Override
                        public void onFailure(int i, String s) {

                        }
                    });
                }
                break;
            case R.id.post_info_liuyan_quxiao:
                liuyan_ll.setVisibility(View.GONE);
                mean_ll.setVisibility(View.VISIBLE);
                break;
        }
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
            public void rightButtonOnClick() { }
        });
    }

    public boolean isLogin() {
        user = BmobUser.getCurrentUser(this, User.class);
        if (user != null) {
            return true;
        }
        return false;
    }

    public void liuyanQuery(){
        //查询帖子留言/评论
        BmobQuery<PostMessage> massageQuery = new BmobQuery<PostMessage>();
        massageQuery.include("user");
        massageQuery.order("-createdAt");
        massageQuery.addWhereEqualTo("post", postId);

        massageQuery.findObjects(this, new FindListener<PostMessage>() {
            @Override
            public void onSuccess(List<PostMessage> list) {
                if (list.size() != 0){
                    Message message = new Message();
                    message.what = POSTMASSAGE_OK;
                    handler.sendMessage(message);
                }
                postMassageAdpter.addList(list);
                liuyan_lv.setAdapter(postMassageAdpter);
            }

            @Override
            public void onError(int i, String s) {
                Log.i("info", "查询留言失败");
            }
        });
    }

    //查询并更新报名列表
    public void baomingQuery(){
        BmobQuery<Applicants> bmobQuery = new BmobQuery<Applicants>();
        bmobQuery.addWhereEqualTo("post", postId);
        bmobQuery.include("user");
        bmobQuery.findObjects(context, new FindListener<Applicants>() {
            @Override
            public void onSuccess(List list) {
                baomingStr="";
                for(Object a : list){
                    Applicants applicants = (Applicants) a;
                    if (a == list.get(list.size()-1)){
                        baomingStr = baomingStr + ((Applicants) a).getUser().getUsername();
                    } else {
                        baomingStr = baomingStr + ((Applicants) a).getUser().getUsername() +"、";
                    }
                }
                baoming_number = list.size() + "人已报名";
                Message message = new Message();
                message.what = 4;
                handler.sendMessage(message);
                Log.i("info", baomingStr);
            }

            @Override
            public void onError(int i, String s) {
                Log.i("info", "更新出错");
            }
        });
    }

    //初始化查询是否报名
    public void QueryIsBM(){

        if (isLogin()){
            BmobQuery<Applicants> bmobQuery = new BmobQuery<Applicants>();
            bmobQuery.addWhereEqualTo("user", user.getObjectId());
            bmobQuery.addWhereEqualTo("post", postId);

            bmobQuery.findObjects(this, new FindListener<Applicants>() {
                @Override
                public void onSuccess(List<Applicants> list) {
                    if (list.size() != 0){
                        isBaoming = true;
                    }
                }

                @Override
                public void onError(int i, String s) {

                }
            });
        }

    }

    /**
     * 隐藏软键盘
     *
     * @param e 在该控件上输出的软键盘将隐藏
     */
    private void hideSoftKeyBoard(EditText e) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(e.getWindowToken(), 0);
    }
}
