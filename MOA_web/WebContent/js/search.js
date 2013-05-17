/*----- Bagian Search ----*/
var search_text = document.getElementById("search_text");

search_text.onmouseover = function()
{
	this.focus();	
};
search_text.onfocus = function()
{
	var search = document.getElementById("search");
	search.className = "active";
};
search_text.onblur = function()
{
	if (search_text.value==="")
	{
		var search = document.getElementById("search");
		search.className = "";
	}
};