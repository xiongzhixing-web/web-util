package dao.impl;


import bean.ColumnStruct;
import bean.TableStruct;
import dao.GetTablesDao;
import dao.MapperXmlAutoDao;
import util.*;

import java.util.List;

public class MapperXmlAutoDaoImpl implements MapperXmlAutoDao {

    // 从GetTablesDaoImpl中获得装有所有表结构的List
    GetTablesDao getTables = new GetTablesDaoImpl();
    List<TableStruct> list = getTables.getTablesStruct();

    // 通过表名、字段名称、字段类型创建Mapper.xml
    @Override
    public boolean createMapperXml() {
        // 获得配置文件的参数
        // 项目路径
        String projectPath = ConfigUtil.serviceProjectPath;
        // 是否生成Mapper.xml
        String mapperXmlFalg = ConfigUtil.mapperXmlFlag;
        // Bean实体类的包名
        String beanPackage = ConfigUtil.beanPackage;
        if ("true".equals(mapperXmlFalg)) {
            // Mapper.xml的路径
            String path = projectPath + ConfigUtil.resourcesDefultPath;
            // 遍历装有所有表结构的List
            for (int i = 0; i < list.size(); i++) {
                // 表名
                String tableName = list.get(i).getTableName();

                // 文件名
                String fileName = "sqlmap_"+tableName;
                String beanName = NameUtil.fileName(tableName) + ConfigUtil.entitySuffix;
                String lowerBeanName = NameUtil.toLowerColumnName(beanName);
                String beanNameMap = lowerBeanName + "Map";

                // 获得每个表的所有列结构
                List<ColumnStruct> columns = list.get(i).getColumns();

                // 主键名
                String beanIdName = NameUtil.columnName(columns.get(0).getColumnName());
                String IdName = columns.get(0).getColumnName();
                // 主键类型
                String IdType = DataTypeUtil.getType(columns.get(0).getDataType());
                String IdParamType = ParamTypeUtil.getParamType(IdType);
                String IdJdbcType = JdbcTypeUtil.getJdbcType(IdType);
                if (IdJdbcType == "INT" || "INT".equals(IdJdbcType)) {
                    IdJdbcType = "INTEGER";
                }

                // (Mapper.xml）文件内容
                StringBuffer headCon = new StringBuffer();
                headCon.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                headCon.append("<!DOCTYPE sqlMap PUBLIC \"-//ibatis.apache.org//DTD SQL Map 2.0//EN\" \"http://ibatis.apache.org/dtd/sql-map-2.dtd\" >\n");
                headCon.append("<sqlMap namespace=\"" + tableName + "\">\n");
                headCon.append("<typeAlias alias=\"" + lowerBeanName + "\" type=\""+beanPackage+"."+beanName+"\" />\n");
                
                StringBuffer resultMapCon = new StringBuffer();
                resultMapCon.append("\t" + "<resultMap id=\""+beanNameMap+"\" class=\"" + lowerBeanName + "\">\n");

                StringBuffer baseColCon = new StringBuffer();
                baseColCon.append("\t" + "<sql id=\"Base_Column_List\">\n");

                StringBuffer insertRecordCon = new StringBuffer();
                insertRecordCon.append("\t" + "<!--添加一条完整记录 -->\n");
                insertRecordCon.append("\t" + "<insert id=\"insertRecord\" parameterClass=\"" + lowerBeanName +"\">\n");
                insertRecordCon.append("\t\t" + "insert into " + tableName + "(");

                StringBuffer insertRecordCons = new StringBuffer();
                insertRecordCons.append("\t\t" + "values (");

                StringBuffer delByIdCon = new StringBuffer();
                delByIdCon.append("\t" + "<!-- 通过Id(主键)删除一条记录 -->\n");
                delByIdCon.append("\t" + "<delete id=\"deleteById\" parameterClass=\"" + IdParamType + "\">\n");
                delByIdCon.append("\t\t" + "delete from " + tableName + " where " + IdName + "= #"
                        + beanIdName + "#\n");
                delByIdCon.append("\t" + "</delete>\n\n");

                StringBuffer updateByIdCon = new StringBuffer();
                updateByIdCon.append("\t" + "<!-- 按Id(主键)修改指定列的值 -->\n");
                updateByIdCon.append("\t" + "<update id=\"updateById\" parameterClass=\"" + lowerBeanName + "\">\n");
                updateByIdCon.append("\t\t" + "update " + tableName + "\n");
                updateByIdCon.append("\t\t" + "<dynamic prepend=\"SET \">\n");


                StringBuffer listSelCon = new StringBuffer();
                listSelCon.append("\t" + "<!-- 根据参数查询有效统计 -->\n");
                listSelCon.append("\t" + "<select id=\"queryListByParams\" parameterClass=\"java.util.HashMap\"  resultMap=\"" + beanNameMap + "\" >\n");
                listSelCon.append("\t\t" + "select\n" + "\t\t" + "<include refid=\"Base_Column_List\"/>\n");
                listSelCon.append("\t\t" + "from " + tableName + " where 1=1\n");
                
                StringBuffer countSelCon = new StringBuffer();
                countSelCon.append("\t" + "<!-- 通过Id(主键)查询一条记录 -->\n");
                countSelCon.append("\t" + "<select id=\"queryCountByParams\" parameterClass=\"java.util.HashMap\" resultClass=\"java.lang.Integer\">\n");
                countSelCon.append("\t\t" + "select count(*) from " + tableName + " where 1=1\n");


                StringBuffer selectByIdCon = new StringBuffer();
                selectByIdCon.append("\t" + "<select id=\"queryById\" parameterClass=\"" + IdParamType
                        + "\" resultMap=\""+beanNameMap+"\">\n");
                selectByIdCon.append("\t\t" + "select\n" + "\t\t" + "<include refid=\"Base_Column_List\"/>\n");
                selectByIdCon.append("\t\t" + "from " + tableName + "\n" + "\t\t" + "where " + IdName + "= #"
                        + beanIdName + "#\n");
                selectByIdCon.append("\t" + "</select>\n");


                // 遍历List，将字段名称和字段类型、属性名写进文件
                for (int j = 0; j < columns.size(); j++) {
                    // 字段名
                    String columnName = columns.get(j).getColumnName();
                    // 属性（变量）名
                    String attrName = NameUtil.columnName(columns.get(j).getColumnName());
                    // 字段类型
                    String type = DataTypeUtil.getType(columns.get(j).getDataType());
                    // 是否需要剔除
                    boolean isNotExceptColumn = this.isNotExceptColumn(columnName);
                    String jdbcType = JdbcTypeUtil.getJdbcType(type);
                    if (jdbcType == "INT" || "INT".equals(jdbcType)) {
                        jdbcType = "INTEGER";
                    }
                    if (j == 0) {
                        resultMapCon.append("\t\t" + "<result column=\"" + columnName + "\" property=\""
                                + attrName + "\" />\n");
                        baseColCon.append("\t\t" + columnName);
                    } else {
                        resultMapCon.append("\t\t" + "<result column=\"" + columnName + "\" property=\""
                                + attrName + "\" />\n");
                        baseColCon.append("," + columnName);
                        if(isNotExceptColumn){
                            if(j == 1){
                                insertRecordCon.append(columnName);
                                insertRecordCons.append("#" + attrName + "#");
                            }else{
                                insertRecordCon.append(",\n" + "\t\t\t" + columnName);
                                insertRecordCons.append(",\n" + "\t\t\t" + "#" + attrName + "#");
                            }
                            updateByIdCon.append("\t\t\t<isNotEmpty property=\""+attrName+"\" prepend=\",\">" + columnName + "=" + "#" + attrName + "# </isNotEmpty> \n");
                        }
                    }
                    if(isNotExceptColumn){
                        countSelCon.append("\t\t<isNotEmpty  property=\""+attrName+"\" prepend=\"and\">\n\t\t\t "+columnName+" = #"+attrName+"#\n\t\t</isNotEmpty>\n");
                        listSelCon.append("\t\t<isNotEmpty  property=\""+attrName+"\" prepend=\"and\">\n\t\t\t "+columnName+" = #"+attrName+"#\n\t\t</isNotEmpty>\n");
                    }

                }
                resultMapCon.append("\t" + "</resultMap>\n\n");
                baseColCon.append("\n\t" + "</sql>\n\n");
                insertRecordCon.append(")\n");
                insertRecordCons.append(")\n" + "\t\t<selectKey resultClass=\"int\" keyProperty=\""+beanIdName+"\">\n\t\t\tSELECT @@IDENTITY\n\t\t\tAS ID\n\t\t</selectKey>\n" + "\t</insert>\n\n");
                updateByIdCon.append("\t\t</dynamic>\n\t\t" + "where " + IdName + "= #" + beanIdName + "#\n" + "\t" + "</update>\n");
                countSelCon.append("\t" + "</select>\n\n");
                listSelCon.append("\t\torder by create_time desc\n\t\tLIMIT #offset#,#limit#\n\t</select>\n\n");
                // 拼接(sqlmap_*.xml）文件内容
                StringBuffer content = new StringBuffer();
                content.append(headCon);
                content.append(resultMapCon);
                content.append(baseColCon);
                
                content.append(insertRecordCon);
                content.append(insertRecordCons);
                content.append(delByIdCon);
                content.append(updateByIdCon);
                
                content.append(listSelCon);
                content.append(countSelCon);
                content.append(selectByIdCon);
                content.append("</sqlMap>");

                FileUtil.createFileAtPath(path + "/", fileName + ".xml", content.toString());
            }
            return true;
        }
        return false;
    }
    
    /**
     * 不是剔除的字段
     * 
     * @param columnName
     * @return
     * @author 卢家伟  2018年3月5日 新建
     */
    private boolean isNotExceptColumn(String columnName){
        String[] exceptColumnArray = new String[]{"create_time","modify_time"};
        for(String exceptColumn:exceptColumnArray){
            if(exceptColumn.equals(columnName)){
                return false;
            }
        }
        return true;
    }

}
