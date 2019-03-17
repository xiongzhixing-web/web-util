package dao;


import bean.TableStruct;

import java.util.List;

public interface GetTablesDao {

    //获得数据库的所有表名
    public List<String> getTablesName();

    //获得数据表中的字段名称、字段类型
    public List<TableStruct> getTablesStruct();
}
