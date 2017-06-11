package cn.e3mall.fast;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import cn.e3mall.common.utils.FastDFSClient;

public class FastDfsTest {
	
//	@Test
//	public void testImageUpload() throws Exception{
//		//加载配置文件
//		ClientGlobal.init("G:/myeclipse_workspace/e3-manager-web/src/main/resources/conf/client.conf");
//		
//		//创建一个TrackerClient对象
//		TrackerClient trackerClient = new TrackerClient();
//		
//		//利用TrackerClient对象获取一个TrackerServer对象
//		TrackerServer trackerServer = trackerClient.getConnection();
//		
//		//创建一个StorageServer引用
//		StorageServer storageServer = null;
//		
//		//创建一个StorageClient对象
//		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
//		
//		//使用StorageClient对象实现图片上传
//		//三个参数分别是:全路径、后缀名、元数据
//		String[] strings = storageClient.upload_file("D:/图/air/1177072575.jpg", "jpg", null);
//		for (String string : strings) {
//			System.out.println(string);
//		}
//		
//	}
//	
//	//测试工具类
//	@Test
//	public void testFastDfsUtils() throws Exception {
//		FastDFSClient fastDFSClient = new FastDFSClient("G:/myeclipse_workspace/e3-manager-web/src/main/resources/conf/client.conf");
//		String uploadFile = fastDFSClient.uploadFile("D:/图/air/鸟之诗.jpg");
//		System.out.println(uploadFile);
//	}
}
