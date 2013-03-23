<?php

require_once 'classes/App.php';

$app = new App;
$app->bootstrap('api', 'fetch_latest_task');
