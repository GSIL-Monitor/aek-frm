package com.aek.common.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * POI Excel工具类
 *
 * @author  Honghui
 * @date    2017年6月29日
 * @version 1.0
 */
public class PoiExcelUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(PoiExcelUtils.class);
	
	private PoiExcelUtils(){};
	
	//excel中的列名
	private static List<String> columns;
	//sheet下标
	private static int sheetNum = 0; 
	//动态列标记
	private static boolean dynamicColumn = false;
	//动态列配置
	private static Map<String,String> dynamicMapConfig = new ListOrderedMap();
	
	/**
	 * 读取excel文件生成实体集合
	 * <功能详细描述>
	 * @param file  excel文件
	 * @param clazz 实体类对象
	 * @return
	 * @throws Exception [参数说明]
	 * 
	 * @return List<E> [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static <E> List<E> readExcel(File file,Class<E> clazz) throws Exception{
		List<E> eList = new ArrayList<E>();
		InputStream in = new FileInputStream(file);
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.getSheetAt(sheetNum);
		int lastRowNum = sheet.getLastRowNum();
		
		return eList;
	}
	
}
