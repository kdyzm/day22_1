package com.kdyzm.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
/*
 * 处理多个文件同时上传的情况的Servlet
 * @author kdyzm
 *
 */
public class UploadMultipleFilesServlet1 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		//首先获得存放上传文件目录的目录路径以及存放临时文件的目录
		String savaPath=this.getServletContext().getRealPath("/upload");
		String tempDir=this.getServletContext().getRealPath("/temp");
		
		//第一步：获取磁盘文件项目工厂并设置相关参数
		DiskFileItemFactory dfif=new DiskFileItemFactory();
		dfif.setRepository(new File(tempDir));
		dfif.setSizeThreshold(1024*10);
		
		//第二步：声明ServletFileUpload接收DiskFileItemFactory对象
		ServletFileUpload sfu=new ServletFileUpload(dfif);
		
		//第三步：开始接收请求并将文件保存到upload目录
		try {
			List<FileItem>list=sfu.parseRequest(request);
			//遍历List
			Iterator<FileItem>it=list.iterator();
			PrintWriter pw=response.getWriter();
			while(it.hasNext())
			{
				FileItem file=it.next();
				String fileName=file.getName();
				fileName=fileName.substring(fileName.lastIndexOf("\\")+1);
				String fileType=file.getContentType();
				InputStream is=file.getInputStream();
				long fileSize=is.available();
				FileOutputStream fos=new FileOutputStream(new File(savaPath+"/"+fileName));
				int length=-1;
				byte []buff=new byte[1024*1024];
				while((length=is.read(buff))!=-1)
				{
					fos.write(buff,0,length);
				}
				fos.close();
				is.close();
				file.delete();
				pw.println("上传成功！");
				pw.println("<br/>文件名："+fileName);
				pw.println("<br/>文件类型："+fileType);
				pw.println("<br/>文件大小："+fileSize+"Byte<hr/>");
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
	}

}
