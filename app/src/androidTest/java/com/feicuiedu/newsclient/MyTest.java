package com.feicuiedu.newsclient;


import android.content.Context;
import android.content.res.Resources;
import android.test.AndroidTestCase;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenyan on 2016/4/30.
 */
public class MyTest extends AndroidTestCase {

    private String strTag = "feicui_unit_test";

    private Context context;

    private Resources resources;

    private int max_total_connections;
    private int wait_time_out;
    private int max_route_connections;
    private int connect_time_out;
    private int read_time_out;

    private HttpClient httpClient;

    private HttpURLConnection httpURLConnection;

    private void initContext() {

        if (context == null) {
            context = getContext();
        }
    }

    private void initResuource() {
        initContext();

        if (resources == null) {
            resources = context.getResources();
        }
    }

    private void initApacheHttpParam() {

        initResuource();
       /* Log.d(strTag,R.integer.max_total_connections+"");
        Log.d(strTag,resources.getInteger(R.integer.max_total_connections)+"");
        Log.d(strTag,resources.getResourceName(R.integer.max_total_connections)+"");
        Log.d(strTag,resources.getResourceEntryName(R.integer.max_total_connections)+"");
        Log.d(strTag,resources.getResourcePackageName(R.integer.max_total_connections)+"");
        Log.d(strTag,resources.getResourceTypeName(R.integer.max_total_connections)+"");*/

        max_total_connections = resources.getInteger(R.integer.max_total_connections);
        wait_time_out = resources.getInteger(R.integer.wati_time_out);
        max_route_connections = resources.getInteger(R.integer.max_route_connections);
        connect_time_out = resources.getInteger(R.integer.connect_time_out);
        read_time_out = resources.getInteger(R.integer.read_time_out);

    }

    public void test1() {

        Log.d(strTag, "ahahahahahaah");

        Log.d(strTag,getContext().getResources().getString(R.string.test_url));
    }


    // 测试取得HttpClient android 5 以后被标识为已过时，但是仍然可以使用
    private void initApacheHttpClient() {


        initApacheHttpParam();

        HttpParams httpParams = new BasicHttpParams();
        // 设置最大连接数
        ConnManagerParams.setMaxTotalConnections(httpParams, max_total_connections);
        // 设置获取连接的最大等待时间
        ConnManagerParams.setTimeout(httpParams, wait_time_out);
        // 设置每个路由最大连接数
        ConnPerRouteBean connPerRoute = new ConnPerRouteBean(max_route_connections);
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, connPerRoute);


