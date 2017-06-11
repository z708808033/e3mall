//package cn.e3mall.freemarker;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.OutputStreamWriter;
//import java.io.Writer;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.junit.Test;
//
//import freemarker.template.Configuration;
//import freemarker.template.Template;
//
//public class freemarkerTest {
//	@Test
//	public void fun1() throws Exception {
//		//创建一个模板文件
//		//创建一个Configuration对象
//		Configuration config = new Configuration(Configuration.getVersion());
//		
//		//设置模板文件保存的目录
//		config.setDirectoryForTemplateLoading(new File("G:/myeclipse_workspace/e3-item-web/src/main/webapp/WEB-INF/ftl"));
//		
//		//设置模板文件的编码格式,一般是utf-8
//		config.setDefaultEncoding("utf-8");
//		
//		//利用Configuration对象加载一个Template模板文件,创建一个模板对象
//		Template template = config.getTemplate("hello.ftl");
//		
//		//创建一个数据集,可以是pojo,也可以是map,推荐用map
//		Map data = new HashMap();
//		data.put("hello", "hello freemarker");
//		
//		//创建一个Writer对象,指定输出文件的路径及文件名
//		Writer writer = new FileWriter(new File("D:/freemarker/hello.txt"));
//		
//		//生成静态页面
//		template.process(data, writer);
//		
//		//关闭资源
//		writer.close();
//	}
//	
//	@Test
//	public void fun2() throws Exception {
//		Configuration config = new Configuration(Configuration.getVersion());
//		config.setDirectoryForTemplateLoading(new File("G:/myeclipse_workspace/e3-item-web/src/main/webapp/WEB-INF/ftl"));
//		config.setDefaultEncoding("utf-8");
//		Template template = config.getTemplate("person.ftl");
//		Map data = new HashMap();
//		
//		//创建一个pojo对象
//		Person person = new Person(1, "张三", 12, "man");
//		data.put("person", person);
//		
//		Writer writer = new FileWriter(new File("D:/freemarker/person.html"));
//		template.process(data, writer);
//		writer.close();
//	}
//	
//	@Test
//	public void fun3() throws Exception {
//		Configuration config = new Configuration(Configuration.getVersion());
//		config.setDirectoryForTemplateLoading(new File("G:/myeclipse_workspace/e3-item-web/src/main/webapp/WEB-INF/ftl"));
//		config.setDefaultEncoding("utf-8");
//		Template template = config.getTemplate("personList.ftl");
//		Map data = new HashMap();
//		
//		//集合类型
//		List<Person> personList = new ArrayList<>();
//		personList.add(new Person(1, "张三", 12, "男"));
//		personList.add(new Person(2, "李四", 14, "男"));
//		personList.add(new Person(3, "王五", 16, "女"));
//		personList.add(new Person(4, "赵六", 16, "女"));
//		personList.add(new Person(5, "不知道七", 16, "女"));
//		data.put("personList", personList);
//		
//		//日期类型
//		Date date = new Date();
//		data.put("date", date);
//		
//		//null值
//		data.put("val", 123);
//		
//		//include测试
//		data.put("hello","hello free马克");
//		
//		Writer writer = new FileWriter(new File("D:/freemarker/personList.html"));
//		template.process(data, writer);
//		writer.close();
//	}
//}
