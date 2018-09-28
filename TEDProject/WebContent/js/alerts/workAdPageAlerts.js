var urlParams = new URLSearchParams(window.location.search);
var alertParam = urlParams.get('alert');
if (alertParam === "workAdCreationSuccess") {
	$('#workAdCreationSuccessAlert').show();
	setTimeout(function(){
    	$("#workAdCreationSuccessAlert").fadeOut('slow'); 
	}, 2000); 		
} else if (alertParam === "workAdEditSuccess") {
	$('#workAdEditSuccessAlert').show();
	setTimeout(function(){
    	$("#workAdEditSuccessAlert").fadeOut('slow'); 
	}, 2000); 		 	
}
