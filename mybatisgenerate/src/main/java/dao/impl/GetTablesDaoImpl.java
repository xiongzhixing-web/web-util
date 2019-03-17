package dao.impl;


import bean.ColumnStruct;
import bean.TableStruct;
import dao.GetTablesDao;
import util.DataSourceUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetTablesDaoImpl extends DataSourceUtil implements GetTablesDao {

    // 获得数据库的所有表名
    @Override
    public List<String> getTablesName() {
        List<String> tables = new ArrayList<String>();
        String sql = "show tables";
        ResultSet rs = this.query(sql);
        try {
            while (rs.next()) {
                // 将获得的所有表名装进List
                tables.add(rs.getString(1));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return tables;
    }

    // 获得数据表中的字段名称、字段类型
    @Override
    public List<TableStruct> getTablesStruct() {
        // 获得装有所有表名的List
        List<String> tables = this.getTablesName();
        String sqls = null;
        // 装所有的表结构（表名+字段名称+字段类型）
        List<TableStruct> tablesStruct = new ArrayList<TableStruct>();
        for (int i = 0; i < tables.size(); i++) {
            sqls = "show full columns from " + tables.get(i).toString();
            ResultSet rs = this.query(sqls);
            // 装所有的列结构(字段名称+字段类型)
            List<ColumnStruct> list = new ArrayList<ColumnStruct>();
            try {
                while (rs.next()) {
                    ColumnStruct cs = new ColumnStruct(rs.getString(1), rs.getString(2),rs.getString(9));
                    // 找到一列装进List
                    list.add(cs);
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // 遍历完一张表，封装成对象
            TableStruct ts = new TableStruct(tables.get(i).toString(), list);
            // 将对象（一张表）装进集合
            tablesStruct.add(ts);
        }
        return tablesStruct;
    }
}
