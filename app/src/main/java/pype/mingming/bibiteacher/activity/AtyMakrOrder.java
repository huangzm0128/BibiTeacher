package pype.mingming.bibiteacher.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.entity.PluralistBmob;
import pype.mingming.bibiteacher.entity.UnOrderBmob;
import pype.mingming.bibiteacher.entity.User;
import pype.mingming.bibiteacher.myhomepage.TopBar;
import pype.mingming.bibiteacher.utils.CustomProgressDialog;
import pype.mingming.bibiteacher.utils.Map;
import pype.mingming.bibiteacher.utils.ToastUtils;

/**
 * Created by mk on 2016/10/12.
 */
public class AtyMakrOrder extends Activity implements View.OnClickListener {
    private Calendar calendarDate;
    private Calendar calendarTime = Calendar.getInstance();
    private String startTime = "";
    private String endTime = "";
    private Intent intent = new Intent();
    private String[] adr;
    private String strAdress ="";
    private User user;
    private User employeeUser;
    private Spinner subject;
    private Spinner grade;
     //家教时间
    private TextView stime_tv;
    private TextView etime_tv;
     //薪酬
    private EditText price_et;
    private TextView price_way;
     //位置
    private TextView address_tv;
     //内容
    private EditText context_et;
    private EditText phone_et;
    private Button btnSend;
    private TopBar myTopBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_mkorder);
        user = BmobUser.getCurrentUser(this,User.class);
        PluralistBmob pluralistBmob = (PluralistBmob) getIntent().getSerializableExtra("employeeUser");
        employeeUser = pluralistBmob.getUser();

        subject = (Spinner) findViewById(R.id.subject_spinner2);
        grade = (Spinner) findViewById(R.id.grade_spinner2);
        stime_tv = (TextView) findViewById(R.id.sendpost_stime2);
        etime_tv = (TextView) findViewById(R.id.sendpost_etime2);
        price_et = (EditText) findViewById(R.id.sendpost_price2);
        price_way = (TextView) findViewById(R.id.sendpost_price_way2);
        address_tv = (TextView) findViewById(R.id.sendpost_address2);
        context_et = (EditText) findViewById(R.id.sendpost_context2);
        phone_et = (EditText) findViewById(R.id.sendpost_phone2);
        btnSend = (Button) findViewById(R.id.btnSend);
        myTopBar = (TopBar) findViewById(R.id.sendpost_topBar);


        stime_tv.setOnClickListener(this);
        etime_tv.setOnClickListener(this);
        price_way.setOnClickListener(this);
        address_tv.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        this.setResult(0, intent);

        ArrayAdapter<String> adapterSubject = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, new String[]{"语文","数学","英语","政治","历史","地理","物理","化学","生物","其他"});
        adapterSubject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subject.setAdapter(adapterSubject);

        ArrayAdapter<String> adapterGrade = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, new String[]{"高三","高二","高一","初三","初二","初一","小学"});
        adapterGrade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grade.setAdapter(adapterGrade);
        phone_et.setText(user.getMobilePhoneNumber());
        myTopBar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftButtonOnClick() {
                onBackPressed();
            }
            @Override
            public void rightButtonOnClick() {
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendpost_stime2:
                calendarDate = Calendar.getInstance();//获取当前系统的时间
                setCalendarTime(calendarDate, calendarTime, stime_tv);
                break;
            case R.id.sendpost_etime2:
                calendarDate = Calendar.getInstance();//获取当前系统的时间
                setCalendarTime(calendarDate, calendarTime, etime_tv);
                break;
            case R.id.sendpost_price_way2:
                ToastUtils.showToast(AtyMakrOrder.this, "程序员正在加班研发", 1);
                break;
            case R.id.sendpost_address2:
                //接入定位显示对话框选择位置，确定显示在address_tv(TextView)中
                Map map = new Map(AtyMakrOrder.this, 2);
                map.setMapClickListener(new Map.mapClickListener() {
                    @Override
                    public void buttonOnClick(List<String> strings) {

                        adr = new String[strings.size()];

                        for (int i = 0; i < strings.size(); i++) {
                            adr[i] = strings.get(i);
                        }

                        AlertDialog.Builder dialogMap = new AlertDialog.Builder(AtyMakrOrder.this);
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
                                if (strAdress.equals("")) {
                                    address_tv.setText(adr[1]);
                                } else {
                                    address_tv.setText(strAdress);
                                }
                            }
                        });
                        dialogMap.setNeutralButton("取消", null);
                        dialogMap.show();
                    }
                });
                break;
            case R.id.btnSend:
                if (stime_tv.getText().toString().equals("开始时间")||etime_tv.getText().toString().equals("结束时间")||
                        price_et.getText().toString().equals("")||address_tv.getText().toString().equals("请选择家教地点")||
                        context_et.getText().toString().equals("")||phone_et.getText().toString().equals("")){
                    Toast.makeText(AtyMakrOrder.this,"以上均为必填信息，请填完整。。",Toast.LENGTH_SHORT).show();
                }else {
                    final CustomProgressDialog dialog = new CustomProgressDialog(AtyMakrOrder.this,"网络君正在奔跑..", R.drawable.myprogressframe);
                    dialog.show();
                    BmobQuery<UnOrderBmob> query = new BmobQuery<UnOrderBmob>();
                    query.addWhereEqualTo("employer",user);
                    query.addWhereEqualTo("employee",employeeUser);
                    query.findObjects(AtyMakrOrder.this, new FindListener<UnOrderBmob>() {
                        @Override
                        public void onSuccess(List<UnOrderBmob> list) {
                            if (list.size()>0){
                                dialog.dismiss();
                                Snackbar.make(getCurrentFocus(), "您已经提交过应聘订单了,请等待对方的接受", Snackbar.LENGTH_SHORT).setAction("",null).show();
                            }
                            else {
                                UnOrderBmob unOrderBmob = new UnOrderBmob();
                                unOrderBmob.setEmployer(user);
                                unOrderBmob.setEmployee(employeeUser);
                                unOrderBmob.setEmployeeName(employeeUser.getUsername());
                                unOrderBmob.setEmployerName(user.getUsername());
                                unOrderBmob.setCheck(false);
                                unOrderBmob.setSubject(subject.getSelectedItem().toString()+"   "+grade.getSelectedItem().toString());
                                unOrderBmob.setTime(stime_tv.getText().toString()+"-"+etime_tv.getText().toString());
                                unOrderBmob.setAddress(address_tv.getText().toString());
                                unOrderBmob.setMoney(price_et.getText().toString()+"元/天");
                                unOrderBmob.setColsing(price_way.getText().toString());
                                unOrderBmob.setPhone(phone_et.getText().toString());
                                unOrderBmob.setEvent(context_et.getText().toString());
                                unOrderBmob.save(AtyMakrOrder.this, new SaveListener() {
                                    @Override
                                    public void onSuccess() {
                                        Toast.makeText(AtyMakrOrder.this,"提交成功",Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        onBackPressed();
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        dialog.dismiss();
                                        Toast.makeText(AtyMakrOrder.this,"提交失败，请检查网络",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                        @Override
                        public void onError(int i, String s) {
                            Toast.makeText(AtyMakrOrder.this,"查询失败,请检查网络",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }
                break;
        }
    }
    public void setCalendarTime(final Calendar calendarDate, final Calendar calendarTime, final TextView time_iv){
        DatePickerDialog datePickerDialog = new DatePickerDialog(AtyMakrOrder.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                startTime = year+"/"+(monthOfYear+1)+"/"+dayOfMonth +" ";
                TimePickerDialog timeEndDialog = new TimePickerDialog(AtyMakrOrder.this, new TimePickerDialog.OnTimeSetListener() {
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
    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
