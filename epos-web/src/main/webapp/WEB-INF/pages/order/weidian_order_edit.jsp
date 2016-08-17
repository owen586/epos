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
            //初始化用户
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
                var userCode = $("#userCode").val();//用户编号
                if (null == userCode || "" == trim(userCode)) {
                    parent.layer.alert("请选择用户", {icon: 2, title: '提示'}, function (index) {
                        parent.layer.close(index);
                    });
                    return false;
                }

//                var outterUserCode = $("#outterUserCode").val();
//                if (null == outterUserCode || "" == trim(outterUserCode)) {
//                    parent.layer.alert("请输入外部用户编号", {icon: 2, title: '提示'}, function (index) {
//                        parent.layer.close(index);
//                    });
//                    return false;
//                }

                var terminalCode = $("#terminalCode").val();
                if (null == terminalCode || "" == trim(terminalCode)) {
                    parent.layer.alert("请输入终端编号(微店或淘宝订单编号)", {icon: 2, title: '提示'}, function (index) {
                        parent.layer.close(index);
                    });
                    return false;
                }

                var orderMoney = $("#orderMoney").val();
                if (null == orderMoney || "" == trim(orderMoney)) {
                    parent.layer.alert("请输入加款金额", {icon: 2, title: '提示'}, function (index) {
                        parent.layer.close(index);
                    });
                    return false;
                }

                if(!isDigit(orderMoney) || parseInt(orderMoney)>10000){
                    parent.layer.alert("请输入合法的金额,单笔加款金额限制在10000以内", {icon: 2, title: '提示'}, function (index) {
                        parent.layer.close(index);
                    });
                    return false;
                }


                var options = {
                    type: 'POST',
                    url: '${cp}/order/weidian/save',
                    dataType: 'json',
                    //resetForm:true,//提交成功后重置表单
                    success: function (response) {
                        if (response.result == "success") {
                            parent.layer.msg(response.message, {icon: 1, title: '提示'}, function (index) {
                                location="${cp}/order/weidian/list/index";
                                parent.layer.close(index);
                            });
                        }
                        else
                        {
                            parent.layer.msg(response.message, {icon: 2, title: '提示'}, function (index) {
                            });
                        }
                    }
                };

                $("#registerForm").ajaxForm(options);
            });

        });

        //刷新验证码
        var refreshCaptcha = function () {
            var rand = Math.round(999999 * Math.random());
            $("#captchaImg").attr("src", "${cp}/captcha.png?" + rand);
        };
    </script>
</head>
<body>
<form id="registerForm" class="pushT">
    <div>
        <a href="javascript:void(0)"><i class="icon-home"></i>订单管理</a>
        &nbsp;&gt;&nbsp;
				<span>
					新增加款订单
				</span>
    </div>
    <fieldset class="note pushT" style="width: 800px;">
        <ul class="form dislocation-x">
            <li>
                <label>
                    客户名称：
                </label>
                <select name="userCode" id="userCode" class="long">
                    <option value="">请选择</option>
                </select>
            </li>
            <%--<li>--%>
                <%--<label>--%>
                    <%--外部用户编号：--%>
                <%--</label>--%>
                <%--<input type="text" name="outterUserCode" id="outterUserCode" class="long"/>--%>
            <%--</li>--%>
            <li>
                <label>
                    外部订单编号：
                </label>
                <input type="text" name="terminalCode" id="terminalCode" class="middle"/>
            </li>
            <li>
                <label>
                    订单金额：
                </label>
                <input type="text" name="orderMoney" id="orderMoney" class="middle"/>
                <samp class="gray">单位为元,单笔充值限制最高金额10000元</samp>
            </li>
            <%--<li>--%>
                <%--<label>--%>
                    <%--到账类型：--%>
                <%--</label>--%>
                <%--<select name="tranferType" id="tranferType" class="middle">--%>
                    <%--<option value="">全部</option>--%>
                    <%--<option value="1">T+0</option>--%>
                    <%--<option value="2">T+1</option>--%>
                <%--</select>--%>
            <%--</li>--%>
            <li>
                <label>
                    验证码：
                </label>
                <input type="text" name="captcha" id="captcha" class="input inputCode" />
            </li>
            <li>
                <label>
                </label>
                <img id="captchaImg"  style="margin-left: 195px" src="${cp}/captcha.png" alt="captcha" border="1" onclick="refreshCaptcha();" title="点击刷新验证码"/>
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