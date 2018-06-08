function users_loader(instructors) {
    let params = {"instructors": instructors};
    let users = make_request("users", params);

    // var users = [{"id" : 234, "first_name" : "Mona", "last_name" : "Rizvi"},
    //     {"id" : 12, "first_name" : "Ben", "last_name" : "Tyler"}];


    console.log(users);

    let $ul = $("<ul>").addClass("w3-border w3-round w3-ul");

    for (let i = 0; i < users.length; i++) {
        let user = users[i];
        let $li = $("<li>").addClass("w3-hoverable w3-hover-pale-green");
        $li.text(user["first_name"] + " " + user["last_name"]);

        let $button = $("<button>").addClass("w3-button w3-green w3-right w3-tiny");

        if (instructors === 0)
            $button.text("Grant instructor privileges");
        else
            $button.text("Take out instructor privileges");

        $button.click(function() {
            let params = {"user_id": user["id"]};
            if (instructors)
                params["instructor"] = 0;
            else
                params["instructor"] = 1;

            let result = make_request("grant_privilege", params);
            console.log(result);

            $("#instructor_list").empty();
            $("#student_list").empty();

            $("#instructor_list").append(users_loader(1));
            $("#student_list").append(users_loader(0));
        });

        $li.append($button);
        $ul.append($li);
    }

    console.log($ul);
    return $ul;
}