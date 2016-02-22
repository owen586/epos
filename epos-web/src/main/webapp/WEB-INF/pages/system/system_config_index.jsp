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
            $.ajax({
                type: 'POST',
                async: false,
                url: '/system/config/list',
                success: function (response) {
                    if (null != response && response.result == "success") {
                        var returnStr = '';
                        var dataMap = response.dataMap.systemConfigList;
                        $.each(dataMap, function (index, systemConfig) {
                            returnStr += "<tr>";
                            returnStr += "<td class='tcenter'>" + systemConfig.sysName + "</td>";
                            returnStr += "<td class='tcenter'>" + systemConfig.sysDesc + "</td>";
                            if (systemConfig.sysShowType == 1)//下拉列表
                            {
                                returnStr += "<td class='tcenter'>"
                                        + "<select id=value_" + systemConfig.sysCode + ">"
                                        + "<option value='1' " + (systemConfig.sysValue == 1 ? "selected=selected" : "") + ">开启</option>"
                                        + "<option value='2' " + (systemConfig.sysValue == 2 ? "selected=selected" : "") + ">关闭</option>"
                                        + "</select>"
                                        + "</td>";
                            }
                            else if (systemConfig.sysShowType == 2)//时间筛选框
                            {
                                //时间筛选
                                returnStr += "<td class=\'tcenter\'><input type=\'text\' onclick=WdatePicker({dateFmt:\'HH:mm:ss\'}) id=\'value_" + systemConfig.sysCode + "\' value=\'" + systemConfig.sysValue + "\'</td>";
                            }
                            else if (systemConfig.sysShowType == 3)//默认文本显示
                            {
                                returnStr += "<td class=\'tcenter\'><input type=\'text\' id=\'value_" + systemConfig.sysCode + "\' value=\'" + systemConfig.sysValue + "\'</td>";
                            }
                            returnStr += "<td class='tcenter'><a href=javascript:editSystemConfig('" + systemConfig.sysCode + "')>设置</a></td>";
                            returnStr += "</tr>";
                        });
                        if ('' == returnStr) {
                            returnStr = "<tr><td colspan='4' style='color:red;text-align:center'>无记录!</td></tr>";
                        }
                    }
                    else {
                        returnStr = "<tr><td colspan='4' style='color:red;text-align:center'>无记录!</td></tr>";
                    }

                    $("#tbodyList").html(returnStr);
                    initializeTableEffects();//增加样式
                }
            });
        });

        //编辑
        var editSystemConfig = function (systemCode) {
            parent.layer.confirm("确定修改吗?", {icon: 2, title: '提示'}, function (index) {
                var systemValue = $("#value_" + systemCode).val();
                $.ajax({
                    type: 'POST',
                    data: {systemCode: systemCode, systemValue: systemValue},
                    url: '/system/config/update',
                    success: function (response) {
                        if (null != response && response.result == "success") {
                            parent.layer.msg(response.message, {icon: 1, title: '提示'}, function (index) {
                                parent.layer.close(index);
                                location = '/system/config/index';
                            });
                        }
                        else {
                            parent.layer.msg(response.message, {icon: 2, title: '提示'}, function (index) {
                                parent.layer.close(index);
                            });
                        }
                    }
                });

                parent.layer.closeAll();
            });
        };
    </script>
</head>
<body>
<form id="myForm" method="post">
    <div class="pushT">
        <a href="javascript:void(0)">系统管理</a>
        &nbsp;&gt;&nbsp;
				<span> 
					系统配置
				</span>
    </div>
    <table class="tablelist">
        <tr>
            <th width="20%">
                名称
            </th>
            <th width="40%">
                描述
            </th>
            <th>
                配置值
            </th>
            <th width="10%">
                操作
            </th>
        </tr>
        <tbody id="tbodyList">
        </tbody>
    </table>
</form>
</body>
</html>