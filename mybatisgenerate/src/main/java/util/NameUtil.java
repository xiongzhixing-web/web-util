package util;

import org.apache.commons.lang3.StringUtils;

public class NameUtil {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        System.out.println("toLowerColumnName  ::::" + toLowerColumnName("TPushMessageDao"));
    }

    public static String fileName(String tableName) {
        String fileName = "";
        // 获得表名
        // 去掉表名的下划线
        String[] tablesName = tableName.split("_");
        for (int j = 0; j < tablesName.length; j++) {
            // 将每个单词的首字母变成大写
            tablesName[j] = tablesName[j].substring(0, 1).toUpperCase() + tablesName[j].substring(1);
            fileName += tablesName[j].replace("T", "");
        }
        return fileName;
    }

    public static String columnName(String columnName) {
        // 将字段名称user_name格式变成userName格式
        String colName = "";
        // 根据下划线将名字分为数组
        String[] columnsName = columnName.split("_");
        // 遍历数组，将除第一个单词外的单词的首字母大写
        for (int h = 0; h < columnsName.length; h++) {
            for (int k = 1; k < columnsName.length; k++) {
                columnsName[k] = columnsName[k].substring(0, 1).toUpperCase() + columnsName[k].substring(1);
            }
            // 拼接字符串以获得属性名（字段名称）
            colName += columnsName[h];
        }
        return colName;
    }

    public static String toLowerColumnName(String columnName) {
        if(StringUtils.isEmpty(columnName)){
            return "";
        } 
        return columnName.substring(0, 1).toLowerCase() +columnName.substring(1);
    }

}
