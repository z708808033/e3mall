package cn.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;

/**
 * 内容管理
 * @author Administrator
 *
 */

@Service
public class ContentServiceImpl implements ContentService {
	@Autowired
	private TbContentMapper tbContentMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;

	/**
	 * 添加内容
	 */
	@Override
	public E3Result addContent(TbContent tbContent) {
		//补全属性
		tbContent.setCreated(new Date());
		tbContent.setUpdated(new Date());
		
		tbContentMapper.insert(tbContent);
		
		//缓存同步
		jedisClient.hdel(CONTENT_LIST, tbContent.getCategoryId() + "");
		
		return E3Result.ok();
	}

	/**
	 * 根据内容分类id查询该分类下所有内容
	 */
	@Override
	public List<TbContent> getContentListByCid(Long cid) {
		//查询缓存,如果缓存中有则直接响应结果,如果没有则查询数据库
		//由于即使在查询缓存、添加缓存的过程中抛异常也不会影响数据的传输，所以用try/catch把这两个过程包起来,让它们即便抛异常也不中止程序
		try {
			String json = jedisClient.hget(CONTENT_LIST, cid + "");
			if(json != null) {
				List<TbContent> tbContentList = JsonUtils.jsonToList(json, TbContent.class);
				return tbContentList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		//设置查询条件,并执行查询
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(cid);
		List<TbContent> tbContentList = tbContentMapper.selectByExampleWithBLOBs(example);
		
		//把结果添加到缓存中
		try {
			jedisClient.hset(CONTENT_LIST, cid + "", JsonUtils.objectToJson(tbContentList));
		} catch (Exception e) {
			e.printStackTrace();
		}

		//返回结果
		return tbContentList;
	}

	/**
	 * 查询内容
	 */
	@Override
	public EasyUIDataGridResult getContentList(Long categoryId, Integer page,Integer rows) {
		PageHelper.startPage(page, rows);
		
		//根据categoryId查询所有内容,并将内容添加至EasyUIDataGridResult对象中
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> contentList = tbContentMapper.selectByExampleWithBLOBs(example);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(contentList);
		
		//设置EasyUIDataGridResult的total值
		PageInfo<TbContent> pageInfo = new PageInfo<>(contentList);
		long total = pageInfo.getTotal();
		result.setTotal(total);
		
		return result;
	}

	/**
	 * 编辑内容
	 */
	@Override
	public E3Result updateContent(TbContent tbContent) {
		//补全属性
		tbContent.setCreated(new Date());
		tbContent.setUpdated(new Date());
		tbContentMapper.updateByPrimaryKeyWithBLOBs(tbContent);
		
		//缓存同步
		jedisClient.hdel(CONTENT_LIST, tbContent.getCategoryId() + "");
		
		return E3Result.ok();
	}

	/**
	 * 删除内容
	 */
	@Override
	public E3Result deleteContent(Long[] ids) {
		for (Long id : ids) {
			//缓存同步
			TbContent tbContent = tbContentMapper.selectByPrimaryKey(id);
			jedisClient.hdel(CONTENT_LIST, tbContent.getCategoryId() + "");
			
			tbContentMapper.deleteByPrimaryKey(id);
		}
		
		return E3Result.ok();
	}

}
