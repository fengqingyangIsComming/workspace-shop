package com.taotao.fdfs;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import com.taotao.utils.FastDFSClient;

public class TestFDFS {

	@Test
	public void testUpLoad()throws Exception{
		//1，创建配置文件fast_dfs.conf 内容即指定trackerServer的地址
		//2，加载配置文件
		ClientGlobal.init("D:/workspace-taotao/taotao-manager-web/src/main/resources/fast_dfs.conf");
		//3，创建一个trackerClient对象
		TrackerClient trackerClient = new TrackerClient();
		//4，通过trackerClient对象获得trackerServer
		TrackerServer trackerServer = trackerClient.getConnection();
		//5，创建一个StorageServer的引用，null就可以
		StorageServer storageServer=null;
		//6，创建一个StorageClient对象，两个参赛trackerServer，StorageServer
		StorageClient storageClient= new StorageClient(trackerServer, storageServer);
		//7，使用StorageClient上传文件 
		//参数1：文件名 参数2：扩展名，不包含“.” 参数3：文件元数据，这里为null
		String[] upload_file = storageClient.upload_file("D:/pic/Beach.jpg", "jpg", null);
		for (String string : upload_file) {
			System.out.println(string);
		}
		//group1
//		M00/00/00/wKgZhVhlubiAaF1dABD0Nv5Q9xo082.jpg
		
	}
	@Test
	public void testFastDFSClient()throws Exception{
		FastDFSClient client = new FastDFSClient("D:/workspace-taotao/taotao-manager-web/src/main/resources/fast_dfs.conf");
		String uploadFile = client.uploadFile("D:/pic/Lion.jpg");
		System.out.println(uploadFile);//group1/M00/00/00/wKgZhVhlu6OAHbXjAArmq8j3O5U334.jpg
	}
}
