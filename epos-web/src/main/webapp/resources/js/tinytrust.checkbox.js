$(function () {
    //全选checkbox click事件
    $("#checkAll").click(function () {
        if ($(this).prop('checked')) {
            $(":checkbox").prop('checked', true)
        }
        else {
            $(":checkbox").removeProp('checked');//.removeAttr("checked");
        }
    });

    //checkbox click事件
    $(document).on('click', ":checkbox", function () {
        var chkTotalSize = $(":checkbox").not($("#checkAll")).size();//total num
        var checkedChkSize = $(":checkbox:checked").not($("#checkAll")).size();//checked num
        if (checkedChkSize == 0) {
            $("#checkAll").removeProp('checked');
        }
        else if (chkTotalSize == checkedChkSize) {
            $("#checkAll").prop("checked", "checked");
        }
        else {
            $("#checkAll").removeProp("checked");
        }
    });

});

//获得选中的checkbox的个数
var getCheckedNums = function () {
    var nums = $(":checkbox:checked").not($("#checkAll")).length;
    return nums;
};

//获得选中的checkbox值
var getCheckedValues = function () {
    var values = '';
    $(":checkbox:checked").not($("#checkAll")).each(function () {
        var aVal = $(this).val();
        if (values == '') {
            values += aVal;
        }
        else {
            values += ',' + aVal;
        }
    });
    return values;
};
