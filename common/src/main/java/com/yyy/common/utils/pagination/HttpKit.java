package com.yyy.common.utils.pagination;

import cn.hutool.json.JSONObject;
import com.yyy.common.constants.Constant;
import com.yyy.common.utils.pagination.wrapper.WafRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author: Mr.Liu
 * @Description:
 * @Date: 2019/5/29 10:09
 */
@Slf4j
public class HttpKit {
    private HttpKit(){
        //私有构造
    }


    public static String getAbsUrl() {
        HttpServletRequest request = getRequest();
        return request.getRequestURL().toString();
    }

    public static String getUri() {
        HttpServletRequest request = getRequest();
        return request.getRequestURI();
    }

    public static String getIp() {
        return getRequest().getRemoteHost();
    }

    public static Map<String, String> getRequestParameters() {
        HashMap<String, String> values = new HashMap();
        HttpServletRequest request = getRequest();
        Enumeration enums = request.getParameterNames();

        while (enums.hasMoreElements()) {
            String paramName = (String) enums.nextElement();
            String paramValue = request.getParameter(paramName);
            values.put(paramName, paramValue);
        }
        return values;
    }

    public static JSONObject getRequestParametersJSON() {
        HttpServletRequest request = getRequest();
        JSONObject jsonObj = new JSONObject();
        Map<String, String[]> params = request.getParameterMap();
        Iterator var3 = params.entrySet().iterator();

        while (var3.hasNext()) {
            Map.Entry<String, String[]> entry = (Map.Entry) var3.next();
            String[] v = (String[]) entry.getValue();
            Object o = v.length == 1 ? v[0] : v;
            jsonObj.put((String) entry.getKey(), o);
        }

        return jsonObj;
    }

    public static HttpServletResponse getResponse() {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        return response;
    }

    public static HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return new WafRequestWrapper(request);
    }

    public static String sendGet(String url, Map<String, String> param) {
        return send(url, (Map) null, param, RequstMethod.GET, (Object) null);
    }

    public static String sendPost(String url, Map<String, String> param) {
        return send(url, (Map) null, param, RequstMethod.POST, param);
    }

    private static String getUrlNameString(String url,Map<String, String> params) throws UnsupportedEncodingException {
        StringBuffer query = new StringBuffer();
        Iterator var10 = params.entrySet().iterator();

        while (var10.hasNext()) {
            Map.Entry<String, String> kv = (Map.Entry) var10.next();
            query.append(URLEncoder.encode(kv.getKey(), "UTF-8") + "=");
            query.append(URLEncoder.encode(kv.getValue(), "UTF-8") + "&");
        }

        if (query.lastIndexOf("&") > 0) {
            query.deleteCharAt(query.length() - 1);
        }

        return url + "?" + query.toString();
    }

    public static String send(String url, Map<String, String> heads, Map<String, String> params, RequstMethod method, Object body) {
        String result = "";
        String line;
        try {
            String urlNameString = url;
            if (params != null) {
                urlNameString =getUrlNameString(url,params);
            }

            URL realUrl = new URL(urlNameString);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestMethod(method.value());
            if (heads != null) {
                Iterator var25 = heads.keySet().iterator();

                while (var25.hasNext()) {
                    String key = (String) var25.next();
                    conn.setRequestProperty(key, (String) heads.get(key));
                }
            }

            if (!Constant.GET.equals(method.value())) {
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                if (body != null) {
                    try(PrintWriter out  = new PrintWriter(conn.getOutputStream())) {
                        out.print(body);
                        out.flush();
                    }
                }
            }

            try( BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                for (; (line = in.readLine()) != null; result = result + line) {

                }
            }

        } catch (Exception var21) {
            log.error("发送 请求出现异常！,异常信息为：{}", var21.getMessage());
        }

        return result;
    }

    public static enum RequstMethod {
        GET("GET"),
        POST("POST"),
        PUT("PUT"),
        DELETE("DELETE");

        private final String method;

        private RequstMethod(String m) {
            this.method = m;
        }

        public String value() {
            return this.method;
        }
    }
}
