package pype.mingming.bibiteacher.entity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.ui.CircleImageView;

/**
 * Created by mk on 2016/9/24.
 */
public class PluralistAdapter extends BaseAdapter {
    private Context context = null;
    private PluralistBmob pluralistBmob;
    private List<PluralistBmob> pluralistBmobList = new ArrayList<PluralistBmob>();
    private listViewData[] listViewDatas = new listViewData[10];
    //获取对象
    /*public PluralistAdapter(Context context,PluralistBmob pluralistBmob){
        this.context = context;
        this.pluralistBmob = pluralistBmob;
    }*/
    public PluralistAdapter(Context context,List<PluralistBmob> pluralistBmobList){
        this.context = context;
        this.pluralistBmobList = pluralistBmobList;
        init();
    }
    public Context getContext(){
        return context;
    }
    public PluralistBmob getPluralistBmob(){
        return pluralistBmob;
    }
    public void init(){

    }
    //返回长度
    @Override
    public int getCount() {
        return pluralistBmobList.size();
    }

    @Override
    public PluralistBmob getItem(int position) {
        return pluralistBmobList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout li = null;
        if(convertView!=null){
            li =(LinearLayout)convertView;
        }else {
            li = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.pluralist_linearlyuot,null);
        }
        PluralistBmob bmob = getItem(position);
        CircleImageView circleImageView = (CircleImageView) li.findViewById(R.id.imgPluralist);
        TextView tvPluralistName = (TextView) li.findViewById(R.id.tvPluralistName);
        TextView tvPluralistSex = (TextView) li.findViewById(R.id.tvPluralistSex);
        TextView tvPluralistUniversity = (TextView) li.findViewById(R.id.tvPluralistUniversity);
        TextView tvPluralistSubject = (TextView) li.findViewById(R.id.tvPluralistSubject);
        RatingBar rbPluralist = (RatingBar) li.findViewById(R.id.rbPluralist);
        TextView tvPluralistAge = (TextView) li.findViewById(R.id.tvPluralistAge);
        if (bmob.getUser().getAvatar()!=null){
            String avatorUri = bmob.getUser().getAvatar().getFileUrl(context);
            Picasso.with(context)
                    .load(avatorUri)
                    .into(circleImageView);
        }else {
            circleImageView.setImageResource(R.drawable.pluralist_image);

        }
        tvPluralistName.setText(bmob.getpUserName());
        tvPluralistSex.setText(bmob.getpSex());
        tvPluralistUniversity.setText(bmob.getpUniversity());
        tvPluralistAge.setText("教龄："+(bmob.getpTeacherAge()+0));
        tvPluralistSubject.setText(bmob.getpSubject());
        rbPluralist.setRating(3);
        return li;
    }
}
