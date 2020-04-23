package com.springmvc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.modle.util.ObjectMapperUtils;
import com.springmvc.dto.ModDataDto;
import com.springmvc.entity.ModData;
import com.springmvc.entity.ModSen;
import com.springmvc.service.ModDataService;
import com.springmvc.service.ModSenService;
import com.springmvc.validator.ModDataFormValidator;

/**
 * 感應裝置controller
 * 
 * @author hrne
 *
 */
@Controller
public class ModDataController {

	@Autowired
	ModDataFormValidator modDataFormValidator;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(modDataFormValidator);
	}

	@Autowired
	private ModDataService modDataService;

	@Autowired
	private ModSenService modSenService;

	@Autowired
	private ResourceBundleMessageSource messageSource;

	/**
	 * 感應裝置列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/modData/list", method = RequestMethod.GET)
	public String showAllModData(Model model) {

		// 查詢所有感應裝置
		List<ModData> listModData = modDataService.findAll();

		// 將感應裝置map到DTO上供頁面顯示
		List<ModDataDto> listModDataDto = ObjectMapperUtils.mapAll(listModData, ModDataDto.class);
		
		//資料放至頁面
		model.addAttribute("listModDataDto", listModDataDto);
		
		//導頁至感應裝置列表
		return "modData/listModData";
	}

	/**
	 * 新增感應裝置
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/modData/add", method = RequestMethod.GET)
	public String showAddModDataForm(Model model) {
		ModDataDto modDataDto = new ModDataDto();
		
		//將DTO放至頁面以暫存資料
		model.addAttribute("modDataDto", modDataDto);
		
		//產生頁面選單資料
		createFormOptions(model);
		
		//導頁至新增頁面
		return "modData/modDataForm";
	}

	/**
	 * 修改感應裝置
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/modData/{id}/update", method = RequestMethod.GET)
	public String showUpdateModDataForm(@PathVariable("id") int id, Model model) {

		// 查詢感應裝置
		ModData modData = modDataService.findByPK(id);

		// 將感應裝置map到DTO上
		ModDataDto modDataDto = ObjectMapperUtils.map(modData, ModDataDto.class);

		List<Integer> modSenIDs = new ArrayList<Integer>();

		// 將現有的感應裝置放入顯示
		for (ModSen modSen : modData.getModSenSet()) {
			modSenIDs.add(modSen.getId());
		}		
		modDataDto.setModSenIDs(modSenIDs);
		
		//資料放至頁面
		model.addAttribute("modDataDto", modDataDto);

		//產生頁面選單資料
		createFormOptions(model);
		
		//導頁至修改頁面
		return "modData/modDataForm";
	}
	
	/**
	 * 儲存感應裝置 from add or update
	 * 
	 * @param modDataDto
	 * @param result
	 * @param model
	 * @param redirectAttributes
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "/modData/save", method = RequestMethod.POST)
	public String saveOrUpdateModData(@ModelAttribute("senMachForm") @Validated ModDataDto modDataDto, BindingResult result,
			Model model, final RedirectAttributes redirectAttributes, Locale locale) {
		if (result.hasErrors()) {
			createFormOptions(model);
			return "modData/modDataForm";
		} else {
			
			//儲存資料
			modDataService.saveModDataByDto(modDataDto);

			//顯示回傳訊息
			redirectAttributes.addFlashAttribute("css", "success");
			if (modDataDto.isNew()) {
				redirectAttributes.addFlashAttribute("msg", messageSource.getMessage("senMachAddSucess", new Object[] { }, locale));
			} else {
				redirectAttributes.addFlashAttribute("msg", messageSource.getMessage("senMachUpdateSucess", new Object[] { }, locale));
			}
			return "redirect:/modData/list";
		}
	}

	/**
	 * 產生頁面資訊
	 * 
	 * @param model
	 */
	private void createFormOptions(Model model) {
		
		// 查詢所有感應模組
		List<ModSen> listModSen = modSenService.findAll();

		Map<Integer, String> senList = new HashMap<>();
		
		// 感應模組選單
		for (ModSen modSen : listModSen) {
			senList.put(modSen.getId(), modSen.getSenName());
		}

		model.addAttribute("senList", senList);

	}

}