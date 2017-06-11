package cn.e3mall.search.mapper;

import java.util.List;

import cn.e3mall.common.pojo.SearchItem;

/**
 * 索引库mapper
 * @author Administrator
 *
 */
public interface ItemMapper {
	/**
	 * 查询所有SearchItem
	 * @return
	 */
	public List<SearchItem> getItemList();
	
	/**
	 * 根据id查询SearchItem
	 * @param itemId
	 * @return
	 */
	public SearchItem getItemById(Long itemId);
}
