/* Generic AJAX JS script for forms                                                                                *
 * Loops on all form fields with a name attribute and sends their "name=value" paramaters to the server with AJAX  */
$("#AJAXform").on("submit", function(){
	
	var that = $(this),
	    url = that.attr('action'),
	    type = that.attr('method'),
	    data = {};
	
	
	// find all form fields with a name and for each of them:
	that.find('[name]').each(function(index, value){
		var that = $(this),
		    name = that.attr('name'),
		    value = that.val();
		
		data[name] = value;
	});
	
	// send AJAX request to server
	$.ajax({
		url: url,
		type: type,
		data: data,
		/* Here we define what to do on success: *
		 * Print response to the ajax target div */
		success: function(response){
			console.log(response);
			$(".ajax_target_div").html(response);
		}
	})
	
	return false;
	
});
