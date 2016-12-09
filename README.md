##日志归档系统
使用java编写的liunx日志归档系统，相比于传统的复制脚本的方式，此系统更加灵活。只需在一台机器上安装java,mysql（保存配置信息）即可使用本系统管理任意集群上的日志归档，其他机器无需安装和复制任何软件。
注意：受管理的机器远程ssh端口必须为22，用户名、密码需要一样。

### 安装配置
在mysql中创建数据库archive并导入sql文件
``` xml
doc/archivelog.sql
``` 
修改application.properties中相关配置
``` xml
server.port=8011  #访问端口 http://localhost:8011
archivelog.username=crmuser   #ssh用户名
archivelog.password=1qaz2wsx  #ssh密码
...   #mysql相关配置
```

####示例:

- 配置管理


点击参数设置菜单，设置配置参数
![](https://github.com/qhts/archivelog/blob/master/src/main/resources/static/img/settings.png)  

- 服务器管理


点击服务器管理，添加服务器并选择使用的配置
![](https://github.com/qhts/archivelog/blob/master/src/main/resources/static/img/server.png)
- 历史查看


点击归档历史，可以查看最近10次的归档记录，可以点击立即执行开始压缩（默认每周日凌晨1点执行一次）。
![](https://github.com/qhts/archivelog/blob/master/src/main/resources/static/img/history.png)
