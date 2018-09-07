var comm_buttons = document.getElementsByClassName("comment_button");
var i;
for (i = 0; i < comm_buttons.length; i++) {
	comm_buttons[i].addEventListener("click", function() {
	comm_form = this.nextElementSibling.firstElementChild;
    if (comm_form.style.height !== "0px") {
    	comm_form.style.height = 0;
    } else {
    	comm_form.style.height = "auto";
	    }
  	});
}