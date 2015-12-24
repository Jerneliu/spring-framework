package com.hithinksoft.hrmgnt.tools

import groovy.text.SimpleTemplateEngine
import groovy.text.Template

/**
 * @author snowblink on 15/12/19.
 */

def degreeImportSql = 'INSERT INTO dictionary (dictionary_category, dictionary_name) VALUES(0, \'$value\');'
def jobImportSql = 'INSERT INTO dictionary (dictionary_category, dictionary_name) VALUES(1, \'$value\');'
def majorImportSql = 'INSERT INTO dictionary (dictionary_category, dictionary_name) VALUES(2, \'$value\');'
def schoolImportSql = 'INSERT INTO dictionary (dictionary_category, dictionary_name) VALUES(3, \'$value\');'
def cityImportSql = 'INSERT INTO dictionary (dictionary_category, dictionary_name) VALUES(4, \'$value\');'

def importSqls = [degreeImportSql, jobImportSql, majorImportSql, schoolImportSql, cityImportSql]
def root = this.class.getResource("/txts").getFile();

['degree.txt', 'job.txt', 'major.txt', 'school.txt','city.txt'].eachWithIndex { it, idx ->
    def dic = new File(root, it);
    def bindings = []
    dic.eachLine("GBK") { line ->
        if (!line.isEmpty()) {
            bindings.add([value: line])
        }
    }

    def sqlImporter = new SqlImporter(importSqls[idx], bindings)
    sqlImporter.dump()
}




class SqlImporter {
    private String sql
    private List<Map<String, String>> bindings
    private List<String> processedSqls = []
    private Template template;

    SqlImporter(String sql, List<Map<String, String>> bindings) {
        this.sql = sql
        this.bindings = bindings
    }


    def preProcess() {
        if (processedSqls.size() != 0) {
            return
        }

        if (template == null) {
            template = new SimpleTemplateEngine().createTemplate(this.sql);
        }

        bindings.each { it ->
            processedSqls.add(template.make(it))
        }
    }

    def execute() {
        preProcess();
    }

    def dump() {
        preProcess();
        processedSqls.each { it ->
            println it
        }
    }
}
