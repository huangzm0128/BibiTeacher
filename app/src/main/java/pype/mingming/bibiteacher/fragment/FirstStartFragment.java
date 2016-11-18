package pype.mingming.bibiteacher.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import pype.mingming.bibiteacher.R;
import pype.mingming.bibiteacher.activity.LoginActivity;
import pype.mingming.bibiteacher.activity.MainActivity;

/**
 * 第一次启动软件是滚动页面的fragment
 * Created by mingming on 2016/8/1.
 */
public class FirstStartFragment extends Fragment {
    final static String LAYOYT_ID = "layoutId";
    final int DEFAULT_VALUE = -1;


    public static FirstStartFragment newInstance(int layoutId){
        FirstStartFragment pane = new FirstStartFragment();
        Bundle args = new Bundle();
        args.putInt(LAYOYT_ID, layoutId);
        pane.setArguments(args);
        return pane;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(getArguments().getInt(LAYOYT_ID, DEFAULT_VALUE),container, false);
        //找到定义的button
        Button start = (Button) rootView.findViewById(R.id.startMainActivity);
//        Button skip = (Button) rootView.findViewById(R.id.first_start_skip);
        setListener(start);
//        setListener(skip);

        return rootView;
    }

    /**
     * 第一次启动后就将isFirstStart = false 写进内存卡
     * @param button
     */
    private void setListener(Button button){
        if(button != null){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //第一次启动
                    SharedPreferences preferences = getContext().getSharedPreferences("isFirstStart", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("isFirstStart", false);
                    editor.commit();

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
        }
    }
}
