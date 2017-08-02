# 基于Spring的一致性哈希redis缓存

![wen icon](https://fourwenwen.github.io/fww/image/logo100_100.jpg)

目前关于redis集群方案，官方方案是Redis cluster,Redis3.0以后正式提供。大家可以了解下。
虽然官方方案都有，并且网上也有很多关于这方面的内容，为什么还要自己造轮子。哈哈，程序员不久最喜欢造轮子。核心功能已经完成，但是会持续更新的。
功能简陋，代码难看，多多谅解。任意的意见和建议，欢迎随意与我沟通。
项目的Bug和改进点，可在GitHub上以issue的方式直接提交给我。

##代码获取
GitHub:

##ConsistentHash
写这个东西是为了实现一致性hash来控制redis集群.

##项目结构
spring-consistent-hash-cache 基于spring的一致性hash的redis集群方案的jar包。依赖进去项目，根据订制规则配置就可以使用。
spring-boot-example 使用spring-consistent-hash-cache的spring boot例子
spring-mvc-example 使用spring-consistent-hash-cache的spring mvc例子

### 需要知识点
- 基于spring开发的
- spring cache,基于注释的cache技术，它本质上不是一个具体的缓存实现方案，而是一个对缓存使用的抽象。
- redis,key-value存储系统。
- 一致性哈希算法，可自行去问度娘。

<a name="配置说明"></a>
### 配置说明
必须在项目resources目录下放置ch-cache.properties文件。
####redis服务器数量
-redis.count=2
####redis服务器0配置
-redis.host0=192.168.0.1
-redis.port0=6379
-reids.passwd0=test
-redis.db.size0=16
####redis服务器1配置
-redis.host1=192.168.0.2
-redis.port1=6380
-reids.passwd1=test
-redis.db.size1=16

### 使用方法（简单说明）
1. maven导入项目包
<dependency>
    <groupId>win.pangniu.four</groupId>
    <artifactId>spring-consistent-hash-cache</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>

2. 在项目resources文件加中添加ch-cache.properties文件。配置内容细看[配置说明](#配置说明)。

3. 在项目中进行配置
    * *Spring boot项目* 参考spring-boot-example项目中的配置，细看CacheConfig.class。
    * *Spring mvc项目* 参考spring-mvc-example项目中的配置，细看spring-context-rediscache.xml。
    
4. 功能使用。详情请看两个example项目的 controller和service层的代码。
