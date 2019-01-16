package com.cblue.fastdfsdemo;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

public class TestFastDFS {
	
	public static void main(String[] args) throws Exception {
		//加载配置文件
		ClientGlobal.init("E:\\Workspaces_MALL\\fastdfsdemo\\src\\main\\resources\\fdfs_client.conf");
	    //创建Tracker对象
		TrackerClient trackerClient = new TrackerClient();
		//创建TrackerServer对象，获得连接
		TrackerServer trackerServer = trackerClient.getConnection();
		//创建一个空的StorageServer对象
		StorageServer storageServer = null;
		//创建一个StroageClient对象
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		//上传  参数1 文件绝对地址  参数2是文件的后缀  参数3 不需要
		String [] path = storageClient.upload_file("E:\\yyy.jpg", "jpg",null);
		for(int i=0;i<path.length;i++){
			System.out.println(path[i]);
		}
		
	
	}

}
