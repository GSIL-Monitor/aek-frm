package com.aek.common.core.util;

import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddressList;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.Name;

/**
 * Excel下拉框
 *	
 * @author HongHui
 * @date   2017年8月11日
 */
public class ExcelSelectUtils {
	
	public static void main(String[] args) throws Exception{
		/*String[] provinceList = {"浙江","山东","江西","江苏","四川"};  
		String hideSheetName = "hideselectinfosheet";
		String formulaString = "superviseTenantInfo";
		HSSFWorkbook workbook = new HSSFWorkbook();//excel文件对象    
	    HSSFSheet userinfosheet1 = workbook.createSheet("用户信息表-1");//工作表对象  
	    //创建一个隐藏页和隐藏数据集  
	    ExcelSelectUtils.creatHideSheet(workbook, hideSheetName,provinceList);  
	    //设置名称数据集  
	    ExcelSelectUtils.creatExcelNameList(workbook,hideSheetName,formulaString); 
	    
	    for(int i=1; i < 6;i++){
	    	//得到验证对象    
	        DataValidation data_validation_list = ExcelSelectUtils.getDataValidationByFormula(formulaString,i,4);  
	        //工作表添加验证数据    
	        userinfosheet1.addValidationData(data_validation_list);  
	    }
         
        //生成输入文件  
        FileOutputStream out=new FileOutputStream("d://success2.xls");    
        workbook.write(out);    
        out.close();  */
        
        String colstr = "AH";
        int colIndex = excelColStrToNum(colstr, colstr.length());
        System.out.println("'" + colstr + "' column index of " + colIndex);

        colIndex = 26;
        colstr = excelColIndexToStr(colIndex);
        System.out.println(colIndex + " column in excel of " + colstr);

        colstr = "AAAA";
        colIndex = excelColStrToNum(colstr, colstr.length());
        System.out.println("'" + colstr + "' column index of " + colIndex);

        colIndex = 466948;
        colstr = excelColIndexToStr(colIndex);
        System.out.println(colIndex + " column in excel of " + colstr);
	}
	
	/**
	 * 给excel下拉列表添加监管机构信息
	 * @param workbook
	 * @param sheet
	 * @param textlist
	 * @param firstRow
	 * @param endRow
	 * @param collNum
	 * @return
	 */
	public static HSSFSheet setHSSFSuperviseValidation(HSSFWorkbook workbook,HSSFSheet sheet, String[] textlist, int firstRow, int endRow,
			int collNum){
		String hideSheetName = "superviseTenantInfoSheet";
		String formulaString = "superviseTenantInfo";
		//创建一个隐藏页和隐藏数据集  
	    ExcelSelectUtils.creatHideSheet(workbook, hideSheetName,textlist);  
	    //设置名称数据集  
	    ExcelSelectUtils.creatExcelNameList(workbook,hideSheetName,formulaString,textlist.length);
	    for(int i = (firstRow+1); i < endRow; i++ ){
	    	//得到验证对象    
	        DataValidation data_validation_list = ExcelSelectUtils.getDataValidationByFormula(formulaString,i,collNum);  
	        //工作表添加验证数据    
	        sheet.addValidationData(data_validation_list);  
	    }
		return sheet;
	}

	 /** 
     * 名称管理 
     * @param workbook 
     */  
    public static void creatExcelNameList(HSSFWorkbook workbook,String hiddenSheetName,String formulaString,int collNum){  
        //名称管理  
        Name name;  
        name = workbook.createName();  
        name.setNameName(formulaString); 
        String collNumStr = excelColIndexToStr(collNum);
        name.setRefersToFormula(hiddenSheetName+"!$A$1:$"+collNumStr+"$1");  
    } 
    
