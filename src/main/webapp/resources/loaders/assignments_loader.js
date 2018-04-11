function assignments_loader(course_id) {
    let params = {"id" : course_id};
    let assignments = make_request("assignments", params);

   // let assignments = [{"title" : "Implement Python code", "description" : "implement in a fast way", "id" : "id001"}, 
       // {"title" : "Implement Python file", "description" : "code in a fast way", "id" : "id001"}];
    
    let $divmain = $("<div>");
    
    for (let i = 0; i < assignments.length; i++) {
        let assignment = assignments[i];
        let $adiv = $("<div>");

        let $button = $("<button>").addClass("w3-btn w3-block w3-border w3-left-align");
        $button.text(assignment["title"]);
        $button.appendTo($adiv);
        
        let $infoDiv = $("<div>").addClass("w3-container w3-hide w3-padding-24").appendTo($adiv);
    
        $button.click(function() {
            let description = assignment["description"];
            let $div2 = $infoDiv;
            
            $div2.empty();
            $div2.text(assignment["description"]);
            let $uploadView = getUploadView(assignment["id"]);
            $div2.append($uploadView);

            toggle($div2);
        });

        $divmain.append($adiv)
    }
    
    return $divmain;
}