package WXPay;

import java.util.HashMap;

public class getXmldata {
	public static String buildUnifiedOrderReq(HashMap<String,String> payParams){
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
        payStr +=    "<trade_type>JSAPI</trade_type>";
        payStr +=    "<sign>"+payParams.get("sign")+"</sign>";
        payStr +="</xml>";
        return payStr;
    }
}
