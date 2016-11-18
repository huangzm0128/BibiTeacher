package pype.mingming.bibiteacher.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.entity.Post;
import pype.mingming.bibiteacher.utils.TimeUtils;

/**
 * Created by wushaohongly on 2016/9/25.
 */

public class PostAdapter extends RecyclerView.Adapter {

    private List<Post> list;
    private ItemClickListener listener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_layout, null), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Date date = null;

        Post i = list.get(position);

        String subject = i.getSubject();
        switch (subject){
            case "语文" :
                viewHolder.getTeach_type_iv().setImageResource(R.mipmap.icon_home_yuwen);
                break;
            case "数学" :
                viewHolder.getTeach_type_iv().setImageResource(R.mipmap.icon_home_shuxue);
                break;
            case "英语" :
                viewHolder.getTeach_type_iv().setImageResource(R.mipmap.icon_home_yingyu);
                break;
            case "政治" :
                viewHolder.getTeach_type_iv().setImageResource(R.mipmap.icon_home_zhengzhi);
                break;
            case "历史" :
                viewHolder.getTeach_type_iv().setImageResource(R.mipmap.icon_home_lishi);
                break;
            case "地理" :
                viewHolder.getTeach_type_iv().setImageResource(R.mipmap.icon_home_dili);
                break;
            case "物理" :
                viewHolder.getTeach_type_iv().setImageResource(R.mipmap.icon_home_wuli);
                break;
            case "化学" :
                viewHolder.getTeach_type_iv().setImageResource(R.mipmap.icon_home_huaxue);
                break;
            case "生物" :
                viewHolder.getTeach_type_iv().setImageResource(R.mipmap.icon_home_shengwul);
                break;
            default:
                viewHolder.getTeach_type_iv().setImageResource(R.mipmap.icon_home_gengduo);
                break;
        }

        viewHolder.getTeach_title_tv().setText(i.getPostTitle());

        //格式化时间
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd");
        long time = new Date(System.currentTimeMillis()).getTime();
        String stime = i.getStartTime().getDate();
        String etime = i.getEndTime().getDate();

        try {
            date =  formatter.parse(stime);
            if(date != null){
                time = date.getTime();
            }
            stime = TimeUtils.dateLongToString(time, "MM/dd");

            date =  formatter.parse(etime);
            if(date != null){
                time = date.getTime();
            }
            etime = TimeUtils.dateLongToString(time, "MM/dd");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //显示时间
        viewHolder.getTeach_time_tv().setText(stime +" 至 " +etime);
        viewHolder.getTeach_address_tv().setText(i.getTeachAddress());

        String priceStr = "¥"+i.getPrice()+"/天";
        SpannableStringBuilder style = new SpannableStringBuilder(priceStr);
        style.setSpan(new ForegroundColorSpan(Color.RED), 1, priceStr.length()-2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        style.setSpan(new AbsoluteSizeSpan(50), 1, priceStr.length()-2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        viewHolder.getTeach_price_tv().setText(style);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void add(List<Post> list){
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void addALL(List<Post> list){
        this.list = list;
        notifyDataSetChanged();
    }

    //数据列表list的set和get方法
    public List<Post> getList() {
        return list;
    }
    public void setList(List<Post> list) {
        this.list = list;
    }

    public void setOnItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ItemClickListener listener;

        private ImageView teach_type_iv;
        private TextView teach_title_tv;
        private TextView teach_time_tv;
        private TextView teach_address_tv;
        private TextView teach_price_tv;

        public ViewHolder(View itemView, ItemClickListener listener) {
            super(itemView);
            this.listener = listener;

            teach_type_iv = (ImageView) itemView.findViewById(R.id.teach_type_iv);
            teach_title_tv = (TextView) itemView.findViewById(R.id.teach_title_tv);
            teach_time_tv = (TextView) itemView.findViewById(R.id.teach_time_tv);
            teach_address_tv = (TextView) itemView.findViewById(R.id.teach_address_tv);
            teach_price_tv = (TextView) itemView.findViewById(R.id.teach_price_tv);

            itemView.setOnClickListener(this);

        }

        public ImageView getTeach_type_iv() {
            return teach_type_iv;
        }

        public void setTeach_type_iv(ImageView teach_type_iv) {
            this.teach_type_iv = teach_type_iv;
        }

        public TextView getTeach_title_tv() {
            return teach_title_tv;
        }

        public void setTeach_title_tv(TextView teach_title_tv) {
            this.teach_title_tv = teach_title_tv;
        }

        public TextView getTeach_time_tv() {
            return teach_time_tv;
        }

        public void setTeach_time_tv(TextView teach_time_tv) {
            this.teach_time_tv = teach_time_tv;
        }

        public TextView getTeach_address_tv() {
            return teach_address_tv;
        }

        public void setTeach_address_tv(TextView teach_address_tv) {
            this.teach_address_tv = teach_address_tv;
        }

        public TextView getTeach_price_tv() {
            return teach_price_tv;
        }

        public void setTeach_price_tv(TextView teach_price_tv) {
            this.teach_price_tv = teach_price_tv;
        }

        @Override
        public void onClick(View v) {
            if(listener != null){
                listener.onItemClick(v, this.getPosition());
            }
        }
    }
}
