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
                var outterUserCode = $("#outterUserCode").val();
                var terminalCode = $("#terminalCode").val();
                var orderMoney = $("#orderMoney").val();
                var tranferType = $("#tranferType").val();

                if (null == outterUserCode || "" == trim(outterUserCode)) {
                    parent.layer.alert("请输入外部用户编号", {icon: 2, title: '提示'}, function (index) {
                        parent.layer.close(index);
                    });
                    return false;
                }

                if (null == terminalCode || "" == trim(terminalCode)) {
                    parent.layer.alert("请输入终端（支付通）编号", {icon: 2, title: '提示'}, function (index) {
                        parent.layer.close(index);
                    });
                    return false;
                }

                if (null == orderMoney || "" == trim(orderMoney)) {
                    parent.layer.alert("请输入加款金额", {icon: 2, title: '提示'}, function (index) {
                        parent.layer.close(index);
                    });
                    return false;
                }

                if (null == tranferType || "" == tranferType) {
                    parent.layer.alert("请选择到账类型", {icon: 2, title: '提示'}, function (index) {
                        parent.layer.close(index);
                    });
                    return false;
                }

                var options = {
                    type: 'POST',
                    url: '${cp}/order/offline/save',
                    dataType: 'json',
                    //resetForm:true,//提交成功后重置表单
                    success: function (response) {
                        if (response.result == "success") {
                            parent.layer.msg(response.message, {icon: 1, title: '提示'}, function (index) {
                                //location="${cp}/user/index";
                                parent.layer.close(index);
                            });
                        }
                    }
                };

                $("#registerForm").ajaxForm(options);
            });

        });
    </script>
</head>
<body>
<form id="registerForm" class="pushT">
    <div>
        <a href="javascript:void(0)"><i class="icon-home"></i>订单管理</a>
        &nbsp;&gt;&nbsp;
				<span>
					新增线下加款订单
				</span>
    </div>
    <fieldset class="note pushT" style="width: 800px;">
        <ul class="form dislocation-x">
            <li>
                <label>
                    外部用户编号：
                </label>
                <input type="text" name="outterUserCode" id="outterUserCode" class="long"/>
            </li>
            <li>
                <label>
                    终端（支付通）编号：
                </label>
                <input type="text" name="terminalCode" id="terminalCode" class="middle"/>
            </li>
            <li>
                <label>
                    订单金额：
                </label>
                <input type="text" name="orderMoney" id="orderMoney" class="middle"/>
            </li>
            <li>
                <label>
                    到账类型：
                </label>
                <select name="tranferType" id="tranferType" class="middle">
                    <option value="">全部</option>
                    <option value="1">T+0</option>
                    <option value="2">T+1</option>
                </select>
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