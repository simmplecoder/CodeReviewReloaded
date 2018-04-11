function getUploadView(assignment_id) {

	let $uploadView = $("<div>");

	//      MARK: - File Uploading
    let $input = $("<input id='home_input' type='file' multiple>").appendTo($uploadView);
    var filesStr = [];

    $input.change( function() {
        var files = this.files;
        filesStr.length = 0
        var filesToUpload = [];

        for (let i = 0; i < files.length; i++) {
                var content = "";
                // console.log(files[i].name.split('.').pop());
                var reader = new FileReader();
                reader.onload = (function(reader) {
                    return function(i) {
                        filesToUpload.push({"filename" : files[i], "content" : reader.result});
                    }
                }) (reader);

                reader.readAsText(files[i]);
        }
        console.log(files)
        console.log(filesToUpload);
        for(let i = 0; i < files.length; i++) {
            console.log(i);
            console.log(filesToUpload[i]);
            // console.log(filesToUpload["0"]);
            //["filename"] = files[i].name;
            console.log(files[i].name);
        }
        console.log(filesToUpload);
    });

    $uploadView.append($("<br/>"));
    $uploadView.append($input);
    $uploadView.append($("<br/>"));

    let $submissionsList = submissions_loader(assignment_id);
    
    $uploadView.append(getUploadBtn($submissionsList, $uploadView, filesStr, assignment_id));

    $uploadView.append($submissionsList);
    
    return $uploadView;
}

function getUploadBtn($submissionsList, $uploadView, filesStr, assignment_id) {
    let $uploadButton = $("<button>").text("Upload").click(function() {
        console.log(filesStr)
	    let uploadedFilesJson = {
	        "files": filesStr,
	    }
	
	    $submissionsList.empty();
	    $submissionsList = submissions_loader(assignment_id);
	    $uploadView.append($submissionsList);
	
        console.log(uploadedFilesJson)

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

    return $uploadButton;
}