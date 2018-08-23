package as.bwei.com.gouwuche.model;

import android.os.Handler;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

import as.bwei.com.gouwuche.bean.CartBean;
import as.bwei.com.gouwuche.utils.OkHttpUtils;
import as.bwei.com.gouwuche.utils.RequestCallback;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by HP on 2018/8/22.
 */

public class CartModel {

    Handler handler =new Handler();

    /**
     * 请求购物车数据
     *
     * @param params 参数
     * @param url    请求url
     */
    public void getCarts(HashMap<String, String> params, String url, final CartCallback cartCallback) {

        OkHttpUtils.getInstance().postData(url, params, new RequestCallback() {
            @Override
            public void failure(Call call, IOException e) {

                if (cartCallback != null) {
                    cartCallback.fail("网络有异常，请稍后再试");
                }

            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String jsonResult = response.body().string();
                    if (!TextUtils.isEmpty(jsonResult)) {

                        parseResult(jsonResult, cartCallback);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 解析购物车数据
     *
     * @param jsonResult
     * @param cartCallback
     */
    private void parseResult(String jsonResult, final CartCallback cartCallback) {
        final CartBean cartBean = new Gson().fromJson(jsonResult, CartBean.class);
        if (cartCallback != null && cartBean != null) {//代码规范，代码优化

            handler.post(new Runnable() {
                @Override
                public void run() {
                    cartCallback.success(cartBean);
                }
            });
        }


    }

    public interface CartCallback {
        void success(CartBean cartBean);//回调bean对象给presenter

        void fail(String msg);//异常信息回调
    }
}
