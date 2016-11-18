package pype.mingming.bibiteacher.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.entity.BmobData;
import pype.mingming.bibiteacher.entity.User;
import pype.mingming.bibiteacher.myhomepage.TopBar;
import pype.mingming.bibiteacher.utils.ActivityUtils;
import pype.mingming.bibiteacher.utils.CustomProgressDialog;
import pype.mingming.bibiteacher.utils.LogUtils;

/**
 * Created by mk on 2016/9/10.
 */
public class AtyImageWall extends Activity implements View.OnClickListener {

    private static final String IMAGEWALLNAME = "imageWall.jpg";//压缩图片名称
    private final String[] items = {"手机相册","现在拍摄","删除"};
    private File phoneFile;
    private int[] imageWallId = {R.id.image_wall01, R.id.image_wall02,R.id.image_wall03};
    private Button btnImageWall;
    private ImageView[] imageWall = new ImageView[3];
    private int IMAGEPHOTO = 10,IMAGECAMERA=0;
    private Bitmap mBitmap=null;//图片墙的资源
    private String[] imageWallPath = new String[3];
    private String phonePath = null;//图片路径
    private User user;
    private TopBar myTopBar;


    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagewall);

        btnImageWall = (Button) findViewById(R.id.btnImageWall);
        myTopBar = (TopBar) findViewById(R.id.myTB_imagewall);
        btnImageWall.setOnClickListener(this);

        for (int i = 0; i < 3; i++) {
            imageWall[i] = (ImageView) findViewById(imageWallId[i]);
            imageWall[i].setOnClickListener(this);
        }
        user = BmobUser.getCurrentUser(AtyImageWall.this,User.class);
        if (user.getImageWall1()!=null){
            String url = user.getImageWall1().getUrl();
            Picasso.with(AtyImageWall.this).load(url).into(imageWall[0]);
        }else {
            imageWall[0].setImageResource(R.drawable.image_add);

        }
        if (user.getImageWall2()!=null){
            String url = user.getImageWall2().getUrl();
            Picasso.with(AtyImageWall.this).load(url).into(imageWall[1]);
        }else {
            imageWall[1].setImageResource(R.drawable.image_add);

        }
        if (user.getImageWall3()!=null){
            String url = user.getImageWall3().getUrl();
            Picasso.with(AtyImageWall.this).load(url).into(imageWall[2]);
        }else {
            imageWall[2].setImageResource(R.drawable.image_add);

        }
        myTopBar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftButtonOnClick() {
                //导航栏的左菜单监听事件
                onBackPressed();
            }

            @Override
            public void rightButtonOnClick() {
                refresh();
            }
        });
    }

    //图像墙为图片未设置时点击时的dialog
    public void dialog(final int imageWallID){
        new AlertDialog.Builder(AtyImageWall.this).setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i==0){
                    Intent photo = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(photo, IMAGEPHOTO+imageWallID);
                }else if(i==1){
                    Intent shoot = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//调用系统的相机启动
                    phoneFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()
                            + "/" + getTime() + ".jpg");//绝对路径
                    shoot.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(phoneFile));
                    startActivityForResult(shoot, IMAGECAMERA+imageWallID);
                }else if (i==2){
                    BmobData bmobData = new BmobData();
                    bmobData.imageWallDelete(user,imageWallID,AtyImageWall.this);
                }
            }
        }).setNeutralButton("取消",null).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.image_wall01:
                dialog(0);
                break;
            case R.id.image_wall02:
                dialog(1);
                break;
            case R.id.image_wall03:
                dialog(2);
                break;
            case R.id.btnImageWall:
                //imageUploadAll(imageWallPath);
                Intent intent = new Intent(AtyImageWall.this,UserActivity.class);
                startActivity(intent);
                break;

        }
    }
    //图片批量压缩
    public String[] imageCompress(String[] path) {
        String[] imageWallPath = new String[path.length];
        for (int i = 0; i < path.length; i++) {
            //在当前程序路径中建一个文件夹
            String imagePath = getApplicationContext().getFilesDir().getAbsolutePath() + "/imageWall/";
            File file = new File(imagePath);
            if (!file.exists()) {
                file.mkdir();
            }
            //定义一个file，为压缩后的图片
            File newFile = new File(imagePath, i + IMAGEWALLNAME);
            Bitmap bitmap = BitmapFactory.decodeFile(path[i]);

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
                FileOutputStream fos = new FileOutputStream(newFile);
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
                imageWallPath[i] = newFile.getPath();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imageWallPath;
    }
    //图片单文件上传
    public void imageUpload(String path,int i){
        final CustomProgressDialog dialog = new CustomProgressDialog(AtyImageWall.this,"网络君真正上传..", R.drawable.myprogressframe);
        dialog.show();
        String imagePath = getApplicationContext().getFilesDir().getAbsolutePath() + "/imageWall/";
        File file = new File(imagePath);
        if (!file.exists()) {
            file.mkdir();
        }
        //定义一个file，为压缩后的图片
        File newFile = new File(imagePath, i + IMAGEWALLNAME);
        Bitmap bitmap = BitmapFactory.decodeFile(path);

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
            FileOutputStream fos = new FileOutputStream(newFile);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final BmobFile imageWall = new BmobFile(newFile);
        if (i==0){
            user.setImageWall1(imageWall);
        }else if (i==1){
            user.setImageWall2(imageWall);
        }else if (i==2){
            user.setImageWall3(imageWall);
        }
        imageWall.uploadblock(this, new UploadFileListener() {
            @Override
            public void onSuccess() {
                user.update(AtyImageWall.this, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(AtyImageWall.this,"上传成功",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(AtyImageWall.this,"数据更新失败",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(AtyImageWall.this,"网络出错，上传失败",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }


    //获取时间
    public String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date();
        String str = format.format(date);
        return str;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){

            if (requestCode>=10){//图库资源获取
                Uri originalUri = data.getData(); // 获得图片的uri
                /*// 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver()接口
                try {
                //这是不获取图片地址直接显示的例子
                    mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), originalUri);
                } catch (IOException e) {
                    Log.e("TAG-->Error", e.toString());
                }*/
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(originalUri,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                //获取图片的索引值
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                mBitmap = BitmapFactory.decodeFile(picturePath);
                phonePath = picturePath;

            }else {//相机资源获取
                /*Bundle Extras = data.getExtras();
                mBitmap = (Bitmap)Extras.get("data");//这是直接提取相册的图片资源！不用获取图片路径*/
                mBitmap = BitmapFactory.decodeFile(phoneFile.getAbsolutePath());
                phonePath = phoneFile.getAbsolutePath();
            }
            //图片显示
            if (requestCode==0||requestCode==10){
                imageWall[0].setImageBitmap(mBitmap);
                imageUpload(phonePath,0);
                imageWallPath[0] = phonePath;

            }else if (requestCode==1||requestCode==11){
                imageWall[1].setImageBitmap(mBitmap);
                imageUpload(phonePath,1);
                imageWallPath[1] = phonePath;

            }else if (requestCode==2||requestCode==12) {
                imageWall[2].setImageBitmap(mBitmap);
                imageUpload(phonePath,2);
                imageWallPath[2] = phonePath;
            }
        }else {
            Toast.makeText(AtyImageWall.this,"取消选择",Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 刷新, 这种刷新方法，只有一个Activity实例。
     */
    public void refresh(){
        onCreate(null);
    }
    /*//图片批量上传
    public void imageUploadAll(final String[] imagePath){
        String image = "";
        for (int i = 0;i<imagePath.length;i++){
            if (imagePath[i]!=null){
                image = image + imagePath[i] +"--";
            }
        }
        String[] newImagePath = image.split("--");

        if (newImagePath[0].equals("")){
            Toast.makeText(this,"木有选择图片!!",Toast.LENGTH_SHORT).show();
        }
        final String[] newImageWallPath = imageCompress(newImagePath);//压缩处理后的图片集路径

        BmobFile.uploadBatch(AtyImageWall.this, newImageWallPath, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> list1) {
                Log.e("error",list.get(0)+"2"+"++++"+list1.get(0));
                if(list1.size()==newImageWallPath.length){
                    if (newImageWallPath.length==1){
                        user.setImageWall1(list.get(0));
                    }else if (newImageWallPath.length==2){
                        user.setImageWall1(list.get(0));
                        user.setImageWall2(list.get(1));
                    }else if (newImageWallPath.length==3){
                        user.setImageWall1(list.get(0));
                        user.setImageWall2(list.get(1));
                        user.setImageWall3(list.get(2));
                    }
                    user.update(AtyImageWall.this, new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(AtyImageWall.this,"数据更新成功",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(AtyImageWall.this,"数据更新失败",Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {

            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(AtyImageWall.this,"上传失败",Toast.LENGTH_SHORT).show();

            }
        });
    }*/

}
