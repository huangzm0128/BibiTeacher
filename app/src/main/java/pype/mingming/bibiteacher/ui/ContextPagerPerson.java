package pype.mingming.bibiteacher.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.c.V;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.activity.AtyMkOrderNew;
import pype.mingming.bibiteacher.activity.CompleteInformation;
import pype.mingming.bibiteacher.activity.LoginActivity;
import pype.mingming.bibiteacher.activity.MainActivity;
import pype.mingming.bibiteacher.activity.MyPostActivity;
import pype.mingming.bibiteacher.activity.OrdersActivity;
import pype.mingming.bibiteacher.activity.SettingActivity;
import pype.mingming.bibiteacher.activity.ShareActivity;
import pype.mingming.bibiteacher.activity.UserActivity;
import pype.mingming.bibiteacher.entity.MyDate;
import pype.mingming.bibiteacher.entity.Orders;
import pype.mingming.bibiteacher.entity.User;
import pype.mingming.bibiteacher.utils.CalendarUtil;
import pype.mingming.bibiteacher.utils.LogUtils;
import pype.mingming.bibiteacher.utils.ToastUtils;

import static android.app.Activity.RESULT_OK;

/**
 * Created by mingming on 2016/7/27.
 */
public class ContextPagerPerson extends Fragment {
    @BindView(R.id.index_person_list1_avatar)
    CircleImageView indexPersonList1Avatar;
    @BindView(R.id.index_person_list1_login)
    TextView indexPersonList1Login;
    @BindView(R.id.index_person_list1_address)
    TextView indexPersonList1Address;
    @BindView(R.id.index_person_list1_right)
    ImageView indexPersonList1Right;
    @BindView(R.id.post_point)
    ImageView postPoint;
    @BindView(R.id.index_person_list1_my_post)
    LinearLayout indexPersonList1MyPost;
    @BindView(R.id.favorite_point)
    ImageView favoritePoint;
    @BindView(R.id.index_person_list1_my_favorite)
    LinearLayout indexPersonList1MyFavorite;
    @BindView(R.id.bill_point)
    ImageView billPoint;
    @BindView(R.id.index_person_list1_my_bill)
    LinearLayout indexPersonList1MyBill;
    @BindView(R.id.index_person_curriculum)
    LinearLayout indexPersonCurriculum;
    @BindView(R.id.index_person_custom_teacher)
    LinearLayout indexPersonCustomTeacher;
    @BindView(R.id.index_person_student)
    LinearLayout indexPersonStudent;
    @BindView(R.id.index_person_message)
    LinearLayout indexPersonMessage;
    @BindView(R.id.index_person_invite)
    LinearLayout indexPersonInvite;
    @BindView(R.id.index_person_nearby_recommend)
    LinearLayout indexPersonNearbyRecommend;
    @BindView(R.id.index_person_recent_browse)
    LinearLayout indexPersonRecentBrowse;
    @BindView(R.id.index_person_setting)
    LinearLayout indexPersonSetting;
    @BindView(R.id.index_person_custom_teacher_line)
    View indexPersonCustomTeacherLine;
    @BindView(R.id.index_person_student_line)
    View indexPersonStudentLine;
    @BindView(R.id.index_person_list1_my_post_tv)
    TextView indexPersonList1MyPostTv;

    private User user;
    private Context context;
    private CalendarUtil calendarUtil;
    private Calendar calendar;
    private List<MyDate> selectedDays; //（用户未来有课程的日期）

    List<Orders> myList;

