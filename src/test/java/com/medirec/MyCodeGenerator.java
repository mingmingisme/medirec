package com.medirec;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MyCodeGenerator {
    private static final String DATABASE_NAME = "medirec";
    private static final String URL = "jdbc:mysql://localhost:3306/" + DATABASE_NAME + "?useUnicode=true&useSSL=false&characterEncoding=utf8";
    private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "admin";
    private static final String PROJECT_PACKAGE = "com.medirec";
    private static final String CONTROLLER_PACKAGE = "controller";
    private static final String ENTITY_PACKAGE = "entity";

    public static void main(String[] args) {
        // code generator
        AutoGenerator mpg = new AutoGenerator();

        // global configuration
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("zsm");
        gc.setOpen(false);

        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");

        gc.setBaseResultMap(true);
        gc.setActiveRecord(true);

        mpg.setGlobalConfig(gc);

        // data source configuration
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(URL);
        dsc.setDriverName(DRIVER_NAME);
        dsc.setUsername(USERNAME);
        dsc.setPassword(PASSWORD);

        mpg.setDataSource(dsc);

        // package configuration
        PackageConfig pc = new PackageConfig();
        pc.setParent(PROJECT_PACKAGE);
        pc.setController(CONTROLLER_PACKAGE);
        pc.setEntity(ENTITY_PACKAGE);

        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        List<FileOutConfig> focList = new ArrayList<>();

        String templatePath = "/templates/mapper.xml.ftl";

        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/src/main/resources/mapper/"
                        + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });

        String entityPath = "/templates/entity.java.ftl";

        focList.add(new FileOutConfig(entityPath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/src/main/java" + PROJECT_PACKAGE.replaceAll("\\.", "/")
                        + "/entity/" + tableInfo.getEntityName()+ ".java";
            }
        });

        String controllerPath = "/templates/controller.java.ftl";

        focList.add(new FileOutConfig(controllerPath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/java/" + PROJECT_PACKAGE.replaceAll("\\.", "/")
                        + "/controller/" + tableInfo.getEntityName() + "Controller" + ".java";
            }
        });

        cfg.setFileOutConfigList(focList);

        // template configuration
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setController(null);
        templateConfig.setEntity(null);
        templateConfig.setXml(null);

        // strategy configuration
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setRestControllerStyle(true);
        strategy.setSuperControllerClass("com.example.mybatisplus.common.BaseController");
        strategy.setEntityLombokModel(true);
    }
}
