package com.ts.util;

import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.sql.CLOB;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.ts.util.PageData;
import com.ts.util.Tools;

/**
 * 导入到EXCEL 类名称：
 * 
 * @author FH Q313596790
 * @version 1.0
 */
@Component
public class ObjectExcelView extends AbstractExcelView {
	private int startRowIndex = 0;
	private boolean borderFlag = false;
	private String sheetName = null;
	private String url = null;
	
	public void setTemplate (String url,int startRowIndex){
		this.startRowIndex = startRowIndex;
		this.url = url;
		setUrl(url);
	}

	@Override
	@SuppressWarnings("deprecation")
	protected void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Date date = new Date();
		String filename = Tools.date2Str(date, "yyyyMMddHHmmss");
		HSSFSheet sheet;
		HSSFCell cell;
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename="+ filename + ".xls");
		if(this.url != null){
			sheet = workbook.getSheetAt(0);
		}else{
			sheet = workbook.createSheet(sheetName==null?"sheet1":sheetName);
		}

		List<String> titles = (List<String>) model.get("titles");
		int len = titles.size();
		 // 标题样式
		HSSFCellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		 // 标题字体
		HSSFFont headerFont = workbook.createFont();
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerFont.setFontHeightInPoints((short) 11);
		headerStyle.setFont(headerFont);
		if(borderFlag){
			headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		}
		short width = 120, height = 25 * 20;
		sheet.setDefaultColumnWidth(1200);
		
		// 设置标题
		for (int i = 0; i < len; i++) { 
			String title = titles.get(i);
			cell = getCell(sheet, startRowIndex, i);
			cell.setCellStyle(headerStyle);
			setText(cell, title);
		}
		
		HSSFCellStyle contentStyle = workbook.createCellStyle(); // 内容样式
		contentStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		if(borderFlag){
			contentStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			contentStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			contentStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			contentStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		}
		
		List<PageData> varList = (List<PageData>) model.get("varList");
		Set<String> countCells = (Set<String>) model.get("countCells");
		
		int varCount = varList.size();
		boolean countFlag = false;
		for (int i = 0; i < varCount; i++) {
			PageData vpd = varList.get(i);
			for (int j = 0; j < len; j++) {
				String key = "var" + (j + 1);
				Object obj = vpd.get(key);
				cell = getCell(sheet,startRowIndex + i + 1, j);
				cell.setCellStyle(contentStyle);
				
				boolean cellCountFlag  = false;
				Double countVal = 0D; 
				if (obj instanceof String) {
					String varstr = vpd.getString(key) != null ? vpd.getString(key) : "";
					setText(cell, varstr);
				} else if (obj instanceof Integer) {
					cellCountFlag = true;
					Integer val = vpd.get(key) == null ? 0 : (Integer) vpd.get(key);
					countVal += val;
					cell.setCellValue(val);
				} else if (obj instanceof Double) {
					cellCountFlag = true;
					Double val = vpd.get(key) == null ? 0 : (Double) vpd.get(key);
					countVal += val;
					cell.setCellValue(val);
				} else if (obj instanceof Date) {
					Date dat = vpd.get(key) == null ? null : (Date) vpd.get(key);
					String val = DateUtil.getTime(dat);
					cell.setCellValue(val);
				} else if (obj instanceof Long) {
					cellCountFlag = true;
					Long val = vpd.get(key) == null ? 0 : (Long) vpd.get(key);
					countVal += val;
					cell.setCellValue(val);
				} else if (obj instanceof CLOB) {
					Clob clob = vpd.get(key) == null ? null : (Clob) vpd.get(key);
					if ("".equals(clob)) {
						StringBuffer clobString = new StringBuffer();
						if (clob instanceof Clob) {
							int y;
							char ac[] = new char[4096];
							Reader reader;
							try {
								reader = ((Clob) clob).getCharacterStream();
								while ((y = reader.read(ac, 0, 4096)) != -1) {
									clobString.append(new String(ac, 0, y));
								}
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
						cell.setCellValue(clobString.toString());
					}
				}
				
				//  统计列
				if(cellCountFlag && countCells!=null && countCells.contains(key)){
					countFlag = true;
					cell = getCell(sheet, startRowIndex + varCount+1, j);
					Double d = cell.getNumericCellValue();
					d = d==null?0:d;
					d += countVal;
					cell.setCellValue(d);
				}
			}
		}
		if(countFlag){
			// 统计样式
			HSSFCellStyle countStyle = workbook.createCellStyle(); 
			countStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			cell = getCell(sheet, startRowIndex + varCount+1, 0);
			cell.setCellStyle(countStyle);
			cell.setCellValue("总计：");
		}
		for (int i = 0; i < len; i++) { 
			sheet.autoSizeColumn((short)i); //调整第一列宽度
		}
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public boolean isBorderFlag() {
		return borderFlag;
	}

	public void setBorderFlag(boolean borderFlag) {
		this.borderFlag = borderFlag;
	}
	
}
