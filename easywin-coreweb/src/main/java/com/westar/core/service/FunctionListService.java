package com.westar.core.service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.sun.jna.Function;
import com.westar.base.pojo.HttpResult;
import com.westar.base.util.BeanRefUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.base.util.Encodes;
import freemarker.template.SimpleDate;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.westar.base.model.FunctionList;
import com.westar.base.model.UserInfo;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.core.dao.FunctionListDao;

@Service
public class FunctionListService {

	@Autowired
	FunctionListDao functionListDao;
	
	@Autowired
	ItemService itemService;

	/**
	 * 获取功能清单树
	 * @author hcj 
	 * @date: 2018年10月17日 下午1:19:20
	 * @param userInfo
	 * @param functionList
	 * @return
	 */
	public List<FunctionList> listTreeFun(UserInfo userInfo, FunctionList functionList) {
		return functionListDao.listTreeFun(userInfo, functionList);
	}
	
	/**
	 * 新增功能
	 * @author hcj 
	 * @date: 2018年10月17日 下午3:28:33
	 * @param functionList
	 * @param userInfo
	 */
	public void addFunction(FunctionList functionList, UserInfo userInfo) {
		functionList.setComId(userInfo.getComId());
		functionList.setCreator(userInfo.getId());
		functionListDao.add(functionList);
		if(ConstantInterface.TYPE_ITEM.equals(functionList.getBusType())){
			itemService.addItemLog(userInfo.getComId(),functionList.getBusId(),userInfo.getId(),userInfo.getUserName(),"添加了功能清单功能："+functionList.getFunctionName());
	}
	}
	
	/**
	 * 根据id获取功能详情
	 * @author hcj 
	 * @date: 2018年10月17日 下午4:12:52
	 * @param id
	 * @return
	 */
	public FunctionList queryFunById(Integer id) {
		return functionListDao.queryFunById(id);
	}
	
	/**
	 * 更新功能
	 * @author hcj 
	 * @date: 2018年10月17日 下午4:30:34
	 * @param functionList
	 * @param userInfo
	 */
	public void updateFunction(FunctionList functionList, UserInfo userInfo) {
		functionListDao.update(functionList);
		functionList = this.queryFunById(functionList.getId());
		if(ConstantInterface.TYPE_ITEM.equals(functionList.getBusType())){
			itemService.addItemLog(userInfo.getComId(),functionList.getBusId(),userInfo.getId(),userInfo.getUserName(),"更新了功能清单功能："+functionList.getFunctionName());
		}
		
	}
	
	/**
	 * 批量删除功能
	 * @author hcj 
	 * @date: 2018年10月17日 下午4:49:37
	 * @param ids
	 * @param userInfo
	 */
	public void delFunction(Integer[] ids, UserInfo userInfo) {
		//添加日志
		if(!CommonUtil.isNull(ids)){
			String content = "";
			String busType = "";
			Integer busId = null;
			for (int i = 0; i < ids.length; i++) {
				FunctionList functionList = this.queryFunById(ids[i]);
				if(!CommonUtil.isNull(functionList)){
					if("".equals(content) && "".equals(busType) && null==busId){
						busType = functionList.getBusType();
						busId = functionList.getBusId();
						content += functionList.getFunctionName();
					}else{
						content += ","+functionList.getFunctionName();
					}
				}
				
			}
			if(ConstantInterface.TYPE_ITEM.equals(busType)){
				itemService.addItemLog(userInfo.getComId(),busId,userInfo.getId(),userInfo.getUserName(),"删除了功能清单功能："+content);
			}
		}
		functionListDao.delById(FunctionList.class, ids);
	}

	/**
	 * 根据选择的项目或者产品导入功能清单
	 * @author hcj 
	 * @date: 2018年10月18日 上午10:32:13
	 * @param busId
	 * @param busType
	 * @param chooseBusType
	 * @param chooseBusId
	 * @param userInfo
	 */
	public void addImportFunctionList(Integer busId, String busType, String chooseBusType, Integer chooseBusId,
			UserInfo userInfo) {
		//删除之前的功能清单
		functionListDao.delByField("functionList", new String[]{"comId","busId","busType"}, new Object[]{userInfo.getComId(),busId,busType});
		//导入选择的功能清单
		functionListDao.addImportFunctionList(busId,busType,chooseBusType,chooseBusId,userInfo) ;
	}

