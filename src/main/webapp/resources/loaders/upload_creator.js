function getUploadView() {

	let $uploadView = $("div");

	//      MARK: - File Uploading
    let $input = $("<input id='home_input' type='file' multiple>").appendTo($uploadView);
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

    $uploadView.append($("<br/>"));
    $uploadView.append($input);
    $uploadView.append($("<br/>"));

    let $submissionsList = submissions_loader();
    
    $uploadView.append(getUploadBtn($submissionsList, $adiv, filesStr));

    $uploadView.append($submissionsList);
    
    return $uploadView;
}




function getUploadBtn($submissionsList, $adiv, filesStr) {
    let $uploadButton = $("<button>").text("Upload").click(function() {
    let uploadedFilesJson = {
        "files": filesStr,
    }

    $submissionsList.empty();
    $submissionsList = submissions_loader();
    $adiv.append($submissionsList);

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