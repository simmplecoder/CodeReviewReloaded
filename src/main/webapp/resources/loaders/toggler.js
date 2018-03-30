function toggle() {
    var $div = arguments[0];

    if ($div.hasClass("w3-hide")) {
        $div.removeClass("w3-hide");
        $div.addClass("w3-show");

        if (arguments.length > 1)
            arguments[1].addClass("w3-text-green");
    } else {
        $div.addClass("w3-hide");
        $div.removeClass("w3-show");

        if (arguments.length > 1)
            arguments[1].removeClass("w3-text-green");
    }
}