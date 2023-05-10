package com.atguigu.api.statement;

import com.mysql.cj.jdbc.Driver;

import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

/**
 * @author Steven
 * @ClassName StatementUserLoginPart.java
 * @Description 模拟用户登录
 * @createTime 2023-05-05 17:33
 *
 *
 * todo：
 *      1.明确jdbc的使用流程 和 详细讲解内部设计api的方法
 *      2.发现问题，引出preparedStatement
 *
 *  todo:
 *      输入账号和密码
 *      进行数据库信息查询（t_uesr）
 *      反馈登录的成功和失败
 *      todo：
 *          1. 键盘输入时间，收集账号和密码信息
 *          2.注册驱动
 *          3.获取连接
 *          4.创建statement
 *          5.发送查询的SQL语句，并获取返回的结果
 *          6.结果判断 显示登录成功还是失败
 *          7.关闭资源
 *
 **/
public class StatementUserLoginPart {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        //1.获取用户输入信息
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入账号");
        String account = scanner.nextLine();
        System.out.println("请输入你的密码");
        String password = scanner.nextLine();

        //2.注册驱动
        /**
         * 方案一：
         * DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver())
         * 注意：8+ com.mysql.cj.jdbc.Driver
         *      5+ com.mysql.jdbc.Driver
         * 问题：会注册两次驱动
         * 一次是DriverManager,registerDriver()本身会注册一次
         * 第二次是在Driver.static{DriverManager.registerDriver()}静态代码块，也会注册一次
         * 解决：只想注册一次驱动
         *      1.只触发静态代码块即可！Driver
         *      静态代码块的触发：
         *      类加载机制：类加载的时刻，会触发静态代码块！
         *                  加载[class文件 -> jvm虚拟机的class对象]
         *                  连接[验证（检查文件类型） ->准备（静态变量默认值） ->解析（触发静态代码块）]
         *                  初始化（静态属性赋真实值）
         *  触发类加载：
         *      1.new 关键字
         *      2.调用静态方法
         *      3.调用静态属性
         *      4.接口 1.8 default默认实现
         *      5.反射
         *      6.子类触发父类
         *      7.程序的入口main
         */

        //方案1:
        //DriverManager.registerDriver(new Driver());

         //方案2：mysql 新版本的驱动 如果换成oracle的话得修改代码(方便数据库驱动的切换)
        // new Driver();
        //字符串->提取到外部的配置文件->可以在不改变代码的情况下·1，完成数据库的驱动切换
        //反射 字符串的Driver全限定符 可以引导外部的配置文件 ->xx.properties -> oracle->配置文件修改
        Class.forName("com.mysql.cj.jdbc.Driver");//通过反射的方式触发类加载，触发静态代码块的调用
        //2.获取数据库连接
        /**
         * getConnection(1,2,3)方法，是一个重载方法！
         * 允许开发者，用不同的形式传入数据库连接的核心参数！
         *
         * 核心属性：
         * 1.数据库软件所在的主机的ip地址：localhost / 127.0.0.1
         * 2.数据库软件所在的主机的端口号：3306
         * 3.连接的具体库：atguigu
         * 4.连接的账号
         * 5.连接的密码
         * 6.可选的信息 没有
         *
         *
         * 三个参数：
         *  String url          数据库软件所在的信息，连接具体的库，以及其他可选的信息！
         *                      语法：jdbc：数据库管理软件的名称[mysql、oracle]：//ip地址|主机名：part端口号/数据库名？key = value
         *                          &key = value 可选信息！
         *                      具体： jdbc:mysql://127.0.0.1:3306/atguigu = jdbc:mysql:///atguigu
         *                            省略了【本机地址】【3306端口号】
         *                            强调：必须是本机，并且端口号是3306方可省略 使用///
         * String user       数据库的账号  root
         * String password   数据库的密码  030726aA
         *
         *两个参数：
         * String url       ：此url和三个参数的url的作用一样！ 数据库ip，端口号，具体的数据库和可选信息
         * Properties info:   存放账号密码
         *                      Properties 类似于Map 只不过key = value 都是字符串形式的！
         *                      key user ：账号信息
         *                      key password：密码信息
         *
         * 一个参数：
         * String url       ：数据库ip，端口号，具体的数据库 可选信息（账号密码）
         *                  jdbc:数据库软件名字：//ip:port/数据库？key=value&key=value&key=value
         *                  jdbc：mysql://localhost:3306/atguigu?user = root&password=030726aA
         *url的路径属性可选信息：
         *             url?user=账号&password=密码
         */
        //三个参数
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/atguigu", "root", "030726aA");

