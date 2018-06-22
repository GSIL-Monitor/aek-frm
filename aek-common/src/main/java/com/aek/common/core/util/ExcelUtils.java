package com.aek.common.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;

public class ExcelUtils {

	/**
	 * 设置某些列的值只能输入预制的数据,显示下拉框.
	 * 
	 * @param sheet
	 *            要设置的sheet.
	 * @param textlist
	 *            下拉框显示的内容
	 * @param firstRow
	 *            开始行
	 * @param endRow
	 *            结束行
	 * @param firstCol
	 *            开始列
	 * @param endCol
	 *            结束列
	 * @return 设置好的sheet.
	 */
	public static HSSFSheet setHSSFValidation(HSSFSheet sheet, String[] textlist, int firstRow, int endRow,
			int firstCol, int endCol) {
		// 加载下拉列表内容
		DVConstraint constraint = DVConstraint.createExplicitListConstraint(textlist);
		// 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
		CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
		// 数据有效性对象
		HSSFDataValidation data_validation_list = new HSSFDataValidation(regions, constraint);
		sheet.addValidationData(data_validation_list);
		return sheet;
	}
	
	/**
	 * 针对监管机构下拉列表： 设置某些列的值只能输入预制的数据,显示下拉框. 避免出现数据过多报错问题
	 * 
	 * @param sheet
	 *            要设置的sheet.
	 * @param textlist
	 *            下拉框显示的内容
	 * @param firstRow
	 *            开始行
	 * @param endRow
	 *            结束行
	 * @param firstCol
	 *            开始列
	 * @param endCol
	 *            结束列
	 * @return 设置好的sheet.
	 */
	public static HSSFSheet setHSSFSuperviseValidation(HSSFWorkbook workbook,HSSFSheet sheet, String[] textlist, int firstRow, int endRow,
			int firstCol, int endCol) {
		//=======当前代码问题：当下拉框数据过多时报错：String literals in formulas can't be bigger than 255 characters ASCII========
		// 加载下拉列表内容
		//DVConstraint constraint = DVConstraint.createExplicitListConstraint(textlist);
		// 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
		//CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
		// 数据有效性对象
		//HSSFDataValidation data_validation_list = new HSSFDataValidation(regions, constraint);
		//sheet.addValidationData(data_validation_list);
		//return sheet;
		//=======当前代码问题：当下拉框数据过多时报错：String literals in formulas can't be bigger than 255 characters ASCII========
		
		// 改进方案：如下代码
		String hiddenSheetName = "SuperviseTenantHidden";
		// 创建隐藏域
		HSSFSheet hiddenSheet = workbook.createSheet(hiddenSheetName); 
        for (int i = 0, length = textlist.length; i < length; i++) { 
        	// 循环赋值（为了防止下拉框的行数与隐藏域的行数相对应来获取>=选中行数的数组，将隐藏域加到结束行之后）
        	hiddenSheet.createRow(endRow + i).createCell(firstCol).setCellValue(textlist[i]);
        	
        } 
        Name name = workbook.createName(); 
        name.setNameName(hiddenSheetName); 
        // A1:A代表隐藏域创建第?列createCell(?)时。以A1列开始A行数据获取下拉数组
        name.setRefersToFormula(hiddenSheetName + "!E"+(endRow + 1)+":E" + (textlist.length+endRow));
        
        DVConstraint constraint = DVConstraint.createFormulaListConstraint(hiddenSheetName); 
        CellRangeAddressList addressList = new CellRangeAddressList(firstRow, endRow, firstCol, endCol); 
        HSSFDataValidation validation = new HSSFDataValidation(addressList, constraint); 
        // 1隐藏、0显示
        workbook.setSheetHidden(1, true); 
        sheet.addValidationData(validation);
        return sheet;
	}

	/**
	 * 设置单元格上提示
	 * 
	 * @param sheet
	 *            要设置的sheet.
	 * @param firstRow
	 *            开始行
	 * @param endRow
	 *            结束行
	 * @param firstCol
	 *            开始列
	 * @param endCol
	 *            结束列
	 * @return 设置好的sheet.
	 */
	public static HSSFSheet setHSSFPrompt(HSSFSheet sheet, int firstRow, int endRow, int firstCol, int endCol) {
		// 构造constraint对象
		DVConstraint constraint = DVConstraint.createCustomFormulaConstraint("BB1");
		// 四个参数分别是：起始行、终止行、起始列、终止列
		CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);

