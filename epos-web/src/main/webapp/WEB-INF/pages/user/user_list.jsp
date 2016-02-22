<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/tinytrust/security" prefix="security" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>E-POS - 易珀斯</title>
    <link href="${cp}/resources/images/favicon.ico" rel="shortcut icon"/>
    <jsp:include page="/WEB-INF/pages/common/include.jsp"/>
    <script type="text/javascript">
        var wd_current_page = 0;//当前页
        //初始化分页组件
        var installPagination = function (total) {
            //加入分页的绑定
            $("#Pagination").pagination(total, {
                callback: pageSelectCallback,
                items_per_page: $("#pageSize").val(),//每页显示的记录数
                num_display_entries: 4,//连续分页主体部分显示的分页条目数
                current_page: wd_current_page,//当前选中的页面
                num_edge_entries: 2//两侧显示的首尾分页的条目数
            });

            $("#Pagination").find("a").each(function () {
                $(this).attr("href", "javaScript:void(0)");
            });
        };

        var pageSelectCallback = function (page_id, jq) {
            $("#submitBtn").attr("disabled", "disabled");//查询按钮
            //设置"loading"效果
            $("#tbodyList").html('<tr align="center">'
                    + '<td colspan="9" class="red">'
                    + '<img src="${cp}/resources/images/loading.gif" style="margin: 0pt auto;" align="center">'
                    + '</td>'
                    + '</tr>');

            wd_current_page = page_id;
            $("#pageNo").val(wd_current_page);

            //表单提交
            $("#myForm").submit();
        };

        $(function () {
            var total = 0;
            var url = "${cp}/user/list";
            var options = {
                type: "POST",
                url: url,
                dataType: "json",
                success: function (responseData) {
                    if (responseData != null && responseData.result == "success") {
                        var dataMap = responseData.dataMap;
                        total = dataMap.count;//总记录数
                        $("#totalCount").text(total);
                        var returnStr = '';
                        $.each(dataMap.list, function (index, aUser) {
                            returnStr += "<tr>";
                            returnStr += "<td class='tcenter'><input type='checkbox' name='subCheck' value='" + aUser.userCode + "'/></td>";
                            returnStr += "<td class='tcenter'>" + aUser.userCode + "</td>";
                            returnStr += "<td class='tcenter'>" + aUser.cellphone + "</td>";
                            returnStr += "<td class='tcenter'>" + aUser.userName + "</td>";
                            returnStr += "<td class='tcenter'>";
                            var roleName = '';
                            if (aUser.roleCode == 1) {
                                roleName = '管理员';
                            }
                            else if (aUser.roleCode == 2) {
                                roleName = '经销商'
                            }
                            else {
                                roleName = '直销商'
                            }
                            returnStr += roleName + "</td>";
                            returnStr += "<td class='tcenter'>";
                            var transferName = '';
                            if (aUser.tranferType == 1) {
                                transferName = 'T+0';
                            }
                            else {
                                transferName = 'T+1';
                            }
                            returnStr += transferName + "</td>";
                            returnStr += "<td class='tcenter'>" + aUser.topUserCode + "</td>";
                            returnStr += "<td class='tcenter'>" + (aUser.outterUserCode == null ? "" : aUser.outterUserCode) + "</td>";
                            var status = aUser.status;//用户状态
                            var statusClass = '';
                            var statusTxt = '';
                            if (status == 1) {
                                statusClass = 'orange';
                                statusTxt = '待审核'
                            }
                            else if (status == 2) {
                                statusClass = 'green';
                                statusTxt = '正常'
                            }
                            else if (status == 3) {
                                statusClass = 'red';
                                statusTxt = '停用'
                            }
                            returnStr += "<td class='tcenter " + statusClass + "'>" + statusTxt + "</td>";
                            returnStr += "</tr>";
                        });

                        if ('' == returnStr) {
                            returnStr = "<tr><td colspan='9' style='color:red;text-align:center'>无记录!</td></tr>";
                        }
                    }
                    else {
                        returnStr = "<tr><td colspan='9' style='color:red;text-align:center'>无记录!</td></tr>";
                    }

                    $("#tbodyList").html(returnStr);

                    initializeTableEffects();//增加样式
                    installPagination(total);//分页绑定
                    $("#submitBtn").removeAttr("disabled");
                }
            };

            $("#myForm").ajaxForm(options);
            //表单提交
            $("#myForm").submit();
        });

        <security:security menuPath="/user/index/add">
        //新增用户
        var add = function () {
            location = "/user/user_edit_index";
        };
        </security:security>

        <security:security menuPath="/user/index/edit">
        //编辑用户
        var edit = function () {
            var checkedNum = this.getCheckedNums();
            if (checkedNum != 1) {
                parent.layer.alert("请选择一条记录", {icon: 2, title: '提示'}, function (index) {
                    parent.layer.close(index);
                });
                return;
            }
            var checkedValues = this.getCheckedValues();
            location = "/user/user_edit_index?userCode=" + checkedValues;
        };
        </security:security>
        <security:security menuPath="/user/index/verify">
        //审核通过
        var verify = function () {
            var checkedNum = this.getCheckedNums();
            if (checkedNum != 1) {
                parent.layer.alert("请选择一条记录", {icon: 2, title: '提示'}, function (index) {
                    parent.layer.close(index);
                });
                return;
            }
            var checkedValues = this.getCheckedValues();
            userStatusAjax(2, checkedValues);
        };
        </security:security>
        <security:security menuPath="/user/index/lock">
        //锁定
        var lock = function () {
            var checkedNum = this.getCheckedNums();
            if (checkedNum != 1) {
                parent.layer.alert("请选择一条记录", {icon: 2, title: '提示'}, function (index) {
                    parent.layer.close(index);
                });
                return;
            }
            var checkedValues = this.getCheckedValues();
            userStatusAjax(3, checkedValues);
        };
        </security:security>
        <security:security menuPath="/user/index/unlock">
        //解锁
        var unlock = function () {
            var checkedNum = this.getCheckedNums();
            if (checkedNum != 1) {
                parent.layer.alert("请选择一条记录", {icon: 2, title: '提示'}, function (index) {
                    parent.layer.close(index);
                });
                return;
            }
            var checkedValues = this.getCheckedValues();
            userStatusAjax(2, checkedValues);
        };
        </security:security>
        <security:security menuPath="/user/terminal/index/add">
        //终端配置
        var terminalCfg = function () {
            var checkedNum = this.getCheckedNums();
            if (checkedNum != 1) {
                parent.layer.alert("请选择一条记录", {icon: 2, title: '提示'}, function (index) {
                    parent.layer.close(index);
                });
                return;
            }
            var checkedValues = this.getCheckedValues();
            location = "/user/terminal/edit/index?userCode=" + checkedValues;
        };
        </security:security>
        <security:security menuPath="/user/status/edit">
        //异步编辑用户状态
        var userStatusAjax = function (status, userCode) {
            var url = "/user/state_edit/" + status + "/" + userCode;
            $.ajax({
                type: 'POST',
                async: false,//同步
                url: url,
                success: function (response) {
                    if (null != response && response.result == "success") {
                        parent.layer.msg(response.message, {icon: 1, title: '提示'}, function (index) {
                            pageSelectCallback($("#pageNo").val());
                            parent.layer.close(index);
                        });
                    }
                    else {
                        parent.layer.msg(response.message, {icon: 2, title: '提示'}, function (index) {
                            parent.layer.close(index);
                        });
                    }
                }
            });
        };
        </security:security>
        $(function () {
            //初始化角色
            $.ajax({
                type: 'POST',
                async: false,//同步
                url: '${cp}/user/role/all',
                success: function (response) {
                    if (null != response && response.result == "success") {
                        var roleOptions = "";
                        $.each(response.dataMap.roleList, function (index, aRole) {
                            roleOptions += "<option value='" + aRole.roleCode + "'>" + aRole.roleName + "</option>"
                        });
                        $("#roleCode").append(roleOptions);
                    }
                    else {
                        parent.layer.msg(response.message, {icon: 2, title: '提示'}, function (index) {
                            parent.layer.close(index);
                        });
                    }
                }
            });
        });
    </script>
