function loader(where, action, id) {

    $("#course_list").empty();
    if(action == "load_submissions")
        $("#course_list").append(load_submissions(id));

    if(action == "load_courses")
        $("#course_list").append(load_courses(id));

}
function load_courses(id) {

    // // get sumbissions json
    // $.ajax({
    //     type: "GET",
    //     url: "services/getSubmissions",          // SHOULD Be Changed
    //     success: function(data){
    //         submissions = data;
    //     },
    // });  

    let courses = [
     {"title": "Programming in C", "course_id" : "id1"}, 
     {"title": "Computational Physics", "course_id" : "id2"},
     {"title": "Operating Systems", "course_id" : "id3"},
     {"title": "Performance and Data Structures", "course_id" : "id4"}
    ];

    return createUl("title", courses);
}


function load_submissions(id) {

    // // get sumbissions json
    // $.ajax({
    //     type: "GET",
    //     url: "services/getSubmissions",          // SHOULD Be Changed
    //     success: function(data){
    //         submissions = data;
    //     },
    // });  

    // let courses = [
    //  {"title": "Programming in C", "course_id" : "id1"}, 
    //  {"title": "Computational Physics", "course_id" : "id2"},
    //  {"title": "Operating Systems", "course_id" : "id3"},
    //  {"title": "Performance and Data Structures", "course_id" : "id4"}
    // ];
    return createUl("date", submissions["submissions"]);
}

function createUl(titleKey, data) {
    console.log(data);

    let $ul = $("<ul>").addClass("w3-ul w3-hoverable");
    
    for (let i = 0; i < data.length; i++) {
     let course = data[i];
     let $li = $("<li>");
     $li.addClass("w3-padding-large w3-hover-pale-green");
     $li.text(course[titleKey]);
     $li.click(function() {
         let id = data["id"];
         assignments_loader(id, "#course_list");
     });
        
     $ul.append($li);
    }

    return $ul;
}