package com.hqu.service;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/** 
 * @author WangWeiWei 
 * @email  1345352429@qq.com
 * @time   2016年9月26日 上午10:59:12 
 */
import com.hqu.domain.Message;
import com.hqu.domain.Site;
public interface MessageService {
    /**
    * 获取全部消息
    * @return
    */
    public Message getAllMessage();
    /**
     * 通过类型名称type查看消息
     * @return
     */
    public Message getTypeMessage(String type);
    /**
    * 根据用户账户来查找信息
    * @param receiver
    * @return
    */
    public List<Message> getMessageByReceiver(String receiver,int start,int pageSize);
    /**
    * 插入一条消息
    * @param message
    * @return
    */
    public int insertMessage(Message message);

    /**
    * 更新一条消息
    * @param message
    */
    public void updateMessage(Message message);

    /**
    * 根据流水号swiftNumber删除一条消息
    * @param username
    */
    public int deleteMessage(String XXLSH);

    int selectMessageCount(String JSR);
    /**
     * 更新一条消息状态
     * @param message
     */
    public int updateMessageStatus(int swiftNumber);
    
    //
    public List<Message> selectXXLXMC();//消息类型选项
    
    public List<Message> selectJSRLX();//接收人类型选项
    
    public List<Message> selectall(Timestamp startTime,Timestamp endTime);//查找时间内的消息
    
    public List<Message> selectconditions(Message message,Timestamp startTime,Timestamp endTime);//按条件查询
    
    public List<Message> selectpassenger();//接收人下拉框
    public List<String> selectJSR(String CKJBDM);//选出消息接收人
    
    public List<Message> selectdetail(String XXLSH);//查看某条消息的具体内容
    
    public List<Message> getnewMessage(String receiver);
}
