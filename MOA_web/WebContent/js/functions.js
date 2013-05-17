function serialize(obj)
{
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

function serialize2(data) 
{
	form = data;

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

function ajax (url) 
{
	var handle;
	if (window.XMLHttpRequest) 
	{
		handle = new XMLHttpRequest();
	}
	else {
		if (window.ActiveXObject) {
			var activexmodes=["Msxml2.XMLHTTP", "Microsoft.XMLHTTP"];
			for (var i=0; i<activexmodes.length; i++) {
				try {
					handle = new ActiveXObject(activexmodes[i]);
				}
				catch(e) 
				{
					Console.log(e);
				}
			}

		}
	}

	handle.url = url;

	this.oninit = function() {};
	this.onload = function() {};

	handle.get = function(url) {
		if (url == undefined)
			url = this.url;
		this.open('GET', url, true);
		this.send();

		return this;
	};

	handle.post = function(url, data) {
		this.open('POST', url, true);
		this.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		if (typeof data === "object")
			data = serialize(data);
		this.send(data);

		return this;
	};

	return handle;
}