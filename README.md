# Easyweb

## 目标

1. 简单易用
2. 易于扩展

## 模块说明

1. easyweb: 核心实现
2. container: 基于jetty的web运行容器，可以指定App目录直接run
3. spring: 当有spring容器的时候，使用spring bean方式启动。将spring托管的bean注入到easyweb

## 快速入门

### 初始化

```
Easyweb.initialize()
```

常见初始化时机：

1. 如果使用了spring模块，则SpringBootstrap已经整合了处理。业务不需要额外处理
2. 如果使用servlet Filter机制，则在filter initialize的时候初始化
3. 直接使用main方法运行，则直接调用初始化即可

### 处理请求

```
try{
    Easyweb.process(HttpServletRequest,HttpServletResponse)
}catch(Exception e){
    //do your job
}
```

说明

1. Easyweb.process的出入参为Http Request和Response
2. 处理中有任何业务代码问题异常都将直接抛出，由容器应用捕获处理


