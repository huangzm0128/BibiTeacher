package pype.mingming.bibiteacher.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.entity.UnOrderBmob;
import pype.mingming.bibiteacher.entity.User;

/**
 * Created by mk on 2016/10/12.
 */
public class MKOrderAdapter extends BaseAdapter{
    private Context context = null;
    private List<UnOrderBmob> UnOrderBmobList = new ArrayList<UnOrderBmob>();
    private User user = null;
    public MKOrderAdapter(Context context,List<UnOrderBmob> UnOrderBmobList,User user){
        this.context = context;
        this.UnOrderBmobList = UnOrderBmobList;
        this.user = user;
    }
    public Context getContext(){
        return context;
    }
    @Override
    public int getCount() {
        return UnOrderBmobList.size();
    }

    @Override
    public UnOrderBmob getItem(int position) {
        return UnOrderBmobList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout li = null;
        if (convertView!=null){
            li = (LinearLayout) convertView;
        }else {
            li = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.mkorder_adapter,null);
        }
        ImageView imageView = (ImageView) li.findViewById(R.id.imgOrder);
        TextView tvEmploy = (TextView) li.findViewById(R.id.tvEmploy);
        TextView tvMkorderUser = (TextView) li.findViewById(R.id.tvMkorderUser);
        TextView tvMkorderWriter = (TextView) li.findViewById(R.id.tvMkorderWriter);
        TextView tvTime = (TextView) li.findViewById(R.id.tvTime);
        ImageView imgYesNo = (ImageView) li.findViewById(R.id.imgYesNo);

        UnOrderBmob unOrderBmob = getItem(position);
        Log.e("test",unOrderBmob.getEmployer().getUsername().equals(user.getUsername())+"");
        if (unOrderBmob.getEmployer().getUsername().equals(user.getUsername())){
            BmobFile imageFile = unOrderBmob.getEmployee().getAvatar();
            if(imageFile != null){
                String avatorUri = unOrderBmob.getEmployee().getAvatar().getFileUrl(context);
                Picasso.with(context)
                        .load(avatorUri)
                        .into(imageView);
            }
            tvEmploy.setText("大学生：");
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
            tvMkorderUser.setText(unOrderBmob.getEmployee().getUsername()+" "+"年龄："+userYear+"   "+sex);
            tvMkorderWriter.setText(unOrderBmob.getEvent());
            tvTime.setText(unOrderBmob.getCreatedAt());
            if (unOrderBmob.getCheck()){
                imgYesNo.setImageResource(R.mipmap.yes);
            }
        }else {
            BmobFile imageFile = unOrderBmob.getEmployer().getAvatar();
            if(imageFile != null){
                String avatorUri = unOrderBmob.getEmployer().getAvatar().getFileUrl(context);
                Picasso.with(context)
                        .load(avatorUri)
                        .into(imageView);
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
            tvMkorderUser.setText(unOrderBmob.getEmployer().getUsername()+" "+"年龄："+userYear+"   "+sex);
            tvMkorderWriter.setText(unOrderBmob.getEvent());
            tvTime.setText(unOrderBmob.getCreatedAt());
            if (unOrderBmob.getCheck()){
                imgYesNo.setImageResource(R.mipmap.yes);
            }
        }
        return li;
    }
}
