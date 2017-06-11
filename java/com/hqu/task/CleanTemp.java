package com.hqu.task;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component("CleanTemp")
public class CleanTemp {
	
	@Scheduled(cron = "0 0 0 * * ?")//凌晨00:00清理图片的临时文件
	public void cleanImagetemp(){
		String rootpath=System.getProperty("springmvc.root");
		File tempCatalog = new File(rootpath+"/images/temp/");
		if(tempCatalog.exists()){
			for(File file:tempCatalog.listFiles()){
				long lastTime = file.lastModified();
				long timeDifference=System.currentTimeMillis()-lastTime;
				double tdf =((double)timeDifference)/(1000*60*60);
				if(tdf>24){
					file.delete();//上传的文件如果在临时文件夹中的存在时间超过了24小时就会被删除
				}
			}
		}
	}

}
