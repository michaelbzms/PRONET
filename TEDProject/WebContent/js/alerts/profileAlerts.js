var urlParams = new URLSearchParams(window.location.search);
var alertParam = urlParams.get('alert');
console.log(alertParam);
if (alertParam === "editSuccess") {
	$('#editSuccessAlert').show();
	setTimeout(function(){
    	$("#editSuccessAlert").fadeOut('slow'); 
	}, 2000); 		
}
