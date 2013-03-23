<?php

require_once 'classes/App.php';

$app = new App;
$app->bootstrap('api', 'retrieve_tasks');
