package com.springmvc.service;

import java.util.List;

import com.modle.service.BaseService;
import com.springmvc.entity.SenHx711;

/**
 * 重量hx711感應資料的Service介面
 * 
 * @author hrne
 *
 */
public interface SenHx711Service extends BaseService<SenHx711> {
	
	/**
	 * 查詢每個啟用的感應裝置最新一筆重量資料
	 * 
	 * @return SenHx711 list 重量列表
	 */
	List<SenHx711> findLatestHx711Data();

}