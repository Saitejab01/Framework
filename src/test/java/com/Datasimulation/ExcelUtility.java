package com.Datasimulation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelUtility {
	public String getDataFromExcel(String sheetName,int rowNum, int colNum) throws EncryptedDocumentException, IOException {
		File file = new File(Driver.path);
		Workbook wb = WorkbookFactory.create(file);
		Sheet sh = wb.getSheet(sheetName);
		Row row = sh.getRow(rowNum);
		String data = row.getCell(colNum).toString();
		return data;
	}
	public int getTheLastRowIndex(String sheetName) throws EncryptedDocumentException, IOException {
		File file = new File(Driver.path);
		Workbook wb = WorkbookFactory.create(file);
		Sheet sh = wb.getSheet(sheetName);
		return sh.getLastRowNum();
	}
	  public void setCellData(String sheetName, int rowNum, int cellNum, String value)
	            throws EncryptedDocumentException, IOException {
	        FileInputStream fis = new FileInputStream(Driver.path);
	        Workbook wb = WorkbookFactory.create(fis);
	        Sheet sh = wb.getSheet(sheetName);
	        Row row = sh.getRow(rowNum);
	        Cell cell = row.getCell(cellNum);
	        cell.setCellValue(value);
	        FileOutputStream fos = new FileOutputStream(Driver.path);
	        wb.write(fos);
	            }
}
