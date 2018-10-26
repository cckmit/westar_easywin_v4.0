package com.westar.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.westar.base.model.FormCompon;
import com.westar.base.model.FormLayout;
import com.westar.base.model.FormMod;
import com.westar.base.model.SpFlowHiStep;
import com.westar.base.model.SpFlowModel;
import com.westar.base.model.SpFlowRelevanceCfg;
import com.westar.base.model.SpFlowScopeByDep;
import com.westar.base.model.SpFlowScopeByUser;
import com.westar.base.model.SpFlowStep;
import com.westar.base.model.SpFlowStepExecutor;
import com.westar.base.model.SpFlowStepRelation;
import com.westar.base.model.SpFlowType;
import com.westar.base.model.SpStepConditions;
import com.westar.base.model.SpStepFormControl;
import com.westar.base.model.UserInfo;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;
import com.westar.base.util.DateTimeUtil;
import com.westar.core.dao.FlowDesignDao;
import com.westar.core.thread.IndexUpdateThread;

@Service
public class FlowDesignService {

    private static final Log loger = LogFactory.getLog(IndexUpdateThread.class);

    @Autowired
    FlowDesignDao flowDesignDao;


    @Autowired
    UserInfoService userInfoService;

    @Autowired
    FormService formService;

    @Autowired
    CusActivitService cusActivitService;
    @Autowired
    LogsService logsService;

    /**
     * 获取流程模型集合
     *
     * @return
     */
    public List<SpFlowModel> listFlowModel(UserInfo userInfo, SpFlowModel spFlowModel) {
        return flowDesignDao.listFlowModel(userInfo, spFlowModel);
    }

    /**
     * 初始化流程起始步骤
     *
     * @return
     */
    public String orgFlowStepInit() {
        // 默认流程起始步骤
        StringBuffer process = new StringBuffer("{'total':5,'list':[");
        // 开始步骤
        process.append("{'id':'firstStep','flow_id':'4','process_name':'开始','process_to':'endStep','icon':'icon-ok',");
        process.append("'style':'width:100px;height:30px;line-height:30px;color:#0e76a8;left:100px;top:orgHighInit;'},");
        // 结束步骤
        process.append("{'id':'endStep','flow_id':'4','process_name':'结束','process_to':'','icon':'icon-star',");
        process.append("'style':'width:100px;height:30px;line-height:30px;color:#0e76a8;left:350px;top:orgHighInit;'}");
        process.append("]}");

        return process.toString();
    }

    /**
     * 新增流程
     *
     * @param userInfo
     * @param flowConfig
     * @return
     */
    public void addFlow(UserInfo userInfo, SpFlowModel flowConfig) {
        flowConfig.setComId(userInfo.getComId());// 设置团队主键
        flowConfig.setCreator(userInfo.getId());// 设置流程创建人
        flowConfig.setStatus(1);// 启用流程
        flowConfig.setDeployed(0);//流程是否部署(0未部署)
        flowConfig.setTitleRule("{user}" + flowConfig.getFlowName() + "-{year}年{month}月{day}日");//默认流程实例显示title
        
        Integer spFlowTypeId = flowConfig.getSpFlowTypeId();
        if(null == spFlowTypeId){
        	flowConfig.setSpFlowTypeId(0);
        }
        
        Integer flowId = flowDesignDao.add(flowConfig);
        // 创建流程时，默认添加开始和结束步骤
        SpFlowStep startStep = new SpFlowStep();
        startStep.setComId(userInfo.getComId());// 设置团队主键
        startStep.setFlowId(flowId);// 关联流程主键
        startStep.setStepName("开始");
        startStep.setStepType("start");
        startStep.setSpCheckCfg(ConstantInterface.SPSTEP_CHECK_NO);//默认步骤不进行验证码权限验证
        Integer startStepId = flowDesignDao.add(startStep);
        SpFlowStep endStep = new SpFlowStep();
        endStep.setComId(userInfo.getComId());// 设置团队主键
        endStep.setFlowId(flowId);// 关联流程主键
        endStep.setStepName("结束");
        endStep.setStepType("end");
        endStep.setSpCheckCfg(ConstantInterface.SPSTEP_CHECK_NO);//默认步骤不进行验证码权限验证
        Integer endStepId = flowDesignDao.add(endStep);
        SpFlowStepRelation stepRelation = new SpFlowStepRelation();// 建立开始步骤与结束步骤间关系
        stepRelation.setComId(userInfo.getComId());
        stepRelation.setFlowId(flowId);
        stepRelation.setCurStepId(startStepId);
        stepRelation.setNextStepId(endStepId);
        flowDesignDao.add(stepRelation);
        logsService.addLogs(userInfo.getComId(), flowId, userInfo.getId(), userInfo.getUserName(),
                "添加流程：" + flowConfig.getFlowName(), ConstantInterface.TYPE_WORK_FLOW);
    }

    /**
     * 获取流程的配置详情
     *
     * @param userInfo
     * @param flowId   流程主键
     * @return
     */
    public SpFlowModel querySpFlowModel(UserInfo userInfo, Integer flowId) {
        SpFlowModel spFlowModel = flowDesignDao.querySpFlowModel(userInfo,
                flowId);
        if (null != spFlowModel) {
            List<SpFlowStep> listFlowSteps = flowDesignDao.listFlowSteps(
                    userInfo.getComId(), flowId);
            // 去重
            Map<Integer, SpFlowStep> stepMap = new LinkedHashMap<Integer, SpFlowStep>();
            for (SpFlowStep step : listFlowSteps) {
                if (stepMap.containsKey(step.getId())) {
                    if (stepMap.get(step.getId()).getStepLevel() < step
                            .getStepLevel()) {// 如果是相同步骤，则保留层级最高的（level）
                        stepMap.remove(step.getId());
                        stepMap.put(step.getId(), step);
                    }
                } else {
                    stepMap.put(step.getId(), step);
                }
            }
            listFlowSteps = new ArrayList<SpFlowStep>(stepMap.values());
            spFlowModel.setListFlowSteps(listFlowSteps);// 关联流程的步骤
            for (SpFlowStep stepVo : listFlowSteps) {
                if (!stepVo.getStepType().equals("end")) {
                    List<SpFlowStep> listNextStep = flowDesignDao
                            .listNextFlowStep(userInfo.getComId(), flowId,
                                    stepVo.getId());
                    stepVo.setListNextStep(listNextStep);// 关联流程下步骤集合
                }
            }
            //查询流程根据部门设置的发起权限集合
            List<SpFlowScopeByDep> listSpFlowScopeByDep = flowDesignDao.listSpFlowScopeByDep(userInfo.getComId(), flowId);
            spFlowModel.setListSpFlowScopeByDep(listSpFlowScopeByDep);
            //查询流程根据人员设置的发起权限集合
            List<SpFlowScopeByUser> listSpFlowScopeByUser = flowDesignDao.listSpFlowScopeByUser(userInfo.getComId(), flowId);
            spFlowModel.setListSpFlowScopeByUser(listSpFlowScopeByUser);
        }
        return spFlowModel;
    }

    /**
     * 获取有效的流程配置信息
     *
     * @param userInfo
     * @param flowId   流程主键
     * @return
     */
    public SpFlowModel querySpFlowModelForDeploy(UserInfo userInfo, Integer flowId) {
        SpFlowModel spFlowModel = flowDesignDao.querySpFlowModel(userInfo,
                flowId);
        if (null != spFlowModel) {
            List<SpFlowStep> listFlowSteps = flowDesignDao.listFlowStepsForDeploy(
                    userInfo.getComId(), flowId);
            // 去重
            Map<Integer, SpFlowStep> stepMap = new LinkedHashMap<Integer, SpFlowStep>();
            for (SpFlowStep step : listFlowSteps) {
                if (stepMap.containsKey(step.getId())) {
                    if (stepMap.get(step.getId()).getStepLevel() < step
                            .getStepLevel()) {// 如果是相同步骤，则保留层级最高的（level）
                        stepMap.remove(step.getId());
                        stepMap.put(step.getId(), step);
                    }
                } else {
                    stepMap.put(step.getId(), step);
                }
            }
            listFlowSteps = new ArrayList<SpFlowStep>(stepMap.values());
            spFlowModel.setListFlowSteps(listFlowSteps);// 关联流程的步骤
            for (SpFlowStep stepVo : listFlowSteps) {
                if (!stepVo.getStepType().equals("end")) {
                    List<SpFlowStep> listNextStep = flowDesignDao
                            .listNextFlowStep(userInfo.getComId(), flowId,
                                    stepVo.getId());
                    stepVo.setListNextStep(listNextStep);// 关联流程下步骤集合
                }
            }
        }
        return spFlowModel;
    }

    /**
     * 删除部署流程模型
     *
     * @param userInfo 当前操作人
     * @param flowId   流程主键
     * @return
     */
    public boolean delFlow(UserInfo userInfo, Integer flowId) {
        SpFlowModel delSpFlowModel = flowDesignDao.querySpFlowModel(userInfo, flowId);//获取需删除的流程模型信息
        flowDesignDao.delByField("spStepConditions", new String[]{"comId",
                "flowId"}, new Object[]{userInfo.getComId(), flowId});// 删除步骤条件
        flowDesignDao.delByField("spStepFormControl", new String[]{"comId",
                "flowId"}, new Object[]{userInfo.getComId(), flowId});// 删除步骤权限控制
        flowDesignDao.delByField("spFlowStepRelation", new String[]{"comId",
                "flowId"}, new Object[]{userInfo.getComId(), flowId});// 删除流程步骤关系
        flowDesignDao.delByField("spFlowStepExecutor", new String[]{"comId",
                "flowId"}, new Object[]{userInfo.getComId(), flowId});// 删除流程步骤审批人
        flowDesignDao.delByField("spFlowStep",
                new String[]{"comId", "flowId"},
                new Object[]{userInfo.getComId(), flowId});// 删除流程步骤
        flowDesignDao.delByField("spFlowScopeByUser", new String[]{"comId", "flowId"},
                new Object[]{userInfo.getComId(), flowId});// 删除通过人员控制流程范发起权限
        flowDesignDao.delByField("spFlowScopeByDep", new String[]{"comId", "flowId"},
                new Object[]{userInfo.getComId(), flowId});// 删除通过部门控制流程范发起权限
        flowDesignDao.delByField("spFlowModel", new String[]{"comId", "id"},
                new Object[]{userInfo.getComId(), flowId});// 删除流程定义表

        //删除流程模型后，删除activity部署信息
        cusActivitService.delFlowMod(delSpFlowModel);
        return true;
    }

