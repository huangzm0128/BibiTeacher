package pype.mingming.bibiteacher.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Calendar;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.entity.Orders;
import pype.mingming.bibiteacher.entity.UnOrderBmob;
import pype.mingming.bibiteacher.entity.User;
import pype.mingming.bibiteacher.myhomepage.TopBar;
import pype.mingming.bibiteacher.utils.CustomProgressDialog;

/**
 * Created by mk on 2016/10/13.
 */
public class AtyMkOrderMore extends Activity {
    private TextView tvEmploy2,tvEmployUser,tvClosing,tvMap,tvTime;
    private EditText etMoney,etTutorPhone,etEdit;
    private UnOrderBmob unOrderBmob;
    private User user;
    private ImageView imgEmploy,imgYesNo;
    private Button btnConsider,btnAccept;
    private TopBar topBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mkorder_more);
        tvEmploy2 = (TextView) findViewById(R.id.tvEmploy2);
        tvEmployUser = (TextView) findViewById(R.id.tvEmployUser);
        tvClosing = (TextView) findViewById(R.id.tvClosing);
        tvMap = (TextView) findViewById(R.id.tvMap);
        etMoney = (EditText) findViewById(R.id.etMoney);
        etTutorPhone = (EditText) findViewById(R.id.etTutorPhone);
        etEdit = (EditText) findViewById(R.id.etEdit);
        imgEmploy = (ImageView) findViewById(R.id.imgEmploy);
        tvTime = (TextView) findViewById(R.id.tvTime);
        imgYesNo = (ImageView) findViewById(R.id.imgYesNo);
        btnConsider = (Button) findViewById(R.id.btnConsider);
        btnAccept = (Button) findViewById(R.id.btnAccept);
        topBar = (TopBar) findViewById(R.id.myTopBar);
        topBar.getLeftImageButtonn().setImageResource(R.drawable.back_white_top);
        topBar.getLeftImageButtonn().setVisibility(View.VISIBLE);
        topBar.getRightButton().setVisibility(View.GONE);

        unOrderBmob = (UnOrderBmob) getIntent().getSerializableExtra("UnOrderBmob");
        user = (User) getIntent().getSerializableExtra("user");
        if (unOrderBmob.getCheck()){
            btnAccept.setEnabled(false);
            btnAccept.setText("已接受");
        }

        if (unOrderBmob.getEmployer().getUsername().equals(user.getUsername())){
            btnAccept.setVisibility(View.INVISIBLE);
            btnConsider.setVisibility(View.INVISIBLE);
            BmobFile imageFile = unOrderBmob.getEmployee().getAvatar();
            if(imageFile != null){
                String avatorUri = unOrderBmob.getEmployee().getAvatar().getFileUrl(this);
                Picasso.with(this)
                        .load(avatorUri)
                        .into(imgEmploy);
            }
            tvEmploy2.setText("雇员：");
            int userYear = 0;
            if (unOrderBmob.getEmployee().getBirthDay()!=null){
                String[] data = unOrderBmob.getEmployee().getBirthDay().split("-");
                Calendar c = Calendar.getInstance();//获取当前日期
                int nowYear = c.get(Calendar.YEAR);
                userYear = nowYear - Integer.parseInt(data[0]);
            }
            String sex = null;
            if (unOrderBmob.getEmployee().getSex()==null||user.getSex()){
                sex = "男";
            }else {
                sex = "女";
            }
            tvEmployUser.setText(unOrderBmob.getEmployee().getUsername()+" "+"年龄："+userYear+"   "+sex);
            etEdit.setText(unOrderBmob.getEvent()+"\n"+"时间："+unOrderBmob.getTime()+"\n"+
            "科目："+unOrderBmob.getSubject());
            tvTime.setText("创建于："+unOrderBmob.getCreatedAt());
            tvClosing.setText(unOrderBmob.getColsing());
            tvMap.setText(unOrderBmob.getAddress());
            etMoney.setText(unOrderBmob.getMoney());
            etTutorPhone.setText(unOrderBmob.getPhone());
            if (unOrderBmob.getCheck()){
                imgYesNo.setImageResource(R.mipmap.yes);
            }
        }else {
            tvEmploy2.setText("雇主：");

            BmobFile imageFile = unOrderBmob.getEmployer().getAvatar();
            if(imageFile != null){
                String avatorUri = unOrderBmob.getEmployer().getAvatar().getFileUrl(this);
                Picasso.with(this)
                        .load(avatorUri)
                        .into(imgEmploy);
            }
            int userYear = 0;
            if (unOrderBmob.getEmployer().getBirthDay()!=null){
                String[] data = unOrderBmob.getEmployer().getBirthDay().split("-");
                Calendar c = Calendar.getInstance();//获取当前日期
                int nowYear = c.get(Calendar.YEAR);
                userYear = nowYear - Integer.parseInt(data[0]);
            }
            String sex = null;
            if (unOrderBmob.getEmployer().getSex()==null||unOrderBmob.getEmployer().getSex()){
                sex = "男";
            }else {
                sex = "女";
            }
            tvEmployUser.setText(unOrderBmob.getEmployer().getUsername()+" "+"年龄："+userYear+"   "+sex);
            etEdit.setText(unOrderBmob.getEvent()+"\n"+"时间："+unOrderBmob.getTime()+"\n"+
                    "科目："+unOrderBmob.getSubject());
            tvTime.setText("创建于："+unOrderBmob.getCreatedAt());
            tvClosing.setText(unOrderBmob.getColsing());
            tvMap.setText(unOrderBmob.getAddress());
            etTutorPhone.setText(unOrderBmob.getPhone());
            etMoney.setText(unOrderBmob.getMoney());
            if (unOrderBmob.getCheck()){
                imgYesNo.setImageResource(R.mipmap.yes);
            }
        }
        btnConsider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnAccept.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final CustomProgressDialog dialog = new CustomProgressDialog(AtyMkOrderMore.this,"网络君正在奔跑..", R.drawable.myprogressframe);
                dialog.show();
                unOrderBmob.setCheck(true);
                unOrderBmob.update(AtyMkOrderMore.this, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Orders orders = new Orders();
                        orders.setUnOrderBmob(unOrderBmob);
                        orders.save(AtyMkOrderMore.this, new SaveListener() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(AtyMkOrderMore.this,"已接受",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                onBackPressed();
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                Toast.makeText(AtyMkOrderMore.this,"联网失败",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();

                            }
                        });
                    }
                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(AtyMkOrderMore.this,"联网失败",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });
        topBar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftButtonOnClick() {
                onBackPressed();
            }

            @Override
            public void rightButtonOnClick() {

            }
        });

    }
}
