var urlParams = new URLSearchParams(window.location.search);
var alertParam = urlParams.get('alert');
if (alertParam === "editSuccess") {
	$('#editSuccessAlert').show();
	setTimeout(function(){
    	$("#editSuccessAlert").fadeOut('slow'); 
	}, 2000); 		
}
