package com.springmvc.task;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import com.modle.util.ApplicationContextUtil;
import com.springmvc.entity.ModData;
import com.springmvc.entity.ModSen;
import com.springmvc.service.SenDht11Service;
import com.springmvc.service.ModDataService;
import com.springmvc.service.ModRespLogService;

/**
 * 掃描感應裝置
 * 
 * @author hrne
 *
 */
@Component
public class SensorClient {

	// 感應裝置service
	ModDataService modDataService;

	// 感應裝置更新紀錄serivce
	ModRespLogService modRespLogService;

	// 溫濕度dht11感應資料serivce
	SenDht11Service senDht11Service;

	// 每5秒掃描一次
	//@Scheduled(cron = "0/5 * * * * ? ")
	public void startClient() {

		modDataService = (ModDataService) ApplicationContextUtil.getBean("modDataService");
		modRespLogService = (ModRespLogService) ApplicationContextUtil.getBean("modRespLogService");
		senDht11Service = (SenDht11Service) ApplicationContextUtil.getBean("senDht11Service");

		System.out.println("start scan");

		// 查詢所有啟用感應裝置
		List<ModData> scanMachList = modDataService.findByModEnable();
		
		//掃描每一台感應裝置
		for (ModData modData : scanMachList) {
			for (ModSen modSen : modData.getModSenSet()) {
				// 連線感應器
				String str = getModData(modSen, modSen.getSenCode());
				if (str != null) {
					//儲存dht11資料
					senDht11Service.createDht11(scanMach, str);
				}

			}
		}
	}

	/**
	 * 連線感應裝置讀取資料
	 * 
	 * @param scanMach 要掃描感應裝置
	 * @param modCode  感應模組代號
	 * @return 回傳json格式資料
	 */
	public String getModData(ModData scanMach, String modCode) {

		CloseableHttpClient httpCilent = HttpClients.createDefault();

		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(5000)
				.setConnectionRequestTimeout(5000).setSocketTimeout(5000).setRedirectsEnabled(true).build();

		// 傳送ip+感應器代號
		HttpGet httpGet = new HttpGet("http://" + scanMach.getIp() + "/" + modCode);
		httpGet.setConfig(requestConfig);

		String respJsonStr = null;
		try {

			// 讀取感應裝置
			HttpResponse httpResponse = httpCilent.execute(httpGet);

			int statusCode = httpResponse.getStatusLine().getStatusCode();

			// 接受感應器回傳資料
			// status code:200 代表成功
			if (statusCode == 200) {
				// 轉換格式
				respJsonStr = EntityUtils.toString(httpResponse.getEntity());
				// 成功:訊息紀錄收到資料
				senRespLogService.createRespLog(scanMach, true, respJsonStr);
			} else {
				// 失敗:訊息紀錄錯誤代碼
				senRespLogService.createRespLog(scanMach, false, String.valueOf(statusCode));
			}

			System.out.println("status code:    " + statusCode + "   content:   " + respJsonStr);

		} catch (IOException e) {
			// 連線意外失敗:紀錄錯誤訊息
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			senRespLogService.createRespLog(scanMach, false, "連線意外失敗:" + errors.toString());
		} finally {
			try {
				httpCilent.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return respJsonStr;
	}

}
