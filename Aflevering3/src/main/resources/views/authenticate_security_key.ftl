<#-- @ftlvariable name="" type="views.AuthenticateSecurityKeyView" -->
<html>
<head>
<script src="https://demo.yubico.com/js/u2f-api.js"></script>
<script>
    var challenge = ${challenge};
    console.log(challenge);
    setTimeout(function() {
        u2f.sign("${APP_ID}", challenge.authenticateRequests[0].challenge, challenge.authenticateRequests,
                function (deviceResponse) {
                    document.getElementById('response-input').value = JSON.stringify(deviceResponse);
                    console.log(deviceResponse);
                    document.getElementById('login-form').submit();
                })
    }, 1500)
</script>
</head>
<body>
<h2>Tap the gold button on the Yubikey to login</h2>
<form id="login-form" method="post" action="finish_authentication">
    <input name="username" type="hidden" value="${username}">
    <input id="response-input" name="response-input" type="hidden">
</form>
</body>
</html>