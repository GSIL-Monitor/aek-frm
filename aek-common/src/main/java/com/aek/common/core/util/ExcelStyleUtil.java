package com.aek.common.core.util;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelStyleUtil {

	/**
	 * 机构导入excel title样式
	 * 
	 * @param workbook
	 * @return
	 */
	public static HSSFCellStyle tenantTempltTitleStyle(HSSFWorkbook workbook) {

		HSSFCellStyle titleStyle = workbook.createCellStyle();

		titleStyle.setWrapText(true); // 自动换行
		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中

		// 设置边框
		titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);

		HSSFFont titlefont = workbook.createFont();
		titlefont.setFontName("宋体");
		titlefont.setColor(HSSFColor.BLACK.index);
		titlefont.setFontHeightInPoints((short) 10);
		titleStyle.setFont(titlefont);
		return titleStyle;
	}

	/**
	 * 机构导入 excel header 样式
	 * 
	 * @param workbook
	 * @return
	 */
	public static CellStyle tenantTempltHeaderStyle(Workbook workbook) {
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中

		// 背景边框
		headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);

		// 背景颜色
		headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		headerStyle.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);

		Font font = workbook.createFont();
		font.setFontName("微软雅黑");
		font.setColor(HSSFColor.WHITE.index);
		font.setFontHeightInPoints((short) 10); // 字体大小
		headerStyle.setFont(font);
		return headerStyle;
	}

	/**
	 * 导入机构cell错误，背景颜色样式
	 * 
	 * @param workbook
	 * @return
	 */
	public static CellStyle tenantImportCellWrongStyle(Workbook workbook) {
		CellStyle wrongStyle = workbook.createCellStyle();
		// 背景边框
		wrongStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		wrongStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		wrongStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		wrongStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);

		wrongStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		wrongStyle.setFillForegroundColor(HSSFColor.ORANGE.index);

		Font font = workbook.createFont();
		font.setFontName("微软雅黑");
		font.setFontHeightInPoints((short) 10); // 字体大小
		wrongStyle.setFont(font);
		return wrongStyle;
	}

	/**
	 * 导入机构错误行尾cell样式
	 * 
	 * @param workbook
	 * @return
	 */
	public static CellStyle tenantImportWrongStyle(Workbook workbook) {
		CellStyle wrongStyle = workbook.createCellStyle();

		// 背景边框
		wrongStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		wrongStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		wrongStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		wrongStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		wrongStyle.setWrapText(true); // 自动换行
		Font font = workbook.createFont();
		font.setFontName("微软雅黑");
		font.setColor(HSSFColor.RED.index);
		font.setFontHeightInPoints((short) 8); // 字体大小
		wrongStyle.setFont(font);

		return wrongStyle;
	}
}
