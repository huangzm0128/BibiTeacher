package pype.mingming.bibiteacher.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.entity.HeadPager;
import pype.mingming.bibiteacher.myhomepage.TopBar;
import pype.mingming.bibiteacher.utils.LogUtils;
import pype.mingming.bibiteacher.utils.ToastUtils;

/**
 * Created by mingming on 2016/10/14.
 */

public class HeadPageAty extends BaseActivity {
    @BindView(R.id.myTopBar)
    TopBar myTopBar;
    @BindView(R.id.headpage_content_title)
    TextView headpageContentTitle;
    @BindView(R.id.headpage_content_pic)
    ImageView headpageContentPic;
    @BindView(R.id.headpage_content_text)
    TextView headpageContentText;

    private String id; //页码

    @Override
    protected int getLayoutId() {
        return R.layout.activity_headpage;
    }

    @Override
    protected void initDada() {
        super.initDada();
        myTopBar.getLeftImageButtonn().setImageResource(R.drawable.back_white_top);
        myTopBar.getLeftImageButtonn().setVisibility(View.VISIBLE);
        myTopBar.getRightButton().setVisibility(View.GONE);

        bmobQuery();
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

            }
        });
    }

    private void bmobQuery() {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        LogUtils.i("0000", "id:" + id);
        BmobQuery<HeadPager> query = new BmobQuery<HeadPager>();
        query.getObject(this, id, new GetListener<HeadPager>() {

            @Override
            public void onSuccess(HeadPager object) {
                myTopBar.setTvTitle(object.getTopTitle());
                headpageContentTitle.setText(object.getContentTitle());
                headpageContentText.setText(object.getContent());
                Picasso.with(context)
                        .load(object.getContentPicture().getFileUrl(context))
                        .into(headpageContentPic);

            }

            @Override
            public void onFailure(int code, String arg0) {
                ToastUtils.showToast(context, "获取失败", Toast.LENGTH_SHORT);
                LogUtils.i("0000", code + arg0);
            }

        });
    }
}
