# springboot-stage
springboot脚手架

## 结构
   ----- springboot-stage         ------- 父工程\
   ----- springboot-stageapi      ------- api层 对外提供服务 \
   ----- springboot-stagebiz      ------- 业务层\
   ----- springboot-stagedao      ------- 持久层\
   ----- springboot-stagecommon   ------- 基础组件\
   ----- springboot-stageservice  ------- 工程启动层\
   
## 启动配置参数
该处配置的一般是个性化的配置，配置文件中的配置一般是不动的，以防经常变动而出错且增加维护成本\

-Dsimplelii.env.logger.level=INFO



## 集成简单API层日志打印


## 集成Mybatis
1. 使用provider注解实现
2. 在使用```SQLCondition```对象时，如果出现sql注入，也允许sql执行。\
    仅打印了sql注入的判断日志，请根据需要调整或者进行配置
      ```java 
      BaseEoUtil.returnUpdateWhereColumnNames(T obj){
     // ...
      if ((sb.length() > 0) && (!SqlUtil.isSpiteParams(sb.toString().toLowerCase()))) {
                  logger.error("Malice SQL Attack : {}", sb.toString());
              }
      // ...
      }
3. 推荐使用```SQLCondition```,如果使用普通的eo对象，where条件不能指定顺序，导致不能有效使用索引
4. 使用模糊查询，组件不进行%拼接
5. 该组件未集成redis缓存功能
6. 为了能更好的使用索引，where条件请使用```SqlCondition```对象
7. 使用组件时，尽量通过das对象，或者调用方法前进行必要参数的判断 
8. 在调用查询方法返回集合是，默认的是 id DESC 排序
9. ```SqlCondition```与EO对象映射表字段直接set值，不要混用。为方便使用尽量使用das对象调用方法




3. 

    1. 分页
    2. 日志开启
    3. 驼峰
    4. 多数据源待定
    5. AOP 
    6. 实例化bean的先后与关系
    7. redis
    8. es
    9. mq
    10. 工具类
    11. ThreadLocal
    12. nacos
    13. datasouce连接池
    14. uuid