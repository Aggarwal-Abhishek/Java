/**
 * Particleground demo
 * @author Jonathan Nicol - @mrjnicol
 */

document.addEventListener('DOMContentLoaded', 
	function () {
    	particleground(
			document.getElementById('particles'), {
      			dotColor: '#586bd5',
      			lineColor: '#75c1ff'
		  	}
		);
  	var intro = document.getElementById('intro');
  	intro.style.marginTop = - intro.offsetHeight/2  + 'px';
	},
false);
