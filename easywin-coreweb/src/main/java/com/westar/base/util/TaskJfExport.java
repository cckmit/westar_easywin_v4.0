package com.westar.base.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import com.westar.base.model.Item;
import com.westar.base.pojo.ItemStagedInfo;
import com.westar.base.pojo.TaskJfStatis;

public class TaskJfExport {
	private static Logger logger = Logger.getLogger(TaskJfExport.class);

	/**
	 * 导出excel到response输出流
	 * 
	 * @param data
	 * @param fileName
	 * @param response
	 */
	public static void exportExcel(List<TaskJfStatis> data, String fileName, List<String> headTitle, HttpServletResponse response,
			HttpServletRequest request) {
		OutputStream os = null;
		try {
			if (data != null && data.size() > 0) {
				response.reset();
				String agent = (String) request.getHeader("USER-AGENT");
				if (agent != null && agent.toLowerCase().indexOf("firefox") > 0) {// 火狐
					String enableFileName = "=?UTF-8?B?"
							+ (new String(Base64.encodeBase64(fileName.getBytes("UTF-8")))) + "?=";
					response.setHeader("Content-Disposition", "attachment; filename=" + enableFileName + ".xls");
				} else {
					String enableFileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
					response.setHeader("Content-Disposition", "attachment; filename=" + enableFileName + ".xls");
				}
				response.setContentType("application/msexcel");// 定义输出类型
				HSSFWorkbook wb = createWorkbook(data, fileName, headTitle);
				os = response.getOutputStream();// 取得输出流
				wb.write(os);
				os.flush();
			}
		} catch (Exception e) {
			logger.error("导出Excel失败。", e);
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 创建excel
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	private static HSSFWorkbook createWorkbook(List<TaskJfStatis> data, String titleName, List<String> headTitle) throws Exception {
		HSSFWorkbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet();
		int headerRowSpan = createHeader(sheet, titleName, headTitle);
		createBody(sheet, data, headerRowSpan);
		return wb;
	}

	/**
	 * 创建标题和表头
	 * 
	 * @param sheet
	 * @param dataRow
	 */
	private static int createHeader(Sheet sheet, String titleName, List<String> headTitle) {
		int headerIndex = 0;
		// 创标题
		if (!StringUtil.isBlank(titleName)) {
			Row titleRow = sheet.createRow(0);
			titleRow.setHeight((short) 500);//行高
			Cell titleCell = titleRow.createCell(0);
			titleCell.setCellStyle(createTitleStyle((HSSFWorkbook) sheet.getWorkbook()));
			titleCell.setCellValue(titleName);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headTitle.size() - 1));// 设置跨列
			headerIndex++;
		}

		// 创建表头
		Row headerRow = sheet.createRow(headerIndex);
		for (int i = 0; i < headTitle.size(); i++) {
			Cell headerCell = headerRow.createCell(i);
			headerCell.setCellStyle(createHeaderStyle((HSSFWorkbook) sheet.getWorkbook()));
			headerCell.setCellValue(headTitle.get(i));
		}
		return headerIndex;
	}
	/**
	 * 创建表身
	 * 
	 * @param sheet
	 * @param dataRow
	 */
	private static void createBody(Sheet sheet, List<TaskJfStatis> data, int headerRowSpan) throws Exception {
		int rowNum = headerRowSpan+1;
		
		if(!data.isEmpty() && data.size() > 0){
			HSSFWorkbook wb = (HSSFWorkbook) sheet.getWorkbook();
			HSSFCellStyle style = createBodyStyle(wb);
			HSSFCellStyle numStyle = createNumStyle(wb);
			HSSFCellStyle totalStyle = createTotalStyle(wb);
			
			int preDepId = 0;
			int num = 1;//序号
			
			int taskTotal = 0;
			int jfTaskTotal = 0;
			String scoreTotal = "0";
			int firstRow = rowNum;//合并行开始行
			for(int i =0;i<data.size();i++){//循环数据
				TaskJfStatis taskJf = data.get(i);
				
				if(taskJf.getDepId() != preDepId && preDepId !=0){//和上一列不是同一部门，添加合计行,添加一单位行
					//合计
					Row totalRow = sheet.createRow(rowNum);
					Cell cell0 = totalRow.createCell(0);//序号
					cell0.setCellStyle(style);
					cell0.setCellValue("");
					
					Cell cell1 = totalRow.createCell(1);//部门名称
					cell1.setCellStyle(style);
					cell1.setCellValue("");
					
					Cell cell2 = totalRow.createCell(2);//人员
					cell2.setCellStyle(totalStyle);
					cell2.setCellValue("合计");
					
					Cell cell3 = totalRow.createCell(3);//任务总数
					cell3.setCellStyle(totalStyle);
					cell3.setCellValue(taskTotal);
					
					//已评分任务数量
					Cell cell4 = totalRow.createCell(4);
					cell4.setCellStyle(totalStyle);
					cell4.setCellValue(jfTaskTotal);
					
					//积分总数
					Cell cell5 = totalRow.createCell(5);
					cell5.setCellStyle(totalStyle);
					cell5.setCellValue(scoreTotal);
					//重置总计
					 taskTotal = 0;
					 jfTaskTotal = 0;
					 scoreTotal = "0";
					
					 //合并
					 sheet.addMergedRegion(new CellRangeAddress(firstRow, rowNum , 0, 0));
					 sheet.addMergedRegion(new CellRangeAddress(firstRow, rowNum , 1, 1));
					
					 //序号增加
					 num+=1;
					 
					 firstRow = rowNum+1;
					 
					 //行数增加
					 rowNum +=1;
					 Row row = sheet.createRow(rowNum);
					 Cell newCell0 = row.createCell(0);//序号
					 newCell0.setCellStyle(style);
					 newCell0.setCellValue(num);
					
					Cell newCell1 = row.createCell(1);//部门名称
					newCell1.setCellStyle(style);
					newCell1.setCellValue(taskJf.getDepName());
					
					Cell newCell2 = row.createCell(2);//人员
					newCell2.setCellStyle(style);
					newCell2.setCellValue(taskJf.getUserName());
					
					Cell newCell3 = row.createCell(3);//任务总数
					newCell3.setCellStyle(numStyle);
					newCell3.setCellValue(CommonUtil.isNull(taskJf.getTaskTotal())?0:taskJf.getTaskTotal());
					
					//已评分任务数量
					Cell newCell4 = row.createCell(4);
					newCell4.setCellStyle(numStyle);
					newCell4.setCellValue(CommonUtil.isNull(taskJf.getJfTaskNum()) ? 0 :taskJf.getJfTaskNum());
					
					//积分总数
					Cell newCell5 = row.createCell(5);
					newCell5.setCellStyle(numStyle);
					newCell5.setCellValue((CommonUtil.isNull(taskJf.getScoreTotal())? 0 :taskJf.getScoreTotal())+"");
					//增加行数
					rowNum +=1;
					
					taskTotal +=CommonUtil.isNull(taskJf.getTaskTotal())?0:taskJf.getTaskTotal();
					jfTaskTotal += CommonUtil.isNull(taskJf.getJfTaskNum()) ? 0 :taskJf.getJfTaskNum();
					scoreTotal = (Float.valueOf(scoreTotal)+(CommonUtil.isNull(taskJf.getScoreTotal())? 0 :taskJf.getScoreTotal()))+"";
					
					preDepId = taskJf.getDepId();
				}else{//
					Row row = sheet.createRow(rowNum);
					Cell cell0 = row.createCell(0);//序号
					cell0.setCellStyle(style);
					
					Cell cell1 = row.createCell(1);//部门名称
					cell1.setCellStyle(style);
					
					if(preDepId!=0){
						cell0.setCellValue("");
						cell1.setCellValue("");
					}else{
						cell0.setCellValue(num);
						cell1.setCellValue(taskJf.getDepName());
					}
					
					
					Cell cell2 = row.createCell(2);//人员
					cell2.setCellStyle(style);
					cell2.setCellValue(taskJf.getUserName());
					
					Cell cell3 = row.createCell(3);//任务总数
					cell3.setCellStyle(numStyle);
					cell3.setCellValue(CommonUtil.isNull(taskJf.getTaskTotal())?0:taskJf.getTaskTotal());
					
					//已评分任务数量
					Cell cell4 = row.createCell(4);
					cell4.setCellStyle(numStyle);
					cell4.setCellValue(CommonUtil.isNull(taskJf.getJfTaskNum()) ? 0 :taskJf.getJfTaskNum());
					
					//积分总数
					Cell cell5 = row.createCell(5);
					cell5.setCellStyle(numStyle);
					cell5.setCellValue((CommonUtil.isNull(taskJf.getScoreTotal())? 0 :taskJf.getScoreTotal())+"");
					rowNum +=1;
					
					taskTotal +=CommonUtil.isNull(taskJf.getTaskTotal())?0:taskJf.getTaskTotal();
					jfTaskTotal += CommonUtil.isNull(taskJf.getJfTaskNum()) ? 0 :taskJf.getJfTaskNum();
					scoreTotal = (Float.valueOf(scoreTotal)+(CommonUtil.isNull(taskJf.getScoreTotal())? 0 :taskJf.getScoreTotal()))+"";
					preDepId = taskJf.getDepId();
				}
				//最后一行增加合计
				if(i==data.size()-1){
					//合计
					Row totalRow = sheet.createRow(rowNum);
					Cell cell0 = totalRow.createCell(0);//序号
					cell0.setCellStyle(style);
					cell0.setCellValue("");
					
					Cell cell1 = totalRow.createCell(1);//部门名称
					cell1.setCellStyle(style);
					cell1.setCellValue("");
					
					Cell cell2 = totalRow.createCell(2);//人员
					cell2.setCellStyle(totalStyle);
					cell2.setCellValue("合计");
					
					Cell cell3 = totalRow.createCell(3);//任务总数
					cell3.setCellStyle(totalStyle);
					cell3.setCellValue(taskTotal);
					
					//已评分任务数量
					Cell cell4 = totalRow.createCell(4);
					cell4.setCellStyle(totalStyle);
					cell4.setCellValue(jfTaskTotal);
					
					//积分总数
					Cell cell5 = totalRow.createCell(5);
					cell5.setCellStyle(totalStyle);
					cell5.setCellValue(scoreTotal);
					 //合并
					 sheet.addMergedRegion(new CellRangeAddress(firstRow, rowNum , 0, 0));
					 sheet.addMergedRegion(new CellRangeAddress(firstRow, rowNum , 1, 1));
				}
				
			}
			// 设置自动适应宽度
			for (int i = 0; i < data.size(); i++) {
				sheet.autoSizeColumn(i,true);
			}
		}
		
	}
	/**
	 * 创建标题样式
	 * 
	 * @return
	 */
	private static HSSFCellStyle createTitleStyle(HSSFWorkbook wb) {
		// 创建字体样式
		HSSFFont font = wb.createFont();
		font.setFontName("Verdana");
		font.setFontHeight((short) 450);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 字体加粗  

		// 创建样式
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平对齐
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直对齐
		style.setWrapText(true);
		style.setFont(font);

		return style;
	}

