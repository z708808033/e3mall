package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;

/**
 * 商品管理Controller
 * @author Administrator
 *
 */
@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;
	
	/**
	 * 根据商品id查询商品
	 * @param itemId
	 * @return
	 */
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable Long itemId) {
		return itemService.getItemById(itemId);
	}
	
	/**
	 * 查询所有商品
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(@RequestParam(defaultValue = "1")Integer page,@RequestParam(defaultValue = "30")Integer rows) {
		EasyUIDataGridResult result = itemService.getItemList(page, rows);
		return result;
	}
	
	/**
	 * 添加商品
	 * @param tbItem
	 * @param desc
	 * @return
	 */
	@RequestMapping(value="/item/save",method=RequestMethod.POST)
	@ResponseBody
	public E3Result addItem(TbItem tbItem,String desc) {
		E3Result e3Result = itemService.addItem(tbItem, desc);
		return e3Result;
		
	}
	
	/**
	 * 商品编辑前回显数据
	 * @param itemId
	 * @return
	 */
	@RequestMapping("/rest/item/query/item/desc/{itemId}")
	@ResponseBody
	public E3Result itemPreEdit(@PathVariable Long itemId) {
		E3Result e3Result = itemService.itemPreEdit(itemId);
		return e3Result;
	}
	
	/**
	 * 编辑商品
	 * @param tbItem
	 * @param desc
	 * @return
	 */
	@RequestMapping("/rest/item/update")
	@ResponseBody
	public E3Result updateItem(TbItem tbItem,String desc) {
		return itemService.updateItem(tbItem, desc);
	}
	
	/**
	 * 删除商品
	 * @param ids
	 * @return
	 */
	@RequestMapping("/rest/item/delete")
	@ResponseBody
	public E3Result deleteItem(Long[] ids) {
		return itemService.deleteItem(ids);
	}
	
	/**
	 * 下架商品
	 * @param ids
	 * @return
	 */
	@RequestMapping("/rest/item/instock")
	@ResponseBody
	public E3Result updateItemStatusToInstock(Long[] ids) {
		return itemService.updateItemStatusToInstock(ids);
	}
	
	/**
	 * 上架商品
	 * @param ids
	 * @return
	 */
	@RequestMapping("/rest/item/reshelf")
	@ResponseBody
	public E3Result updateItemStatusToReshelf(Long[] ids) {
		return itemService.updateItemStatusToReshelf(ids);
	}
	
	
}
