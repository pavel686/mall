package com.cblue.sellergoods.service.impl;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.cblue.mapper.TbSpecificationOptionMapper;
import com.cblue.mapper.TbTypeTemplateMapper;
import com.cblue.pojo.TbSpecificationOption;
import com.cblue.pojo.TbSpecificationOptionExample;
import com.cblue.pojo.TbTypeTemplate;
import com.cblue.pojo.TbTypeTemplateExample;
import com.cblue.pojo.TbTypeTemplateExample.Criteria;
import com.cblue.sellergoods.service.TypeTemplateService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class TypeTemplateServiceImpl implements TypeTemplateService {

	@Autowired
	private TbTypeTemplateMapper typeTemplateMapper;
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
	private TbSpecificationOptionMapper tbSpecificationOptionMapper;
	
	//[{"id":27,"text","网络","options":[{"id":"98","optionname""移动3G"}]},
	//{"id":27,"text","网络","options":[{"id":"98","optionname""移动3G"}]}]
	@Override
	public List<Map> findSpecIdsList(Long templateId) {
		TbTypeTemplate tbTypeTemplate = typeTemplateMapper.selectByPrimaryKey(templateId);
		//[{"id":27,"text":"网络"},{"id":32,"text":"机身内存"}]
		List<Map> list = JSON.parseArray(tbTypeTemplate.getSpecIds(),Map.class);
	    //根据id，查询规格选项  {"id":27,"text":"网络"}
		for(Map map : list){
			
			TbSpecificationOptionExample tbSpecificationOptionExample = new TbSpecificationOptionExample();
			com.cblue.pojo.TbSpecificationOptionExample.Criteria criteria = tbSpecificationOptionExample.createCriteria();
			criteria.andSpecIdEqualTo(new Long((Integer)map.get("id")));
			//[{"id":"98","optionname":"移动3G"},{"id":"99","optionname":"移动$G"}]
			List<TbSpecificationOption> tbSpecificationOptions=  tbSpecificationOptionMapper.selectByExample(tbSpecificationOptionExample);
			//把规格和规格选项，按照要去放一起
			map.put("options", tbSpecificationOptions);
			
		}
		return list;
	}
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbTypeTemplate> findAll() {
		return typeTemplateMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbTypeTemplate> page=   (Page<TbTypeTemplate>) typeTemplateMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbTypeTemplate typeTemplate) {
		typeTemplateMapper.insert(typeTemplate);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbTypeTemplate typeTemplate){
		typeTemplateMapper.updateByPrimaryKey(typeTemplate);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbTypeTemplate findOne(Long id){
		return typeTemplateMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			typeTemplateMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
	@Override
	public PageResult findPage(TbTypeTemplate typeTemplate, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbTypeTemplateExample example=new TbTypeTemplateExample();
		Criteria criteria = example.createCriteria();
		
		if(typeTemplate!=null){			
						if(typeTemplate.getName()!=null && typeTemplate.getName().length()>0){
				criteria.andNameLike("%"+typeTemplate.getName()+"%");
			}
			if(typeTemplate.getSpecIds()!=null && typeTemplate.getSpecIds().length()>0){
				criteria.andSpecIdsLike("%"+typeTemplate.getSpecIds()+"%");
			}
			if(typeTemplate.getBrandIds()!=null && typeTemplate.getBrandIds().length()>0){
				criteria.andBrandIdsLike("%"+typeTemplate.getBrandIds()+"%");
			}
			if(typeTemplate.getCustomAttributeItems()!=null && typeTemplate.getCustomAttributeItems().length()>0){
				criteria.andCustomAttributeItemsLike("%"+typeTemplate.getCustomAttributeItems()+"%");
			}
	
		}
		
		//添加缓存
		saveToRedis();
		
		Page<TbTypeTemplate> page= (Page<TbTypeTemplate>)typeTemplateMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}
	
	private void saveToRedis(){
		List<TbTypeTemplate> typeTemplateList = this.findAll();
		for(TbTypeTemplate tbTypeTemplate:typeTemplateList){
			//添加品牌
			List<Map> brandList = JSON.parseArray(tbTypeTemplate.getBrandIds(),Map.class);
			redisTemplate.boundHashOps("brand").put(tbTypeTemplate.getId(), brandList);
			
			//添加规格
			List<Map> specList = findSpecIdsList(tbTypeTemplate.getId());
			redisTemplate.boundHashOps("spec").put(tbTypeTemplate.getId(),specList);
		}
		System.out.println("保存了品牌和规格缓存");
	}

		
	
}
