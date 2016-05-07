package com.jhy.org.yueqiu.test.h;

import com.jhy.org.yueqiu.utils.Logx;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/4/27 0027.
 */
public class HeWeatherUtils {
    private static final String HTTP_URL = "http://apis.baidu.com/heweather/weather/free";
    private static final String ARG_CITY = "city";
    private static final String API_KEY = "d28afcb7b903a5b6300b0a8f25ab3793";
    private static Logx logx = new Logx(HeWeatherUtils.class);

    public static String request (String city) {
        String result = null;
        String httpUrl = HTTP_URL + "?" + ARG_CITY + "=" + city;
        try {
            URL url = new URL(httpUrl);
            logx.e("url: " + httpUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("apikey", API_KEY);
            connection.connect();

            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuffer buffer = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
                buffer.append("\r\n");
            }
            reader.close();
            result = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