    /**
     * 新增流程步骤
     *
     * @param stepVo  步骤配置信息
     * @param curUser 当前操作人信息
     */
    public void addFlowStep(SpFlowStep stepVo, UserInfo curUser) {
        // 新增步骤
        stepVo.setComId(curUser.getComId());
        stepVo.setStepType("exe");
        Integer stepId = flowDesignDao.add(stepVo);
        stepVo.setId(stepId);
        // 如果executorWay为appointMan指定人方式时；初始化步骤审批人
        if ("appointMan".equals(stepVo.getExecutorWay())) {
            if (null != stepVo.getListExecutor()
                    && !stepVo.getListExecutor().isEmpty()) {
                SpFlowStepExecutor spFlowStepExecutor = null;
                for (UserInfo executor : stepVo.getListExecutor()) {
                    spFlowStepExecutor = new SpFlowStepExecutor();
                    spFlowStepExecutor.setComId(curUser.getComId());
                    spFlowStepExecutor.setFlowId(stepVo.getFlowId());
                    spFlowStepExecutor.setStepId(stepId);
                    spFlowStepExecutor.setExecutor(executor.getId());
                    flowDesignDao.add(spFlowStepExecutor);
                }
            }
        }
        // 步骤表单授权
        if (null != stepVo.getFormComponIds()
                && stepVo.getFormComponIds().length > 0) {
            SpStepFormControl sfc = null;
            for (String colKey : stepVo.getFormComponIds()) {
                sfc = new SpStepFormControl();
                sfc.setComId(curUser.getComId());
                sfc.setFlowId(stepVo.getFlowId());
                sfc.setFormControlKey(colKey.split(";")[0]);
                sfc.setStepId(stepId);
                sfc.setIsFill(Integer.parseInt(colKey.split(";")[1]));
                flowDesignDao.add(sfc);
            }
        }

        //环形验证
        boolean checkState = this.checkStepLoop(stepVo, curUser, stepVo.getPstepId());
        if (!checkState) {//没通过，则默认一个
            stepVo.setNextStepIds(null);
        }

        // 创建当前步骤与父步骤之间的关系
        SpFlowStepRelation stepRelation = new SpFlowStepRelation();
        stepRelation.setComId(curUser.getComId());
        stepRelation.setFlowId(stepVo.getFlowId());
        stepRelation.setCurStepId(stepVo.getPstepId());
        stepRelation.setNextStepId(stepId);
        flowDesignDao.add(stepRelation);

        // 如果新建步骤的同级步骤大于1时，默认统计步骤之间关系为并行
        List<SpFlowStep> listNextStep = this.listNextFlowStep(
                curUser.getComId(), stepVo.getFlowId(), stepVo.getPstepId());
        if (null != listNextStep && listNextStep.size() > 1) {
            stepRelation = new SpFlowStepRelation();
            stepRelation.setComId(curUser.getComId());
            stepRelation.setFlowId(stepVo.getFlowId());
            stepRelation.setCurStepId(stepVo.getPstepId());
            stepRelation.setStepWay("branch");
            flowDesignDao.updateChildrenFlowStepInSameStepWay(stepRelation);
            stepRelation.setDefaultStep(0);// 先全部设置为0状态
            flowDesignDao
                    .update("update spFlowStepRelation a set a.defaultStep=:defaultStep "
                                    + "where a.comid=:comId and a.flowId=:flowId and a.curStepId=:curStepId",
                            stepRelation);
            stepRelation.setDefaultStep(1);// 再把当前步骤设置为默认步骤1状态
            stepRelation.setNextStepId(stepId);
            flowDesignDao
                    .update("update spFlowStepRelation a set a.defaultStep=:defaultStep "
                                    + "where a.comid=:comId and a.flowId=:flowId and a.curStepId=:curStepId and a.nextStepId=:nextStepId",
                            stepRelation);
        }

        // 设置下一步步骤
        Integer[] nextStepIds = stepVo.getNextStepIds();
        if (null != nextStepIds) {// 设置流程步骤下步骤关系
            for (int childStepId : nextStepIds) {
                stepRelation = new SpFlowStepRelation();
                stepRelation.setComId(curUser.getComId());
                stepRelation.setFlowId(stepVo.getFlowId());
                stepRelation.setCurStepId(stepId);
                stepRelation.setNextStepId(childStepId);
                flowDesignDao.add(stepRelation);
            }
        } else {
            // 没有下一步骤，则当前父级步骤作为当前步骤的下步步骤
            stepRelation = new SpFlowStepRelation();
            stepRelation.setComId(curUser.getComId());
            stepRelation.setFlowId(stepVo.getFlowId());
            stepRelation.setCurStepId(stepId);
            stepRelation.setOldCurStepId(stepVo.getPstepId());
            flowDesignDao.updateNextStepByPstepId(stepRelation);// 根据父节点设置步骤的下步骤
        }

        // 更新同级步骤相关配置
        if (null != stepVo.getNextStepWay()
                && null != nextStepIds
                && nextStepIds.length > 1
                && ("branch".equals(stepVo.getNextStepWay().trim()) || "parallel"
                .equals(stepVo.getNextStepWay().trim()))) {// 分支、并行
            stepRelation = new SpFlowStepRelation();
            stepRelation.setComId(curUser.getComId());
            stepRelation.setFlowId(stepVo.getFlowId());
            stepRelation.setCurStepId(stepId);
            stepRelation.setStepWay(stepVo.getNextStepWay());
            flowDesignDao.updateChildrenFlowStepInSameStepWay(stepRelation);// 更新同级步骤扭转方式类型
            if ("branch".equals(stepVo.getNextStepWay().trim())
                    && null != stepVo.getDefaultStepId()
                    && stepVo.getDefaultStepId() > 0) {// 更新同级分支默认步骤只能有一个
                stepRelation = new SpFlowStepRelation();
                stepRelation.setComId(curUser.getComId());
                stepRelation.setFlowId(stepVo.getFlowId());
                stepRelation.setCurStepId(stepId);
                stepRelation.setDefaultStep(0);// 先全部设置为0状态
                flowDesignDao
                        .update("update spFlowStepRelation a set a.defaultStep=:defaultStep "
                                        + "where a.comid=:comId and a.flowId=:flowId and a.curStepId=:curStepId",
                                stepRelation);
                stepRelation.setDefaultStep(1);// 再对默认步骤设置1状态
                stepRelation.setNextStepId(stepVo.getDefaultStepId());
                flowDesignDao
                        .update("update spFlowStepRelation a set a.defaultStep=:defaultStep "
                                        + "where a.comid=:comId and a.flowId=:flowId and a.curStepId=:curStepId and a.nextStepId=:nextStepId",
                                stepRelation);
                // flowDesignDao.updateOtherFlowDefaultStepTo0(stepVo);//更新同级默认步骤状态为0
            }
        }
        logsService.addLogs(curUser.getComId(), stepVo.getFlowId(), curUser.getId(), curUser.getUserName(),
                "添加流程步骤：" + stepVo.getStepName(), ConstantInterface.TYPE_WORK_FLOW);
    }

    /**
     * 步骤环形验证
     *
     * @param stepVo
     * @param curUser
     * @return
     */
    public boolean checkStepLoop(SpFlowStep stepVo, UserInfo curUser, Integer stepId) {
        boolean checkState = true;
        //取得当前步骤前所有步骤
        List<SpFlowStepRelation> listFlowParentStep = flowDesignDao.listFlowParentStep(curUser.getComId(), stepVo.getFlowId(), stepId);
        //当前步骤前所有步骤主键
        Set<Integer> pStepIdSet = new HashSet<Integer>();
        //是否有前一步骤
        if (null != listFlowParentStep && !listFlowParentStep.isEmpty()) {
            //遍历当前步骤前所有步骤，缓存主键
            for (SpFlowStepRelation flowStepRelation : listFlowParentStep) {
                pStepIdSet.add(flowStepRelation.getCurStepId());
            }
            //移除当前步骤主键
            pStepIdSet.remove(stepId);
        }
        //下一步骤集合
        Integer[] nextStepIds = stepVo.getNextStepIds();
        if (null != nextStepIds && nextStepIds.length > 0) {
            for (int childStepId : nextStepIds) {// 建立与下步步骤间关系
                if (pStepIdSet.contains(childStepId)) {
                    checkState = false;
                    break;
                }
            }
        }
        return checkState;
    }

    /**
     * 获取流程的其它步骤
     *
     * @param comId  团地主键
     * @param flowId 流程主键
     * @param stepId 比对步骤主键
     * @return
     */
    public List<SpFlowStep> listOtherFlowStep(Integer comId, Integer flowId,
                                              Integer stepId) {
        return flowDesignDao.listOtherFlowStep(comId, flowId, stepId);
    }

    /**
     * 根据步骤主键获取所有的后代步骤数据集
     *
     * @param comId  团地主键
     * @param flowId 流程主键
     * @param stepId 步骤主键
     * @return
     */
    public List<SpFlowStep> listChildrenFlowStepByStepId(Integer comId, Integer flowId, Integer stepId) {
        return flowDesignDao.listChildrenFlowStepByStepId(comId, flowId, stepId);
    }

