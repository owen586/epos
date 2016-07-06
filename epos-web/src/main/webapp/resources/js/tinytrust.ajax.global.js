$(function () {

    //ajax全局设置
    $.ajaxSetup({
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        complete: function (XMLHttpRequest, textStatus) {
            var sessionstatus = XMLHttpRequest.getResponseHeader("sessionstatus"); //通过XMLHttpRequest取得响应头，sessionstatus，
            if (sessionstatus == "timeout") {
                parent.layer.msg("登录超时,请重新登录",{shade:0.5,time:800});
                //如果超时就处理 ，指定要跳转的页面
                setTimeout(function(){
                    parent.window.location.replace("/index.html");
                },1000);
            }
        }
    });

});