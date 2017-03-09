package com.siweidg.comm.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
	
	public final static String FileType_XLS = ".xls";
	public final static String FileType_XLSX = ".xlsx";
	
	public static Workbook read(InputStream stream, String fileType)
			throws IOException {
		Workbook wb = null;
		if (fileType.equals(FileType_XLS)) {
			wb =  new HSSFWorkbook(stream);
		} else if (fileType.equals(FileType_XLSX)) {
			wb = new XSSFWorkbook(stream);
		} 
		return wb;
	}
	
	
	
	
}
