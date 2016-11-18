package pype.mingming.bibiteacher.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.entity.PluralistBmob;
import pype.mingming.bibiteacher.entity.User;
import pype.mingming.bibiteacher.ui.CircleImageView;

/**
 * Created by mk on 2016/10/7.
 */
public class Atyinformation_recycler extends AppCompatActivity {
    private PluralistBmob pluralistBmob;
    private CircleImageView circleImageView;
    private Button btn_forMyTeacher;
    private ImageView recyclerImg1,recyclerImg2,recyclerImg3,img_favorite;
    private boolean favoriteCheck = false;
    private int[] textViewID = {R.id.tvRecyclerUserName,R.id.tvRecyclerSex,
            R.id.tvRecyclerUniversity,R.id.tvRecyclerSubject,R.id.tvRecyclerWeek,
            R.id.tvRecyclerBirthAddress, R.id.tvRecyclerIntroduce,R.id.tvRecyclerClosing,
            R.id.tvRecyclerTime, R.id.tvRecyclerDay,R.id.tvRecyclerBadReport
    };
    private TextView[] textViewsBT = new TextView[textViewID.length];
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_recycler);
        user = BmobUser.getCurrentUser(Atyinformation_recycler.this,User.class);
        img_favorite = (ImageView) findViewById(R.id.img_favorite);
        pluralistBmob = (PluralistBmob) getIntent().getSerializableExtra("PluralistBmob");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        btn_forMyTeacher = (Button) findViewById(R.id.btn_forMyTeacher);
        /*CollapsingToolbarLayout collapsingToolbar =(CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbar.setTitle("Title");*/
        toolbar.setTitle("用户："+pluralistBmob.getpUserName());
        toolbar.setNavigationIcon(R.drawable.back_black_top);
        setSupportActionBar(toolbar);
        init(pluralistBmob);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "确定联系TA?", Snackbar.LENGTH_LONG)
                        .setAction("YES", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(Atyinformation_recycler.this,"开发者正在开发即时聊天功能。。",Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });
        btn_forMyTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = BmobUser.getCurrentUser(Atyinformation_recycler.this,User.class);
                if (user==null){
                    Toast.makeText(Atyinformation_recycler.this,"请登录...",Toast.LENGTH_LONG).show();
                }else {
                    if (user.getIsStudent()==null){
                        Toast.makeText(Atyinformation_recycler.this,"请完善资料...",Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Atyinformation_recycler.this,CompleteInformation.class);
                        startActivity(i);
                    }else {
                        if (user.getIsStudent()){
                            Toast.makeText(Atyinformation_recycler.this,"只有家长才能招聘...",Toast.LENGTH_LONG).show();
                        }else {
                            Intent i = new Intent(Atyinformation_recycler.this,AtyMakrOrder.class);
                            i.putExtra("employeeUser",pluralistBmob);
                            startActivity(i);
                        }
                    }
                }

            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        img_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favoriteCheck){
                    img_favorite.setImageResource(R.drawable.favorite_add);
                    Snackbar.make(v, "已取消收藏", Snackbar.LENGTH_SHORT).setAction("",null).show();
                    favoriteCheck = false;
                }else {
                    img_favorite.setImageResource(R.drawable.favorite_delete);
                    Snackbar.make(v, "已收藏", Snackbar.LENGTH_SHORT).setAction("",null).show();
                    favoriteCheck = true;

                }
            }
        });
    }
    //初始化控件
    public void init(PluralistBmob pluralistBmob){
        for (int i=0;i<textViewID.length;i++){
            textViewsBT[i] = (TextView) findViewById(textViewID[i]);
        }
        circleImageView = (CircleImageView) findViewById(R.id.imgRecyclerHear);
        recyclerImg1 = (ImageView) findViewById(R.id.recyclerImg1);
        recyclerImg2 = (ImageView) findViewById(R.id.recyclerImg2);
        recyclerImg3 = (ImageView) findViewById(R.id.recyclerImg3);
        BmobFile imageFile = pluralistBmob.getUser().getAvatar();
        Log.e("imageFile",imageFile+"");
        if(imageFile != null){
            String uri = pluralistBmob.getUser().getAvatar().getFileUrl(this);
            Picasso.with(this)
                    .load(uri)
                    .into(circleImageView);
        }else {
            circleImageView.setImageResource(R.drawable.pluralist_image);
        }
        if(pluralistBmob.getUser().getImageWall1()!= null){
            String uri = pluralistBmob.getUser().getImageWall1().getFileUrl(this);
            Picasso.with(this)
                    .load(uri)
                    .into(recyclerImg1);
        }
        if(pluralistBmob.getUser().getImageWall2()!= null){
            String uri = pluralistBmob.getUser().getImageWall2().getFileUrl(this);
            Picasso.with(this)
                    .load(uri)
                    .into(recyclerImg2);
        }
        if(pluralistBmob.getUser().getImageWall3()!= null){
            String uri = pluralistBmob.getUser().getImageWall3().getFileUrl(this);
            Picasso.with(this)
                    .load(uri)
                    .into(recyclerImg3);
        }
        textViewsBT[0].setText(pluralistBmob.getpUserName());
        textViewsBT[1].setText(pluralistBmob.getpSex());
        textViewsBT[2].setText(pluralistBmob.getpUniversity());
        textViewsBT[3].setText(pluralistBmob.getpSubject());
        textViewsBT[4].setText(pluralistBmob.getpWeek());
        textViewsBT[5].setText(pluralistBmob.getUser().getAddress());
        textViewsBT[6].setText(pluralistBmob.getpIntroduce());
        textViewsBT[7].setText(pluralistBmob.getpClosing());
        textViewsBT[8].setText(pluralistBmob.getpTime());
        textViewsBT[9].setText(pluralistBmob.getpDay());
        textViewsBT[10].setText(pluralistBmob.getUser().getBadReport()+"");
    }
}

