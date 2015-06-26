package com.kdyzm.servlet;
/*
 * ʹ��Ŀ¼��ɢʵ���ļ����������������ܡ�
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class CatalogBreakUp extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter pw=response.getWriter();
		
		//1.��ñ����ϴ��ļ���Ŀ¼��·���Լ������ļ���Ŀ¼
		String savaPath=this.getServletContext().getRealPath("upload");
		String tempPath=this.getServletContext().getRealPath("temp");
		
		//2.���ô����ļ���Ŀ�������ʵ�������û���Ŀ¼�Լ��ļ���С����
		DiskFileItemFactory dfif=new DiskFileItemFactory();
		dfif.setRepository(new File(tempPath));
		dfif.setSizeThreshold(1024*10);
		
		//3.���ServletFileUpload��ʵ��
		ServletFileUpload sfu=new ServletFileUpload(dfif);
		sfu.setSizeMax(1024*1024*3);//�����ϴ����ܴ�СΪ3MB
		sfu.setFileSizeMax(1024*1024);//����ÿ���ϴ����ļ���С���Ϊ1MB
		//4.�����ϴ��б�
		try {
			List<FileItem>list=sfu.parseRequest(request);
			Iterator<FileItem>it=list.iterator();
			
			while(it.hasNext())
			{
				FileItem file=it.next();
				String fileName=file.getName();
				fileName=fileName.substring(fileName.lastIndexOf("\\")+1);
				String fileType=file.getContentType();
				InputStream is=file.getInputStream();
				
				int hashCode=fileName.hashCode();
				String dir1=Integer.toHexString(hashCode&0XF);//�����һ��Ŀ¼
				String dir2=Integer.toHexString((hashCode>>4)&0XF);//����ڶ���Ŀ¼
				
				String aimDir=savaPath+"/"+dir1;
				if(!new File(aimDir).exists())
				{
					new File(aimDir).mkdir();
				}
				aimDir=aimDir+"/"+dir2;
				if(!new File(aimDir).exists())
				{
					new File(aimDir).mkdir();
				}
//				String extName=fileName.substring(fileName.lastIndexOf("."));
				String savaFileName=aimDir+"/";
				String extName=fileName.substring(fileName.lastIndexOf("."));
				fileName=UUID.randomUUID().toString().replaceAll("-", "")+extName;
				savaFileName=savaFileName+fileName;
				FileOutputStream fos=new FileOutputStream(new File(savaFileName));
				long fileSize=is.available();
				int length=-1;
				byte []buff=new byte[1024*1024];
				while((length=is.read(buff))!=-1){
					fos.write(buff, 0, length);
				}
				fos.close();
				is.close();
				file.delete();//��ջ����ļ�
				
				pw.println("�ļ��ϴ��ɹ���");
				pw.println("<br/>�ļ�����"+fileName);
				pw.println("<br/>�ļ���С��"+fileSize);
				pw.println("<br/>�ļ����ͣ�"+fileType);
				if(fileType.contains("image"))
				{
					pw.println("<br/>ͼƬ��");
					String temp=getServletContext().getContextPath()+"/upload/"+dir1+"/"+dir2+"/"+fileName;
					pw.println("<br/><img width='500' src='"+temp+"'/>");
				}
				pw.println("<hr/>");
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
	}
}
