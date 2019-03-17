package dao.impl;


import bean.ColumnStruct;
import bean.TableStruct;
import dao.*;
import util.*;

import java.util.Date;
import java.util.List;

public class DaoAutoDaoImpl implements DaoAutoDao {

    // 从GetTablesDaoImpl中获得装有所有表结构的List
    GetTablesDao getTables = new GetTablesDaoImpl();
    List<TableStruct> list = getTables.getTablesStruct();

    // 通过表名、字段名称、字段类型创建Dao接口
    @Override
    public boolean createDao() {
        // 获得配置文件的参数
        // 项目路径
        String projectPath = ConfigUtil.serviceProjectPath;
        // 是否生成Dao
        String daoFalg = ConfigUtil.daoFlag;
        // Dao接口的包名
        String daoPackage = ConfigUtil.daoPackage;
        // Bean实体类的包名
        String beanPackage = ConfigUtil.beanPackage;
        if ("true".equals(daoFalg)) {
            // 将包名com.xxx.xxx形式，替换成com/xxx/xxx形成
            String daoPath = daoPackage.replace(".", "/");
            // Dao接口的路径
            String path = projectPath + ConfigUtil.defultPath + daoPath;
            // 遍历装有所有表结构的List
            for (int i = 0; i < list.size(); i++) {
                String tableName = list.get(i).getTableName();
                String tableTransName = NameUtil.fileName(tableName);
                // 文件名
                String fileName = tableTransName + "Dao";
                String lowerFileName = NameUtil.toLowerColumnName(fileName);

                String beanName = tableTransName + ConfigUtil.entitySuffix;
                // 获得每个表的所有列结构
                List<ColumnStruct> columns = list.get(i).getColumns();
                // 变量名（属性名）
                String columnName = NameUtil.columnName(columns.get(0).getColumnName());
                // 获得数据类型
                String type = columns.get(0).getDataType();
                // 将mysql数据类型转换为java数据类型
                String dateType = DataTypeUtil.getType(type);

                // (Dao接口）文件内容
                String packageCon = "package " + daoPackage + ";\n\n";
                StringBuffer importCon = new StringBuffer();
                StringBuffer classCon = new StringBuffer();

                // 生成导包内容
                importCon.append("import org.springframework.stereotype.Repository;\n\n");
                importCon.append("import " + beanPackage + "." + beanName + ";\n\n");
                importCon.append("import com.yunji.base.olddao.BasicDao;\n\n");
                // 有date类型的数据需导包
                String importDate = "import java.util.Date;\n\n";
                if ("Date".equals(dateType) && importCon.indexOf(importDate) == -1) {
                    importCon.append(importDate);
                }
                importCon.append("\n\n");
                importCon.append("import java.util.List;\n");
                importCon.append("import java.util.Map;\n\n");
                
                StringBuffer className = new StringBuffer();
                String date = DateUtil.dateformat(new Date(), "yyyy年MM月dd日");
                className.append("/**\n * \n * \n * @author \n * @since JDK1.7\n * @history "+date+" 新建\n */\n");
                className.append("@Repository(value = \"" + lowerFileName + "\")\npublic class " + fileName + " extends BasicDao{\n\n");
                // 生成接口方法 
                classCon.append("\t//添加一条完整记录\n" + "\t@SuppressWarnings(\"deprecation\")\n");
                classCon.append("\t" + "public int insert"+beanName+"Record(" + beanName + " record){\n");
                classCon.append("\t\t" + "return (Integer) getSqlMapClientTemplate().insert(\""+tableName+".insertRecord\", record);\n\t}\n\n");
                
                classCon.append("\t//通过Id(主键)删除一条记录\n" + "\t@SuppressWarnings(\"deprecation\")\n");
                classCon.append("\t" + "public int delete"+beanName+"ById(" + dateType +" "+ columnName + "){\n");
                classCon.append("\t\t" + "return (Integer) getSqlMapClientTemplate().delete(\""+tableName+".deleteById\", "+ columnName + ");\n\t}\n\n");
                
                classCon.append("\t//按Id(主键)修改指定列的值\n" + "\t@SuppressWarnings(\"deprecation\")\n");
                classCon.append("\t" + "public int update"+beanName+"ById(" + beanName + " record){\n");
                classCon.append("\t\t" + "return (Integer) getSqlMapClientTemplate().update(\""+tableName+".updateById\", record);\n\t}\n\n");
                
                classCon.append("\t//根据参数查询有效列表\n" + "\t@SuppressWarnings({ \"deprecation\", \"unchecked\" })\n");
                classCon.append("\t" + "public List<"+beanName+"> queryListByParams(Map<String, Object> params){\n");
                
                classCon.append("\t\t" + "int pageSize = params.get(\"pageSize\") == null ? 10: (int)params.get(\"pageSize\");\n");
                classCon.append("\t\t" + "int pageIndex = params.get(\"pageIndex\") == null ? 0: (int)params.get(\"pageIndex\");\n");
                classCon.append("\t\t" + "params.put(\"offset\", pageIndex * pageSize);\n\t\tparams.put(\"limit\", pageSize);\n");
                classCon.append("\t\t" + "return getSqlMapClientTemplate().queryForList(\""+tableName+".queryListByParams\", params);\n\t}\n\n");
                
                classCon.append("\t//根据参数查询有效统计\n" + "\t@SuppressWarnings({ \"deprecation\" })\n");
                classCon.append("\t" + "public Integer queryCountByParams(Map<String, Object> params){\n");
                classCon.append("\t\t" + "Object obj =  getSqlMapClientTemplate().queryForObject(\""+tableName+".queryCountByParams\", params);\n");
                classCon.append("\t\t" + "return obj == null ? 0 : (Integer) obj;\n\t}\n\n");
                
                classCon.append("\t//通过Id(主键)查询一条记录\n" + "\t@SuppressWarnings({ \"deprecation\" })\n");
                classCon.append("\t" + "public " + beanName + " query"+beanName+"ById(" + dateType + " " + columnName + "){\n");
                classCon.append("\t\t" + "Object obj =  getSqlMapClientTemplate().queryForObject(\""+tableName+".queryById\", " + columnName + ");\n");
                classCon.append("\t\t" + "return obj == null ? null : ("+beanName+") obj;\n\t}\n");

                
                // 拼接(Dao接口）文件内容
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
