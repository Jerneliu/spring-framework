package com.hithinksoft.hrmgnt.migration

import groovy.sql.Sql

import java.text.SimpleDateFormat

/**
 * @author XiangZiWei at 2015/12/10.
 */
//旧表中的uuid，内容，人，时间
def sql = Sql.newInstance(
        "jdbc:mysql://121.42.158.69:3306/resume_require?useUnicode=true&characterEncoding=UTF-8", // db url
        "root", // 用户名
        "ksdhc1031", // 密码
        "com.mysql.jdbc.Driver"// jdbc驱动
)
def list = sql.rows("SELECT * FROM resume_comments")
def a = []
def list2 = []
for(i in 0..list.size()-1){
    //原来简历uuid
    a = sql.firstRow("Select base_words FROM resume_base WHERE base_id = ${list[i].("comments_id")}")
//    println(a[0])
//    println(list[i].("comments_content"))
//    def b = list[i].("comments_time")
//    def c = list[i].("comments_username")
    String draft_date_posted = list[i].("comments_time")
    String str_post = draft_date_posted.toString()
    str_date_post = ""
    SimpleDateFormat sdf_draft = new SimpleDateFormat("MMM d, yyyy K:m:s a",Locale.ENGLISH)
    SimpleDateFormat sdf_convert = new SimpleDateFormat("yyyy-MM-dd HH:mm")
    if (!draft_date_posted.toString().equals(""))
    {
        Date dt_post = sdf_draft.parse(str_post)
        str_date_post = sdf_convert.format(dt_post)
    }
    String c;
    String uuid = UUID.randomUUID();
    c = uuid;
    list2[i] = [
            c, str_date_post, null, list[i].("comments_content"),
        list[i].("comments_username"), a[0]
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
    sql2.execute("insert into remark values (?,?,?,?,?,?)", params1)
    println("数据迁移成功，迁移条数："+list.size())
}

