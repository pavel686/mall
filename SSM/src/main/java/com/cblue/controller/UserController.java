package com.cblue.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cblue.pojo.Users;
import com.cblue.service.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService userService;
	
	
	@RequestMapping("goMain")
	public String goMain(){
		return "main";
	}
	
	@RequestMapping("goUserlist")
	public String goUserlist(){
		return "userlist";
	}
	
	/**
	 * 添加日期，需要一个类型转换器
	 * 添加上传，需要上传组件
	 * 
	 */
	
	@RequestMapping("register")
	public void addUser(@Validated Users users,BindingResult br,@RequestParam(required=true)MultipartFile uploadFile,HttpServletRequest request,HttpServletResponse response){
		if(br.hasErrors()){
	        	//把校验的错误信息放到域中
	        	request.getSession().setAttribute("allErrors", br.getAllErrors());
	        	for(ObjectError objerror:br.getAllErrors()){
	        		System.out.println("***"+objerror.getDefaultMessage());
	        	}
	        	try {
					request.getRequestDispatcher("register.jsp").forward(request, response);
	        		return ;
				} catch (ServletException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	     }
		
		//先上传，后添加数据
		//1.确定上传文件保存的路径  /WebRoot/images
/*		System.out.println(uploadFile.isEmpty());
		System.out.println(uploadFile);*/
		if(!uploadFile.isEmpty()){
			 String savepath = request.getSession().getServletContext().getRealPath("images");
			 System.out.println("savepath="+savepath);
			 //2获得上传文件的文件名
			 String originalFile = uploadFile.getOriginalFilename(); //a.jpg
			//3.把上传的文件以指定的文件名保存到上传路径上
			 //确定上传文件的目录是否存在，如果存在，直接上传，如果不存在，我们创建上传目录
			  if(savepath!=null){
				  File saveFolder = new File(savepath);
				  if(!saveFolder.exists()){
					  saveFolder.mkdirs();  
				  }
				  //上传
				  try {
					uploadFile.transferTo(new File(savepath,originalFile));
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			  }
			 //把上传的路径返回给上传页面
			// request.setAttribute("imageurl", "images/"+fileName); 
			users.setImage("images/"+originalFile);
		}
		System.out.println("userss="+users);
		
		userService.addUser(users);
		System.out.println("---success---");
		try {
			request.getRequestDispatcher("login.jsp").forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping("Login")
	public String queryUserByNameAndPass(Users users,HttpServletRequest request,HttpServletResponse response){
		List<Users> userList = userService.queryUser(users);
		System.out.println("userList="+userList);
		if(userList!=null){
			return "redirect:goMain";
		}
		try {
			request.getRequestDispatcher("login.jsp").forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

	@RequestMapping("isExist")
	@ResponseBody
	public String queryUserIsExist(String username){
	  boolean isExist = userService.isExist(username);
	  return String.valueOf(isExist?0:1);
	}
	
}
