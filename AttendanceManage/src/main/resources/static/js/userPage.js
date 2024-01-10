$(".openbtn").click(function () {
	$(this).toggleClass('active');
    $("#g-nav").toggleClass('panelactive');
});
$("#g-nav a").click(function () {
    $(".openbtn").removeClass('active');
    $("#g-nav").removeClass('panelactive');
});
$("body").click(function (e) {
    if (!$(e.target).closest("#g-nav").length && !$(e.target).closest(".openbtn").length) {
        $(".openbtn").removeClass('active');
        $("#g-nav").removeClass('panelactive');
    }
});
$("#g-nav").click(function (e) {
    e.stopPropagation();
});
