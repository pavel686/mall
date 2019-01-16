package com.cblue.page.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import com.alibaba.dubbo.config.annotation.Service;
import com.cblue.mapper.TbGoodsDescMapper;
import com.cblue.mapper.TbGoodsMapper;
import com.cblue.mapper.TbItemCatMapper;
import com.cblue.mapper.TbItemMapper;
import com.cblue.page.service.ItemPageService;
import com.cblue.pojo.TbGoods;
import com.cblue.pojo.TbGoodsDesc;
import com.cblue.pojo.TbItem;
import com.cblue.pojo.TbItemExample;
import com.cblue.pojo.TbItemExample.Criteria;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Service
public class ItemPageServiceImpl implements ItemPageService {
	
	@Autowired
	private FreeMarkerConfig freeMarkerConfig;
	
	@Autowired
	private TbGoodsMapper goodsMapper;
	
	@Autowired
	private TbGoodsDescMapper goodsDescMapper;
	
	@Autowired
	private TbItemCatMapper itemCatMapper;
	
	@Autowired
	private TbItemMapper itemMapper;
	
	@Value("${pageDir}")
	private String pageDir;

	@Override
	public boolean genItemHtml(Long goodId) {
	
		try {
			Configuration configuration = freeMarkerConfig.getConfiguration();
			Template template = configuration.getTemplate("item.ftl");
			Map map = new HashMap();
			
			//查询商品
			TbGoods tbGoods = goodsMapper.selectByPrimaryKey(goodId);
			map.put("good", tbGoods);
			
			//查询三级分类
			String category01Name = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory1Id()).getName();
			String category02Name = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory2Id()).getName();
			String category03Name = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id()).getName();
			
			map.put("category01Name", category01Name);
			map.put("category02Name", category02Name);
			map.put("category03Name", category03Name);
			
			
			
			//查询商品描述
			TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(goodId);
			map.put("goodDesc", tbGoodsDesc);
			
			
			//根据商品id，查询对应的SUK列表
			TbItemExample tbItemExample = new TbItemExample();
			Criteria criteria = tbItemExample.createCriteria();
			criteria.andStatusEqualTo("1");
			criteria.andGoodsIdEqualTo(goodId);
			//设定根据is_default进行降序排序
			tbItemExample.setOrderByClause("is_default desc");
			List<TbItem> items = itemMapper.selectByExample(tbItemExample);
			map.put("items",items);
			
			//可以生成静态页
			Writer write = new FileWriter(new File(pageDir+goodId+".html"));
			template.process(map, write);
			write.close();
			
			return true;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean deleteItemHtml(Long[] goodIds) {
		// TODO Auto-generated method stub
		try {
			for(Long id:goodIds){
				System.out.println("删除文件："+pageDir+id+".html");
				new File(pageDir+id+".html").delete();
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		
	}

}
