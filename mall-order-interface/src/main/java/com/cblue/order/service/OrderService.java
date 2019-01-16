package com.cblue.order.service;
import java.util.List;

import com.cblue.pojo.TbOrder;
import com.cblue.pojo.TbPayLog;

import entity.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface OrderService {

	//从redis查询出支付日志对象
	public TbPayLog queryTbPayLogByUserId(String UserId);
	
	//支付之后，修改支付状态（支付日志状态，订单状态，从redis删除支付日志对象）
	/**
	 * @param orderIdList 支付日志中订单Id的集合
	 * @param transactionId 微信支付订单号
	 */
	public void updateOrderStatus(String orderIdList,String transactionId);
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbOrder> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbOrder order);
	
	
	/**
	 * 修改
	 */
	public void update(TbOrder order);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbOrder findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long [] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbOrder order, int pageNum,int pageSize);
	
}
