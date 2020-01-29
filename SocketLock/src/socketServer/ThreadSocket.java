package socketServer;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import Controller.Controller;
import Controller.ControllerFactory;
import util.LogUtil;
import util.TimeUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.*;

public class ThreadSocket extends Thread {	
	private Socket socket;
	private InputStream in;
	private OutputStream out;
	private String name ;
	private Set<String> lock_set;

	public ThreadSocket(Socket socket , String name){
		this.socket = socket;
		this.name = name ;
		this.lock_set = new HashSet<String>();
		
		try {
			this.in= this.socket.getInputStream();
			this.out  = socket.getOutputStream();	
			
			String response = "开始请求";
		 	out.write(response.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void dealWith() {
		System.out.println("进入线程循环");
        byte[] buffer = new byte[2048];
        int len = -1;
		while (true) {
		       try { 
		    	   len = in.read( buffer );
		           String msg= new String(buffer ,0,len);
		           Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		           Matcher m= p.matcher(msg);
		           msg = m.replaceAll("");
		           
		           System.out.print(msg);
		           
		           String requestlog =  "request:"+TimeUtil.getDate()+"---"+msg;
                   String response = ProtocolParser.getResponse(this.doThing(msg));
                   String responselog = "response:"+TimeUtil.getDate()+"---"+response;
                   
                   String logdataString = requestlog + "  @@@@  " + responselog;
                   
                  /// LogUtil.getLogUtil().add(logdataString);
                   
                   System.out.print(response);
                   
                   out.write(response.getBytes());
		        }catch (   Exception e) {	 
		        	System.out.print("sfsdfsfs");
		        	Iterator<String> iterator = this.lock_set.iterator() ;
		        	
		        	while (iterator.hasNext()) {
		        	     String next = iterator.next();
		        	     LockContainer.unlock(this.name, next);
		        	      
		        	}	        	
				    return ; 
				} 
		}
	}
	
	public Map<String, String>  doThing(String msg) {
		   try {
			        Map<String, String> hash_mapMap = ProtocolParser.getRequest(msg);
			        String controllerString = hash_mapMap.get("do");
			        if(controllerString == null)  throw new RunException(ResponseStatus.PROTOCOL_DO_NOT_FIND, "缺少do字段");
			        
			     	String secretkey = hash_mapMap.get("secretkey");
			     	if(secretkey == null || !secretkey.equals(Config.Auth_secretkey)) { 
			     		   Map<String,String>  response = new HashMap<String,String>();
			     		   response.put( "status" , ResponseStatus.AUTH_FAIL+"");
			     		   response.put("data", " 鉴权失败");
			     		   return response;
			     	}
			     	
			     	String LockName = hash_mapMap.get("LockName");
			     	
			     	if(LockName != null) {
			     		 this.lock_set.add(LockName);
			     		 System.out.print(LockName);
			     	}
			        
			        return  ControllerFactory.getController(controllerString)
			    		                     .setThreadName(this.name)
			    		                     .DoSomeThing(hash_mapMap);
			} catch (RunException e) {
				e.printStackTrace();
				Map<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("status",String.valueOf(e.getStatus()));
				hashMap.put("data",e.getMsg());
			    return hashMap;
			}
	}
	
	public void run() {
	  ThreadContainer.getContainer().increase(this.name, this);
	  this.dealWith();
	  ThreadContainer.getContainer().desc(this.name);
	}
 
}
