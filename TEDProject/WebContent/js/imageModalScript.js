$(document).on("click", ".article_img", function () {
    var imgURI = $(this).attr('id');
    $('#modal_' + imgURI).modal('show'); 
});
