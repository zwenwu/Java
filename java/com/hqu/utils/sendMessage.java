package com.hqu.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.logging.log4j.Log4jImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hqu.model.PostBean;
import com.hqu.model.MessageResponse;
import com.hqu.model.templateSMSModel;
import com.hqu.realm.ShiroDbRealm;

public class sendMessage {
	public static final String accountsid = "02b715bdf7bf5fcbf1fb8a15bbe2c237";//02b715bdf7bf5fcbf1fb8a15bbe2c237
    public static final String authtoken = "e71c4519bdd5e515653dd703756742a1";//e71c4519bdd5e515653dd703756742a1
    public static final String resturl = "https://api.ucpaas.com";
    public static final String appId = "8c56ec2b8d654bbe97d19deef0eed171";//8c56ec2b8d654bbe97d19deef0eed171
    public static final String templeteId = "31414";//31414
    public static final String version = "2014-06-30";
    private static Logger logger = LoggerFactory.getLogger(ShiroDbRealm.class);
    /*private static String param;
    
    private static void setParam() {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            sb.append(random.nextInt(10));
        }
        param = sb.toString();

    }
    private static String getparam() {
        return param;
    }*/
    /**
     * 获取url
     * @return
     */
    private static String getSmsRestUri() {
    	String SigParameter = getSigParameter();
        StringBuffer sb = new StringBuffer();
        String url = sb.append(resturl).append("/").append(version).append("/")
                .append("Accounts").append("/").append(accountsid)
                .append("/Messages/templateSMS").append("?sig=")
                .append(SigParameter).toString();
        return url;
    }
    private static String getSigParameter() {
        String timeStamp = getTimeStamp();
        String sig="";
        try {
            sig = CipherUtil.encodeByMD5(accountsid + authtoken + timeStamp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sig.toUpperCase();// 转大写
    }
    
    private static String getTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sdf.format(new Date());
        return date;
    }
    private static String getAuthorization() throws Exception {
        String src = accountsid + ":" + getTimeStamp();
        String authorization = new String((new sun.misc.BASE64Encoder()).encode( src.getBytes() ));  

        return authorization;
    }
    public static String checkNum(String toPhone,String code) {
        String resp = "test";
        /*DefaultHttpClient client = new DefaultHttpClient();*/
        HttpClient client = HttpClientBuilder.create().build();
        String uri = getSmsRestUri();
        PostBean bean = new PostBean();
        bean.setAppId(appId);
        bean.setParam(code);
        bean.setTemplateId(templeteId);
        bean.setTo(toPhone);
        String body = JSON.toJSONString(bean);
        body = "{\"templateSMS\":" + body + "}";
        logger.debug(body);
        try {
            HttpResponse response = post(client, uri, body);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                resp = EntityUtils.toString(entity, "utf-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
        MessageResponse model = JSON.parseObject(resp,MessageResponse.class);
        return model.getResp().getRespCode();
    }
    public static String charterCarSendMessage(String toPhone,String content) throws IOException{
    	String resp = "test";
		/*DefaultHttpClient client = new DefaultHttpClient();*/
		HttpClient client = HttpClientBuilder.create().build();
        String uri = getSmsRestUri();
        PostBean bean = new PostBean();
        bean.setAppId(appId);
        bean.setParam(content);
        bean.setTemplateId("31695");
        bean.setTo(toPhone);
        templateSMSModel templateSMSModel = new templateSMSModel();
        templateSMSModel.setTemplateSMS(bean);
        String body = JSON.toJSONString(templateSMSModel);
        /*body = "{\"templateSMS\":" + body + "}";*/
        logger.debug(body);
        HttpResponse response=null;
        try {
             response = post(client, uri, body);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                resp = EntityUtils.toString(entity, "utf-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
        	EntityUtils.consume(response.getEntity());
        }
        MessageResponse model = JSON.parseObject(resp,MessageResponse.class);
        return model.getResp().getRespCode();
    }
    public static String sendMessageToPeople(String toPhone,String content) throws IOException{
    	String resp = "test";
		/*DefaultHttpClient client = new DefaultHttpClient();*/
		HttpClient client = HttpClientBuilder.create().build();
        String uri = getSmsRestUri();
        PostBean bean = new PostBean();
        bean.setAppId(appId);
        bean.setParam(content);
        bean.setTemplateId("32546");
        bean.setTo(toPhone);
        templateSMSModel templateSMSModel = new templateSMSModel();
        templateSMSModel.setTemplateSMS(bean);
        String body = JSON.toJSONString(templateSMSModel);
        /*body = "{\"templateSMS\":" + body + "}";*/
        logger.debug(body);
        HttpResponse response=null;
        try {
             response = post(client, uri, body);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                resp = EntityUtils.toString(entity, "utf-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
        	EntityUtils.consume(response.getEntity());
        }
        MessageResponse model = JSON.parseObject(resp,MessageResponse.class);
        return model.getResp().getRespCode();
    }
    public static String sendMessageToAllPeople(String toPhone,String content) throws IOException{
    	String resp = "test";
		/*DefaultHttpClient client = new DefaultHttpClient();*/
		HttpClient client = HttpClientBuilder.create().build();
        String uri = getSmsRestUri();
        PostBean bean = new PostBean();
        bean.setAppId(appId);
        bean.setParam(content);
        bean.setTemplateId("33648");
        bean.setTo(toPhone);
        templateSMSModel templateSMSModel = new templateSMSModel();
        templateSMSModel.setTemplateSMS(bean);
        String body = JSON.toJSONString(templateSMSModel);
        /*body = "{\"templateSMS\":" + body + "}";*/
        logger.debug(body);
        HttpResponse response=null;
        try {
             response = post(client, uri, body);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                resp = EntityUtils.toString(entity, "utf-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
        	EntityUtils.consume(response.getEntity());
        }
        MessageResponse model = JSON.parseObject(resp,MessageResponse.class);
        return model.getResp().getRespCode();
    }
    public static HttpResponse post(HttpClient client, String uri, String body)
            throws Exception {
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
        String authorization = getAuthorization();
        httpPost.setHeader("Authorization", authorization);
        logger.debug(body);
        logger.debug(body.getBytes("UTF-8").toString());
        if (body != null && body.length() > 0) {
            BasicHttpEntity entity = new BasicHttpEntity();
            entity.setContent(new ByteArrayInputStream(body.getBytes("UTF-8")));
            entity.setContentLength(body.getBytes("UTF-8").length);
            httpPost.setEntity(entity);
        }
        HttpResponse response = client.execute(httpPost);
        return response;

    }
    public static void main(String[] args) {		
    	String  jsonString ="{'resp':{'respCode':'000000','templateSMS':{'createDate':'20161031132551','smsId':'5b51aaa7e6fe6f5738046d9f3ca3f8c2'}}}";
    	
    	MessageResponse model = JSON.parseObject(jsonString,MessageResponse.class);
    	/*String jsString = "{'CSDM':'0101'}";//{"CSDM":"0101"}
    	Line model = JSON.parseObject(jsString, Line.class);*/
    	String para = "";
		try {
			para = sendMessageToPeople("18850042980","车牌号换了");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println(para);
	}
}
