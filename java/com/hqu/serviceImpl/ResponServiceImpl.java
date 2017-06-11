package com.hqu.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hqu.utils.JSON;
import com.hqu.model.ResponseModel;
import com.hqu.service.ResponseService;
import com.hqu.utils.DesUtil;

@Service("responseService")
public class ResponServiceImpl implements ResponseService {

    @Autowired
    private ResponseModel responseModel;
	@Override
	public String returnData(boolean status, String msg, Object data,Boolean wechat) {
		if(msg.equals("请重新登录。。。")){
			responseModel.setHasLogin(false);
		}
		// TODO 自动生成的方法存根
		responseModel.setData(data);
		responseModel.setMsg(msg);
		responseModel.setStatus(status);
		String json = JSON.toJSONString(responseModel);
		System.out.println("转换为json");
		System.out.println(json);
		String deString = null;
		if(wechat){//如果是微信端就直接返回
			return json;
		}
		try {
			deString = DesUtil.encrypt(json);
			System.out.println("try=================");
		} catch (Exception e) {
			e.printStackTrace();
			responseModel.setData(null);
	        responseModel.setMsg("出错了");
	        responseModel.setStatus(false);
	        json = JSON.toJSONString(responseModel);
	        try {
                deString = DesUtil.encrypt(json);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
		}
		System.out.println("加密后返回值");
		System.out.println(deString);
		return deString;
	}
	
	public String returnFail(String msg,Boolean wechat) {
        return this.returnData(false, msg, null,wechat);
    }
	public String returnSucc(String msg,Boolean wechat) {
	    return this.returnData(true, msg, null,wechat);
	}
	public String returnData(Object data,Boolean wechat) {
	    return this.returnData(true, "", data, wechat);
	}

}
