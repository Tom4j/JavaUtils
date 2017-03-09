package com.siweidg.comm.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.tools.ant.types.FileList.FileName;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.siweidg.comm.utils.SqliteDao;
import com.siweidg.comm.utils.StreamTool;
import com.siweidg.comm.init.Constants;
import com.siweidg.comm.utils.DateUtil;
import com.siweidg.map.dao.MapdataDao;
import com.siweidg.map.entry.Mapdata;

public class FileServer {

	private ExecutorService executorService;// 线程池

	@Autowired
	private MapdataDao mapdataDao;
	
	private int port;// 监听端口
	private boolean quit = false;// 退出
	private ServerSocket server;
	private static final Logger logger = Logger.getLogger(FileServer.class);

	private final static int blockSize = 4096;

	// private Map<Long, FileLog> datas = new HashMap<Long, FileLog>();//存放断点数据

	// private String oldFileName;
	public FileServer(int port) {
		this.port = port;
		logger.info("服务端启动Socket 2 端口 : " + port);
		// 创建线程池，池中具有(cpu个数*50)条线程
		// 创建固定数量的线程池 获取cpu的个数 100个并发链接
		executorService = Executors.newFixedThreadPool(5);
	}

	/**
	 * 退出
	 */
	public void quit() {
		this.quit = true;
		try {
			server.close();
		} catch (IOException e) {
		}
	}

	/**
	 * 启动服务
	 * 
	 * @throws Exception
	 */

	public void start() throws Exception {
		// 建立服务器端口事例
		server = new ServerSocket(port);
		while (!quit) {
			try {
				// 接受客户端链接
				Socket socket = server.accept();
				// 为支持多用户并发访问，采用线程池管理每一个用户的连接请求
				executorService.execute(new SocketTask(socket));
			} catch (Exception e) {

				e.printStackTrace();

			}

		}

	}

	private final class SocketTask implements Runnable {
		private Socket socket = null;

		public SocketTask(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				logger.info("连接到服务器了acceptedconnection "
						+ socket.getInetAddress() + ":" + socket.getPort());
				PushbackInputStream inStream = new PushbackInputStream(
						socket.getInputStream());
				String head = StreamTool.readLine(inStream);
				logger.info("发过来头信息head:" + head);
				if (head != null) {
					// 下面从协议数据中提取各项参数值
					String[] items = head.split(";");
					String token = items[items.length - 1]
							.substring(items[items.length - 1].indexOf("=") + 1);
					if (!"6f23504346874f234d879351d126c89f".equals(token)) { //验证不通过
						logger.error("非法连接 == head " + head);
						socket.close();
						return;
					}
					String fileLeng = items[0]
							.substring(items[0].indexOf("=") + 1);
					String filename = items[1]
							.substring(items[1].indexOf("=") + 1);
					boolean existForFile = isExistForFile(filename, fileLeng);
					OutputStream outputStream = socket.getOutputStream();
				try {
					if (!existForFile) { // 上传相同文件
						String response = "-1\r\n";
						outputStream.write(response.getBytes());
						System.out.println("服务端 发送 : " + response);
					} else {
						String response = "0\r\n";
						outputStream.write(response.getBytes());
						String type = "";
						// oldFileName = filename;
						/*if (items.length > 2) {
							type = items[2].split("=")[1];
						}*/
						File dir = new File(Constants.TEMP_FILE_PATH);// 临时目录
						if (!dir.exists()) {
							dir.mkdirs();
						}
						File tempFile = dir.createTempFile("tmp", null, dir);
						long tempLength = tempFile.length();
						
						// filename = DateUtil.getCurrentDate()+".db";
						// OutputStream outStream = socket.getOutputStream();
						// String response = "开始传输\r\n";
						// outStream.write(response.getBytes());
						// RandomAccessFile fileOutStream = new
						// RandomAccessFile(file, "rwd");
						// fileOutStream.seek(0);//移动文件指定的位置开始写入数据
						FileOutputStream fileOutStream = new FileOutputStream(
								tempFile);
						byte[] buffer = new byte[blockSize];
						int len = -1;
						long length = Long.parseLong(fileLeng);
						try {
							while ((len = inStream.read(buffer)) != -1) {// 从输入流中读取数据写入到文件中
								fileOutStream.write(buffer, 0, len);
								fileOutStream.flush();
								if (tempFile.length() == length) {
									break;
								}
								// length-= len;
							}
						} catch (Exception e) {
							fileOutStream.close();
							tempFile.delete();
							logger.error("传输文件fileOutStream流出错", e);
						}
						fileOutStream.close();
						File newFilePath = new File(Constants.MAP_FILE_PATH);
						if (!newFilePath.exists()) {
							newFilePath.mkdirs();
						}
						File newFile = new File(newFilePath, filename);
						// 文件迁移判断逻辑，文件大小与实际大小一致才迁移文件
						if (length == tempFile.length()) {
							FileUtils.copyFile(tempFile, newFile);
							tempFile.delete();
							System.out.println("接收完成; 文件名: " + filename + ";文件长度: "
									+ length);							
							updateDBXml(filename, length, type);
							outputStream.write("success\r\n".getBytes());
							outputStream.flush();
						} else {
							outputStream.write("fail\r\n".getBytes());
							outputStream.flush();
						}
						
						// outStream.close();
					}
					} catch (Exception e1) {
						e1.printStackTrace();
						outputStream.write("fail\r\n".getBytes());
						logger.error("文件上传Socket传输出错", e1);
					} finally {
						outputStream.close();
						inStream.close();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("文件上传Socket出错", e);
			} finally {
				try {
					if (socket != null && !socket.isClosed())
						socket.close();
				} catch (IOException e) {
				}
			}

		}

	}

