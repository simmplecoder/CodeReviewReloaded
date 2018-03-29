function assignments_loader(course_id) {
//    var params = { "username" : "ibro@nu.edu.kz", "id" : course_id };
//	var request = $.ajax({
//        type: "POST",
//        url: "services/assignments",
//        contentType: "application/json",
//        async: false,
//        data: JSON.stringify(params)
//    });
//	
//	let assignments;
//    request.success(function (data, textStatus, xhr) {
//    		console.log(data);
//    		assignments = JSON.parse(data);
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
        
        $("#assignment_list").append($button);
     
        let $div = $("<div>").addClass("w3-container w3-hide w3-padding-24").text(assignment["description"]);
        
//      MARK: - File Uploading
        let $input = $("<input id='home_input' type='file' multiple>");
        var filesStr = [];

        $input.change( function() {
            var files = this.files;
            filesStr = [];

            console.log($(this).parent());
            for (var i = 0; i < files.length; i++) {
                	// console.log(files[i].name.split('.').pop());
                    var reader = new FileReader();
                    reader.onload = (function(reader) {
                        return function() {
                        filesStr.push(reader.result);
                    };
                }) (reader);
                
                console.log(reader.readAsText(files[i]));
            }
            console.log(files);
        });

        $div.append($("<br/>"));
        $div.append($input);
        $div.append($("<br/>"));
    
        $button.click(function() {  
            let $div2 = $div;
            if ($div2.hasClass("w3-hide")) {
                $div2.removeClass("w3-hide");
                $div2.addClass("w3-show");
            } else {
                $div2.addClass("w3-hide");
                $div2.removeClass("w3-show");
            }
        });

        let $uploadButton = $("<button>").text("Upload").click(function() {
            let uploadedFilesJson = {
                "files": filesStr,
            }

            $submissionsList.empty();
            $submissionsList = getSubmissions();
            $div.append($submissionsList);

            // Should be implemented, when backend will be ready
            // $.ajax({
            //     type: "POST",
            //     url: "services/UPLOAD",          // SHOULD Be Changed
            //     contentType: "application/json",
            //     data: JSON.stringify(uploadedFilesJson), 
            //     success: function(result){
            //         console.log("Success");
            //     },
            //     error: function() {
            //         alert("Failed to load files.")
            //     },
            // });

        });
        
        $div.append($uploadButton);

        let $submissionsList = getSubmissions();
        $div.append($submissionsList);

        $("#assignment_list").append($div);
    }
    
    return $divmain;
}