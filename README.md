目录
==================

* [创建Spring Boot项目](#创建Spring Boot项目)
* [配置mybatis的依赖](#配置mybatis的依赖)
    * [pom.xml配置](#pom.xml配置)
    * [添加mybatis的自动生成](#添加mybatis的自动生成)
    * [application.properties配置](#application.properties配置)
    * [generatorConfig.xml配置](#在src/main/resources文件夹下创建generatorConfig.xml文件)
* [链接数据库查询](#链接数据库查询)
    * [controller](#创建controller和路由，查询广告列表和单个广告内容)
    * [service](#service)
    * [mapper](#resource目录下mapper对应文件编写sql)
    * [mapper配置](#mapper配置)
    

### 创建Spring Boot项目

File->New->Project选择Spring Initializr默认使用官方的https://start.spring.io/选择下一步。

groupId: com.axd
artifactId: spring_boot_mybatis
name: spring_boot_mybatis

Type: Maven Project 
Java Version: 1.8
packaging: Jar 

点击下一步选择web，创建一个新的项目

### 配置mybatis的依赖

#### pom.xml配置
```
<!-- Spring Boot Mybatis 依赖 -->
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>1.3.2</version>
</dependency>
<!-- MySQL 连接驱动依赖 -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.13</version>
</dependency>
```
#### 添加mybatis的自动生成
```
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>2.3.2</version>
    <configuration>
        <source>1.8</source>
        <target>1.8</target>
    </configuration>
</plugin>
<plugin>
    <groupId>org.mybatis.generator</groupId>
    <artifactId>mybatis-generator-maven-plugin</artifactId>
    <version>1.3.6</version>
    <configuration>
        <verbose>true</verbose>
        <overwrite>true</overwrite>
    </configuration>
    <dependencies>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.13</version>
        </dependency>
    </dependencies>
</plugin>
```

![pom.xml具体配置](https://github.com/wuyachao/spring_boot_mybatis/blob/master/pom.xml)

#### application.properties配置

```
spring.datasource.url = jdbc:mysql://192.168.80.129:3306/db_linkbiz
spring.datasource.username = root
spring.datasource.password = 123456
spring.datasource.driverClassName = com.mysql.cj.jdbc.Driver
mybatis.mapper-locations = classpath*:mapper/*Mapper.xml
```

#### 在src/main/resources文件夹下创建generatorConfig.xml文件

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
    <!-- 导入配置文件 -->
    <properties resource="application.properties"/>

    <!-- defaultModelType="flat" 设置复合主键时不单独为主键创建实体 -->
    <context id="default" defaultModelType="flat">

        <!-- 生成的POJO实现java.io.Serializable接口 -->
        <plugin type="org.mybatis.generator.plugins.SerializablePlugin" />

        <!--注释-->
        <commentGenerator>
            <!-- 将数据库中表的字段描述信息添加到注释 -->
            <property name="addRemarkComments" value="true"/>
            <!-- 注释里不添加日期 -->
            <property name="suppressDate" value="true"/>
        </commentGenerator>
        <!-- 数据库连接，直接通过${}读取application.properties里的配置 -->
        <jdbcConnection
                driverClass="com.mysql.cj.jdbc.Driver"
                connectionURL="jdbc:mysql://localhost:3306/db_linkbiz"
                userId="root"
                password="123456"/>

        <!-- 生成POJO对象，并将类放到com.axd.mybatis.model包下 -->
        <javaModelGenerator targetPackage="com.axd.mybatis.model" targetProject="src/main/java"></javaModelGenerator>
        <!-- 生成mapper xml文件，并放到resources下的mapper文件夹下 -->
        <sqlMapGenerator targetPackage="mapper"  targetProject="src/main/resources"></sqlMapGenerator>


        <!-- 生成mapper xml对应dao接口，放到com.axd.mybatis.mapper包下-->
        <javaClientGenerator targetPackage="com.axd.mybatis.mapper" targetProject="src/main/java" type="XMLMAPPER"></javaClientGenerator>

       <table schema="data" tableName="tb_%">
            <!-- 是否只生成POJO对象 -->
            <!--<property name="modelOnly" value="false"/>-->
            <!-- 数据库中表名有时我们都会带个前缀，而实体又不想带前缀，这个配置可以把实体的前缀去掉 -->
            <!--<generatedKey column="id" sqlStatement=""/>-->
            <domainObjectRenamingRule searchString="^tb" replaceString=""/>
        </table>

    </context>
</generatorConfiguration>
```
![具体](https://github.com/wuyachao/spring_boot_mybatis/blob/master/src/main/resources/generatorConfig.xml)

点击idea最右侧的Maven Projects => 点击mybatis-generator => 右键mybatis-generator:generate => Run Maven Build


### 链接数据库查询

#### 创建controller和路由，查询广告列表和单个广告内容
```
@RestController
public class AdvertContent {

    @Resource
    private AdvertService advertService;
    @GetMapping("/advert/content/{id}")
    public TbAdvertContent advertContents(@PathVariable int id){
        return advertService.getById(id);
    }

    @GetMapping("/advert/content/all")
    public List<TbAdvertContent> getAllAdvertContents(){
        return advertService.getAllAdvertContent();
    }
}
```

#### service

```
    @Resource
    private TbAdvertContentMapper advertContentMapper;

    public List<TbAdvertContent> getAllAdvertContent() {
        return advertContentMapper.selectAll();
    }

    public TbAdvertContent getById(Integer id) {
        return advertContentMapper.getById(id);
    }
```
#### resource目录下mapper对应文件编写sql

#### mapper配置
SpringBootMybatisApplication文件添加`@MapperScan("com.axd.mybatis.mapper")`
