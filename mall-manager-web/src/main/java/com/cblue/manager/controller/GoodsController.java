package com.cblue.manager.controller;
import java.util.Arrays;
import java.util.List;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.naming.directory.SearchControls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.cblue.pojo.TbGoods;
import com.cblue.pojo.TbItem;
import com.cblue.pojogroup.Goods;
import com.cblue.sellergoods.service.GoodsService;
import com.cblue.sellergoods.service.ItemService;

import entity.PageResult;
import entity.Result;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;
	
	/*@Reference
	private SearchItemService searchItemService;*/
	
/*	@Reference(timeout=4000)
	private ItemPageService itemPageService;*/
	
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	
	@Autowired
	private Destination queueTextDestination;
	
	@Autowired
	private Destination topicgenHtmlDestination;
	
	
	//测试生成静态页
	@RequestMapping("/genHtml")
	public void genHtml(Long goodId){
		//itemPageService.genItemHtml(goodId);
	}
	
	
	/**
	 * 实现商品审核
	 * 修改audit_statue为1
	 */
	@RequestMapping("/updateStatus")
	public Result updateStatus(final Long[] ids ,String status){
		try {
			//修改audit_statue
			goodsService.updateState(ids, status);
			//查询审核通过商品
			List<TbItem> tbItemList =  goodsService.findTbListListByIdAndStatus(ids, status);
			System.out.println("tbItemList.size()="+tbItemList.size());
			if(tbItemList.size()>0){
				/*searchItemService.importList(tbItemList);*/
				final String tbItemListJSONStr = JSON.toJSONString(tbItemList);
				//发送消息
				jmsTemplate.send(queueTextDestination, new MessageCreator() {
					
					@Override
					public Message createMessage(Session session) throws JMSException {
						// TODO Auto-generated method stub
						return session.createTextMessage(tbItemListJSONStr);
					}
				});
				
			}else{
				System.out.println("没有新数据添加到索引库");
			}
			//生成静态页
			/*for(Long id:ids){
				itemPageService.genItemHtml(id);
			}*/
			
			jmsTemplate.send(topicgenHtmlDestination, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					// TODO Auto-generated method stub
					return session.createObjectMessage(ids);
				}
			});
			
			
			return new Result(true, "更新索引库成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new Result(false, "更新索引库失败");
		}
	
	}
	
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll(){			
		return goodsService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return goodsService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param goods
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody Goods goods){
		try {
			goodsService.add(goods);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody Goods goods){
		try {
			goodsService.update(goods);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public Goods findOne(Long id){
		return goodsService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			goodsService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param brand
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbGoods goods, int page, int rows  ){
		return goodsService.findPage(goods, page, rows);		
	}
	
}
