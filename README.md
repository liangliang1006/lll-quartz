这是一个使用quartz实现动态的定时任务的代码
因为是内嵌的html页面，所以你不需要重新启动一个前端的应用，前端页面是基于element-ui+vue实现的，语法比较简单，有前端功底的人应该事半功倍
clone下代码之后，直接修改下数据库连接启动项目的同时就可以帮你自动创建表了
你应该会看见帮你生成了三张表，你无需担心，其中两张表是liquibase的日志表和锁表，有兴趣可以去了解下，你也可以去配置文件关闭liquibase的使用或去看我写的关于liquibase使用的文章
（https://blog.csdn.net/liang_336/article/details/83031467）了解他的使用


