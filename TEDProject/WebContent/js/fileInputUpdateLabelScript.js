$('.custom-file-input').change(function (e) {
    var files = [];
    for (var i = 0; i < $(this)[0].files.length; i++) {
        files.push($(this)[0].files[i].name);
    }
    var new_label = files.join(', ');
    if (new_label === "") {
    	new_label = document.getElementById('fileInputUpdateLabelScript').getAttribute('data-emptyText');
    }
    $(this).next('.custom-file-label').html(new_label);
});
