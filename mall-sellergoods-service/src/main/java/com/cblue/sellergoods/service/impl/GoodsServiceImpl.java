package com.cblue.sellergoods.service.impl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.cblue.mapper.TbBrandMapper;
import com.cblue.mapper.TbGoodsDescMapper;
import com.cblue.mapper.TbGoodsMapper;
import com.cblue.mapper.TbItemMapper;
import com.cblue.pojo.TbBrand;
import com.cblue.pojo.TbGoods;
import com.cblue.pojo.TbGoodsDesc;
import com.cblue.pojo.TbGoodsExample;
import com.cblue.pojo.TbGoodsExample.Criteria;
import com.cblue.pojo.TbItem;
import com.cblue.pojo.TbItemExample;
import com.cblue.pojogroup.Goods;
import com.cblue.sellergoods.service.GoodsService;
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
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private TbGoodsMapper goodsMapper;
	
	@Autowired
	private TbGoodsDescMapper goodsDescMapper;
	
	@Autowired
	private TbItemMapper tbItemMapper;
	
	@Autowired
	private TbBrandMapper tbBrandMapper;
	
	
	/**
	 * status是tb_item表中上下架的状态，如果是status=1，表示上架
	 */
	
	@Override
	public List<TbItem> findTbListListByIdAndStatus(Long[] ids,
			String status) {
		TbItemExample example=new TbItemExample();
		com.cblue.pojo.TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andGoodsIdIn(Arrays.asList(ids));
		criteria.andStatusEqualTo(status);
		return tbItemMapper.selectByExample(example);
	}
	
	@Override
	public void updateState(Long[] ids, String status) {
		// TODO Auto-generated method stub
		for(int i=0;i<ids.length;i++){
			TbGoods tbGoods = goodsMapper.selectByPrimaryKey(ids[i]);
			tbGoods.setAuditStatus(status);
			goodsMapper.updateByPrimaryKey(tbGoods);
		}
	}
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbGoods> findAll() {
		return goodsMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbGoods> page=   (Page<TbGoods>) goodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Goods goods) {
		//设置商品为未审核状态
		goods.getTbGoods().setAuditStatus("0");
		goodsMapper.insert(goods.getTbGoods());
		
		//int i = 3/0;
		
		//保存上面的描述信息
		goods.getTbGoodsDesc().setGoodsId(goods.getTbGoods().getId());
		goodsDescMapper.insert(goods.getTbGoodsDesc());
		System.out.println("goods.getTbGoods().getIsEnableSpec()="+goods.getTbGoods().getIsEnableSpec());
		
		setItemList(goods);

		
	}
	
	private void setItemList(Goods goods){
		if("1".equals(goods.getTbGoods().getIsEnableSpec())){
			for(TbItem tbItem:goods.getItemList()){
				//保存SKU信息
				//SKU的标题=商品名称+规格
				String title = goods.getTbGoods().getGoodsName();  //无火香薰精
				System.out.println(tbItem.getSpec());  //{"网络":"移动3G","机身内存":"16G"}
				Map<String,Object> specMap = JSON.parseObject(tbItem.getSpec());
				for(String key:specMap.keySet()){
					title += " "+key;  //无火香薰精  网络  机身内存
				}
				tbItem.setTitle(title);
				setItemValue(goods,tbItem);
				
				tbItemMapper.insert(tbItem);
				
				
			}
		}else{
			 TbItem tbItem = new TbItem();
			 tbItem.setTitle(goods.getTbGoods().getGoodsName());
			 tbItem.setPrice(goods.getTbGoods().getPrice());
			 tbItem.setIsDefault("1");
			 tbItem.setNum(100);
			 tbItem.setSpec("{}");
			 setItemValue(goods,tbItem);
			 tbItemMapper.insert(tbItem);
		}
	}
	
	
	private void setItemValue(Goods goods,TbItem tbItem){
		
		 //设置商品id
		tbItem.setGoodsId(goods.getTbGoods().getId());
		//商家id
		tbItem.setSellerId(goods.getTbGoods().getSellerId());
		//分类id--第三级分类的id
		tbItem.setCategoryid(goods.getTbGoods().getCategory3Id());
		//商品状态
		tbItem.setStatus("1");
		//创建时间，更新时间
		tbItem.setCreateTime(new Date());
		tbItem.setUpdateTime(new Date());
		
		//设定品牌
		TbBrand tbBrand = tbBrandMapper.selectByPrimaryKey(goods.getTbGoods().getBrandId());
		tbItem.setBrand(tbBrand.getName());
		
		
		//商品图片只显示上传的第一张图片
		List<Map> itemImages = JSON.parseArray(goods.getTbGoodsDesc().getItemImages(),Map.class);
		if(itemImages.size()>0){
			tbItem.setImage((String)itemImages.get(0).get("url"));
		}
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(Goods goods){
		goods.getTbGoods().setAuditStatus("0");//设置未申请状态:如果是经过修改的商品，需要重新设置状态
		goodsMapper.updateByPrimaryKey(goods.getTbGoods());//保存商品表
		goodsDescMapper.updateByPrimaryKey(goods.getTbGoodsDesc());//保存商品扩展表
		//删除原有的sku列表数据		
		TbItemExample example=new TbItemExample();
		com.cblue.pojo.TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(goods.getTbGoods().getId());	
		tbItemMapper.deleteByExample(example);
		//添加新的sku列表数据
		setItemList(goods);//插入商品SKU列表数据	
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public Goods findOne(Long id){
		Goods goods=new Goods();
		TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
		goods.setTbGoods(tbGoods);
		TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(id);
		goods.setTbGoodsDesc(tbGoodsDesc);
		//查询SKU商品列表
		TbItemExample example=new TbItemExample();
		com.cblue.pojo.TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(id);//查询条件：商品ID
		List<TbItem> itemList = tbItemMapper.selectByExample(example);		
		goods.setItemList(itemList);
		return goods;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			goodsMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbGoodsExample example=new TbGoodsExample();
		Criteria criteria = example.createCriteria();
		
		if(goods!=null){			
			if(goods.getSellerId()!=null && goods.getSellerId().length()>0){
				//criteria.andSellerIdLike("%"+goods.getSellerId()+"%");
				criteria.andSellerIdEqualTo(goods.getSellerId());
			}
			if(goods.getGoodsName()!=null && goods.getGoodsName().length()>0){
				criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
			}
			if(goods.getAuditStatus()!=null && goods.getAuditStatus().length()>0){
				criteria.andAuditStatusLike("%"+goods.getAuditStatus()+"%");
			}
			if(goods.getIsMarketable()!=null && goods.getIsMarketable().length()>0){
				criteria.andIsMarketableLike("%"+goods.getIsMarketable()+"%");
			}
			if(goods.getCaption()!=null && goods.getCaption().length()>0){
				criteria.andCaptionLike("%"+goods.getCaption()+"%");
			}
			if(goods.getSmallPic()!=null && goods.getSmallPic().length()>0){
				criteria.andSmallPicLike("%"+goods.getSmallPic()+"%");
			}
			if(goods.getIsEnableSpec()!=null && goods.getIsEnableSpec().length()>0){
				criteria.andIsEnableSpecLike("%"+goods.getIsEnableSpec()+"%");
			}
			if(goods.getIsDelete()!=null && goods.getIsDelete().length()>0){
				criteria.andIsDeleteLike("%"+goods.getIsDelete()+"%");
			}
	
		}
		
		Page<TbGoods> page= (Page<TbGoods>)goodsMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

		

		
	
}
