package Controller;

import java.util.HashMap;
import java.util.Map;
import socketServer.LockContainer;
 
 
import socketServer.ResponseStatus;
import socketServer.RunException;
import util.LogUtil;

public class LockController extends Controller {
	
	private String LockName ; 
	private int lock_type;
	private long Waittime;

	@Override
	public Map<String, String> DoSomeThing(Map<String, String> hasMap ) throws RunException {
		   this.setParameter(hasMap.get("LockName"),hasMap.get("lock_type"),hasMap.get("WaitTime"));
		   Boolean  isLockBoolean = LockContainer.lock(this.threadName, this.LockName , this.lock_type , this.Waittime);
		   Map<String, String> hashMap = new HashMap<String, String>();
		   if(isLockBoolean) {
			   hashMap.put("status", String.valueOf(ResponseStatus.SUCCESS));
			   hashMap.put("data","获取锁成功");
		   }else {
			   hashMap.put("status", String.valueOf(ResponseStatus.LOCK_FAIL));
			   hashMap.put("data","获取锁失败");
		   }
		   
		//   LogUtil.getLogUtil().add("Lock", this.threadName, this.LockName,  this.Unlocktime, this.Waittime , arg, status, responsedata, createtime);
		   
		   
		   return hashMap;
	}
	
	
	private void setParameter(String LockName , String lock_type , String Waittime) throws RunException {
		   this.setLockName( LockName );
		   this.setLockType(lock_type); 
		   this.setWaittime( Waittime );	
	}
	
	private void setLockName(String LockName) throws RunException {
		   if(LockName == null) {
			   throw new RunException(ResponseStatus.ATTRS_FAIL, "缺少参数LockName");
		   }	
		   this.LockName = LockName;
	}
	
	private void setLockType(String lock_type) throws RunException {
	   if(lock_type == null) {
		   throw new RunException(ResponseStatus.ATTRS_FAIL, "缺少参数lock_type");
	   }
	   try {
		   this.lock_type = Integer.parseInt(lock_type);
	   } catch (Exception e) {
		   throw new RunException(ResponseStatus.RUN_FAIL, e.getMessage());
	   }
	}
	
	private void setWaittime(String Waittime) throws RunException {
		   if(Waittime == null) {
			   throw new RunException(ResponseStatus.ATTRS_FAIL, "缺少参数Waittime");
		   }	
		   try {
			   this.Waittime = Long.parseLong(Waittime);
		   } catch (Exception e) {
			   throw new RunException(ResponseStatus.RUN_FAIL, e.getMessage());
		   }	     
	}
}
