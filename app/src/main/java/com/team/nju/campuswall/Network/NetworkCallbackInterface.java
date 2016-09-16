package com.team.nju.campuswall.Network;

/**
 * Created by user on 2016/9/16.
 */
import org.json.JSONException;

import java.io.IOException;
public class NetworkCallbackInterface {
    /**
     * 网络请求回调接口
     */
    public interface NetRequestIterface {
        void requestFinish(String result, String requestUrl) throws JSONException;
        void exception(IOException e, String requestUrl);
    }
    /**
     * 点击退出查看添加图片大图回调接口
     */
    public interface OnSingleTapDismissBigPhotoListener {
        void onDismissBigPhoto();
    }
}