    /**
     * 获取流程步骤下步步骤集合
     *
     * @param comId  团队主键
     * @param flowId 流程主键
     * @param stepId 流程步骤主键
     * @return
     */
    public List<SpFlowStep> listNextFlowStep(Integer comId, Integer flowId, Integer stepId) {
        return flowDesignDao.listNextFlowStep(comId, flowId, stepId);
    }

    /**
     * 获取流程步骤父步骤集合
     *
     * @param comId  团队主键
     * @param flowId 流程主键
     * @param stepId 流程步骤主键
     * @return
     */
    public List<SpFlowStep> listParentFlowStep(Integer comId, Integer flowId,
                                               Integer stepId) {
        return flowDesignDao.listParentFlowStep(comId, flowId, stepId);
    }

    /**
     * 获取流程步骤信息
     *
     * @param comId  团队主键
     * @param flowId 流程主键
     * @param stepId 流程步骤主键
     * @return
     */
    public SpFlowStep queryFlowStep(Integer comId, Integer flowId,
                                    Integer stepId) {
        SpFlowStep stepVo = flowDesignDao.querFlowStep(comId, flowId, stepId);// 获取流程步骤信息
        return stepVo;
    }

    /**
     * 获取流程步骤所有的配置信息
     *
     * @param comId  团队主键
     * @param flowId 流程主键
     * @param stepId 步骤主键
     * @return
     */
    public SpFlowStep queryFlowStepAllInfo(Integer comId, Integer flowId,
                                           Integer stepId) {
        SpFlowStep stepVo = this.queryFlowStep(comId, flowId, stepId);
        if (null == stepVo){
        	return null;
        }
        List<SpFlowStep> listNextFlowStep = this.listNextFlowStep(comId,
                flowId, stepId);// 获取步骤下步集合
        stepVo.setListNextStep(listNextFlowStep);
        List<UserInfo> listExecutor = this.listExecutorOfFlowStep(comId,
                flowId, stepId);// 获取步骤审批人数据集
        stepVo.setListExecutor(listExecutor);

        List<SpStepFormControl> listFormCompon = this.listFormCompon(comId,
                flowId, stepId);// 获取步骤授权表单控件数据集

        //用于将存入数据库的子表单的子组件替换为子表单组件
        //整理后的步骤权限集合
        List<SpStepFormControl> withSubForms = new ArrayList<SpStepFormControl>();
        if (null != listFormCompon && !listFormCompon.isEmpty()) {
            //查询流程步骤关联的所有组件
            List<FormCompon> formCompons = formService.listSetpCompon(comId, stepVo.getFlowId());

            //记录组件主键和组件类型
            Map<Integer, String> comPonKeyType = new HashMap<Integer, String>();
            //记录组件的父类主键
            Map<Integer, Integer> parent = new HashMap<Integer, Integer>();

            //需要移除的子类组件标识集合
            List<Integer> removeSubForms = new ArrayList<Integer>();

            //本次需要添加的子表单标识
            Map<Integer, SpStepFormControl> addSubForms = new HashMap<Integer, SpStepFormControl>();
            if (null != formCompons && !formCompons.isEmpty()) {
                for (FormCompon formCompon : formCompons) {
                    //组件类型
                    String componentKey = formCompon.getComponentKey();
                    //组件主键
                    Integer componeId = formCompon.getId();
                    //存放组件主键--组件类型
                    comPonKeyType.put(componeId, componentKey);

                    //组件的父类主键
                    Integer parentId = formCompon.getParentId();
                    //存放组件标识---父类主键
                    parent.put(formCompon.getFieldId(), parentId);
                    if ("DataTable".equals(componentKey)) {//若是子表单
                        //实例化步骤控制对象
                        SpStepFormControl spStepFormControl = new SpStepFormControl();
                        //设置团队号
                        spStepFormControl.setComId(comId);
                        //甚至步骤主键
                        spStepFormControl.setStepId(stepId);
                        //设置流程流程主键
                        spStepFormControl.setFlowId(flowId);
                        //设置子表单的主键标识
                        spStepFormControl.setFormControlKey(formCompon.getFieldId().toString());

                        //记录可能添加的子表单步骤控制对象
                        addSubForms.put(componeId, spStepFormControl);
                    } else if (comPonKeyType.containsKey(parentId)
                            && "DataTable".equals(comPonKeyType.get(parentId))) {//是子表单的子组件
                        //记录可能替换的子主键
                        removeSubForms.add(formCompon.getFieldId());
                    }
                }
                //记录已经替换的子表单
                Set<Integer> hasAddSubForm = new HashSet<Integer>();
                for (SpStepFormControl spStepFormControl : listFormCompon) {
                    //步骤主键标识
                    Integer formControlKey = Integer.parseInt(spStepFormControl.getFormControlKey().split(";")[0]);
                    //步骤主键的父类主键
                    Integer parentId = parent.get(formControlKey);
                    if (comPonKeyType.containsKey(parentId) //有记录
                            && "DataTable".equals(comPonKeyType.get(parentId))//是子表单
                            && !hasAddSubForm.contains(parentId)) {//没有被替换过
                        //设置该子表单已被替换
                        hasAddSubForm.add(parentId);
                        //添加子表单组件
                        withSubForms.add(addSubForms.get(parentId));
                    }
                    if (!removeSubForms.contains(formControlKey)) {//不是待移除的子表单组件，则添加组件信息
                        withSubForms.add(spStepFormControl);
                    }

                }
            }
        }

        stepVo.setListFormCompon(withSubForms);
        return stepVo;
    }

    /**
     * 删除流程步骤
     *
     * @param curUser 当前操作人
     * @param flowId  流程主键
     * @param stepId  流程步骤主键
     */
    public void deleteFlowStep(UserInfo curUser, Integer flowId, Integer stepId) {
        SpFlowStep stepVo = this.queryFlowStep(curUser.getComId(), flowId,
                stepId);// 获取流程步骤信息
        logsService.addLogs(curUser.getComId(), flowId, curUser.getId(), curUser.getUserName(), "删除流程步骤：" + stepVo.getStepName(), ConstantInterface.TYPE_WORK_FLOW);
        if (null == stepVo){
        	return;// 如果步骤对象为空，直接退出
        }
        // 1、更改步骤上下级关系
        List<SpFlowStep> listP = this.listParentFlowStep(curUser.getComId(),
                flowId, stepId);// 父级步骤集合
        List<SpFlowStep> listC = this.listNextFlowStep(curUser.getComId(),
                flowId, stepId);// 下步步骤集合
        if (null != listP && !listP.isEmpty() && null != listC
                && !listC.isEmpty()) {
            SpFlowStepRelation stepRelation = null;
            for (int i = 0; i < listP.size(); i++) {
                for (SpFlowStep cStep : listC) {
                    stepRelation = flowDesignDao.querySpFlowStepRelation(curUser.getComId(), flowId, listP.get(i).getId(), cStep.getId());
                    if (null == stepRelation) {// 关系不存在时建立关系
                        stepRelation = new SpFlowStepRelation();
                        stepRelation.setComId(curUser.getComId());
                        stepRelation.setFlowId(stepVo.getFlowId());
                        stepRelation.setCurStepId(listP.get(i).getId());
                        stepRelation.setNextStepId(cStep.getId());
                        flowDesignDao.add(stepRelation);
                    }
                }
            }
        }
        // 2、步骤表中删除步骤信息
        flowDesignDao.delByField("spStepConditions", new String[]{"comId",
                "flowId", "stepId"}, new Object[]{curUser.getComId(),
                flowId, stepId});// 删除流程步骤条件
        flowDesignDao.delByField("spStepFormControl", new String[]{"comId",
                "flowId", "stepId"}, new Object[]{curUser.getComId(),
                flowId, stepId});// 删除流程步骤授权
        flowDesignDao.delByField("spFlowStepExecutor", new String[]{"comId",
                "flowId", "stepId"}, new Object[]{curUser.getComId(),
                flowId, stepId});// 删除流程步骤审批人
        flowDesignDao.delByField("spFlowStepRelation", new String[]{"comId",
                "flowId", "nextStepId"}, new Object[]{curUser.getComId(),
                flowId, stepId});// 删除流程步骤关系
        flowDesignDao.delByField("spFlowStepRelation", new String[]{"comId",
                "flowId", "curStepId"}, new Object[]{curUser.getComId(),
                flowId, stepId});// 删除流程步骤关系
        flowDesignDao.delByField("spFlowStep", new String[]{"comId",
                "flowId", "id"}, new Object[]{curUser.getComId(), flowId,
                stepId});// 删除流程步骤
    }

