package pype.mingming.bibiteacher.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.entity.PluralistBmob;
import pype.mingming.bibiteacher.entity.User;
import pype.mingming.bibiteacher.myhomepage.TopBar;
import pype.mingming.bibiteacher.ui.CircleImageView;
import pype.mingming.bibiteacher.utils.CustomProgressDialog;
import pype.mingming.bibiteacher.utils.LogUtils;
import pype.mingming.bibiteacher.utils.Map;


/**
 * Created by mk on 2016/9/29.
 */
public class AtyTutorRegi extends BaseActivity implements View.OnClickListener {

    private int[] editTextID = {
            R.id.etMoney,R.id.etTutorPhone,R.id.etUniversity,R.id.etEdit,
    };
    private int[] linearyoutID = {R.id.lySubject,R.id.lyWeek,R.id.lyDay,
            R.id.lyStartTime,R.id.lyEndTime,R.id.lyMap,R.id.lyClosing};

    private LinearLayout[] linearyoutBT = new LinearLayout[linearyoutID.length];
    private EditText[] editTextBT = new EditText[editTextID.length];
    private Button btnTuor;
    private TextView tvTRUserName,tvTRSex,tvSubject,tvWeek,tvDay,
            tvStartTime,tvEndTime,tvMap,tvClosing;
    private CircleImageView imgTutor;
    private Calendar c;
    private Calendar endTime = Calendar.getInstance();
    private String[] week = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
    private boolean[] weekCheck = new boolean[week.length];
    private final String[] subject = {"语文", "英语", "数学", "物理","政治","化学","生物","历史"};
    private String[] clising = {"在线支付","现实支付","无所谓"};
    private String getSubject ="|";
    private int chenckNum = 0;
    private String endStrTime;//结束时段
    private String strClosing = "";//完工结算
    private User user;
    private TopBar myTopBar;
    private Handler mHandler;
    private LocationClient locationClient = null;
    private LocationClientOption mOption = new LocationClientOption();
    private List<String> adress = new ArrayList<String>();
    private String[] adr;
    private String strAdress="";//选择的定位信息
    private BmobGeoPoint geoPoint = new BmobGeoPoint();


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        user = BmobUser.getCurrentUser(AtyTutorRegi.this,User.class);
        for (int i = 0;i<linearyoutID.length;i++){
            linearyoutBT[i] = (LinearLayout) findViewById(linearyoutID[i]);
            linearyoutBT[i].setOnClickListener(this);
        }
        for (int i = 0;i<editTextID.length;i++){
            editTextBT[i] = (EditText) findViewById(editTextID[i]);
        }
        btnTuor = (Button) findViewById(R.id.btnTuor);
        tvTRUserName = (TextView) findViewById(R.id.tvTRUserName);
        tvTRSex = (TextView) findViewById(R.id.tvTRSex);
        tvSubject = (TextView) findViewById(R.id.tvSubject);
        tvWeek = (TextView) findViewById(R.id.tvWeek);
        tvDay = (TextView) findViewById(R.id.tvDay);
        tvStartTime = (TextView) findViewById(R.id.tvStartTime);
        tvEndTime = (TextView) findViewById(R.id.tvEndTime);
        tvMap = (TextView) findViewById(R.id.tvMap);
        tvClosing = (TextView) findViewById(R.id.tvClosing);
        imgTutor = (CircleImageView) findViewById(R.id.imgTutor);
        myTopBar = (TopBar) findViewById(R.id.myTB_tutorregi);
        myTopBar.getLeftImageButtonn().setImageResource(R.drawable.back_white_top);
        myTopBar.getLeftImageButtonn().setVisibility(View.VISIBLE);
        myTopBar.getRightButton().setVisibility(View.GONE);
        btnTuor.setOnClickListener(this);
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
        BmobFile imageFile = user.getAvatar();
        if(imageFile != null){
            String avatorUri = user.getAvatar().getFileUrl(this);
            Picasso.with(this)
                    .load(avatorUri)
                    .into(imgTutor);
        }
        tvTRUserName.setText(user.getUsername());
        tvTRSex.setText("年龄："+userYear+" "+sex);
        editTextBT[1].setText(user.getMobilePhoneNumber());
        myTopBar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftButtonOnClick() {
                onBackPressed();
            }

