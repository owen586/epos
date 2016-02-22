//删除字符串中所有空格
var trim = function (str) {
    return str.replace(/\s*/g, "");
};

//删除字符串前后的空格
var trim_ = function (str) {
    return str.replace(/(^\s*)|(\s*$)/g, "");
};

//字符串函数_endWith
String.prototype.endWith = function (str) {
    if (str == null || str == "" || this.length == 0 || str.length > this.length)
        return false;
    if (this.substring(this.length - str.length) == str)
        return true;
    else
        return false;
    return true;
};

//字符串函数_startWith
String.prototype.startWith = function (str) {
    if (str == null || str == "" || this.length == 0 || str.length > this.length)
        return false;
    if (this.substr(0, str.length) == str)
        return true;
    else
        return false;
    return true;
};

//初始表格效果
var initializeTableEffects = function () {
    $(".tablelist tr:even").addClass("even");
    $(".tablelist tr th:last-child").addClass("last");
    $(".tablelist tr td:last-child").addClass("last");

    $(".tablelist tr").hover(function () {
        $(this).addClass("hover");
    }, function () {
        $(this).removeClass("hover");
    });
};

//是否是数字
var isDigit = function (s) {
    var patrn = /^[0-9]{1,10}$/;
    if (!patrn.exec(s))
        return false;
    return true;
};

//回车提交表单
$(document).keyup(function (event) {
    if (event.keyCode == 13) {
        $("#submitBtn").trigger("click");
    }
});

//日期格式化
Date.prototype.format = function (format) {
    var date = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S+": this.getMilliseconds()
    };
    if (/(y+)/i.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
    }
    for (var k in date) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1
                ? date[k] : ("00" + date[k]).substr(("" + date[k]).length));
        }
    }
    return format;
}


//时间格式化
var timeFmt = function (aDate) {
    if (null == aDate) {
        return "";
    }
    else {
        return new Date(aDate).format("yyyy-MM-dd hh:mm:ss");
    }
};