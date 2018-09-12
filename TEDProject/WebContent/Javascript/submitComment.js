$(document).on("submit", "[id^='comment_input_form']", function(e){			
	e.preventDefault();
	var articleID = ($(this).attr('id')).replace("comment_input_form", "");
	var profID = document.getElementById('submitCommentScript').getAttribute('data-profID');
	$.ajax({
		url: "/TEDProject/AJAXServlet?action=addComment",
		type: "post",
		data: { commentText: $("#comment_input_textarea" + articleID).val(), 
			  	ArticleID: articleID,
			 	AuthorID: profID },
		success: function(newCommentID){
			if ( $.isNumeric(newCommentID) ){
	   			$("#past_comments_container" + articleID).prepend($(`
		   				<div id="comment` + newCommentID + `" class="comment">
							<div class="d-flex flex-row vertical_center">
								<div>
									<a href="/TEDProject/ProfileLink?ProfID=` + profID + `">
										<img class="img-thumbnail float-left comment_prof_img" src="` + document.getElementById('submitCommentScript').getAttribute('data-profProfilePicURI') + `" alt="Profile picture">
									</a>
								</div>
								<div>
									<a href="/TEDProject/ProfileLink?ProfID=` + profID + `">` + document.getElementById('submitCommentScript').getAttribute('data-profFullName') + `</a> 
									&nbsp;<small class="text-secondary">just now</small>
								</div>
								<div class="ml-auto">
									<button id="deleteComment` + articleID + "_" + newCommentID + `" class="btn btn-sm btn-outline-secondary mt-1">✕</button>
								</div>
							</div> 
							<div class="content_container">
								`+ $("#comment_input_textarea" + articleID).val() +`
							</div>
						</div>
					`).fadeIn('slow'));
				$("#comment_input_textarea" + articleID).val("");
			} else {
				window.alert(newCommentID);
			}
		},
	});
	$(this.parentElement).animate({
        height: '0px'
    });
	$("#commentsCount" + articleID).html(parseInt($("#commentsCount" + articleID).html(), 10) + 1);
});
		   		