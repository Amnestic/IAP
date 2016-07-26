<?php
include_once('./constants.php');

// Error handling not included if user declines
$app_id = 297106610639946;
$graph_url = 'https://graph.facebook.com';

$secret = 'd1eb17ecb23c0ee7bc44b335ccdae60d';
$code = $_GET['code'];

// Ensure identity
$user_access_token_uri = "$graph_url/v2.3/oauth/access_token?client_id=297106610639946&redirect_uri=".constant('REDIRECT_URI')."&client_secret=$secret&code=$code";
$user_access_token_data = json_decode(file_get_contents($user_access_token_uri), TRUE);
$user_access_token = $user_access_token_data['access_token'];

$app_access_token_uri = "$graph_url/oauth/access_token?client_id=297106610639946&client_secret=d1eb17ecb23c0ee7bc44b335ccdae60d&grant_type=client_credentials";
$app_access_token = explode('=', file_get_contents($app_access_token_uri))[1]   ;

$inspected_token_uri = "https://graph.facebook.com/debug_token?input_token=$user_access_token&access_token=$app_access_token";
$inspected_token_uri_data = json_decode(file_get_contents($inspected_token_uri), TRUE);
$user_id_inspected = $inspected_token_uri_data['data']['user_id'];
$app_id_inspected = $inspected_token_uri_data['data']['app_id'];

// Ensure access token is for our app
if ($app_id != $app_id_inspected) {
    print "You don' goofed";
    exit;
}

// Get user data
$user_data = json_decode(file_get_contents("$graph_url/v2.7/$user_id_inspected?fields=age_range&access_token=$user_access_token"), TRUE);
$age_range = $user_data['age_range']['min'];
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login landing page</title>
</head>
<body>
<?php
if ($age_range < 21) {
    print 'You are not old enough to see sexy beer bottle';
} else {
    print '<img src="http://www.royalunibrew.com/Files/Billeder/MediaDB/Originals/ceres_topPilsner1.jpg" />';
}
?>
</body>
</html>

