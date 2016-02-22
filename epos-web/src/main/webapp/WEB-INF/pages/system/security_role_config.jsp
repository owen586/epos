<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="cp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>TinyTrust - a tiny trust can make world beautiful</title>
    <link href="/resources/images/favicon.ico" rel="shortcut icon"/>
    <link rel="stylesheet"
          href="/resources/js/zTree/zTreeStyle/zTreeStyle.css"/>
    <script src="/resources/js/jquery-1.9.1.min.js"></script>
    <script src="/resources/js/zTree/jquery.ztree.all-3.5.min.js"></script>
    <script>
        var setting = {
            check: {
                enable: true
            },
            data: {
                simpleData: {
                    enable: true
                }
            }
        };

        //根据角色编号初始化菜单树
        var initMenuTree = function (roleCode) {
            $.ajax({
                type: 'POST',
                url: '/authority/role/' + roleCode,
                success: function (response) {
                    if (null != response && response.result == "success") {
                        var zTreeData = response.dataMap.treeNodeList;
                        $.fn.zTree.init($("#roleTree"), setting, zTreeData);
                    }
                }
            });
        };

        //获取选中的菜单编号
        var getCheckedMenu = function () {
            var zTree = $.fn.zTree.getZTreeObj("roleTree");
            var nodes = zTree.getCheckedNodes(true);
            var menuCodes = "";
            for (var i = 0, j = nodes.length; i < j; i++) {
                if (menuCodes == "") {
                    menuCodes += nodes[i].id + "#" + nodes[i].url;
                }
                else {
                    menuCodes += "|" + nodes[i].id + "#" + nodes[i].url;
                }
            }
            return menuCodes;
        };

        initMenuTree('${roleCode}');
    </script>
</head>
<body>
<div>
    <ul id="roleTree" class="ztree"></ul>
</div>
</body>
</html>

