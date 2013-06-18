//
// GENERAL UI STUFF
//

vpac.RsaUiError = vpac.subclassError(Error, 'RsaUiError');

// Extend jQuery to be able to create split panels. The pattern used for this
// plugin is described here: http://docs.jquery.com/Plugins/Authoring
(function ($) {
	$.fn.splitPanel = function(options) {

		var _split = function(opts) {
			var $this = $(this);
			if ($this.children().length != 2)
				throw new vpac.RsaUiError(
						'Split panel must have exactly two children.');

			var first;
			var second;
			first = $this.children().eq(0);
			second = $this.children().eq(1);

			$this.addClass('rsa-ui-split-container');

			var hResize = function(fst, scnd, width) {
				var frac = width / fst.offsetParent().width();
				frac = frac.toFixed(4);
				fst.css({
					height : '',
					width : frac * 100 + '%'
				});
				scnd.css({
					height : '',
					width : (1 - frac) * 100 + '%',
					left : 'auto'
				});
			};
			var vResize = function(fst, scnd, height) {
				var frac = height / fst.offsetParent().height();
				frac = frac.toFixed(4);
				fst.css({
					width : '',
					height : frac * 100 + '%'
				});
				scnd.css({
					width : '',
					height : (1 - frac) * 100 + '%',
					top : 'auto'
				});
			};

			// Make the first panel resizable, and make the second respond to
			// that to fill the available space.
			if (opts.orientation == 'horizontal') {
				first.addClass('rsa-ui-split-left');
				second.addClass('rsa-ui-split-right');
				first.resizable({
					handles : 'e',
					resize : function(event, ui) {
						// Convert to a percentage to allow window resizing to adjust split.
						// Disable height resizing.
						var fst, scnd;
						fst = ui.element;
						scnd = fst.siblings().eq(0);
						hResize(fst, scnd, ui.size.width);
					}
				});

				hResize(first, second, first.width());

			} else {
				first.addClass('rsa-ui-split-top');
				second.addClass('rsa-ui-split-bottom');
				second.css('top', first.height());
				first.resizable({
					handles : 's',
					resize : function(event, ui) {
						// Convert to a percentage to allow window resizing to adjust split.
						// Disable width resizing.
						var fst, scnd;
						fst = ui.element;
						scnd = fst.siblings().eq(0);
						vResize(fst, scnd, ui.size.height);
					}
				});

				vResize(first, second, first.height());
			}

		};

		// Defaults
		var settings = $.extend({
			orientation : 'horizontal'
		}, options);

		// Run over all selected jQuery objects
		this.each(function() {
			_split.apply(this, [ settings ]);
		});

		return this;
	};
})(jQuery);

// Accordion widget wrapper that refreshes itself when the window changes size.
(function($) {
	$.fn.accordionVpac = function(options) {
		this.accordion(options);
		// $.fn.accordion.apply(this, options);

		$this = this;
		$(window).resize(function() {
			$this.accordion('refresh');
		});

		setTimeout(function() {
			$this.accordion('refresh');
		}, 0);

		return this;
	};
})(jQuery);