        // 设置连接超时时间
        HttpConnectionParams.setConnectionTimeout(httpParams, connect_time_out);
        // 设置读取超时时间
        HttpConnectionParams.setSoTimeout(httpParams, read_time_out);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 9092));
        registry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

        ClientConnectionManager connectionManager = new ThreadSafeClientConnManager(httpParams, registry);

        httpClient = new DefaultHttpClient(connectionManager, httpParams);


        if (httpClient != null) {

            Log.d(strTag, "httpClinet success");
        } else {
            Log.d(strTag, "httpClinet failed");
        }
    }

    // 测试Apache的Get方法
    public void testApacheHttpClientGet() {

        String resultStr = null;
        if (httpClient == null) {
            initApacheHttpClient();

        }

        String strUrl = resources.getString(R.string.interface_url)+resources.getString(R.string.test_url);

        String param = "?ver="+resources.getString(R.string.p_ver)+"&subid="+resources.getString(R.string.p_subid)
                +"&dir="+resources.getString(R.string.p_dir)+"&nid="+resources.getString(R.string.p_nid)
                +"&stamp="+resources.getString(R.string.p_stamp)+"&cnt="+resources.getString(R.string.p_cnt);

        strUrl += param;
        Log.d("strUrl",strUrl);

        HttpGet httpGet=new HttpGet(strUrl);
        HttpResponse response= null;
        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            resultStr = EntityUtils.toString(entity, "GBK");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(strTag, "apache_get_resultStr="+resultStr);
    }

    // 测试Apache的Post方法
    public void testApacheHttpClientPost() {

        String resultStr = null;
        if (httpClient == null) {
            initApacheHttpClient();
        }

        String strUrl = resources.getString(R.string.interface_url)+resources.getString(R.string.test_url);

        BasicNameValuePair ver = new BasicNameValuePair("ver", resources.getString(R.string.p_ver));
        BasicNameValuePair subid = new BasicNameValuePair("subid", resources.getString(R.string.p_subid));
        BasicNameValuePair dir = new BasicNameValuePair("dir", resources.getString(R.string.p_dir));
        BasicNameValuePair nid = new BasicNameValuePair("nid", resources.getString(R.string.p_nid));
        BasicNameValuePair stamp = new BasicNameValuePair("stamp", resources.getString(R.string.p_stamp));
        BasicNameValuePair cnt = new BasicNameValuePair("cnt", resources.getString(R.string.p_cnt));

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(ver);
        params.add(subid);
        params.add(dir);
        params.add(nid);
        params.add(stamp);
        params.add(cnt);

        HttpPost httpPost=new HttpPost(strUrl);
        HttpResponse response= null;
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(params,"UTF-8");
            httpPost.setEntity(entity);
            HttpResponse httpResponse =  httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity2 = httpResponse.getEntity();
                resultStr = EntityUtils.toString(entity2, "utf-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(strTag, "apache_post_resultStr="+resultStr);
    }


    // 测试取得HttpClent 目前官方建议
    public void testHttpClientGet() {

        initResuource();

        String resultStr = null;
        URL url = null;
        try {

            String strUrl = resources.getString(R.string.interface_url)+resources.getString(R.string.test_url);

            String param = "?ver="+resources.getString(R.string.p_ver)+"&subid="+resources.getString(R.string.p_subid)
                    +"&dir="+resources.getString(R.string.p_dir)+"&nid="+resources.getString(R.string.p_nid)
                    +"&stamp="+resources.getString(R.string.p_stamp)+"&cnt="+resources.getString(R.string.p_cnt);

            strUrl += param;

            url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // 设置连接超时为5秒
            conn.setConnectTimeout(connect_time_out);
            // 设置读取时间
            conn.setReadTimeout(read_time_out);

            // 设置请求类型为Get类型
            conn.setRequestMethod("GET");
            // 判断请求Url是否成功
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("请求url失败");
            }
            else {
                InputStream in = conn.getInputStream();
                byte[] data = read(in);
                resultStr = new String(data, "UTF-8");
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(strTag, "http_get_resultStr="+resultStr);
    }

    public void testHttpClientPost() {

        initResuource();

        String resultStr = null;

        URL url = null;
        try {

            String strUrl = resources.getString(R.string.interface_url)+resources.getString(R.string.test_url);

            String param = "ver="+resources.getString(R.string.p_ver)+"&subid="+resources.getString(R.string.p_subid)
                    +"&dir="+resources.getString(R.string.p_dir)+"&nid="+resources.getString(R.string.p_nid)
                    +"&stamp="+resources.getString(R.string.p_stamp)+"&cnt="+resources.getString(R.string.p_cnt);

            url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // 设置连接超时为5秒
            conn.setConnectTimeout(connect_time_out);
            // 设置读取时间
            conn.setReadTimeout(read_time_out);

            // 设置请求类型为Post类型
            conn.setRequestMethod("POST");

            //获取输出流
            OutputStream out = conn.getOutputStream();
            out.write(param.getBytes());
            out.flush();

            // 判断请求Url是否成功
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("请求url失败");
            }
            else {
                InputStream in = conn.getInputStream();
                byte[] data = read(in);
                resultStr = new String(data, "UTF-8");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(strTag, "http_post_resultStr="+resultStr);
    }

    //从流中读取数据
    public static byte[] read(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len = inStream.read(buffer)) != -1)
        {
            outStream.write(buffer,0,len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

}
