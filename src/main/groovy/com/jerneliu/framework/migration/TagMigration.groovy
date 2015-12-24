package com.hithinksoft.hrmgnt.migration

import groovy.sql.Sql

/**
 * @author XiangZiWei at 2015/12/10.
 */
//旧表中的base_type
def sql = Sql.newInstance(
        "jdbc:mysql://121.42.158.69:3306/resume_require?useUnicode=true&characterEncoding=UTF-8", // db url
        "root", // 用户名
        "ksdhc1031", // 密码
        "com.mysql.jdbc.Driver"// jdbc驱动
)
//新表
def sql2 = Sql.newInstance(
        "jdbc:mysql://hithinksoft.mysql.rds.aliyuncs.com:3306/hr-manager?useUnicode=true&characterEncoding=UTF-8", // db url
        "hr_manager", // 用户名
        "hr_manager123789456", // 密码
        "com.mysql.jdbc.Driver"// jdbc驱动
)

def list = sql.rows("SELECT base_type,base_words FROM resume_base")
def c = ""
for(i in 0 .. list.size()-1){
//tag list[i][0]  id list[i][1]
    if (list[i][0]==""){
        c=0;
    }else if(list[i][0]=="Java"){
        c=1;
    }else if(list[i][0]=="web"){
        c=2;
    }else if(list[i][0]=="Android"){
        c=4;
    }else if(list[i][0]=="IOS"){
        c=8;
    }else if(list[i][0]=="PHP"){
        c=16;
    }else if(list[i][0]=="net"){
        c=32;
    }else if(list[i][0]=="C"){
        c=64;
    }else if(list[i][0]=="C++"){
        c=128;
    }else if(list[i][0]=="COBOL"){
        c=256;
    }else if(list[i][0]=="UI"){
        c=512;
    }else if(list[i][0]=="Testing"){
        c=1024;
    }else if(list[i][0]=="BPO"){
        c=2048;
    }else{
        c=0;
    }
    sql2.executeUpdate("UPDATE resume SET tag = ${c} WHERE id = ${list[i][1]}")
    println("数据迁移成功，迁移条数："+list.size())
}

