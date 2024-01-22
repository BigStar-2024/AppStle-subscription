<!DOCTYPE html>
<html>
<head>
    <script src="https://unpkg.com/@shopify/app-bridge@3.7.4"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            if (window.top === window.self) {
                window.location.href = '${authorizationUrl}';
            } else {
                var AppBridge = window['app-bridge'];
                var createApp = AppBridge.default;
                var Redirect = AppBridge.actions.Redirect;

                const app = createApp({
                    apiKey: '${apiKey}',
                    host: '${host}',
                });

                const redirect = Redirect.create(app);

                redirect.dispatch(
                    Redirect.Action.REMOTE,
                    '${topLevelAuthorizationUrl}',
                );
            }
        });
    </script>
</head>
<body></body>
</html>
