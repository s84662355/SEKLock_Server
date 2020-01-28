package socketServer;

import java.util.HashSet;
import java.util.Set;

public class SEKeyLock {
	
	public static final int UPDATE = 1;  
	public static final int SHARE = 2; 
	
	private Set<String> thread_name_set;
 
	private boolean islock = false;
	private long locktime ;
 
	private int waitlockcount = 0;
	
	private int lock_type = 0 ;
	
	private String lock_name;
	
	private int share_lock_count = 0;
	
	public SEKeyLock(String lock_name) {
		this.lock_name = lock_name;
		this.thread_name_set = new HashSet<String>();
		///this.thread_name_set.contains(o)
	}
	
	public Boolean getUpdateLock(String thread_name) {
		synchronized (this) {
			
			if(this.islock) {
				
				if(this.lock_type == this.UPDATE && this.thread_name_set.contains(thread_name)) {
					 System.out.print("获取同一个锁");
					 return true;
				}
				
				return false;
			}
				 
			
			    this.thread_name_set.add(thread_name);
			    this.islock = true;
			    this.lock_type = this.UPDATE;
			    this.locktime = System.currentTimeMillis();
			return true;	
		}
	}
	
	public Boolean UnLockUpdate(String thread_name) {
		synchronized (this) {
			
			if(!this.islock || this.lock_type != this.UPDATE) 
				return false; 
			
			if(!this.thread_name_set.contains(thread_name))
				return false; 
			
			this.islock = false;
			this.lock_type = 0;
			this.locktime = 0;
			this.thread_name_set.clear();
			this.share_lock_count = 0;
			return true;	
		}
		 
	}
	
    public Boolean getShareLock(String thread_name) {
    	synchronized (this) {
    		
    		if(this.lock_type == this.UPDATE)
    			return false;
    		
    		if(!this.islock) {
			    this.thread_name_set.add(thread_name);
			    this.islock = true;
			    this.lock_type = this.SHARE;
			    this.locktime = System.currentTimeMillis();
			    this.share_lock_count++;
			    
			    System.out.print("sssssss:::"+this.share_lock_count);
			    return true;
    		} 
    			
			if(this.thread_name_set.contains(thread_name)) {
				 System.out.print("获取同一个锁");
				 System.out.print( this.share_lock_count);
				 
				 System.out.print("sssssss:::"+this.share_lock_count);
				 return true;
			}
				 
			
		    this.thread_name_set.add(thread_name);
		    this.islock = true;
		    this.lock_type = this.SHARE;
		    this.locktime = System.currentTimeMillis();
		    this.share_lock_count++;
		    
		    System.out.print("sssssss:::"+this.share_lock_count);
		    return true;		
    							
			 
    	}
	}
    
    public Boolean UnLockShare(String thread_name) {
    	synchronized (this) {
    		
			if(!this.islock || this.lock_type != this.SHARE) 
				return false; 	
			if(!this.thread_name_set.contains(thread_name))
				return false; 
			
			this.share_lock_count--;
			if(this.share_lock_count<=0) {
				this.islock = false;
				this.lock_type = 0;
				this.locktime = 0;
				this.share_lock_count = 0;
				this.thread_name_set.clear();
			}else {
		 
				this.thread_name_set.remove(thread_name);
				
			}
			
			System.out.print("ooooo:::"+this.share_lock_count);
    	
			return true;	
			
		}
		
	}

	
	
	public Boolean lock(String thread_name ,  int lock_type ,long waittime)  {
	 
		long starttime = System.currentTimeMillis();
		this.waitlockcount++;
		while ( true ) {
			 
	 
			if(lock_type == this.SHARE) {
			    if(this.getShareLock(thread_name)) {
			    	this.waitlockcount--; 
			    	return true;
			    }
			}
			
			if(lock_type == this.UPDATE) {
				if(this.getUpdateLock(thread_name)) {
			    	this.waitlockcount--; 
			    	return true;	
				}
			}
			
 
			
			if(waittime>0){
				long nowtime = System.currentTimeMillis();
				if(nowtime-starttime>=waittime) { this.waitlockcount--; return false ;}				
			}else if (waittime == 0) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					this.waitlockcount--;
					return false;
				}
			}else {
				this.waitlockcount--;
				return false;
			}
		}		
	}
	
	
	public Boolean unlock(String thread_name) {
           
		if(this.lock_type == this.SHARE)
			return this.UnLockShare(thread_name);
		
		return this.UnLockUpdate(thread_name);
		 
	}
	
	
	public int getWaitlockcount() {
		return waitlockcount;
	}
	
 

	public boolean isIslock() {
		return islock;
	}

	public long getLocktime() {
		return locktime;
	}
	
	public String getLockName() {
		  return lock_name;
	}
	
	public int getLockType() {
		return lock_type;
	}
	
 
	
	
}
