$(document).on("submit", "[id^='like_button']", function(e){			
	e.preventDefault();
	var articleID = ($(this).attr('id')).replace("like_button", "");
	$.ajax({
		url: "/TEDProject/AJAXServlet?action=toggleInterest",
		type: "post",
		data: { ArticleID: articleID,
			 	ProfID: document.getElementById('toggleInterestScript').getAttribute('data-profID') },
		success: function(response){
			if ( response !== "success" ){
				window.alert(response);
			}
		},
	});
	if (this.firstChild.classList.contains("btn-outline-primary")) {
		this.firstChild.classList.add("btn-primary");
		this.firstChild.classList.remove("btn-outline-primary");
		$("#interestsCount" + articleID).html(parseInt($("#interestsCount" + articleID).html(), 10) + 1);
		$("#interestsCount" + articleID + "_2").html(parseInt($("#interestsCount" + articleID + "_2").html(), 10) + 1);
		document.getElementById('selfInterest' + articleID).removeAttribute('style');
		if (document.getElementById('noInterestP' + articleID)) {
			document.getElementById('noInterestP' + articleID).setAttribute('style', 'display: none');
		}
	} else {
		this.firstChild.classList.add("btn-outline-primary");
		this.firstChild.classList.remove("btn-primary");
		$("#interestsCount" + articleID).html(parseInt($("#interestsCount" + articleID).html(), 10) - 1);
		$("#interestsCount" + articleID + "_2").html(parseInt($("#interestsCount" + articleID + "_2").html(), 10) - 1);
		document.getElementById('selfInterest' + articleID).setAttribute('style', 'display: none !important');
		if (document.getElementById('noInterestP' + articleID)) {
			document.getElementById('noInterestP' + articleID).removeAttribute('style');
		}
	}
});
