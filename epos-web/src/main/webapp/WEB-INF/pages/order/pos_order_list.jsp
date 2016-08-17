<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/tinytrust/security" prefix="security" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>E-POS - 易珀斯</title>
    <link href="${cp}/resources/images/favicon.ico" rel="shortcut icon"/>
    <jsp:include page="/WEB-INF/pages/common/include.jsp"/>

    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="expires" content="0"/>
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/>
    <meta http-equiv="description" content="This is my page"/>
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
            $("#selectBtn").attr("disabled", "disabled");//查询按钮
            //设置"loading"效果
            $("#tbodyList").html('<tr align="center">'
                    + '<td colspan="16" class="red">'
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
            var url = "${cp}/order/pos/list";
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
                        $.each(dataMap.list, function (index, aOrder) {
                            //debugger;
                            returnStr += "<tr>";
                            returnStr += "<td class='tcenter'>"
                                    + "<input type='checkbox' name='subCheck' value='" + aOrder.orderId + "'/>"
                                    + "</td>";
                            /* returnStr += "<td class='tcenter'>"+(aOrder.orderSrc==1?"POS":aOrder.orderSrc==2?"拍拍":aOrder.orderSrc==3?"淘宝":"火星")+"</td>"; */
                            returnStr += "<td class='tcenter'>" + aOrder.orderCode + "</td>";
                            var type = aOrder.orderType;//订单类型
                            var typeClass = '';
                            var typeTxt = '';
                            if (type == 1) {
                                typeClass = 'orange';
                                typeTxt = '普'
                            }
                            else if (type == 2) {
                                typeClass = 'green';
                                typeTxt = '返'
                            }
                            returnStr += "<td class='tcenter " + typeClass + "'>" + typeTxt + "</td>";
                            var tranferType = aOrder.tranferType;
                            returnStr += "<td class='tcenter'>";
                            var transferName = '';
                            if (tranferType == 1) {
                                transferName = 'T+0';
                            }
                            else {
                                transferName = 'T+1';
                            }
                            returnStr += transferName + "</td>";
                            returnStr += "<td class='tcenter'>" + aOrder.terminalCode + "</td>";
                            //returnStr += "<td class='tcenter'>"+aOrder.userCode+"</td>";
                            returnStr += "<td class='tcenter'>" + aOrder.userName + "</td>";
                            returnStr += "<td class='tcenter'>" + aOrder.cellphone + "</td>";
                            returnStr += "<td class='tcenter'>" + aOrder.payBankCode + "</td>";
                            returnStr += "<td class='tcenter'>" + aOrder.outterUserCode + "</td>";
                            returnStr += "<td class='tcenter'>" + aOrder.orderMoney + "</td>";
                            returnStr += "<td class='tcenter'>" + aOrder.feeRate + "</td>";
                            returnStr += "<td class='tcenter'>" + aOrder.tradeMoney + "</td>";
                            returnStr += "<td class='tcenter'>" + timeFmt(aOrder.addDate) + "</td>";
                            //returnStr += "<td class='tcenter'>"+timeFmt(aOrder.shouldDealDate)+"</td>";
                            var status = aOrder.status;//订单状态
                            var statusClass = '';
                            var statusTxt = '';
                            if (status == -1) {
                                statusClass = 'orange';
                                statusTxt = '待核实'
                            }
                            else if (status == 1) {
                                statusClass = 'orange';
                                statusTxt = '待处理'
                            }
                            else if (status == 2) {
                                statusClass = 'orange';
                                statusTxt = '处理中'
                            }
                            else if (status == 4) {
                                statusClass = 'green';
                                statusTxt = '充值成功'
                            }
                            else if (status == 3) {
                                statusClass = 'red';
                                statusTxt = '充值失败'
                            }
                            returnStr += "<td class='tcenter " + statusClass + "'>" + statusTxt + "</td>";
                            returnStr += "<td class='tcenter'>" + timeFmt(aOrder.indeedDealDate) + "</td>";
                            returnStr += "<td class='tcenter'>" + (aOrder.memo != null ? aOrder.memo : "") + "</td>";
                            returnStr += "</tr>";
                        });

                        if ('' == returnStr) {
                            returnStr = "<tr><td colspan='14' style='color:red;text-align:center'>无记录!</td></tr>";
                        }
                    }
                    else {
                        returnStr = "<tr><td colspan='14' style='color:red;text-align:center'>无记录!</td></tr>";
                    }

                    $("#tbodyList").html(returnStr);

                    initializeTableEffects();//增加样式
                    installPagination(total);//分页绑定
                    $("#selectBtn").removeAttr("disabled");
                }
            };

            $("#myForm").ajaxForm(options);
            //表单提交
            $("#myForm").submit();
        });

    </script>
