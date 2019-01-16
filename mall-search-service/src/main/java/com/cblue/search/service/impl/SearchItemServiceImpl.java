package com.cblue.search.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.cblue.pojo.TbItem;
import com.cblue.search.SearchItemService;

@Service(timeout=3000)
public class SearchItemServiceImpl implements SearchItemService {

	@Autowired
	private SolrTemplate solrTemplate;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	
	@Override
	public void deleteByIds(List ids) {
		// TODO Auto-generated method stub
		Query query = new SimpleQuery();
		Criteria criteria = new Criteria("item_goodsid").in(ids);
		query.addCriteria(criteria);
		solrTemplate.delete(query);
		solrTemplate.commit();	
	}
	
	@Override
	public void importList(List<TbItem> list) {
		try {
			 System.out.println("list----"+list.size());
			 for(TbItem item:list){
					System.out.println(item.getTitle());	
					Map specMap= JSON.parseObject(item.getSpec());//将spec字段中的json字符串转换为map
					item.setSpecMap(specMap);//给带注解的字段赋值	
					System.out.println("list-----end"+item);
			}
			
			 solrTemplate.saveBeans(list);
			 solrTemplate.commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
       
	}
	
	// keywords --> 内容
	@Override
	public Map<String, Object> search(Map searchMap) {
		Map<String,Object> map=new HashMap<>();
		//简单搜索
		Query query=new SimpleQuery();
		System.out.println("keywords="+searchMap.get("keywords"));
		
		//对关键词进行去空格处理
		
		String keywords = (String)searchMap.get("keywords");
		keywords = keywords.replace(" ","");
		System.out.println("keyword="+keywords);
		
		
		//添加查询条件
		Criteria criteria=new Criteria("item_keywords").is(keywords);
		query.addCriteria(criteria);
		
		System.out.println("searchMap="+searchMap.get("categoryId"));
		//添加分类查询条件
		if(!"".equals(searchMap.get("categoryId"))){
			query = cateoryQuery(searchMap, query);
		}
		//添加品牌查询
		if(!"".equals(searchMap.get("brand"))){
			query = brandQuery(searchMap, query);
		}
		
		//添加规格查询
		if(!"".equals(searchMap.get("spec"))){
			query = specQuery(searchMap, query);
		}
		
		System.out.println("price---"+searchMap.get("price"));
		System.out.println("".equals(searchMap.get("price")));
		//根据价格查询
		if(!"".equals(searchMap.get("price"))){
			query = priceQuery(searchMap, query);
		}
		
		//分页查询
		Integer pageNo = (Integer)searchMap.get("pageNo");
		if(pageNo==null){
			pageNo = 1;
		}
		System.out.println("pageSize="+searchMap.get("pageSize"));
		System.out.println((Integer)searchMap.get("pageSize"));
		Integer pageSize = (Integer)searchMap.get("pageSize");
		if(pageSize == null){
			pageSize =10;
		}
		
		query.setOffset((pageNo-1)*pageSize);
		query.setRows(pageSize);
		
		//设置排序
		String sortFiled = (String)searchMap.get("sortFiled");//排序的字段
		String sort = (String)searchMap.get("sort");  //排序的方式  desc  asc
		System.out.println("sortFiled="+sortFiled);
		System.out.println("sort="+sort);
		if(sort!=null&&!"".equals(sort)){
			//升序
			if("ASC".equals(sort)){
				System.out.println("item_"+sortFiled);
				Sort sortMethod = new Sort(Sort.Direction.ASC,"item_"+sortFiled);
				query.addSort(sortMethod);
			}
			//降序
			if("DESC".equals(sort)){
				System.out.println("item_"+sortFiled);
				Sort sortMethod = new Sort(Sort.Direction.DESC,"item_"+sortFiled);
				query.addSort(sortMethod);
			}
			
		}
		
		
		
	    ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
		map.put("rows", page.getContent());
		//分页处理
		map.put("totolPage", page.getTotalPages());//总页数
		map.put("totol", page.getTotalElements());//总记录数
		
		
		//实现高亮搜索
		//map.putAll(searchHightLight(searchMap));
		
		//实现分组查询
		List<String> categoryList  = searchGroup(searchMap);
		map.put("categoryList", categoryList);
		
		//System.out.println("categoryList.get(0)="+categoryList.get(0));
		
		//根据选择的分类，显示需要的品牌和规格
		String categoryId = (String)searchMap.get("categoryId");
		if(!"".equals(categoryId)){
			//根据分类查询
			map.putAll(getBrandAndSpecFromRedis(categoryId));
		}else{
			//获得品牌和规格
			if(categoryList.size()>0){
				map.putAll(getBrandAndSpecFromRedis(categoryList.get(0)));
			}
		}
		
		
		
		
		
		
		
		return map;
	}
	
	/**
	 * 根据分类查询
	 */
	private Query cateoryQuery(Map searchMap,Query query){	
		Criteria filCriteria = new Criteria("item_category").is(searchMap.get("categoryId"));
		FilterQuery filterQuery  = new SimpleFilterQuery(filCriteria);
		return query.addFilterQuery(filterQuery);
	}
	
	/**
	 * 根据品牌查询
	 */
	private Query brandQuery(Map searchMap,Query query){
		Criteria filterCriteria=new Criteria("item_brand").is(searchMap.get("brand"));
		FilterQuery filterQuery=new SimpleFilterQuery(filterCriteria);
		return query.addFilterQuery(filterQuery);
	}
	
	/**
	 * 根据价格查询
	 */
	private Query priceQuery(Map searchMap,Query query){
		String price [] = ((String)searchMap.get("price")).split("-");
		if(!"0".equals(price[0])){
			Criteria filterCriteria=new Criteria("item_price").greaterThanEqual(price[0]);
			FilterQuery filterQuery=new SimpleFilterQuery(filterCriteria);
			query.addFilterQuery(filterQuery);
		}
		if(!"*".equals(price[1])){
			Criteria filterCriteria=new Criteria("item_price").lessThanEqual(price[1]);
			FilterQuery filterQuery=new SimpleFilterQuery(filterCriteria);
			query.addFilterQuery(filterQuery);
		}
		return query;
	}
	
	/**
	 * 根据规格查询
	 */
	private Query specQuery(Map searchMap,Query query){
		Map<String,String> specMap= (Map) searchMap.get("spec");
		for(String key:specMap.keySet() ){
         Criteria filterCriteria=new Criteria("item_spec_"+key).is( specMap.get(key) );
			FilterQuery filterQuery=new SimpleFilterQuery(filterCriteria);
			query.addFilterQuery(filterQuery);				
		}	
	   return query;
	}
	
	
	/*
	 * 高亮显示
	 */
	private Map searchHightLight(Map searchMap){
		Map<String,Object> map=new HashMap<>();
		
		//创建一个高亮的搜索对象
		HighlightQuery highlightQuery = new SimpleHighlightQuery();
		
		//创建高亮显示的选项（如何实现高亮）
		HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");;
		highlightOptions.setSimplePrefix("<font color='red'>");
		highlightOptions.setSimplePostfix("</font>");
		
		highlightQuery.setHighlightOptions(highlightOptions);
		
		//按照关键字进行查询
		Criteria criteria=new Criteria("item_keywords").is(searchMap.get("keywords"));
		highlightQuery.addCriteria(criteria);
		
		HighlightPage<TbItem> page =  solrTemplate.queryForHighlightPage(highlightQuery,TbItem.class);
		
		//还需要执行高亮选项
		for(HighlightEntry<TbItem> h :page.getHighlighted()){
			  TbItem tbItem =  h.getEntity();
			  if(h.getHighlights().size()>0&&h.getHighlights().get(0).getSnipplets().size()>0){
				  //给标题设置高亮显示<font color='red'>关键字</font>
				  System.out.println("设置高亮显示="+h.getHighlights().get(0).getSnipplets().get(0));
				  tbItem.setTitle(h.getHighlights().get(0).getSnipplets().get(0));
			  }
			
		}
		map.put("rows", page.getContent());
		return map;
	}
	
	
	//分组查询
	private List searchGroup(Map searchMap){
		List<String> list = new ArrayList<String>();
		
		Query query = new SimpleQuery();
		
		
		//根据关键字进行查询
		Criteria criteria=new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);
		
		//设置分组选项
		GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");
		query.setGroupOptions(groupOptions);
		
		//执行查询
		GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);
		
