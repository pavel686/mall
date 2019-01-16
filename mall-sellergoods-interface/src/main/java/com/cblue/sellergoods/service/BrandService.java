package com.cblue.sellergoods.service;

import java.util.List;
import java.util.Map;

import com.cblue.pojo.TbBrand;

import entity.PageResult;

public interface BrandService {
	
	/**
	 * select2获得品牌数据
	 */
	public List<Map> selectBrandList();
	
	/**
	 * 查询所有的品牌
	 */
	public List<TbBrand> getAllBrand();
	/**
	 * 分页查询
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	/**
	 * 条件分页查询
	 * @param pageNum 当前页码
	 * @param pageSize 每页记录数
	 */
	public PageResult findPage(TbBrand brand, int pageNum,int pageSize);
	
	/**
	 * 增加
	 */
	public void add(TbBrand brand);
	/**
	 * 根据ID获取实体
	 */
	public TbBrand findOne(Long id);	
    /**
	 * 修改
	 */
	public void update(TbBrand brand);
	
	/**
	 * 批量删除
	 */
	public void delete(Long [] ids);
	

}
