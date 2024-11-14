-------
- 时间:2024.11.14
### 关于解决mybatis mybatis-plus springboot版本冲突各种问题
在成功让前后端可以正常通信后（删除OAuth2等一系列包），尝试后端通过mybatis-plus与数据库交互，出现以下报错
```shell
org.springframework.beans.factory.BeanNotOfRequiredTypeException: Bean named 'ddlApplicationRunner' is expected to be of type 'org.springframework.boot.Runner' but was actually of type 'org.springframework.beans.factory.support.NullBean'
	at org.springframework.beans.factory.support.AbstractBeanFactory.adaptBeanInstance(AbstractBeanFactory.java:422) ~[spring-beans-6.1.14.jar:6.1.14]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:403) ~[spring-beans-6.1.14.jar:6.1.14]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:205) ~[spring-beans-6.1.14.jar:6.1.14]
	at org.springframework.boot.SpringApplication.callRunners(SpringApplication.java:770) ~[spring-boot-3.3.5.jar:3.3.5]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:342) ~[spring-boot-3.3.5.jar:3.3.5]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1363) ~[spring-boot-3.3.5.jar:3.3.5]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1352) ~[spring-boot-3.3.5.jar:3.3.5]
	at com.example.backend.BackendApplication.main(BackendApplication.java:11) ~[classes/:na]
```
通过查询可以发现这应该是版本号冲突导致不兼容问题（怎么maven整的跟pip一个德行）
找到一个issue：https://github.com/baomidou/mybatis-plus/issues/5867
执行，发现：
```xml
		<dependency>
			<groupId>com.baomidou</groupId>
			<artifactId>mybatis-plus-spring-boot3-starter</artifactId>
			<version>3.5.5</version>
		</dependency>
```
这个包爆红，也就是说找不到该版本
继续搜索，发现：
https://blog.csdn.net/KUKUKAKASSAN/article/details/130046698
![maven install](https://cdn.jsdelivr.net/gh/ToughMamba/PicRepo@main/20241114130145.png)
之前一直被maven导包找不到版本号困扰，现在知道怎么解决了