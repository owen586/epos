<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>E-POS - 易珀斯</title>
    <link href="${cp}/resources/images/favicon.ico" rel="shortcut icon"/>
    <jsp:include page="/WEB-INF/pages/common/include.jsp"/>

    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="expires" content="0"/>
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/>
    <meta http-equiv="description" content="This is my page"/>
    <script type="text/javascript">
        $(function () {
            //初始化角色
            $.ajax({
                type: 'POST',
                async: false,//同步
                url: '${cp}/user/list/all',
                success: function (response) {
                    if (null != response && response.result == "success") {
                        var userOptions = "";
                        $.each(response.dataMap.userList, function (index, aUser) {
                            userOptions += "<option value='" + aUser.userCode + "'>" + aUser.userName + "</option>"
                        });
                        $("#userCode").append(userOptions);
                    }
                }
            });

            $("#submitBtn").click(function () {
                var userCode = $("#userCode").val();//角色编号
                var feeRate = $("#feeRate").val();//费率
                var topUserFeeRateReturn = $("#topUserFeeRateReturn").val();//上级返点费率

                if (null == userCode || "" == userCode) {
                    parent.layer.alert("请选择用户", {icon: 2, title: '提示'}, function (index) {
                        parent.layer.close(index);
                    });
                    return false;
                }
                if (null == feeRate || "" == feeRate) {
                    parent.layer.alert("请输入费率", {icon: 2, title: '提示'}, function (index) {
                        parent.layer.close(index);
                    });
                    return false;
                }

                if (null == topUserFeeRateReturn || "" == topUserFeeRateReturn) {
                    parent.layer.alert("请输入上级返点费率", {icon: 2, title: '提示'}, function (index) {
                        parent.layer.close(index);
                    });
                    return false;
                }

                var options = {
                    type: 'POST',
                    url: '${cp}/price/user/edit',
                    dataType: 'json',
                    success: function (response) {
                        if (response.result == "success") {
                            parent.layer.msg(response.message, {icon: 1, title: '提示'}, function (index) {
                                location = "${cp}/price/user/list/index";
                                parent.layer.close(index);
                            });
                        }
                    }
                };

                $("#myForm").ajaxForm(options);
            });
        });

    </script>
</head>
<body>
<form id="myForm" class="pushT">
    <div>
        <a href="javascript:void(0)">价格管理</a>
        &nbsp;&gt;&nbsp;
				<span>
					编辑用户价格
				</span>
    </div>
    <fieldset class="note pushT" style="width: 800px;">
        <ul class="form dislocation-x">
            <li>
                <label>
                    用户:
                </label>
                <select name="userCode" id="userCode" class="long">
                    <option value="">请选择</option>
                </select>
            </li>
            <li>
                <label>
                    到账费率:
                </label>
                <input type="text" name="feeRate" id="feeRate" value="${priceUser.feeRate}"/>‱
                <samp class="gray">请输入费率</samp>
            </li>
            <li>
                <label>
                    上级返点费率:
                </label>
                <input type="text" name="topUserFeeRateReturn" id="topUserFeeRateReturn"
                       value="${priceUser.topUserFeeRateReturn}"/>‱
                <samp class="gray">请输入上级返点费率</samp>
            </li>
            <li class="btnarea">
                <button id="submitBtn">
                    确认
                </button>
                <a href="javascript:void(0);" onclick="history.go(-1);">&nbsp;&nbsp;返回</a>
            </li>
            <!-- li class="label">
                <span id="message" style="color:green;display:none"></span>
            </li -->
        </ul>
    </fieldset>
</form>
</body>
<c:if test="${not empty priceUser }">
    <script>
        $(function () {
            $("#userCode").val("${priceUser.userCode}");
        });
    </script>
</c:if>
</html>