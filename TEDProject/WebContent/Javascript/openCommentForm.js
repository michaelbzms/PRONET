$(document).on("click", "[id^='commentButton']", function(){ 
	var articleID = ($(this).attr('id')).replace("commentButton", "");
    commentForm = document.getElementById("comment_form" + articleID);
    if (commentForm.style.height !== "0px") {
    	$(commentForm).animate({
            height: '0px'
        });
    } else {
    	$(commentForm).animate({ 
    		height : commentForm.scrollHeight+'px' 
    	});
    	setTimeout(function() { 
    		commentForm.style.height = "auto"; 
    	}, 500);
    }
});
