package pype.mingming.bibiteacher.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.SaveListener;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.entity.Post;
import pype.mingming.bibiteacher.entity.User;
import pype.mingming.bibiteacher.myhomepage.TopBar;
import pype.mingming.bibiteacher.utils.Map;
import pype.mingming.bibiteacher.utils.ToastUtils;

/**
 * Created by wushaohongly on 2016/10/5.
 */

public class SendPostActivity extends BaseActivity implements View.OnClickListener{

    private Calendar calendarDate;
    private Calendar calendarTime = Calendar.getInstance();
    private String startTime = "";
    private String endTime = "";
    private Intent intent = new Intent();
    private String[] adr;
    private String strAdress ="";

    Post post = new Post();

    @BindView(R.id.sendpost_topBar)
    TopBar topBar;

    //标题
    @BindView(R.id.sendpost_title)
    TextInputEditText title_et;

    @BindView(R.id.subject_spinner)
    Spinner subject;
    @BindView(R.id.grade_spinner)
    Spinner grade;

    //家教时间
    @BindView(R.id.sendpost_stime)
    TextView stime_tv;
    @BindView(R.id.sendpost_etime)
    TextView etime_tv;

    //薪酬
    @BindView(R.id.sendpost_price)
    EditText price_et;
    @BindView(R.id.sendpost_price_way)
    TextView price_way;

    //位置
    @BindView(R.id.sendpost_address)
    TextView address_tv;

    //内容
    @BindView(R.id.sendpost_context)
    EditText context_et;

    //电话
    @BindView(R.id.sendpost_phone)
    EditText phone_et;

    //发布按钮
    @BindView(R.id.sendpost_bt)
    Button send_bt;

    private User user;

    @Override
    protected int getLayoutId() {
        return R.layout.sendpostlayout;
    }

    @Override
    protected void initDada() {
        super.initDada();

        initToolbar();
        if (isLogin()){
            phone_et.setText(user.getMobilePhoneNumber());
        }
        stime_tv.setOnClickListener(this);
        etime_tv.setOnClickListener(this);
        price_way.setOnClickListener(this);
        address_tv.setOnClickListener(this);
        send_bt.setOnClickListener(this);
        this.setResult(0, intent);

        ArrayAdapter<String> adapterSubject = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, new String[]{"语文","数学","英语","政治","历史","地理","物理","化学","生物","其他"});
        adapterSubject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subject.setAdapter(adapterSubject);