	/**
	 * 创建表头样式
	 * 
	 * @return
	 */
	private static HSSFCellStyle createHeaderStyle(HSSFWorkbook wb) {
		// 创建字体样式
		HSSFFont font = wb.createFont();
		font.setFontName("Verdana");
		font.setFontHeight((short) 280);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		// 创建样式
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平对齐
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直对齐
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		style.setWrapText(false);//自动换行

		style.setFont(font);

		return style;
	}
	/**
	 * 创建表体样式
	 * 
	 * @return
	 */
	private static HSSFCellStyle createBodyStyle(HSSFWorkbook wb) {
		// 创建字体样式
		HSSFFont font = wb.createFont();
		font.setFontName("Verdana");
		font.setFontHeight((short) 220);
					
		// 创建样式
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平对齐
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直对齐
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		style.setWrapText(true);
		style.setFont(font);

		return style;
	}
	/**
	 * 创建表体样式
	 * 
	 * @return
	 */
	private static HSSFCellStyle createNumStyle(HSSFWorkbook wb) {
		// 创建字体样式
		HSSFFont font = wb.createFont();
		font.setFontName("Verdana");
		font.setFontHeight((short) 220);
		//绿色字体Style
		font.setColor(HSSFColor.GREEN.index);	
		
		// 创建样式
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平对齐
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直对齐
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		style.setWrapText(true);
		style.setFont(font);

		return style;
	}
	/**
	 * 创建表体样式
	 * 
	 * @return
	 */
	private static HSSFCellStyle createTotalStyle(HSSFWorkbook wb) {
		// 创建字体样式
		HSSFFont font = wb.createFont();
		font.setFontName("Verdana");
		font.setFontHeight((short) 240);
		font.setColor(HSSFColor.RED.index);
		
		// 创建样式
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平对齐
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直对齐
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		style.setWrapText(true);
		style.setFont(font);

		return style;
	}
}