package pype.mingming.bibiteacher.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobUser;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.entity.BmobData;
import pype.mingming.bibiteacher.entity.User;
import pype.mingming.bibiteacher.entity.UserDate;
import pype.mingming.bibiteacher.myhomepage.TopBar;
import pype.mingming.bibiteacher.utils.Map;

/**
 * 这是用户个人资料的编辑界面
 * Created by mk on 2016/9/10.
 */
public class AtyDatumEdit extends BaseActivity implements View.OnClickListener, View.OnTouchListener {

    private TextView tvUserName,tvData;
    private RadioGroup sexRadio;
    private TopBar myTopBar;
    private EditText etIdiograph,etAddress,etPhone,etRealName;
    private RadioButton btnBoyRadio,btnGirlRadio;
    private Button btnChange,btnCancel;
    private UserDate userDate = new UserDate();
    private Calendar c = Calendar.getInstance();//获取当前系统的时间
    private TextInputLayout tlIdiograph;
    private BmobUser bmobUser;
    private User user;
    private BmobData bmobData = new BmobData();
    private String[] strAddress = {"直接定位选择","开启手动填写"};
    private String[] adr;
    private String strAdress ="";
    private AlertDialog md;
    @Override
    protected void initDada() {
        bmobUser = BmobUser.getCurrentUser(context);
        user = BmobUser.getCurrentUser(context,User.class);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvData = (TextView) findViewById(R.id.tvData);
        sexRadio = (RadioGroup) findViewById(R.id.sexRadio);
        etIdiograph = (EditText) findViewById(R.id.etIdiograph);
        etAddress = (EditText)findViewById(R.id.etAddress);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etRealName = (EditText) findViewById(R.id.etRealName);
        btnBoyRadio = (RadioButton) findViewById(R.id.btnBoyRadio);
        btnGirlRadio = (RadioButton) findViewById(R.id.btnGirlRadio);
        btnChange = (Button) findViewById(R.id.btnChange);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        myTopBar = (TopBar) findViewById(R.id.myTB_datumedit);
        tlIdiograph = (TextInputLayout) findViewById(R.id.tlIdiograph);
        etAddress.setFocusableInTouchMode(false);
        etAddress.setOnClickListener(this);
        /*etAddress.setOnTouchListener(this);*/

        //设置返回图标
        myTopBar.getLeftImageButtonn().setImageResource(R.drawable.back_white_top);
        myTopBar.getLeftImageButtonn().setVisibility(View.VISIBLE);

//      tlIdiograph.getEditText().addTextChangedListener(new MinLengthTextWatcher(tlIdiograph, "长度应低于50!"));
        tlIdiograph.setCounterEnabled(true);
        tlIdiograph.setCounterMaxLength(50);
        getUser();
    }
    //获取用户信息和初始化控件
    public void getUser() {
        if (user!=null){
            tvUserName.setText(user.getUsername());
            etPhone.setText(user.getMobilePhoneNumber());
            etAddress.setText(user.getAddress());
            etRealName.setText(user.getRealName());
            etIdiograph.setText(user.getSignature());
            if (user.getBirthDay()==null){
                tvData.setText("点我");
            }else {
                tvData.setText(user.getBirthDay());
            }
            if (user.getSex()==null||user.getSex()){
                sexRadio.check(R.id.btnBoyRadio);
            }else if (user.getSex()==false){
                sexRadio.check(R.id.btnGirlRadio);
            }
        }

    }
    /*//内部类！进行输入框内容的监控
    class MinLengthTextWatcher implements TextWatcher {
        private String errorStr;
        private TextInputLayout textInputLayout;
        public  MinLengthTextWatcher(TextInputLayout textInputLayout, String errorStr){
            this.textInputLayout = textInputLayout;
            this.errorStr = errorStr;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Editable s) {
            // 文字改变后回调
            if(textInputLayout.getEditText().getText().toString().length()<=50){
                textInputLayout.setErrorEnabled(false);
            }else{
                textInputLayout.setErrorEnabled(true);
                textInputLayout.setError(errorStr);
            }
        }
    }*/
    @Override
    protected void setListener() {
        super.setListener();
        //自定义标题栏监听事件
        myTopBar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftButtonOnClick() {
                //导航栏的左菜单监听事件
              onBackPressed();
            }

            @Override
            public void rightButtonOnClick() {
                Intent intent = new Intent(AtyDatumEdit.this,AtyChangePassword.class);
                startActivity(intent);
            }
        });
        //性别选择器
        sexRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (btnBoyRadio.getId()==checkedId){
                    userDate.setSex(true);
                }
                else if (btnGirlRadio.getId()==checkedId){
                    userDate.setSex(false);
                }
            }
        });
        tvData.setOnClickListener(this);
        btnChange.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.datum_edit;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvData:
                new DatePickerDialog(AtyDatumEdit.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String data = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                        tvData.setText(data);
                        userDate.setData(data);

                    }
                },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE)).show();

                break;
            case R.id.btnChange:
                //数据更新
                userDate.setPersonality(etIdiograph.getText().toString());
                userDate.setAddress(etAddress.getText().toString());
                userDate.setPhone(etPhone.getText().toString());
                userDate.setRealName(etRealName.getText().toString());
                if (bmobData.updateData(userDate,bmobUser,AtyDatumEdit.this)){
                    onBackPressed();
                }
                break;
            case R.id.btnCancel:
                onBackPressed();
