var Day = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
var Mon = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Des"];

function buildcomment(comment)
{
	var commentList = document.getElementById("commentsList");
	var string = '<article id="comment_'+comment.id_komentar+'" class="comment">';
		string += '<a href="profile?username='+comment.username+'">';
			string += '<img src="'+comment.avatar+'" alt="'+comment.fullname+'" class="icon_pict" >';
		string += '</a>';
		string += '<div class="right">';
			date = new Date(comment.timestamp.substr(0,4),(parseInt(comment.timestamp.substr(5,2))-1),comment.timestamp.substr(8,2),
							comment.timestamp.substr(11,2),comment.timestamp.substr(14,2),comment.timestamp.substr(17,2));
			var temphour = (date.getHours()>=10)? "" : "0";
			var tempmin = (date.getMinutes()>=10)? "" : "0";
			string += temphour + date.getHours()+":"+tempmin+date.getMinutes()+" - "+Day[date.getDay()]+"/"+Mon[date.getMonth()];
			if (comment.username==username)
				string += ' <a href="javascript:delete_comment('+comment.id_komentar+')">DELETE</a>';
		string += '</div>';
		string += '<header>';
			string += '<a href="profile?id='+comment.username+'">';
				string += comment.username;
			string += '</a>';
		string += '</header>';
		string += '<p>';
			string += comment.komentar;
		string += '</p>';
		string += '<div class="clear"></div>';
	string += '</article>';		
	commentList.innerHTML += string;
	total_comment++;
	current_total_comment++;
}

function retrievecomment()
{
	var req = ajax();
	req.onreadystatechange = function() {
		switch (req.readyState) {
			case 1:
			case 2:
			case 3:
				break;
			case 4:
				try {
					response = JSON.parse(req.responseText);
					for (var i in response)
					{
						buildcomment(response[i]);
						document.getElementById("show_status").innerHTML = "Menunjukkan "+current_total_comment+" dari "+total_comment;
						timestamp = response[i].timestamp;
					}
				}
				catch (e) {
					console.log(e);
					console.log(e.toString());
				}
				break;
		}
	};
	req.get('api/retrieve_comments?id_task=' + id_task+"&timestamp="+timestamp);
}

document.getElementById("comment_form").onsubmit = function(e)
{
	e.preventDefault();

	var serialized = serialize2(this);;

	var req = ajax();
	req.onreadystatechange = function() {
		switch (req.readyState) {
			case 1:
			case 2:
			case 3:
				break;
			case 4:
				try {
					response = JSON.parse(req.responseText);
					if (response.status == "success")
					{
						document.getElementById("comment_input").value = "";
						retrievecomment();
					}
				}
				catch (e) {

				}
				break;
		}
	};
	req.post('api/comment', serialized);
	return false;
};

window.onload = function()
{
	setInterval(function(){retrievecomment();},7000);
};

function delete_comment(id)
{
	var serialized = "id="+id;

	var req = ajax();
	req.onreadystatechange = function() {
		switch (req.readyState) {
			case 1:
			case 2:
			case 3:
				break;
			case 4:
				try {
					response = JSON.parse(req.responseText);
					if (response.status == "success")
					{
						var element = document.getElementById("comment_"+id);
						element.parentNode.removeChild(element);
						total_comment--;
						current_total_comment--;
						document.getElementById("show_status").innerHTML = "Menunjukkan "+current_total_comment+" dari "+total_comment;
					}
				}
				catch (e) {

				}
				break;
		}
	};
	req.post('api/remove_comment', serialized);
}

function prepend_comment(comment)
{
	var commentList = document.getElementById("commentsList");
	var string = '<article id="comment_'+comment.id_komentar+'" class="comment">';
		string += '<a href="profile?username='+comment.username+'">';
			string += '<img src="'+comment.avatar+'" alt="'+comment.fullname+'" class="icon_pict" >';
		string += '</a>';
		string += '<div class="right">';
			date = new Date(comment.timestamp.substr(0,4),(parseInt(comment.timestamp.substr(5,2))-1),comment.timestamp.substr(8,2),
								comment.timestamp.substr(11,2),comment.timestamp.substr(14,2),comment.timestamp.substr(17,2));
			var temphour = (date.getHours()>=10)? "" : "0";
			var tempmin = (date.getMinutes()>=10)? "" : "0";
			var tempstring = temphour + date.getHours()+":"+tempmin+date.getMinutes()+" - "+Day[date.getDay()]+"/"+Mon[date.getMonth()];
			string += tempstring;
			if (comment.username==username)
				string += ' <a href="javascript:delete_comment('+comment.id_komentar+')">DELETE</a>';
		string += '</div>';
		string += '<header>';
			string += '<a href="profile?username='+comment.username+'">';
				string += comment.username;
			string += '</a>';
		string += '</header>';
		string += '<p>';
			string += comment.komentar;
		string += '</p>';
		string += '<div class="clear"></div>';
	string += '</article>';
	commentList.innerHTML = string + commentList.innerHTML;
}

function more_comment()
{
	var req = ajax();
	req.onreadystatechange = function() {
		switch (req.readyState) {
			case 1:
			case 2:
			case 3:
				break;
			case 4:
				try {
					response = JSON.parse(req.responseText);
					for (var i in response)
					{
						prepend_comment(response[i]);
						first_timestamp = response[i].timestamp;
						current_total_comment++;
					}
					if (total_comment==current_total_comment)
					{
						var element = document.getElementById("more_link");
						element.parentNode.removeChild(element);
					}
					else
					{
						document.getElementById("more_link").innerHTML = "Komentar sebelumnya";
					}
					document.getElementById("show_status").innerHTML = "Menunjukkan "+current_total_comment+" dari "+total_comment;
				}
				catch (e) {
					console.log(e);
				}
				break;
		}
	};
	req.get('api/get_previous_comments?id_task=' + id_task+"&timestamp="+first_timestamp);
}
