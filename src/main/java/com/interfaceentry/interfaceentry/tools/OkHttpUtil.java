package com.interfaceentry.interfaceentry.tools;

import okhttp3.*;
import okio.BufferedSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created By zengyh on 2018/1/11
 */
@SuppressWarnings("deprecation")
public class OkHttpUtil {
    private Logger logger = LoggerFactory.getLogger(OkHttpUtil.class);


    private static final okhttp3.MediaType MEDIA_TYPE_TEXT = okhttp3.MediaType.parse("text/plain; charset=utf-8");
//  private static final okhttp3.MediaType APPLICATION_FORM_URLENCODED = okhttp3.MediaType.parse("application/x-www-form-urlencoded");

    public static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";
    public static final String APPLICATION_JSON = "application/json";
    public static final String APPLICATION_JSON_UTF8 = "application/json;charset=UTF-8";
    public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
    public static final String APPLICATION_PDF = "application/pdf";
    public static final String APPLICATION_STREAM_JSON = "application/stream+json";
    public static final String APPLICATION_XML = "application/xml";
    public static final String IMAGE_GIF = "image/gif";
    public static final String IMAGE_JPEG = "image/jpeg";
    public static final String IMAGE_PNG = "image/png";
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    public static final String TEXT_EVENT_STREAM = "text/event-stream";
    public static final String TEXT_HTML = "text/html";
    public static final String TEXT_MARKDOWN = "text/markdown";
    public static final String TEXT_PLAIN = "text/plain";
    public static final String TEXT_XML = "text/xml";


    private static OkHttpUtil okHttpUtil;
    public static OkHttpUtil getInstance(){
        if(okHttpUtil == null){
            okHttpUtil = new OkHttpUtil();
        }
        return okHttpUtil;
    }

    public static OkHttpClient createOkHttpClient(){
        return OkHttpUtil.getInstance().createClient();
    }

    public static OkHttpClient createOkHttpsClient(byte[] keyStorePath, String password){
        try {
            //初始化证书文件
            SSLContext sslContext = SSLContextUtil.getSSLContext(keyStorePath, password);
            return OkHttpUtil.getInstance().createSSLClient(sslContext);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String get(String url) {
        return OkHttpUtil.getInstance().doGet(url);
    }

    public static String post(String url, String data, String contentType) {
        return OkHttpUtil.getInstance().doPost(url, data, contentType);
    }

    public static String postRaw(String url, String rawData, String contentType) {
        return OkHttpUtil.getInstance().doPostRaw(url, rawData, contentType);
    }

    public static String postSSL(String url, String data, String contentType, byte[] keyStorePath, String password) {
        try {
            //初始化证书文件
            SSLContext sslContext = SSLContextUtil.getSSLContext(keyStorePath, password);
            return OkHttpUtil.getInstance().doPostSSL(url, data, contentType, sslContext);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }


    /**
     * Get请求
     * @param url
     * @return
     */
    public String doGet(String url){
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            Response response =  this.doExecute(request);
            return new String(response.body().bytes(),"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 普通body传输
     * @param url
     * @param data
     * @param contentType
     * @return
     */
    public String doPost(String url, String data, String contentType){
        try {
            okhttp3.MediaType mediaType = MediaType.parse(contentType);
            RequestBody requestBody = RequestBody.create(mediaType,data);
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            Response response = this.doExecute(request);
            return new String(response.body().bytes(),"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 流传输
     * @param url
     * @param rawData
     * @param contentType
     * @return
     */
    public String doPostRaw(String url, String rawData, String contentType){
        try {
            okhttp3.MediaType mediaType = MediaType.parse(contentType);
            RequestBody body = new RequestBody() {
                @Override
                public MediaType contentType() {
                    return mediaType;
                }
                @Override
                public void writeTo(BufferedSink bufferedSink) throws IOException {
                    bufferedSink.writeUtf8(rawData);
                }
            };

            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            Response response = doExecute(request);
            return new String(response.body().bytes(), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * HTTPS请求
     * @param url
     * @param data
     * @param contentType
     * @param sslContext
     * @return
     */
    public String doPostSSL(String url, String data, String contentType, SSLContext sslContext){
        try {
            okhttp3.MediaType mediaType = MediaType.parse(contentType);
            RequestBody requestBody = RequestBody.create(mediaType,data);
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            Response response = doExecute(request, sslContext);
            logger.info("response status code:" + response.code());
            return new String(response.body().bytes(),"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    /**
     * 执行HTTP请求
     * @param request
     * @return
     * @throws IOException
     */
    public Response doExecute(Request request) throws IOException {
        OkHttpClient client = this.createClient();
        Call call = client.newCall(request);
        //call.enqueue(callback);
        Response response = call.execute();
        logger.info("response status code:" + response.code());
        if(!response.isSuccessful()){
            throw new RuntimeException();
        }
        return response;
    }

    /**
     * 执行HTTPS请求
     * @param request
     * @param sslContext
     * @return
     * @throws IOException
     */
    public Response doExecute(Request request, SSLContext sslContext) throws IOException {
        OkHttpClient sslClient = this.createSSLClient(sslContext);
        Call call = sslClient.newCall(request);
        //call.enqueue(callback);
        Response response = call.execute();
        logger.info("response status code:" + response.code());
        if(!response.isSuccessful()){
            throw new RuntimeException();
        }
        return response;
    }

    /**
     *  添加响应拦截器
     */
    private Interceptor respInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            return chain.proceed(chain.request());
        }
    };

    /**
     * 回调处理
     */
    private Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String responseStr = new String(response.body().bytes(),"UTF-8");
            logger.info(responseStr);
        }
    };

    /**
     * 创建HTTP对象
     */
    public OkHttpClient createClient(){
        return new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(10, 10, TimeUnit.MINUTES))
                .addInterceptor(respInterceptor)
                .build();
    }

    /**
     * 创建HTTPS对象
     */
    public OkHttpClient createSSLClient(SSLContext sslContext){
        return new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(10, 10, TimeUnit.MINUTES))
                .sslSocketFactory(sslContext.getSocketFactory())
                .addInterceptor(respInterceptor)
                .build();
    }
}
