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
                var cellphone = $("#cellphone").val();
                var userName = $("#userName").val();
                var roleCode = $("#roleCode").val();
                var tranferType = $("#tranferType").val();
                var status = $("#status").val();

                if (null == cellphone || "" == trim(cellphone)) {
                    parent.layer.alert("请输入手机号", {icon: 2, title: '提示'}, function (index) {
                        parent.layer.close(index);
                    });
                    return false;
                }

                if (null == userName || "" == trim(userName)) {
                    parent.layer.alert("请输入姓名", {icon: 2, title: '提示'}, function (index) {
                        parent.layer.close(index);
                    });
                    return false;
                }

                <c:if test="${empty user}">
                var password = $("#password").val();
                if (null == password || "" == trim(password)) {
                    parent.layer.alert("请输入密码", {icon: 2, title: '提示'}, function (index) {
                        parent.layer.close(index);
                    });
                    return false;
                }
                </c:if>

                if (null == roleCode || "" == roleCode) {
                    parent.layer.alert("请选择角色", {icon: 2, title: '提示'}, function (index) {
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

                if (null == status || "" == status) {
                    parent.layer.alert("请选择用户状态", {icon: 2, title: '提示'}, function (index) {
                        parent.layer.close(index);
                    });
                    return false;
                }
                var options = {
                    type: 'POST',
                    url: '${cp}/user/user_edit',
                    dataType: 'json',
                    //resetForm:true,//提交成功后重置表单
                    success: function (response) {
                        if (response.result == "success") {
                            parent.layer.msg(response.message, {icon: 1, title: '提示'}, function (index) {
                                location = "${cp}/user/index";
                                parent.layer.close(index);
                            });
                        }
                    }
                };

                $("#registerForm").ajaxForm(options);
            });

            //手机号唯一性校验
            $("#cellphone").blur(function () {
                var cellphone = $(this).val();
                if (null != cellphone && "" != cellphone) {
                    $.ajax({
                        type: 'POST',
                        async: false,//同步
                        url: '${cp}/system/cellphone_unique_check',
                        data: {"cellphone": cellphone},
                        success: function (isUnique) {
                            if (!isUnique) {
                                parent.layer.msg("手机号已经注册", {icon: 2, title: '提示'}, function (index) {
                                    $("#cellphone").val("");//清空登陆账号
                                    parent.layer.close(index);
                                });
                            }
                        }
                    });
                }
            });
        });
    </script>
</head>
<body>
<form id="registerForm" class="pushT">
    <div>
        <a href="javascript:void(0)"><i class="icon-home"></i>会员管理</a>
        &nbsp;&gt;&nbsp;
        <a href="javascript:void(0);">用户列表</a>
        &nbsp;&gt;&nbsp;
				<span>
					编辑用户信息
				</span>
    </div>
    <fieldset class="note pushT" style="width: 800px;">
        <ul class="form dislocation-x">
            <li>
                <label>
                    手机号：
                </label>
                <input type="text" name="cellphone" id="cellphone" class="middle" value="${user.cellphone}"/>
            </li>
            <li>
                <label>
                    姓名：
                </label>
                <input type="text" name="userName" id="userName" class="middle" value="${user.userName}"/>
            </li>
            <li>
                <label>
                    密码：
                </label>
                <input type="text" name="password" id="password" class="long" <c:if
                        test='${not empty user}'> placeholder="密码为空则保留原密码"</c:if> />
            </li>
            <li>
                <label>
                    角色：
                </label>
                <select name="roleCode" id="roleCode" class="middle">
                    <option value="">全部</option>
                    <c:forEach items="${roleList}" var="role">
                        <option value="${role.roleCode }">${role.roleName }</option>
                    </c:forEach>
                </select>
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
            <li>
                <label>
                    用户状态：
                </label>
                <select name="status" id="status" class="middle">
                    <option value="">全部</option>
                    <option value="1">待审核</option>
                    <option value="2">正常</option>
                    <option value="3">停用</option>
                </select>
            </li>
            <li>
                <label>
                    外部商家编号：
                </label>
                <input type="text" name="outterUserCode" id="outterUserCode" class="middle"
                       value="${user.outterUserCode }"/>
            </li>
            <li>
                <label>
                    上级编号：
                </label>
                <input type="text" name="topUserCode" id="topUserCode" class="long" value="${user.topUserCode }"/>
            </li>
            <li class="btnarea">
                <button id="submitBtn">
                    确认
                </button>
                <a href="javascript:void(0);" onclick="history.go(-1);">&nbsp;&nbsp;返回</a>
            </li>
        </ul>
    </fieldset>
    <c:if test="${not empty user}">
        <input type="hidden" name="userCode" value="${user.userCode }"/>
        <script>
            $("#roleCode").val("${user.roleCode}");//角色编号
            $("#tranferType").val("${user.tranferType}");//到账类型
            $("#status").val("${user.status}");//用户状态
            $("#cellphone").prop("disabled", "disabled");//手机号码
        </script>
    </c:if>
</form>
</body>
</html>