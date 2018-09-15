/* Global variables */
var profID =  document.getElementById('messages_script').getAttribute('data-profID');
console.log("profID = " + profID);
var alerted_once = false;

// when attempted to chat with an unknown professor:
$("#404prof").on("click", function(){
	// Make the corresponding conversation active and the rest hidden
	$(".conversation").hide();
	$(".conversation").removeClass("active_conv");
	$(".conv_li").css("background-color", "#fbfcff");
	$("#404CHATWITH").show();
	$("#404prof").css("background-color", "#b2cdff");
});

// when clicked on a conversation box:
function select_conversation(other_prof_ID){
	// Make the corresponding conversation active and the rest hidden
	$(".conversation").hide();
	$(".conversation").removeClass("active_conv");
	$(".conv_li").css("background-color", "#fbfcff");
	$("#conversation" + other_prof_ID).show();
	$("#conversation" + other_prof_ID).addClass("active_conv");
	$("#conv" + other_prof_ID).css("background-color", "#b2cdff");
	// load the conversation from server using AJAX
	$.ajax({
		url: "/TEDProject/AJAXServlet?action=loadConvo",
		type: "post",
		data: { homeprof: profID, awayprof: other_prof_ID },
		success: function(response){
			$("#conversation" + other_prof_ID).html(response);
			updateScroll();
		}
	});
}

// send text AJAX form
$("#send_text").on("submit", function(e){
	e.preventDefault();
	if ( document.getElementsByClassName("active_conv").length > 0 ){     // if there exists a class "active_conv" 	
		// Parse the id of active_conv to get the conversation's other professional id
		var other_prof_id = $('.active_conv').attr('id').replace("conversation","");
		// update database on the text with AJAX
		$.ajax({
			url: "/TEDProject/AJAXServlet?action=addMessage",
			type: "post",
			data: { text: $("#msg_input").val(),
					sentBy: profID,
					sentTo: other_prof_id,
	              },
			success: function(response){
				if ( response === "success" ){
					// CONFIG: Date format here might be different from server's if changed there
					// also it might be different (ex by 0-2 secs) from the time saved on database but there is little we can do about that client-side (one idea would be for the server to return that time as an AJAX response)
					var dt = new Date();
					var datetime = twoDigits(dt.getDate()) + "/" + twoDigits(1 + dt.getMonth()) + "/" + dt.getFullYear() + " " + twoDigits(dt.getHours()) + ":" + twoDigits(dt.getMinutes()) + ":" + twoDigits(dt.getSeconds());	
					// append text to the (must be only one) active conversation
					$(".active_conv").append("<span class=\"home_timestamp\">" + datetime + "</span><p class=\"home_message\">" + $("#msg_input").val().replaceAll("\n","\n<br>\n") + "</p><br>");
					// reset input value
					$("#msg_input").val("");
					updateScroll();
				} else { // else toast-notify user
					window.alert(response);
				}
			}
		});
	} else {
		window.alert("Cannot send text: no proper conversation is selected");
	}
});

// use this to scroll a conversation to the bottom
function updateScroll(){
	var scroll_box = $('.active_conv');
    var height = scroll_box[0].scrollHeight;
    scroll_box.scrollTop(height);
}

// two digits precision
function twoDigits(d) {
    if (0 <= d && d < 10) return "0" + d.toString();
    if (-10 < d && d < 0) return "-0" + (-1*d).toString();
    return d.toString();
}

// submit text with 'enter' but not on shift+enter
$("#send_text").keypress(function (e) {
    if(e.which == 13 && !e.shiftKey) {        
        $(this).closest("form").submit();
        e.preventDefault();
        return false;
    }
});	

//update (ONLY) the active_conv in real time every 2 secs:
window.setInterval(function(){
	if ( document.getElementsByClassName("active_conv").length > 0 ){     // if there exists a class "active_conv" 	
		// find latest away message on the (ONE) active conversation
		var latest_timestamp = $(".active_conv .away_timestamp").last().text();
		// Parse the id of active_conv to get the conversation's other professional id
		var other_prof_id = $('.active_conv').attr('id').replace("conversation","");
		// use ajax to update it with new messages - if they exist
		$.ajax({
			url: "/TEDProject/AJAXServlet?action=checkForNewMessages",
			type: "post",
			data: { latestGot: latest_timestamp,
					homeprof: profID,
					awayprof: other_prof_id,
	              },
			success: function(response){                  // on success we append any new (away) messages to the conversation
				if ( response.includes("DATABASE_DOWN") ){
					console.log("Error: Database is down!")
					if (!alerted_once){
						window.alert("Warning: Cannot connect to database");
						alerted_once = true;
					}
				}
				else if ( !response.includes("NO_NEW_MESSAGES") ) {    // append response ONLY if it contained new messages
					$(".active_conv").append(response);   // alternatively use: $(".conversation" + other_prof_id)
					updateScroll();
				}
			}
		});
	}
}, 2000);
