package com.homechart.app.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 获取HTML数据
 *
 * @author David
 */
public class HtmlService {

    public static String getHtml(String path, String name) throws Exception {

        // 通过网络地址创建URL对象
        URL url = new URL(path);
        // 根据URL
        // 打开连接，URL.openConnection函数会根据URL的类型，返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 设定URL的请求类别，有POST、GET 两类
        conn.setRequestMethod("POST");
        //设置从主机读取数据超时（单位：毫秒）
        conn.setConnectTimeout(10000);
        //设置连接主机超时（单位：毫秒）
        conn.setReadTimeout(10000);
        conn.connect();
        // 通过打开的连接读取的输入流,获取html数据
        InputStream inStream = conn.getInputStream();
        // 得到html的二进制数据
        byte[] data = readInputStream(inStream);
        // 是用指定的字符集解码指定的字节数组构造一个新的字符串
        if (name.trim().equals("天猫")) {

            String html = new String(data, "GB2312");

            return html;
        } else if (name.trim().equals("极有家")) {
//            String location = conn.getHeaderField("Location");
//            URL url1 = new URL(location);
//            // 根据URL
//            // 打开连接，URL.openConnection函数会根据URL的类型，返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
//            HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
//            // 设定URL的请求类别，有POST、GET 两类
//            conn1.setRequestMethod("GET");
//            //设置从主机读取数据超时（单位：毫秒）
//            conn1.setConnectTimeout(10000);
//            //设置连接主机超时（单位：毫秒）
//            conn1.setReadTimeout(10000);
//            // 通过打开的连接读取的输入流,获取html数据
//            InputStream inStream1 = conn1.getInputStream();
//            // 得到html的二进制数据
//            byte[] data1 = readInputStream(inStream1);
            String html = new String(data, "utf-8");

            return html;
        } else {

            String html = new String(data, "utf-8");

            return html;
        }
    }

    /**
     * 读取输入流，得到html的二进制数据
     *
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

}