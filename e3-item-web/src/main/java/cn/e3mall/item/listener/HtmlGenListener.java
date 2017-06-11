package cn.e3mall.item.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 监听商品改动消息,生成对应的静态页面
 * @author Administrator
 *
 */
public class HtmlGenListener implements MessageListener{
	@Autowired
	private FreeMarkerConfig freeMarkerConfig;
	@Autowired
	private ItemService itemService;
	@Value("${HTML_GEN_PATH}")
	private String HTML_GEN_PATH;
	
	@Override
	public void onMessage(Message msg) {
		try {
			//从消息中取出商品id
			TextMessage message = (TextMessage) msg;
			String text = message.getText();
			Long itemId = Long.parseLong(text);
			
			//等待事务提交
			Thread.sleep(1000);
			
			//根据商品id查询商品信息,包括基本信息和商品描述
			TbItem tbItem = itemService.getItemById(itemId);
			TbItemDesc itemDesc = itemService.getItemDescById(itemId);
			Item item = new Item(tbItem);
			
			//创建一个数据集,把商品数据封装进里面
			Map data = new HashMap();
			data.put("item", item);
			data.put("itemDesc", itemDesc);
			
			//加载模板对象
			Configuration configuration = freeMarkerConfig.getConfiguration();
			Template template = configuration.getTemplate("item.ftl");
			
			//创建一个输出流,指定输出的目录及文件名
			Writer writer = new FileWriter(new File(HTML_GEN_PATH + itemId + ".html"));
			
			//生成静态页面
			template.process(data, writer);
			
			//关闭资源
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