        //两个参数
//         Properties info = new Properties();
//         info.put("user", "root");
//         info.put("password", "030726aA");
//        Connection connection = DriverManager.getConnection("jbdc:mysql://localhost:3306/atguigu", info);

        //一个参数
        //Connection connection1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/atguigu?user=root&password=030726aA");

        //3.创建发送SQL语句的statement对象
        //statement  可以发送SQL语句到数据库，并且获得返回结果（小汽车）
        Statement statement = connection.createStatement();


        //4.发送SQL语句（1.编写SQL语句 2.发送SQL语句）
        String sql = "select * from t_user where account = '" + account + "' and password = '" + password + "'";
        /**
         *   String分类：DDL（容器创建，修改，删除） DML（插入，修改，删除） DQL(查询) DCL（权限控制） TPL（事务控制语言）
         *
         *   参数：sql 非DQL
         *   返回：int
         *              情况1：DML 返回影响的行数 例如删除了三条数据 return 3；插入了两条return 2；修改了0条 return 0；
         *              情况2：非DML return 0；
         * int row = executeU[date(sql)
         *
         *参数：sql DQL
         *返回：returnSet 结果封装对象
         *
         *
         *
         */
       // int i = statement.executeUpdate(sql);
        ResultSet resultSet = statement.executeQuery(sql);


        //5.查询结果集解析 resultSet
        /**
         * Java是一种面向对象的思维，将查询的结果封装成了resultSet对象，我们应该理解，内部一定也是有行有列的！和小海豚的数据是一样！
         * resultSet -> 逐行获取数据，行->行的列数据
         *
         *A ResultSet object maintains a cursor pointing to its current row of data.
         *  Initially the cursor is positioned before the first row. The next method moves
         *  the cursor to the next row, and because it returns false when there are no more rows in the ResultSet object,
         *  it can be used in a while loop to iterate through the result set.
         *
         *想要进行数据的解析，需要我们进行两件事：1.移动游标指定获取数据行 2.获取指定数据行的列数据即可
         *
         * 1.游标移动问题
         *   resultSet 内部包含一个游标，指定当前行数据！
         *   默认游标指定的是第一行数据之前！
         *   我们可以调用next方法后移动一行游标！
         *   如果我们有很多行数据我们可以使用while（next）{获取每一行的数据}
         *
         *  boolean = next() true: 有更多行数据，并且向下移动一行
         *                      false：没有更多行数据，不一定！
         * todo:
         *      移动光标的方法有很多，但是只需要记next即可，配合while循环获取全部数据
         *      果next不满足你的需求 ，是查询的sql出现问题 不应该使用光标的别的方法来实现数据的查询
         * 2.获取列的数据问题（获取光标指定的行的列的数据）
         *
         *      resultSet。get类型（String columnLabel | int columnIndex）;
         *          columnLabel 列名 如果有别名使用别名  select * | (id,account,password,nickname)
         *                                           select id as aid , account as ac from
         *          columnIndex :列的下角标获取  从左向右 从1开始
         *
         * */
//        遍历数组
//        while(resultSet.next()){
//            int id  = resultSet.getInt(1);
//            String account1 = resultSet.getString("account");
//            String password1 = resultSet.getString(3);
//            String nickname = resultSet.getString("nickname");
//            System.out.println("account" +"="+account1);
//            System.out.println(id + "--" + account1 + "--" + password1 + "--" + nickname);
//
//
//        }

        //业务逻辑  实现登录  找到账号密码  如果输入的账号密码是正确的那么将可以查询到数据
        //所以此刻只要查询的列表中有数据就说明账号密码是正确的
        if(resultSet.next()){
            System.out.println("登录成功");
        }else{
                System.out.println("登录失败");

        }
        //6.关闭资源
        connection.close();
        statement.close();
        resultSet.close();


    }
}