    /**
     * 更新流程属性
     *
     * @param userInfo  当前操作人信息
     * @param attrType  更新属性类型
     * @param flowModel
     */
    public void updateFlowAttr(UserInfo userInfo, String attrType,
                               SpFlowModel flowModel) {
        flowModel.setComId(userInfo.getComId());
        flowModel.setDeployed(0);//流程是否部署(0未部署)
        if ("flowName".equals(attrType)) {
            // 更新流程名称
            flowDesignDao.update("update spFlowModel a set a.flowName=:flowName where a.comid=:comId and a.id=:id",
                    flowModel);
            //需重新部署流程
            flowDesignDao.update("update spFlowModel a set a.deployed=:deployed where a.comid=:comId and a.id=:id", flowModel);
            logsService.addLogs(userInfo.getComId(), flowModel.getId(), userInfo.getId(), userInfo.getUserName(), "将流程名称更改为：" + flowModel.getFlowName(), ConstantInterface.TYPE_WORK_FLOW);
        } else if ("titleRule".equals(attrType)) {
            // 流程命名规则
            flowDesignDao.update("update spFlowModel a set a.titleRule=:titleRule where a.comid=:comId and a.id=:id", flowModel);
            logsService.addLogs(userInfo.getComId(), flowModel.getId(), userInfo.getId(), userInfo.getUserName(), "流程命名规则为：" + flowModel.getTitleRule(), ConstantInterface.TYPE_WORK_FLOW);
        } else if ("remark".equals(attrType)) {
            // 更新流程备注
            flowDesignDao
                    .update("update spFlowModel a set a.remark=:remark where a.comid=:comId and a.id=:id",
                            flowModel);
            logsService.addLogs(userInfo.getComId(), flowModel.getId(), userInfo.getId(), userInfo.getUserName(), "更新流程说明为：" + flowModel.getRemark().substring(15), ConstantInterface.TYPE_WORK_FLOW);
        } else if ("formKey".equals(attrType)) {
            // 更新流程关联表单
            flowDesignDao
                    .update("update spFlowModel a set a.formKey=:formKey where a.comid=:comId and a.id=:id",
                            flowModel);
            flowDesignDao.delByField("spStepFormControl", new String[]{
                    "comId", "flowId"}, new Object[]{userInfo.getComId(),
                    flowModel.getId()});// 删除流程步骤表单授权
            //需重新部署流程
            flowDesignDao.update("update spFlowModel a set a.deployed=:deployed where a.comid=:comId and a.id=:id", flowModel);
            logsService.addLogs(userInfo.getComId(), flowModel.getId(), userInfo.getId(), userInfo.getUserName(),
                    "更改流程关联表单", ConstantInterface.TYPE_WORK_FLOW);
        } else if ("status".equals(attrType)) {
            // 更新流程备注
            flowDesignDao
                    .update("update spFlowModel a set a.status=:status where a.comid=:comId and a.id=:id",
                            flowModel);
            logsService.addLogs(userInfo.getComId(), flowModel.getId(), userInfo.getId(), userInfo.getUserName(),
                    "更改流程状态", ConstantInterface.TYPE_WORK_FLOW);
        } else if ("spFlowType".equals(attrType)) {
            // 更新流程分类
            flowDesignDao.update("update spFlowModel a set a.spFlowTypeId=:spFlowTypeId where a.comid=:comId and a.id=:id",
                    flowModel);
            if(null != flowModel.getSpFlowTypeId()
            		&& flowModel.getSpFlowTypeId()>0){
            	SpFlowType spFlowType = (SpFlowType) flowDesignDao.objectQuery(SpFlowType.class, flowModel.getSpFlowTypeId());
            	logsService.addLogs(userInfo.getComId(), flowModel.getId(), userInfo.getId(), userInfo.getUserName(),
            			"更改流程分类为:" + spFlowType.getTypeName(), ConstantInterface.TYPE_WORK_FLOW);
            }else{
            	logsService.addLogs(userInfo.getComId(), flowModel.getId(), userInfo.getId(), userInfo.getUserName(),
            			"更改流程分类为:无类别", ConstantInterface.TYPE_WORK_FLOW);
            }
        } else if ("scopeByDep".equals(attrType)) {//更新根据部门控制流程范发起权限
            flowDesignDao.delByField("spFlowScopeByDep", new String[]{"comId",
                    "flowId"}, new Object[]{userInfo.getComId(), flowModel.getId()});// 删除部门控制流程范发起权限
            if (null != flowModel.getDepIds() && flowModel.getDepIds().length > 0) {
                for (Integer depId : flowModel.getDepIds()) {
                    SpFlowScopeByDep spFlowScopeByDep = new SpFlowScopeByDep();
                    spFlowScopeByDep.setComId(userInfo.getComId());
                    spFlowScopeByDep.setFlowId(flowModel.getId());
                    spFlowScopeByDep.setDepId(depId);
                    flowDesignDao.add(spFlowScopeByDep);
                }
            }
            logsService.addLogs(userInfo.getComId(), flowModel.getId(), userInfo.getId(), userInfo.getUserName(),
                    "更改流程部门范围", ConstantInterface.TYPE_WORK_FLOW);
        } else if ("scopeByUser".equals(attrType)) {//更新根据人员控制流程范发起权限
            flowDesignDao.delByField("spFlowScopeByUser", new String[]{"comId",
                    "flowId"}, new Object[]{userInfo.getComId(), flowModel.getId()});// 删除人员控制流程范发起权限
            if (null != flowModel.getUserIds() && flowModel.getUserIds().length > 0) {
                for (Integer userId : flowModel.getUserIds()) {
                    SpFlowScopeByUser spFlowScopeByUser = new SpFlowScopeByUser();
                    spFlowScopeByUser.setComId(userInfo.getComId());
                    spFlowScopeByUser.setFlowId(flowModel.getId());
                    spFlowScopeByUser.setUserId(userId);
                    flowDesignDao.add(spFlowScopeByUser);
                }
            }
            logsService.addLogs(userInfo.getComId(), flowModel.getId(), userInfo.getId(), userInfo.getUserName(),
                    "更改流程人员范围", ConstantInterface.TYPE_WORK_FLOW);
        }

    }

    /**
     * 获取步骤授权表单控件数据集
     *
     * @param comId  团队主键
     * @param flowId 流程主键
     * @param stepId 步骤主键
     * @return
     */
    public List<SpStepFormControl> listFormCompon(Integer comId,
                                                  Integer flowId, Integer stepId) {
        return flowDesignDao.listFormCompon(comId, flowId, stepId);
    }

    /**
     * 获取步骤审批人数据集
     *
     * @param comId  团队主键
     * @param flowId 流程主键
     * @param stepId 步骤主键
     * @return
     */
    public List<UserInfo> listExecutorOfFlowStep(Integer comId, Integer flowId,
                                                 Integer stepId) {
        return flowDesignDao.listExecutorOfFlowStep(comId, flowId, stepId);
    }


    /**
     * 更新步骤配置信息
     *
     * @param stepVo  步骤配置信息
     * @param curUser 当前操作人
     */
    public Map<String, Object> updateFlowStep(SpFlowStep stepVo, UserInfo curUser) {
        Map<String, Object> map = new HashMap<String, Object>();
        //步骤环形验证
        boolean checkState = this.checkStepLoop(stepVo, curUser, stepVo.getId());
        if (!checkState) {
            map.put("status", "f");
            map.put("info", "更新失败！设置的步骤不能为环形！请重新设置！");
            return map;
        }
        // 更新步骤
        flowDesignDao.update(stepVo);
        logsService.addLogs(curUser.getComId(), stepVo.getFlowId(), curUser.getId(), curUser.getUserName(),
                "更新步骤：" + stepVo.getStepName(), ConstantInterface.TYPE_WORK_FLOW);
        // 先删除步骤审批人配置信息
        flowDesignDao.delByField(
                "spFlowStepExecutor",
                new String[]{"comId", "flowId", "stepId"},
                new Object[]{curUser.getComId(), stepVo.getFlowId(),
                        stepVo.getId()});// 删除流程步骤审批人
        // 如果executorWay为appointMan指定人方式时；初始化步骤审批人
        if ("appointMan".equals(stepVo.getExecutorWay())) {
            if (null != stepVo.getListExecutor()
                    && !stepVo.getListExecutor().isEmpty()) {
                SpFlowStepExecutor spFlowStepExecutor = null;
                for (UserInfo executor : stepVo.getListExecutor()) {
                    spFlowStepExecutor = new SpFlowStepExecutor();
                    spFlowStepExecutor.setComId(curUser.getComId());
                    spFlowStepExecutor.setFlowId(stepVo.getFlowId());
                    spFlowStepExecutor.setStepId(stepVo.getId());
                    spFlowStepExecutor.setExecutor(executor.getId());
                    flowDesignDao.add(spFlowStepExecutor);
                }
            }
        }
        // 先删除步骤表单授权
        flowDesignDao.delByField(
                "spStepFormControl",
                new String[]{"comId", "flowId", "stepId"},
                new Object[]{curUser.getComId(), stepVo.getFlowId(),
                        stepVo.getId()});// 删除流程步骤表单授权
        // 步骤表单授权
        if (null != stepVo.getFormComponIds()
                && stepVo.getFormComponIds().length > 0) {

            //需要整理是否有子表单，若有子表单需要删除子表单父类的组件标识，同时处理子表单的子元素组件
            //先将已有的组件标识集合化
            List<String> pre = Arrays.asList(stepVo.getFormComponIds());
            //处理后的组件标识集合
            List<String> listComponeFieldId = new ArrayList<String>();
            //先添加已有的组件标识
            listComponeFieldId.addAll(pre);

            SpStepFormControl sfc = null;
            for (String colKey : listComponeFieldId) {
                sfc = new SpStepFormControl();
                sfc.setComId(curUser.getComId());
                sfc.setFlowId(stepVo.getFlowId());
                sfc.setFormControlKey(colKey.split(";")[0]);
                sfc.setIsFill(Integer.parseInt(colKey.split(";").length < 2 ? "-1" : colKey.split(";")[1]));
                sfc.setStepId(stepVo.getId());
                flowDesignDao.add(sfc);
            }
        }

        SpFlowStepRelation stepRelation = null;
        // 先删除当前步骤的下步步骤关系
        flowDesignDao.delByField("spFlowStepRelation", new String[]{"comId",
                "flowId", "curStepId"}, new Object[]{curUser.getComId(),
                stepVo.getFlowId(), stepVo.getId()});// 删除流程步骤关系
        Integer[] nextStepIds = stepVo.getNextStepIds();
        if (null != nextStepIds) {
            for (int childStepId : nextStepIds) {// 建立与下步步骤间关系
                stepRelation = new SpFlowStepRelation();
                stepRelation.setComId(curUser.getComId());
                stepRelation.setFlowId(stepVo.getFlowId());
                stepRelation.setCurStepId(stepVo.getId());
                stepRelation.setNextStepId(childStepId);
                flowDesignDao.add(stepRelation);
            }
        }

        // 更新同级步骤相关配置
        if (null != stepVo.getNextStepWay()
                && null != nextStepIds
                && nextStepIds.length > 1
                && ("branch".equals(stepVo.getNextStepWay().trim()) || "parallel"
                .equals(stepVo.getNextStepWay().trim()))) {// 分支、并行
            stepRelation = new SpFlowStepRelation();
            stepRelation.setComId(curUser.getComId());
            stepRelation.setFlowId(stepVo.getFlowId());
            stepRelation.setCurStepId(stepVo.getId());
            stepRelation.setStepWay(stepVo.getNextStepWay());
            flowDesignDao.updateChildrenFlowStepInSameStepWay(stepRelation);// 更新同级步骤扭转方式类型
            if ("branch".equals(stepVo.getNextStepWay().trim())
                    && null != stepVo.getDefaultStepId()
                    && stepVo.getDefaultStepId() > 0) {// 更新同级分支默认步骤只能有一个
                stepRelation = new SpFlowStepRelation();
                stepRelation.setComId(curUser.getComId());
                stepRelation.setFlowId(stepVo.getFlowId());
                stepRelation.setCurStepId(stepVo.getId());
                stepRelation.setDefaultStep(0);// 先全部设置为0状态
                flowDesignDao
                        .update("update spFlowStepRelation a set a.defaultStep=:defaultStep "
                                        + "where a.comid=:comId and a.flowId=:flowId and a.curStepId=:curStepId",
                                stepRelation);
                stepRelation.setDefaultStep(1);// 再对默认步骤设置1状态
                stepRelation.setNextStepId(stepVo.getDefaultStepId());
                flowDesignDao
                        .update("update spFlowStepRelation a set a.defaultStep=:defaultStep "
                                        + "where a.comid=:comId and a.flowId=:flowId and a.curStepId=:curStepId and a.nextStepId=:nextStepId",
                                stepRelation);
                // flowDesignDao.updateOtherFlowDefaultStepTo0(stepVo);//更新同级默认步骤状态为0
            }
        }

        SpFlowModel flowModel = new SpFlowModel();
        flowModel.setComId(curUser.getComId());
        flowModel.setId(stepVo.getFlowId());
        //需重新部署流程
        flowModel.setDeployed(0);//流程是否部署(0未部署)
        flowDesignDao
                .update("update spFlowModel a set a.deployed=:deployed where a.comid=:comId and a.id=:id",
                        flowModel);

        map.put("status", "y");
        return map;

    }

