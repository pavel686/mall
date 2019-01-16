package com.cblue.manager.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cblue.pojo.TbBrand;
import com.cblue.sellergoods.service.BrandService;

import entity.PageResult;
import entity.Result;

@Controller
@RequestMapping("/brand")
public class BrandController {
	
	@Reference
	private BrandService brandService;
	
	@RequestMapping("/selectBrandList")
	@ResponseBody
	public List<Map> selectBrandList(){
		return brandService.selectBrandList();
	}
	
	
	@RequestMapping("/allBrand")
	@ResponseBody
	public List<TbBrand> findAll(){
		System.out.println("BrandController--findAll");
		return brandService.getAllBrand();
	}
	
	@RequestMapping("/findPage")
	@ResponseBody
	public PageResult findPage(int page,int rows){
		return brandService.findPage(page, rows);
	}
	
	/**
	 * 分页条件查询
	 */
	@RequestMapping("/search")
	@ResponseBody
	public PageResult search(@RequestBody TbBrand brand, int page, int rows){
		System.out.println("BrandController--search--"+brand.getFirstChar());
		return brandService.findPage(brand, page, rows);
		/*return brandService.findPage(page, rows);*/
	}
	
	@RequestMapping("/add")
	@ResponseBody
	public Result add(@RequestBody TbBrand brand){
		try {
			brandService.add(brand);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 获取实体
	 */
	@RequestMapping("/findOne")
	@ResponseBody
	public TbBrand findOne(Long id){
		return brandService.findOne(id);		
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@ResponseBody
	public Result update(@RequestBody TbBrand brand){
		try {
			brandService.update(brand);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}
	
	
	/**
	 * 批量删除
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Result delete(Long [] ids){
		System.out.println("------"+ids.length);
		try {
			brandService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}

}
