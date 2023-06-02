
$(function() {
	$('body ').on('click', '.btn-succ', function(e) {
		$('.panel-form').remove();
		$('.modal-content').prepend(`<div class="panel-form">
	    										<div class="franja">
												
											 		<div class="col-xs-1 franja-amarilla"></div>
											 		<div class="col-xs-11 franja-roja"></div>
													
												</div>
	    								</div>`)
	})
})