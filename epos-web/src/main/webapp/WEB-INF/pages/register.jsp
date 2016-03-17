<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="cp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="baidu-site-verification" content="bSntFq8VC1"/>
    <link href="${cp}/resources/images/favicon.ico" rel="shortcut icon"/>
    <title>E-POS - 易珀斯</title>
    <link href="${cp}/resources/css/global.css" rel="stylesheet" type="text/css"/>
    <link href="${cp}/resources/css/common.css" rel="stylesheet" type="text/css"/>
    <script src="${cp}/resources/js/jquery-1.9.1.min.js"></script>
    <script src="${cp}/resources/js/jquery.form.js"></script>
    <script src="${cp}/resources/js/jquery.validate.min.js"></script>
    <script src="${cp}/resources/js/additional-methods.min.js"></script>
    <script src="${cp}/resources/js/tinytrust.register.js"></script>
    <script src="${cp}/resources/js/tinytrust.common.js"></script>
    <script src="${cp}/resources/js/layer/layer.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#registerForm").validate({
                event: "blur",
                rules: {
                    "cellphone": {
                        required: true,
                        isPhoneCheck: true,
                        minlength: 11,
                        maxlength: 11,
                        remote: {
                            url: "${cp}/system/cellphone_unique_check",
                            type: "post",    //数据发送方式
                            dataType: "json",//接受数据格式
                            data: {                     //要传递的数据
                                "cellphone": function () {
                                    return $("#cellphone").val();
                                }
                            }

                        }
                    },
                    "password": {required: true, minlength: 6, maxlength: 16, stringCheck: true},
                    "repassword": {
                        required: true,
                        minlength: 6,
                        maxlength: 16,
                        stringCheck: true,
                        equalTo: $("#password")
                    },
                    "userName": {required: true, minlength: 2, maxlength: 4, isChineseCheck: true},
                    "captcha": {
                        required: true,
                        stringCheck: true,
                        remote: {
                            url: "${cp}/system/captcha_check",
                            type: "post",               //数据发送方式
                            dataType: "json",           //接受数据格式
                            data: {                     //要传递的数据
                                "captcha": function () {
                                    return $("#captcha").val();
                                }
                            }
                        }
                    }
                },
                messages: {
                    "cellphone": {isPhoneCheck: "请输入正确的手机号", remote: "手机号已被注册"},
                    "userName": {isChineseCheck: "请输入真实姓名，关系到您的账户加款操作，长度为2-4个字符"},
                    "captcha": {remote: "验证码错误"}
                },
                success: function (form) {
                    var options = {
                        type: 'POST',
                        url: '${cp}/user/register',
                        dataType: 'json',
                        resetForm: true,//提交成功后重置表单
                        success: function (response) {
                            if (response.result == "success") {
                                layer.msg(response.message, {icon: 1, title: '提示'}, function (index) {
                                    location = "${cp}/index.html";
                                    layer.close(index);
                                });
                            }
                        }
                    };
                    $("#registerForm").ajaxForm(options);
                }
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
<header class="siteHead">
    <div class="W980">
        <figure class="logoarea">
        </figure>
    </div>
</header><!--header end-->
<section class="W980 block mainWrap mainCont">
    <div class="regbox">
        <div class="title">
            <h3 class="bold">注册</h3>
        </div>
        <form id="registerForm">
            <ul class="form">
                <li>
                    <label>手机号：</label>
                    <input type="text" name="cellphone" id="cellphone" class="input" placeholder="手机号"/>
                </li>
                <li>
                    <label>姓名：</label>
                    <input type="text" name="userName" id="userName" class="input" placeholder="姓名"/>
                </li>
                <li>
                    <label>密码：</label>
                    <input type="password" name="password" id="password" class="input" placeholder="密码"/>
                </li>
                <li>
                    <label>确认密码：</label>
                    <input type="password" name="repassword" id="repassword" class="input" placeholder="确认密码"/>
                </li>
                <li>
                    <label>邀请码：</label>
                    <input type="text" name="topUserCode" id="topUserCode" class="input" placeholder="邀请码 ( 非必填  ) "/>
                </li>
                <li>
                    <label>
                        验证码：
                    </label>
                    <input id="captcha" name="captcha" class="input" style="width:100px" placeholder="验证码">
                </li>
                <li class="label">
                    <img id="captchaImg" src="${cp}/captcha.png" alt="captcha" border="2" onclick="refreshCaptcha();"
                         title="看不清？点击更换另一个验证码。"/>
                </li>
                <li class="label">
                    <button type="submit" id="submitBtn" class="reg">立即注册</button>
                    <a href="${cp}/index.html">&nbsp;&nbsp;已有账号,请直接登录</a>
                </li>
            </ul>
        </form>
    </div>
</section><!--end mainWrap-->
</body>
</html>