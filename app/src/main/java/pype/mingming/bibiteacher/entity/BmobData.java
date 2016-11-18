package pype.mingming.bibiteacher.entity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.myhomepage.SlidingMenu;
import pype.mingming.bibiteacher.utils.CustomProgressDialog;
import pype.mingming.bibiteacher.utils.Map;
import pype.mingming.bibiteacher.utils.ToastUtils;

/**
 * Created by mk on 2016/9/12.
 */
public class BmobData {
    private boolean result = false;
    private double longitude,latitude;
    private getInterfacechlick interfacechlick;
    private List<PluralistBmob> newList = new ArrayList<PluralistBmob>();
    private List<UnOrderBmob> odBmobList = new ArrayList<UnOrderBmob>();
    private orderInterface mOrderInterface;
    private boolean b = false;

    //资料编辑
    public boolean updateData(UserDate userDate, BmobUser bmobUser, final Context context){
        final CustomProgressDialog dialog = new CustomProgressDialog(context,"网络君正在奔跑..", R.drawable.myprogressframe);
        dialog.show();
        final User u = new User();
        u.setAddress(userDate.getAddress());
        u.setSignature(userDate.getPersonality());
        u.setSex(userDate.getSex());
        u.setBirthDay(userDate.getData());
        u.setRealName(userDate.getRealName());
        u.setMobilePhoneNumber(userDate.getPhone());
        u.update(context, bmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                ToastUtils.showToast(context, "更改成功", Toast.LENGTH_SHORT);
                dialog.dismiss();
                b = true;
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtils.showToast(context, "联网失败", Toast.LENGTH_SHORT);
                dialog.dismiss();
                b = false;
            }
        });
        return b;
    }
    //图片墙的删除操作
    public void imageWallDelete(User user, int imageId, final Context context){
        final CustomProgressDialog dialog = new CustomProgressDialog(context,"网络君正在奔跑..", R.drawable.frame);
        dialog.show();
        if (imageId==0){
            user.remove("imageWall1");
        }else if (imageId==1){
            user.remove("imageWall2");
        }else {
            user.remove("imageWall3");
        }
        user.update(context, new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(context,"联网失败",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }
    //修改密码
    public boolean changePassw(final Context context, String old, String newPS){
        final CustomProgressDialog dialog = new CustomProgressDialog(context,"网络君正在奔跑..", R.drawable.myprogressframe);
        dialog.show();
        User user = BmobUser.getCurrentUser(context,User.class);
        user.updateCurrentUserPassword(context, old, newPS, new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(context,"修改成功",Toast.LENGTH_SHORT).show();
                result = true;
                dialog.dismiss();

            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(context,"修改失败"+s,Toast.LENGTH_SHORT).show();
                result = false;
                dialog.dismiss();

            }
        });
        return result;
    }
    //个人主页的名片初始化
    public void getBackground(User user,Context context, SlidingMenu slidingMenu){

        /*Drawable drBackground = slidingMenu.getBackground();
        BitmapDrawable bd = (BitmapDrawable) drBackground;
        Bitmap btBackground = bd.getBitmap();*/
        Bitmap btBackground = BitmapFactory.decodeResource(context.getResources(),R.drawable.background);


        String[] backCheck = {"MasterMap","OldPhoto","Rillievo","Negative"};
        String background = user.getBackground();
        if (background==null||background.equals(backCheck[0])){
            BitmapDrawable db = new BitmapDrawable(btBackground);
            slidingMenu.setBackground(db);
        }else if (background.equals(backCheck[1])){
            BitmapDrawable db = new BitmapDrawable(MyBackgroud.pixelEffectOldPhoto(btBackground));
            slidingMenu.setBackground(db);

        }else if (background.equals(backCheck[2])){
            slidingMenu.setBackground(new BitmapDrawable(MyBackgroud.pixelEffectRillievo(btBackground)));
        }else {
            slidingMenu.setBackground(new BitmapDrawable(MyBackgroud.pixelEffectNegative(btBackground)));
        }

    }
    public void getNearUniversitier(final Context context){
        final CustomProgressDialog dialog = new CustomProgressDialog(context,"网络君正在奔跑..", R.drawable.myprogressframe);
        dialog.show();
        Map map = new Map(context,1);
        map.setMapLonLatClickListener(new Map.mapLonLatClickListener() {
            @Override
            public void buttonOnClick(double[] longlat) {
                longitude = longlat[0];
                latitude = longlat[1];
                BmobQuery<PluralistBmob> bmobQuery = new BmobQuery<PluralistBmob>();
                BmobGeoPoint point = new BmobGeoPoint(longitude, latitude);
                bmobQuery.addWhereNear("pBGaddress",point);
                bmobQuery.setLimit(20);    //获取最接近用户地点的10条数据
                bmobQuery.include("user");
                bmobQuery.findObjects(context, new FindListener<PluralistBmob>() {
                    @Override
                    public void onSuccess(List<PluralistBmob> list) {
                        newList = new ArrayList<PluralistBmob>();
                        newList.addAll(list);
                        interfacechlick.getData(newList);
                        dialog.dismiss();
                    }
                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(context,"查询失败",Toast.LENGTH_SHORT).show();
                        interfacechlick.getData(newList);
                        dialog.dismiss();

                    }
                    @Override
                    public void postOnFailure(int code, String msg) {
                        super.postOnFailure(code, msg);
                    }
                });
            }
        });
    }
    public void getAllUniversitier(final Context context){
        BmobQuery<PluralistBmob> bmobQuery = new BmobQuery<PluralistBmob>();
        // 根据score字段降序显示数据
        bmobQuery.order("-updatedAt");
        bmobQuery.include("user");
        bmobQuery.findObjects(context, new FindListener<PluralistBmob>() {
            @Override
            public void onSuccess(List<PluralistBmob> list) {
                newList = new ArrayList<PluralistBmob>();
                newList.addAll(list);
                Log.e("bmobData",newList.size()+"");
                interfacechlick.getData(newList);
            }
            @Override
            public void onError(int i, String s) {
                Toast.makeText(context,"查询失败",Toast.LENGTH_SHORT).show();
                interfacechlick.getData(newList);
            }
        });
    }
    public interface getInterfacechlick{
        public void getData(List<PluralistBmob> list);
    }
    //获取附近大学生的全部信息
    public void getgetAllUniversitierChlick(Context context,getInterfacechlick interfacechlick){
        this.interfacechlick = interfacechlick;
        getAllUniversitier(context);
    }
    //获取全部大学生的全部信息
    public void getNearUniversitierChlick(Context context,getInterfacechlick interfacechlick){
        this.interfacechlick = interfacechlick;
        getNearUniversitier(context);
    }
    //查询应聘消息的
    public void getMkorderNew(final Context context, User user){
        BmobQuery<UnOrderBmob> bmobQuery = new BmobQuery<UnOrderBmob>();
        bmobQuery.order("-updatedAt");
        bmobQuery.include("employer,employee");
        BmobQuery<UnOrderBmob> bmobQuery1 = new BmobQuery<UnOrderBmob>();
        bmobQuery1.addWhereEqualTo("employer",user);
        BmobQuery<UnOrderBmob> bmobQuery2 = new BmobQuery<UnOrderBmob>();
        bmobQuery2.addWhereEqualTo("employee",user);
        List<BmobQuery<UnOrderBmob>> queries = new ArrayList<BmobQuery<UnOrderBmob>>();
        queries.add(bmobQuery1);
        queries.add(bmobQuery2);
        bmobQuery.or(queries);
        bmobQuery.findObjects(context, new FindListener<UnOrderBmob>() {
            @Override
            public void onSuccess(List<UnOrderBmob> list) {
                odBmobList = new ArrayList<UnOrderBmob>();
                odBmobList.addAll(list);
                mOrderInterface.getData(odBmobList);
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(context,"查询失败",Toast.LENGTH_SHORT).show();
                mOrderInterface.getData(odBmobList);
            }
        });
    }
    public interface orderInterface{
        public void getData(List<UnOrderBmob> odBmobList);

    }
    public void getorderInterfaceCherck(Context context,User user,orderInterface mOrderInterface){
        this.mOrderInterface = mOrderInterface;
        getMkorderNew(context,user);

    }



}
