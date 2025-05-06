package ul.group14.utils

import org.intellij.lang.annotations.Language

const val EMAIL_REGEX = ("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$")

val phoneNumberRegex = Regex("^(0|\\+?27[ -]?)[5-9]\\d[ -]?\\d{3}[ -]?\\d{4}$")

@Language("html")
val EMAIL_PASSWORD_RESET = """
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Password Reset - Music Piracy Protection</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f2f2f2;
            padding: 20px;
        }
        .container {
            max-width: 600px;
            margin: 0 auto;
            background-color: #fff;
            padding: 20px;
            border-radius: 5px;
        }
        .content {
            margin-bottom: 20px;
        }
        .otp {
            background-color: #41836c;
            color: white;
            padding: 15px 25px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            border-radius: 5px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="content">
            <p>Hi {{email_to}},</p>
            <p>You have requested a password reset for your account on <b>Music Piracy Protection</b> System.</p>
            <p>To reset your password, use the following OTP:</p>
            <h1 class="otp"><b>{{otp}}</b></h1>
            <p>This OTP will expire at <b>{{expiration_time}}</b>.</p>
            <p>If you did not ask to reset your password, you can ignore this email.</p>
            <p>Best regards,<br>Your <b>Music Piracy Protection</b> team</p>
        </div>
    </div>
</body>
</html>
""".trimIndent()

@Language("html")
val EMAIL_EMAIL_VERIFICATION = """
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Email Verification - Music Piracy Protection</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f2f2f2;
            padding: 20px;
        }
        .container {
            max-width: 600px;
            margin: 0 auto;
            background-color: #fff;
            padding: 20px;
            border-radius: 5px;
        }
        .content {
            margin-bottom: 20px;
        }
        .otp {
            background-color: #41836c;
            color: white;
            padding: 15px 25px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            border-radius: 5px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="content">
            <p>Hi {{email_to}},</p>
            <p>You have requested email verification for your email above on <b>Music Piracy Protection</b> System.</p>
            <p>To verify your email, use the following OTP:</p>
            <h1 class="otp"><b>{{otp}}</b></h1>
            <p>This OTP will expire at <b>{{expiration_time}}</b>.</p>
            <p>If you did not ask for email verification, you can ignore this email.</p>
            <p>Best regards,<br>Your <b>Music Piracy Protection</b> team</p>
        </div>
    </div>
</body>
</html>
""".trimIndent()
