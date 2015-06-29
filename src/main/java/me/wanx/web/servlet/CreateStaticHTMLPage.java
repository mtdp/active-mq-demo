package me.wanx.web.servlet;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/** 
 * 生成静态HTML页面的方法 
 * @param request 请求对象 
 * @param response 响应对象 
 * @param servletContext Servlet上下文 
 * @param fileName 文件名称 
 * @param fileFullPath 文件完整路径 
 * @param jspPath 需要生成静态文件的JSP路径(相对即可) 
 * @throws IOException 
 * @throws ServletException 
 */ 
public class CreateStaticHTMLPage {
    public void createStaticHTMLPage(HttpServletRequest request, HttpServletResponse response,ServletContext servletContext,
    								String fileName,String fileFullPath,String jspPath) throws ServletException, IOException{ 
        response.setContentType("text/html;charset=UTF-8");//设置HTML结果流编码(即HTML文件编码)  
        RequestDispatcher rd = servletContext.getRequestDispatcher(jspPath);//得到JSP资源  
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();//用于从ServletOutputStream中接收资源  
        final ServletOutputStream servletOuputStream = new ServletOutputStream(){//用于从HttpServletResponse中接收资源  
            public void write(byte[] b, int off,int len){ 
                byteArrayOutputStream.write(b, off, len); 
            } 
            public void write(int b){ 
                byteArrayOutputStream.write(b); 
            } 
        }; 
        final PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(byteArrayOutputStream));//把转换字节流转换成字符流  
        HttpServletResponse httpServletResponse = new HttpServletResponseWrapper(response){//用于从response获取结果流资源(重写了两个方法)  
            public ServletOutputStream getOutputStream(){ 
                return servletOuputStream; 
            } 
            public PrintWriter getWriter(){ 
                return printWriter; 
            } 
        }; 
        rd.include(request, httpServletResponse);//发送结果流  
        printWriter.flush();//刷新缓冲区，把缓冲区的数据输出  
        FileOutputStream fileOutputStream = new FileOutputStream(fileFullPath); 
        byteArrayOutputStream.writeTo(fileOutputStream);//把byteArrayOuputStream中的资源全部写入到fileOuputStream中  
        fileOutputStream.close();//关闭输出流，并释放相关资源  
        response.sendRedirect(fileName);//发送指定文件流到客户端  
    } 
} 
