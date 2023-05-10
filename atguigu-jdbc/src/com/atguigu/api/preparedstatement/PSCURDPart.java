package com.atguigu.api.preparedstatement;

import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Steven
 * @ClassName PSCURDPart.java
 * @Description 使用preparedStatement进行t_user表的CURD动作
 * @createTime 2023-05-07 17:40
 **/
public class PSCURDPart {
    @Test
    public void testInsert() throws Exception {
        /**
         *
         * t_user插入一条数据
         *      account    test
         *      password   test
         *      nicknname  二狗子
         */



        //1.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //2.获取连接
        Connection connection = DriverManager.getConnection("jdbc:mysql:///atguigu", "root", "030726aA");
        //3.编写SQL语句结果，动态值的部分使用？代替
        String sql = "insert into t_user(account,password,nickname)Values(?,?,?);";
        //4.创建preparedStatement，并且传入SQL语句结果
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
        //5.占位符赋值
        preparedStatement.setObject(1,"test");
        preparedStatement.setObject(2,"test");
        preparedStatement.setObject(3,"二狗子");
        //6.发送SQL语句
        //语句是DML语句 使用 preparedStatement.executeUpdate();
        int rows = preparedStatement.executeUpdate();
        //7.输出结果
        if(rows>0){
            System.out.println("数据插入成功");
        }else{
            System.out.println("数据插入失败");
        }
        //8.关闭资源
        preparedStatement.close();
        connection.close();



    }

    @Test
    public void testUpdata() throws Exception {
        //1.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");

        //2.获取连接
        Connection connection = DriverManager.getConnection("jdbc:mysql:///atguigu", "root", "030726aA");
        //3.编写SQL语句结果，动态值的部分使用？代替
        String sql = "Update t_user set nickname = ? where id=?;";
        //4.创建preparedStatement，并且传入SQL语句结果
         PreparedStatement preparedStatement = connection.prepareStatement(sql);
        //5.占位符赋值
        preparedStatement.setObject(1,"三狗子");
        preparedStatement.setObject(2,3);
        //6.发送SQL语句
        int rows = preparedStatement.executeUpdate();
        //7.输出结果
        if(rows>0){
            System.out.println("修改成功");
        }else{
            System.out.println("修改失败");
        }
        //8.关闭资源
        preparedStatement.close();
        connection.close();
    }

    @Test
    public void testDelete() throws Exception {
        //1.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //2.获取连接
        Connection connection = DriverManager.getConnection("jdbc:mysql:///atguigu", "root", "030726aA");
        //3.编写SQL语句结果，动态值的部分使用？代替
        String sql = "delete from t_user where id = ?;";
        //4.创建preparedStatement，并且传入SQL语句结果
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        //5.占位符赋值
        preparedStatement.setObject(1,3);
        //6.发送SQL语句
        int i = preparedStatement.executeUpdate();
        //7.输出结果
        if(i>0){
            System.out.println("删除成功");
        }else{
            System.out.println("删除失败");
        }
        //8.关闭资源
        preparedStatement.close();
        connection.close();

    }

    /**
     * 需求：查询所以用户信息，并且封装到一个List<Map>List集合中
     *
     *
     *
     * @throws Exception
     */
    @Test
    public void testSelect() throws Exception {
        //1.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //2.获取连接
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atguigu", "root", "030726aA");
        //3.编写SQL语句结果，动态值的部分使用？代替
        String sql = "select id,account,password,nickname from t_user;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        //4.创建preparedStatement，并且传入SQL语句结果
        //5.占位符赋值
        //省略
        //6.发送SQL语句
        ResultSet resultSet = preparedStatement.executeQuery();
        //7.结果集解析
        /**
         *todo: 回顾
         *  resultSet: 有行和有列！获取数据的时候，一行一行数据！
         *              内部有一个游标，默认指向数据的第一行之前！
         *              我们可以利用next（）方法移动游标！指向数据行！
         *              获取表中的列数据
         */
        List<Map> list = new ArrayList<>();

        //获取列的信息对象
        //todo: metaDate 装的当前结果集列的信息对象！（它可以获取列的名称根据下角标，可以获取列的数量）
        ResultSetMetaData metaData = resultSet.getMetaData();//把表的信息传给metaData
        //有了它以后，我们可以水平遍历列！
        int columnCount = metaData.getColumnCount();//使用metaData中的getcolumnCount（）方法获得有多少列

        while(resultSet.next()){
            Map map = new HashMap();
            //一行数据对应一个map

            //纯手动输入值！ map.put(key,value)
//            map.put("id",resultSet.getInt("id"));
//            map.put("account",resultSet.getString("account"));
//            map.put("password",resultSet.getString("password"));
//            map.put("nickname",resultSet.getString("nickname"));


            //注意从下标1开始 并且小于等于总列数
            for (int i = 1; i <= columnCount; i++) {

                Object value = resultSet.getObject(i);//获取第i列的值
                //获取指定列下角标的列的名称！ResultSetMetaData

                //select *[列名] |xxx_xxx_xxx_xxx_xxx as xxx
                //getColumnLabel:会获取别名，如果没有写别名才是列的名称 不要使用 getColumnName 只会获取列的名称
                String columnLabel = metaData.getColumnLabel(i);


                //map.put(key,value)
                map.put(columnLabel,value);

            }

            //一行数据的所有列全部存到了map中
            //将map存储到集合中即可
            list.add(map);
        }
        System.out.println("list = " + list);

        //8.关闭资源
        resultSet.close();
        preparedStatement.close();
        connection.close();
    }
}
