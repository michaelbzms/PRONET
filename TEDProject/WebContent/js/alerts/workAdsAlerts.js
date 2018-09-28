var urlParams = new URLSearchParams(window.location.search);
var alertParam = urlParams.get('alert');
if (alertParam === "workAdDeletionSuccess") {
	$("#workAdDeletionSuccessAlert").show();
	setTimeout(function(){
    	$("#workAdDeletionSuccessAlert").fadeOut('slow'); 
	}, 2000); 	
} else if (alertParam === "applicationSubmissionSuccess") {
	$("#applicationSubmissionSuccessAlert").show();
	setTimeout(function(){
    	$("#applicationSubmissionSuccessAlert").fadeOut('slow'); 
	}, 2000); 	
} else if (alertParam === "applicationCancelationSuccess") {
	$("#applicationCancelationSuccessAlert").show();
	setTimeout(function(){
    	$("#applicationCancelationSuccessAlert").fadeOut('slow'); 
	}, 2000); 	
}
