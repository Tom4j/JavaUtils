package com.siweidg.comm.init;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.siweidg.comm.servlet.FileServer;

/**
 * 加载初始化配置文件
 * 
 * @author cheng
 * @copyRight SIWEIDG 北京航天世景信息技术有限公司
 */
@SuppressWarnings("serial")
public class ServiceInit extends HttpServlet {

	private static final Logger logger = Logger.getLogger(ServiceInit.class);

	public void init(ServletConfig config) throws ServletException {
		logger.info("启动服务"+ "地图上传" +" . . . . . . . . ");

		ServletContext servletContext = config.getServletContext();
		WebApplicationContext ctx = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		
		FileServer server = new FileServer(Constants.MAP_SOCKET_PORT);
		Thread dataThread = new Thread(new DataThread(server));
		dataThread.start();
		
		logger.info("服务"+ "地图上传" +"已启动 . . . . . . . . ");
	}
	
	
	private class DataThread implements Runnable {
		FileServer fileServer;
		
		public DataThread(FileServer fileServer) {
			this.fileServer = fileServer;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				fileServer.start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	/**
	 * 由此方法释放资源
	 */
	public void destroy() {
		super.destroy();
	}

}