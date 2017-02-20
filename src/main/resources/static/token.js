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

    $('#resource_btn').click(function() {
        url = "http://localhost:8080" + $('#resource').val();
        var method = $('#method').val();
        var token = $('#token').val();
        var type = $('#type').val();
        $.ajax({
            type: method,
            async: false,
            url: url,
            dataType: "html",
            contentType: "application/json",
            beforeSend: function (request) {
                request.setRequestHeader("Authorization", type + " " + token);
            },
            data: {},
            success: function (data) {
                $("#userdetail").html(data);
            }, error: function (XMLHttpRequest, textStatus, errorThrown) {
                console.log("错误状态：" + XMLHttpRequest.status + "," + XMLHttpRequest.readyState + "," + textStatus);
            }
        });
    });
});