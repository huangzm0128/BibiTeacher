package pype.mingming.bibiteacher.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.listener.UpdateListener;
import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.entity.User;
import pype.mingming.bibiteacher.myhomepage.TopBar;
import pype.mingming.bibiteacher.utils.CustomProgressDialog;
import pype.mingming.bibiteacher.utils.ToastUtils;

/**
 * 完善信息页面
 * Created by mingming on 2016/10/7.
 */

public class CompleteInformation extends BaseActivity {

    @BindView(R.id.myTopBar)
    TopBar myTopBar;
    @BindView(R.id.user_parents)
    TextView userParents;
    @BindView(R.id.user_student)
    TextView userStudent;
    @BindView(R.id.complete_user_phone)
    EditText completeUserPhone;
    @BindView(R.id.complete_button)
    Button completeButton;
    @BindView(R.id.complete_user_realName)
    EditText completeUserRealName;

    private boolean isStudent = false;
    private User user;
    CustomProgressDialog dialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_complete_information;
    }

    @Override
    protected void initDada() {
        myTopBar.getLeftImageButtonn().setImageResource(R.drawable.back_white_top);
        myTopBar.getLeftImageButtonn().setVisibility(View.VISIBLE);
        myTopBar.getRightButton().setVisibility(View.GONE);
        user = User.getCurrentUser(context, User.class);
        dialog = new CustomProgressDialog(CompleteInformation.this, "网络君正在奔跑..", R.drawable.myprogressframe);
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
                //无
            }
        });

        completeUserPhone.addTextChangedListener(new MyTextWatcher(completeButton));
    }

    @OnClick({R.id.user_parents, R.id.user_student, R.id.complete_button, R.id.complete_user_realName})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_parents:
                userParents.setBackgroundResource(R.drawable.login_select_user_bg);
                userParents.setTextColor(getResources().getColor(R.color.default_tab_view_selected_color));
                userStudent.setBackgroundResource(R.drawable.login_other_login_bg);
                userStudent.setTextColor(getResources().getColor(R.color.white));
                isStudent = false;
                break;
            case R.id.user_student:
                userStudent.setBackgroundResource(R.drawable.login_select_user_bg);
                userStudent.setTextColor(getResources().getColor(R.color.default_tab_view_selected_color));
                userParents.setBackgroundResource(R.drawable.login_other_login_bg);
                userParents.setTextColor(getResources().getColor(R.color.white));
                isStudent = true;
                break;

            case R.id.complete_button:
                if (completeUserPhone.getText() != null && completeUserRealName.getText() != null) {
                    dialog.show();
                    user.setMobilePhoneNumber(completeUserPhone.getText().toString());
                    user.setRealName(completeUserRealName.getText().toString());
                    user.setIsStudent(isStudent);
                    user.update(context, user.getObjectId(), new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            dialog.dismiss();
                            onBackPressed();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            dialog.dismiss();
                            ToastUtils.showToast(context, s, Toast.LENGTH_SHORT);
                        }
                    });
//                    user.update(context, new UpdateListener() {
//                        @Override
//                        public void onSuccess() {
//                            dialog.dismiss();
//                            onBackPressed();
//                        }
//
//                        @Override
//                        public void onFailure(int i, String s) {
//                            dialog.dismiss();
//                            ToastUtils.showToast(context, s, Toast.LENGTH_SHORT);
//                        }
//                    });

                }
                break;
        }
    }


    //动态监听输入过程
    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (completeUserPhone.getText().length() == 11 && completeUserRealName.getText() != null) {
                completeButton.setEnabled(true);
                completeButton.setAlpha(1.0f);
            } else {
                completeButton.setEnabled(false);
                completeButton.setAlpha(0.3f);
            }
        }
    }
}
