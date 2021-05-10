# MS聊天室服务器
一个 Android 聊天软件的服务器程序<br/>

[Click me to go to the README in English](https://github.com/MrShieh-X/mscrserver/blob/master/README.md) <br/>
[点我转到本程序的更新日志](https://github.com/MrShieh-X/mscrserver/blob/master/update_logs-zh.md) <br/>
[点我转到MS聊天室](https://github.com/MrShieh-X/mschatroom) <br/>

## 版权
MrShiehX 拥有该程序的版权。<br/>
任何人都可以对此程序提出意见和建议。

## 版本
最新版本：<br/>
<b>1.3 (2021年5月10日)（对应客户端1.3）</b><br/>
历史版本：<br/>
<b>1.3 (2021年5月10日)（对应客户端1.3）</b><br/>
<b>1.2 (2021年4月13日)（对应客户端1.2）</b><br/>
<b>1.1 (2021年4月05日)（对应客户端1.1-1.1.1）</b><br/>
<b>1.0 (2021年1月31日)（对应客户端1.0）</b><br/>

## 本应用程序需要的软件配置有：
* Java

## 支持的语言
- 英语（美国）
- 简体中文（中国）

## 使用教程
首先把该程序放在一个目录中，然后在此目录中新建一个名为“mscrserver_config.json”的文件，此文件用于存放该程序的配置文件，它的内容是一个JSON对象，其JSON对象包含了以下内容：<br/>

|名称|含义|默认值|
|-------|------|:----:|
|port|服务器（或本地计算机）端口（用于通讯）|6553|
|serverAddress|服务器（或本地计算机）地址|127.0.0.1|
|databaseName|MySQL数据库名称|mschatroom|
|databaseUserName|MySQL数据库账号|root|
|databaseUserPassword|MySQL数据库密码|mypassword|
|databaseTableName|MySQL表格名称|users|
|isPrint|接收了消息或者发送了消息是否把内容打印出来|false|

![配置文件内容](https://gitee.com/MrShiehX/Repository/raw/master/33.png "配置文件内容")</br>

保存完成后，就可以使用`java -jar MSCRServer_1.3.jar`命令启动了。

## 关于作者
MrShiehX<br/>
- 职业：<br/>
学生<br/>
- 邮箱：<br/>
Bntoylort@outlook.com<br/>
- QQ：<br/>
3553413882（备注来意）<br/>

## 如果您在本应用程序发现任何BUG，或者有新的想法，欢迎发送邮件或添加我的QQ。
