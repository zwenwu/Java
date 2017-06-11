package com.hqu.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.hqu.domain.Message;
import com.hqu.domain.Site;
import com.hqu.domain.SiteStatus;
import com.hqu.domain.User;


public interface MessageDao {
    /**
    * 返回全部消息
    * @return
    */
    //List<Message> selectAllMessage();
    /**
    * 返回全部消息类型
    * @return
    */
  //  List<MessageType> selectMessageType();
    /**
    * 按照流水号来查找消息
    * @param swiftNumber流水号
    * @return
    */
  //  List<Message> selectBySwiftNumber(String swiftNumber);
    /**
    * 按照消息类型来查找消息
    * @param type消息类型
    * @return
    */
      List<Message> selectByMessageType(String type);
    /**
    * 按时间间隔查找消息
    * @param startTime
    * @param endTime
    * @return
    */
  //  List<Message> selectByDateTime(Timestamp startTime,Timestamp endTime);
    /**
    * 按截止时间查找消息
    * @param timestamp
    * @return
    */
    //List<Message> selectByBeforeTime(Timestamp endTime);
    /**
    * 按起始时间查找消息
    * @param time
    * @return
    */
   // List<Message> selectByAfterTime(Timestamp startTime);
    /**
     * 按照发送者名称来查找信息
     * @param sender发送者名称
     * @return
     */
    // List<Message> selectBySender(String sender);
    /**
    * 按照接收者名称来查找信息
    * @param receiver
    * @return
    */
    List<Message> selectMessageByReceiver(String JSR,int start,int pageSize);
    
    int selectMessageCount(String JSR);
    /**
    * 组合查询
    * @param map
    * @return
    */
    List<Message> selectByConditions(Map<String, Object> map);
    /**
    * 更新消息
    * @param message
    * @return
    */
   // int updateMessage(Message message);
    /**
    * 删除消息
    * @param message
    * @return
    */
    int deleteMessage(String XXLSH);
    /**
     * 更新消息状态
     * @param id 消息流水号
     * @return
     */
     int updateMessageStatus(int id);
    //
     List<Message> selectXXLXMC();//消息类型选项
     
     List<Message> selectJSRLX();//接收人类型选项
     
     List<Message> selectall(Timestamp startTime,Timestamp endTime);//查找时间内的消息
     
     List<Message> selectconditions(Message message,Timestamp startTime,Timestamp endTime);//按条件查询
     
     List<Message> selectpassenger();//接收人下拉框
     
     List<String> selectJSR(String CKJBDM);//选出消息接收人
     
     int insertMessage(Message message);//插入消息
     
     List<Message> selectdetail(String XXLSH);//查看某条消息的具体内容
     
     List<Message> getnewMessage(String receiver);
}
