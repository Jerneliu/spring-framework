package com.hithinksoft.hrmgnt.migration

import groovy.sql.Sql

/**
 * @author XiangZiWei creat at 2015/12/7.
 */
//原始数据
def sql = Sql.newInstance(
        "jdbc:mysql://121.42.158.69:3306/resume_require?useUnicode=true&characterEncoding=UTF-8", // db url
        "root", // 用户名
        "ksdhc1031", // 密码
        "com.mysql.jdbc.Driver"// jdbc驱动
)
def list2 = []
def list = sql.rows("SELECT * FROM resume_base")
for(i in 0 .. list.size()-1 ) {
    def a = sql.rows("Select * FROM resume_degree WHERE degree_uuid = ${list[i].("base_first_degree")} ")
    def b = sql.rows("Select * FROM resume_ability WHERE ability_uuid = ${list[i].("base_final_degree")} ")
    def c = sql.rows("Select * FROM resume_user WHERE user_id = ${list[i].("user_id")}")
    list2[i] = [
            list[i].("base_words"),null,null,null,null,list[i].("base_email"),
            list[i].("base_phone"),null,b[0].("final_degree"),b[0].("final_degree_end"), b[0].("final_degree_start"),
            b[0].("final_degree_major"), b[0].("final_degree_school"), a[0].("first_degree"), a[0].("first_degree_end"),
            a[0].("first_degree_start"), a[0].("first_degree_major"), a[0].("first_degree_school"), list[i].("base_gender"),
            list[i].("base_name"), null, list[i].("base_native_place"), null, null, null, null, null, null,
            b[0].("ability_skill"), null, b[0].("ability_bussiness"), b[0].("ability_experience_year"),c[0].("user_name")
    ]
}
//目标数据
def sql2 = Sql.newInstance(
        "jdbc:mysql://hithinksoft.mysql.rds.aliyuncs.com:3306/hr-manager?useUnicode=true&characterEncoding=UTF-8", // db url
        "hr_manager", // 用户名
        "hr_manager123789456", // 密码
        "com.mysql.jdbc.Driver"// jdbc驱动
)
for (i in 0 .. list.size()-1){
    def params1 =list2[i]
    sql2.execute("insert into resume values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", params1)
}
println("数据迁移成功，迁移条数："+list.size())
