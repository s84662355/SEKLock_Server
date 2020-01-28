package socketServer;

import java.awt.List;
import java.util.HashMap;
import java.util.Map;

public class LockContainer {
	static private LockContainer iContainer = null;
	private Map<String, SEKeyLock> lockMap;
	
	private LockContainer() {
		this.lockMap =  new HashMap<String,  SEKeyLock>();
	}
	static  public LockContainer getContainer() {
		if(LockContainer.iContainer == null) LockContainer.iContainer = new LockContainer();
		return LockContainer.iContainer;
	}
	static public boolean lock(String thread_name,String key ,int lock_type,long waittime)   {
		return LockContainer.getContainer().SEKLock(thread_name, key, lock_type,  waittime);
	}
	static public boolean unlock(String thread_name,String key) {
		return LockContainer.getContainer().SEKUnLock(thread_name, key);
	}
	public synchronized  SEKeyLock set(String key) {
		SEKeyLock lock = this.lockMap.get(key);
		if( lock  != null)return lock;
		lock = new SEKeyLock(key);
		this.lockMap.put(key, lock);
		return this.lockMap.get(key);
	}
	public SEKeyLock get(String key) {
			SEKeyLock lock = this.lockMap.get(key);
			if( lock  != null) {
				return lock;
			} 
			return this.set(key);
	}
	public boolean  SEKLock(String thread_name,String key ,int lock_type ,long waittime )  {
		   SEKeyLock eklock = this.get(key);
		   return  eklock.lock(thread_name, lock_type , waittime );
	}
	
	public boolean  SEKUnLock(String thread_name,String key ) {
		 SEKeyLock eklock = this.get(key);
		 return eklock.unlock(thread_name);
	}
	
	
	public Map<String, SEKeyLock> getLocks(){
		return this.lockMap;
	}
	
 
}
