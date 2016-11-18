package pype.mingming.bibiteacher.activity;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.entity.User;
import pype.mingming.bibiteacher.ui.CircleImageView;

/**
 * Created by wushaohongly on 2016/10/16.
 */

public class VisitorActivity extends BaseActivity {

    @BindView(R.id.infor_icon)
    CircleImageView icon;
    @BindView(R.id.infor_username)
    TextView username;
    @BindView(R.id.infor_sex)
    TextView sex;
    @BindView(R.id.infor_qianming)
    TextView qianming;
    @BindView(R.id.infor_buliang)
    TextView buliang;
    @BindView(R.id.infor_huoyue)
    TextView huoyue;
    @BindView(R.id.infor_address)
    TextView address;

    @BindView(R.id.bottom_img1)
    ImageView img1;
    @BindView(R.id.bottom_img2)
    ImageView img2;
    @BindView(R.id.bottom_img3)
    ImageView img3;

    private User user;

    @Override
    protected int getLayoutId() {
        return R.layout.information;
    }

    @Override
    protected void initDada() {
        super.initDada();
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        String iconURL = user.getAvatar().getUrl();
        Picasso.with(context)
                .load(iconURL)
                .centerCrop()
                .resize(150, 150)
                .into(icon);

        username.setText(user.getUsername()+"");
        sex.setText(user.getSex()? "男":"女");
        qianming.setText(user.getSignature()+"");
        buliang.setText(user.getBadReport()+"");
        huoyue.setText("0");
        address.setText(user.getAddress()+"");
    }
}
