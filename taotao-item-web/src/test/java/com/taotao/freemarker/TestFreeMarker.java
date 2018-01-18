package com.taotao.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class TestFreeMarker {
	@Test
	public void genFile() throws Exception {
		Configuration configuration = new Configuration(Configuration.getVersion());
		String filePath = "D:/workspace-taotao/taotao-item-web/src/main/webapp/WEB-INF/ftl";
		File file = new File(filePath);
		configuration.setDirectoryForTemplateLoading(file);
		configuration.setDefaultEncoding("utf-8");
		String fileName="hello.ftl";
		Template template = configuration.getTemplate(fileName);
		Map<Object, Object> dataModel = new HashMap<>();
		dataModel.put("hello", "this is my first freemarker test");
		
		dataModel.put("stu", new Student(1,"xiaobai",18));
		List<Student> list  = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			list.add(new Student(2+i,"stuname"+i,19+i));
		}
		dataModel.put("stulist",list);
		
		dataModel.put("myval", "this is my value");
		dataModel.put("date", new Date());
		dataModel.put("other", "ok other date is "+new Date());
		
		String filepathname="D:/workspace-taotao/taotao-item-web/src/main/webapp/hello.html";
		Writer out = new FileWriter(new File(filepathname));
		template.process(dataModel, out);
		out.close();
	}

}
