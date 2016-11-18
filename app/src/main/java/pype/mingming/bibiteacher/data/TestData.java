package pype.mingming.bibiteacher.data;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import pype.mingming.bibiteacher.entity.Post;

/**
 * Created by wushaohongly on 2016/8/5.
 */
public class TestData {

    static List<Post> returnList = null;

    public static List<Post> getDate(Context context){

        returnList = new ArrayList<Post>();


        BmobQuery<Post> query = new BmobQuery<Post>();

        query.findObjects(context, new FindListener<Post>() {
            @Override
            public void onSuccess(List<Post> list) {
                if (list.size() != 0){
                    for (Post post : list) {
                        Log.i("info","success");
                        returnList.add(post);
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.i("info","error");
            }
        });

        return returnList;
    }
}
