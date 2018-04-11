function make_request(url, params) {
    var request = $.ajax({
        type: "POST",
        url: "services/" + url,
        contentType: "application/json",
        async: false,
        data: JSON.stringify(params)
    });

    var result = [];
    request.success(function (response) {

    console.log("Hello")
        result = response;
    });

    return result;
}