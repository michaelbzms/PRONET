$(document).on("click", "[id^='deleteArticle']", function(){
	var result = confirm("Are you sure you want to delete this article?");
	if (result) {
		var articleID = ($(this).attr('id')).replace("deleteArticle", "");
		$.ajax({
			url: "/TEDProject/AJAXServlet?action=deleteArticle",
			type: "post",
			data: { ArticleID: articleID,
				 	AuthorID: document.getElementById('deleteArticleScript').getAttribute('data-profID') },
			success: function(response){
				if ( response === "success" ){
					$("#article" + articleID).fadeOut();
				} else {
					window.alert(response);
				}
			},
		});
	}
});