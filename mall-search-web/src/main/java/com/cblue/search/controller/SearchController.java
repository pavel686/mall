package com.cblue.search.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cblue.search.SearchItemService;

@RestController
@RequestMapping("/itemsearch")
public class SearchController {

	@Reference
	private SearchItemService searchItemService;
	@RequestMapping("/search")
	public Map<String, Object> search(@RequestBody Map searchMap ){
		return  searchItemService.search(searchMap);
	}
}
