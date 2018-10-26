<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.westar.base.cons.SystemStrConstant"%>
<%@page import="com.westar.base.util.ConstantInterface"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
<%@taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<body>
	<div class="page-content">
    <div class="page-body" >
        <div class="row">
            <div class="col-md-12 col-xs-12 ">
                <div class="wrapper">
			        <div class="head-info">
			            <jsp:include page="./operationSummarize_head.jsp"></jsp:include>
			        </div>
			        <!--任务分析-->
			        <div class="task-analysis">
			            <jsp:include page="./operationSummarize_task-analysis.jsp"></jsp:include>
			        </div>
			        <!--分类信息-->
			        <div class="type-info">
			            <jsp:include page="./operationSummarize_type-info.jsp"></jsp:include>
			        </div>
			    </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>