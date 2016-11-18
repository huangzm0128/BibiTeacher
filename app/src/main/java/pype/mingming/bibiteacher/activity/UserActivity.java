package pype.mingming.bibiteacher.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.entity.BmobData;
import pype.mingming.bibiteacher.entity.User;
import pype.mingming.bibiteacher.myhomepage.SlidingMenu;
import pype.mingming.bibiteacher.myhomepage.TopBar;
import pype.mingming.bibiteacher.ui.CircleImageView;
import pype.mingming.bibiteacher.utils.CustomProgressDialog;
import pype.mingming.bibiteacher.utils.LogUtils;
import pype.mingming.bibiteacher.utils.ToastUtils;

/**
 * 用户资料页面
 * Created by mk on 2016/7/30.
 */
public class UserActivity extends BaseActivity implements View.OnClickListener {

    private final String[] items = new String[]{"从相册获取","拍照上传"};
    private static final String IMAGE_NAME = "userAvatar.jpg";
    private static final int START_FP_CODE = 10;
    private static final int START_FC_CODE = 11;
    private static final int RESULT_CODE = 12;
    private static final int BACKGROUND = 20; //跳转设置名片页时的请求吗

    @BindView(R.id.Sliding_linear)
    LinearLayout Sliding_linear;
    @BindView(R.id.myTopBar)
    TopBar myTopBar;//导航栏
    int[] relativeLayouts = {R.id.rt_userNum,R.id.rt_write,R.id.rt_num,R.id.rt_address};
    @BindView(R.id.btnLogin)
    Button btnLogin;//注销按钮
    @BindView(R.id.bottom_imgUpload)
    ImageView bottom_imgUpload;//照片墙的图片上传
    @BindView(R.id.imgUser)
    CircleImageView userImg; //用户头像
    @BindView(R.id.btnUser)
    Button uploadAvatar; // 上传头像按钮
    @BindView(R.id.btnBackground)
    Button btnBackground;//个性名片按钮
    @BindView(R.id.linear_imagewall)
    LinearLayout linearWall;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvWrite)
    TextView tvWrite;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvUserSex)
    TextView tvUserSex;
    @BindView(R.id.Sliding_layout)
    SlidingMenu slidingMenu;//名片设置
    private int[] imageId = {R.id.bottom_img1,R.id.bottom_img2,R.id.bottom_img3};
    private ImageView[] bottomImage = new ImageView[3];

    private User user;
    private BmobData bmobData = new BmobData();

    @Override
    protected int getLayoutId() {
        return R.layout.userhomepage;
    }
    @Override
    protected void initDada() {
        super.initDada();
        myTopBar.getTvTitle().setText("个人资料");
        myTopBar.getLeftImageButtonn().setImageResource(R.drawable.back_white_top);
        myTopBar.getLeftImageButtonn().setVisibility(View.VISIBLE);
        for (int i=0;i<imageId.length;i++){
            bottomImage[i] = (ImageView) findViewById(imageId[i]);
        }
        user = BmobUser.getCurrentUser(this,User.class);
        initView(user);

    }
    public void initView(User user){
        updateData(user);
        BmobFile imageFile = user.getAvatar();
        if(imageFile != null){
            String avatorUri = user.getAvatar().getFileUrl(this);
            Picasso.with(this)
                    .load(avatorUri)
                    .into(userImg);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateData(user);
    }


    private void updateData(User user){
        user = BmobUser.getCurrentUser(this,User.class);
        if(user != null){
            bmobData.getBackground(user,UserActivity.this,slidingMenu);
            //控件初始化
            tvUserName.setText(user.getUsername());
            if (user.getAddress()!=null){
                tvAddress.setText(user.getAddress());
            }else {
                tvAddress.setText("来自外星。。。。");
            }
            tvWrite.setText(user.getSignature());
            tvPhone.setText(user.getMobilePhoneNumber());
            int userYear = 0;
            if (user.getBirthDay()!=null){
                String[] data = user.getBirthDay().split("-");
                Calendar c = Calendar.getInstance();//获取当前日期
                int nowYear = c.get(Calendar.YEAR);
                userYear = nowYear - Integer.parseInt(data[0]);
            }
            String sex = null;
            if (user.getSex()==null||user.getSex()){
                sex = "男";
            }else {
                sex = "女";
            }
            tvUserSex.setText("年龄："+userYear+"   "+sex);
            if (user.getImageWall1()!=null){
                Picasso.with(this)
                        .load(user.getImageWall1().getUrl())
                        .into(bottomImage[0]);
            }else {
                bottomImage[0].setImageResource(R.drawable.girl1);
            }if (user.getImageWall2()!=null){
                Picasso.with(this)
                        .load(user.getImageWall2().getUrl())
                        .into(bottomImage[1]);
            }else {
                bottomImage[1].setImageResource(R.drawable.girl2);
            }
            if (user.getImageWall3()!=null){

                Picasso.with(this)
                        .load(user.getImageWall3().getUrl())
                        .into(bottomImage[2]);
            }else {
                bottomImage[2].setImageResource(R.drawable.beij2);
            }
        }else {
            Toast.makeText(UserActivity.this,"联网失败。。。",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        //菜单的监听事件
        for (int i=0;i<relativeLayouts.length;i++){
            findViewById(relativeLayouts[i]).setOnClickListener(this);
        }
        btnLogin.setOnClickListener(this);
        bottom_imgUpload.setOnClickListener(this);
        uploadAvatar.setOnClickListener(this);
        userImg.setOnClickListener(this);
        linearWall.setOnClickListener(this);
        btnBackground.setOnClickListener(this);
        myTopBar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftButtonOnClick() {
                //导航栏的左菜单监听事件
                onBackPressed();
            }

            @Override
            public void rightButtonOnClick() {
                //导航栏的右菜单监听事件
                Intent intent = new Intent(UserActivity.this,AtyDatumEdit.class);
                startActivity(intent);
//                finish();
            }
        });
    }

    //进入个人主页是就调用动画效果
    @Override
    protected void onStart() {
        super.onStart();
        contentAnimator();
    }
    /**
     * 动画效果
     */
    public void contentAnimator(){
        //第一次弹动
        ObjectAnimator animatorUp = ObjectAnimator.ofFloat(Sliding_linear,"translationY",0,-250);
        ObjectAnimator animatorDown = ObjectAnimator.ofFloat(Sliding_linear,"translationY",-200,0);
        AnimatorSet set = new AnimatorSet();
        set.play(animatorUp);
        set.play(animatorDown).after(animatorUp);
        set.setDuration(1000);
        set.setInterpolator(new OvershootInterpolator());
        set.start();
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.rt_userNum||v.getId()==R.id.rt_write||v.getId()==R.id.rt_address) {
            Intent intent = new Intent(UserActivity.this,AtyDatumEdit.class);
            startActivity(intent);
        }else if (v.getId()==R.id.btnLogin) {
            BmobUser.logOut(context);
            switchActivity(MainActivity.class, true);

        }else if (v.getId()==R.id.linear_imagewall||v.getId()==R.id.bottom_imgUpload) {
            Intent intent = new Intent(UserActivity.this,AtyImageWall.class);
            startActivity(intent);

        }else if (v.getId()==R.id.btnUser) {
            createUploadAvatarDialog();
        }else if (v.getId()==R.id.imgUser) {
            createUploadAvatarDialog();
        }else if (v.getId()==R.id.btnBackground){
            Intent intent = new Intent(UserActivity.this,AtyUserBackground.class);
            startActivityForResult(intent,20);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case START_FP_CODE:
                if(data != null){
                    startCrop(data.getData());
                }
                break;

            case START_FC_CODE:
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                //在这个路径中找file,找不到指定名称的就为空
                File file = new File(path,IMAGE_NAME);
                Uri uri = Uri.fromFile(file);
                startCrop(uri);

            case RESULT_CODE:
                if(data != null){
                    getImagePicture(data);
                }
                break;
            case BACKGROUND:
                //从设置名片页返回时再重新获取当前用户
                user =  BmobUser.getCurrentUser(this,User.class);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 创建用户上传头像对话框
     */
    private void createUploadAvatarDialog(){
        //创建对话框选择从相册上传还是拍照上传
        new AlertDialog.Builder(this)
                .setTitle("上传头像")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                Intent fp = new Intent();
                                //获取内容
                                fp.setAction(Intent.ACTION_GET_CONTENT);
                                //获取所有图片
                                fp.setType("image/*");
                                startActivityForResult(fp,START_FP_CODE);
                                break;
                            case 1:
                                Intent fc = new Intent();
                                fc.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                                String state = Environment.getExternalStorageState();
                                if(state.equals(Environment.MEDIA_MOUNTED)){
                                    //获取并初始化系统定义的共享图片目录
                                    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                                    if(!path.exists()){
                                        path.mkdir();
                                    }

                                    //构造需要存储的文件图片
                                    File file = new File(path, IMAGE_NAME);
                                    fc.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                                    startActivityForResult(fc,START_FC_CODE);
                                }else {
                                    ToastUtils.showToast(UserActivity.this,"未找到储存卡，请检查存储空间", Toast.LENGTH_SHORT);
                                }
                        }
                    }
                }).show();
    }



    /**
     * 剪裁图片的设置
     * @param data 图片的Uri
     */
    private void startCrop(Uri data) {
        //跳转到系统自带图片剪切工具
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data,"image/*");
        //设置剪裁
        intent.putExtra("crop",true);
        //设置比例
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        //剪裁宽高
        intent.putExtra("outputX",300);
        intent.putExtra("outputY",300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent,RESULT_CODE);
    }

    private void getImagePicture(Intent data) {
        if(data != null){
            Bundle bundle = data.getExtras();
            if(bundle != null){
                Bitmap bitmap = bundle.getParcelable("data");
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                saveImageInfo(bitmap, IMAGE_NAME);
                userImg.setImageDrawable(drawable);
            }
        }
    }


    /**
     * 压缩并保存图片,并且上传到Bmob
     * @param bitmap 要压缩保存的位图
     * @param imgName 名称
     */
    private void saveImageInfo( Bitmap bitmap, String imgName){
        //  获取当前程序路径
        String save_path = getApplicationContext().getFilesDir().getAbsolutePath() + "/userAvatar/";
        File file = new File(save_path);
        if(!file.exists()){
            file.mkdir();
        }

        File mFile = new File(save_path, imgName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 60;//压缩率
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 100) {
            baos.reset();
            options -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            if (options==0){
                break;//压缩率达到了百分之百
            }
        }

        try {
            FileOutputStream fos = new FileOutputStream(mFile);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


//        try {
//            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(mFile));
//            //还原图片 compress 压缩
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//            bos.flush();
//            bos.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //上传头像
        final BmobFile userAvatarFile = new BmobFile(mFile);
        user.setAvatar(userAvatarFile);
//        final File userIcon = mFile;
        final CustomProgressDialog dialog = new CustomProgressDialog(UserActivity.this,"网络君正在奔跑..", R.drawable.myprogressframe);
        dialog.show();
        userAvatarFile.upload(this, new UploadFileListener() {
            @Override
            public void onSuccess() {
                user.update(UserActivity.this, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        dialog.dismiss();
                        ToastUtils.showToast(context,"上传头像成功", Toast.LENGTH_SHORT);
                        LogUtils.i("UserActivity","upload userAvatar success");
//                        Picasso.with(context)
//                                .load(userIcon)
//                                .into(userImg);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        dialog.dismiss();
                        LogUtils.i("UserActivity", s);
                        ToastUtils.showToast(context,"s", Toast.LENGTH_SHORT);
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                LogUtils.i("UserActivity", s);
                dialog.dismiss();
                ToastUtils.showToast(context, s, Toast.LENGTH_SHORT);
            }
        });
    }

}