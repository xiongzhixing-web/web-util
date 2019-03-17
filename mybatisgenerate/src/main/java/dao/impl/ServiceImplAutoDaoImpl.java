package dao.impl;


import bean.ColumnStruct;
import bean.TableStruct;
import dao.GetTablesDao;
import dao.ServiceImplAutoDao;
import util.*;

import java.util.Date;
import java.util.List;

public class ServiceImplAutoDaoImpl implements ServiceImplAutoDao {

    // 获得配置文件的参数
    // 项目路径
    String projectPath = ConfigUtil.serviceProjectPath;
    // 是否生成Service
    String serviceImplFalg = ConfigUtil.serviceImplFlag;
    // Service接口的包名
    String serviceImplPackage = ConfigUtil.serviceImplPackage;
    // Bean实体类的包名
    String beanPackage = ConfigUtil.beanPackage;
    // Service接口的包名
    String servicePackage = ConfigUtil.servicePackage;
    // Dao接口的包名
    String daoPackage = ConfigUtil.daoPackage;
    
    // 从GetTablesDaoImpl中获得装有所有表结构的List
    GetTablesDao getTables = new GetTablesDaoImpl();
    List<TableStruct> list = getTables.getTablesStruct();

    // 通过表名、字段名称、字段类型创建ServiceImpl实现类
    @Override
    public boolean createServiceImpl() {
        if(createReadServiceImpl() && createWriteServiceImpl()){
            return true;
        }
        return false;
    }
    private boolean createReadServiceImpl() {
        
        if ("true".equals(serviceImplFalg)) {
            // 将包名com.xxx.xxx形式，替换成com/xxx/xxx形成
            String serviceImplPath = serviceImplPackage.replace(".", "/");
            // Service接口的路径
            String path = projectPath + ConfigUtil.defultPath + serviceImplPath;
            // 遍历装有所有表结构的List
            for (int i = 0; i < list.size(); i++) {
                // 文件名
                String tableTransName = NameUtil.fileName(list.get(i).getTableName());
                String fileName = tableTransName + "ReadServiceImpl";
                String serviceName = "I"+tableTransName + "ReadService";
                String lowerServiceName = NameUtil.toLowerColumnName(serviceName);
                String beanName = tableTransName + ConfigUtil.entitySuffix;
                String daoName = tableTransName + "Dao";
                String lowerDaoName = NameUtil.toLowerColumnName(daoName);
                // 获得每个表的所有列结构
                List<ColumnStruct> columns = list.get(i).getColumns();
                // 主键变量名（属性名）
                String columnName = NameUtil.columnName(columns.get(0).getColumnName());
                // 获得主键数据类型
                String type = columns.get(0).getDataType();
                // 将mysql数据类型转换为java数据类型
                String dateType = DataTypeUtil.getType(type);
                
                // (ServiceImpl实现类）文件内容
                String packageCon = "package " + serviceImplPackage + ";\n\n";
                StringBuffer importCon = new StringBuffer();
                StringBuffer className = new StringBuffer();
                String date = DateUtil.dateformat(new Date(), "yyyy年MM月dd日");
                className.append("/**\n * \n * \n * @author \n * @since JDK1.7\n * @history "+date+" 新建\n */\n");
                className.append("@Service(\""+lowerServiceName+"\")\npublic class " + fileName + " implements " + serviceName + "{\n\n");
                StringBuffer classCon = new StringBuffer();
                
                // 生成导包内容
                importCon.append("import org.springframework.beans.factory.annotation.Autowired;\n");
                importCon.append("import org.springframework.stereotype.Service;\n");
                importCon.append("import " + servicePackage + "." + serviceName + ";\n\n");
                importCon.append("import" + " " + beanPackage + "." + beanName + ";\n\n");
                importCon.append("import" + " " + daoPackage + "." + daoName + ";\n\n");
                // 有date类型的数据需导包
                if ("Date".equals(dateType)) {
                    importCon.append("import java.util.Date;\n\n");
                }
                importCon.append("import java.util.List;\n\n");
                importCon.append("import java.util.Map;\n\n");
                
                // 生成Dao属性
                classCon.append("\t@Autowired\n\tprivate " + daoName + " " + lowerDaoName + ";\n\n");
                
                // 生成实现方法
                classCon.append("\t//根据参数查询有效列表\n");
                classCon.append("\tpublic List<"+beanName+"> queryListByParams(Map<String, Object> params){\n");
                classCon.append("\t\treturn " + lowerDaoName + ".queryListByParams(params);\n\t}\n\n");
                
                classCon.append("\t//根据参数查询有效统计\n");
                classCon.append("\t" + "public Integer queryCountByParams(Map<String, Object> params){\n");
                classCon.append("\t\treturn " + lowerDaoName + ".queryCountByParams(params);\n\t}\n\n");
                
                classCon.append("\t//通过Id(主键)查询一条记录\n");
                classCon.append("\t" + "public " + beanName + " query"+beanName+"ById(" + dateType + " " + columnName + "){\n");
                classCon.append("\t\treturn " + lowerDaoName + ".query"+beanName+"ById(" + columnName + ");\n\t}\n\n");
                
                // 拼接(ServiceImpl实现类）文件内容
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
    private boolean createWriteServiceImpl() {
        if ("true".equals(serviceImplFalg)) {
            // 将包名com.xxx.xxx形式，替换成com/xxx/xxx形成
            String serviceImplPath = serviceImplPackage.replace(".", "/");
            // Service接口的路径
            String path = projectPath + ConfigUtil.defultPath + serviceImplPath;
            // 遍历装有所有表结构的List
            for (int i = 0; i < list.size(); i++) {
                // 文件名
                String tableTransName = NameUtil.fileName(list.get(i).getTableName());
                String fileName = tableTransName + "WriteServiceImpl";
                String serviceName = "I"+tableTransName + "WriteService";
                String lowerServiceName = NameUtil.toLowerColumnName(serviceName.substring(1));
                String beanName = tableTransName + ConfigUtil.entitySuffix;
                String daoName = tableTransName + "Dao";
                String lowerDaoName = NameUtil.toLowerColumnName(daoName);
                // 获得每个表的所有列结构
                List<ColumnStruct> columns = list.get(i).getColumns();
                // 主键变量名（属性名）
                String columnName = NameUtil.columnName(columns.get(0).getColumnName());
                // 获得主键数据类型
                String type = columns.get(0).getDataType();
                // 将mysql数据类型转换为java数据类型
                String dateType = DataTypeUtil.getType(type);
                
                // (ServiceImpl实现类）文件内容
                String packageCon = "package " + serviceImplPackage + ";\n\n";
                StringBuffer importCon = new StringBuffer();
                StringBuffer className = new StringBuffer();
                String date = DateUtil.dateformat(new Date(), "yyyy年MM月dd日");
                className.append("/**\n * \n * \n * @author \n * @since JDK1.7\n * @history "+date+" 新建\n */\n");
                className.append("@Service(\""+lowerServiceName+"\")\npublic class " + fileName + " implements " + serviceName + "{\n\n");
                StringBuffer classCon = new StringBuffer();
                
                // 生成导包内容
                importCon.append("import org.springframework.beans.factory.annotation.Autowired;\n");
                importCon.append("import org.springframework.stereotype.Service;\n");
                importCon.append("import " + servicePackage + "." + serviceName + ";\n\n");
                importCon.append("import" + " " + beanPackage + "." + beanName + ";\n\n");
                importCon.append("import" + " " + daoPackage + "." + daoName + ";\n\n");
                // 有date类型的数据需导包
                if ("Date".equals(dateType)) {
                    importCon.append("import java.util.Date;\n\n");
                }
                
                // 生成Dao属性
                classCon.append("\t@Autowired\n\tprivate " + daoName + "\t" + lowerDaoName + ";\n\n");
                
                // 生成实现方法
                classCon.append("\t//添加一条完整记录\n" + "\t@Override\n\tpublic int insert"+beanName+"Record(" + beanName
                        + " record){\n\t\treturn\t" + lowerDaoName + ".insert"+beanName+"Record(record);\n\t}\n\n");
                classCon.append("\t//通过Id(主键)删除一条记录\n" + "\t@Override\n\tpublic int delete"+beanName+"ById(" + dateType + " " + columnName
                        + "){\n\t\treturn\t" + lowerDaoName + ".delete"+beanName+"ById(" + columnName + ");\n\t}\n\n");
                classCon.append("\t//按Id(主键)修改指定列的值\n" + "\t@Override\n\tpublic int update"+beanName+"ById(" + beanName
                        + " record){\n\t\treturn\t" + lowerDaoName + ".update"+beanName+"ById(record);\n\t}\n\n");
                
                // 拼接(ServiceImpl实现类）文件内容
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
