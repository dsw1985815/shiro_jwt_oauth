function gettoken() {
    var url = "http://localhost:8080/oauth/token";
    var loginPara = $("#loginPara").val();
    $.ajax({
        type: "POST",
        async: false,
        url: url,
        dataType: "html",
        contentType: "application/json",
        data: loginPara,
        success: function (data) {
            $("#tokendetail").html(data);
        } ,error: function(XMLHttpRequest, textStatus, errorThrown) {  //#3这个error函数调试时非常有用，如果解析不正确，将会弹出错误框
            alert(XMLHttpRequest.status);
            alert(XMLHttpRequest.readyState);
            alert(textStatus); // paser error;
        },
    })
}


function getresource() {
    var url = "http://localhost:8080"+$('#resource').val();
    var method = $('#method').val();
    var token = $('#token').val();
    var type = $('#type').val();

    $.ajax({
        type: method,
        async: false,
        url: url,
        dataType: "html",
        contentType: "application/json",
        beforeSend: function(request) {
            request.setRequestHeader("Authorization", type+" "+token);
        },
        data: {},
        success: function (data) {
            $("#userdetail").html(data);
        } ,error: function(XMLHttpRequest, textStatus, errorThrown) {  //#3这个error函数调试时非常有用，如果解析不正确，将会弹出错误框
            alert(XMLHttpRequest.status);
            alert(XMLHttpRequest.readyState);
            alert(textStatus); // paser error;
        },
    })
}

