package com.westar.base.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.westar.base.annotation.ExcelExportColumn;
import com.westar.base.annotation.ExcelExportTitle;

/**
 * excel导出工具类
 * 
 */
public class ExcelExportUtil {

	private static Logger logger = Logger.getLogger(ExcelExportUtil.class);

	/**
	 * 导出excel到response输出流
	 * 
	 * @param data
	 * @param fileName
	 * @param response
	 */
	public static void exportExcel(List data, String fileName,
			HttpServletResponse response, HttpServletRequest request) {
		OutputStream os = null;
		try {
			if (data != null && data.size() > 0) {
				response.reset();
				String agent = (String) request.getHeader("USER-AGENT");
				if (agent != null && agent.toLowerCase().indexOf("firefox") > 0) {// 火狐
					String enableFileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileName.getBytes("UTF-8"))))+ "?=";
					response.setHeader("Content-Disposition", "attachment; filename=" + enableFileName+".xls");
				} else {
					String enableFileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
					response.setHeader("Content-Disposition", "attachment; filename=" + enableFileName+".xls");
				}
				response.setContentType("application/msexcel");// 定义输出类型
				HSSFWorkbook wb = createWorkbook(data);
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
	 * 导出excel到文件
	 * 
	 * @param data
	 * @param file
	 * @throws Exception
	 */
	public static void exportExcel(List data, File file) throws Exception {
		OutputStream os = null;
		try {
			if (data != null && data.size() > 0) {
				HSSFWorkbook wb = createWorkbook(data);
				os = new FileOutputStream(file);
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
	 * 导出excel到response输出流
	 * 
	 * @param html
	 * @param fileName
	 * @param response
	 */
	public static void exportExcel(String html, String fileName,
			HttpServletResponse response, HttpServletRequest request) {
		OutputStream os = null;
		try {
			if (!StringUtil.isBlank(html)) {
				response.reset();
				fileName = encodeFilename(fileName, request);
				response.setHeader("Content-disposition",
						"attachment; filename=" + fileName + ".xls");// 设定输出文件头
				response.setContentType("application/msexcel");// 定义输出类型
				HSSFWorkbook wb = createWorkbook(html);
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
	 * 导出excel到文件
	 * 
	 * @param html
	 * @param file
	 * @throws Exception
	 */
	public static void exportExcel(String html, File file) throws Exception {
		OutputStream os = null;
		try {
			if (!StringUtil.isBlank(html)) {
				HSSFWorkbook wb = createWorkbook(html);
				os = new FileOutputStream(file);
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
	private static HSSFWorkbook createWorkbook(List data) throws Exception {
		HSSFWorkbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet();
		int headerRowSpan = createHeader(sheet, data.get(0));
		createBody(sheet, data, headerRowSpan);
		return wb;
	}

	/**
	 * 创建excel
	 * 
	 * @param html
	 * @return
	 * @throws Exception
	 */
	/**
	 * @param html
	 * @return
	 * @throws Exception
	 */
	private static HSSFWorkbook createWorkbook(String html) throws Exception {
		HSSFWorkbook wb = new HSSFWorkbook();

		// 解析HTML
		Document document = Jsoup.parse(html);
		Elements table_elements = document.getElementsByTag("table");
		if (table_elements != null && table_elements.size() > 0) {
			for (Element table_element : table_elements) {
				List<String> tempPoint = new ArrayList<String>();
				// 创建ExcelSheet
				Sheet sheet = wb.createSheet();
				Elements tr_elements = table_element.select("tr");
				Map<Integer, Integer> colMaxWidth = new HashMap<Integer, Integer>();
				int rowNum = 0;
				for (Element tr_element : tr_elements) {
					// 创建行
					Row row = sheet.createRow(rowNum);
					Elements td_elements = tr_element.select("td,th");
					int colNum = 0;
					for (Element td_element : td_elements) {
						// 创建单元格
						String text = td_element.text();
						Cell cell = createCell(row, tempPoint, colNum);

						boolean titleFlag = false;
						Elements td_element_parents = td_element.parents();
						for (Element td_element_parent : td_element_parents) {
							if (td_element_parent.tagName().equalsIgnoreCase(
									"thead")) {
								titleFlag = true;
								break;
							}
						}
						if (titleFlag) {
							cell.setCellStyle(createHeaderStyle(wb));
						} else {
							cell.setCellStyle(createBodyStyle(wb));
						}
						cell.setCellValue(text);

						int rowspan = 1;
						int colspan = 1;
						if (!StringUtil.isBlank(td_element.attr("rowspan"))) {
							rowspan = Integer.parseInt(td_element
									.attr("rowspan"));
						}
						if (!StringUtil.isBlank(td_element.attr("colspan"))) {
							colspan = Integer.parseInt(td_element
									.attr("colspan"));
						}

						// 计算并保存空格单元坐标
						if (rowspan > 1 || colspan > 1) {
							for (int x = rowNum + 1; x < rowNum + rowspan; x++) {
								for (int y = colNum; y < colNum + colspan; y++) {
									tempPoint.add(x + "," + y);
								}
							}
						}
						// 合并单元格
						sheet.addMergedRegion(new CellRangeAddress(rowNum,
								rowNum + rowspan - 1, cell.getColumnIndex(),
								cell.getColumnIndex() + colspan - 1));
						colNum = cell.getColumnIndex() + colspan - 1;

						// 保存当前列的最大长度
						Integer width = colMaxWidth.get(colNum);
						if (width == null) {
							colMaxWidth.put(colNum, text.getBytes().length);
						} else {
							if (width < text.getBytes().length) {
								colMaxWidth.put(colNum, text.getBytes().length);
							}
						}

						colNum++;
					}
					rowNum++;
				}
				// 自动适应高度
				for (Iterator<Integer> iterator = colMaxWidth.keySet()
						.iterator(); iterator.hasNext();) {
					Integer key = iterator.next();
					sheet.setColumnWidth(key, (colMaxWidth.get(key) + 1) * 256);
				}
			}
		}
		return wb;
	}

	/**
	 * 创建单元格
	 * 
	 * @param row
	 * @param tempPoint
	 * @param cellNum
	 * @return
	 */
	private static Cell createCell(Row row, List<String> tempPoint, int cellNum) {
		if (tempPoint.contains(row.getRowNum() + "," + cellNum)) {
			return createCell(row, tempPoint, cellNum + 1);
		}
		return row.createCell(cellNum);
	}

	/**
	 * 创建标题和表头
	 * 
	 * @param sheet
	 * @param dataRow
	 */
	private static int createHeader(Sheet sheet, Object dataRow) {
		List<Field> exportFields = listExportFields(dataRow);
		int headerRowSpan = 1;
		if (exportFields.size() > 0) {
			// 创建标题
			String title = ""; // 标题
			ExcelExportTitle titleAnnotation = dataRow.getClass().getAnnotation(ExcelExportTitle.class);
			if (titleAnnotation != null) {
				title = titleAnnotation.title();
			}

			int headerIndex = 0;

			if (!StringUtil.isBlank(title)) {
				Row titleRow = sheet.createRow(0);
				Cell titleCell = titleRow.createCell(0);
				titleCell.setCellStyle(createTitleStyle((HSSFWorkbook) sheet.getWorkbook()));
				titleCell.setCellValue(title);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0,exportFields.size() - 1));// 设置跨列
				headerIndex++;
				headerRowSpan++;
			}

			// 创建表头
			Row headerRow = sheet.createRow(headerIndex);
			for (int i = 0; i < exportFields.size(); i++) {
				Field field = exportFields.get(i);
				ExcelExportColumn excelExportColumn = field
						.getAnnotation(ExcelExportColumn.class);
				Cell headerCell = headerRow.createCell(i);
				headerCell.setCellStyle(createHeaderStyle((HSSFWorkbook) sheet
						.getWorkbook()));
				headerCell.setCellValue(excelExportColumn.name());
			}

		}
		return headerRowSpan;

	}

	/**
	 * 创建表身
	 * 
	 * @param sheet
	 * @param dataRow
	 */
	private static void createBody(Sheet sheet, List data, int headerRowSpan)
			throws Exception {
		List<Field> exportFields = listExportFields(data.get(0));
		if (exportFields.size() > 0) {
			HSSFCellStyle style = createBodyStyle((HSSFWorkbook) sheet.getWorkbook());
			for (int i = 0; i < data.size(); i++) {
				Object o = data.get(i);
				Row row = sheet.createRow(i + headerRowSpan);
				for (int j = 0; j < exportFields.size(); j++) {
					Cell cell = row.createCell(j);
					cell.setCellStyle(style);
					Field field = exportFields.get(j);
					field.setAccessible(true);
					cell.setCellValue(field.get(o).toString());
				}
			}

			// 设置自动适应宽度
			for (int i = 0; i < exportFields.size(); i++) {
				sheet.autoSizeColumn(i);
			}

		}
	}

	/**
	 * 获取需要导出的字段（排序后）
	 * 
	 * @param dataRow
	 * @return
	 */
	private static List<Field> listExportFields(Object dataRow) {
		// 获取需要导出的字段
		List<Field> exportFields = new ArrayList<Field>();
		Field[] allFields = dataRow.getClass().getDeclaredFields();
		for (Field field : allFields) {
			if (field.getAnnotation(ExcelExportColumn.class) != null) {
				exportFields.add(field);
			}
		}

		// 根据排序号进行排序
		Collections.sort(exportFields, new Comparator<Field>() {
			@Override
			public int compare(Field f1, Field f2) {
				int order1 = f1.getAnnotation(ExcelExportColumn.class).order();
				int order2 = f2.getAnnotation(ExcelExportColumn.class).order();
				if (order1 > order2) {
					return 1;
				} else if (order1 < order2) {
					return -1;
				}
				return 0;
			}
		});

		return exportFields;
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
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

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
		font.setFontHeight((short) 220);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

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

	private static String encodeFilename(String filename,
			HttpServletRequest request) {
		/**
		 * 获取客户端浏览器和操作系统信息 在IE浏览器中得到的是：User-Agent=Mozilla/4.0 (compatible; MSIE
		 * 6.0; Windows NT 5.1; SV1; Maxthon; Alexa Toolbar)
		 * 在Firefox中得到的是：User-Agent=Mozilla/5.0 (Windows; U; Windows NT 5.1;
		 * zh-CN; rv:1.7.10) Gecko/20050717 Firefox/1.0.6
		 */
		String agent = request.getHeader("USER-AGENT");
		try {
			if ((agent != null) && (-1 != agent.indexOf("MSIE"))) {
				String newFileName = URLEncoder.encode(filename, "UTF-8");
				newFileName = StringUtils.replace(newFileName, "+", "%20");
				if (newFileName.length() > 150) {
					newFileName = new String(filename.getBytes("GB2312"),
							"ISO8859-1");
					newFileName = StringUtils.replace(newFileName, " ", "%20");
				}
				return newFileName;
			}
			if ((agent != null) && (-1 != agent.indexOf("Mozilla"))){
				return MimeUtility.encodeText(filename, "UTF-8", "B");
			}

			return filename;
		} catch (Exception ex) {
			return filename;
		}
	}

	public static Set<String> getCells(File logoFile) {
		String fileName = logoFile.getName();
		Set<String> set = new HashSet<String>();
		try {
			InputStream inputStream = new FileInputStream(logoFile);
			Workbook wb = null;
			if (fileName.endsWith("xls")) {
				wb = new HSSFWorkbook(inputStream);// 解析xls格式
			} else if (fileName.endsWith("xlsx")) {
				wb = new XSSFWorkbook(inputStream);// 解析xlsx格式
			}
			for (int i = 0; i < wb.getNumberOfSheets(); i++) {

				Sheet sheet = wb.getSheetAt(i);// 第i个工作表
				int firstRowIndex = sheet.getFirstRowNum();
				int lastRowIndex = sheet.getLastRowNum();
				for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {
					Row row = sheet.getRow(rIndex);
					if (row != null) {
						int firstCellIndex = row.getFirstCellNum();
						int lastCellIndex = row.getLastCellNum();
						for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {
							Cell cell = row.getCell(cIndex);
							String value = "";
							if (cell != null) {
								value = cell.toString();
								set.add(value);
							}
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return set;
	}
	
	public static Set<String> getTxt(File logoFile) {
		Set<String> set = new HashSet<String>();
		InputStreamReader read = null;
		BufferedReader bufferedReader = null;
		try {
			String encoding = "GBK";
			read = new InputStreamReader(new FileInputStream(
					logoFile), encoding);// 考虑到编码格式
			bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			while ((lineTxt = bufferedReader.readLine()) != null) {
				lineTxt = lineTxt.replaceAll(".com", ".com;");
				StringTokenizer st = new StringTokenizer(lineTxt,
						"\t\n\r\f;,；，。:： ", false);
				while (st.hasMoreElements()) {
					String next = st.nextToken();
					if (!"".equals(next)) {
						set.add(next);
					}
				}
			}
		} catch (Exception e) {
			logger.info("读取文件内容出错", e);
			e.printStackTrace();
		} finally{
			if(null!=read){
				try {
					read.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(null!=bufferedReader){
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return set;
	}
}
