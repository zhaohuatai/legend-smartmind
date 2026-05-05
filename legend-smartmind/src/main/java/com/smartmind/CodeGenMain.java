package com.smartmind;

import java.io.File;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.legend.framework.kit.coder.CodeGenerator;
import org.legend.framework.kit.coder.GenScope;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.VerboseProgressCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
//import org.foundation.legend.cloud.zbss.datav.generator.BatisGenerator;

public class CodeGenMain{

	 static String project_path = "E:\\worksapce\\sts5.0\\legend-smartmind";
	    static String mybatis_gen_configfile = project_path + "\\src\\main\\resources\\generatorConfig-smartmind.xml";
	    
	public static void main(String[] args) throws Exception {
		SqlSessionFactory  sqlSessionFactory=null;
		//GenMain.doGen(project_path, project_path+"/src/main/resources/generatorConfig-activity.xml");
    	
    	//GenMain.doGen(project_path, project_path+"/src/main/resources/generatorConfig-order.xml");
    	
    	//GenMain.doGen(project_path, project_path+"/src/main/resources/generatorConfig-pet.xml");
    	
//    	GenMain.doGen(project_path, project_path+"/src/main/resources/generatorConfig-walk.xml");
    	
    	//GenMain.doGen(project_path, project_path+"/src/main/resources/generatorConfig-sns.xml");
		
		CodeGenerator.doGen(project_path, mybatis_gen_configfile,GenScope.newAll());
    }

	
}
