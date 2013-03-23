// BAJURI
// BukAn JqUeRI
// Sebuah pustaka Javascript yang mengimitasi jQuery, tapi bukan
// All of this was created through reverse engineering with some Stack Overflow help,
// but no looking at jQuery code at all!

bajuri = function(selector, context) {
	return new bajuri.fn.init(selector, context);
};

bajuri.fn =
bajuri.prototype = {
	nodes: [],

	getElements: function() {
		return this;
	},

	init: function(selector, context) {
		this.nodes = [];
		this.add(selector, context);

		return this;
	},

	add: function(selector, context) {
		if (typeof selector == 'function') {
			// http://stackoverflow.com/questions/799981/document-ready-equivalent-without-jquery
			if (document.addEventListener)
				document.addEventListener('DOMContentLoaded', selector);
			else if (document.attachEvent) {
				document.attachEvent('onreadystatechange', function() {
					if (document.readyState == 'complete') {
						document.detachEvent('onreadystatechange', arguments.callee);
						selector();
					}
				});
			}
		}
		else if (selector instanceof HTMLElement) {
			this.nodes.push(selector);
		}
		else if (selector instanceof bajuri) {
			for (node in selector.nodes) {
				this.nodes.push(node);
			}
		}
		else if (document.querySelectorAll && typeof selector == 'string') {
			n = document.querySelectorAll(selector);
			for (i = 0; i < n.length; i++) {
				this.nodes.push(n.item(i));
			}
		}
		else if (m = selector.match(/^#(.*)$/)) {
			this.nodes.push(document.getElementById(m[1]));
		}
		else if (m = selector.match(/^([a-zA-Z\-]*)(?:.([a-zA-Z_\-]+)|)?$/)) {
			tagName = m[1] ? m[1] : '*';
			className = m[2];
			elements = document.getElementsByTagName(selector);
			for (i = 0; i < elements.length; i++) {
				if (className && context)
				this.nodes.push(elements.item(i));
			}
		}

		return this;
	},

	hasClass: function(cls) {
		pattern = new RegExp(cls +' | ' + cls + '|^' + cls + '$');
		return this.nodes[0].className.match(pattern);
	},

	toggleClass: function(cls) {
		if (this.hasClass(cls))
			this.removeClass(cls);
		else
			this.addClass(cls);

		return this;
	},

	addClass: function(cls) {
		cls = cls.toLowerCase();
		this.each(function() {
			classes = this.className.toLowerCase().split(/\s+/);
			if (classes.indexOf(cls) === -1) {
				classes.push(cls);
			}
			this.className = classes.join(' ');
		});

		return this;
	},

	removeClass: function(cls) {
		cls = cls.toLowerCase();
		this.each(function() {
			classes = this.className.toLowerCase().split(/\s+/);
			while (classes.indexOf(cls) !== -1) {
				idx = classes.indexOf(cls);
				classes.splice(idx, 1);
			}
			this.className = classes.join(' ');
		});

		return this;
	},

	attr: function(a, b) {
		if (b !== undefined) {
			this.each(function() {
				this.setAttribute(a, b);
			});

			return this;
		}
		else {
			return this.nodes[0].getAttribute(a);
		}
	},

	hasAttr: function(a) {
		return this.nodes[0].getAttribute(a) && true;
	},

	removeAttr: function(a) {
		this.nodes.forEach(function(node) {
			node.removeAttribute(a);
		});

		return this;
	},

	first: function() {
		return bajuri([this.nodes[0]]);
	},

	on: function(e, callback) {
		this.nodes.forEach(function(node) {
			if (node) {
				if (node.addEventListener)
					node.addEventListener(e, callback);
				else if (node.attachEvent)
					node.attachEvent(e, callback);
			}
		});

		return this;
	},

	val: function() {
		return this.nodes[0].value;
	},

	text: function() {
		el = this.nodes[0];
		if (el) {
			if (el.textContent)
				return el.textContent;
			else if (el.innerText)
				return el.innerText;
		}
	},

	append: function(content) {
		this.each(function() {
			if (content instanceof HTMLElement)
				this.appendChild(content);
			else if (content instanceof bajuri) {
				var node = this;
				content.each(function() {
					node.appendChild(this);
				});
			}
		});

		return this;
	},

	each: function(callback) {
		this.nodes.forEach(function(node) {
			callback.call(node);
		});
	},

	data: function(key, value) {
		if (value !== undefined) {
			if (typeof bajuri.cache[this.nodes[0]] !== 'object')
				bajuri.cache[this.nodes[0]] = {};

			bajuri.cache[this.nodes[0]][key] = value;
			return this;
		}
		else {
			if (bajuri.cache[this.nodes[0]])
				return bajuri.cache[this.nodes[0]][key];
			else
				return undefined;
		}
	},

	show: function() {
		this.each(function() {
			if (this.style.display === 'none')
				this.style.display = Rp(this).data('formerDisplayValue') ? Rp(this).data('formerDisplayValue') : 'block';
		});

		return this;
	},

	hide: function() {
		this.each(function() {
			currentDisplay = this.style.display;
			Rp(this).data('formerDisplayValue', currentDisplay);
			this.style.display = 'none';
		});

		return this;
	},

	length: function() {
		return this.nodes.length;
	},

	css: function(style) {
		if (arguments.length > 1) {
			var value;
			value = arguments[1];
			this.each(function() {
				this.style[style] = value;
			});

			return this;
		}
		else {
			return this.nodes[0].style[style];
		}
	},

	prop: function(prop) {
		if (arguments.length > 1) {
			var value;
			value = arguments[1];
			this.each(function() {
				this[prop] = value;
			});

			return this;
		}
		else {
			return this.nodes[0][prop];
		}
	},

	text: function(data) {
		if (data === undefined) {
			if (this.nodes[0].innerText)
				return this.nodes[0].innerText;
			if (this.nodes[0].textContent)
				return this.nodes[0].textContent;
			else
				return this.nodes[0].innerHTML;
		}
		else {
			if (this.nodes[0].innerText)
				this.prop('innerText', data);
			else if (this.nodes[0].textContent)
				this.prop('textContent', data);
			else
				this.prop('innerHTML', data);

			return this;
		}
	},

	html: function(data) {
		if (data === undefined) {
			return this.nodes[0].innerHTML;
		}
		else {
			return this.prop('innerHTML', data);
		}
	},

	empty: function() {
		return this.html('');
	},

	serialize: function() {
		form = this.nodes[0];

		if (!form || form.nodeName !== "FORM") {
			return;
		}
		var i, j, q = [];
		for (i = form.elements.length - 1; i >= 0; i = i - 1) 
		{
			if (form.elements[i].name === "") 
			{
				continue;
			}
			
			switch (form.elements[i].nodeName) {
			case 'INPUT':
				switch (form.elements[i].type) 
				{
					case 'text':
					case 'hidden':
					case 'password':
					case 'button':
					case 'reset':
					case 'submit':
						q.push(form.elements[i].name + "=" + encodeURIComponent(form.elements[i].value));
						break;
					case 'checkbox':
					case 'radio':
						if (form.elements[i].checked) 
						{
							q.push(form.elements[i].name + "=" + encodeURIComponent(form.elements[i].value));
						}                                               
						break;
				}
				break;
			case 'file':
				break; 
			case 'TEXTAREA':
				q.push(form.elements[i].name + "=" + encodeURIComponent(form.elements[i].value));
				break;
			case 'SELECT':
				switch (form.elements[i].type) 
				{
					case 'select-one':
						q.push(form.elements[i].name + "=" + encodeURIComponent(form.elements[i].value));
						break;
					case 'select-multiple':
						for (j = form.elements[i].options.length - 1; j >= 0; j = j - 1) 
						{
							if (form.elements[i].options[j].selected) 
							{
								q.push(form.elements[i].name + "=" + encodeURIComponent(form.elements[i].options[j].value));
							}
						}
						break;
				}
				break;
			case 'BUTTON':
				switch (form.elements[i].type) 
				{
					case 'reset':
					case 'submit':
					case 'button':
						q.push(form.elements[i].name + "=" + encodeURIComponent(form.elements[i].value));
						break;
				}
				break;
			}
		}
		return q.join("&");
	}
}

bajuri.cache = {};

// Split classes from an element's class attribute, case-insensitively
bajuri._parseClasses = function(className) {
	return className.toLowerCase().split(/\s+/);
};

bajuri.factory = function(tagName) {
	return bajuri(document.createElement(tagName));
}

bajuri.serialize = function(obj) {
	unjoined = [];
	for (var prop in obj) {
		left = encodeURIComponent(prop);
		right = encodeURIComponent(obj[prop]);
		leftright = left + '=' + right;
		unjoined.push(leftright);
	}

	joined = unjoined.join('&');

	return joined;
}

bajuri.ajaxRequest = function(url) {
	var handle;
	if (window.XMLHttpRequest) {
		handle = new XMLHttpRequest();
	}
	else {
		if (window.ActiveXObject) {
			var activexmodes=["Msxml2.XMLHTTP", "Microsoft.XMLHTTP"];
			for (var i=0; i<activexmodes.length; i++) {
				try {
					handle = new ActiveXObject(activexmodes[i])
				}
				catch(e) {}
			}

		}
	}

	handle.url = url;

	handle.openAndSend = function(method, url) {
		this.open(method, url);
		this.send();
	}

	handle.get = function(url) {
		this.open('GET', url, true);
		this.send();
	}

	handle.post = function(data) {
		this.open('POST', this.url, true);
		this.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		if (typeof data === "object")
			data = bajuri.serialize(data);
		this.send(data);
	}

	return handle;
}

bajuri.parseJSON = function(o) {
	if (window.JSON) {
		return JSON.parse(o);
	}
	else {
		return eval('(' + o + ')');
	}
}

bajuri.fn.init.prototype = bajuri.fn;

Rp = bajuri;