    /**
     * 获取流程表单控件集合
     *
     * @param curUser 当前操作人信息
     * @param flowId  流程主键
     * @return
     */
    public List<FormCompon> listAllFormCompont(UserInfo curUser, Integer flowId) {
        SpFlowModel flowModel = this.querySpFlowModel(curUser, flowId);// 基本信息
        if (null == flowModel.getFormKey()
                || "".equals(flowModel.getFormKey().trim())){
        	return null;
        }
        return formService.listAllFormCompont(curUser.getComId(),
                Integer.parseInt(flowModel.getFormKey()), null, null, null);// 获取表单控件
    }

    /**
     * 添加步骤条件
     *
     * @param stepVo  步骤配置信息
     * @param curUser 当前操作人信息
     */
    public void addFlowStepConditions(SpFlowStep stepVo, UserInfo curUser) {
        stepVo.setComId(curUser.getComId());
        flowDesignDao.delByField(
                "spStepConditions",
                new String[]{"comId", "flowId", "stepId"},
                new Object[]{curUser.getComId(), stepVo.getFlowId(),
                        stepVo.getId()});// 删除流程步骤条件
        flowDesignDao
                .update("update spFlowStep a set a.conditionExp=:conditionExp where a.comid=:comId and a.flowId=:flowId and a.id=:id",
                        stepVo);
        if (null != stepVo.getListSpStepCondition() && !stepVo.getListSpStepCondition().isEmpty()) {
            for (SpStepConditions spStepCondition : stepVo.getListSpStepCondition()) {
                spStepCondition.setComId(curUser.getComId());
                spStepCondition.setFlowId(stepVo.getFlowId());
                spStepCondition.setStepId(stepVo.getId());
                flowDesignDao.add(spStepCondition);
            }
        } else {
            stepVo.setConditionExp(null);
            flowDesignDao.update("update spFlowStep a set a.conditionExp=:conditionExp where a.comid=:comId and a.flowId=:flowId and a.id=:id",
                    stepVo);
        }
        SpFlowModel flowModel = new SpFlowModel();
        flowModel.setComId(curUser.getComId());
        flowModel.setId(stepVo.getFlowId());
        //需重新部署流程
        flowModel.setDeployed(0);//流程是否部署(0未部署)
        flowDesignDao.update("update spFlowModel a set a.deployed=:deployed where a.comid=:comId and a.id=:id",
                flowModel);
        
        SpFlowStep spFlowStep = (SpFlowStep) flowDesignDao.objectQuery(SpFlowStep.class, stepVo.getId());
        logsService.addLogs(curUser.getComId(), stepVo.getFlowId(), curUser.getId(), curUser.getUserName(),
        		"步骤:"+spFlowStep.getStepName() + "，设置条件", ConstantInterface.TYPE_WORK_FLOW);
    }

    /**
     * 获取表单条件数据集
     *
     * @param curUser
     * @param flowId  流程主键
     * @param stepId  流程步骤主键
     * @return
     */
    public List<SpStepConditions> listFlowStepConditions(UserInfo curUser,
                                                         Integer flowId, Integer stepId) {
        SpFlowModel spFlowModel = flowDesignDao.querySpFlowModel(curUser,
                flowId);
        if (null == spFlowModel.getFormKey()
                || "".equals(spFlowModel.getFormKey().trim())){
        	return null;
        }
        FormLayout formLayout = formService.getFormLayoutByModId(
                Integer.parseInt(spFlowModel.getFormKey()), curUser);
        return flowDesignDao.listFlowStepConditions(curUser.getComId(), flowId,
                stepId, formLayout.getId());
    }

    /**
     * 流程部署
     *
     * @param curUser 当前操作人
     * @param flowId  部署流程主键
     */
    public void initSpFlowDeploy(UserInfo curUser, Integer flowId) {
        // 1、获取流程步骤以及步骤配置参数
        SpFlowModel flowModel = this.querySpFlowModelForDeploy(curUser, flowId);
        // 2、构建BPMN文件
        String processKey = "flow_" + curUser.getComId() + "_" + flowModel.getId();
        String flowBpmnXml = toBpmnXml(curUser, flowModel.getId(), flowModel.getFlowName(), processKey, flowModel.getListFlowSteps());
        // 3、部署流程
        cusActivitService.createDeployment(flowModel.getFlowName(), flowModel.getFlowName() + ".bpmn", flowBpmnXml);
        // 4、更新流程模型中的act_deployment_id关联字段
        flowModel.setAct_deployment_id(processKey);
        flowModel.setDeployed(1);
        // 更新流程名称
        flowDesignDao
                .update("update spFlowModel a set a.act_deployment_id=:act_deployment_id,a.deployed=:deployed where a.comid=:comId and a.id=:id",
                        flowModel);


    }

    /**
     * 部署用户自定义的审批流程
     *
     * @param curUser    当前操作人
     * @param spUser     审批步骤审批人配置数组
     * @param instanceId 流程实例主键
     * @return
     */
    public String initSpFlowDeployByUserDefined(UserInfo curUser, Integer[] spUser, Integer instanceId) {
        // 1、获取流程步骤以及步骤配置参数
        // 2、构建BPMN文件
        String processKey = "flow_" + curUser.getComId() + "_" + curUser.getId() + "_" + DateTimeUtil.getNowDateStr(DateTimeUtil.yyyyMMddHHmmss);
        String flowName = curUser.getUserName() + "的自定义审批（" + DateTimeUtil.getNowDateStr(DateTimeUtil.yyyy_MM_dd) + "）";
        List<SpFlowStep> listFlowSteps = new ArrayList<SpFlowStep>();
        SpFlowStep step = null;
        Integer stepId = 1;//申明默认审批步骤主键
        //1、开始步骤配置
        step = new SpFlowStep();
        step.setId(stepId);
        step.setStepName("开始");
        step.setStepType("start");
        listFlowSteps.add(step);
        stepId++;//步骤主键自增
        //2、审批步骤配置
        if (null != spUser && spUser.length > 0) {
            for (Integer userId : spUser) {
                step = new SpFlowStep();
                step.setId(stepId);
                UserInfo selectedUser = userInfoService.getUserBaseInfo(curUser.getComId(), userId);
                step.setStepName(selectedUser.getUserName());//设置步骤名称
                step.setStepType("exe");
                step.setExecutorWay(ConstantInterface.EXECUTOR_BY_APPOINT);
                //步骤审批人设置
                List<UserInfo> listExecutor = new ArrayList<UserInfo>();
                UserInfo user = new UserInfo();
                user.setId(userId);
                listExecutor.add(user);
                step.setListExecutor(listExecutor);
                // 流程下一步步骤集合
                List<SpFlowStep> listNextStep = new ArrayList<SpFlowStep>();
                listNextStep.add(step);//当当前步骤放进步骤关系集合中
                listFlowSteps.get((listFlowSteps.size() - 1)).setListNextStep(listNextStep);//建立当前步骤与上一步骤间的步骤关系
                stepId++;//步骤主键自增
                listFlowSteps.add(step);
            }
        }
        //3、结束步骤配置
        step = new SpFlowStep();
        step.setId(stepId);
        step.setStepName("结束");
        step.setStepType("end");
        // 流程下一步步骤集合
        List<SpFlowStep> listNextStep = new ArrayList<SpFlowStep>();
        listNextStep.add(step);//当当前步骤放进步骤关系集合中
        listFlowSteps.get((listFlowSteps.size() - 1)).setListNextStep(listNextStep);//建立当前步骤与上一步骤间的步骤关系
        listFlowSteps.add(step);
        //生成部署配置文件
        String flowBpmnXml = toBpmnXml(curUser, null, flowName, processKey, listFlowSteps);
        // 3、部署流程
        cusActivitService.createDeployment(flowName, flowName + ".bpmn", flowBpmnXml);
        //记录自定义审批流程实例步骤
        SpFlowHiStep spFlowHiStep = new SpFlowHiStep();
        spFlowHiStep.setComId(curUser.getComId());
        spFlowHiStep.setBusId(instanceId);
        spFlowHiStep.setBusType(ConstantInterface.TYPE_FLOW_SP);
        for (SpFlowStep spFlowStep : listFlowSteps) {
            spFlowHiStep.setStepId(spFlowStep.getId());
            spFlowHiStep.setStepName(spFlowStep.getStepName());
            spFlowHiStep.setStepType(spFlowStep.getStepType());
            spFlowHiStep.setSpCheckCfg(ConstantInterface.SPSTEP_CHECK_NO);
            flowDesignDao.add(spFlowHiStep);
        }
        return processKey;
    }

