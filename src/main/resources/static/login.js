$(function(){
    $('#token_btn').click(function() {
        var url = "http://localhost:8080/oauth/token";
        $.ajax({
            type: "POST",
            async: false,
            url: url,
            dataType: "html",
            contentType: "application/json",
            data: JSON.stringify($('#loginform').serializeObject()),
            success: function (data) {
                $("#tokendetail").html(data);
            }, error: function (XMLHttpRequest, textStatus, errorThrown) {
                console.log("错误状态：" + XMLHttpRequest.status + "," + XMLHttpRequest.readyState + "," + textStatus);
            }
        });
    });
});