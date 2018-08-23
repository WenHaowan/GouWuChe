package as.bwei.com.gouwuche.utils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Author:kson
 * E-mail:19655910@qq.com
 * Time:2018/08/08
 * Description:
 */
public interface RequestCallback {

    void failure(Call call, IOException e);
    void onResponse(Call call, Response response);
}