        ArrayAdapter<String> adapterGrade = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, new String[]{"高三","高二","高一","初三","初二","初一","小学"});
        adapterGrade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grade.setAdapter(adapterGrade);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sendpost_stime:
                calendarDate = Calendar.getInstance();//获取当前系统的时间
                setCalendarTime(calendarDate, calendarTime, stime_tv);
                break;
            case R.id.sendpost_etime:
                calendarDate = Calendar.getInstance();//获取当前系统的时间
                setCalendarTime(calendarDate, calendarTime, etime_tv);
                break;
            case R.id.sendpost_price_way:
                ToastUtils.showToast(context, "程序员正在加班研发", 1);
                break;
            case R.id.sendpost_address:
                //接入定位显示对话框选择位置，确定显示在address_tv(TextView)中
                Map map = new Map(SendPostActivity.this,2);
                map.setMapClickListener(new Map.mapClickListener() {
                    @Override
                    public void buttonOnClick(List<String> strings) {
                        adr = new String[strings.size()];
                        for (int i=0;i<strings.size();i++){
                            adr[i] = strings.get(i);
                        }
                        AlertDialog.Builder dialogMap = new AlertDialog.Builder(SendPostActivity.this);
                        dialogMap.setTitle("地理定位");
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
                                    address_tv.setText(adr[1]);
                                }else {
                                    address_tv.setText(strAdress);
                                }
                            }
                        });
                        dialogMap.setNeutralButton("取消",null);
                        dialogMap.show();
                    }
                });
                break;
            case R.id.sendpost_bt:
                if (sendPost()){

                }else {
                    ToastUtils.showToast(context, "请完善填写内容", 0);
                }
                break;
        }
    }

    public void setCalendarTime(final Calendar calendarDate, final Calendar calendarTime, final TextView time_iv){
        DatePickerDialog datePickerDialog = new DatePickerDialog(SendPostActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                startTime = year+"/"+(monthOfYear+1)+"/"+dayOfMonth +" ";
                TimePickerDialog timeEndDialog = new TimePickerDialog(SendPostActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendarTime.set(Calendar.HOUR,hourOfDay);
                        calendarTime.set(Calendar.MINUTE,minute);
                        calendarTime.set(Calendar.SECOND,0);
                        calendarTime.set(Calendar.MILLISECOND,0);
                        startTime += hourOfDay+":"+minute;
                        time_iv.setText(startTime);
                    }
                },calendarDate.get(Calendar.HOUR),calendarDate.get(Calendar.MINUTE),true);
                timeEndDialog.setTitle("选择时间");
                timeEndDialog.show();
            }
        },calendarDate.get(Calendar.YEAR),calendarDate.get(Calendar.MONTH),calendarDate.get(Calendar.DATE));
        datePickerDialog.setTitle("选择日期");
        datePickerDialog.getDatePicker().setMinDate(calendarDate.getTimeInMillis());
        datePickerDialog.show();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    SendPostActivity.this.setResult(1, intent);
                    finish();
                    break;
            }
        }
    };

    public boolean sendPost(){
        String title = title_et.getText().toString();
        String s = subject.getSelectedItem().toString();
        String g = grade.getSelectedItem().toString();

        startTime = stime_tv.getText().toString();
        endTime = etime_tv.getText().toString();

        if (price_et.getText().toString().equals("") || price_et.getText().toString() == ""){
            return false;
        }
        double price = Double.valueOf(price_et.getText().toString());
        String address;
        address = address_tv.getText().toString();

        String c = context_et.getText().toString();
        String phone = phone_et.getText().toString();

        User user = BmobUser.getCurrentUser(this, User.class);

        Date sd = new Date();
        Date ed = new Date();

        if (title.equals("") || title == null){
            ToastUtils.showToast(context, "请完整描述招募信息", 0);
            return false;
        }
        if (startTime.equals("开始时间") || startTime == "开始时间"){
            ToastUtils.showToast(context, "请完整描述招募信息", 0);
            return false;
        }
        if (endTime.equals("结束时间") || startTime == "结束时间"){
            ToastUtils.showToast(context, "请完整描述招募信息", 0);
            return false;
        }
        if (address.equals("") || address == null){
            ToastUtils.showToast(context, "请完整描述招募信息", 0);
            return false;
        }
        if (c.equals("") || c == null){
            ToastUtils.showToast(context, "请完整描述招募信息", 0);
            return false;
        }
        if (phone.equals("") || phone == null){
            ToastUtils.showToast(context, "请完整描述招募信息", 0);
            return false;
        }

        DateFormat fmt =new SimpleDateFormat("yyyy/MM/dd hh:mm");
        try {
            sd = fmt.parse(startTime);
            ed = fmt.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.i("info", sd.toString() +"-"+ed.toString());

        post.setPostUser(user);
        post.setPostTitle(title);
        post.setSubject(s);
        post.setGrade(g);
        post.setStartTime(new BmobDate(sd));
        post.setEndTime(new BmobDate(ed));
        post.setPrice(price);
        post.setTeachAddress(address);
        post.setPostContent(c);
        post.setPhone(phone);
        post.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });

        return true;
    }

    /*
     * 标题栏
     */
    private void initToolbar() {
        topBar.getLeftImageButtonn().setImageResource(R.drawable.back_white_top);
        topBar.getLeftImageButtonn().setVisibility(View.VISIBLE);
        topBar.getRightButton().setVisibility(View.GONE);
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
}
