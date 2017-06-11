package com.hqu.serviceImpl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hqu.dao.MessageDao;
import com.hqu.dao.SiteDao;
import com.hqu.domain.Message;
import com.hqu.service.MessageService;

/** 
 * @author WangWeiWei 
 * @email  1345352429@qq.com
 * @time   2016年9月26日 上午11:18:28 
 */
@Service("MessageService")
public class MessageServiceImpl implements MessageService{
    @Resource
    private MessageDao messageDao;
    @Override
    public Message getAllMessage() {
        // TODO 自动生成的方法存根
        return null;
    }
    @Override
    public Message getTypeMessage(String type) {
        // TODO 自动生成的方法存根
        return null;
    }
    @Override
    public int insertMessage(Message message) {
        
        return messageDao.insertMessage(message);
    }
    @Override
    public void updateMessage(Message message) {
        // TODO 自动生成的方法存根
        
    }
    @Override
    public int deleteMessage(String XXLSH) {
        
        return messageDao.deleteMessage(XXLSH);
     }
	@Override
	public List<Message> getMessageByReceiver(String receiver,int start,int pageSize) {
		// TODO 自动生成的方法存根
		return messageDao.selectMessageByReceiver(receiver,start,pageSize);
	}
	@Override
	public int selectMessageCount(String JSR) {
		
		return messageDao.selectMessageCount(JSR);
	};
	@Override
	public int updateMessageStatus(int XXLSH) {
		// TODO 自动生成的方法存根
		int result=messageDao.updateMessageStatus(XXLSH);
		return result;
	};
	
	//
	public List<Message> selectXXLXMC(){//消息类型选项
		return messageDao.selectXXLXMC();
	}
	
	public List<Message> selectJSRLX(){//接收人类型选项
		return messageDao.selectJSRLX();
	}
	 public List<Message> selectall(Timestamp startTime,Timestamp endTime){//查找时间内的消息
		
		 return messageDao.selectall(startTime,endTime);
		 
	 }
	 
	 public List<Message> selectconditions(Message message,Timestamp startTime,Timestamp endTime){//按条件查询
		/* List<Message> listAll = new ArrayList<Message>();
		 Message message2=new Message();
		 List<Message> list = messageDao.selectconditions(message,startTime, endTime );
		 String xxnr="";
		 List<String> listXXNR= new ArrayList<String>();
		 for(Message pojo : list){
			 if(pojo.getXXNR().length()>10){
				 for(int i=0;i<10;i++){
					xxnr=xxnr+pojo.getXXNR().charAt(i);
				 }
				 System.out.println(xxnr);
				 listXXNR.add(xxnr);
				 xxnr="";
			 }
			 else{
				xxnr=pojo.getXXNR(); 
				listXXNR.add(xxnr);
				xxnr="";
			 }
		 }
		 message2.setListXXNR(listXXNR);
		 listAll.add(message2);*/
		 return messageDao.selectconditions(message,startTime, endTime );
	 }
	 
	 public List<Message> selectpassenger(){//接收人下拉框
		 
		 return messageDao.selectpassenger();
	 }

public List<String> selectJSR(String CKJBDM){//选出消息接收人
		 
		 return messageDao.selectJSR(CKJBDM);
	 } 

public List<Message> selectdetail(String XXLSH){//查看某条消息的具体内容
	
	return messageDao.selectdetail(XXLSH);
}
public List<Message> getnewMessage(String receiver){
	return messageDao.getnewMessage(receiver);
}
}
