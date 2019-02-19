

# springboot-mybatis-shiro-jwt-restful
springboot，mybatis多数据源，tkmapper，自定义mybatis generator，shiro，jwt，socket.io，restful，excel导出导入 空项目
  
# 简介

使用TestDB.sql文件可导入项目所需基础表，及表格导出示例表
## 基础结构
```shell
src
─main
    ├─java
    │  └─server
    │      │  WebApplication.java    //启动类
    │      │
    │      ├─config    //配置文件目录
    │      │  ├─cors    //跨域
    │      │  │      CorsConfig.java
    │      │  │
    │      │  ├─db    //数据库
    │      │  │      PrimaryDBConfig.java
    │      │  │      SecondaryDBConfig.java
    │      │  │
    │      │  ├─exception    //异常处理
    │      │  │      ExceptionController.java
    │      │  │      MyErrorController.java
    │      │  │
    │      │  ├─json    //json序列化
    │      │  │      SpringFastJsonConfig.java
    │      │  │
    │      │  ├─log    //日志输出
    │      │  │  ├─controller
    │      │  │  │      ControllerLogger.java
    │      │  │  │
    │      │  │  └─format
    │      │  │          EasyHighlightingCompositeConverter.java
    │      │  │          EasyPatternLayout.java
    │      │  │          ProcessIdClassicConverter.java
    │      │  │
    │      │  ├─security    //身份认证
    │      │  │      JavaJWT.java
    │      │  │      MyJWTFilter.java
    │      │  │      MyRealm.java
    │      │  │      ShiroConfig.java
    │      │  │
    │      │  └─tkmapper    //通用mapper
    │      │      └─common
    │      │              CommonMapper.java
    │      │
    │      ├─controller    //controller
    │      │  ├─example    //简单接口实例
    │      │  │      ExampleController.java
    │      │  │
    │      │  ├─login    //登录接口实例
    │      │  │      LoginController.java
    │      │  │
    │      │  └─sheet    //表格导出实例
    │      │          ComplexSheet.java
    │      │          SimpleSheet.java
    │      │
    │      ├─db    //数据库
    │      │  └─primary
    │      │      ├─dto    //dto自行创建前端需要的数据的实体类
    │      │      │  └─login
    │      │      │          LoginDTO.java
    │      │      │
    │      │      ├─mapper    //mapper类 自动生成
    │      │      │  └─basic
    │      │      │          PermissionMapper.java
    │      │      │          RoleMapper.java
    │      │      │          RolePermissionMapper.java
    │      │      │          UserMapper.java
    │      │      │          UserRoleMapper.java
    │      │      │
    │      │      └─model    //实体类 自动生成
    │      │          ├─basic
    │      │          │      Permission.java
    │      │          │      Role.java
    │      │          │      RolePermission.java
    │      │          │      User.java
    │      │          │      UserRole.java
    │      │          │
    │      │          └─sheet
    │      │                  ComplexSheetData.java
    │      │                  ComplexSheetForm.java
    │      │                  SimpleSheet.java
    │      │
    │      ├─service    //service
    │      │  ├─imp    //sevice接口
    │      │  │  ├─basic
    │      │  │  │      UserImp.java
    │      │  │  │
    │      │  │  └─login
    │      │  │          LoginImp.java
    │      │  │
    │      │  └─interf    //service实现类
    │      │      ├─basic
    │      │      │      UserService.java
    │      │      │
    │      │      └─login
    │      │              LoginService.java
    │      │
    │      └─tool    //自定工具类 及 测试用的小demo
    │          │  ExcelExport.java    //表格导出工具
    │          │  ExcelImport.java    //表格导入工具
    │          │  FileRec.java    //文件上传工具
    │          │  ListCompute.java    //集合类增加计算行工具
    │          │  Res.java    //返回值包装类
    │          │  TreeData.java    //树形结构数据构建类
    │          │
    │          └─Independent    //测试及部署用
    │              │  axios-file-download-MT.html
    │              │  docker简单使用.txt
    │              │  ExcelReadTool.java
    │              │  MapperXmlToDTOGenerator.java
    │              │  socketio-test.html
    │              │
    │              ├─linux
    │              │      install-java.sh
    │              │      服务注册
    │              │
    │              └─win
    │                  │  start-server.bat
    │                  │
    │                  ├─log
    │                  │      log_debug.log
    │                  │      log_debug_mybatis1.log
    │                  │      log_debug_mybatis2.log
    │                  │      log_debug_request.log
    │                  │      log_error.log
    │                  │      log_info.log
    │                  │      log_warn.log
    │                  │
    │                  └─winsw
    │                          ser-install.bat
    │                          ser-restart.bat
    │                          ser-start.bat
    │                          ser-stop.bat
    │                          ser-uninstall.bat
    │                          trans2Serv.exe
    │                          trans2Serv.xml
    │
    └─resources
        │  application.properties    //springboot配置文件
        │  generatorConfig.xml    //mybatis代码生成工具配置
        │  logback-spring.xml    //自定义日志输出配置
        │
        ├─mybatis    //mybatis相关
        │  │  mybatis.cfg.xml    //mybatis额外配置（可整合进application.properties或配置Bean中）
        │  │
        │  └─mapper    //mapper xml文件（自动生成）
        │      └─primary
        │         └─generate
        │                 UserMapper.xml
        └─static    //静态资源目录
            │  maven.txt    //maven声明周期介绍
            │  mybatis_test_db.sql    //测试用基础数据库表生成sql语句(mysql)
            │
            └─files
                    ces.png    //静态资源测试图片
```
## 快速启动测试项目
1、使用src/main/resources/static/mybatis_test_db.sql中的sql生成基础数据库表  
2、修改src/main/resources/static/application.properties中的数据库连接字符串为自己的数据库  
3、[非必须]修改src/main/resources/static/application.properties中server.port为未占用的端口  
4、启动src/main/java/server/WebApplication  
5、使用Postman等http请求测试工具对controller中给出的实例进行访问测试

