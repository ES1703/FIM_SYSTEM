package com.springmvc.dto;

import java.util.Date;

/**
 * 感應模組Dto
 * 
 * @author hrne
 *
 */
public class ModSenDto {

	private int id;

	/**
	 * 感應模組名稱
	 */
	private String senName;

	/**
	 * 感應模組代號
	 */
	private String senCode;

	/**
	 * 更新時間，透過SQL自動產生
	 */
	private Date updateDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 感應模組名稱
	 */
	public String getSenName() {
		return senName;
	}

	public void setSenName(String senName) {
		this.senName = senName;
	}

	/**
	 * 感應模組代號
	 */
	public String getSenCode() {
		return senCode;
	}

	public void setSenCode(String senCode) {
		this.senCode = senCode;
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
