var urlParams = new URLSearchParams(window.location.search);
var alertParam = urlParams.get('alert');
console.log(alertParam);
if (alertParam === "emailChangeSuccess") {
	$('#emailChangeSuccessAlert').show();
	setTimeout(function(){
    	$("#emailChangeSuccessAlert").fadeOut('slow'); 
	}, 2000); 		
} else if (alertParam === "passwordChangeSuccess") {
	$("#passwordChangeSuccessAlert").show();
	setTimeout(function(){
    	$("#passwordChangeSuccessAlert").fadeOut('slow'); 
	}, 2000); 	
}
	    	