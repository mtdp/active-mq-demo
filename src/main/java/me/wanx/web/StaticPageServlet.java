package me.wanx.web;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class StaticPageServlet
 */
public class StaticPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public void service(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException { 
		String chapterId = request.getParameter("id");
	    if(chapterId != null){ 
	        String chapterFileName = "index_"+ chapterId +".html"; 
	        String chapterFilePath = getServletContext().getRealPath("/") + "/" + chapterFileName; 
	        System.out.println(chapterFilePath);
	        File chapterFile = new File(chapterFilePath); 
	        if(chapterFile.exists()){response.sendRedirect(chapterFileName);return;}//如果有这个文件就告诉浏览器转向  
	        request.setAttribute("testValue", "test index value"+chapterId);
	        new CreateStaticHTMLPage().createStaticHTMLPage(request, response, getServletContext(),  
	                chapterFileName, chapterFilePath, "/index.jsp"); 
	    } 
	} 

}
