package cn.e3mall.service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;

/**
 * 商品服务Service
 * @author Administrator
 *
 */
public interface ItemService {
	/**
	 * 根据商品id查询商品信息
	 */
	public TbItem getItemById(Long itemId);
	
	/**
	 * 根据商品id查询商品描述
	 */
	public TbItemDesc getItemDescById(Long itemId);
	
	/**
	 * 查询所有商品
	 */
	public EasyUIDataGridResult getItemList(Integer page,Integer rows);
	
	/**
	 * 添加商品
	 * @param tbItem
	 * @param desc
	 * @return
	 */
	public E3Result addItem(TbItem tbItem,String desc);
	
	/**
	 * 商品编辑前回显数据
	 * @param ids
	 * @return
	 */
	public E3Result itemPreEdit(Long itemId);
	
	/**
	 * 编辑商品
	 * @param itemid
	 */
	public E3Result updateItem(TbItem tbItem,String desc);
	
	/**
	 * 删除商品
	 * @param ids
	 * @return
	 */
	public E3Result deleteItem(Long[] ids);
	
	/**
	 * 下架商品
	 * @param ids
	 * @return
	 */
	public E3Result updateItemStatusToInstock(Long[] ids);
	
	/**
	 * 上架商品
	 * @param ids
	 * @return
	 */
	public E3Result updateItemStatusToReshelf(Long[] ids);
}
