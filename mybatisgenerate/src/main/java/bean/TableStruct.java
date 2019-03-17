package bean;

import java.util.List;

/**
 * 数据结构表
 * 
 * @author 卢家伟
 * @since JDK1.7
 * @history 2018年2月28日 新建
 */
public class TableStruct {

    private String tableName;// 表名
    private List<ColumnStruct> Columns;// 所有的列

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<ColumnStruct> getColumns() {
        return Columns;
    }

    public void setColumns(List<ColumnStruct> columns) {
        Columns = columns;
    }

    public TableStruct(String tableName, List<ColumnStruct> columns) {
        super();
        this.tableName = tableName;
        Columns = columns;
    }

    public TableStruct() {
        super();
    }

    @Override
    public String toString() {
        return "TableStruct [tableName=" + tableName + ", Columns=" + Columns + "]";
    }
}