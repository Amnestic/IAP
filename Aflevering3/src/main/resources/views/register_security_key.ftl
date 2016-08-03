<#-- @ftlvariable name="" type="views.RegisterSecurityKeyView" -->
<html>
<head>
    <script src="https://demo.yubico.com/js/u2f-api.js"></script>
    <script>
        var challenge = ${challenge};
        setTimeout(function() {
            u2f.register("${APP_ID}", challenge.registerRequests, [],
                    function (deviceResponse) {
                        document.getElementById('response-input').value = JSON.stringify(deviceResponse);
                        console.log(deviceResponse);
                        document.getElementById('login-form').submit();
                    })
        }, 1500)
    </script>
</head>
<body>
<h2>Tap the gold button on the Yubikey</h2>
<form id="login-form" method="post" action="finish_registration">
    <input name="username" type="hidden" value="${username}">
    <input id="response-input" name="response-input" type="hidden">
</form>
</body>
</html>