</head>

<body>
<form id="myForm" method="post">
    <div class="pushT">
        <a href="javascript:void(0)">会员管理</a>
        &nbsp;&gt;&nbsp;
				<span> 
					用户列表
				</span>
    </div>
    <input type="hidden" name="pageNo" id="pageNo" value="0"/>

    <div class="block pushT">
        <ul class="form column3">
            <li>
                <label>
                    手机号:
                </label>
                <input type="text" name="cellphone" id="cellphone" class="middle"/>
            </li>
            <li>
                <label>
                    用户名称:
                </label>
                <input type="text" name="userName" id="userName" class="middle"/>
            </li>
            <li>
                <label>
                    上级编号:
                </label>
                <input type="text" name="topUserCode" id="topUserCode" class="middle"/>
            </li>
            <li>
                <label>
                    外部编号:
                </label>
                <input type="text" name="outterUserCode" id="outterUserCode" class="middle"/>
            </li>
            <li>
                <label>
                    到账类型:
                </label>
                <select name="tranferType" id="tranferType">
                    <option value="-1">全部</option>
                    <option value="1">T+0</option>
                    <option value="2">T+1</option>
                </select>
            </li>
            <li>
                <label>
                    用户状态:
                </label>
                <select name="status" id="status">
                    <option value="-1">全部</option>
                    <option value="1">待审核</option>
                    <option value="2">正常</option>
                    <option value="3">停用</option>
                </select>
            </li>
            <security:admin>
                <li>
                    <label>
                        用户角色:
                    </label>
                    <select name="roleCode" id="roleCode">
                        <option value="-1">全部</option>
                    </select>
                </li>
            </security:admin>
        </ul>
        <div class="tcenter">
            <button type="button" id="submitBtn" onclick="pageSelectCallback(0);">
                查询
            </button>
        </div>
    </div>
    <!-- 各种消息样式 -->
    <!--
    <div id="message" class="block important pushT green">important</div>
    <div id="message" class="block warning pushT green">warning</div>
    <div id="message" class="block remark pushT green">remark</div>
    <div id="message" class="block attention pushT green">attention</div>
    <div id="message" class="block info pushT green">info</div>
    <div id="message" class="block tip pushT green">tip</div>
    -->
    <section class="tableToolbar pushT clearfix">
        <ul class="ToolbarL">
            <security:security menuPath="/user/index/add">
                <li><a href="javascript:void(0);" onclick="javascript:add();">新增</a></li>
            </security:security>
            <security:security menuPath="/user/index/edit">
                <li><a href="javascript:void(0);" onclick="javascript:edit();">编辑</a></li>
            </security:security>
            <security:security menuPath="/user/index/verify">
                <li><a href="javascript:void(0);" onclick="javascript:verify();">审核通过</a></li>
            </security:security>
            <security:security menuPath="/user/index/lock">
                <li><a href="javascript:void(0);" onclick="javascript:lock();">锁定</a></li>
            </security:security>
            <security:security menuPath="/user/index/unlock">
                <li><a href="javascript:void(0);" onclick="javascript:unlock();">解锁</a></li>
            </security:security>
            <security:security menuPath="/user/terminal/index/add">
                <li><a href="javascript:void(0);" onclick="javascript:terminalCfg();">POS终端配置</a></li>
            </security:security>
        </ul>
    </section>
    <table class="tablelist">
        <tr>
            <th width="10%">
                <input type="checkbox" name="checkAll" id="checkAll"/>全选
            </th>
            <th>
                用户编号
            </th>
            <th>
                手机号
            </th>
            <th>
                用户名
            </th>
            <th>
                角色
            </th>
            <th>
                到账类型
            </th>
            <th>
                上级编号
            </th>
            <th>
                外部商家编号
            </th>
            <th>
                用户状态
            </th>
        </tr>
        <tbody id="tbodyList"></tbody>
    </table>

    <section class="block tablePageBar">
        <div class="tableListR">共<samp id="totalCount">0</samp>条</div>
        <div class="tableListL">
            <strong>每页显示数量：</strong>
            <select id="pageSize" name="pageSize" onchange="pageSelectCallback(0);">
                <option selected="selected" value="10">10</option>
                <option value="30">30</option>
                <option value="50">50</option>
            </select>
        </div>
        <div id="pages">
            <div class="pagination" id="Pagination"></div>
        </div>
    </section>
</form>
</body>
</html>