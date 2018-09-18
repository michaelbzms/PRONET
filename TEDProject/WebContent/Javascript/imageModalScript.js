$(document).on("click", ".article_img", function () {
    var imgURI = $(this).attr('id');
    console.log('#modal_' + imgURI);
    $('#modal_' + imgURI).modal('show'); 
});
