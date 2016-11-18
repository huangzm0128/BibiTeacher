package pype.mingming.bibiteacher.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kyleduo.switchbutton.SwitchButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.myhomepage.TopBar;
import pype.mingming.bibiteacher.utils.ToastUtils;

/**
 * 设置页面
 * Created by mingming on 2016/9/17.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener{
    Context context;

    @BindView(R.id.myTopBar)
    TopBar myTopBar;
    @BindView(R.id.sb_2G_3G)
    SwitchButton sb2G3G;
    @BindView(R.id.sb_pull_message)
    SwitchButton sbPullMessage;
    @BindView(R.id.linear_about)
    LinearLayout linearAbout;
    @BindView(R.id.checkUpdate)
    LinearLayout checkUpdate;
    @BindView(R.id.invite_friends)
    LinearLayout inviteFriends;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initDada() {
        super.initDada();
        context = this;
        myTopBar.getLeftImageButtonn().setImageResource(R.drawable.back_white_top);
        myTopBar.getLeftImageButtonn().setVisibility(View.VISIBLE);
        myTopBar.getRightButton().setVisibility(View.GONE);
//        myTopBar.getRightImageButtom().setImageResource(R.drawable.back_white_top);
//        myTopBar.getRightImageButtom().setVisibility(View.VISIBLE);
    }

    @Override
    protected void setListener() {
        super.setListener();
        myTopBar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftButtonOnClick() {
                onBackPressed();
            }

            @Override
            public void rightButtonOnClick() {
//                ToastUtils.showToast(context, "正在开发,敬请期待", Toast.LENGTH_SHORT);
            }
        });

        linearAbout.setOnClickListener(this);
        checkUpdate.setOnClickListener(this);
        inviteFriends.setOnClickListener(this);

        sb2G3G.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ToastUtils.showToast(context, "正在完善，敬请期待",Toast.LENGTH_SHORT);
                }else {
                    ToastUtils.showToast(context, "正在完善,敬请期待", Toast.LENGTH_SHORT);
                }
            }
        });

        sbPullMessage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ToastUtils.showToast(context, "正在完善,敬请期待", Toast.LENGTH_SHORT);
                }else {
                    ToastUtils.showToast(context, "正在完善,敬请期待", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linear_about:
                ToastUtils.showToast(this, "正在完善,敬请期待", Toast.LENGTH_SHORT);
                break;
            case R.id.checkUpdate:
                ToastUtils.showToast(this, "正在完善,敬请期待", Toast.LENGTH_SHORT);
                break;
            case R.id.invite_friends:
                Intent share = new Intent(SettingActivity.this, ShareActivity.class);
                startActivity(share);
                break;
            default:
                break;
        }

    }
}
