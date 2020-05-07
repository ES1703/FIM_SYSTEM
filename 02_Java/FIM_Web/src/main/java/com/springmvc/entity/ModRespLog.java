package com.springmvc.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 感應紀錄
 * 
 * @author hrne
 *
 */
@Entity
@Table(name = "Mod_Resp_Log")
public class ModRespLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	/**
	 * 感應裝置id
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "mod_data_id")
	private ModData modData;

	/**
	 * 感應模組id
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "mod_sen_id")
	private ModSen modSen;

	/**
	 * 回傳狀態 <br/> 
	 * 00:成功 <br/>
	 * 01:感應裝置連線失敗 <br/>
	 * 02:讀取不到感應模組資料 <br/>
	 * 99:未知原因
	 */
	@Column(name = "status_code")
	private String statusCode;

	/**
	 * 回傳訊息
	 */
	@Column(name = "resp_message")
	private String respMessage;

	/**
	 * 更新時間，透過SQL自動產生
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_date", insertable = false, updatable = false)
	private Date updateDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 感應裝置id
	 */
	public ModData getModData() {
		return modData;
	}

	public void setModData(ModData modData) {
		this.modData = modData;
	}

	/**
	 * 感應模組id
	 */
	public ModSen getModSen() {
		return modSen;
	}

	public void setModSen(ModSen modSen) {
		this.modSen = modSen;
	}

	/**
	 * 回傳狀態 <br/> 
	 * 00:成功 <br/>
	 * 01:感應裝置連線失敗 <br/>
	 * 02:讀取不到感應模組資料 <br/>
	 * 99:未知原因
	 */
	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * 回傳訊息
	 */
	public String getRespMessage() {
		return respMessage;
	}

	public void setRespMessage(String respMessage) {
		this.respMessage = respMessage;
	}

	/**
	 * 更新時間，透過SQL自動產生
	 */
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

}