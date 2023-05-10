package com.atguigu.api.statement;

import com.mysql.cj.jdbc.Driver;

import java.sql.*;

public class StatementQueryPart {
    public static void main(String[] args) throws SQLException {
        //1:注册驱动
        /**
         *  注册驱动
         *  依赖：驱动版本 8+ com.mysql.cj.jdbc .Driver
         *       驱动版本 5+ com.mysql.jdbc.Driver
         */

        DriverManager.registerDriver(new Driver());
        //2.获取连接
        /**
         * java程序要和数据库创建连接！
         * java程序，连接数据库，肯定是调用某个方法，方法也需要填入连接数据库的基本信息：
         *      数据库ip地址：localhost(127.0.0.1)
         *      数据库端口号：3306
         *      账号：root
         *      密码：030726aA
         *      连接数据库的名称：MySQL80*
         * 使用可视化工具
         *
         */


        /**
         * todo：
         *  参数1：ur1
         *      jdbc：数据库厂商名：//ip地址：part/数据库名
         *      jdbc:mysql://127.0.0.1:3306/atguigu
         *  参数2：username 数据库软件的账号 root
         *  参数3：password 数据库的密码 030726aA
         */

        //java.sql 接口 = 实现类
        Connection connection =DriverManager.
                getConnection("jdbc:mysql://127.0.0.1:3306/atguigu","root","030726aA");
        //3.创建statement
        Statement statement = connection.createStatement();
        //4.发送sql语句，并且获取返回结果
        String sql ="select * from t_user;";
        ResultSet resultSet = statement.executeQuery(sql);
        //5.进行结果集解析
        //看看有没有下一行数据，有就可以取
        while(resultSet.next()){
            int id = resultSet.getInt("id");
            String account = resultSet.getString("account");
            String password = resultSet.getString("password");
            String nickname = resultSet.getString("nickname");
            System.out.println(id+"--"+account+"--"+password+"--"+nickname);

        }
        //6.关闭资源
        resultSet.close();
        statement.close();
        connection.close();
    }
}