	/**
	 * 读取excel
	 * @param filePath 文件地址
	 * @return
	 */
	public HttpResult<Integer> readExcel(String filePath, UserInfo userInfo, Integer id,String busType) {
		HttpResult<Integer> result = new HttpResult<>();
 		//结束所有循环
		boolean allBreak = false;
        //查询所有功能名
        List<FunctionList> oldTable = new ArrayList<>();
		result.setCode(HttpResult.CODE_OK);
		result.setMsg("导入成功！");
		try {
			// 读取Excel文件
			InputStream inputStream = new FileInputStream(filePath);
			HSSFWorkbook excel = new HSSFWorkbook(inputStream);

			// 循环工作表Sheet
			for (int numSheet = 0; numSheet < excel.getNumberOfSheets(); numSheet++) {
				HSSFSheet sheet = excel.getSheetAt(numSheet);
				if (sheet == null) {
					continue;
				}
                List<Integer> functionCache = new ArrayList<>();
				// 读行
				for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
					HSSFRow row = sheet.getRow(rowNum);
					int column = row.getPhysicalNumberOfCells();
					if (row == null) {
						continue;
					}

					//读列
					FunctionList functionList = null;
					for(int j = 0;j < column;j = j + 2){
						oldTable = functionListDao.listTreeFun(userInfo,new FunctionList());
					    //获取单元格内容
					    String functionName = getValue(row.getCell(j),String.class).toString();
					    String functionDescribe = getValue(row.getCell(j + 1),String.class).toString();
					    //如果为空则不继续
                        if("".equals(functionName)){
                            continue;
                        }

                        //初始化功能
                        functionList = new FunctionList();
                        functionList.setComId(userInfo.getComId());
                        functionList.setCreator(userInfo.getId());
                        functionList.setBusId(id);
                        functionList.setBusType(busType);
                        functionList.setFunctionName(functionName);
                        functionList.setFunctionDescribe(functionDescribe);

                        //检测是否是已经存在的功能
                        for(FunctionList functionListT : oldTable){
                            if(functionListT.getFunctionName().equals(functionName)){
                                //如果是，则将id给新的功能，准备更新
                                functionList.setId(functionListT.getId());
                                break;
                            }
                        }

                        if(j == 0){
                            //第一个单元格有内容的都是根节点下的二级节点
                            functionList.setParentId(-1);
                            //如果第一个单元格有内容，该单元格必然是新的一行，所以清除内容，为下一行做准备
                            functionCache.clear();
                        }else{
                            //获取缓存中前一个位置的功能作为父节点
                            functionList.setParentId(functionCache.get((j / 2) - 1));
                        }

                        //当存在父子结点相同的情况则结束所有循环，并打印错误信息
                        if(functionList.getParentId().equals(functionList.getId())){
                        	//跳出所有循环
							allBreak = true;
							result.setCode(HttpResult.CODE_ERROR);
							result.setMsg("sheet" + (numSheet + 1) + "-行" + (rowNum + 1) + "：存在父子结点相同！");
							break;
						}

                        //根据id判断是更新还是添加
                        if(null != functionList.getId()){
                            functionListDao.update(functionList);
                            //如果缓存长度不够则添加，够了说明之前该位置有数据，需要替换
							if(functionCache.size() > j / 2){
								functionCache.set(j / 2,functionList.getId());
							}else{
								functionCache.add(functionList.getId());
							}
                        }else{
                            Integer parentId = functionListDao.add(functionList);
							//如果缓存长度不够则添加，够了说明之前该位置有数据，需要替换
                            if(functionCache.size() > j / 2){
								functionCache.set(j / 2,parentId);
							}else{
                            	functionCache.add(parentId);
							}
                        }
					}
					if(allBreak){
						break;
					}
				}
				if(allBreak){
					break;
				}
			}
		} catch (Exception e) {
			result.setCode(HttpResult.CODE_ERROR);
			result.setMsg("-：请检查内容格式！");
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 根据type进行返回值的处理
	 * @param cell
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unused")
	private static Object getValue(HSSFCell cell, Class type) {

		String className = type.getSimpleName();
		try {
			if("Byte".equals(className)){
				return new Byte(new Double(cell.getNumericCellValue()).byteValue());
			}else if("Short".equals(className)){
				return new Short(new Double(cell.getNumericCellValue()).shortValue());
			}else if("Integer".equals(className)){
				return new Integer(new Double(cell.getNumericCellValue()).intValue());
			}else if("Long".equals(className)){
				return new Long(new Double(cell.getNumericCellValue()).longValue());
			}else if("Double".equals(className)){
				return new Double(cell.getNumericCellValue()).byteValue();
			}else if("Float".equals(className)){
				return new Float(new Double(cell.getNumericCellValue()).floatValue());
			}else if("Character".equals(className)){
				cell.setCellType(Cell.CELL_TYPE_STRING);
				return new Character(cell.getStringCellValue().charAt(0));
			}else if("String".equals(className)){
				cell.setCellType(Cell.CELL_TYPE_STRING);
				return cell.getStringCellValue();
			}else if("Date".equals(className)){
 				Date date = cell.getDateCellValue();
				return date;
			}else if("Boolean".equals(className)){
				return cell.getBooleanCellValue();
			}else{
				return "字段读取失败！";
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return "字段读取失败！";
	}
}
