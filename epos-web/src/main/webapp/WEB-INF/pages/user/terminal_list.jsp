<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
                    + '<td colspan="5" class="red">'
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
            var url = "${cp}/user/terminal/list";
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
                        $.each(dataMap.list, function (index, terminal) {
                            returnStr += "<tr>";
                            returnStr += "<td class='tcenter'><input type='checkbox' name='subCheck' value='" + terminal.userCode + "'/></td>";
                            returnStr += "<td class='tcenter'>" + terminal.terminalCode + "</td>";
                            returnStr += "<td class='tcenter'>" + terminal.cellphone + "</td>";
                            returnStr += "<td class='tcenter'>" + terminal.userCode + "</td>";
                            returnStr += "<td class='tcenter'>" + terminal.userName + "</td>";
                            returnStr += "</tr>";
                        });

                        if ('' == returnStr) {
                            returnStr = "<tr><td colspan='5' style='color:red;text-align:center'>无记录!</td></tr>";
                        }
                    }
                    else {
                        returnStr = "<tr><td colspan='5' style='color:red;text-align:center'>无记录!</td></tr>";
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

        //终端配置
        var edit = function () {
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

        //删除终端
        var del = function () {
            var checkedNum = this.getCheckedNums();
            if (checkedNum != 1) {
                parent.layer.alert("请选择一条记录", {icon: 2, title: '提示'}, function (index) {
                    parent.layer.close(index);
                });
                return;
            }
            var checkedValues = this.getCheckedValues();
            parent.layer.confirm("确定删除吗?", {icon: 2, title: '提示'}, function (index) {
                $.ajax({
                    type: 'POST',
                    async: false,//同步
                    url: '${cp}/user/terminal/delete/' + checkedValues,
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

            });

        };
    </script>
</head>

<body>
<form id="myForm" method="post">
    <div class="pushT">
        <a href="javascript:void(0)">POS终端管理</a>
        &nbsp;&gt;&nbsp;
				<span> 
					终端列表
				</span>
    </div>
    <input type="hidden" name="pageNo" id="pageNo" value="0"/>

    <div class="block pushT">
        <ul class="form column3">
            <li>
                <label>
                    终端编号:
                </label>
                <input type="text" name="terminalCode" id="terminalCode" class="middle"/>
            </li>
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
                    用户编号:
                </label>
                <input type="text" name="userCode" id="userCode" class="middle"/>
            </li>
        </ul>
        <div class="tcenter">
            <button type="button" id="submitBtn" onclick="pageSelectCallback(0);">
                查询
            </button>
        </div>
    </div>
    <section class="tableToolbar pushT clearfix">
        <ul class="ToolbarL">
            <li><a href="javascript:void(0);" onclick="javascript:edit();">编辑</a></li>
            <li><a href="javascript:void(0);" onclick="javascript:del();">删除</a></li>
        </ul>
    </section>
    <div id="message" class="hide pushT"></div>
    <table class="tablelist">
        <tr>
            <th width="10%">
                <input type="checkbox" name="checkAll" id="checkAll"/>全选
            </th>
            <th>
                终端编号
            </th>
            <th>
                手机号
            </th>
            <th>
                用户编号
            </th>
            <th>
                用户名称
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