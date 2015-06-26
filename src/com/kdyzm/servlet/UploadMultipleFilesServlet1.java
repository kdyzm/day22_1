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
 * �������ļ�ͬʱ�ϴ��������Servlet
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
		
		//���Ȼ�ô���ϴ��ļ�Ŀ¼��Ŀ¼·���Լ������ʱ�ļ���Ŀ¼
		String savaPath=this.getServletContext().getRealPath("/upload");
		String tempDir=this.getServletContext().getRealPath("/temp");
		
		//��һ������ȡ�����ļ���Ŀ������������ز���
		DiskFileItemFactory dfif=new DiskFileItemFactory();
		dfif.setRepository(new File(tempDir));
		dfif.setSizeThreshold(1024*10);
		
		//�ڶ���������ServletFileUpload����DiskFileItemFactory����
		ServletFileUpload sfu=new ServletFileUpload(dfif);
		
		//����������ʼ�������󲢽��ļ����浽uploadĿ¼
		try {
			List<FileItem>list=sfu.parseRequest(request);
			//����List
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
				pw.println("�ϴ��ɹ���");
				pw.println("<br/>�ļ�����"+fileName);
				pw.println("<br/>�ļ����ͣ�"+fileType);
				pw.println("<br/>�ļ���С��"+fileSize+"Byte<hr/>");
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
	}

}
