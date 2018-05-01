### javaChat 一个纯java实现的LAN内聊天工具，sockts + 多线程 。
演示：(javaChat的源代码文件在src目录中)
![](https://github.com/LockGit/javaChat/blob/master/doc/javaChatPro.gif)

```
支持注册，登录，私信，广播消息。
1，启动Server会监听当前主机的8888端口 （netstat -an | grep 8888)
2，启动Client端会连接Server的8888端口，Server将启动一个线程处理当前sockts连接，
并将当前sockts连接add进一个集合，登录成功后会将user也add进一个集合（内存中）
3，用户的用户名与密码存在/tmp目录的指定文件中

共5个class文件:(图片由starUML导出)
```
![](https://github.com/LockGit/javaChat/blob/master/doc/javaChat.png)
---
```
-+-+-
➜  ~ java -version
java version "1.8.0_65"
Java(TM) SE Runtime Environment (build 1.8.0_65-b17)
Java HotSpot(TM) 64-Bit Server VM (build 25.65-b01, mixed mode)

理解比coding更重要！！！！！
-+-+-
```

```
附带一个video 转 gif 的方法：
https://gist.github.com/dergachev/4627207
ffmpeg -i lock.mov -s 600x400 -pix_fmt rgb24 -r 10 -f gif - | gifsicle --optimize=3 --delay=3 > out.gif
javaChat的动图是由：GIF Brewery 3 生成
```


### 使用 Swagger2 自动生成java接口文档 (仓库demo-spring文件夹)
```
多人协作的时候经常为了与前端接口对接而感到苦恼，大量的wiki文档，不停的更改，都是很多的工作量。
使用Swagger2增加文档注解的同时也增强了代码可读性,成本较低。

pom.xml 依赖增加
<dependency>
   <groupId>io.springfox</groupId>
   <artifactId>springfox-swagger2</artifactId>
   <version>2.8.0</version>
</dependency>
<dependency>
   <groupId>io.springfox</groupId>
   <artifactId>springfox-swagger-ui</artifactId>
   <version>2.8.0</version>
</dependency>

新增Swagger2.java
src/main/java/com/lock/demo/Swagger2.java

在controller中添加以下相关内容(至少包含这两个描叙)
自写一些注解即可，避免了多人协作时需要写大量的wiki文档，降低了沟通成本。
例：
@Api(value = "用户controller", description = "用户操作", tags = {"用户登录接口"})
@ApiOperation(value = "测试方法", notes = "需要提供SessionID与版本号AppVersion")

接口UI页面
http://127.0.0.1:8080/swagger-ui.html
```
![](https://github.com/LockGit/javaChat/blob/master/doc/api.gif)


### thrift RPC,以Java为服务端，PHP RPC调用 Java Service
```
brew install thrift
thrift -gen java thrift.thrift 生成java代码
将thrift生成的代码复制到 demo-spring/target/generated-sources/thrift,IntelliJ IDEA->mark as Source root
编写UserRpcService.java 实现thrift中定义的两个接口
编写服务RpcService.java 实现服务端端口监听与服务注册(如下)
```
```java
public class RpcService {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(RpcService.class);
    private final static Integer PORT = 6666;
    public static void main(String[] args) {
        try {
            LOGGER.info("start java rpc service ...");

            TServerSocket serverTransport = new TServerSocket(PORT);
            TServer.Args argsRpc = new TServer.Args(serverTransport);
            TProcessor processUserRpc = new UserRpc.Processor(new UserRpcService());
            TBinaryProtocol.Factory portFactory = new TBinaryProtocol.Factory(true, true);

            TMultiplexedProcessor processor = new TMultiplexedProcessor();
            processor.registerProcessor("userRpc", processUserRpc);

            argsRpc.protocolFactory(portFactory);
            TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));

//            简单的单线程服务模型，一般用于测试 (测试方法2)
//            TProcessor tprocessor = new UserRpc.Processor<UserRpc.Iface>(new UserRpcService());
//            TServerSocket serverTransport = new TServerSocket(PORT);
//            TServer.Args tArgs = new TServer.Args(serverTransport);
//            tArgs.processor(tprocessor);
//            tArgs.protocolFactory(new TBinaryProtocol.Factory());
//            TServer server = new TSimpleServer(tArgs);

            LOGGER.info("start listen port " + PORT);
            server.serve();
        } catch (Exception e) {
            LOGGER.info("have exception, msg is:" + e.getMessage());
        }

    }
}
```

```
生成客户端代码：thrift -gen php thrift.thrift
php调用java测试：
```
```php
define('THRIFT_PATH','php生成的gen-php路径');
include_once THRIFT_PATH . '/Types.php';
include_once THRIFT_PATH . '/UserRpc.php';

$socket = new \Thrift\Transport\TSocket('192.168.1.106', 6666);
$transport = new \Thrift\Transport\TBufferedTransport($socket, 1024, 1024);
$protocol = new \Thrift\Protocol\TBinaryProtocol($transport);
$loginProtocol = new \Thrift\Protocol\TMultiplexedProtocol($protocol, "userRpc");
$loginClient = new \lock\rpc\UserRpcClient($loginProtocol);
$transport->open();
echo $loginClient->login('lock', 'test');
$transport->close();
```
**显示:**
`my rpc service test,you name is:lock,you passwd is:test,from java thrift service`
--
**php RPC调用java test Pass**

