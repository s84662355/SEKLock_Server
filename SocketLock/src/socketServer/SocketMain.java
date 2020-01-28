package socketServer;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
 
import com.sun.net.httpserver.spi.*;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;

import WebServer.ApiHandler;
import WebServer.StaticHandler;
import util.LogUtil;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import org.dtools.ini.*;

import org.iq80.leveldb.*;
import static org.fusesource.leveldbjni.JniDBFactory.*;
import java.io.*;
 

public class SocketMain {
	public static void main(String[] args) throws IOException {
		
		/*
		Set<String> testSet = new HashSet<String>();
		
		String a = new String("123456") ;
		String b = new String("123456") ;
		
		testSet.add(a);
		System.out.print(testSet.size());
		
		System.out.print("---------");
		
		testSet.remove(b);
		
		System.out.print(testSet.size());
		
		return;
		*/
		
		
		 /*
		TreeSet<Integer> sortSet = new TreeSet<Integer>(new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				if(o1>o2) return 1;
				if(o1<o2) return -1;
				return 0;
			 
			}
		});
        sortSet.add(12111);
        sortSet.add(111122231);
        sortSet.add(11110000);
        sortSet.add(3333333);
        sortSet.add(6546575);
        sortSet.add(11);
        sortSet.add(11);
        System.out.print(sortSet.add(11));
        System.out.println(sortSet.toString());
       */
	 	 
	   IniFile iniFile= IniParser.getParser().setIni("base",args[0] ).get("base");
	   IniSection iniSection=iniFile.getSection("LockServer");
	   int LockServerPort = Integer.parseInt(iniSection.getItem("port").getValue()) ;
	   int LockServerThread_Num  = Integer.parseInt(iniSection.getItem("thread_num").getValue()) ;
		 
 
	   Config.LockServer_port = Integer.parseInt(iniSection.getItem("port").getValue()) ;
	   Config.LockServer_thread_num = Integer.parseInt(iniSection.getItem("thread_num").getValue()) ;
	   Config.LockWeb_port = Integer.parseInt( IniParser.getParser().get("base").getSection("LockWeb").getItem("port").getValue() ) ;
 	   Config.Auth_secretkey = IniParser.getParser().get("base").getSection("Auth").getItem("secretkey").getValue() ;
		
		 
		 /*
	     HttpServerProvider provider = HttpServerProvider.provider();  
	     HttpServer httpserver =provider.createHttpServer(new InetSocketAddress(Config.LockWeb_port), 100);
	   
	     httpserver.createContext("/api",new ApiHandler());
	     httpserver.createContext("/static",new StaticHandler());
	     httpserver.setExecutor(null);  
	     httpserver.start();
	     */
	    
	   ////  LogUtil.getLogUtil().start();
	     
 
 
		 ServerSocket serverSocket = new ServerSocket(Config.LockServer_port);
		 ExecutorService fixedThreadPool = Executors.newFixedThreadPool(Config.LockServer_thread_num );
		 System.out.print("服务器开启");
		 while(true){
			 Socket socket = serverSocket.accept();
			 socket.setKeepAlive(true);
			 ThreadSocket th = ThreadContainer.getContainer().createThread(socket,System.currentTimeMillis()+""+getRandomString(8))  ;
			 fixedThreadPool.execute(th);		 
		 }
		
		 
	}
	
	 public static String getRandomString(int length){
	     String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	     Random random=new Random();
	     StringBuffer sb=new StringBuffer();
	     for(int i=0;i<length;i++){
	       int number=random.nextInt(62);
	       sb.append(str.charAt(number));
	     }
	     return sb.toString();
	 }
	
	
	
}
 
 