            @Override
            public void rightButtonOnClick() {
                return;
            }
        });

       /* //线程操作
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        //完成主界面更新,拿到数据
                        String data = (String)msg.obj;
                        updateWeather();
                        tvMap.setText(data);
                        break;
                    default:
                        break;
                }
            }

        };*/
    }
    /*private void updateWeather() {


        new Thread(new Runnable(){

            @Override
            public void run() {
                //耗时操作，完成之后发送消息给Handler，完成UI更新；

                mHandler.sendEmptyMessage(0);
                //需要数据传递，用下面方法；
                Message msg =new Message();
                msg.obj = "数据";//可以是基本类型，可以是对象，可以是List、map等；
                mHandler.sendMessage(msg);
            }

        }).start();

    }*/

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tutoregi;
    }

    //初始化定位参数
    public void initOption(LocationClientOption mOption){
        mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        mOption.setScanSpan(3000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        mOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        mOption.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
        mOption.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
        mOption.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        mOption.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        mOption.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        mOption.setIsNeedAltitude(false);//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lySubject:
                getSubject = "";
                LayoutInflater inflater = LayoutInflater.from(this);
                LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.mydialog, null);
                final int[] checkID = {R.id.check1,R.id.check2,R.id.check3,R.id.check4,
                        R.id.check5,R.id.check6,R.id.check7,R.id.check8};
                final CheckBox[] checkBoxes = new CheckBox[checkID.length];
                final Dialog dialog = new AlertDialog.Builder(AtyTutorRegi.this).create();
                dialog.setCancelable(false);
                dialog.show();
                dialog.getWindow().setContentView(layout);
                dialog.setTitle("擅长科目（只能选三科）");

                for (int i=0;i<checkID.length;i++){
                    checkBoxes[i] = (CheckBox) layout.findViewById(checkID[i]);
                }
                layout.findViewById(R.id.btn_checkSubmit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i=0;i<checkID.length;i++){
                            if (checkBoxes[i].isChecked()){
                                chenckNum++;
                            }
                            Log.e("error",checkBoxes[i].isChecked()+" "+chenckNum);
                        }
                        if(chenckNum>3){
                            Toast.makeText(AtyTutorRegi.this,"不能超过3个",Toast.LENGTH_SHORT).show();
                            chenckNum = 0;
                        }else {
                            for (int i=0;i<checkID.length;i++){
                                if (checkBoxes[i].isChecked()){
                                    getSubject += subject[i]+"|";
                                }
                            }
                            chenckNum = 0;
                            dialog.dismiss();
                            tvSubject.setText(getSubject);
                        }
                    }
                });
                layout.findViewById(R.id.btn_checkColes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.lyWeek:
                weekCheck = new boolean[week.length];
                AlertDialog.Builder builder = new AlertDialog.Builder(AtyTutorRegi.this);
                builder.setTitle("空闲周天");
                builder.setIcon(android.R.drawable.ic_menu_week);
                builder.setMultiChoiceItems(week, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        weekCheck[which] = isChecked;
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    String weekNum = "星期：";
                    String w = "|";
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i=0;i<week.length;i++){
                            if (weekCheck[i]){
                                w +=i+1+"|";
                            }
                        }
                        weekNum = weekNum+w;
                        tvWeek.setText(weekNum);
                    }
                });
                builder.setNeutralButton("取消",null);
                builder.show();
                break;
            case R.id.lyDay:
                c = Calendar.getInstance();//获取当前系统的时间
                DatePickerDialog datePickerDialog = new DatePickerDialog(AtyTutorRegi.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String day = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                        tvDay.setText(day);
                    }
                },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE));
                datePickerDialog.setTitle("有效期");
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickerDialog.show();
                break;
            case R.id.lyStartTime:
                c = Calendar.getInstance();//获取当前系统的时间
                final TimePickerDialog timePickerDialog = new TimePickerDialog(AtyTutorRegi.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String startTime = hourOfDay+":"+minute;
                        tvStartTime.setText(startTime);
                        c.set(Calendar.HOUR,hourOfDay);
                        c.set(Calendar.MINUTE,minute);
                        c.set(Calendar.SECOND,0);
                        c.set(Calendar.MILLISECOND,0);
                    }
                },c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true);
                timePickerDialog.setTitle("开始时段");
                timePickerDialog.show();
                break;
            case R.id.lyEndTime:
                if (tvStartTime.getText().equals("开始时段")){
                    Toast.makeText(AtyTutorRegi.this,"请选择开始时段",Toast.LENGTH_SHORT).show();
                }else {
                    final TimePickerDialog timeEndDialog = new TimePickerDialog(AtyTutorRegi.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            endTime.set(Calendar.HOUR,hourOfDay);
                            endTime.set(Calendar.MINUTE,minute);
                            endTime.set(Calendar.SECOND,0);
                            endTime.set(Calendar.MILLISECOND,0);
                            endStrTime = hourOfDay+":"+minute;
                            if (endTime.getTimeInMillis()<=c.getTimeInMillis()){
                                Toast.makeText(AtyTutorRegi.this,"结束时段不能比开始时段小",Toast.LENGTH_SHORT).show();
                            }else {
                                tvEndTime.setText(endStrTime);
                            }

                        }
                    },c.get(Calendar.HOUR),c.get(Calendar.MINUTE),true);

                    timeEndDialog.setTitle("结束时段");
                    timeEndDialog.show();
                }
                break;
            case R.id.lyMap:
                adress = new ArrayList<String>();//清空历史定位信息
                locationClient = new LocationClient(this);
                initOption(mOption);
                //定位参数注入
                locationClient.setLocOption(mOption);
                locationClient.registerLocationListener(mListener);
                locationClient.start();
                break;
            case R.id.lyClosing:
                AlertDialog.Builder bClosing = new AlertDialog.Builder(AtyTutorRegi.this);
                bClosing.setTitle("完工结算");
                bClosing.setSingleChoiceItems(clising,2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        strClosing = clising[which];
                    }
                });
                bClosing.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (strClosing.equals("")){
                            tvClosing.setText(clising[2]);
                        }else {
                            tvClosing.setText(strClosing);
                        }

                    }
                });
                bClosing.setNeutralButton("取消",null);
                bClosing.show();
                break;
            case R.id.btnTuor:
                if (tvStartTime.getText().toString().equals("开始时段")||tvEndTime.getText().toString().equals("结束时段")){
                    Toast.makeText(AtyTutorRegi.this,"开始时段/结束时段 信息没填",Toast.LENGTH_SHORT).show();
                    break;
                }
                if (tvSubject.equals("擅长科目")){
                    tvStartTime.setText("语文");
                }
                if (tvWeek.equals("有空星期")){
                    tvWeek.setText("全周有空");
                }
                if (tvDay.equals("有效期")){
                    tvDay.setText("");
                }
                if (tvClosing.equals("完工结算")){
                    tvClosing.setText("无所谓");
                }
                final CustomProgressDialog progressDialog = new CustomProgressDialog(AtyTutorRegi.this,"网络君正在奔跑..", R.drawable.myprogressframe);
                progressDialog.show();
                final PluralistBmob pluralistBmob = new PluralistBmob();
                pluralistBmob.setUser(user);
                pluralistBmob.setpBGaddress(geoPoint);
                pluralistBmob.setpUserName(user.getUsername());
                pluralistBmob.setpPhone(editTextBT[1].getText().toString());
                pluralistBmob.setpSex(tvTRSex.getText().toString());
                pluralistBmob.setpSubject(tvSubject.getText().toString());
                pluralistBmob.setpClosing(tvClosing.getText().toString());
                pluralistBmob.setpDay(tvDay.getText().toString());
                pluralistBmob.setpWeek(tvWeek.getText().toString());
                pluralistBmob.setpTime(tvStartTime.getText().toString()+"-"+tvEndTime.getText().toString());
                pluralistBmob.setpUniversity(editTextBT[2].getText().toString());
                pluralistBmob.setpIntroduce(editTextBT[3].getText().toString());
                pluralistBmob.setpAddress(tvMap.getText().toString());
                final BmobQuery<PluralistBmob> query = new BmobQuery<PluralistBmob>();
                query.addWhereEqualTo("user",user);
                query.findObjects(AtyTutorRegi.this, new FindListener<PluralistBmob>() {

                    @Override
                    public void onSuccess(final List<PluralistBmob> list) {
                        if(list.size()<1){
                            pluralistBmob.save(AtyTutorRegi.this, new SaveListener() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(AtyTutorRegi.this,"提交成功。。",Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    onBackPressed();
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    LogUtils.i("1111",s);
                                    Toast.makeText(AtyTutorRegi.this,"提交失败。。",Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            });
                        }else {
                            list.get(0).setUser(user);
                            list.get(0).setpUserName(user.getUsername());
                            list.get(0).setpBGaddress(geoPoint);
                            list.get(0).setpPhone(editTextBT[1].getText().toString());
                            list.get(0).setpSex(tvTRSex.getText().toString());
                            list.get(0).setpSubject(tvSubject.getText().toString());
                            list.get(0).setpClosing(tvClosing.getText().toString());
                            list.get(0).setpDay(tvDay.getText().toString());
                            list.get(0).setpTime(tvStartTime.getText().toString()+"--"+tvEndTime.getText().toString());
                            list.get(0).setpUniversity(editTextBT[2].getText().toString());
                            list.get(0).setpIntroduce(editTextBT[3].getText().toString());
                            list.get(0).setpAddress(tvMap.getText().toString());
                            list.get(0).setpWeek(tvWeek.getText().toString());
                            list.get(0).update(AtyTutorRegi.this, new UpdateListener() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(AtyTutorRegi.this,"提交成功。。",Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    onBackPressed();

                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    Log.e("error",s);
                                    Toast.makeText(AtyTutorRegi.this,"提交失败。。",Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.e("error",s);
                        Toast.makeText(AtyTutorRegi.this,"联网失败。。",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });

                break;
        }
    }
    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(final BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                /*StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                *//**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 *//*
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                sb.append(location.getLocTypeDescription());
                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");// 经度
                sb.append(location.getLongitude());
                sb.append("\nradius : ");// 半径
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");// 国家码
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");// 国家名称
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");// 城市编码
                sb.append(location.getCityCode());
                sb.append("\ncity : ");// 城市
                sb.append(location.getCity());
                sb.append("\nDistrict : ");// 区
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");// 街道
                sb.append(location.getStreet());
                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddrStr());
                sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
                sb.append(location.getUserIndoorState());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());// 方向
                sb.append("\nlocationdescribe: ");
                sb.append(location.getLocationDescribe());// 位置语义化信息
                sb.append("\nPoi: ");// POI信息*/
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        adress.add(location.getPoiList().get(i).getName()+"");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果

                    Toast.makeText(AtyTutorRegi.this,"gps定位成功",Toast.LENGTH_SHORT).show();
                }else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    Toast.makeText(AtyTutorRegi.this,"离线定位成功，离线定位结果也是有效的",Toast.LENGTH_SHORT).show();

                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    Toast.makeText(AtyTutorRegi.this,"服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因",Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    Toast.makeText(AtyTutorRegi.this,"网络不同导致定位失败，请检查网络是否通畅",Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    Toast.makeText(AtyTutorRegi.this,"无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机",Toast.LENGTH_SHORT).show();
                }
                adr = new String[adress.size()];
                for (int i=0;i<adress.size();i++){
                    adr[i] = adress.get(i);
                }
                AlertDialog.Builder dialogMap = new AlertDialog.Builder(AtyTutorRegi.this);
                dialogMap.setTitle("地理定位。。");
                dialogMap.setSingleChoiceItems(adr, 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        strAdress = adr[which];
                    }
                });
                dialogMap.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (strAdress.equals("")){
                            tvMap.setText(adr[1]);
                        }else {
                            tvMap.setText(strAdress);
                        }
                        geoPoint = new BmobGeoPoint(location.getLongitude(),location.getLatitude());
                        locationClient.stop();
                    }
                });
                dialogMap.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        locationClient.stop();

                    }
                });
                dialogMap.show();

            }
        }

    };
}
