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
                var oldPwd = $("#oldPwd").val();
                if (null == oldPwd || "" == oldPwd) {
                    parent.layer.alert("请输入旧密码", {icon: 2, title: '提示'}, function (index) {
                        parent.layer.close(index);
                    });
                    return false;
                }

                var newPwd = $("#newPwd").val();
                if (null == newPwd || "" == newPwd) {
                    parent.layer.alert("请输入新密码", {icon: 2, title: '提示'}, function (index) {
                        parent.layer.close(index);
                    });
                    return false;
                }
                var reNewPwd = $("#reNewPwd").val();
                if (null == reNewPwd || "" == reNewPwd) {
                    parent.layer.alert("请输入确认密码", {icon: 2, title: '提示'}, function (index) {
                        parent.layer.close(index);
                    });
                    return false;
                }

                if (newPwd != reNewPwd) {
                    parent.layer.alert("两次输入的密码不一致", {icon: 2, title: '提示'}, function (index) {
                        parent.layer.close(index);
                    });
                    return false;
                }

                var update_password_url = '${cp}/system/modify_password';
                var options = {
                    type: "POST",
                    url: update_password_url,
                    resetForm: true,//提交成功后重置表单
                    dataType: "json",
                    success: function (responseData) {
                        var message = responseData.message;
                        if (responseData.result == "success") {
                            parent.layer.msg(message + ',为了您的账户安全,请重新登陆!', {icon: 1, title: '提示'}, function (index) {
                                parent.window.location = '${cp}/system/logout';
                                parent.layer.close(index);
                            });
                        }
                        else {
                            parent.layer.msg(message, {icon: 2, title: '提示'}, function (index) {
                                parent.layer.close(index);
                            });
                        }
                    }
                };
                $("#updatePwdForm").ajaxForm(options);
            });
        });
    </script>
</head>
<body>
<form id="updatePwdForm">
    <div class="pushT">
        <a href="javascript:void(0);">系统管理</a>
        &nbsp;&gt;&nbsp;
			<span>
				修改密码
			</span>
    </div>

    <fieldset class="note pushT">
        <ul class="form dislocation-x">
            <li>
                <label>旧密码：</label>
                <input type="password" name="oldPwd" id="oldPwd" class="middle"/>
            </li>
            <li>
                <label>新密码：</label>
                <input type="password" name="newPwd" id="newPwd" class="middle"/>
            </li>
            <li>
                <label>确认新密码：</label>
                <input type="password" name="reNewPwd" id="reNewPwd" class="middle"/>
            </li>
            <li class="btnarea">
                <button id="submitBtn">
                    确认
                </button>
            </li>
        </ul>
    </fieldset>
</form>
</body>
</html>
