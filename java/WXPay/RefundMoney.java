package WXPay;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.security.*;
import java.security.cert.CertificateException;
public class RefundMoney {
	//连接超时时间，默认10秒
    private int socketTimeout = 10000;
    //传输超时时间，默认30秒
    private int connectTimeout = 30000;
    //表示请求器是否已经做了初始化工作
    private boolean hasInit = false;
	//请求器的配置
    private RequestConfig requestConfig;

    //HTTP请求器
    private CloseableHttpClient httpClient;
    
    public RefundMoney() throws UnrecoverableKeyException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException, IOException{
    	init();
    }
    private void init() throws IOException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException {

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream("C:/cert/apiclient_cert.p12");//加载本地的证书进行https加密传输C:/Users/Jack/Desktop/cert/apiclient_cert.p12
        try {
            keyStore.load(instream, "1405186702".toCharArray());//设置证书密码
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, "1405186702".toCharArray())
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[]{"TLSv1"},
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

        httpClient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();

        //根据默认超时限制初始化requestConfig
        requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();

    }
	 public String sendPost(String url, Object xmlObj) throws IOException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException {

		 	if (!hasInit) {
	            init();
	        }
	        String result = null;

	        HttpPost httpPost = new HttpPost(url);

	        //解决XStream对出现双下划线的bug
	        XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));

	        //将要提交给API的数据对象转换成XML格式数据Post给API
	        String postDataXML = xStreamForRequestPostData.toXML(xmlObj);


	        //得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
	        StringEntity postEntity = new StringEntity(postDataXML, "UTF-8");
	        httpPost.addHeader("Content-Type", "text/xml");
	        httpPost.setEntity(postEntity);

	        //设置请求器的配置
	        httpPost.setConfig(requestConfig);


	        try {
	            HttpResponse response = httpClient.execute(httpPost);

	            HttpEntity entity = response.getEntity();

	            result = EntityUtils.toString(entity, "UTF-8");

	        } catch (Exception e) {
	            System.out.println("http get throw Exception");

	        } finally {
	            httpPost.abort();
	        }

	        return result;
	    }
}
