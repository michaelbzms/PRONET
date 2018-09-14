$(document).on("click", "[id^='deleteComment']", function(){
	var result = confirm("Are you sure you want to delete this comment?");
	if (result) {
		var articleID = ($(this).attr('id')).replace("deleteComment", "");
		articleID = articleID.slice(0, articleID.lastIndexOf("_"));
		var commentID = ($(this).attr('id')).replace("deleteComment" + articleID + "_", "");
		$.ajax({
			url: "/TEDProject/AJAXServlet?action=deleteComment",
			type: "post",
			data: { CommentID: commentID,
				 	AuthorID: document.getElementById('deleteCommentScript').getAttribute('data-profID') },
			success: function(response){
				if ( response === "success" ){
					$("#comment" + commentID).fadeOut();
				} else {
					window.alert(response);
				}
			},
		});
		$("#commentsCount" + articleID).html(parseInt($("#commentsCount" + articleID).html(), 10) - 1);
	}
});