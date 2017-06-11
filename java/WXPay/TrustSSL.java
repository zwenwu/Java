package WXPay;
import java.io.BufferedReader;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.io.PrintWriter;  
import java.io.UnsupportedEncodingException;  
import java.net.HttpURLConnection;  
import java.net.MalformedURLException;  
import java.net.URL;  
import java.net.URLEncoder;  
import java.security.cert.CertificateException;  
import java.security.cert.X509Certificate;  
import java.sql.Timestamp;
import java.util.HashMap;  
import java.util.Iterator;  
  

import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.net.ssl.*; 

import net.sf.json.xml.XMLSerializer;



import com.hqu.model.WXPay;
import com.hqu.model.WXPayReturn;
import com.hqu.utils.JSON;
import com.hqu.utils.MD5Util;
public class TrustSSL {
	private  class TrustAnyTrustManager implements X509TrustManager {  
	      
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {  
        }  
      
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {  
        }  
      
        public X509Certificate[] getAcceptedIssuers() {  
            return new X509Certificate[]{};  
        }  
    }  
      
    private  class TrustAnyHostnameVerifier implements HostnameVerifier {  
        public boolean verify(String hostname, SSLSession session) {  
            return true;  
        }  
    }  
      
    /** 
     * http请求 
     * @param url 
     * @param data 
     * @return 
     * @throws Exception 
     */  
    public static  String requestUrl(String url, SortedMap<String, String> data)  
            throws Exception {      	
        HttpURLConnection conn;  
        try {  
            URL requestUrl = new URL(url);  
            conn = (HttpURLConnection) requestUrl.openConnection();  
        } catch (MalformedURLException e) {          	
            return e.getMessage();  
        }  
  
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");  
  
        conn.setDoInput(true);  
        conn.setDoOutput(true);  
        
        PrintWriter writer = new PrintWriter(conn.getOutputStream());  
        writer.print( buildUnifiedOrderReq(data));
        writer.flush();  
        writer.close();  
  
        String line;  
        BufferedReader bufferedReader;  
        StringBuilder sb = new StringBuilder();  
        InputStreamReader streamReader = null;  
        try {  
            streamReader = new InputStreamReader(conn.getInputStream(), "UTF-8");  
        } catch (IOException e) {  
            streamReader = new InputStreamReader(conn.getErrorStream(), "UTF-8");  
        } finally {  
            if (streamReader != null) {  
                bufferedReader = new BufferedReader(streamReader);  
                sb = new StringBuilder();  
                while ((line = bufferedReader.readLine()) != null) {  
                    sb.append(line);  
                }  
            }  
        }  
        return sb.toString();  
    }  
      
    /** 
     * 参数编码 
     * @param data 
     * @return  
     */  
    public  String httpBuildQuery(HashMap<String, String> data) {  
        String ret = "";  
        String k, v;  
        Iterator<String> iterator = data.keySet().iterator();  
        while (iterator.hasNext()) {  
            k = iterator.next();  
            v = data.get(k);  
            try {  
                ret += URLEncoder.encode(k, "utf8") + "=" + URLEncoder.encode(v, "utf8");  
            } catch (UnsupportedEncodingException e) {  
            }  
            ret += "&";  
        }  
        return ret.substring(0, ret.length() - 1);  
    }  
      
  
    public  String sengHTTPSGet(String url,HashMap<String, String> data) throws Exception {  
        SSLContext sc = SSLContext.getInstance("SSL");  
        sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());  
        URL console = new URL(url);  
        HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();  
        conn.setSSLSocketFactory(sc.getSocketFactory());  
        conn.setHostnameVerifier(new TrustAnyHostnameVerifier());  
       // conn.connect();  
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");  
  
        conn.setDoInput(true);  
        conn.setDoOutput(true);  
  
        PrintWriter writer = new PrintWriter(conn.getOutputStream());  
        writer.print(new String( httpBuildQuery(data).getBytes("UTF-8"), "ISO-8859-1"));  
        writer.flush();  
        writer.close();  
  
