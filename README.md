# MSChatRoom Server
A Server Program for An Android Chat Application<br/>

[点我转到中文的README页面](https://github.com/MrShieh-X/mscrserver/blob/master/README-zh.md) <br/>
[Click me to go to the update log of this program](https://github.com/MrShieh-X/mscrserver/blob/master/update_logs.md) <br/>
[Click me to go to MSChatRoom](https://github.com/MrShieh-X/mschatroom) <br/>

## Copyright
MrShiehX own this program's copyright.<br/>
Anyone can take advices of this program to us.

## Version
The latest version: <br/>
<b>1.2 (Apr. 13, 2021) (Corresponding to client 1.2)</b><br/>
Historical version: <br/>
<b>1.2 (Apr. 13, 2021) (Corresponding to client 1.2)</b><br/>
<b>1.1 (Apr. 05, 2021) (Corresponding to client 1.1)</b><br/>
<b>1.0 (Jan. 31, 2021) (First version) (Corresponding to client 1.0)</b><br/>

## The software configuration required for this application is:
* Java

## Supported languages
- English (United States)
- Simplified Chinese (China)

## Use Tutorial
First put the program in a directory, and then create a new file named "mscrserver_config.json" in this directory, this file is a configuration file of this program, its content is a JSON object, the JSON object contains the following contents:<br/>

|Name|Meaning|Default Keys|
|-------|------|:----:|
|port|Server (or local computer) port (used for communication)|6553|
|serverAddress|Server (or local computer) address|127.0.0.1|
|databaseName|MySQL database name|mschatroom|
|databaseUserName|MySQL database account|root|
|databaseUserPassword|MySQL database password|mypassword|
|databaseTableName|MySQL table name|users|
|isPrint|Whether to print out the content after receiving the message or sending the message|false|

![Configuration File Content](https://gitee.com/MrShiehX/Repository/raw/master/33.png "Configuration File Content")</br>

After saving, you can use the `java -jar MSCRServer_1.2.jar` command to start.

## About Author
MrShiehX<br/>
- Occupation: <br/>
Student<br/>
- Email address: <br/>
Bntoylort@outlook.com<br/>
- QQ:<br/>
3553413882 (Remember to tell me why you want to add me)<br/>

## If you find any bugs in this application or have new ideas, please send an email or add my QQ.
