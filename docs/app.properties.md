# app.properties 说明

## 重要性

1. 用来识别目录是否是一个app根目录。Easyweb在扫描时，发现某个目录下有app.properties，则认为该目录是一个app跟目录，随后进行app的启动操作
2. app配置，如应用名称、web路径等等
3. 其他扩展配置，如velocity.noescape、数据源相关配置等等

## 配置说明


### 基本配置

* app.name：应用名称配置，必须
* app.web.path：web相对路径
* velocity.noescape：velocity配置不做转义的工具类
* pipeline.valves：请求正式处理之前可以通过实现Valve接口做请求预处理，值为bean名称，多个以英文逗号分隔

### ORM数据源相关配置

* ds.name：数据源名称，多个以英文逗号分隔
* ds.{name}.type：{name}数据源的类型，目前有simple（nutz自带，不建议线上使用）、dbcp（使用dbcp连接池的数据源）
* ds.{name}.driver：驱动类
* ds.{name}.url：数据库连接url
* ds.{name}.username：数据库用户名
* ds.{name}.password：数据库密码

## 配置示例
```
app.name = demo
app.web.path = src/demo/web

ds.name = local
ds.local.type = dbcp
ds.local.driver = org.sqlite.JDBC
ds.local.url = jdbc:sqlite:local.db
```
