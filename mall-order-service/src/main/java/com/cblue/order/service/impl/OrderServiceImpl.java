package com.cblue.order.service.impl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.dubbo.config.annotation.Service;
import com.cblue.common.utils.IdWorker;
import com.cblue.mapper.TbOrderItemMapper;
import com.cblue.mapper.TbOrderMapper;
import com.cblue.mapper.TbPayLogMapper;
import com.cblue.order.service.OrderService;
import com.cblue.pojo.TbOrder;
import com.cblue.pojo.TbOrderExample;
import com.cblue.pojo.TbOrderExample.Criteria;
import com.cblue.pojo.TbOrderItem;
import com.cblue.pojo.TbPayLog;
import com.cblue.pojo.TbPayLogExample;
import com.cblue.pojogroup.Cart;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private TbOrderMapper orderMapper;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
	private IdWorker idWorker;
	
	@Autowired
	private TbOrderItemMapper tbOrderItemMapper;
	
	@Autowired
	private TbPayLogMapper tbPayLogMapper;

	
	@Override
	public void updateOrderStatus(String orderIdList,String transactionId) {
		// TODO Auto-generated method stub
		//修改支付日志的状态
		System.out.println("3333updateOrderStatus="+orderIdList);
		
		TbPayLogExample tbPayLogExample = new TbPayLogExample();
		com.cblue.pojo.TbPayLogExample.Criteria criteria = tbPayLogExample.createCriteria();
		criteria.andOrderListEqualTo(orderIdList);
		List<TbPayLog> tbPayLogs = (List<TbPayLog>) tbPayLogMapper.selectByExample(tbPayLogExample);
		TbPayLog tbPayLog = null;
		if(tbPayLogs.size()>0){
			tbPayLog = tbPayLogs.get(0);
			tbPayLog.setPayTime(new Date());
			tbPayLog.setTradeState("1");
			tbPayLog.setTransactionId(transactionId);
			tbPayLogMapper.updateByPrimaryKey(tbPayLog);
			
			//修改订单状态
			 //先获得订单号1062602252293767168,1062602252293767168
			String orderList = tbPayLog.getOrderList();
			String orderIds[] = orderList.split(",");
			for(String orderId:orderIds){
				TbOrder tbOrder = orderMapper.selectByPrimaryKey(Long.valueOf(orderId));
				tbOrder.setStatus("2");
			}
			
			//清除redis中支付日志信息
			redisTemplate.boundHashOps("paylog").delete(tbPayLog.getUserId());
		}
		
		
	}
	
	@Override
	public TbPayLog queryTbPayLogByUserId(String UserId) {
		// TODO Auto-generated method stub
		return (TbPayLog) redisTemplate.boundHashOps("paylog").get(UserId);
	}
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbOrder> findAll() {
		return orderMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbOrder> page=   (Page<TbOrder>) orderMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 * 把结算页面中的数据保存到order表里
	 */
	@Override
	public void add(TbOrder order) {
		//得到购物车的数据
		List<Cart> cartList = (List<Cart>)redisTemplate.boundHashOps("cartlist").get(order.getUserId());
		//创建一个集合
		List<String> orderIds = new ArrayList<String>();
		//支付总金额
		double payTotalMoney = 0;
		
		for(Cart cart: cartList){
			//把购物车的数据都放到order中
			long orderId  = idWorker.nextId();
			TbOrder tbOrder = new TbOrder();
			tbOrder.setOrderId(orderId);
			tbOrder.setUserId(order.getUserId());
			tbOrder.setPaymentType(order.getPaymentType());
			tbOrder.setStatus("1");
			tbOrder.setCreateTime(new Date());
			tbOrder.setUpdateTime(new Date());
			tbOrder.setReceiverAreaName(order.getReceiverAreaName());
			tbOrder.setReceiverMobile(order.getReceiverMobile());
			tbOrder.setReceiver(order.getReceiver());
			tbOrder.setSourceType(order.getSourceType());
			tbOrder.setSellerId(cart.getSellerId());
			
			double money =0;
			//购物车的清单
			for(TbOrderItem orderItem:cart.getOrderItemList()){
				orderItem.setId(idWorker.nextId());
				orderItem.setOrderId(orderId);
				orderItem.setSellerId(cart.getSellerId());
				orderItem.setPicPath(orderItem.getPicPath());
				//总价
				money += orderItem.getTotalFee().doubleValue();
				//保存购物车商品信息
				
				tbOrderItemMapper.insert(orderItem);
			}
			//总价
			tbOrder.setPayment(new BigDecimal(money));
			System.out.println("1111---addorder="+tbOrder.getOrderId());
			orderMapper.insert(tbOrder);
			
			orderIds.add(orderId+"");
			payTotalMoney+=money;

		}
		
		//添加一条支付日志
		if(order.getPaymentType().equals("1")){
			TbPayLog tbPayLog = new TbPayLog();
			tbPayLog.setOutTradeNo(idWorker.nextId()+"");
			tbPayLog.setCreateTime(new Date());
			tbPayLog.setTotalFee((long)payTotalMoney); //支付的总金额
			tbPayLog.setUserId(order.getUserId());
			tbPayLog.setTradeState("0");//未支付
			System.out.println("222---paylogOrderId="+orderIds.toString());
			tbPayLog.setOrderList(orderIds.toString().replace("[","").replace("]",""));
			tbPayLog.setPayType("1");
			tbPayLogMapper.insert(tbPayLog);
			redisTemplate.boundHashOps("paylog").put(order.getUserId(),tbPayLog);
		}
		
		//把redis中数据删掉
		redisTemplate.boundHashOps("cartlist").delete(order.getUserId());

			
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbOrder order){
		orderMapper.updateByPrimaryKey(order);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbOrder findOne(Long id){
		return orderMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			orderMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbOrder order, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbOrderExample example=new TbOrderExample();
		Criteria criteria = example.createCriteria();
		
		if(order!=null){			
						if(order.getPaymentType()!=null && order.getPaymentType().length()>0){
				criteria.andPaymentTypeLike("%"+order.getPaymentType()+"%");
			}
			if(order.getPostFee()!=null && order.getPostFee().length()>0){
				criteria.andPostFeeLike("%"+order.getPostFee()+"%");
			}
			if(order.getStatus()!=null && order.getStatus().length()>0){
				criteria.andStatusLike("%"+order.getStatus()+"%");
			}
			if(order.getShippingName()!=null && order.getShippingName().length()>0){
				criteria.andShippingNameLike("%"+order.getShippingName()+"%");
			}
			if(order.getShippingCode()!=null && order.getShippingCode().length()>0){
				criteria.andShippingCodeLike("%"+order.getShippingCode()+"%");
			}
			if(order.getUserId()!=null && order.getUserId().length()>0){
				criteria.andUserIdLike("%"+order.getUserId()+"%");
			}
			if(order.getBuyerMessage()!=null && order.getBuyerMessage().length()>0){
				criteria.andBuyerMessageLike("%"+order.getBuyerMessage()+"%");
			}
			if(order.getBuyerNick()!=null && order.getBuyerNick().length()>0){
				criteria.andBuyerNickLike("%"+order.getBuyerNick()+"%");
			}
			if(order.getBuyerRate()!=null && order.getBuyerRate().length()>0){
				criteria.andBuyerRateLike("%"+order.getBuyerRate()+"%");
			}
			if(order.getReceiverAreaName()!=null && order.getReceiverAreaName().length()>0){
				criteria.andReceiverAreaNameLike("%"+order.getReceiverAreaName()+"%");
			}
			if(order.getReceiverMobile()!=null && order.getReceiverMobile().length()>0){
				criteria.andReceiverMobileLike("%"+order.getReceiverMobile()+"%");
			}
			if(order.getReceiverZipCode()!=null && order.getReceiverZipCode().length()>0){
				criteria.andReceiverZipCodeLike("%"+order.getReceiverZipCode()+"%");
			}
			if(order.getReceiver()!=null && order.getReceiver().length()>0){
				criteria.andReceiverLike("%"+order.getReceiver()+"%");
			}
			if(order.getInvoiceType()!=null && order.getInvoiceType().length()>0){
				criteria.andInvoiceTypeLike("%"+order.getInvoiceType()+"%");
			}
			if(order.getSourceType()!=null && order.getSourceType().length()>0){
				criteria.andSourceTypeLike("%"+order.getSourceType()+"%");
			}
			if(order.getSellerId()!=null && order.getSellerId().length()>0){
				criteria.andSellerIdLike("%"+order.getSellerId()+"%");
			}
	
		}
		
		Page<TbOrder> page= (Page<TbOrder>)orderMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

		

		
	
}
