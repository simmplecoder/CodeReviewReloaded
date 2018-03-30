function items_loader(url, params) {
    var request = $.ajax({
        type: "POST",
        url: "services/" + url,
        contentType: "application/json",
        async: false,
        data: JSON.stringify(params)
    });

    var result = [];
    request.success(function (response) {
        result = response;
    });

    return result;
}