<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Login</title>
        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
              rel="stylesheet">
        <style>
                body {
                    background-color: beige;
                }
                .login-container {
                    min-height: 100vh;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                }
                .login-form {
                    width: 100%;
                    max-width: 400px;
                    padding: 15px;
                    margin: auto;
                }
        </style>
    </head>
    <body>
        <div class="container login-container">
            <div class="login-form bg-white shadow rounded p-4">
                <h2 class="text-center mb-4">Login</h2>
                <form action="MetacriticServlet" method="post">
                    <input type="hidden" name="action" value="login">
                    <div class="mb-3">
                        <input type="text"
                               name="username"
                               class="form-control"
                               placeholder="Enter your Username"
                               required>
                    </div>
                    <button type="submit" class="btn btn-primary w-100">Login</button>
                </form>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