        String line;  
        BufferedReader bufferedReader;  
        StringBuilder sb = new StringBuilder();  
        InputStreamReader streamReader = null;  
        try {  
            streamReader = new InputStreamReader(conn.getInputStream(), "UTF-8");  
        } catch (IOException e) {  
            /* 
            Boolean ret2 = true; 
            if (ret2) { 
                return e.getMessage(); 
            } 
            */  
            streamReader = new InputStreamReader(conn.getErrorStream(), "UTF-8");  
        } finally {  
            if (streamReader != null) {  
                bufferedReader = new BufferedReader(streamReader);  
                sb = new StringBuilder();  
                while ((line = bufferedReader.readLine()) != null) {  
                    sb.append(line);  
                }  
            }  
        }  
        return sb.toString();  
    }  
    /**
     * 转成xml格式
     * @param payParams
     * @return
     * @throws Exception 
     */
    public static  String buildUnifiedOrderReq(SortedMap<String,String> payParams) throws Exception{
        String payStr = "";
        payStr += "<xml>";
        payStr +=    "<appid>"+payParams.get("appid")+"</appid>";
        payStr +=    "<body>"+payParams.get("body")+"</body>";
        payStr +=    "<mch_id>"+payParams.get("mch_id")+"</mch_id>";
        payStr +=    "<nonce_str>"+payParams.get("nonce_str")+"</nonce_str>";
        payStr +=    "<notify_url>"+payParams.get("notify_url")+"</notify_url>";
        payStr +=    "<out_trade_no>"+payParams.get("out_trade_no")+"</out_trade_no>";
        payStr +=    "<spbill_create_ip>"+payParams.get("spbill_create_ip")+"</spbill_create_ip>";
        payStr +=    "<total_fee>"+payParams.get("total_fee")+"</total_fee>";
        payStr +=    "<trade_type>APP</trade_type>";
        payStr +=    "<sign>"+payParams.get("sign")+"</sign>";
        payStr +="</xml>";
        System.out.println(payStr);
        payStr = new String(payStr);
        return payStr;
    }
    /**
     * 生成32位随机数
     * @return
     */
    public static String getRandomString() {
		String base = "0123456789QWERTYUIOPLKJHGFDSAZXCVBNM";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 32; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
    /**
     * 获取签名
     * @param signParams
     * @return
     */
    public static String getSign(SortedMap<String, String> signParams){
    	System.out.println(signParams.get("appid"));
    	Iterator iterator = signParams.entrySet().iterator();
    	String str="";
    	while(iterator.hasNext()){
    		Map.Entry me = (Map.Entry)iterator.next();
    		if(me.getValue()!=""&me.getValue()!=null){
    			str +=me.getKey()+"="+me.getValue()+"&";
    		}
    	}
    	str+="key="+Key;
    	System.out.println(str);
    	return str;
    }
    private static final String Key="wvIfF9RGwIz9oMN8DbSYs8hGzCXkif7Q";
    private static final String appid = "wx9565fa1a8f53d654";
    private static final String mch_id = "1405186702";
    //private static final String notify_url = "120.25.150.89:8080/bus/api/WXnotify";//以后发布后要改
    private static final String notify_url = "120.76.23.209:8080/bus/api/WXnotify";//以后发布后要改
    public static void main(String[] args) throws Exception{
    	SortedMap<String, String> signParams = new TreeMap<String, String>(); 
    	signParams.put("appid", appid);  //wx9565fa1a8f53d654
    	signParams.put("mch_id", mch_id);//1405186702 
        signParams.put("nonce_str",getRandomString());  //getRandomString()
        signParams.put("body", "万逸通出行支付");//传值
        signParams.put("out_trade_no", "20150806125346");//订单号自己生成
        signParams.put("total_fee", "1");
        signParams.put("spbill_create_ip", "123.12.12.123");//ip传值
        signParams.put("notify_url", notify_url);
        signParams.put("trade_type", "APP");
        
    	String	sign=MD5Util.MD5Encode(getSign(signParams), null).toUpperCase();
    	System.out.println(sign);
        signParams.put("sign", sign);
    	String urlString = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    	String reString = requestUrl(urlString, signParams);
    	
    	System.out.println(reString);
    }
    public static String getResponse(WXPay wxPay){
    	
    	return null;
    }
    //生成json字符串传给app端
    public static WXPay returnSign(String responseString) throws Exception{
    	
    	XMLSerializer xmlSerializer = new XMLSerializer(); 
    	@SuppressWarnings("static-access")
		String jsonString = xmlSerializer.readObject(responseString).toString();
    	WXPayReturn wxPayReturn = JSON.parseObject(jsonString,WXPayReturn.class);
    	SortedMap<String, String> sortedMap = new TreeMap<String, String>();
    	long ts = System.currentTimeMillis()/1000;
    	String tsTimestamp = ts+"";
    	System.out.println(tsTimestamp);
    	String timestamp = (tsTimestamp).toString();
    	System.out.println(timestamp);
    	sortedMap.put("appid", wxPayReturn.getAppid());
    	sortedMap.put("noncestr", getRandomString());
    	sortedMap.put("package", "Sign=WXPay");
    	sortedMap.put("partnerid", wxPayReturn.getMch_id());
    	sortedMap.put("prepayid", wxPayReturn.getPrepay_id());
    	sortedMap.put("timestamp", timestamp);
    	String	sign=MD5Util.MD5Encode(getSign(sortedMap), "utf-8").toUpperCase();
    	WXPay wxPay = new WXPay();
    	wxPay.setAppid(sortedMap.get("appid"));
    	wxPay.setPartnerId(sortedMap.get("partnerid"));
    	wxPay.setPrepayId(sortedMap.get("prepayid"));
    	wxPay.setNonceStr(sortedMap.get("noncestr"));
    	wxPay.setTimeStamp(new Timestamp(ts));
    	wxPay.setPackage_(sortedMap.get("package"));
    	wxPay.setSign(sign);
    	return wxPay;
    }   
    
}
