package bean;

public class ColumnStruct {

    private String columnName;// 字段名称
    private String dataType;// 字段类型
    private String comment;//字段注释

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ColumnStruct(String columnName, String dataType,String comment) {
        super();
        this.columnName = columnName;
        this.dataType = dataType;
        this.comment = comment;
    }

    public ColumnStruct() {
        super();
    }

    @Override
    public String toString() {
        return "ColumnStruct [columnName=" + columnName + ", dataType=" + dataType + ", comment=" + comment + "]";
    }
}