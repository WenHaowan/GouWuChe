package as.bwei.com.gouwuche.utils;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Author:kson
 * E-mail:19655910@qq.com
 * Time:2018/08/07
 * Description:工具类，java的23种设计模式之一单例模式
 */
public class OkHttpUtils {

    //
    private static OkHttpUtils okHttpUtils;
    private OkHttpClient okHttpClient;


    //构造方法私有的？因为不能被调用者new的对象，只能给自己new
    private OkHttpUtils() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new MyInterceptor())//添加拦截器
                .addInterceptor(httpLoggingInterceptor)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(5,TimeUnit.SECONDS)
                .build();


    }

    /**
     * public 暴露给调用者，双重检验锁
     *
     * @return
     */
    public static OkHttpUtils getInstance() {
        if (okHttpUtils == null) {
            synchronized (OkHttpUtils.class) {
                if (okHttpUtils == null) {
                    okHttpUtils = new OkHttpUtils();
                }
            }
        }
        return okHttpUtils;
    }


    /**
     * get封装
     *
     * @param params
     * @param url
     * @param requestCallback
     */
    public void getData(String url, HashMap<String, String> params, final RequestCallback requestCallback) {

        StringBuilder urlsb = new StringBuilder();
        String allUrl = "";
        for (Map.Entry<String, String> stringStringEntry : params.entrySet()) {
            urlsb.append("?").append(stringStringEntry.getKey()).append("=").append(stringStringEntry.getValue()).append("&");
        }

        allUrl = url + urlsb.toString().substring(0, urlsb.length() - 1);
        System.out.println("url:" + allUrl);

        final Request request = new Request.Builder()
                .url(allUrl).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            //请求失败
            @Override
            public void onFailure(Call call, IOException e) {
                if (requestCallback != null) {
                    requestCallback.failure(call, e);
                }

            }

            //请求成功
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (requestCallback != null) {
                    requestCallback.onResponse(call, response);
                }

            }
        });
    }

    /**
     * post请求方式
     *
     * @param url
     * @param prams
     */
    public void postData(String url, HashMap<String, String> prams, final RequestCallback requestCallback) {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();

        if (prams != null && prams.size() > 0) {
            for (Map.Entry<String, String> stringStringEntry : prams.entrySet()) {

                formBodyBuilder.add(stringStringEntry.getKey(), stringStringEntry.getValue());
            }
        }

        Request request = new Request.Builder()
                .url(url).post(formBodyBuilder.build()).build();


//        Call call = okHttpClient.newCall(request);
//        try {
//            System.exit(0);
//            Response response = call.execute();
////            call.execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally {
//
//        }
//        Call call2 = okHttpClient.newCall(request);
//        try {
//            call2.execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }




        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (requestCallback != null) {
                    requestCallback.failure(call, e);
                }

            }

            //请求成功
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (requestCallback != null) {
                    requestCallback.onResponse(call, response);
                }

            }
        });
    }


    /**
     * 封装上传文件
     *
     * @param url
     * @param params
     */
    public void uploadFile(String url, HashMap<String, Object> params, final RequestCallback requestCallback) {

//        OkHttpClient okHttpClient1 = new OkHttpClient();
        //多表单上传的的请求体对象，multipart/form-data
        MultipartBody.Builder builder1 = new MultipartBody.Builder();
        builder1.setType(MultipartBody.FORM);//设置上传类型是表单形式




        //1。字符串key，value，2。文件key，value
        for (Map.Entry<String, Object> stringObjectEntry : params.entrySet()) {

            String key = stringObjectEntry.getKey();
            Object value = stringObjectEntry.getValue();

            if (value instanceof File){//如果value类型是文件类型
                File file = (File) value;
                //创建文件请求体
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                builder1.addFormDataPart(key,file.getName(),requestBody);
            }else {
                builder1.addFormDataPart(key, (String) value);
            }
        }

        Request request1 = new Request.Builder().addHeader("","").post(builder1.build()).url(url).build();

//        Call call = okHttpClient.newCall(request1);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//            }
//        });
//
//        call.cancel();

        okHttpClient.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (requestCallback != null) {
                    requestCallback.failure(call, e);
                }

                URLEncoder.encode("url");

            }

            //请求成功
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (requestCallback != null) {
                    requestCallback.onResponse(call, response);
                }

            }
        });

    }
}
