package com.cblue.seller.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cblue.common.utils.FastDFSClient;

import entity.Result;

@RestController
public class UploadContorller {
	
	@Value("${FILE_SERVER_URL}")
	private String FILE_SERVER_URL;
	
	@RequestMapping("/upload")
	public Result upload(MultipartFile file){
		try {
			//文件后缀
			String fileName = file.getOriginalFilename();
			
			//错误String extName = fileName.substring(fileName.indexOf("."), fileName.length()+1);
			String extName = fileName.substring(fileName.indexOf(".")+1);
			//这个配置文件是fastDFS的jar包默认提供给我们的配置文件
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/fdfs_client.conf");
			String path = fastDFSClient.uploadFile(file.getBytes(), extName);
			System.out.println("path="+path);
			//把完整的图片地址返回出来
			String imageUrl = FILE_SERVER_URL + path;
			System.out.println("imageUrl="+imageUrl);
			return new Result(true, imageUrl);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new Result(false, "上传失败");
		}
		
	}

}
