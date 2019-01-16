package com.cblue.search;

import java.util.List;
import java.util.Map;

import com.cblue.pojo.TbItem;

public interface SearchItemService {
	
	
	public Map<String,Object> search(Map searchMap);
	
	//导入数据
	public void importList(List<TbItem> list);
	
	//删除索引
	public void deleteByIds(List ids);
	

}
