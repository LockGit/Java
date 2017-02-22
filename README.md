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

完成这个javaChat的时候，由于不熟悉java遇到了不少坑。
整个过程中，也经过了大量的Google和Baidu。
网上翻阅其他类似项目时，似乎不是这里哪里都有些问题。
在参考的同时我尽量完善了javaChat，当然还是有很多bug存在其中，
图形界面的绘制非常啰嗦复杂,我几乎复制了UI界面,节省了很多时间。

最后：
	不需要去重复造轮子,你要做的就是如何使用好现有的轮子！
	这个世界已经很美好了,不要妄想去改变世界！你要做的就是让这个世界再美好一点！
	写这个并不是要去做java，也不熟悉java，理解比coding更重要！！！
-+-+-
```



```
附带一个video 转 gif 的方法：
https://gist.github.com/dergachev/4627207
ffmpeg -i lock.mov -s 600x400 -pix_fmt rgb24 -r 10 -f gif - | gifsicle --optimize=3 --delay=3 > out.gif
javaChat的动图是由：GIF Brewery 3 生成
```


---
---
---
---
---
# base目录下的java文件为 了解Java 的 HelloWorld 程序

#1,Sort.java
a quick sort for java 

#2,Mul.java
base multiplication rhymes for java

#3,Calendar.java
```
Sun Mon Tue Wed Thu Fri Sat
					(11) 12
 13  14  15  16  17  18  19
 20  21  22  23  24  25  26
 27  28  29  30 
```

#4,MulitThreadRunnable.java   MultiThread.java
```
两种java多线程实现方式
1，MultiThread 通过继承Thread类实现
2，MulitThreadRunnable 通过实现 Runnable 接口实现
3，两种方式必须实现抽象方法 run
demo：
···
.....
I am sleep , time is: 2016-11-12 16:14:22:118
I am sleep , time is: 2016-11-12 16:14:22:118
I am sleep , time is: 2016-11-12 16:14:22:118
时间精确到毫
```
