package dao.impl;


import bean.ColumnStruct;
import bean.TableStruct;
import dao.GetTablesDao;
import dao.ServiceAutoDao;
import util.*;

import java.util.Date;
import java.util.List;

public class ServiceAutoDaoImpl implements ServiceAutoDao {


    // 获得配置文件的参数
    // 项目路径
    String projectPath = ConfigUtil.apiProjectPath;
    // 是否生成Service
    String serviceFalg = ConfigUtil.serviceFlag;
    // Service接口的包名
    String servicePackage = ConfigUtil.servicePackage;
    // Bean实体类的包名
    String beanPackage = ConfigUtil.beanPackage;
    
    // 从GetTablesDaoImpl中获得装有所有表结构的List
    GetTablesDao getTables = new GetTablesDaoImpl();
    List<TableStruct> list = getTables.getTablesStruct();

    // 通过表名、字段名称、字段类型创建Service接口
    @Override
    public boolean createService() {
        if(createReadService() && createWriteService()){
            return true;
        }
        return false;
    }
    
    /**
     * 
     * @return
     * @author 卢家伟  2018年3月2日 新建
     */
    private boolean createReadService() {
        if ("true".equals(serviceFalg)) {
            // 将包名com.xxx.xxx形式，替换成com/xxx/xxx形成
            String servicePath = servicePackage.replace(".", "/");
            // Service接口的路径
            String path = projectPath + ConfigUtil.defultPath + servicePath;
            // 遍历装有所有表结构的List
            for (int i = 0; i < list.size(); i++) {
                // 文件名
                String fileName = "I"+ NameUtil.fileName(list.get(i).getTableName()) + "ReadService";
                String beanName = NameUtil.fileName(list.get(i).getTableName()) + ConfigUtil.entitySuffix;
                // 获得每个表的所有列结构
                List<ColumnStruct> columns = list.get(i).getColumns();
                // 主键变量名（属性名）
                String columnName = NameUtil.columnName(columns.get(0).getColumnName());
                // 获得主键数据类型
                String type = columns.get(0).getDataType();
                // 将mysql数据类型转换为java数据类型
                String dateType = DataTypeUtil.getType(type);
                
                // (Service接口）文件内容
                String packageCon = "package " + servicePackage + ";\n\n";
                StringBuffer importCon = new StringBuffer();
                StringBuffer className = new StringBuffer();
                String date = DateUtil.dateformat(new Date(), "yyyy年MM月dd日");
                className.append("/**\n * \n * \n * @author \n * @since JDK1.7\n * @history "+date+" 新建\n */\n");
                className.append("public interface " + fileName + "{\n\n");
                StringBuffer classCon = new StringBuffer();
                
                // 生成导包内容
                importCon.append("import" + " " + beanPackage + "." + beanName + ";\n\n");
                // 有date类型的数据需导包
                if ("Date".equals(dateType)) {
                    importCon.append("import java.util.Date;\n\n");
                }
                // 有Timestamp类型的数据需导包
                if ("Timestamp".equals(dateType)) {
                    importCon.append("import java.sql.Timestamp;\n\n");
                }
                importCon.append("import java.util.List;\n\n");
                importCon.append("import java.util.Map;\n\n");
                
                // 生成接口方法
                classCon.append("\t//根据参数查询有效列表\n");
                classCon.append("\t" + "public List<"+beanName+"> queryListByParams(Map<String, Object> params);\n\n");
                
                classCon.append("\t//根据参数查询有效统计\n");
                classCon.append("\t" + "public Integer queryCountByParams(Map<String, Object> params);\n\n");
                
                classCon.append("\t//通过Id(主键)查询一条记录\n");
                classCon.append("\t" + "public " + beanName + " query"+beanName+"ById(" + dateType + " " + columnName + ");\n\n");
                
                // 拼接(Service接口）文件内容
                StringBuffer content = new StringBuffer();
                content.append(packageCon);
                content.append(importCon.toString());
                content.append(className);
                content.append(classCon.toString());
                content.append("\n}");
                FileUtil.createFileAtPath(path + "/", fileName + ".java", content.toString());
            }
            return true;
        }
        return false;
    }
    
    /**
     * 
     * @return
     * @author 卢家伟  2018年3月2日 新建
     */
    private boolean createWriteService() {
        if ("true".equals(serviceFalg)) {
            // 将包名com.xxx.xxx形式，替换成com/xxx/xxx形成
            String servicePath = servicePackage.replace(".", "/");
            // Service接口的路径
            String path = projectPath + ConfigUtil.defultPath + servicePath;
            // 遍历装有所有表结构的List
            for (int i = 0; i < list.size(); i++) {
                // 文件名
                String fileName = "I"+NameUtil.fileName(list.get(i).getTableName()) + "WriteService";
                String beanName = NameUtil.fileName(list.get(i).getTableName()) + ConfigUtil.entitySuffix;
                // 获得每个表的所有列结构
                List<ColumnStruct> columns = list.get(i).getColumns();
                // 主键变量名（属性名）
                String columnName = NameUtil.columnName(columns.get(0).getColumnName());
                // 获得主键数据类型
                String type = columns.get(0).getDataType();
                // 将mysql数据类型转换为java数据类型
                String dateType = DataTypeUtil.getType(type);
                
                // (Service接口）文件内容
                String packageCon = "package " + servicePackage + ";\n\n";
                StringBuffer importCon = new StringBuffer();
                StringBuffer className = new StringBuffer();
                String date = DateUtil.dateformat(new Date(), "yyyy年MM月dd日");
                className.append("/**\n * \n * \n * @author \n * @since JDK1.7\n * @history "+date+" 新建\n */\n");
                className.append("public interface " + fileName + "{\n\n");
                StringBuffer classCon = new StringBuffer();
                
                // 生成导包内容
                importCon.append("import" + " " + beanPackage + "." + beanName + ";\n\n");
                // 有date类型的数据需导包
                if ("Date".equals(dateType)) {
                    importCon.append("import java.util.Date;\n\n");
                }
                
                // 生成接口方法
                classCon.append("\t//添加一条完整记录\n");
                classCon.append("\t" + "public int insert"+beanName+"Record(" + beanName + " record);\n\n");
                classCon.append("\t//通过Id(主键)删除一条记录\n");
                classCon.append("\t" + "public int delete"+beanName+"ById(" + dateType + " " + columnName + ");\n\n");
                classCon.append("\t//按Id(主键)修改指定列的值\n");
                classCon.append("\t" + "public int update"+beanName+"ById(" + beanName + " record);\n\n");
                
                // 拼接(Service接口）文件内容
                StringBuffer content = new StringBuffer();
                content.append(packageCon);
                content.append(importCon.toString());
                content.append(className);
                content.append(classCon.toString());
                content.append("\n}");
                FileUtil.createFileAtPath(path + "/", fileName + ".java", content.toString());
            }
            return true;
        }
        return false;
    }
    
    

}
