var specifiedElement = document.getElementById("articleInputContainer");
document.addEventListener('click', function(e) {
	var isClickInside = specifiedElement.contains(e.target);
	if (isClickInside) {
		$(".CodeMirror").animate({ 'min-height': "300px" }, 400);
		$(".CodeMirror-scroll").animate({ 'min-height': "300px" }, 400);
	} else {
		$(".CodeMirror").animate({ 'min-height': "100px" }, 400);
		$(".CodeMirror-scroll").animate({ 'min-height': "100px" }, 400);	
	}
});
