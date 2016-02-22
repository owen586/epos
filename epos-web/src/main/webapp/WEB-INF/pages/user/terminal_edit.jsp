<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>E-POS - 易珀斯</title>
    <link href="${cp}/resources/images/favicon.ico" rel="shortcut icon"/>
    <jsp:include page="/WEB-INF/pages/common/include.jsp"/>
    <script type="text/javascript">
        $(function () {
            $("#submitBtn").click(function () {
                var terminalCode = $("#terminalCode").val();

                if (null == terminalCode || "" == trim(terminalCode)) {
                    parent.layer.alert("请输入终端编号", {icon: 2, title: '提示'}, function (index) {
                        parent.layer.close(index);
                    });
                    return false;
                }
                var options = {
                    type: 'POST',
                    url: '${cp}/user/terminal/edit',
                    dataType: 'json',
                    //resetForm:true,//提交成功后重置表单
                    success: function (response) {
                        if (response.result == "success") {
                            parent.layer.msg(response.message, {icon: 1, title: '提示'}, function (index) {
                                location = "${cp}/user/terminal/index";
                                parent.layer.close(index);
                            });
                        }
                    }
                };

                $("#ttFrom").ajaxForm(options);
            });

        });
    </script>
</head>
<body>
<form id="ttFrom" class="pushT">
    <div>
        <a href="javascript:void(0)"><i class="icon-home"></i>POS终端管理</a>
        &nbsp;&gt;&nbsp;
        <a href="javascript:void(0);">POS终端列表</a>
        &nbsp;&gt;&nbsp;
				<span>
					编辑终端信息
				</span>
    </div>
    <fieldset class="note pushT" style="width: 800px;">
        <ul class="form dislocation-x">
            <li>
                <label>
                    用户编号：
                </label>
                <input type="text" name="userCode_" id="userCode_" disabled="disabled" class="long"
                       value="${userCode}"/>
                <input type="hidden" name="userCode" value="${userCode }"/>
            </li>
            <li>
                <label>
                    终端编号：
                </label>
                <input type="text" name="terminalCode" id="terminalCode" class="middel"
                       value="${terminal.terminalCode }"/>
            </li>
            <li class="btnarea">
                <button id="submitBtn">
                    确认
                </button>
                <a href="javascript:void(0);" onclick="history.go(-1);">&nbsp;&nbsp;返回</a>
            </li>
        </ul>
    </fieldset>
</form>
</body>
</html>