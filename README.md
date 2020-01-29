# SEKLock_Server
这个工具是因为我觉用redis加锁的话，太麻烦，性能太差了，所以19年初就萌生了想法，做了这个工具出来
，现在这个工具是基于之前的代码再进行修改，加入了共享锁


启动命令
java -jar SEKLock.jar test.ini.json


配置文件

[LockServer]
port = 90
thread_num = 10
 

[LockWeb]
port = 89

[Auth]
secretkey = 111111

LockServer.port 监听的端口 
LockServer.thread_num 线程池数 可以设置大一点例如100

LockWeb.port 暂时不需要理会

Auth.secretkey 密钥

lock_type 1  独占锁  2 共享锁

do:Lock|LockName:aaa|lock_type:1|WaitTime:1000|secretkey:111111

do:Lock|LockName:aaa|lock_type:2|WaitTime:1000|secretkey:111111

do:UnLock|LockName:aaa|secretkey:111111


data:获取锁成功|status:0


package socketServer;

public class ResponseStatus {
   static public int SUCCESS = 0;
   static public int PROTOCOL_DO_NOT_FIND = 1;
   static public int REQUEST_PROTOCOL_FAIL = 2;
   static public int REQUEST_DO_NOT_FIND = 3;
   static public int CONTROLLER_NOTFIND = 4;
   static public int CONTROLLER_CONSTRUCTOR_NOTFIND = 5;
   static public int CONTROLLER_CONSTRUCTOR_ACCESS = 6;
   static public int CONTROLLER_CONSTRUCTOR_PROBLEM = 7;
   static public int LOCK_FAIL = 8 ;
   static public int UNLOCK_FAIL = 9 ;
   static public int ATTRS_FAIL = 10;
   static public int RUN_FAIL = 10;
   static public int AUTH_FAIL = 11;
   static public int TOKEN_FAIL = 12;
}



后续会加入启动脚本 可以用后台的形式运行

还会加入一个web管理界面 有日志功能 ， 甚至会加入命令行管理

最终目标是可以实现分布式部署，因为现在是单进程 并发量有限

 

