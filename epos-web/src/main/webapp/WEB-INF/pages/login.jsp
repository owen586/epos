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
    <script src="${cp}/resources/js/simplefoucs.js"></script>
    <script src="${cp}/resources/js/tinytrust.common.js"></script>
</head>
<body>
<header class="siteHead">
    <div class="W980">
        <figure class="logoarea">
        </figure>
    </div>
</header>
<section class="W980 mainWrap">
    <div class="logLeft left" style="padding-left:20px;">
        <img src="${cp}/resources/images/login.png"/></div>
    <aside class="" style="float:right;margin-top:30px;">
        <div class="block loginArea">
            <hgroup><h3>登录</h3></hgroup>
            <form id="loginFrm" method="post" action="${cp}/system/login">
                <ul>
                    <li><input type="text" name="cellphone" id="cellphone" class="input" placeholder="请输入手机号"
                               onfocus="this.value=''" onblur="if(this.value==''){this.value='请输入手机号'}"/></li>
                    <li><input type="password" name="password" id="password" class="input" placeholder="请输入密码"
                               onfocus="this.value=''" onblur="if(this.value==''){this.value='请输入密码'}"/></li>
                    <li><input type="text" name="captcha" id="captcha" class="input inputCode" placeholder="验证码"
                               onfocus="this.value=''" onblur="if(this.value==''){this.value='验证码'}"/>
                        <img id="captchaImg" src="${cp}/captcha.png" alt="captcha" border="1"
                             onclick="refreshCaptcha();" title="点击刷新验证码"/>
                    <li class="tcenter">
                        <button type="button" class="btn btnLogin" id="submitBtn">登录</button>
                    </li>
                    <li class="tright">
                        <a id="register" href="${cp}/system/register">免费注册</a>
                    </li>
                </ul>
            </form>
            <dl class="otherLogin">
                <dd id="msg" style="color:red">
                    ${login_message_error}
                </dd>
            </dl>
        </div>
    </aside>
</section>
<footer class="siteFoot clearfix" style="position: fixed;bottom:0px">
    <div class="W980 copyright">
        &copy; Copyright 2015-2016. <span class="bold"><a href="http://www.ah918.net"
                                                     target="_blank">青岛傲华网络信息技术有限公司</a></span> 版权所有 | 鲁ICP备13010353号
    </div>
</footer>
</body>
<script type="text/javascript">
    //刷新验证码
    var refreshCaptcha = function () {
        var rand = Math.round(999999 * Math.random());
        $("#captchaImg").attr("src", "${cp}/captcha.png?" + rand);
        $("#captcha").val("验证码");
    };
    $(document).ready(function () {
        $("input[type='text'],input[type='password']").focus(function () {
            $(this).addClass("focus")
        }).blur(function () {
            $(this).removeClass("focus")
        });
        //登录
        $("#submitBtn").click(function () {
            var cellphone = $("#cellphone").val();
            var password = $("#password").val();
            var captcha = $("#captcha").val();
            if (null == cellphone || "" == trim(cellphone) || cellphone == "手机号") {
                $("#msg").text("请输入手机号");
                return false;
            }
            if (null == password || "" == trim(password) || password == "密码") {
                $("#msg").text("请输入密码");
                return false;
            }
            if (null == captcha || "" == trim(captcha) || captcha == "验证码") {
                $("#msg").text("请输入验证码");
                return false;
            }
            $("#loginFrm").submit();
        });
    });
</script>
</html>