//                Intent intent = new Intent(AtyDatumEdit.this,UserActivity.class);
//                startActivity(intent);
                break;
            case R.id.etAddress:
                new AlertDialog.Builder(AtyDatumEdit.this).setTitle("选择")
                        .setItems(strAddress, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which==0){
                                    //接入定位显示对话框选择位置，确定显示在address_tv(TextView)中
                                    Map map = new Map(AtyDatumEdit.this,2);
                                    map.setMapClickListener(new Map.mapClickListener() {
                                        @Override
                                        public void buttonOnClick(List<String> strings) {
                                            adr = new String[strings.size()];
                                            for (int i=0;i<strings.size();i++){
                                                adr[i] = strings.get(i);
                                            }
                                            AlertDialog.Builder dialogMap = new AlertDialog.Builder(AtyDatumEdit.this);
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
                                                        etAddress.setText(adr[1]);
                                                    }else {
                                                        etAddress.setText(strAdress);
                                                    }
                                                }
                                            });
                                            dialogMap.setNeutralButton("取消",null);
                                            dialogMap.show();
                                        }
                                    });
                                }else if (which==1){
                                    etAddress.setFocusableInTouchMode(true);
                                    etAddress.setFocusable(true);
                                }
                            }
                        }
                        ).setNeutralButton("取消",null).show();
                break;
        }
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean click = true;
        final AlertDialog.Builder mDialog = new AlertDialog.Builder(AtyDatumEdit.this);
        mDialog.setTitle("选择")
                .setItems(strAddress, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            mDialog.create();
                            //接入定位显示对话框选择位置，确定显示在address_tv(TextView)中
                            Map map = new Map(AtyDatumEdit.this, 2);
                            map.setMapClickListener(new Map.mapClickListener() {
                                @Override
                                public void buttonOnClick(List<String> strings) {
                                    adr = new String[strings.size()];
                                    for (int i = 0; i < strings.size(); i++) {
                                        adr[i] = strings.get(i);
                                    }
                                    AlertDialog.Builder dialogMap = new AlertDialog.Builder(AtyDatumEdit.this);
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
                                                etAddress.setText(adr[1]);
                                            } else {
                                                etAddress.setText(strAdress);
                                            }
                                        }
                                    });
                                    dialogMap.setNeutralButton("取消", null);
                                    dialogMap.show();
                                }
                            });
                        }
                    }
                }).setNeutralButton("取消",null);
                mDialog.show();
        return click;
    }
}
