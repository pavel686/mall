package com.cblue.main.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cblue.content.service.ContentService;
import com.cblue.pojo.TbContent;

@RestController
public class ContentController {
	

	@Reference
	private ContentService contentService;
	
	@RequestMapping("/findCotentByCategoryId")
	public List<TbContent> findContentByCategoryId(Long categoryId){
		return contentService.findContentByCategoryId(categoryId);
	}

}
