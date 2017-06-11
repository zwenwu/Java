package WXPay;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;













import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hqu.domain.Order;
import com.hqu.model.RefundModel;
import com.hqu.realm.ShiroDbRealm;
import com.hqu.utils.MD5Util;

public class RefundApply {
	private static Logger logger = LoggerFactory.getLogger(ShiroDbRealm.class);
	private static final String Key="wvIfF9RGwIz9oMN8DbSYs8hGzCXkif7Q";
    private static final String appid = "wx9565fa1a8f53d654";
    private static final String mch_id = "1405186702";
    private static final String op_user_id = "1405186702";
    private static final String urlString = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    public static String refundOp(Order order) throws Exception{
    	int INT_total_fee = (int) (order.getZJ()*100);
    	int INT_refund_fee = INT_total_fee;
    	
    	String total_fee = INT_total_fee +"";
    	String refund_fee = INT_refund_fee +"";
    	String out_trade_no = order.getDDH()+"";
    	
    	String out_refund_no = out_trade_no;
    	SortedMap<String, String> signParams = new TreeMap<String, String>(); 
    	signParams.put("appid", appid);  //wx9565fa1a8f53d654
    	signParams.put("mch_id", mch_id);//1405186702 
    	String randomString = getRandomString();
        signParams.put("nonce_str",randomString);  //getRandomString()
        signParams.put("out_trade_no", out_trade_no);
        signParams.put("out_refund_no", out_refund_no);
        signParams.put("total_fee", total_fee);
        signParams.put("refund_fee", refund_fee);
        signParams.put("op_user_id", op_user_id);
        String	sign=MD5Util.MD5Encode(getSign(signParams), null).toUpperCase();
        RefundModel refundModel = new RefundModel();
        refundModel.setAppid(appid);
        refundModel.setMch_id(mch_id);
        refundModel.setNonce_str(randomString);
        refundModel.setOut_trade_no(out_trade_no);
        refundModel.setOut_refund_no(out_refund_no);
        refundModel.setTotal_fee(INT_total_fee);
        refundModel.setRefund_fee(INT_refund_fee);
        refundModel.setOp_user_id(op_user_id);
        refundModel.setSign(sign);        
        logger.debug(sign);
        RefundMoney refundMoney = new RefundMoney();
        String reString = refundMoney.sendPost(urlString, refundModel);        
    	return reString;
    }
    public static String refundSomeMoneyOp(Order order,int SomeMoney) throws Exception{
    	int INT_total_fee = (int) (order.getZJ()*100);
    	int INT_refund_fee = SomeMoney;
    	
    	String total_fee = INT_total_fee +"";
    	String refund_fee = INT_refund_fee +"";
    	String out_trade_no = order.getDDH()+"";
    	
    	String out_refund_no = out_trade_no;
    	SortedMap<String, String> signParams = new TreeMap<String, String>(); 
    	signParams.put("appid", appid);  //wx9565fa1a8f53d654
    	signParams.put("mch_id", mch_id);//1405186702 
    	String randomString = getRandomString();
        signParams.put("nonce_str",randomString);  //getRandomString()
        signParams.put("out_trade_no", out_trade_no);
        signParams.put("out_refund_no", out_refund_no);
        signParams.put("total_fee", total_fee);
        signParams.put("refund_fee", refund_fee);
        signParams.put("op_user_id", op_user_id);
        String	sign=MD5Util.MD5Encode(getSign(signParams), null).toUpperCase();
        RefundModel refundModel = new RefundModel();
        refundModel.setAppid(appid);
        refundModel.setMch_id(mch_id);
        refundModel.setNonce_str(randomString);
        refundModel.setOut_trade_no(out_trade_no);
        refundModel.setOut_refund_no(out_refund_no);
        refundModel.setTotal_fee(INT_total_fee);
        refundModel.setRefund_fee(INT_refund_fee);
        refundModel.setOp_user_id(op_user_id);
        refundModel.setSign(sign);        
        logger.debug(sign);
        RefundMoney refundMoney = new RefundMoney();
        String reString = refundMoney.sendPost(urlString, refundModel);        
    	return reString;
    }
    public static void main(String[] args) throws Exception{
    	
    	Order order = new Order();    	
    	order.setDDH(Long.valueOf(("2495")));
    	order.setZJ(1);
    	String res = refundOp(order);
    	System.out.println(res);
    	System.exit(0);
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
}
