package com.cblue.sellergoods.service.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.cblue.mapper.TbSpecificationMapper;
import com.cblue.mapper.TbSpecificationOptionMapper;
import com.cblue.pojo.TbSpecification;
import com.cblue.pojo.TbSpecificationExample;
import com.cblue.pojo.TbSpecificationExample.Criteria;
import com.cblue.pojo.TbSpecificationOption;
import com.cblue.pojo.TbSpecificationOptionExample;
import com.cblue.pojogroup.Specification;
import com.cblue.sellergoods.service.SpecificationService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional
public class SpecificationServiceImpl implements SpecificationService {

	@Autowired
	private TbSpecificationMapper specificationMapper;
	@Autowired
	private TbSpecificationOptionMapper specificationOptionMapper;
	
	
	@Override
	public void add(Specification specification) {
		// TODO Auto-generated method stub
		//添加规格
		specificationMapper.insert(specification.getTbSpecification());
		//添加规格选项
		System.out.println("specification--add---"+specification.getTbSpecificationOptions());
		for(TbSpecificationOption option:specification.getTbSpecificationOptions()){
			option.setSpecId(specification.getTbSpecification().getId());
			specificationOptionMapper.insert(option);
		}
		
	}
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbSpecification> findAll() {
		return specificationMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbSpecification> page=   (Page<TbSpecification>) specificationMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbSpecification specification) {
		specificationMapper.insert(specification);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(Specification specification){
		//先修改规格
		specificationMapper.updateByPrimaryKey(specification.getTbSpecification());
		//先删除原有的规格，再重新插入
		TbSpecificationOptionExample emExample = new TbSpecificationOptionExample();
		com.cblue.pojo.TbSpecificationOptionExample.Criteria criteria = emExample.createCriteria();
		criteria.andSpecIdEqualTo(specification.getTbSpecification().getId());
		specificationOptionMapper.deleteByExample(emExample);
		
		for(TbSpecificationOption option:specification.getTbSpecificationOptions()){
			option.setSpecId(specification.getTbSpecification().getId());
			specificationOptionMapper.insert(option);
		}
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public Specification findOne(Long id){
		//查询的规格信息
		TbSpecification tbSpecification = specificationMapper.selectByPrimaryKey(id);
		//查询规格选项
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		com.cblue.pojo.TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		criteria.andSpecIdEqualTo(id);
		List<TbSpecificationOption> tbSpecificationOptions =specificationOptionMapper.selectByExample(example);
		
		Specification specification = new Specification();
		specification.setTbSpecification(tbSpecification);
		specification.setTbSpecificationOptions(tbSpecificationOptions);
		return specification;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			specificationMapper.deleteByPrimaryKey(id);
			//删除原有的规格选项		
			TbSpecificationOptionExample example=new TbSpecificationOptionExample();
			com.cblue.pojo.TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
			criteria.andSpecIdEqualTo(id);//指定规格ID为条件
			specificationOptionMapper.deleteByExample(example);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbSpecification specification, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbSpecificationExample example=new TbSpecificationExample();
		Criteria criteria = example.createCriteria();
		
		if(specification!=null){			
						if(specification.getSpecName()!=null && specification.getSpecName().length()>0){
				criteria.andSpecNameLike("%"+specification.getSpecName()+"%");
			}
	
		}
		
		Page<TbSpecification> page= (Page<TbSpecification>)specificationMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

		
	
}
