package com.cblue.sellergoods.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.cblue.mapper.TbBrandMapper;
import com.cblue.pojo.TbBrand;
import com.cblue.pojo.TbBrandExample;
import com.cblue.pojo.TbBrandExample.Criteria;
import com.cblue.sellergoods.service.BrandService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import entity.PageResult;

@Service
public class BrandServiceImpl implements BrandService {
	
	@Autowired
	private TbBrandMapper tbBrandMapper;
	
	@Override
	public List<Map> selectBrandList() {
		// TODO Auto-generated method stub
		return tbBrandMapper.selectBrandList();
	}


	public List<TbBrand> getAllBrand() {
		return tbBrandMapper.selectByExample(null);
	}

	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		//得到最开始的分页数据
		PageHelper.startPage(pageNum, pageSize);
		//查询所有数据，把数据进行分页处理，放到page对象中
		Page<TbBrand> page = (Page)tbBrandMapper.selectByExample(null);
		
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public PageResult findPage(TbBrand brand, int pageNum, int pageSize) {
		// TODO Auto-generated method stub
		//得到最开始的分页数据
		PageHelper.startPage(pageNum, pageSize);
		//查询所有数据，把数据进行分页处理，放到page对象中
		TbBrandExample tbBrandExample = new TbBrandExample();
		Criteria criteria = tbBrandExample.createCriteria();
		if(brand!=null){
			if(brand.getName()!=null&&brand.getName().length()>0){
				criteria.andNameLike("%"+brand.getName()+"%");
			}
			if(brand.getFirstChar()!=null&&brand.getFirstChar().length()>0){
				criteria.andFirstCharEqualTo(brand.getFirstChar());
			}
		}
		Page<TbBrand> page = (Page)tbBrandMapper.selectByExample(tbBrandExample);
				
		return new PageResult(page.getTotal(), page.getResult());
	}
	
	@Override
	public void add(TbBrand brand) {
		// TODO Auto-generated method stub
		tbBrandMapper.insert(brand);
	}

	@Override
	public TbBrand findOne(Long id) {
		// TODO Auto-generated method stub
		return tbBrandMapper.selectByPrimaryKey(id);
	}

	@Override
	public void update(TbBrand brand) {
		// TODO Auto-generated method stub
		tbBrandMapper.updateByPrimaryKey(brand);
	}

	@Override
	public void delete(Long[] ids) {
		// TODO Auto-generated method stub
		for(int i=0;i<ids.length;i++){
			tbBrandMapper.deleteByPrimaryKey(ids[i]);
		}
		
	}

	
	

}
