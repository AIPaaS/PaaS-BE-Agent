package com.ai.platform.agent.client.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

public class HttpXmlClient {

	 private static Logger log = LogManager.getLogger(HttpXmlClient.class);  
     
	    public static String post(String url, Map<String, String> params) {  
	        DefaultHttpClient httpclient = new DefaultHttpClient();  
	        String body = null;  
	          
	        log.info("create httppost:" + url);  
	        HttpPost post = postForm(url, params);  
	          
	        body = invoke(httpclient, post);  
	          
	        httpclient.getConnectionManager().shutdown();  
	          
	        return body;  
	    }  
	      
	    public static String get(String url) {  
	        DefaultHttpClient httpclient = new DefaultHttpClient();  
	        String body = null;  
	          
	        log.info("create httppost:" + url);  
	        HttpGet get = new HttpGet(url);  
	        body = invoke(httpclient, get);  
	          
	        httpclient.getConnectionManager().shutdown();  
	          
	        return body;  
	    }  
	          
	      
	    private static String invoke(DefaultHttpClient httpclient,  
	            HttpUriRequest httpost) {  
	          
	        HttpResponse response = sendRequest(httpclient, httpost);  
	        String body = paseResponse(response);  
	          
	        return body;  
	    }  
	  
	    private static String paseResponse(HttpResponse response) {  
	        log.info("get response from http server..");  
	        HttpEntity entity = response.getEntity();  
	          
	        log.info("response status: " + response.getStatusLine());  
	        String charset = EntityUtils.getContentCharSet(entity);  
	        log.info(charset);  
	          
	        String body = null;  
	        try {  
	            body = EntityUtils.toString(entity);  
	            log.info(body);  
	        } catch (ParseException e) {  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	          
	        return body;  
	    }  
	  
	    private static HttpResponse sendRequest(DefaultHttpClient httpclient,  
	            HttpUriRequest httpost) {  
	        log.info("execute post...");  
	        HttpResponse response = null;  
	          
	        try {  
	            response = httpclient.execute(httpost);  
	        } catch (ClientProtocolException e) {  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	        return response;  
	    }  
	  
	    private static HttpPost postForm(String url, Map<String, String> params){  
	          
	        HttpPost httpost = new HttpPost(url);  
	        List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
	          
	        Set<String> keySet = params.keySet();  
	        for(String key : keySet) {  
	            nvps.add(new BasicNameValuePair(key, params.get(key)));  
	        }  
	          
	        try {  
	            log.info("set utf-8 form entity to httppost");  
	            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));  
	        } catch (UnsupportedEncodingException e) {  
	            e.printStackTrace();  
	        }  
	          
	        return httpost;  
	    } 
	    /**
	     * 
	     * @param url 
	     * @param json request from client , the type is application/json
	     * @date  2016-03-19
	     * @author zhangzhongde
	     * @return
	     */
	    public static JSONObject doPost(String url,JSONObject json){
	        DefaultHttpClient client = new DefaultHttpClient();
	        HttpPost post = new HttpPost(url);
	        JSONObject response = null;
	        try {
	          StringEntity s = new StringEntity(json.toString());
	          s.setContentEncoding("UTF-8");
	          s.setContentType("application/json");//发送json数据需要设置contentType
	          post.setEntity(s);
	          HttpResponse res = client.execute(post);
	          if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
	            HttpEntity entity = res.getEntity();
	            String result = EntityUtils.toString(res.getEntity());// 返回json格式：
	            response = JSONObject.parseObject(result);
	          }
	        } catch (Exception e) {
	          throw new RuntimeException(e);
	        }
	        return response;
	      }

}
