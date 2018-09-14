String.prototype.replaceAll = function(search, replacement) {
    var target = this;
    return target.replace(new RegExp(search, 'g'), replacement);
};

$.fn.scrollTo = function(speed) {
    $('html, body').animate({
        scrollTop: parseInt($(this).offset().top)
    }, speed);
};
