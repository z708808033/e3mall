package cn.e3mall.pagehelper;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemExample;

public class PageHelperTest {
//	@Test
//	public void testPageHelper() throws Exception {
//		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
//		TbItemMapper tbItemMapper = context.getBean(TbItemMapper.class);
//		PageHelper.startPage(1, 10);
//		TbItemExample example = new TbItemExample();
//		List<TbItem> list = tbItemMapper.selectByExample(example);
//		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
//		System.out.println(pageInfo.getTotal());			//获取总记录数
//		System.out.println(pageInfo.getPages());			//获取总页数
//		System.out.println(list.size());
//	}
}
