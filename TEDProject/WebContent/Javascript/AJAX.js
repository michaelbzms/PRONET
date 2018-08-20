/* Generic AJAX JS script for forms                                                                                *
 * Loops on all form fields with a name attribute and sends their "name=value" paramaters to the server with AJAX  */
$("form.ajax").on("submit", function(){
	
	var that = $(this),
	    url = that.attr('action'),
	    type = that.attr('method'),
	    data = {};
	
	// the following code computes the "action" parameter of the form's url. 
	// This is in turn used to select the correct ".ajax_target_div" for AJAX, withound needing a different script for each AJAX form.
	var i = 0;
	for (i = 0 ; i < url.length ; i++){
		if (url[i] === '='){
			i++;
			break;
		}
	}
	var action = url.substring(i);   // keep only the action's parameter value
	
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
			// target ALL .ajax_target_div : $(".ajax_target_div").html(response);
			$("#" + action).html(response);     // this targets only an element with that unique (for each form) id
		}
	});
	
	return false;
	
});