</head>

<body>
<form id="myForm" method="post">
    <div class="pushT">
        <a href="javascript:void(0)">订单管理</a>
        &nbsp;&gt;&nbsp;
				<span>
					POS订单列表
				</span>
    </div>
    <input type="hidden" name="pageNo" id="pageNo" value="0"/>

    <div class="block pushT">
        <ul class="form column3">
            <li>
                <label>
                    订单类型:
                </label>
                <select name="orderType" id="orderType" class="auto">
                    <option value="-1">全部</option>
                    <option value="1">普通订单</option>
                    <option value="2">返点订单</option>
                </select>
            </li>
            <li>
                <label>
                    订单编号:
                </label>
                <input type="text" name="orderCode" id="orderCode" class="middle"/>
            </li>
            <li>
                <label>
                    到账类型:
                </label>
                <select id="tranferType" name="tranferType">
                    <option value="-1">全部</option>
                    <option value="1">T+0</option>
                    <option value="2">T+1</option>
                </select>
            </li>
            <security:admin>
                <li>
                    <label>
                        终端编号:
                    </label>
                    <input type="text" name="terminalCode" id="terminalCode" class="middle"/>
                </li>
                <!-- li>
                <label>
                用户编号:
                </label>
                <input type="text" name="userCode" id="userCode" class="middle" />
                </li -->
                <li>
                    <label>
                        外部用户编号:
                    </label>
                    <input type="text" name="outterUserCode" id="outterUserCode" class="middle"/>
                </li>
                <li>
                    <label>
                        用户名称:
                    </label>
                    <input type="text" name="userName" id="userName" class="middle"/>
                </li>
                <li>
                    <label>
                        手机号:
                    </label>
                    <input type="text" name="cellphone" id="cellphone" class="middle"/>
                </li>
            </security:admin>
            <li>
                <label>
                    订单状态:
                </label>
                <select name="status" id="status" class="auto">
                    <option value="-999">全部</option>
                    <option value="-1">待核实</option>
                    <option value="1">待处理</option>
                    <option value="2">处理中</option>
                    <option value="3">处理失败</option>
                    <option value="4">处理成功</option>
                </select>
            </li>
            <li>
                <label> 入库起始时间: </label>
                <input type="text" name="startTime" id="startTime" class="middle"
                       onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:180px"/>
            </li>
            <li>
                <label> 入库结束时间: </label>
                <input type="text" name="endTime" id="endTime" class="middle"
                       onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:180px"/>
            </li>
        </ul>
        <div class="tcenter">
            <button type="button" id="selectBtn"
                    onclick="pageSelectCallback(0);">
                查询
            </button>
        </div>
    </div>
    <table class="tablelist">
        <tr>
            <th width="4%">
                <input type="checkbox" name="checkAll" id="checkAll"/>
            </th>
            <th>
                订单<br/>编号
            </th>
            <th>
                订单<br/>类型
            </th>
            <th>
                到账<br/>类型
            </th>
            <th>
                终端<br/>编号
            </th>
            <!-- th>
                用户编号
            </th -->
            <th>
                用户<br/>名称
            </th>
            <th>
                手机号
            </th>
            <th>
                支付<br/>卡号
            </th>
            <th>
                外部<br/>用户编号
            </th>
            <th>
                订单<br/>金额
            </th>
            <th>
                到账<br/>费率‱
            </th>
            <th>
                结算<br/>金额
            </th>
            <th>
                入库<br/>时间
            </th>
            <!-- 					<th>
                                    应处理时间
                                </th> -->
            <th>
                订单<br/>状态
            </th>
            <th>
                实际<br/>处理时间
            </th>
            <th>
                备忘
            </th>
        </tr>
        <tbody id="tbodyList"></tbody>
    </table>
    <!-- 分页信息block -->

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