		//得到查询的分类内容
		GroupResult<TbItem> groupResult = page.getGroupResult("item_category");
		//得到分类对象
		Page<GroupEntry<TbItem>> groupEntryPage = groupResult.getGroupEntries();
		//多个分类
		List<GroupEntry<TbItem>> content = groupEntryPage.getContent();
		
		for(GroupEntry<TbItem> g:content){
			System.out.println("g.getGroupValue()="+g.getGroupValue());
			list.add(g.getGroupValue());
		}
		
		return list;
	}
	
	//从redis中查询品牌和规格
	private Map getBrandAndSpecFromRedis(String categoryId){
		Map map = new HashMap();
		//根据id，查询typeid
		Long typeId = (Long) redisTemplate.boundHashOps("itemCat").get(categoryId);
		System.out.println("typeId2"+redisTemplate.boundHashOps("itemCat").get(categoryId));
		System.out.println("typeId="+typeId);
		
		if(typeId!=null){
			
			List brandList = (List)redisTemplate.boundHashOps("brand").get(typeId);
			map.put("brand", brandList);
			
			List specList = (List)redisTemplate.boundHashOps("spec").get(typeId);
			map.put("spec", specList);
			
			System.out.println(brandList.size());
			System.out.println(brandList.get(0).toString());
		}
		return map;
	}

	

	

}
