function answer_request(decision, askerID, receiverID){
	$.ajax({
		url: "/TEDProject/AJAXServlet?action=connectionRequest",
		type: "post",
		data: { AskerID: askerID, ReceiverID: receiverID, decision: ((decision) ? "accept" : "decline") },
		success: function(response){
			$("#request" + askerID).fadeOut();
			decreaseNumberOfNotifications();
			setTimeout(function (){
			var gotEmpty = true;
			$(".request").each(function(){
				if ( $(this).is(":visible") ){
					gotEmpty = false;
				}
			});
			if ( gotEmpty ){
				$("#connection_requests_bar ul").html("You don't have any Connection Requests.");
			}
 		}, 420);
		}
	});
}

function mark_as_seen(i, type, notificationID, interestBy, articleID){	
	$.ajax({
		url: "/TEDProject/AJAXServlet?action=markAsSeen",
		type: "post",
		data: {  
			type: type,
			commentORapplicationID: notificationID,     // null or idComment or idApplication
			interestBy: interestBy,                     // idInterestShownBy
			articleID: articleID                        // idArticle
		},
		success: function(response){
			if ( response === "success" ) {
				$("#notification" + i).fadeOut();
				decreaseNumberOfNotifications();
				setTimeout(function (){
					var gotEmpty = true;
					$(".notification").each(function(){
						if ( $(this).is(":visible") ){
							gotEmpty = false;
						}
					});
					if ( gotEmpty ){
						$("#notifications_bar ul").html("<p><i>You don't have any Notifications.</i></p>");
					}
		 		}, 420);
			} else {
				window.alert(response);
			}
		}
	});
}

$("#markAll").on("click", function(){
	$(".cancel").trigger("click");      // trigger click event for all "cancel" buttons
});

function decreaseNumberOfNotifications(){
	var num = parseInt($("#numberOfNotifications").text());
	--num;
	if (num > 0){
		$("#numberOfNotifications").text(num.toString());
	} else {
		$("#numberOfNotifications").fadeOut();
	}
}
