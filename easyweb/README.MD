# easyweb框架说明

核心功能：

1. 支持框架内App化，框架本身可以理解为一个App容器；做到App之间独立部署
2. 使用Groovy+Velocity做view逻辑计算和页面渲染，融合webx和springmvc一些有用的特性。如layout、使用Annotation注解URI等等



## 本地开发

1. 下载container.zip
2. 解压container.zip
3. 使用 start.sh -debug 8787 -port 8080 -app /path/to/app