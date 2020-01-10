当前项目是一个用户权限管理系统，包括用户模块，角色模块，权限模块和部门模块

同时包括登陆日志、系统任何增删改日志和在线用户统计还有系统JVM信息等，具体都是参照FEBS搭建的，

只是把redis和MySQL的部分全部交给了mongo处理

源码地址：https://github.com/wuyouzhuguli/FEBS-Vue



说说我这个项目的用法，首先是基于mongo副本集搭建的，可以找一个搭建mongo副本集的教材

为啥用副本集：1.冗余数据2.只有副本集支持事务

搭建完副本集：建一个cobot_shiro_web数据库，相当于MySQL的database

然后用导入mongo初始化脚本

## 2. 怎么运行？

web-user直接运行FebsApplication 就会打开登陆页面，输入账户和密码都是：xiyouyan

那service-user是干嘛的，就是 web-user一个带页面的服务，如果只想单纯引入一个服务，就可以像service-user一样用





