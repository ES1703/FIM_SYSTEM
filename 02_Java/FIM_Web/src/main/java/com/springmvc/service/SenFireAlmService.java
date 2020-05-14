package com.springmvc.service;

import java.util.List;

import com.modle.service.BaseService;
import com.springmvc.entity.SenDht11;
import com.springmvc.entity.SenFireAlm;
import com.springmvc.entity.ModMain;
import com.springmvc.entity.ModSen;

/**
 * 火災警報感應資料Service介面
 * 
 * @author hrne
 *
 */
public interface SenFireAlmService extends BaseService<SenFireAlm> {

	/**
	 * 依據回傳Json儲存資料，若資料有錯誤則紀錄log不儲存
	 * 
	 * @param modMain 感應裝置主檔
	 * @param modSen 感應模組
	 * @param respJson 回傳Json
	 */
	void save_respJson(ModMain modMain, ModSen modSen, String respJson);
	
	/**
	 * 依據啟用感應裝置主檔查詢最新的火災警報感應資料
	 * 
	 * @return SenFireAlm list
	 */
	List<SenFireAlm> find_latest_modMain();

}