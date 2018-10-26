package com.westar.base.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import com.westar.base.model.Item;
import com.westar.base.pojo.ItemStagedInfo;

public class TaskItemStageExport {
	private static Logger logger = Logger.getLogger(TaskItemStageExport.class);

	/**
	 * 导出excel到response输出流
	 * 
	 * @param data
	 * @param fileName
	 * @param response
	 */
	public static void exportExcel(List<Item> data, String fileName, List<String> headTitle, HttpServletResponse response,
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
	private static HSSFWorkbook createWorkbook(List<Item> data, String titleName, List<String> headTitle) throws Exception {
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
	private static void createBody(Sheet sheet, List<Item> data, int headerRowSpan) throws Exception {
		int rowNum = headerRowSpan+1;
		
		if(!data.isEmpty() && data.size() > 0){
			HSSFWorkbook wb = (HSSFWorkbook) sheet.getWorkbook();
			HSSFCellStyle style = createBodyStyle(wb);
			HSSFCellStyle numStyle = createNumStyle(wb);
			HSSFCellStyle totalStyle = createTotalStyle(wb);
			
			for(int i =0;i<data.size();i++){//循环数据
				Item item = data.get(i);
				int stageSize = item.getListStagedItemInfo().size();//项目阶段数量
				
				if(stageSize >0){
					int taskTotal = 0;
					int usedTimeTotal = 0;
					int overTimeTotal = 0;
					//处理阶段
					for(int j= 0;j<stageSize;j++){
						ItemStagedInfo stageInfo = item.getListStagedItemInfo().get(j);
						//写入数据
						Row stageRow = sheet.createRow(rowNum+j);
						
						Cell cell0 = stageRow.createCell(0);//序号
						cell0.setCellStyle(style);
						Cell cell1 = stageRow.createCell(1);//项目名
						cell1.setCellStyle(style);
						Cell cell2 = stageRow.createCell(2);//负责人
						cell2.setCellStyle(style);
						Cell cell3 = stageRow.createCell(3);//创建于
						cell3.setCellStyle(style);
						
						if(j == 0){
							cell0.setCellValue(i+1);
							cell1.setCellValue(item.getItemName());
							cell2.setCellValue(item.getOwnerName());
							cell3.setCellValue(item.getRecordCreateTime());
							
						}else{
							cell0.setCellValue("");
							cell1.setCellValue("");
							cell2.setCellValue("");
							cell3.setCellValue("");
						}
						//阶段名
						Cell cell4 = stageRow.createCell(4);
						cell4.setCellStyle(style);
						cell4.setCellValue(stageInfo.getName());
						
						//任务数
						Cell cell5 = stageRow.createCell(5);
						cell5.setCellStyle(numStyle);
						cell5.setCellValue(stageInfo.getListTask().size());
						taskTotal += stageInfo.getListTask().size();
						
						//耗时
						Cell cell6 = stageRow.createCell(6);
						cell6.setCellStyle(numStyle);
						cell6.setCellValue(stageInfo.getUsedTimes());
						usedTimeTotal += stageInfo.getUsedTimes();
						
						//超时
						Cell cell7 = stageRow.createCell(7);
						cell7.setCellStyle(numStyle);
						cell7.setCellValue(stageInfo.getOverTimes() >0 ? stageInfo.getOverTimes():0);
						overTimeTotal += stageInfo.getOverTimes() >0 ? stageInfo.getOverTimes():0;
					}
					//处理合计
					Row totalRow = sheet.createRow(rowNum + stageSize);
					//红色字体Style
					
					
					Cell cell0 = totalRow.createCell(0);//序号
					cell0.setCellStyle(style);
					cell0.setCellValue("");
					
					Cell cell1 = totalRow.createCell(1);//项目名
					cell1.setCellStyle(style);
					cell1.setCellValue("");
					
					Cell cell2 = totalRow.createCell(2);//负责人
					cell2.setCellStyle(style);
					cell2.setCellValue("");
					
					Cell cell3 = totalRow.createCell(3);//创建于
					cell3.setCellStyle(style);
					cell3.setCellValue("");
					
					Cell cell4 = totalRow.createCell(4);
					cell4.setCellStyle(totalStyle);
					cell4.setCellValue("合计");
					
					Cell cell5 = totalRow.createCell(5);
					cell5.setCellStyle(totalStyle);
					cell5.setCellValue(taskTotal);
					
					Cell cell6 = totalRow.createCell(6);
					cell6.setCellStyle(totalStyle);
					cell6.setCellValue(usedTimeTotal);
					
					Cell cell7 = totalRow.createCell(7);
					cell7.setCellStyle(totalStyle);
					cell7.setCellValue(overTimeTotal);
					
					//跨行
					sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum + stageSize, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum + stageSize, 1, 1));
					sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum + stageSize, 2, 2));
					sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum + stageSize, 3, 3));
				}else{//无项目阶段
					
					Row itemRow = sheet.createRow(rowNum);
					Cell cell0 = itemRow.createCell(0);//序号
					cell0.setCellStyle(style);
					cell0.setCellValue(i+1);
					
					Cell cell1 = itemRow.createCell(1);//项目名
					cell1.setCellStyle(style);
					cell1.setCellValue(item.getItemName());
					
					Cell cell2 = itemRow.createCell(2);//负责人
					cell2.setCellStyle(style);
					cell2.setCellValue(item.getOwnerName());
					
					Cell cell3 = itemRow.createCell(3);//创建于
					cell3.setCellStyle(style);
					cell3.setCellValue(item.getRecordCreateTime());
					
					Cell cell4 = itemRow.createCell(4);
					cell4.setCellStyle(style);
					cell4.setCellValue("");
					
					Cell cell5 = itemRow.createCell(5);
					cell5.setCellStyle(style);
					cell5.setCellValue("");
					
					Cell cell6 = itemRow.createCell(6);
					cell6.setCellStyle(style);
					cell6.setCellValue("");
					
					Cell cell7 = itemRow.createCell(7);
					cell7.setCellStyle(style);
					cell7.setCellValue("");
					
					//跨行合并
					sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 1, 1));
					sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 2, 2));
					sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 3, 3));
				}
				
				rowNum = rowNum + stageSize +1;
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