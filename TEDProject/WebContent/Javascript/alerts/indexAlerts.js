var urlParams = new URLSearchParams(window.location.search);
var alertParam = urlParams.get('alert');
console.log(alertParam);
if (alertParam === "registrationSuccess") {
	$('#registrationSuccessAlert').show();
	setTimeout(function(){
    	$("#registrationSuccessAlert").fadeOut('slow'); 
	}, 4000); 		
} else if (alertParam === "accountDeletionSuccess") {
	$("#accountDeletionSuccessAlert").show();
	setTimeout(function(){
    	$("#accountDeletionSuccessAlert").fadeOut('slow'); 
	}, 4000); 	
}