## 增加方法
1、于数据库中建立相应表  
2、配置generatorConfig.xml中table标签的数据库名、表名  
3、运行mybatis generator  
4、生成  src/main/java/db/primary/mapper/XxxMapper.xml、  src/main/java/db/primary/model/Xxx.java、  src/main/resources/db/primary/mapper/primary/generate/mybatis/XxxMapper.xml  
5、新建相应service接口及实现类于 service/interf、service/imp中  
6、controller中写新方法、调用service、调用mapper、调用sql语句增删改查、返回结果

## 返回值格式
返回值格式 ：  
返回类型Res.java
```shell
{
    "msg":"",        附加消息,无附加消息加入空字符串
    "result":ture,   返执行结果，以判断执行成功或失败，失败将失败原因写至msg中
    "data":{}        返回数据
}
```
返回状态码：  
`200`：成功  
`401`：未登录，需前端将用户跳转至登录界面  
`403`：无权限，已登录用户无指定权限的接口时返回此状态码  
`500`：服务器内部错误，msg附加信息会显示简要异常输出信息  

## RestFul风格接口规范介绍

| http方法 | 资源操作 | 幂等 | 安全 |
| :------: | :------: | :------: | :------: |
| GET | SELECT | √ | √ |
| POST | INSERT | × | × |
| PUT | UPDATE | √ | × |
| DELETE | DELETE | √ | × | 

幂等性：对统一REST接口的多次访问，得到的资源状态是相同的。  
安全性：对该REST接口访问，不会使服务器资源的状态发生变化。  
以上规范仅供参考。  

#### 本项目使用以下方法 ,可统一参数传递与后台获取方式，有利于快速开发  

| http方法 | 资源操作 | 幂等 | 安全 |  
| :------: | :------: | :------: | :------: |   
| POST | SELECT | √ | √ | 
| PUT | INSERT | × | × |  
| PATCH | UPDATE | √ | × |  
| DELETE | DELETE | √ | × |   