	/** 
     * 创建隐藏页和数据域 
     * @param workbook 
     * @param hideSheetName 
     */  
    public static void creatHideSheet(HSSFWorkbook workbook,String hideSheetName,String[] textlist){  
        HSSFSheet hideselectinfosheet = workbook.createSheet(hideSheetName);//隐藏一些信息  
        //在隐藏页设置选择信息  
        HSSFRow provinceRow = hideselectinfosheet.createRow(0);  
        creatRow(provinceRow, textlist);  
        //设置隐藏页标志  
        workbook.setSheetHidden(workbook.getSheetIndex(hideSheetName), true);  
    }  
    
	 /** 
     * 创建一列数据 
     * @param currentRow 
     * @param textList 
     */  
    public static void creatRow(HSSFRow currentRow,String[] textList){  
        if(textList!=null&&textList.length>0){  
            int i = 0;  
            for(String cellValue : textList){  
                HSSFCell userNameLableCell = currentRow.createCell(i++);  
                userNameLableCell.setCellValue(cellValue);  
            }  
        }  
    }  
      
    /** 
     * 对Excel自然行列设置一个数据验证（并出现下拉列表选择格式） 
     * @param selectTextList 
     * @param naturalRowIndex 
     * @param naturalColumnIndex 
     * @return 
     */  
    public static DataValidation getDataValidationList(String[] selectTextList,int naturalRowIndex,int naturalColumnIndex){  
        //加载下拉列表内容    
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(selectTextList);  
        //设置数据有效性加载在哪个单元格上。    
        //四个参数分别是：起始行、终止行、起始列、终止列    
        int firstRow = naturalRowIndex-1;  
        int lastRow = naturalRowIndex-1;  
        int firstCol = naturalColumnIndex-1;  
        int lastCol = naturalColumnIndex-1;  
        CellRangeAddressList regions=new CellRangeAddressList(firstRow,lastRow,firstCol,lastCol);    
        //数据有效性对象  
        DataValidation data_validation_list = new HSSFDataValidation(regions,constraint);    
        return data_validation_list;    
    }  
      
    /** 
     * 使用已定义的数据源方式设置一个数据验证 
     * @param formulaString 
     * @param naturalRowIndex 
     * @param naturalColumnIndex 
     * @return 
     */  
    public static DataValidation getDataValidationByFormula(String formulaString,int naturalRowIndex,int naturalColumnIndex){  
        //加载下拉列表内容    
        DVConstraint constraint = DVConstraint.createFormulaListConstraint(formulaString);   
        //设置数据有效性加载在哪个单元格上。    
        //四个参数分别是：起始行、终止行、起始列、终止列    
        int firstRow = naturalRowIndex-1;  
        int lastRow = naturalRowIndex-1;  
        int firstCol = naturalColumnIndex-1;  
        int lastCol = naturalColumnIndex-1;  
        CellRangeAddressList regions=new CellRangeAddressList(firstRow,lastRow,firstCol,lastCol);    
        //数据有效性对象   
        DataValidation data_validation_list = new HSSFDataValidation(regions,constraint);  
        return data_validation_list;    
    }  
    
    /**
     * Excel column index begin 1
     * @param colStr
     * @param length
     * @return
     */
    public static int excelColStrToNum(String colStr, int length) {
        int num = 0;
        int result = 0;
        for(int i = 0; i < length; i++) {
            char ch = colStr.charAt(length - i - 1);
            num = (int)(ch - 'A' + 1) ;
            num *= Math.pow(26, i);
            result += num;
        }
        return result;
    }

    /**
     * Excel column index begin 1
     * @param columnIndex
     * @return
     */
    public static String excelColIndexToStr(int columnIndex) {
        if (columnIndex <= 0) {
            return null;
        }
        String columnStr = "";
        columnIndex--;
        do {
            if (columnStr.length() > 0) {
                columnIndex--;
            }
            columnStr = ((char) (columnIndex % 26 + (int) 'A')) + columnStr;
            columnIndex = (int) ((columnIndex - columnIndex % 26) / 26);
        } while (columnIndex > 0);
        return columnStr;
    }
}
