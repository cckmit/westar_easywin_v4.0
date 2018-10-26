<%@page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" errorPage="/WEB-INF/jsp/error/pageException.jsp"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%><%@page import="com.westar.core.web.InitServlet"%>
<%@page import="com.westar.base.model.UserInfo"%>
<%@page import="com.westar.core.service.MenuService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<body onload="setStyle()">
<form method="post" id="dailyModForm">
    <!-- Page Content -->
    <div class="page-content">
        <!-- Page Body -->
        <div class="page-body">
            <div class="row">
                <div class="col-md-12 col-xs-12 ">
                    <div class="widget">
                        <div class="widget-header ps-titleHeader bordered-bottom bordered-themeprimary">
                            <span class="widget-caption themeprimary" style="font-size: 20px">分享模板设置</span>
                            <div style="font-size: 15px;color: gray;float:left;margin-top: 8px;">-可以根据指定部门/指定人员  两个不同层级，分别设置分享模板</div>
                        </div>
                        <div class="widget-body">
                            <div class="ws-write-content" style="">
                                <%--部门模板--%>
                                <div style="clear:both;padding-top: 20px">
                                    <div style="font-size: 20px;line-height: 25px;color: black">部门模板<span style="font-size: 15px;color: gray;margin-top: 8px;">-设置需要显示条目的部门</span></div>
                                    <div class="groupLev">
                                        <c:choose>
                                            <c:when test="${not empty dailyMod.contentGroupLevs }">
                                                <c:forEach items="${dailyMod.contentGroupLevs}" var="content">
                                                    <div id="team${content.id}" class="groupLevDetail dailyModContent" >
                                                        <c:if test="${content.isRequire eq '1'}">
                                                            <font style="color: red;float: left;line-height: 30px">*</font>
                                                        </c:if>
                                                        <div class="groupLevContent" style="${content.hideState==1?'text-decoration:line-through;color:gray':'text-decoration:none;color:#000000' }">
                                                            <div class="depLevTxt" >
                                                                    ${content.modContent}
                                                            </div>
                                                            <div class="depSelect" style="clear:both;float:left;margin-top: 5px">
                                                                <c:choose>
                                                                    <c:when test="${not empty content.listDeps}">
                                                                        <c:forEach items="${content.listDeps}" var="dep" varStatus="vs">
                                                                            <span class="label label-default margin-right-5 margin-bottom-5" >${dep.depName}</span>
                                                                        </c:forEach>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="label label-default margin-right-5 margin-bottom-5" >所有部门</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </div>
                                                        </div>
                                                            <%--编辑 --%>
                                                        <div class="groupLevUpdate">
                                                            <div>
                                                                <a href="javascript:void(0)" onclick="editGroupContent(${content.id},'${param.sid }')" style="color:#1E88C7;padding-right: 10px">编辑</a>
                                                                <a href="javascript:void(0)" onclick="hideContent('${param.sid }',${content.id},1,this,${dailyMod.id },2)" style="color:#1E88C7;padding-right: 10px">隐藏</a>
                                                                <a href="javascript:void(0)" class="setRequire" isRequire="${content.isRequire eq '1'?'0':'1'}" contentId="${content.id}" modId="${dailyMod.id}"
                                                                   style="color:#1E88C7;padding-right: 10px">${content.isRequire eq '1'?'非必填':'必填'}</a>
                                                                <a href="javascript:void(0)" onclick="delContent('${param.sid }',${content.id},this,${dailyMod.id },2)" style="color:#1E88C7;padding-right: 2px">删除</a>
                                                            </div>
                                                        </div>
                                                            <%--显示--%>
                                                        <div class="groupLevShow">
                                                            <div>
                                                                已隐藏,<a href="javascript:void(0)" onclick="hideContent('${param.sid }',${content.id},0,this,${dailyMod.id },2)" style="color:#1E88C7;padding-right: 10px"> 显示</a>
                                                                <a href="javascript:void(0)" class="setRequire" isRequire="${content.isRequire eq '1'?'0':'1'}" contentId="${content.id}" modId="${dailyMod.id}"
                                                                   style="color:#1E88C7;padding-right: 10px">${content.isRequire eq '1'?'非必填':'必填'}</a>
                                                                <a href="javascript:void(0)" onclick="delContent('${param.sid }',${content.id},this,${dailyMod.id },2)" style="color:#1E88C7;padding-right: 2px">删除</a>
                                                            </div>
                                                        </div>
                                                            <%--修改 --%>
                                                        <div class="groupLevSave">
                                                                <%--存放模板临时条目 --%>
                                                            <div class="tempdep" style="display:none">
                                                                <select	 multiple="multiple" moreselect="true">
                                                                    <c:forEach items="${content.listDeps}" var="dep" varStatus="vs">
                                                                        <option value="${dep.depId}" selected="selected">${dep.depName }</option>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                            <input type="hidden" id="tempGroupCont_${content.id}" name="tempGroupCont" value="${content.modContent}"/>
                                                            <div>
                                                                <a href="javascript:void(0)" onclick="updateGroup('${param.sid }',${content.id},${dailyMod.id},this)" style="color:#1E88C7;padding-right: 10px;">保存</a>
                                                                <a href="javascript:void(0)" onclick="cancleEditGroup(${content.id})" style="color:#1E88C7;padding-right: 10px;">取消</a>
                                                                <a href="javascript:void(0)" class="setRequire" isRequire="${content.isRequire eq '1'?'0':'1'}" contentId="${content.id}" modId="${dailyMod.id}"
                                                                   style="color:#1E88C7;padding-right: 10px">${content.isRequire eq '1'?'非必填':'必填'}</a>
                                                                <a href="javascript:void(0)" onclick="delContent('${param.sid }',${content.id},this,${dailyMod.id },2)" style="color:#1E88C7;padding-right: 2px">删除</a>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="noGroupLev" id="noGroupLev">
                                                    您还没有设置部门模板条目
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <%--预留用于动态添加的 --%>
                                    <div class="addGroupLev" style="display:none;">
                                        <div class="groupContent">
                                            <div class="depBody">
                                                <input type="text" id="groupContent" class="form-control small"  name="groupContent" style="width: 95%"/>
                                            </div>
                                            <div class="selectDep">
                                                <div style="float: left; width: 250px;display: none">
                                                    <select list="listDeps" listkey="depId" listvalue="depName" id="listDeps_depId" name="listDeps.depId" ondblclick="removeClick(this.id)" multiple="multiple" moreselect="true" style="width: 100%; height: 100px;">
                                                    </select>
                                                </div>
                                                <div id="depMorelistDeps_depId" class="depMorelistRemove"></div>
                                                <a title="部门选择" class="btn btn-blue btn-xs margin-top-5" href="javascript:void(0)" onclick="depMore('listDeps_depId','','${param.sid}','yes','');"><i class="fa fa-plus"></i>选择</a>
                                            </div>
                                        </div>
                                        <div class="groupOpt">
                                            <a href="javascript:void(0)" onclick="addGroup('${param.sid}',this,${dailyMod.id})" style="color:#1E88C7;padding-right: 10px;">保存</a>
                                            <a href="javascript:void(0)" onclick="cancleAddGroup()" style="color:#1E88C7;padding-right: 10px;">取消</a>
                                        </div>
                                    </div>
                                    <%--动态添加 --%>
                                    <div class="addGroupLevContent">
                                        <a href="javascript:void(0)" onclick="$('.addGroupLev').show();$('.addGroupLevContent').hide();" class="btn btn-xs">
                                            <i class="fa fa-plus"></i>添加一条
                                        </a>
                                    </div>
                                </div>
                                <%-- 人员模板--%>
                                <div style="clear:both;padding-top: 20px">
                                    <div style="font-size: 20px;line-height: 25px;color: black">人员模板<span style="font-size: 15px;color: gray;margin-top: 8px;">-设置需要显示条目的人员</span></div>
                                    <div class="memberLev">
                                        <c:choose>
                                            <c:when test="${not empty dailyMod.contentMemLevs }">
                                                <c:forEach items="${dailyMod.contentMemLevs}" var="content">
                                                    <div id="team${content.id}" class="memberLevDetail dailyModContent" >
                                                        <c:if test="${content.isRequire eq '1'}">
                                                            <font style="color: red;float: left;line-height: 30px">*</font>
                                                        </c:if>
                                                        <div class="memberLevContent" style="${content.hideState==1?'text-decoration:line-through;color:gray':'text-decoration:none;color:#000000' }">
                                                            <div class="memberLevTxt" >
                                                                    ${content.modContent}
                                                            </div>
                                                            <div class="memberLevPic">
                                                                <c:choose>
                                                                    <c:when test="${not empty content.listMembers}">
                                                                        <c:forEach items="${content.listMembers}" var="member" varStatus="vs">
                                                                            <div style="float: left;margin-top: 5px;margin-left: 5px;">
                                                                                <img style="display:block;float:left;margin-left:3px;"  src="/downLoad/userImg/${member.comId}/${member.memberId}?sid=${param.sid}" title="${member.memberName }"></img>
                                                                                <i class="zbmember-Name" style="float:left;margin-top: 15px">${member.memberName}</i>
                                                                            </div>
                                                                        </c:forEach>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="label label-default margin-right-5 margin-bottom-5" >所有人员</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </div>

                                                        </div>
                                                            <%--编辑 --%>
                                                        <div class="memberLevUpdate">
                                                            <div>
                                                                <a href="javascript:void(0)" onclick="editMemberContent(${content.id},'${param.sid }')" style="color:#1E88C7;padding-right: 10px">编辑</a>
                                                                <a href="javascript:void(0)" onclick="hideContent('${param.sid }',${content.id},1,this,${dailyMod.id },3)" style="color:#1E88C7;padding-right: 10px">隐藏</a>
                                                                <a href="javascript:void(0)" class="setRequire" isRequire="${content.isRequire eq '1'?'0':'1'}" contentId="${content.id}" modId="${dailyMod.id}"
                                                                   style="color:#1E88C7;padding-right: 10px">${content.isRequire eq '1'?'非必填':'必填'}</a>
                                                                <a href="javascript:void(0)" onclick="delContent('${param.sid }',${content.id},this,${dailyMod.id },3)" style="color:#1E88C7;padding-right: 2px">删除</a>
                                                            </div>
                                                        </div>
                                                            <%--显示--%>
                                                        <div class="memberLevShow">
                                                            <div>
                                                                已隐藏,<a href="javascript:void(0)" onclick="hideContent('${param.sid }',${content.id},0,this,${dailyMod.id },3)" style="color:#1E88C7;padding-right: 10px"> 显示</a>
                                                                <a href="javascript:void(0)" class="setRequire" isRequire="${content.isRequire eq '1'?'0':'1'}" contentId="${content.id}" modId="${dailyMod.id}"
                                                                   style="color:#1E88C7;padding-right: 10px">${content.isRequire eq '1'?'非必填':'必填'}</a>
                                                                <a href="javascript:void(0)" onclick="delContent('${param.sid }',${content.id},this,${dailyMod.id },3)" style="color:#1E88C7;padding-right: 2px">删除</a>
                                                            </div>
                                                        </div>
                                                            <%--修改 --%>
                                                        <div class="memberLevSave">
                                                                <%--存放模板临时条目 --%>
                                                            <div class="tempInfo" style="display:none">
                                                                <div class="tempUser">
                                                                    <select	 multiple="multiple" moreselect="true">
                                                                        <c:forEach items="${content.listMembers}" var="member" varStatus="vs">
                                                                            <option value="${member.memberId}" selected="selected">${member.memberName }</option>
                                                                        </c:forEach>
                                                                    </select>
                                                                </div>
                                                                <div class="tempImg">
                                                                    <c:choose>
                                                                        <c:when test="${not empty content.listMembers}">
                                                                            <c:forEach items="${content.listMembers}" var="member" varStatus="vs">
                                                                                 <img id="${content.id}_${member.memberId}" src="/downLoad/userImg/${member.comID}/${member.memberId}?sid=${param.sid}" title="${member.memberName }"></img>
                                                                            </c:forEach>
                                                                        </c:when>
                                                                    </c:choose>
                                                                </div>
                                                            </div>
                                                            <input type="hidden" id="tempMemberCont_${content.id}" name="tempMemberCont" value="${content.modContent}"/>
                                                            <div>
                                                                <a href="javascript:void(0)" onclick="updateMember('${param.sid }',${content.id},${dailyMod.id},this)" style="color:#1E88C7;padding-right: 10px;">保存</a>
                                                                <a href="javascript:void(0)" onclick="cancleEditMember(${content.id})" style="color:#1E88C7;padding-right: 10px;">取消</a>
                                                                <a href="javascript:void(0)" class="setRequire" isRequire="${content.isRequire eq '1'?'0':'1'}" contentId="${content.id}" modId="${dailyMod.id}"
                                                                   style="color:#1E88C7;padding-right: 10px">${content.isRequire eq '1'?'非必填':'必填'}</a>
                                                                <a href="javascript:void(0)" onclick="delContent('${param.sid }',${content.id},this,${dailyMod.id },3)" style="color:#1E88C7;padding-right: 2px">删除</a>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="noMemberLev" id="noMemberLev">
                                                    您还没有设置成员模板条目
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <%--预留用于动态添加的 --%>
                                    <div class="addMemberLev" style="display:none;">
                                        <div class="memberContent" >
                                            <div class="memberBody">
                                                <input type="text" class="form-control small" id="memberContent"  name="memberContent" style="width: 90%"/>
                                            </div>
                                            <div class="memberPic">
                                                <div style="float: left; display:none;">
                                                    <select list="listMembers" listkey="memberId" listvalue="depName" id="listMembers_memberId"
                                                            name="listMembers.memberId" ondblclick="removeClick(this.id)" multiple="multiple" moreselect="true"
                                                            style="width: 90%;">
                                                    </select>
                                                </div>
                                                <%--存放图片 --%>
                                                <div id="userMoreImglistMembers_memberId" style="float:left;"></div>
                                                <a href="javascript:void(0)" class="btn btn-blue btn-xs margin-top-5" onclick="userMore4Week('listMembers_memberId','','${param.sid}');" title="请选择人员"><i class="fa fa-plus"></i>选择</a>
                                            </div>

                                        </div>
                                        <div class="memberOpt">
                                            <a href="javascript:void(0)"  onclick="addMember('${param.sid}',this,${dailyMod.id})" style="color:#1E88C7;padding-right: 10px;">保存</a>
                                            <a href="javascript:void(0)"  onclick="cancleAddMember()" style="color:#1E88C7;padding-right: 10px;">取消</a>
                                        </div>
                                    </div>
                                    <%--动态添加 --%>
                                    <div class="addMemberLevContent" style="clear:both;padding-left: 10px;padding-top: 10px">
                                        <a href="javascript:void(0)" onclick="$('.addMemberLev').show();$('.addMemberLevContent').hide();" class="btn btn-xs">
                                            <i class="fa fa-plus"></i>添加一条
                                        </a>
                                    </div>
                                </div>
                                    <div style="clear:both;padding-top: 10px">
                                    </div>
                                <hr class="border-dash">
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</form>
</body>
<script type="text/javascript">
    var pageParam = {
        "sid":"${param.sid}"
    }
</script>
</html>

