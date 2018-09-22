$("#article_input_form").on("submit", function(e){
	e.preventDefault();
	
	articleEditor.toTextArea()
	// get form's data
	var formData = new FormData($(this)[0]);
	// DEBUG: Print formData to console
	//for (var pair of formData.entries()) {
	//    console.log(pair[0]+ ', ' + pair[1]); 
	//}
	articleEditor = new SimpleMDE({ element: document.getElementById("article_input_editor"), showIcons: ["code", "table"] });
	
	// send them via AJAX to AJAXServlet		   			
	$.ajax({
		url: "/TEDProject/AJAXServlet?action=addArticle",
		enctype: 'multipart/form-data',
		type: "post",
		data: formData,
		success: function(response){
			var articleID = parseInt(response);
			if ( !isNaN(articleID) ){        // if response is a number then it is the ID of the article we just posted successfully
				// reset editor
				articleEditor.value("");
				$(".custom-file-label").html("<i>Choose file(s) to upload</i>");
				// get article just posted with ajax and prepend it to our wall
				$.ajax({
					url: "/TEDProject/AJAXServlet?action=loadArticle",
					type: "post",
					async: false,           // make these calls synchronous!
					data: { ArticleID : articleID },
					success: function(response){
						$("#wall").prepend(response);
					}
				});
			} else {
				window.alert(response);
			}
		},
		cache: false,
        contentType: false,
        processData: false       // (!) important
	});
	
});
