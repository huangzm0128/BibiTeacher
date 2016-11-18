package pype.mingming.bibiteacher.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.entity.MyBackgroud;
import pype.mingming.bibiteacher.entity.User;
import pype.mingming.bibiteacher.myhomepage.TopBar;
import pype.mingming.bibiteacher.utils.CustomProgressDialog;

/**
 * Created by mk on 2016/9/19.
 */
public class AtyUserBackground extends BaseActivity implements View.OnClickListener {

    public User user ;
    private ImageView imgBackground1,imgBackground2,imgBackground3,imgBackground4;
    private int[] backgroundCheckID = {R.id.backgroundCheck1,
            R.id.backgroundCheck2,
            R.id.backgroundCheck3,
            R.id.backgroundCheck4};
    private ImageView[] backgroundCheck = new ImageView[backgroundCheckID.length];
    private Button btnBackgroundMore,btnBackgroundSetup;
    private int backCheckNum=0;//记录背景图片的选择项
    public String[] backCheck = {"MasterMap","OldPhoto","Rillievo","Negative"};
    private TopBar myTopBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_background);
        user = BmobUser.getCurrentUser(AtyUserBackground.this,User.class);

        imgBackground1 = (ImageView) findViewById(R.id.imgBackground1);
        imgBackground2 = (ImageView) findViewById(R.id.imgBackground2);
        imgBackground3 = (ImageView) findViewById(R.id.imgBackground3);
        imgBackground4 = (ImageView) findViewById(R.id.imgBackground4);
        btnBackgroundMore = (Button) findViewById(R.id.btnBackgroundMore);
        btnBackgroundSetup = (Button) findViewById(R.id.btnBackgroundSetup);
        imgBackground1.setOnClickListener(this);
        imgBackground2.setOnClickListener(this);
        imgBackground3.setOnClickListener(this);
        imgBackground4.setOnClickListener(this);
        btnBackgroundMore.setOnClickListener(this);
        btnBackgroundSetup.setOnClickListener(this);
        myTopBar = (TopBar) findViewById(R.id.myTB_background);
        myTopBar.getLeftImageButtonn().setImageResource(R.drawable.back_white_top);
        myTopBar.getLeftImageButtonn().setVisibility(View.VISIBLE);

        for (int i = 0;i<backgroundCheckID.length;i++){
            backgroundCheck[i] = (ImageView) findViewById(backgroundCheckID[i]);
        }
        initBackgroundCheck();
        getBackground(user);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.background);

        imgBackground1.setImageBitmap(bitmap);
        imgBackground2.setImageBitmap(MyBackgroud.pixelEffectOldPhoto(bitmap));
        imgBackground3.setImageBitmap(MyBackgroud.pixelEffectRillievo(bitmap));
        imgBackground4.setImageBitmap(MyBackgroud.pixelEffectNegative(bitmap));

        myTopBar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftButtonOnClick() {
                onBackPressed();
            }

            @Override
            public void rightButtonOnClick() {
                onBackPressed();
            }
        });


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_background;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgBackground1:
                initBackgroundCheck();
                backCheckNum = 0;
                backgroundCheck[0].setVisibility(View.VISIBLE);
                break;
            case R.id.imgBackground2:
                initBackgroundCheck();
                backCheckNum = 1;
                backgroundCheck[1].setVisibility(View.VISIBLE);
                break;
            case R.id.imgBackground3:
                initBackgroundCheck();
                backCheckNum = 2;
                backgroundCheck[2].setVisibility(View.VISIBLE);
                break;
            case R.id.imgBackground4:
                initBackgroundCheck();
                backCheckNum = 3;
                backgroundCheck[3].setVisibility(View.VISIBLE);
                break;
            case R.id.btnBackgroundMore:
                Toast.makeText(AtyUserBackground.this,"亲！开发君正在加班设计名片。。。",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnBackgroundSetup:
                changeBackground(user,backCheckNum);
                break;
        }


    }
    //服务器获取信息！初始化选择
    public void getBackground(User user){
        if (user.getBackground()==null||user.getBackground().equals(backCheck[0])){
            backgroundCheck[0].setVisibility(View.VISIBLE);
        }else if (user.getBackground().equals(backCheck[1])){
            backgroundCheck[1].setVisibility(View.VISIBLE);
        }else if (user.getBackground().equals(backCheck[2])){
            backgroundCheck[2].setVisibility(View.VISIBLE);
        }else {
            backgroundCheck[3].setVisibility(View.VISIBLE);
        }
    }
    //修改背景数据并上传服务器
    public void changeBackground(User user ,int backCheckNum){

        if (backCheckNum==0){
            user.setBackground(backCheck[0]);
        }else if (backCheckNum==1){
            user.setBackground(backCheck[1]);
        }else if (backCheckNum==2){
            user.setBackground(backCheck[2]);
        }else {
            user.setBackground(backCheck[3]);
        }
        final CustomProgressDialog dialog = new CustomProgressDialog(AtyUserBackground.this,"网络君正在奔跑..", R.drawable.myprogressframe);
        dialog.show();
        user.update(AtyUserBackground.this, new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(AtyUserBackground.this,"名片设置成功",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(AtyUserBackground.this,"名片设置失败"+s,Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }

    //初始化勾选控件
    public void initBackgroundCheck(){
        for(int i=0;i<backgroundCheck.length;i++){
            backgroundCheck[i].setVisibility(View.INVISIBLE);
        }

    }
}
