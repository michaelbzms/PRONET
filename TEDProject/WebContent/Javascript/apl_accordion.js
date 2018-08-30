$.fn.scrollTo = function(speed) {
    $('html, body').animate({
        scrollTop: parseInt($(this).offset().top)
    }, speed);
};

var acc = document.getElementsByClassName("apl_accordion");
var i;
for (i = 0; i < acc.length; i++) {
	acc[i].addEventListener("click", function() {
		this.classList.toggle("apl_accordion_color");
		this.firstElementChild.classList.toggle("apl_active");
	    var panel = this.nextElementSibling;
	    if (panel.style.maxHeight) {
	      panel.style.maxHeight = null;
	    } else {
	      panel.style.maxHeight = panel.scrollHeight + "px";
	    } 
	    console.log('#aplFocusPoint' + i)
	    $('#aplFocusPoint' + i).scrollTo(500)
  	});
}
