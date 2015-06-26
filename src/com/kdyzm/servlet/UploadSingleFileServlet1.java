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
		//�����ϴ������󣬶�ȡ�ϴ����ļ�
		//��ȡ����ϴ��ļ����ϴ��ļ���
		String path=this.getServletContext().getRealPath("/upload");
		//��һ��������һ�������ļ���Ŀ�����࣬����ָ��һ�����̿ռ����ϴ��ļ�
		DiskFileItemFactory dfif=new DiskFileItemFactory();
		dfif.setRepository(new File(this.getServletContext().getRealPath("/temp")));//��������Ŀ¼
		dfif.setSizeThreshold(1024*10);//С��10KB�����ڴ棬����10KB���浽�ļ�
		//�ڶ���������ServletFileUpload���������Ŀ¼
		ServletFileUpload sfu=new ServletFileUpload(dfif);
		//������������Request
		try {
			List<FileItem>list=sfu.parseRequest(request);//��������HttpServletRequest
			//����ֻ��һ���ļ�
			FileItem file=list.get(0);
			//��ȡ�ļ��������ļ�������·��
			String fileName=file.getName();
			fileName=fileName.substring(fileName.lastIndexOf("\\")+1);
			//��ȡ�ļ�����
			String fileType=file.getContentType();
			//��ȡ�����ֽ���
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
			is.close();//���������رգ�������ɾ����ʱ�ļ�
			//ɾ����ʱ�ļ��Ķ������������ر�֮��Ӧ�����ļ�ռ�õ�������ɵ�ʧ�ܡ�
			file.delete();
			PrintWriter pw=response.getWriter();
			pw.println("�ϴ��ɹ���<br/>�ļ���Ϊ��"+fileName);
			pw.println("<br/>�ļ���СΪ��"+fileSize+"Byte");
			pw.println("<br/>�ļ�����Ϊ��"+fileType);
			//ɾ����ʱ�ļ���������FileItem�����ɾ����ʵ�ʴ洢�ڴ����ϵ��ļ�Ҳ��ɾ��
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
	}
}
