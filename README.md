# 嵌入式设备管理系统

背景：对物联网兴趣浓厚和物联网设备如何管理的探究，外加自己想写个项目。
## 项目介绍：
* 项目整体为单机部署，分为主服务器+文件服务器。采用Nginx+Tomcat方式部署。使用树莓派设备模拟嵌入式设备，实现了对嵌入式设备的监控单一控制和复杂控制。

* 项目由个人在大四上学期独立完成，用时4星期左右。（前端1星期左右，后端+设备端3星期左右）。
* 项目技术栈：Maven+Nginx+Tomcat+SSM+Redis+MySQL+vsftpd+Linux
* 网站功能实现包括：**用户模块**（注册，登录，忘记密码，修改密码，个人中心）、**产品模块**（新增，删除，查询）、**设备管理模块**（新增，删除，查询，监控展示）、**设备控制模块**（引脚控制、设备软件/文件更新）、**文件仓库模块**（文件上传、删除）、**支付模块**。
* 嵌入式终端设备（树莓派）：采用Go语言编写并编译成ARM32和ARM64两个版本，实现对设备的信息采集和控制。理论上监控部分程序适用于所有Linux系统的设备。  
设备端项目地址: https://github.com/mrs47/Pi_Monitor

 