    /**
     * 更具流程配置生成BPMN文件
     *
     * @param curUser     当前操作人信息
     * @param flowId      流程主键
     * @param processName 流程名称
     * @param processKey  部署流程KEY
     * @param stepList    步骤集合
     * @return
     */
    private String toBpmnXml(UserInfo curUser, Integer flowId, String processName,
                             String processKey, List<SpFlowStep> stepList) {
        if (null == stepList || stepList.size() == 0 || null == processKey
                || "".equals(processKey.trim()) || null == processName
                || "".equals(processName.trim())) {
            return null;
        }
        StringBuffer xml = new StringBuffer();
        // 文件声明
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n");
        // 根节点以及标准申明
        xml.append("<definitions xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" \n");
        xml.append(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n");
        xml.append(" xmlns:activiti=\"http://activiti.org/bpmn\" \n");
        xml.append(" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" \n");
        xml.append(" xmlns:omgdc=\"http://www.omg.org/spec/DD/20100524/DC\" \n");
        xml.append(" xmlns:omgdi=\"http://www.omg.org/spec/DD/20100524/DI\" \n");
        xml.append(" typeLanguage=\"http://www.w3.org/2001/XMLSchema\" \n");
        xml.append(" expressionLanguage=\"http://www.w3.org/1999/XPath\" \n");
        xml.append(" targetNamespace=\"http://www.signavio.com/bpmn20\"> \n");
        // 流程步骤定义
        xml.append("<process id=\"" + processKey + "\" name=\"" + processName
                + "\"> \n");
        //默认开始事件
        xml.append("<startEvent id=\"step_start\" name=\"开始\"></startEvent> \n");
        SpFlowStep step = null;
        for (int i = 0; i < stepList.size(); i++) {
            step = stepList.get(i);
            // 流程节点定义************************开始*************************************************
            if (step.getStepType().equals("start")) {
                //把开始事件改成开始任务事件
                xml.append("<userTask id=\"step_" + step.getId() + "\" name=\"开始\" ");
                //创建人自己
                xml.append(" activiti:assignee=\"#{var_" + ConstantInterface.EXECUTOR_BY_SELF + "}\" ");
                // 普通节点
                xml.append("></userTask> \n");
                //与开始事件建立连线关系
                xml.append("<sequenceFlow id=\"step_start_To_step_" + step.getId() + "\" sourceRef=\"step_start\" targetRef=\"step_" + step.getId() + "\"></sequenceFlow> \n");
            } else if (step.getStepType().equals("end")) {
                // 结束节点
                xml.append("<endEvent id=\"step_" + step.getId()
                        + "\"  name=\"结束\"></endEvent>\n");
            } else {
                // 执行节点
                xml.append("<userTask id=\"step_" + step.getId() + "\" name=\"" + step.getStepName() + "\" ");
                if (null != step.getExecutorWay()
                        && !"".equals(step.getExecutorWay().trim())) {
                    if (ConstantInterface.EXECUTOR_BY_APPOINT.equals(step.getExecutorWay())) {//手动指定
                        // 节点办理人集合
                        List<UserInfo> listExecutorOfFlowStep = null;
                        if (null == flowId) {//自定义审批流程步骤审批人
                            listExecutorOfFlowStep = step.getListExecutor();
                        } else {//固定流程
                            listExecutorOfFlowStep = flowDesignDao
                                    .listExecutorOfFlowStep(curUser.getComId(), flowId, step.getId());
                        }
                        if (null != listExecutorOfFlowStep && !listExecutorOfFlowStep.isEmpty()) {//配置时指定了办理人的
                            if (listExecutorOfFlowStep.size() == 1) {//办理人只有一个时
                                xml.append(" activiti:assignee=\"" + CommonUtil.buildFlowExetutor(curUser.getComId(), listExecutorOfFlowStep.get(0).getId()) + "\" ");
                            } else {//办理人是多个时
                                StringBuffer candidateUsers = new StringBuffer();
                                for (UserInfo user : listExecutorOfFlowStep) {
                                    candidateUsers.append(CommonUtil.buildFlowExetutor(curUser.getComId(), user.getId()) + ",");
                                }
                                candidateUsers = new StringBuffer(candidateUsers.subSequence(0, candidateUsers.length() - 1));
                                xml.append(" activiti:candidateUsers=\"" + candidateUsers + "\" ");
                            }
                        } else {//配置时，没指定办理人的，则使用变量
                            xml.append(" activiti:candidateUsers=\"#{var_" + ConstantInterface.EXECUTOR_BY_APPOINT + "}\" ");
                        }
                    } else if (ConstantInterface.EXECUTOR_BY_DIRECT.equals(step
                            .getExecutorWay())) {//直属上级
                        xml.append(" activiti:assignee=\"#{var_" + ConstantInterface.EXECUTOR_BY_DIRECT + "}\" ");
                    } else if (ConstantInterface.EXECUTOR_BY_SELF.equals(step.getExecutorWay())) {//创建人自己
                        xml.append(" activiti:assignee=\"#{var_" + ConstantInterface.EXECUTOR_BY_SELF + "}\" ");
                    }
                }
                // 普通节点
                xml.append("></userTask> \n");
            }
            // 流程节点定义************************结束*************************************************
            // 流程节点连线————————————————————————开始—————————————————————————————————————————————————
            if (!step.getStepType().equals("end")) {
                // 除结束节点外，都有下一步
                List<SpFlowStep> listNextStep = step.getListNextStep();
                if (null != listNextStep && !listNextStep.isEmpty()) {
                    if (listNextStep.size() == 1) {//下一步只有一步
                        xml.append("<sequenceFlow id=\"step_" + step.getId() + "_To_step_" + listNextStep.get(0).getId() + "\" sourceRef=\"step_" + step.getId() + "\" targetRef=\"step_" + listNextStep.get(0).getId() + "\"></sequenceFlow> \n");
                    } else {//下一步有多步骤候选
                        if (SpFlowStepRelation.WAY_BY_BRANCH.equals(listNextStep.get(0).getStepWay())) {//分支
                            String exclusiveGatewayId = "exclusivegateway_" + step.getId();//排它网关ID
                            xml.append("<sequenceFlow id=\"step_" + step.getId() + "_To_" + exclusiveGatewayId + "\" sourceRef=\"step_" + step.getId() + "\" targetRef=\"" + exclusiveGatewayId + "\"></sequenceFlow> \n");
                            Integer defaultStep = null;//排他网关默认步骤
                            for (SpFlowStep nextStep : listNextStep) {
                                xml.append("<sequenceFlow id=\"" + exclusiveGatewayId + "_To_step_" + nextStep.getId() + "\" sourceRef=\"" + exclusiveGatewayId + "\" targetRef=\"step_" + nextStep.getId() + "\"> \n");
                                if (nextStep.getDefaultStep() == 1) {//默认步骤不用设置条件
                                    defaultStep = nextStep.getId();
                                } else {
                                    //获取下步骤条件
                                    String conditionExp = this.queryStepConditions(curUser, flowId, nextStep.getId());
                                    if (null != conditionExp) {
                                        //设置条件
                                        xml.append("<conditionExpression xsi:type=\"tFormalExpression\"> \n <![CDATA[${" + conditionExp + "}]]> \n</conditionExpression> \n");
                                    }
                                }
                                xml.append("</sequenceFlow> \n");
                            }
                            //排它网关
                            xml.append("<exclusiveGateway id=\"" + exclusiveGatewayId + "\" name=\"Exclusive Gateway\" default=\"step_" + defaultStep + "\"></exclusiveGateway>\n");
                        } else if (SpFlowStepRelation.WAY_BY_PARALLEL.equals(listNextStep.get(0).getStepWay())) {//并行
//							for (SpFlowStep nextStep : listNextStep) {
//								xml.append("<sequenceFlow id=\""+step.getId()+"_To_"+nextStep.getId()+"\" sourceRef=\""+step.getId()+"\" targetRef=\""+nextStep.getId()+"\"></sequenceFlow> \n");
//							}
                            loger.info("并行步骤：\"" + step.getStepName() + "\"未被连线。");
                        }

                    }
                }
            }
            // 流程节点连线————————————————————————结束—————————————————————————————————————————————————
        }
        xml.append("</process> \n");
        xml.append("</definitions>");
        return xml.toString();
    }

    /**
     * 获取步骤条件
     *
     * @param curUser 当前操作人信息
     * @param flowId  流程主键
     * @param stepId  步骤主键
     * @return
     */
    private String queryStepConditions(UserInfo curUser, Integer flowId, Integer stepId) {
        SpFlowStep step = this.queryFlowStep(curUser.getComId(), flowId, stepId);
        if (null != step.getConditionExp() && !"".equals(step.getConditionExp())) {
            String conditionExp = step.getConditionExp();
            List<SpStepConditions> listFlowStepConditions = this.listFlowStepConditions(curUser, flowId, stepId);
            if (null != listFlowStepConditions && !listFlowStepConditions.isEmpty()) {
                for (SpStepConditions spStepConditions : listFlowStepConditions) {
                    String conditionValue = spStepConditions.getConditionValue();
                    if (!(CommonUtil.isNumeric(spStepConditions.getConditionValue())
                            && CommonUtil.isFloat(spStepConditions.getConditionValue()))) {
                        conditionValue = "\"" + spStepConditions.getConditionValue() + "\"";
                    }
                    String conditionType = spStepConditions.getConditionType();
                    if ("=".equals(conditionType)) {
                        conditionType = "==";
                    }
                    conditionExp = conditionExp.replace(spStepConditions.getConditionNum(), " var_" + spStepConditions.getConditionVar() + " " + conditionType + " " + conditionValue + " ");
                }
                return conditionExp;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }


    /**
     * 获取可用的流程
     *
     * @param curUser 当前操作人
     * @return
     */
    public List<SpFlowModel> listSpFlowForSp(UserInfo curUser, SpFlowModel spFlowModel) {
        return flowDesignDao.listSpFlowForSp(curUser, spFlowModel);
    }

    /**
     * 获取团队流程分类
     *
     * @param curUser 当前操作人
     * @return
     */
    public List<SpFlowType> listSpFlowType(UserInfo curUser) {
        return flowDesignDao.listSpFlowType(curUser);
    }

    /**
     * 获取流程分类下的所属流程
     *
     * @param curUser 当前操作人
     * @return
     */
    public List<SpFlowType> listSpFlowTypeOfFlowModel(UserInfo curUser) {
        List<SpFlowType> listSpFlowType = this.listSpFlowType(curUser);

        if (null == listSpFlowType) {
            listSpFlowType = new ArrayList<SpFlowType>();
        }
        //存入未分类类型
        SpFlowType noType = new SpFlowType();
        noType.setId(0);
        noType.setTypeName("未分类");
        listSpFlowType.add(noType);
        for (SpFlowType spFlowType : listSpFlowType) {//查询每个类型下的所属流程
            SpFlowModel spFlowModel = new SpFlowModel();
            spFlowModel.setSpFlowTypeId(spFlowType.getId());
            List<SpFlowModel> listSpFlow = flowDesignDao.listSpFlowForSp(curUser, spFlowModel);
            spFlowType.setListSpFlowModel(listSpFlow);
        }
        //存入常用
        List<SpFlowModel> listHourlySpFlow = this.listHourlySpFlow(curUser);
        if (null != listHourlySpFlow && !listHourlySpFlow.isEmpty()) {//有常用表单
            //存入未分类类型
            SpFlowType usedType = new SpFlowType();
            usedType.setId(-1);
            usedType.setTypeName("常用审批");
            usedType.setListSpFlowModel(listHourlySpFlow);
            listSpFlowType.add(0, usedType);

        }
        //当没有可发起的流程时，数据集置为空。
        boolean blnRsuult = listSpFlowType.size() == 1 
        		&& (null == listSpFlowType.get(0) || CommonUtil.isNull(listSpFlowType.get(0).getListSpFlowModel()));
        if (blnRsuult) {
            listSpFlowType = null;
        }
        return listSpFlowType;
    }

    /**
     * 修改流程分类属性
     *
     * @param spFlowType 流程分类属性配置信息
     * @param userInfo   当前操作人
     */
    public void updateSpFlowType(SpFlowType spFlowType, UserInfo userInfo) {
        flowDesignDao.update(spFlowType);
    }

    /**
     * 删除流程分类
     *
     * @param spFlowTypeId 流程分类主键
     * @param userInfo     当前操作人
     * @return
     */
    public boolean delSpFlowType(Integer spFlowTypeId, UserInfo userInfo) {
        boolean flag = false;
        SpFlowType spFlowType = (SpFlowType) flowDesignDao.objectQuery(SpFlowType.class, spFlowTypeId);
        if (spFlowType.getComId().equals(userInfo.getComId())) {
            flowDesignDao.updateSpFlowTypeTo0(spFlowTypeId, userInfo.getComId());//将含有此类型的流程的流程类型变为0
            flowDesignDao.delById(SpFlowType.class, spFlowTypeId);
            flag = true;
        }
        return flag;
    }

    /**
     * 获取流程分类最大排序序号
     *
     * @param comId 团队主键
     * @return
     */
    public Integer querySpFlowTypeOrderMax(Integer comId) {
        SpFlowType spFlowType = flowDesignDao.querySpFlowTypeOrderMax(comId);
        return null == spFlowType.getOrderNo() ? 1 : spFlowType.getOrderNo();
    }

    /**
     * 添加流程分类
     *
     * @param spFlowType
     * @param userInfo
     * @return
     */
    public Integer addSpFlowType(SpFlowType spFlowType, UserInfo userInfo) {
        spFlowType.setComId(userInfo.getComId());
        return flowDesignDao.add(spFlowType);
    }

    /**
     * 获取个人频繁使用的前4流程
     *
     * @param userInfo
     * @return
     */
    public List<SpFlowModel> listHourlySpFlow(UserInfo userInfo) {
        return flowDesignDao.listHourlySpFlow(userInfo);
    }

    /**
     * 流程克隆
     *
     * @param userInfo 当前操作人信息
     * @param flowId   被克隆流程主键
     */
    public void initCloneFlowModel(UserInfo userInfo, Integer flowId) {
        SpFlowModel cloneSpFlowModel = flowDesignDao.querySpFlowModel(userInfo, flowId);
        cloneSpFlowModel.setCreator(userInfo.getId());//重置流程创建人
        cloneSpFlowModel.setAct_deployment_id(null);//重置流程部署关键字
        cloneSpFlowModel.setDeployed(0);//重置是否部署标识符
        cloneSpFlowModel.setFlowName(cloneSpFlowModel.getFlowName() + "(克隆)");
        Integer newFlowId = flowDesignDao.add(cloneSpFlowModel);//克隆流程
        flowDesignDao.cloneSpFlowScopeByDep(userInfo.getComId(), flowId, newFlowId);//克隆通过部门控制流程范发起权限
        flowDesignDao.cloneSpFlowScopeByUser(userInfo.getComId(), flowId, newFlowId);//克隆通过人员控制流程范发起权限
        List<SpFlowStep> listSpFlowStep = flowDesignDao.listFlowAllSteps(userInfo.getComId(), flowId);//获取流程的所有步骤
        if (null != listSpFlowStep && !listSpFlowStep.isEmpty()) {
            Map<Integer, Integer> cloneStepsRelation = new HashMap<Integer, Integer>();//建立新老步骤关系集合
            for (SpFlowStep spFlowStep : listSpFlowStep) {
                spFlowStep.setFlowId(newFlowId);//改变步骤、流程关联关系
                Integer newStepId = flowDesignDao.add(spFlowStep);//克隆流程步骤
                cloneStepsRelation.put(spFlowStep.getId(), newStepId);
                flowDesignDao.cloneSpFlowStepExecutor(userInfo.getComId(), flowId, spFlowStep.getId(), newFlowId, newStepId);//克隆流程步骤执行人
                flowDesignDao.cloneSpStepFormControl(userInfo.getComId(), flowId, spFlowStep.getId(), newFlowId, newStepId);//克隆步骤权限控制
                flowDesignDao.cloneSpStepConditions(userInfo.getComId(), flowId, spFlowStep.getId(), newFlowId, newStepId);//克隆步骤条件描述
            }
            //克隆流程步骤间关系
            if (!cloneStepsRelation.isEmpty()) {
                Set<Integer> oldStepSet = cloneStepsRelation.keySet();
                Iterator<Integer> it = oldStepSet.iterator();
                while (it.hasNext()) {
                    Integer oldStepId = it.next();
                    List<SpFlowStepRelation> listSpFlowStepRelation = flowDesignDao.listSpFlowStepRelation(userInfo.getComId(), flowId, oldStepId);//获取老步骤关系
                    if (null != listSpFlowStepRelation && !listSpFlowStepRelation.isEmpty()) {
                        for (SpFlowStepRelation spFlowStepRelation : listSpFlowStepRelation) {
                            spFlowStepRelation.setFlowId(newFlowId);
                            spFlowStepRelation.setCurStepId(cloneStepsRelation.get(oldStepId));
                            spFlowStepRelation.setNextStepId(cloneStepsRelation.get(spFlowStepRelation.getNextStepId()));
                            flowDesignDao.add(spFlowStepRelation);//克隆流程步骤间关系
                        }
                    }
                }
            }
        }
    }

    /**
     * 取得当前步骤的步骤信息
     *
     * @param userInfo     当前操作人
     * @param actInstaceId activity实例化主键
     * @param instanceId   流程实例化主键
     * @return
     */
    public SpFlowHiStep checkStepCfg(UserInfo userInfo, String actInstaceId, Integer instanceId) {
        //1、流程引擎向下走一步
        Task curTask = cusActivitService.queryCurTaskByTaskAssignee(actInstaceId, userInfo);
        if (null == curTask || null == curTask.getTaskDefinitionKey()) {//如果流程步骤信息为NULL时，直接返回
            return null;
        }
        Integer curStepId = null;//当前步骤主键声明

        String actTaskDefinitionKey = curTask.getTaskDefinitionKey();
        if (actTaskDefinitionKey.contains("_") && CommonUtil.isNumeric(actTaskDefinitionKey.split("_")[1])) {
            curStepId = Integer.parseInt(actTaskDefinitionKey.split("_")[1]);
        }
        if (null == curStepId) {
            return null;
        }
        SpFlowHiStep spFlowHiStep = flowDesignDao.querySpFlowHiStep(userInfo.getComId(), curStepId, instanceId);
        return spFlowHiStep;

    }

    /**
     * 查询流程关联配置信息
     *
     * @param flowId   流程主键
     * @param userInfo 当前操作人信息
     * @return
     */
    public SpFlowModel querySpFlowModelRelevanceCfg(Integer flowId,
                                                    UserInfo userInfo) {
        SpFlowModel spFlowModelRelevanceCfg = flowDesignDao.querySpFlowModelRelevanceCfg(flowId, userInfo.getComId());
        if (null != spFlowModelRelevanceCfg) {
            Integer formKey = null;//表单主键
            Integer layoutId = null;//表单布局主键
            SpFlowModel flowModel = this.querySpFlowModel(userInfo, flowId);//获取流程基本信息
            if (null != flowModel.getFormKey() && !"".equals(flowModel.getFormKey())) {
                formKey = Integer.parseInt(flowModel.getFormKey());//获取关联表单主键
                FormMod formMod = formService.getFormModVersion(formKey, userInfo.getComId());//取得模板信息
                layoutId = formMod.getLayoutId();//表单布局主键
            }
//			List<FormCompon> listFlowFormCompons = flowDesignDao.listFlowFormComponsAndChecked(userInfo.getComId(),flowId,formKey,layoutId);
            List<FormCompon> listFlowFormCompons = formService.listAllFormCompont(userInfo.getComId(), formKey, layoutId);
            spFlowModelRelevanceCfg.setListFlowFormCompons(listFlowFormCompons);
        }
        return spFlowModelRelevanceCfg;
    }

    /**
     * 获取子流程表单空间数据集
     *
     * @param flowId   子流程主键
     * @param userInfo 当前操作人信息
     * @return
     */
    public List<FormCompon> listFlowFormCompons(Integer flowId,
                                                UserInfo userInfo) {
        Integer formKey = null;//表单主键
        Integer layoutId = null;//表单布局主键
        SpFlowModel flowModel = this.querySpFlowModel(userInfo, flowId);//获取流程基本信息
        if (null != flowModel.getFormKey() && !"".equals(flowModel.getFormKey())) {
            formKey = Integer.parseInt(flowModel.getFormKey());//获取关联表单主键
            FormMod formMod = formService.getFormModVersion(formKey, userInfo.getComId());//取得模板信息
            layoutId = formMod.getLayoutId();//表单布局主键
//			List<FormCompon> listFlowFormCompons = flowDesignDao.listFlowFormComponsAndChecked(userInfo.getComId(),flowId,formKey,layoutId);
            List<FormCompon> listFlowFormCompons = formService.listAllFormCompont(userInfo.getComId(), formKey, layoutId);
            return listFlowFormCompons;
        }
        return null;
    }

    /**
     * 检测已经建立的映射关系
     *
     * @param flowId              主流程主键
     * @param userInfo            当前操作人
     * @param listFlowFormCompons 被映射流程表单控件集合
     */
    public void flowComponChecked(Integer flowId, UserInfo userInfo,
                                  List<FormCompon> listFlowFormCompons) {
        List<SpFlowRelevanceCfg> listSpFlowRelevanceCfg = flowDesignDao.listSpFlowModelRelevanceCfg(flowId, userInfo.getComId());//获取已关联的关系
        if (null != listSpFlowRelevanceCfg) {
            Map<String, String> cfgMap = new HashMap<String, String>();//映射关系MAP
            for (SpFlowRelevanceCfg spFlowRelevanceCfg : listSpFlowRelevanceCfg) {
                cfgMap.put(spFlowRelevanceCfg.getToFormControlKey(), spFlowRelevanceCfg.getFromFormControlKey());
            }
            for (FormCompon formCompon : listFlowFormCompons) {
                if (cfgMap.containsKey(formCompon.getFieldId().toString())) {
                    formCompon.setFromFormControlKey(cfgMap.get(formCompon.getFieldId().toString()));
                }
            }
        }
//		return listFlowFormCompons;
    }

    /**
     * 流程关联关系添加
     *
     * @param userInfo           当前操作人信息
     * @param spFlowRelevanceCfg 关联配置关系
     */
    public void initFlowModelRelevance(UserInfo userInfo,
                                       SpFlowRelevanceCfg spFlowRelevanceCfg) {
        //建立主子流程关系
        SpFlowModel spFlowModel = new SpFlowModel();
        spFlowModel.setComId(userInfo.getComId());
        spFlowModel.setSonFlowId(spFlowRelevanceCfg.getSonFlowId());
        spFlowModel.setId(spFlowRelevanceCfg.getPflowId());
        flowDesignDao.update("update spFlowModel a set a.sonFlowId=:sonFlowId where a.comid=:comId and a.id=:id", spFlowModel);

        //清除已经建立映射关系
        flowDesignDao.delByField("spFlowRelevanceCfg", new String[]{"comId",
                "pflowId"}, new Object[]{userInfo.getComId(), spFlowRelevanceCfg.getPflowId()});//删除流程关联关系
        //建立映射关系持久化
        String[] keyValueArray = spFlowRelevanceCfg.getKeyValueArray();
        if (null != keyValueArray && keyValueArray.length > 0) {
            SpFlowRelevanceCfg relevanceCfg = null;
            for (String keyValue : keyValueArray) {
                if (keyValue.contains("&")) {
                    relevanceCfg = new SpFlowRelevanceCfg();
                    relevanceCfg.setComId(userInfo.getComId());
                    relevanceCfg.setPflowId(spFlowRelevanceCfg.getPflowId());
                    relevanceCfg.setFromFormControlKey(keyValue.split("&")[0]);
                    relevanceCfg.setSonFlowId(spFlowRelevanceCfg.getSonFlowId());
                    relevanceCfg.setToFormControlKey(keyValue.split("&")[1]);
                    flowDesignDao.add(relevanceCfg);
                }
            }
        }
    }

    /**
     * 清除流程关联关系
     *
     * @param userInfo 当前操作人
     * @param flowId   流程主键
     */
    public void delFlowModelRelevance(UserInfo userInfo, Integer flowId) {
        //清除子流程关系
        SpFlowModel spFlowModel = new SpFlowModel();
        spFlowModel.setComId(userInfo.getComId());
        spFlowModel.setSonFlowId(null);
        spFlowModel.setId(flowId);
        flowDesignDao.update("update spFlowModel a set a.sonFlowId=:sonFlowId where a.comid=:comId and a.id=:id", spFlowModel);
        //清除已经建立映射关系
        flowDesignDao.delByField("spFlowRelevanceCfg", new String[]{"comId",
                "pflowId"}, new Object[]{userInfo.getComId(), flowId});//删除流程关联关系
    }

    /**
     * 根据模块映射关系获取流程表单控件
     *
     * @param flowId                  流程主键
     * @param userInfo                当前操作人信息
     * @param actionSpFlowRelevanceId 模块关联主键
     * @return
     */
    public List<FormCompon> listFlowFormComponsByBusMapFlowId(Integer flowId, UserInfo userInfo, Integer actionSpFlowRelevanceId) {
        Integer formKey = null;// 表单主键
        Integer layoutId = null;// 表单布局主键
        SpFlowModel flowModel = this.querySpFlowModelByBusMapFlowId(userInfo, actionSpFlowRelevanceId);// 获取流程基本信息
        if (null != flowModel.getFormKey() && !"".equals(flowModel.getFormKey())) {
            formKey = Integer.parseInt(flowModel.getFormKey());// 获取关联表单主键
            FormMod formMod = formService.getFormModVersion(formKey, userInfo.getComId());// 取得模板信息
            layoutId = formMod.getLayoutId();// 表单布局主键
            List<FormCompon> listFlowFormCompons = formService.listAllFormCompont4BusMap(userInfo.getComId(), formKey, layoutId);
            return listFlowFormCompons;
        }
        return null;
    }

    /**
     * 根据模块映射关系获取流程的配置详情
     *
     * @param userInfo
     * @param actionSpFlowRelevanceId 流程主键
     * @return
     */
    public SpFlowModel querySpFlowModelByBusMapFlowId(UserInfo userInfo, Integer actionSpFlowRelevanceId) {
        SpFlowModel spFlowModel = flowDesignDao.querySpFlowModelByBusMapFlowId(userInfo, actionSpFlowRelevanceId);
        if (null != spFlowModel) {
            List<SpFlowStep> listFlowSteps = flowDesignDao.listFlowSteps(userInfo.getComId(), spFlowModel.getId());
            // 去重
            Map<Integer, SpFlowStep> stepMap = new LinkedHashMap<Integer, SpFlowStep>();
            for (SpFlowStep step : listFlowSteps) {
                if (stepMap.containsKey(step.getId())) {
                    if (stepMap.get(step.getId()).getStepLevel() < step.getStepLevel()) {// 如果是相同步骤，则保留层级最高的（level）
                        stepMap.remove(step.getId());
                        stepMap.put(step.getId(), step);
                    }
                } else {
                    stepMap.put(step.getId(), step);
                }
            }
            listFlowSteps = new ArrayList<SpFlowStep>(stepMap.values());
            spFlowModel.setListFlowSteps(listFlowSteps);// 关联流程的步骤
            for (SpFlowStep stepVo : listFlowSteps) {
                if (!stepVo.getStepType().equals("end")) {
                    List<SpFlowStep> listNextStep = flowDesignDao.listNextFlowStep(userInfo.getComId(), spFlowModel.getId(), stepVo.getId());
                    stepVo.setListNextStep(listNextStep);// 关联流程下步骤集合
                }
            }
            // 查询流程根据部门设置的发起权限集合
            List<SpFlowScopeByDep> listSpFlowScopeByDep = flowDesignDao.listSpFlowScopeByDep(userInfo.getComId(), spFlowModel.getId());
            spFlowModel.setListSpFlowScopeByDep(listSpFlowScopeByDep);
            // 查询流程根据人员设置的发起权限集合
            List<SpFlowScopeByUser> listSpFlowScopeByUser = flowDesignDao.listSpFlowScopeByUser(userInfo.getComId(), spFlowModel.getId());
            spFlowModel.setListSpFlowScopeByUser(listSpFlowScopeByUser);
        }
        return spFlowModel;
    }

}
