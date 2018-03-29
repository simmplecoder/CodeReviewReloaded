function assignments_loader(course_id) {
//    var params = { "username" : "ibro@nu.edu.kz", "id" : course_id };
//  var request = $.ajax({
//        type: "POST",
//        url: "services/assignments",
//        contentType: "application/json",
//        async: false,
//        data: JSON.stringify(params)
//    });
//  
//  let assignments;
//    request.success(function (data, textStatus, xhr) {
//          console.log(data);
//          assignments = JSON.parse(data);
//    });
   
    
    let assignments = [{"title" : "Implement Python code", "description" : "implement in a fast way", "id" : "id001"}, 
        {"title" : "Implement Python file", "description" : "code in a fast way", "id" : "id001"}];
    
    let $divmain = $("<div>");
    
    for (let i = 0; i < assignments.length; i++) {
        let assignment = assignments[i];
        let $adiv = $("<div>");

        let $button = $("<button>").addClass("w3-btn w3-block w3-border w3-left-align");
        $button.text(assignment["title"]);
        $button.appendTo($adiv);
        
        let $infoDiv = $("<div>").addClass("w3-container w3-hide w3-padding-24").appendTo($adiv);
        $infoDiv.text(assignment["description"]);
        
    
        $button.click(function() {  
            let $div2 = $infoDiv;
            let $uploadView = getUploadView();
            $infoDiv.append($uploadView);

            if ($div2.hasClass("w3-hide")) {
                $div2.removeClass("w3-hide");
                $div2.addClass("w3-show");
            } else {
                $div2.addClass("w3-hide");
                $div2.removeClass("w3-show");
            }
        });


        $divmain.append($adiv)
    }
    
    return $divmain;
}
