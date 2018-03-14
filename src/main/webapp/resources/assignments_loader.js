function assignments_loader(course_id, where) {
	let assignments = { 
			"id1": [ "Implement bubble sort", "Code binary search" ],
			"id2": [ "Code Newton Raphson method", "Implement bisection method", "Implement bisection method" ],
			"id3": [ "Code lseek analog", "Build file read API search" ],
			"id4": [ "Implement quick sort", "Implement binary search tree Traversal" ]
	};
	
	$(where).empty();
	let arr = assignments[course_id];
	for (let i = 0; i < arr.length; i++) {
		let $button = $("<button>").addClass("w3-btn w3-block w3-border w3-left-align");
		$button.text(arr[i]);
		
		$(where).append($button);
		
		console.log(where);
		let $div = $("<div>").addClass("w3-container w3-hide w3-padding-24");
		$div.text(arr[i] + "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).");
		
		let $input = $("<input type='file' multiple>");
		$input.change( function() {
			var files = this.files;
			console.log($(this).parent());
			for (var i = 0; i < files.length; i++) {
//	              console.log(files[i].name.split('.').pop());
	              var reader = new FileReader();
	              reader.onload = (function(reader) {
	                  return function() {
//	                    console.log(reader.result);
	                    // document.getElementById("result").innerHTML += reader.result;
	              };
	          }) (reader);
	            
	          reader.readAsText(files[i]);
	          }
			console.log(files);
		});
		
		$div.append($input);
	
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
		
		
		
		$(where).append($div);
	}
	
	function myFunction(id) {
	    var x = document.getElementById(id);
	    if (x.className.indexOf("w3-show") == -1) {
	        x.className += " w3-show";
	    } else { 
	        x.className = x.className.replace(" w3-show", "");
	    }
	}
}