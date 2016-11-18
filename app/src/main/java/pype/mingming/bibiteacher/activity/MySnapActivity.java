package pype.mingming.bibiteacher.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.adapter.ChooseTeachAdpter;
import pype.mingming.bibiteacher.entity.Applicants;
import pype.mingming.bibiteacher.entity.Post;
import pype.mingming.bibiteacher.myhomepage.TopBar;
import pype.mingming.bibiteacher.ui.S_ListView;
import pype.mingming.bibiteacher.utils.TimeUtils;

/**
 * Created by wushaohongly on 2016/10/13.
 */

public class MySnapActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener{

    @BindView(R.id.progress_dialog)
    RelativeLayout progress_dialog;
    @BindView(R.id.error_rl)
    RelativeLayout error_rl;
    @BindView(R.id.error_iv)
    ImageView error_iv;
    @BindView(R.id.error_tv)
    TextView error_tv;
    @BindView(R.id.mysnap_rl)
    RelativeLayout mysnap;



    private AnimationDrawable mAnimation;
    @BindView(R.id.loadingIv)
    ImageView mImageView;

    @BindView(R.id.choose_teach_title)
    TextView title_tv;
    @BindView(R.id.choose_teach_priceNum)
    TextView price_tv;
    @BindView(R.id.choose_teach_time)
    TextView time_tv;
    @BindView(R.id.choose_teach_read)
    TextView readNum_tv;

    @BindView(R.id.choose_teach_icon)
    ImageView icon_iv;
    @BindView(R.id.choose_teach_username)
    TextView username_tv;

    @BindView(R.id.choose_teach_subject)
    TextView subject_tv;
    @BindView(R.id.choose_teach_grade)
    TextView grade;
    @BindView(R.id.choose_teach_stime)
    TextView setime;
    @BindView(R.id.choose_teach_address)
    TextView address_tv;
    @BindView(R.id.choose_teach_address_rl)
    RelativeLayout address_rl;
    @BindView(R.id.choose_teach_context)
    TextView context_tv;
    @BindView(R.id.choose_teach_baoming_text)
    TextView baoming;
    @BindView(R.id.choose_teach_hint)
    TextView baoming_hint;

    @BindView(R.id.choose_teach_list)
    S_ListView listView;

    @BindView(R.id.choose_teach_topBar)
    TopBar topBar;

    private Post post;
    private final int MESSAGE_OK = 1;
    Post p = new Post();
    Date date = null;
    String iconURL = "";
    List<Applicants> Alist;
    private ChooseTeachAdpter adpter;
    String baomingStr = "";

    @Override
    protected int getLayoutId() {
        return R.layout.choose_teach_layout;
    }

    @Override
    protected void initDada() {
        super.initDada();

        initToolbar();
        mysnap.setVisibility(View.GONE);
        initView();
        error_iv.setOnClickListener(this);
        error_tv.setOnClickListener(this);
        error_rl.setVisibility(View.GONE);
        progress_dialog.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        post = (Post) intent.getSerializableExtra("post");

        listView.setOnItemClickListener(this);
        adpter = new ChooseTeachAdpter(this, post);
        Query(post.getObjectId());
        Querybaoming();
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

    /**
     * 设置标题栏
     */
    private void initToolbar() {
        topBar.getLeftImageButtonn().setImageResource(R.drawable.back_white_top);
        topBar.getLeftImageButtonn().setVisibility(View.VISIBLE);
        topBar.getRightImageButtom().setImageResource(R.mipmap.delete);
        topBar.getRightImageButtom().setVisibility(View.VISIBLE);
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
            public void rightButtonOnClick() {
                Log.i("info", "删除");
                new AlertDialog.Builder(MySnapActivity.this).setTitle("系统提示")
                        .setMessage("删除将是不可恢复操作？").setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        post.setDelete(true);
                        post.update(context, post.getObjectId(), new UpdateListener() {
                            @Override
                            public void onSuccess() {
                                finish();
                            }

                            @Override
                            public void onFailure(int i, String s) {

                            }
                        });

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        });
    }

    public void Query(final String postId) {
        if (postId != null) {
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
                }

                @Override
                public void onFailure(int i, String s) {

                }
            });
        }
    }

    public void Querybaoming(){
        BmobQuery<Applicants> bmobQuery = new BmobQuery<Applicants>();
        bmobQuery.addWhereEqualTo("post", post.getObjectId());
        bmobQuery.include("user");
        bmobQuery.findObjects(this, new FindListener<Applicants>() {
            @Override
            public void onSuccess(List<Applicants> list) {
                Alist = list;
                adpter.addList(list);
                listView.setAdapter(adpter);
                baomingStr = list.size() +"人已报名";

                if (list.size() != 0){
                    Message message = new Message();
                    message.what = 4;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MESSAGE_OK:

                    title_tv.setText(p.getPostTitle());
                    price_tv.setText(p.getPrice().toString());

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
                    readNum_tv.setText(p.getReadNum().toString() +" 浏览");
                    username_tv.setText(p.getPostUser().getUsername());

                    if(p.getPostUser().getAvatar() != null){
                        iconURL = p.getPostUser().getAvatar().getFileUrl(context);
                        Picasso.with(context)
                                .load(iconURL)
                                .centerCrop()
                                .resize(150, 150)
                                .into(icon_iv);
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
                    mysnap.setVisibility(View.VISIBLE);
                    break;

                case 4:
                    baoming.setText(baomingStr);
                    baoming_hint.setVisibility(View.GONE);
                    break;

            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("info", position+"");
    }

    @Override
    public void onClick(View v) {

    }
}
