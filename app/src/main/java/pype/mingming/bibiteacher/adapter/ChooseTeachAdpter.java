package pype.mingming.bibiteacher.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.activity.VisitorActivity;
import pype.mingming.bibiteacher.entity.Applicants;
import pype.mingming.bibiteacher.entity.Orders;
import pype.mingming.bibiteacher.entity.Post;
import pype.mingming.bibiteacher.entity.PostMessage;
import pype.mingming.bibiteacher.utils.TimeUtils;
import pype.mingming.bibiteacher.utils.ToastUtils;

/**
 * Created by wushaohongly on 2016/10/4.
 */

public class ChooseTeachAdpter extends BaseAdapter{

    Context context;
    List<Applicants> list;
    LayoutInflater inflater;
    String iconURL = "";
    Post post;
    boolean isv;
    private String userId;

    public ChooseTeachAdpter(Context context2, Post post){
        this.context = context2;
        this.post = post;
        inflater = LayoutInflater.from(context);
        this.isv = post.isValue();
        queryOrder();
    }
    public ChooseTeachAdpter(Context context, List<Applicants> list){
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if(list == null){
            return 0;
        }
        else{
            return list.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();

        if (view == null){
            view = inflater.inflate(R.layout.teach_list_layout, null);
            viewHolder.icon = (ImageView) view.findViewById(R.id.choose_teach_list_icon);
            viewHolder.username = (TextView) view.findViewById(R.id.choose_teach_list_username);
            viewHolder.realname = (TextView) view.findViewById(R.id.choose_teach_list_realname);
            viewHolder.phone = (TextView) view.findViewById(R.id.choose_teach_list_phone);
            viewHolder.button = (Button) view.findViewById(R.id.choose_teach_bt);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final Applicants applicants = list.get(position);

        if (applicants.getUser().getAvatar() != null){
            iconURL = applicants.getUser().getAvatar().getFileUrl(context);
            Picasso.with(context)
                    .load(iconURL)
                    .centerCrop()
                    .resize(150, 150)
                    .into(viewHolder.icon);
            iconURL="";
        }else {
            viewHolder.icon.setImageResource(R.mipmap.ic_launcher);
        }


        viewHolder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, VisitorActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", list.get(position).getUser());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        viewHolder.username.setText(applicants.getUser().getUsername());

        String sexStr = "男";
        //if (!applicants.getUser().getSex()){
        //    sexStr = "女";
        //}

        viewHolder.realname.setText(applicants.getUser().getRealName() +" "+sexStr);
        viewHolder.phone.setText(applicants.getUser().getMobilePhoneNumber());
        if (isv){
            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context).setTitle("系统提示")
                            .setMessage("选好了吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Orders orders = new Orders();
                            orders.setPost(post);
                            orders.setEmployeeId(applicants.getUser().getObjectId());
                            orders.save(context, new SaveListener() {
                                @Override
                                public void onSuccess() {
                                    post.setValue(false);
                                    post.update(context, post.getObjectId(), new UpdateListener() {
                                        @Override
                                        public void onSuccess() {
                                            isv = false;
                                            userId = applicants.getUser().getObjectId();
                                            notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onFailure(int i, String s) {

                                        }
                                    });
                                }

                                @Override
                                public void onFailure(int i, String s) {

                                }
                            });
                        }
                    }).setNegativeButton("再看一下", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                }
            });
        } else {
            if (userId.equals(applicants.getUser().getObjectId())){
                viewHolder.button.setText("已雇用");
                viewHolder.button.setEnabled(false);
            } else{
                viewHolder.button.setText("选择");
                viewHolder.button.setEnabled(false);
            }
        }

        return view;
    }

    public void addList(List<Applicants> list){
        this.list = list;
        notifyDataSetChanged();
    }

    class ViewHolder{
        public ImageView icon;
        public TextView username;
        public TextView realname;
        public TextView phone;
        public Button button;
    }

    public void queryOrder(){
        BmobQuery<Orders> bmobQuery = new BmobQuery<Orders>();
        bmobQuery.addWhereEqualTo("post", post);
        bmobQuery.findObjects(context, new FindListener<Orders>() {
            @Override
            public void onSuccess(List<Orders> list) {
                if (list.size()!=0){
                    userId = list.get(0).getEmployeeId();
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
}
