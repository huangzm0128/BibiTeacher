package pype.mingming.bibiteacher.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.entity.BmobData;
import pype.mingming.bibiteacher.myhomepage.TopBar;
import pype.mingming.bibiteacher.utils.CustomProgressDialog;

/**
 * Created by mk on 2016/9/18.
 */
public class AtyChangePassword extends BaseActivity {
    private EditText etOldPassword,etNewPassword1,etNewPassword2;
    private Button btnPassword;
    private ImageView imagePassword1,imagePassword2;
    private TopBar myTopBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwordchange);
        etOldPassword = (EditText) findViewById(R.id.etOldPassword);
        etNewPassword1 = (EditText) findViewById(R.id.etNewPassword1);
        etNewPassword2 = (EditText) findViewById(R.id.etNewPassword2);
        btnPassword = (Button) findViewById(R.id.btnPassword);
        imagePassword2 = (ImageView) findViewById(R.id.imagePassword2);
        imagePassword1 = (ImageView) findViewById(R.id.imagePassword1);
        myTopBar = (TopBar) findViewById(R.id.myTB_password);
        //设置返回图标
        myTopBar.getLeftImageButtonn().setImageResource(R.drawable.back_white_top);
        myTopBar.getLeftImageButtonn().setVisibility(View.VISIBLE);
        myTopBar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftButtonOnClick() {
                if(BmobUser.getCurrentUser(AtyChangePassword.this)==null){
                    switchActivity(LoginActivity.class, true);
                }else {
                    onBackPressed();
                }
            }
            @Override
            public void rightButtonOnClick() {
                /*if(BmobUser.getCurrentUser(AtyChangePassword.this)==null){
                    switchActivity(LoginActivity.class, true);
                }else {
                    Intent intent = new Intent(AtyChangePassword.this,AtyRecyclerView.class);
                    startActivity(intent);
                }*/
            }
        });

        btnPassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String old = etOldPassword.getText().toString();
                String new1 = etNewPassword1.getText().toString();
                String new2 = etNewPassword2.getText().toString();
                Log.e("error",old + "---"+new1+"---"+new2);
                if (new1.length()<6||new1.length()>18){
                    Toast.makeText(AtyChangePassword.this,"密码长度大于6小于18",Toast.LENGTH_SHORT).show();
                    imagePassword1.setImageResource(R.drawable.ic_close_light);
                    imagePassword1.setVisibility(View.VISIBLE);
                    return;
                }
                if(new1.equals(new2)){
                    imagePassword2.setImageResource(R.drawable.bmob_update_btn_check_on_holo_light);
                    imagePassword2.setVisibility(View.VISIBLE);
                    BmobData bmobData = new BmobData();
                    boolean result = bmobData.changePassw(AtyChangePassword.this,old,new1);
                    if (result){
                        BmobUser.logOut(context);
                        switchActivity(MainActivity.class, true);
                    }

                }else {
                    Toast.makeText(AtyChangePassword.this,"密码不一致",Toast.LENGTH_SHORT).show();
                    imagePassword2.setImageResource(R.drawable.ic_close_light);
                    imagePassword2.setVisibility(View.VISIBLE);

                }

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_passwordchange;
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
