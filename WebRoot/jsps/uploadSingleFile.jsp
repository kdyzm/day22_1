<%@ page language="java" import="java.util.*" pageEncoding="utf-8" 
	contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!-- 简单的文件上传示例 -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <title>Insert title here!</title>
    <style type="text/css">
    	table{
    		border: 1px solid black;
    		border-collapse: collapse;
    		margin: 0 auto;
    		margin-top: 100px;
    	}
    	td {
			border: 1px solid black;
			text-align: center;
			padding: 3px;
		}
    </style>
  </head>
  
  <body>
	<form action="<c:url value='/uploadSingleFileServlet1'/>" method="post" enctype="multipart/form-data">
		<table>
			<tr>
				<td>文件</td>
				<td><input type="file" name="file"></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value="上传"></td>
			</tr>
		</table>
	</form>
  </body>
</html>
