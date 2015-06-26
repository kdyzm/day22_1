package com.kdyzm.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadSingleFileServlet1 extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		//接收上传的请求，读取上传的文件
		//获取存放上传文件的上传文件夹
		String path=this.getServletContext().getRealPath("/upload");
		//第一步：声明一个磁盘文件项目工厂类，用于指定一个磁盘空间存放上传文件
		DiskFileItemFactory dfif=new DiskFileItemFactory();
		dfif.setRepository(new File(this.getServletContext().getRealPath("/temp")));//声明缓存目录
		dfif.setSizeThreshold(1024*10);//小于10KB留在内存，大于10KB保存到文件
		//第二步：声明ServletFileUpload接收上面的目录
		ServletFileUpload sfu=new ServletFileUpload(dfif);
		//第三步：解析Request
		try {
			List<FileItem>list=sfu.parseRequest(request);//解析请求HttpServletRequest
			//假设只有一个文件
			FileItem file=list.get(0);
			//获取文件名，改文件名带有路径
			String fileName=file.getName();
			fileName=fileName.substring(fileName.lastIndexOf("\\")+1);
			//获取文件类型
			String fileType=file.getContentType();
			//获取输入字节流
			InputStream is=file.getInputStream();
			long fileSize=is.available();
			FileOutputStream fos =new FileOutputStream(new File(path+"/"+fileName));
			int length=-1;
			byte []buff=new byte[1024*1024];
			while((length=is.read(buff))!=-1)
			{
				fos.write(buff, 0, length);
			}
			fos.close();
			is.close();//这个流必须关闭，否则不能删除临时文件
			//删除临时文件的动作必须在流关闭之后。应当是文件占用的问题造成的失败。
			file.delete();
			PrintWriter pw=response.getWriter();
			pw.println("上传成功！<br/>文件名为："+fileName);
			pw.println("<br/>文件大小为："+fileSize+"Byte");
			pw.println("<br/>文件类型为："+fileType);
			//删除临时文件，伴随着FileItem对象的删除，实际存储在磁盘上的文件也被删除
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
	}
}
