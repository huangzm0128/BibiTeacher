package pype.mingming.bibiteacher.myhomepage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import pype.mingming.bibiteacher.R;

/**
 * Created by mk on 2016/7/28.
 */
public class TopBar extends RelativeLayout {
    private String userTopBarTitle;
    private float titleSize;
    private int titleColor;

    private String leftText;
    private Drawable leftBackgroud;
    private int leftTextColor;

    private String rightText;
    private Drawable rightBackgroud;
    private int rightTextColor;

    private Button leftButton,rightButton;
    private ImageButton leftImageButtonn, rightImageButtom;

    private TextView tvTitle;
    private LayoutParams leftParams,rightParams,titleParams;
    private topbarClickListener listener;
    //创建一个接口
    public interface topbarClickListener{
        public void leftButtonOnClick();
        public void rightButtonOnClick();
    }
    public void setOnTopBarClickListener(topbarClickListener listener){
        this.listener = listener;
    }

    public TopBar(Context context){
        super(context);
    }
    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        //解析TopBar的xml属性-----attar.xml文件的userTopBar属性
        TypedArray ty = context.obtainStyledAttributes(attrs, R.styleable.userTopBar);

        userTopBarTitle = ty.getString(R.styleable.userTopBar_userTopBarTitle);
        titleSize = ty.getDimension(R.styleable.userTopBar_titleSize,0);
        titleColor = ty.getColor(R.styleable.userTopBar_titleColor,0);

        leftText = ty.getString(R.styleable.userTopBar_leftText);
        leftBackgroud = ty.getDrawable(R.styleable.userTopBar_leftBackgroud);
        leftTextColor = ty.getColor(R.styleable.userTopBar_leftTextColor,0);

        rightText = ty.getString(R.styleable.userTopBar_rightText);
        rightBackgroud = ty.getDrawable(R.styleable.userTopBar_rightBackgroud);
        rightTextColor = ty.getColor(R.styleable.userTopBar_rightTextColor,0);
        ty.recycle();

        //将解析获取的属性值赋给各控件
        tvTitle = new TextView(context);
        leftButton = new Button(context);
        rightButton = new Button(context);

        leftImageButtonn = new ImageButton(context);
        leftImageButtonn.setBackgroundColor(Color.TRANSPARENT);
        leftImageButtonn.setVisibility(GONE);

        rightImageButtom = new ImageButton(context);
        rightImageButtom.setBackgroundColor(Color.TRANSPARENT);
        rightImageButtom.setVisibility(GONE);

        leftButton.setTextColor(leftTextColor);
        leftButton.setText(leftText);
        leftButton.setTextSize(18);
        leftButton.setBackground(leftBackgroud);

        rightButton.setTextColor(rightTextColor);
        rightButton.setText(rightText);
        rightButton.setTextSize(18);
        rightButton.setBackground(rightBackgroud);

        tvTitle.setText(userTopBarTitle);
        tvTitle.setTextColor(titleColor);
        tvTitle.setTextSize(titleSize);
        tvTitle.setGravity(Gravity.CENTER);
        setBackgroundColor(getResources().getColor(R.color.topBarBG));

        leftParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //margin
        leftParams.leftMargin = 20;
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);//增加规则，向左对齐
        addView(leftImageButtonn,leftParams);
        addView(leftButton);

        rightParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,TRUE);//增加规则，向右对齐

        addView(rightButton,rightParams);
        addView(rightImageButtom,rightParams);

        titleParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT,TRUE);

        addView(tvTitle,titleParams);

        //接口的回调机制
        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.leftButtonOnClick();
            }
        });

        leftImageButtonn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.leftButtonOnClick();
            }
        });

        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.rightButtonOnClick();
            }
        });

        rightImageButtom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.rightButtonOnClick();
            }
        });

    }

    public Button getLeftButton() {
        return leftButton;
    }

    public void setTvTitle(String title){
        this.tvTitle.setText(title);
    }

    public Button getRightButton() {
        return rightButton;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public ImageButton getLeftImageButtonn() {
        return leftImageButtonn;
    }
    public ImageButton getRightImageButtom() {
        return rightImageButtom;
    }
}
