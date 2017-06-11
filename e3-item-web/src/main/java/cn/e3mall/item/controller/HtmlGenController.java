package cn.e3mall.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateNotFoundException;


@Controller
public class HtmlGenController {
	@Autowired
	private FreeMarkerConfig config;
	
	@RequestMapping("/html/test")
	@ResponseBody
	public String ToHtml() throws Exception {
		Configuration configuration = config.getConfiguration();
		//加载模板对象
		Template template = configuration.getTemplate("hello.ftl");
		
		//创建一个数据集
		Map data = new HashMap();
		
		//指定文件输出的路径和文件名
		data.put("hello", "test web freemarker");
		
		//输出文件
		Writer writer = new FileWriter(new File("D:/freemarker/hello1.html"));
		template.process(data, writer);
		
		//关闭资源
		writer.close();
		
		return "OK";
	}
}