		// 数据有效性对象
		HSSFDataValidation data_validation_view = new HSSFDataValidation(regions, constraint);
		data_validation_view.createPromptBox("下拉选择提示", "请使用下拉方式选择合适的值！");
		// 设置输入错误提示信息
		data_validation_view.createErrorBox("选择错误提示", "你输入的值未在备选列表中，请下拉选择合适的值！");
		sheet.addValidationData(data_validation_view);
		return sheet;
	}

	/**
	 * 删除以行一行
	 * 
	 * @param sheet
	 * @param rowIndex
	 */
	public static void removeRow(Sheet sheet, int rowIndex) {
		int lastRowNum = sheet.getLastRowNum();
		if (rowIndex >= 0 && rowIndex < lastRowNum)
			// 将行号为rowIndex+1一直到行号为lastRowNum的单元格全部上移一行，以便删除rowIndex行
			sheet.shiftRows(rowIndex + 1, lastRowNum, -1);
		if (rowIndex == lastRowNum) {
			Row removingRow = sheet.getRow(rowIndex);
			if (removingRow != null)
				sheet.removeRow(removingRow);
		}
	}

	/**
	 * 
	 * 设置单元格注释
	 * 
	 * @param patriarch
	 * 
	 * @param cell
	 * 
	 * @param cellnum
	 * 
	 */
	public static void addComment(HSSFPatriarch patriarch, HSSFCell cell, String note) {
		HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 1, 2, (short) 4, 4));
		comment.setString(new HSSFRichTextString(note));
		cell.setCellComment(comment);

	}
	
	public static void main(String[] args) {
        /**
         * Excel API
         */
        @SuppressWarnings("resource")
        HSSFWorkbook book = new HSSFWorkbook();  
        HSSFSheet sheet = book.createSheet("spu导入模板");  
        
        /**
         * 初始化参数
         */
        Map<String, String> map = new HashMap<String, String>(); // 查询用的map 
        List<Object> list = null; 
        String[] strs = new String[500]; // 用于下拉的数组
        String hiddenSheet = null;
        int cellNum = 0;
        int startRow = 1; // 开始行 
        int endRow = 100; // 结束行
        DVConstraint constraint = null;
        CellRangeAddressList addressList = null;
        HSSFDataValidation validation = null; // 数据验证

        for(int i=0; i<500;i++){
        	strs[i] = "第"+i+"个";
        }
        
        hiddenSheet = "category1Hidden";
        //cellNum = SpuEnu.CATEGORY_1.getNumber();
        
        HSSFSheet category1Hidden = book.createSheet(hiddenSheet); // 创建隐藏域
        for (int i = 0, length = strs.length; i < length; i++) { // 循环赋值（为了防止下拉框的行数与隐藏域的行数相对应来获取>=选中行数的数组，将隐藏域加到结束行之后）
            category1Hidden.createRow(endRow + i).createCell(cellNum).setCellValue(strs[i]);
        } 
        Name category1Name = book.createName(); 
        category1Name.setNameName(hiddenSheet); 
        category1Name.setRefersToFormula(hiddenSheet + "!A1:A" + (strs.length + endRow)); // A1:A代表隐藏域创建第?列createCell(?)时。以A1列开始A行数据获取下拉数组
        
        constraint = DVConstraint.createFormulaListConstraint(hiddenSheet); 
        addressList = new CellRangeAddressList(startRow, endRow, cellNum, cellNum); 
        validation = new HSSFDataValidation(addressList, constraint); 
        book.setSheetHidden(1, true); // 1隐藏、0显示
        sheet.addValidationData(validation);
        
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
        	book.write(os);
        } catch (IOException e) {
         e.printStackTrace();
        }
        
        byte[] b = os.toByteArray();
        ByteArrayInputStream in = new ByteArrayInputStream(b);
        File file = new File("d://1.xls");
		try {
			OutputStream os1 = new FileOutputStream(file);
			int bytesRead = 0;
	        byte[] buffer = new byte[8192];
	        while ((bytesRead = in.read(buffer, 0, 8192)) != -1) {
	         os1.write(buffer, 0, bytesRead);
	        }
	        os.close();
	        in.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
}
