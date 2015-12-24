package com.hithinksoft.hrmgnt.migration
import groovy.sql.Sql
/**
 * @Author ckp on 2015/12/4.
 */
//原始数据库
def sql = Sql.newInstance(
        "jdbc:mysql://localhost:3306/test", // db url
        "root", // 用户名
        "root", // 密码
        "com.mysql.jdbc.Driver"// jdbc驱动
)
def list = sql.rows("select user_name,user_dept FROM resume_user;")
//目标数据库
def sqlMigration = Sql.newInstance(
        "jdbc:mysql://localhost:3306/aaaaaa", // db url
        "root", // 用户名
        "root", // 密码
        "com.mysql.jdbc.Driver"// jdbc驱动
)

//插入部门表数据
sqlMigration.execute("INSERT INTO department (department_id, department_name) VALUES ('003d760f-dd8b-4392-9e09-41f31c92e68c','国内业务部')")
sqlMigration.execute("INSERT INTO department (department_id, department_name) VALUES ('004da8fa-9116-4c5b-8be4-44db138688e8','销售部')")
sqlMigration.execute("INSERT INTO department (department_id, department_name) VALUES ('005aaa82-7e46-434b-a374-52cefd89eba0','人力资源中心')")
sqlMigration.execute("INSERT INTO department (department_id, department_name) VALUES ('007d2f01-1623-44df-9ad2-9bffa995b939','人才推进部')")
sqlMigration.execute("INSERT INTO department (department_id, department_name) VALUES ('00924dc5-ebdf-43d3-8849-58908f10320c','业务一部')")
sqlMigration.execute("INSERT INTO department (department_id, department_name) VALUES ('007d2f01-1623-44df-9ad2-9bffa995b958','业务二部')")
sqlMigration.execute("INSERT INTO department (department_id, department_name) VALUES ('0093e6de-be79-43ac-aa3c-e565d29269a2','业务三部')")
println("插入部门表数据成功")

//插入admin管理员数据
def params = ['admin', '''$2a$10$AUoZxGwTMuNThNqPZvhNhOL5eYgdZ77H3RJ0nLEkqdTsj2J4tDkDC''', true, 'admin', '005aaa82-7e46-434b-a374-52cefd89eba0']
sqlMigration.execute("insert into users (username, password, enabled, full_name, department_id) values (?, ?, ?, ?, ?)", params)
sqlMigration.execute("insert into authorities (username, authority) values ('admin', 'ROLE_ADMIN')")

//插入原始数据库用户数据
for (i in 0 .. list.size()-1){
        def username = list[i].getProperty("user_name")
        def departmentName = list[i].getProperty("user_dept")
        def departmentIdList = []
        departmentIdList = sqlMigration.firstRow("SELECT department_id FROM department WHERE department_name = ${departmentName}")
        def departmentId = departmentIdList[0]
        def params1 = [username, '''$2a$10$AUoZxGwTMuNThNqPZvhNhOL5eYgdZ77H3RJ0nLEkqdTsj2J4tDkDC''', true, username, departmentId]
        def params2 = [username,"ROLE_USER"]
        sqlMigration.execute("insert into users (username, password, enabled, full_name, department_id) values (?, ?, ?, ?, ?)", params1)
        sqlMigration.execute("insert into authorities (username, authority) values (?, ?)", params2)
        println("插入用户成功 "+"用户名："+username)
}
println("数据迁移成功，迁移条数："+list.size())
