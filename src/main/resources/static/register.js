$(function(){
    //表单转json函数
    $.fn.serializeObject = function(){
        var o = {};
        var a = this.serializeArray();
        $.each(a, function() {
            if (o[this.name] !== undefined) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };

    $('#adduser_btn').click(function() {
        url = "http://localhost:8080/user/adduser";
        $.ajax({
            type: "POST",
            async: false,
            url: url,
            dataType: "html",
            contentType: "application/json",
            data: JSON.stringify($('#registerform').serializeObject()),
            success: function (data) {
                alert(data);
                $("#loginpage").html(data);
            }, error: function (XMLHttpRequest, textStatus, errorThrown) {
                console.log("错误状态：" + XMLHttpRequest.status + "," + XMLHttpRequest.readyState + "," + textStatus);
            }
        });
    });

    $('#login_btn').click(function() {
        url = "http://localhost:8080/user/login";
        $.ajax({
            type: "POST",
            async: false,
            url: url,
            dataType: "html",
            contentType: "application/json",
            data: JSON.stringify($('#registerform').serializeObject()),
            success: function (data) {
                $("#loginpage").html(data);
            }, error: function (XMLHttpRequest, textStatus, errorThrown) {
                console.log("错误状态：" + XMLHttpRequest.status + "," + XMLHttpRequest.readyState + "," + textStatus);
            }
        });
    });
});