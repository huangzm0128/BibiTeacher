package pype.mingming.bibiteacher.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.activity.PostInfoActivity;
import pype.mingming.bibiteacher.activity.VisitorActivity;
import pype.mingming.bibiteacher.entity.PostMessage;
import pype.mingming.bibiteacher.utils.TimeUtils;
import pype.mingming.bibiteacher.utils.ToastUtils;

/**
 * Created by wushaohongly on 2016/10/4.
 */

public class PostMassageAdpter extends BaseAdapter{

    Context context;
    List<PostMessage> list;
    LayoutInflater inflater;
    Date date;
    String iconURL = "";

    public PostMassageAdpter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    public PostMassageAdpter(Context context, List<PostMessage> list){
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
            view = inflater.inflate(R.layout.postmassage_item_layout, null);
            viewHolder.icon = (ImageView) view.findViewById(R.id.post_massage_icon);
            viewHolder.username = (TextView) view.findViewById(R.id.post_massage_username);
            viewHolder.time = (TextView) view.findViewById(R.id.post_massage_time);
            viewHolder.content = (TextView) view.findViewById(R.id.post_massage_context);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        PostMessage postMessage = list.get(position);

        if (postMessage.getUser().getAvatar() != null){
            iconURL = postMessage.getUser().getAvatar().getFileUrl(context);
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
        viewHolder.username.setText(postMessage.getUser().getUsername());

        //格式化时间
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
        String strTime = postMessage.getCreatedAt();
        try {
            date =  formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long time = new Date(System.currentTimeMillis()).getTime();
        if(date != null){
            time = date.getTime();
        }
        viewHolder.time.setText(TimeUtils.dataLongToSNS(time));
        viewHolder.content.setText(postMessage.getContext());


        return view;
    }

    public void addList(List<PostMessage> list){
        this.list = list;
        notifyDataSetChanged();
    }

    class ViewHolder{
        public ImageView icon;
        public TextView username;
        public TextView time;
        public TextView content;
    }
}