	private void updateDBXml(String oriName, long size, String type)
			throws Exception {
		File file = new File(Constants.MAP_FILE_PATH + "download.xml");
		String tiles_note_sql = "select * from tiles_note";
		System.out.println("查询db文件:" + oriName + "的sql : " + tiles_note_sql);
		List<Map> list = SqliteDao.executeQuery(Constants.MAP_FILE_PATH
				+ oriName, tiles_note_sql);
		if (list == null || list.size() < 1) {
			// 如果数据库中没有查到则返回默认
			// System.out.println("没有在数据库中查到有关结果");
			throw new RuntimeException("没有在数据库中查到有关结果");
		} else {
			try {
				Map map = list.get(0);
				String name = (String) map.get("name"); // 数据名称
				String describe = (String) map.get("describe"); // 简要描述
				String code = map.get("code99").toString(); // 地理编码
				String date = map.get("date").toString(); // 拍摄时相
				String coordinate = map.get("coordinate").toString(); // 投影坐标
				String tiletype = map.get("tiletype").toString(); // 数据类型(image/vect/tran)
				String version = map.get("version").toString(); // 版本号
				String minlevel = map.get("minlevel").toString(); // 最大层级
				String maxlevel = map.get("maxlevel").toString(); // 最小层级

				String minx = map.get("minx").toString();
				String miny = map.get("miny").toString();
				String maxx = map.get("maxx").toString();
				String maxy = map.get("maxy").toString();
				String centerx = map.get("centerx").toString(); // 中心点经度
				String centery = map.get("centery").toString(); // 中心点纬度
				String remark = (String) map.get("remark"); // 字符备注
				
				File dbFile = new File(Constants.MAP_FILE_PATH+oriName);
				FileInputStream in = new FileInputStream(dbFile);
				String MD5 = DigestUtils.md5Hex(in); // MD5uuid
				in.close();
				String uuid = UUID.randomUUID().toString(); //uuid
				SAXReader saxReader = new SAXReader();
				Document document = saxReader.read(file);
				Element element = document.getRootElement();
				Element maps = element.element("maps");
				Element ditu = maps.element("ditu");
				List<Element> Elements = ditu.elements("map");
				int flag = 0;
				String str = null;
				for (Element e : Elements) {
					str = e.attributeValue("url");
					if (str.substring(str.lastIndexOf("/") + 1, str.length())
							.equals(oriName)) {
						e.setAttributeValue("filename", oriName);
						e.setAttributeValue("name", name);
						e.setAttributeValue("describe", describe);
						e.setAttributeValue("date", date);
						e.setAttributeValue("tiletype", tiletype);
						e.setAttributeValue("version", version);
						e.setAttributeValue("size", String.valueOf(size));
						e.setAttributeValue("type", type);
						e.setAttributeValue("minlevel", minlevel);
						e.setAttributeValue("maxlevel", maxlevel);
						e.setAttributeValue("minx", minx);
						e.setAttributeValue("miny", miny);
						e.setAttributeValue("maxy", maxy);
						e.setAttributeValue("maxx", maxx);
						e.setAttributeValue("minlevel", minlevel);
						e.setAttributeValue("maxlevel", maxlevel);
						e.setAttributeValue("remark", remark);
						e.setAttributeValue("uuid", uuid);
						e.setAttributeValue("MD5", MD5);
						Mapdata oldMap;
						List<Mapdata> preMaps = mapdataDao.find("from Mapdata where filename = ?", oriName);
						if (preMaps != null && preMaps.size() > 0) {
							oldMap = preMaps.get(0);
						} else {
							oldMap = new Mapdata();
						}
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						oldMap.setDate(df.parse(date));
						oldMap.setRemark(remark);
						oldMap.setDescription(describe);
						oldMap.setMaxlevel(Integer.parseInt(maxlevel));
						oldMap.setMinlevel(Integer.parseInt(minlevel));
						oldMap.setMaxx(maxx);
						oldMap.setMaxy(maxy);
						oldMap.setMinx(minx);
						oldMap.setMiny(miny);
						oldMap.setSize(size+"");
						oldMap.setVersion(version);
						oldMap.setType(type);
						oldMap.setTiletype(tiletype);
						oldMap.setName(name);
						oldMap.setUploadtime(new Date());
						oldMap.setMd5(MD5);
						oldMap.setFilename(oriName);
						oldMap.setCenterx(centerx);
						oldMap.setCentery(centery);
						mapdataDao.save(oldMap);
						flag = 1;
						break;
					}
				}
				if (flag == 0) {
					Element newElement = ditu.addElement("map");
					newElement.addAttribute("url", Constants.DOWNLOAD_FILE_URL
							+ oriName);
					newElement.addAttribute("filename", oriName);
					newElement.addAttribute("name", name);
					newElement.addAttribute("describe", describe);
					newElement.addAttribute("date", date);
					newElement.addAttribute("tiletype", tiletype);
					newElement.addAttribute("version", version);
					newElement.addAttribute("size", String.valueOf(size));
					newElement.addAttribute("type", type);
					newElement.addAttribute("minlevel", minlevel);
					newElement.addAttribute("maxlevel", maxlevel);
					newElement.addAttribute("minx", minx);
					newElement.addAttribute("miny", miny);
					newElement.addAttribute("maxy", maxy);
					newElement.addAttribute("maxx", maxx);

					Mapdata preMap = new Mapdata();
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					preMap.setDate(df.parse(date));
					preMap.setRemark(remark);
					preMap.setMaxlevel(Integer.parseInt(maxlevel));
					preMap.setMinlevel(Integer.parseInt(minlevel));
					preMap.setMaxx(maxx);
					preMap.setMaxy(maxy);
					preMap.setMinx(minx);
					preMap.setMiny(miny);
					preMap.setSize(size+"");
					preMap.setVersion(version);
//					preMap.setUrl(Constants.DOWNLOAD_FILE_URL + oriName);
					preMap.setType(type);
					preMap.setTiletype(tiletype);
					preMap.setName(name);
					preMap.setUploadtime(new Date());
					preMap.setMd5(MD5);
					preMap.setDescription(describe);
					preMap.setFilename(oriName);
					preMap.setCenterx(centerx);
					preMap.setCentery(centery);
					mapdataDao.save(preMap);

					newElement.addAttribute("minlevel", minlevel);
					newElement.addAttribute("maxlevel", maxlevel);
					newElement.addAttribute("remark", remark);
					newElement.addAttribute("uuid", uuid);
					newElement.addAttribute("MD5",MD5);
				}
				// XMLWriter xmlWriter = new XMLWriter(new
				// FileWriter(Constants.DOWNLOAD_FILE+"download.xml"),
				// OutputFormat.createPrettyPrint());
				// OutputFormat format = new OutputFormat();
				// format.setEncoding("UTF-8");
				XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(
						Constants.MAP_FILE_PATH + "download.xml"),
						OutputFormat.createPrettyPrint());
				xmlWriter.write(document);
				xmlWriter.flush();
				xmlWriter.close();
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean isExistForFile(String fileName, String fileLength)
			throws Exception {
		boolean flag = true;
		List<Mapdata> list = mapdataDao.find(
				"from Mapdata where filename = ? and size = ? ", fileName, fileLength);
		// System.out.println(list);
		// String sql = "SELECT * FROM pre_map WHERE file_name = '"+ fileName
		// +"' AND size = "+ fileLength +";";
		// List<PreMap> list = preMapDao.listSQL(sql);
		if (list.size() > 0) {
			flag = false;
		}
		return flag;
	}
}
