<?php
include_once('./constants.php');
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manual facebook</title>
</head>
<body>
<a href="https://www.facebook.com/dialog/oauth?client_id=297106610639946&redirect_uri=<?=constant('REDIRECT_URI')?>&scope=email">Login with facebook</a>
</body>
</html>