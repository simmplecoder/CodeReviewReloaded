var filesToUpload = [];
var files;

function getUploadView(assignment_id) {

	let $uploadView = $("<div>");

	//      MARK: - File Uploading
    let $input = $("<input id='home_input' type='file' multiple>").appendTo($uploadView);
    var filesStr = [];

    $input.change( function() {
        files = this.files;
       
        for (let i = 0; i < files.length; i++) {
                var reader = new FileReader();
                reader.onload = (function(reader) {
                    return function(event) {
                        filesToUpload.push(reader.result);
                    }
                }) (reader);

                reader.readAsText(files[i]);
        }
    });

    $uploadView.append($("<br/>"));
    $uploadView.append($input);
    $uploadView.append($("<br/>"));

    let $submissionsList = submissions_loader(assignment_id);
    
    $uploadView.append(getUploadBtn($submissionsList, $uploadView, assignment_id));

    $uploadView.append($submissionsList);
    
    return $uploadView;
}

function getUploadBtn($submissionsList, $uploadView, assignment_id) {
    let $uploadButton = $("<button>").text("Upload").click(function() {
        var temp = [];
        for (var i = 0; i < files.length; i++) {
            temp.push({"filename" : files[i].name, "content" : filesToUpload[i]});
        }

        var data = {"assignment_id" : assignment_id, "files" : temp};

        console.log(JSON.stringify(data));
	    
	    $submissionsList.empty();
	
	    // Should be implemented, when backend will be ready
	     $.ajax({
	         type: "POST",
	         url: "services/upload",
	         contentType: "application/json",
	         data: JSON.stringify(data),
	         success: function(result){
	             console.log("Success");
	             $submissionsList = submissions_loader(assignment_id);
	             $uploadView.append($submissionsList);
	         },
	         error: function() {
	             alert("Failed to load files.")
	         },
	     });

	    filesToUpload = [];
    });

    return $uploadButton;
}