    public ContextPagerPerson() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
        }
        View view = inflater.inflate(R.layout.content_main_pagers_person, container, false);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void initData() {
        context = getContext();
        user = BmobUser.getCurrentUser(context, User.class);
        calendarUtil = new CalendarUtil();
        calendar = Calendar.getInstance();
//        updateUserPerson();

    }


    @OnClick({R.id.index_person_list1_avatar, R.id.index_person_list1_login, R.id.index_person_list1_right, R.id.index_person_list1_my_post,
            R.id.index_person_list1_my_favorite, R.id.index_person_list1_my_bill, R.id.index_person_curriculum,
            R.id.index_person_custom_teacher, R.id.index_person_student, R.id.index_person_message, R.id.index_person_invite,
            R.id.index_person_nearby_recommend, R.id.index_person_recent_browse, R.id.index_person_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.index_person_list1_avatar:

            case R.id.index_person_list1_login:

            case R.id.index_person_list1_right:
                if(isLogin()){
                    Intent intent = new Intent(getActivity(), UserActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.index_person_list1_my_post:
                if (isLogin()){
                    if(user.getIsStudent() != null){
                        if (user.getIsStudent()){
                            Intent i = new Intent(getActivity(),AtyMkOrderNew.class);
                            i.putExtra("user",user);
                            startActivity(i);
                        }else {
                            Intent intent = new Intent(getActivity(), MyPostActivity.class);
                            startActivity(intent);
                        }
                    }else {
                        new AlertDialog.Builder(getActivity()).setTitle("系统提示")
                                .setMessage("请完善个人信息").setPositiveButton("完善个人信息", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getActivity(), CompleteInformation.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton("我知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("info", "我知道了");
                            }
                        }).show();
                    }
                }else {
                    ToastUtils.showToast(context, "请先登录",Toast.LENGTH_SHORT);
                }
                break;
            case R.id.index_person_list1_my_favorite:
                ToastUtils.showToast(context, "正在完善，敬请期待",Toast.LENGTH_SHORT);
                break;
            case R.id.index_person_list1_my_bill:
                if(isLogin()){
                    if(user.getIsStudent() != null){
                        Intent intent = new Intent(getActivity(), OrdersActivity.class);
                        billPoint.setVisibility(View.GONE);
                        if(myList != null){
                            for(int i=0; i<myList.size(); i++){
                                myList.get(i).setIsRead(true);
                                myList.get(i).update(context, new UpdateListener() {
                                    @Override
                                    public void onSuccess() {
                                        LogUtils.i("1111","order.isRead");
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {

                                    }
                                });
                            }
                        }
                        startActivity(intent);
                    }else {
                        new AlertDialog.Builder(getActivity()).setTitle("系统提示")
                                .setMessage("请完善个人信息").setPositiveButton("完善个人信息", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getActivity(), CompleteInformation.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton("我知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("info", "我知道了");
                            }
                        }).show();
                    }

                }else {
                    ToastUtils.showToast(context, "请先登录",Toast.LENGTH_SHORT);
                }
                break;
            case R.id.index_person_curriculum:

                if(isLogin()){

                    calendarUtil.getCalendarDialog(getActivity(), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.DAY_OF_MONTH), MCalendarView.SlideType.Horizontal,selectedDays,
                            new MCalendarDialog.CalendarDialogListener() {
                                @Override
                                public void calendarDialogListener(int year, int month, int day) {
//
                                }
                            });
                }else {
                    ToastUtils.showToast(context,"请登录",Toast.LENGTH_SHORT);
                }
                break;

            case R.id.index_person_custom_teacher:
                ToastUtils.showToast(context, "正在完善，敬请期待",Toast.LENGTH_SHORT);
                break;
            case R.id.index_person_student:
                ToastUtils.showToast(context, "正在完善，敬请期待",Toast.LENGTH_SHORT);
                break;
            case R.id.index_person_message:
                ToastUtils.showToast(context, "正在完善，敬请期待",Toast.LENGTH_SHORT);
                break;
            case R.id.index_person_invite:
                Intent share = new Intent(getActivity(), ShareActivity.class);
                startActivity(share);
                break;
            case R.id.index_person_nearby_recommend:
                ToastUtils.showToast(context, "正在完善，敬请期待",Toast.LENGTH_SHORT);
                break;
            case R.id.index_person_recent_browse:
                ToastUtils.showToast(context, "正在完善，敬请期待",Toast.LENGTH_SHORT);
                break;
            case R.id.index_person_setting:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void getMyDate() {

        selectedDays = new ArrayList<>();

        //bmob查询
        BmobQuery<Orders> query = new BmobQuery<Orders>();
        if(user.getIsStudent() != null){

            query.order("-createdAt");  //时间倒序查询
            query.include("post,unOrderBmob");
            query.findObjects(context, new FindListener<Orders>() {

                @Override
                public void onSuccess(List<Orders> list) {
                    Log.i("1111", "查询成功");

                    for(int i=0; i<list.size(); i++){
                        if(list.get(i).getUnOrderBmob() != null &&
                                list.get(i).getUnOrderBmob().getEmployee().getObjectId().equals(user.getObjectId()) ||
                                list.get(i).getUnOrderBmob() != null &&
                                        list.get(i).getUnOrderBmob().getEmployer().getObjectId().equals(user.getObjectId()) ||
                                list.get(i).getPost()!= null &&
                                        list.get(i).getPost().getPostUser().getObjectId().equals(user.getObjectId()) ||
                                list.get(i).getEmployeeId() != null && list.get(i).getEmployeeId().equals(user.getObjectId())){


                            if(list.get(i).getUnOrderBmob() != null){
                                addTime(list.get(i).getUnOrderBmob().getTime().substring(0,10), list.get(i).getUnOrderBmob().getTime().substring(16,26));
                            }else {
                                addTime(list.get(i).getPost().getStartTime().getDate(), list.get(i).getPost().getEndTime().getDate());

                            }
                        }
                    }
                }

                @Override
                public void onError(int i, String s) {

                }
            });
        }

    }

    /**
     * 将家教结束时间大于或等于现在的天数添加到课表
     * 若家教开始时间大于现在的时间，即将开始到结束的天数都添加到课表里
     *
     * @param startDate 家教开始时间
     * @param endDate 家教结束时间
     */
    private void addTime(String startDate, String endDate){
        int sYear,sMonth,sDay,eYear,eMonth,eDay;
        //取得系统日期

        int nYear = calendar.get(Calendar.YEAR);
        int nMonth = calendar.get(Calendar.MONTH);
        int nDay = calendar.get(Calendar.DAY_OF_MONTH);

        //家教开始日期
        sYear = Integer.parseInt(startDate.substring(0,4));
        sMonth = Integer.parseInt(startDate.substring(5,7));
        sDay= Integer.parseInt(startDate.substring(8,10));

        //家教结束日期
        eYear = Integer.parseInt(endDate.substring(0,4));
        eMonth = Integer.parseInt(endDate.substring(5,7));
        eDay= Integer.parseInt(endDate.substring(8,10));

        LogUtils.i("1111","年份："+ sYear);
        LogUtils.i("1111","月份："+ sMonth);
        LogUtils.i("1111","天："+ sDay);

        if(sYear >= nYear && sMonth >= nMonth && sDay >= nDay){  //家教开始时间大于现在的时间，即将开始到结束的天数都添加到课表里
            for(int i=0; i<=eDay-sDay; i++){
                selectedDays.add(new MyDate(sYear,sMonth,sDay+i));
            }
        }else if(eYear >= nYear && eMonth >= nMonth && eDay >= nDay){ //若家教结束时间大于或等于现在的天数添加到课表
            for(int i=0; i<=eDay-nDay; i++){
                selectedDays.add(new MyDate(sYear,sMonth,nDay+i));
            }
        }

    }


    /**
     * 判断是否已经登录
     * @return
     */
    private boolean isLogin() {
        user = BmobUser.getCurrentUser(context, User.class);
        if (user != null) {
            return true;
        }
        return false;
    }
    private void updateUserPerson(){
        //将订单指示点状态设置为默认
        billPoint.setVisibility(View.GONE);
        if(isLogin()){
            //当活动不可见时清空课表
            selectedDays = null;
            //获取家教时间
            getMyDate();
            //查询是否有订单为阅读过
            BmobQuery();

            if(user.getAvatar() != null){
                String avatar = user.getAvatar().getFileUrl(context);
                Picasso.with(context)
                        .load(avatar)
                        .into(indexPersonList1Avatar);
            }
            indexPersonList1Login.setText(user.getUsername());
            LogUtils.i("1111", "用户地址" +user.getAddress());
            indexPersonList1Address.setText(user.getAddress());

            if(user.getIsStudent()!= null && user.getIsStudent()){
                indexPersonStudent.setVisibility(View.VISIBLE);
                indexPersonStudentLine.setVisibility(View.VISIBLE);
                indexPersonCustomTeacher.setVisibility(View.GONE);
                indexPersonCustomTeacherLine.setVisibility(View.GONE);
                indexPersonList1MyPostTv.setText("我的应聘");
            }

        }else {
            indexPersonList1Avatar.setImageResource(R.mipmap.default_user_avatar);
            indexPersonList1Login.setText("点击登录");
            indexPersonList1Address.setText("");
            indexPersonStudent.setVisibility(View.GONE);
            indexPersonStudentLine.setVisibility(View.GONE);
            indexPersonCustomTeacher.setVisibility(View.VISIBLE);
            indexPersonCustomTeacherLine.setVisibility(View.VISIBLE);
            indexPersonList1MyPostTv.setText("我的发帖");
        }

    }

    //查询当前用户的订单是否有未阅读过的
    private void BmobQuery() {
        //bmob查询
        BmobQuery<Orders> query = new BmobQuery<Orders>();
        query.include("unOrderBmob.employee[objectId],unOrderBmob.employer[objectId],post.postUser[objectId]");
        if(user.getIsStudent() != null){
            query.order("-createdAt");  //时间倒序查询
            query.findObjects(context, new FindListener<Orders>() {

                @Override
                public void onSuccess(List<Orders> list) {
                    Log.i("info", "查询成功");
                    if (list.size() != 0) {
                        myList = new ArrayList<Orders>();
                        for(int i=0; i<list.size(); i++){
                            if(list.get(i).getUnOrderBmob() != null &&
                                    list.get(i).getUnOrderBmob().getEmployee().getObjectId().equals(user.getObjectId()) ||
                                    list.get(i).getUnOrderBmob() != null &&
                                            list.get(i).getUnOrderBmob().getEmployer().getObjectId().equals(user.getObjectId()) ||
                                    list.get(i).getPost()!= null &&
                                            list.get(i).getPost().getPostUser().getObjectId().equals(user.getObjectId()) ||
                                    list.get(i).getEmployeeId() != null && list.get(i).getEmployeeId().equals(user.getObjectId())){
                               if( !list.get(i).getIsRead()){
                                   billPoint.setVisibility(View.VISIBLE);
                                   myList.add(list.get(i));
                               }
                            }
                        }

                    } else {

                    }
                }

                @Override
                public void onError(int i, String s) {

                }
            });
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        updateUserPerson();
//        //将订单指示点状态设置为默认
//        billPoint.setVisibility(View.GONE);
//        if(isLogin()){
//            //查询是否有订单为阅读过
//            BmobQuery();
//        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }


}
