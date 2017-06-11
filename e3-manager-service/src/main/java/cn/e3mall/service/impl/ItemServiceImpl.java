package cn.e3mall.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.service.ItemService;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


/**
 * 商品管理
 * @author Administrator
 *
 */
@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private TbItemMapper tbItemMapper;
	@Autowired
	private TbItemDescMapper tbItemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource
	private Destination topicDestination;
	@Autowired
	private JedisClient jedisClient;
	@Value("${REDIS_ITEM_PRE}")
	private String REDIS_ITEM_PRE;
	@Value("${ITEM_CACHE_EXPIRE}")
	private Integer ITEM_CACHE_EXPIRE;

	/**
	 * 根据商品id查询商品信息
	 */
	@Override
	public TbItem getItemById(Long itemId) {
		//查询缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":BASE");
			if(json != null && !json.isEmpty()) {
				TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
				return tbItem;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//如果缓存中没有,则查询数据库
		TbItem item = tbItemMapper.selectByPrimaryKey(itemId);
		
		if(item != null) {
			//添加至缓存
			jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":BASE", JsonUtils.objectToJson(item));
			//设置过期时间
			jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":BASE", ITEM_CACHE_EXPIRE);
		}
		return item;
	}
	
	/**
	 * 发送消息告知商品有变动,发送的消息内容为变动商品的id
	 * @param itemId
	 */
	private void sendMessage(final Long itemId) {
		jmsTemplate.send(topicDestination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(itemId + "");
			}
		});
	}
	
	/**
	 * 查询所有商品
	 */
	@Override
	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
		PageHelper.startPage(page, rows);
		
		//获取分页结果
		TbItemExample example = new TbItemExample();
		List<TbItem> tbItemList = tbItemMapper.selectByExample(example);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(tbItemList);
		
		//获取总记录数
		PageInfo<TbItem> pageInfo = new PageInfo<>(tbItemList);
		long total = pageInfo.getTotal();
		result.setTotal(total);
		
		return result;
	}

	/**
	 * 添加商品
	 * @param tbItem
	 * @param desc
	 * @return
	 */
	@Override
	public E3Result addItem(TbItem tbItem, String desc) {
		//生成商品id
		Long itemId = IDUtils.getItemId();
		
		//补全item属性
		tbItem.setId(itemId);
		
		//补全其他属性
		//1-正常,2-下架,3-删除
		tbItem.setStatus((byte) 1);
		tbItem.setCreated(new Date());
		tbItem.setUpdated(new Date());
		
		//向商品表插入数据
		tbItemMapper.insert(tbItem);
		
		//创建一个商品描述表对应的pojo对象
		TbItemDesc tbItemDesc = new TbItemDesc();
		
		//补全属性
		tbItemDesc.setItemId(itemId);
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setCreated(new Date());
		tbItemDesc.setUpdated(new Date());
		
		//向商品描述表中插入数据
		tbItemDescMapper.insert(tbItemDesc);
		
		//发送消息告知商品有变动,发送的消息内容为变动商品的id值
		sendMessage(itemId);
		
		//返回成功
		return E3Result.ok();
	}

	/**
	 * 商品编辑前回显数据
	 */
	@Override
	public E3Result itemPreEdit(Long itemId) {
		E3Result e3Result = new E3Result();
		TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
		e3Result.setData(tbItemDesc);
		e3Result.setStatus(200);
		e3Result.setMsg(null);
		return e3Result;
	}

	/**
	 * 编辑商品
	 */
	@Override
	public E3Result updateItem(TbItem tbItem, String desc) {
		//补全tbItem属性
		tbItem.setStatus((byte) 1);
		tbItem.setCreated(new Date());
		tbItem.setUpdated(new Date());
		
		//创建tbItemDesc对象
		TbItemDesc tbItemDesc = new TbItemDesc();
		
		//补全tbItemDesc属性
		tbItemDesc.setItemId(tbItem.getId());
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setCreated(new Date());
		tbItemDesc.setUpdated(new Date());
		
		tbItemMapper.updateByPrimaryKeySelective(tbItem);
		tbItemDescMapper.updateByPrimaryKeySelective(tbItemDesc);
		
		//发送消息告知商品有变动,发送的消息内容为变动商品的id值
		sendMessage(tbItem.getId());
		
		//缓存同步
		jedisClient.del(REDIS_ITEM_PRE + ":" + tbItem.getId() + ":BASE");
				
		return E3Result.ok();
	}

	/**
	 * 删除商品
	 */
	@Override
	public E3Result deleteItem(Long[] ids) {
		for (Long id : ids) {
			TbItem item = tbItemMapper.selectByPrimaryKey(id);
			//1-正常,2-下架,3-删除
			item.setStatus((byte) 3);
			tbItemMapper.updateByPrimaryKey(item);
			
			//发送消息告知商品有变动,发送的消息内容为变动商品的id值
			sendMessage(id);
			
			//缓存同步
			jedisClient.del(REDIS_ITEM_PRE + ":" + id + ":BASE");
		}
		
		
		return E3Result.ok();
	}

	/**
	 * 下架商品
	 */
	@Override
	public E3Result updateItemStatusToInstock(Long[] ids) {
		for (Long id : ids) {
			TbItem item = tbItemMapper.selectByPrimaryKey(id);
			//1-正常,2-下架,3-删除
			item.setStatus((byte) 2);
			tbItemMapper.updateByPrimaryKey(item);
			
			//发送消息告知商品有变动,发送的消息内容为变动商品的id值
			sendMessage(id);
			
			//缓存同步
			jedisClient.del(REDIS_ITEM_PRE + ":" + id + ":BASE");
		}
		return E3Result.ok();
	}

	/**
	 * 上架商品
	 */
	@Override
	public E3Result updateItemStatusToReshelf(Long[] ids) {
		for (Long id : ids) {
			TbItem item = tbItemMapper.selectByPrimaryKey(id);
			//1-正常,2-下架,3-删除
			item.setStatus((byte) 1);
			tbItemMapper.updateByPrimaryKey(item);
			
			//发送消息告知商品有变动,发送的消息内容为变动商品的id值
			sendMessage(id);
			
			//缓存同步
			jedisClient.del(REDIS_ITEM_PRE + ":" + id + ":BASE");
		}
		return E3Result.ok();
	}
	
	

	/**
	 * 根据商品id查询商品描述
	 */
	@Override
	public TbItemDesc getItemDescById(Long itemId) {
		//查询缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":DESC");
			if(json != null && !json.isEmpty()) {
				TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return tbItemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//如果缓存中没有,则查询数据库
		TbItemDesc itemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
		
		if(itemDesc != null) {
			//添加至缓存
			jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":DESC", JsonUtils.objectToJson(itemDesc));
			//设置过期时间
			jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":DESC", ITEM_CACHE_EXPIRE);
		}
		return itemDesc;
	}
}
