<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="cp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
<head>
    <title>E-POS - 易珀斯</title>
    <link href="${cp}/resources/images/favicon.ico" rel="shortcut icon"/>
    <link type="text/css" rel="stylesheet"
          href="${cp}/resources/css/astpt.global.css"/>
    <link type="text/css" rel="stylesheet"
          href="${cp}/resources/css/astpt.common.css"/>
    <script src="${cp}/resources/js/jquery-1.9.1.min.js"></script>
    <script src="${cp}/resources/js/jquery-migrate-1.0.0.js"></script>
    <script src="${cp}/resources/js/layer2/layer.js"></script>
    <script>
        var WinH = ($(window).height() - 97);
        $(document)
                .ready(
                        function () {
                            $("#sideWrap,#shrinkBut,#main").css("height", WinH)
                            $(window).resize(function () {
                                var WinH = ($(window).height() - 97);
                                $("#sideWrap,#shrinkBut,#main").css("height", WinH)
                            });

                            //一级菜单
                            $(".mainNav menu li a")
                                    .on(
                                            "click",
                                            function () {
                                                $(".tab").children().removeClass(
                                                        "current");
                                                $(this).parent()
                                                        .addClass("current");
                                                var topMenuCode = $(this)
                                                        .attr("id");
                                                var subMenuName = $(this).find(
                                                        "span").text();
                                                $
                                                        .ajax({
                                                            type: 'POST',
                                                            url: '${cp}/menu/query_submenu/'
                                                            + topMenuCode,
                                                            success: function (response) {
                                                                if (null != response
                                                                        && response.result == 'success') {
                                                                    $("#subMenu")
                                                                            .empty();
                                                                    $(
                                                                            "#sub_menu_head")
                                                                            .text(
                                                                                    subMenuName);
                                                                    var menuList = response.dataMap.menuList;
                                                                    var firstSubMenuUrl = null;
                                                                    $
                                                                            .each(
                                                                                    menuList,
                                                                                    function (index,
                                                                                              aMenu) {
                                                                                        var liStr = '';
                                                                                        if (index == 0) {
                                                                                            firstSubMenuUrl = aMenu.menuUrl;
                                                                                            liStr = "<li class='current'><a href='${cp}" + aMenu.menuUrl + "'>"
                                                                                                    + aMenu.menuName
                                                                                                    + "</a></li>";
                                                                                        } else {
                                                                                            liStr = "<li><a href='${cp}" + aMenu.menuUrl + "'>"
                                                                                                    + aMenu.menuName
                                                                                                    + "</a></li>";
                                                                                        }
                                                                                        $(
                                                                                                "#subMenu")
                                                                                                .append(
                                                                                                        liStr);
                                                                                    });
                                                                    $("#main")
                                                                            .attr(
                                                                                    "src",
                                                                                    "${cp}"
                                                                                    + firstSubMenuUrl);
                                                                }
                                                            }
                                                        });
                                            });

                            //注册二级菜单点击事件
                            $(document).on("click", "#subMenu li a", function () {
                                $("#subMenu li").removeClass("current");
                                $(this).parent().addClass("current");
                            });

                            //收缩条
                            $("#shrinkBut").toggle(function () {
                                $("body").addClass("shrinkBUp");
                            }, function () {
                                $("body").removeClass("shrinkBUp");
                            });
                        });

        //退出系统
        var logout = function () {
            /*
             *-1： 没有图标   0 ：感叹号  1 正确符号，勾 2：错误符号，叉
             * 3： 问号           4： 图标锁   5：哭脸     6：笑脸
             */
            layer.confirm("确定退出吗?", {
                icon: 3,
                title: '提示'
            }, function (index) {
                parent.location = "/system/logout";
                layer.close(index);
            });
        }
    </script>
    <base target="main"/>
</head>
<body class="origin">
<header id="topWrap">
    <div class="topColumn">
        <div id="logoarea">
            <a title="TinyTrust">TinyTrust </a>
        </div>
        <ul class="pToolbar">
            <li><img title="安全退出" onclick="javascript:logout();"
                     src="${cp}/resources/images/logout.png">
            </li>
        </ul>
        <nav class="mainNav">
            <menu class="tab">
                <c:forEach items="${menuList}" var="menu" varStatus="menuStatus">
                    <li <c:if test="${menuStatus.index == 0 }">class="current"</c:if>>
                        <a href="${menu.menuUrl}" id="${menu.menuCode}"> <span>${menu.menuName}</span>
                        </a></li>
                </c:forEach>
            </menu>
        </nav>
    </div>
    <!-- topcolumn end -->
</header>
<!-- topwrapper end -->

<!-- MAIN -->
<div id="mainWrap">
    <article id="mainColumn">
        <iframe width="100%" scrolling="auto" frameborder="0" id="main"
                name="main" src=""></iframe>
    </article>
    <!-- mainColumn end -->
</div>
<!-- mainwrapper end -->

<aside id="sideWrap">
    <a id="shrinkBut">收缩栏</a>

    <div id="sideColumn">
        <figure class="sideBlock">
            <figcaption class="sideHead">
                <i></i>
                <h6 id="sub_menu_head">首页</h6>
            </figcaption>
            <menu class="sideMenu" id="subMenu">
                <li class="current"><a href="${cp}/system/workpanel">工作面板</a>
                </li>
            </menu>
        </figure>
        <!-- sideblock end -->

        <figure class="sideBlock sideInfo pushT">
            <figcaption class="sideHead">
                <i></i>
                <h6>用户信息</h6>
            </figcaption>
            <ul>
                <li class="siteLine"><tt>
                    <img src="${cp}/resources/images/avator.png" id="logo">
                </tt></li>
                <li><label>手机号：</label><strong>${sessionScope.current_user_in_session.cellphone}</strong>
                </li>
                <li><label>用户编号：</label><strong>${sessionScope.current_user_in_session.userCode}</strong>
                </li>
                <li><label>客户姓名：</label><strong>${sessionScope.current_user_in_session.userName}</strong>
                </li>
                <li><label>客户角色：</label> <strong> <c:if
                        test="${sessionScope.current_user_in_session.roleCode == 1 }">管理员</c:if>
                    <c:if
                            test="${sessionScope.current_user_in_session.roleCode == 2 }">经销商</c:if>
                    <c:if
                            test="${sessionScope.current_user_in_session.roleCode == 3 }">直销商</c:if>
                </strong>
                </li>
            </ul>
        </figure>
        <!-- sideblock end -->
        <figure class="sideBlock sideInfo">
            <figcaption class="sideHead">
                <i></i>
                <h6>客服中心</h6>
            </figcaption>
            <ol>
                <li><label>貂蝉：</label> <a target="_blank"
                                          href="http://wpa.qq.com/msgrd?v=3&uin=318883916&site=qq&menu=yes">
                    <img border="0" src="http://wpa.qq.com/pa?p=2:318883916:51"
                         alt="点击这里给我发消息" title="点击这里给我发消息"/> </a>
                </li>
                <!-- <li><label>西施：</label> <a target="_blank"
                    href="http://wpa.qq.com/msgrd?v=3&uin=318883916&site=qq&menu=yes">
                    <img border="0" src="http://wpa.qq.com/pa?p=2:318883916:51"
                    alt="点击这里给我发消息" title="点击这里给我发消息"/>
                </a></li> -->
                <li><label>公司电话：</label>0532-82837177</li>
                <li><label>技术支持：</label>13913317376</li>
            </ol>
        </figure>
        <!-- sideblock end -->
    </div>
    <!-- sidecolumn end -->
</aside>
<!-- sidewrapper end -->
</body>
</html>