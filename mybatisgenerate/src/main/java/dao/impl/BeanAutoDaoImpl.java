package dao.impl;


import bean.ColumnStruct;
import bean.TableStruct;
import dao.BeanAutoDao;
import dao.GetTablesDao;
import util.*;

import java.util.Date;
import java.util.List;

public class BeanAutoDaoImpl implements BeanAutoDao {

    public static void main(String[] args) {
        StringBuffer importCon = new StringBuffer();
        String importDate = "import java.util.Date;\n";
        importCon.append("aaaaaa");
        importCon.append(importDate);
        System.out.println(importCon.indexOf(importDate));
    }
    // 从GetTablesDaoImpl中获得装有所有表结构的List
    GetTablesDao getTables = new GetTablesDaoImpl();
    List<TableStruct> list = getTables.getTablesStruct();

    // 通过表名、字段名称、字段类型创建Bean实体
    @Override
    public boolean createBean() {
        // 获得配置文件的参数
        // 项目路径
        String projectPath = ConfigUtil.apiProjectPath;
        // 是否生成实体类
        String beanFalg = ConfigUtil.beanFlag;
        // Bean实体类的包名
        String beanPackage = ConfigUtil.beanPackage;
        // 判断是否生成实体类
        if ("true".equals(beanFalg)) {
            // 将包名com.xxx.xxx形式，替换成com/xxx/xxx形成
            String beanPath = beanPackage.replace(".", "/");
            // Bean实体类的路径
            String path = projectPath + ConfigUtil.defultPath + beanPath;
            // 遍历装有所有表结构的List
            for (int i = 0; i < list.size(); i++) {
                // 文件名
                String fileName = NameUtil.fileName(list.get(i).getTableName()) + ConfigUtil.entitySuffix;
                // 获得每个表的所有列结构
                List<ColumnStruct> columns = list.get(i).getColumns();
                // (实体类）文件内容
                String packageCon = "package " + beanPackage + ";\n\n";
                StringBuffer importCon = new StringBuffer();
                importCon.append("import java.io.Serializable;\n\n");
                StringBuffer className = new StringBuffer();
                String date = DateUtil.dateformat(new Date(), "yyyy年MM月dd日");
                className.append("/**\n * \n * \n * @author \n * @since JDK1.7\n * @history "+date+" 新建\n */\n");
                className.append("@SuppressWarnings(\"serial\")\npublic class " + fileName + " implements Serializable {\n\n");
                StringBuffer classCon = new StringBuffer();
                StringBuffer gettersCon = new StringBuffer();
                StringBuffer settersCon = new StringBuffer();
                StringBuffer noneConstructor = new StringBuffer();
                StringBuffer constructor = new StringBuffer();
                String constructorParam = "";
                StringBuffer constructorCon = new StringBuffer();
                // 遍历List，将字段名称和字段类型写进文件
                for (int j = 0; j < columns.size(); j++) {
                    ColumnStruct column = columns.get(j);
                    // 变量名（属性名）
                    String columnName = NameUtil.columnName(column.getColumnName());
                    // 获得数据类型
                    String type = column.getDataType();
                    // 获得字段注释
                    String comment = column.getComment();
                    // 将mysql数据类型转换为java数据类型
                    String dateType = DataTypeUtil.getType(type);
                    // 有date类型的数据需导包
                    String importDate = "import java.util.Date;\n\n";
                    if ("Date".equals(dateType) && importCon.indexOf(importDate) == -1) {
                        importCon.append(importDate);
                    }

                    // 生成属性
                    classCon.append("\t /** "+comment+" */ \n");
                    classCon.append("\t" + "private" + " " + dateType + " " + columnName + ";\n");
                    // get、set的方法名
                    String getSetName = columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
                    // 生成get方法
                    gettersCon.append("\t" + "public" + " " + dateType + " " + "get" + getSetName + "(){\n"
                            + "\t\t" + "return" + " " + columnName + ";\n" + "\t" + "}\n");
                    // 生成set方法
                    settersCon.append("\t" + "public void" + " " + "set" + getSetName + "(" + dateType + " "
                            + columnName + "){\n" + "\t\t" + "this." + columnName + " = " + columnName + ";\n"
                            + "\t" + "}\n");
                    // 获得有参构造器参数
                    if (constructorParam == null || "".equals(constructorParam)) {
                        constructorParam = dateType + " " + columnName;
                    } else {
                        constructorParam += "," + dateType + " " + columnName;
                    }
                    // 获得有参构造器的内容
                    constructorCon.append("\t\t" + "this." + columnName + " = " + columnName + ";\n");
                }
                // 生成无参构造器
                noneConstructor.append(
                        "\t" + "public" + "\t" + fileName + "(){\n" + "\t\t" + "super();\n" + "\t" + "}\n");
                // 生成有参构造
                constructor.append("\t" + "public" + " " + fileName + "(" + constructorParam + "){\n" + "\t\t"
                        + "super();\n" + constructorCon + "\t" + "}\n");
                // 拼接(实体类）文件内容
                StringBuffer content = new StringBuffer();
                content.append(packageCon);
                content.append(importCon.toString());
                content.append(className);
                content.append(classCon.toString());
                content.append(gettersCon.toString());
                content.append(settersCon.toString());
                content.append(noneConstructor.toString());
                content.append(constructor.toString());
                content.append("}");
                FileUtil.createFileAtPath(path + "/", fileName + ".java", content.toString());
            }
            return true;
        }
        return false;
    }

}