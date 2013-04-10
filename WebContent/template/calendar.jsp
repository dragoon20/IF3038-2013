<div id="calendar">
	<a id="date_button_left" class="date_button" title="Prev" onclick="datePicker.prevMonth();">
		<img alt="arrow_left" src="<%= session.getAttribute("base_url") %>images/date-left.png" />
	</a>
	<a id="date_button_right" class="date_button" title="Next" onclick="datePicker.nextMonth();">
		<img alt="arrow_right" src="<%= session.getAttribute("base_url") %>images/date-right.png" />
	</a>
	<div id="tableHeader"></div>
	<table id="calendarTable" border=1>
		<tbody id="tableBody">
			<tr>
				<th>Su</th>
				<th>Mo</th>
				<th>Tu</th>
				<th>We</th>
				<th>Th</th>
				<th>Fr</th>
				<th>Sa</th>
			</tr>
		</tbody>
	</table>
</div>
<script type="text/javascript" src="<%= session.getAttribute("base_url") %>js/datepicker.js"></script>
