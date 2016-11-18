package pype.mingming.bibiteacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.adapter.ItemClickListener;
import pype.mingming.bibiteacher.adapter.PostAdapter;
import pype.mingming.bibiteacher.entity.Post;
import pype.mingming.bibiteacher.entity.User;
import pype.mingming.bibiteacher.myhomepage.TopBar;
import pype.mingming.bibiteacher.ui.SpaceItemDecoration;

/**
 * Created by wushaohongly on 2016/10/13.
 */

public class MyPostActivity extends BaseActivity implements ItemClickListener{

    @BindView(R.id.mypost_recyclerview)
    RecyclerView mypost_list;

    @BindView(R.id.mypost_topBar)
    TopBar topBar;

    private PostAdapter adapter;
    private User user;

    @Override
    protected int getLayoutId() {
        return R.layout.mypostlayout;
    }

    @Override
    protected void initDada() {
        super.initDada();

        initToolbar();
        mypost_list.setLayoutManager(new LinearLayoutManager(this));
        mypost_list.addItemDecoration(new SpaceItemDecoration(5));

        adapter = new PostAdapter();
        adapter.setOnItemClickListener(this);
        user = BmobUser.getCurrentUser(this, User.class);

        BmobQuery<Post> bmobQuery = new BmobQuery<Post>();
        bmobQuery.addWhereEqualTo("postUser", user);
        bmobQuery.addWhereEqualTo("isDelete", false);
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(this, new FindListener<Post>() {
            @Override
            public void onSuccess(List<Post> list) {
                adapter.addALL(list);
                mypost_list.setAdapter(adapter);
            }

            @Override
            public void onError(int i, String s) {
                Log.i("info", "我的帖子失败");
            }
        });

    }

    /**
     * 设置标题栏
     */
    private void initToolbar() {
        topBar.getLeftImageButtonn().setImageResource(R.drawable.back_white_top);
        topBar.getLeftImageButtonn().setVisibility(View.VISIBLE);
        topBar.getRightButton().setVisibility(View.GONE);
        topBar.getRightImageButtom().setVisibility(View.GONE);
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
            public void rightButtonOnClick() {}
        });
    }

    @Override
    public void onItemClick(View view, int postion) {
        Post post = adapter.getList().get(postion);
        Intent intent = new Intent(MyPostActivity.this, MySnapActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("post", post);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        BmobQuery<Post> bmobQuery = new BmobQuery<Post>();
        bmobQuery.addWhereEqualTo("postUser", user);
        bmobQuery.addWhereEqualTo("isDelete", false);
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(this, new FindListener<Post>() {
            @Override
            public void onSuccess(List<Post> list) {
                adapter.addALL(list);
                mypost_list.setAdapter(adapter);
            }

            @Override
            public void onError(int i, String s) {
                Log.i("info", "我的帖子失败");
            }
        });
    }
}
