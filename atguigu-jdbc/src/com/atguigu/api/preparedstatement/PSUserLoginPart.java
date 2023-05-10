package com.atguigu.api.preparedstatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import static java.lang.Class.forName;

/**
 * @author Steven
 * @ClassName PSUserLoginPart.java
 * @Description 使用预编译的statement完成用户登录
 * @createTime 2023-05-07 14:50
 *
 * todo：防止注入攻击|演示 ps的使用流程
 **/
public class PSUserLoginPart {
    public static void main(String[] args) throws Exception {
        //1.获取用户输入信息
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入账号");
        String account = scanner.nextLine();
        System.out.println("请输入你的密码");
        String password = scanner.nextLine();
        //2.ps的数据库流程
            //1、注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            //2、获取连接
        Connection connection = DriverManager.getConnection("jdbc:mysql:///atguigu?user = root&password = 030726aA");
        /**
         *
         * statement
         *          1.创建statement
         *          2.拼接sql语句
         *          3.发送SQL语句，并且获取返回结果
         *
         *preparedstatement
         *               1.编写SQL语句结构 不包括动态值部分的语句，动态值部分使用占位符？替代  注意：？只能替代动态值
         *               2创建preparedStatement，并且传入动态值
         *               3.动态值 占位符 赋值 ？ 单独赋值即可
         *               4.发送SQL语句即可，并获取返回结果
         *
          */
        //3、编写SQL语句结构
        String sql = "select * from t_user where account =? and password =?";
        //4.创建预编译statement并且设置SQL语句结果
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        //5.单独的占位符进行赋值
        /**
         * 参数1： index 占位符的位置  从左向右数 从1 开始 账号 ？ 1
         * 参数2：object 占位符的值 可以设置任何类型的数据，避免了我们拼接和类型更加丰富！
         */
            preparedStatement.setObject(1,account);
            preparedStatement.setObject(2,password);

        //6.发送SQL语句，并且返回结果
        //查询语句使用executeQuery() 非查询使用executeUpdate
        //statement.executeUpdate|executeQuery(String sql);
        //preparedStatement.executeUpdate|executeQuery();

        ResultSet resultSet = preparedStatement.executeQuery();


        //7.结果集解析
        if(resultSet.next()){
            System.out.println("登录成功");
        }else{
            System.out.println("登录失败");
        }

        //8.关闭资源
        resultSet.close();
        preparedStatement.close();
        connection.close();

    }
}
