package ul.group14.database

import io.ktor.server.application.Application
import io.ktor.server.config.tryGetString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.HtmlEmail
import javax.mail.internet.InternetAddress

/**
 * Executes a database operation block within the scope of an IO-optimized coroutine context.
 * Designed for running MongoDB or other database queries that may involve IO operations
 * while leveraging coroutine support for asynchronous programming.
 *
 * @param T The return type of the operation performed within the block.
 * @param block A suspendable lambda function that contains the database operation logic to execute.
 *              This lambda runs in the context of Dispatchers.IO to optimize for IO-bound tasks.
 * @return The result of the operation executed within the block.
 */
suspend inline fun <T> databaseQuery(
    noinline block: suspend CoroutineScope.() -> T
): T = withContext(Dispatchers.IO, block)

/**
 * Sends an email with the specified subject and HTML content to the desired recipient.
 * The email server configurations (e.g., hostname, port, authenticator email, etc.)
 * are retrieved from the application's environment configuration.
 *
 * @param subject The subject of the email.
 * @param htmlMsg The HTML content of the email message.
 * @param emailTo The recipient's email address. If null, the authenticator email is used as the recipient.
 * @param emailFrom The sender's email address. If null, the authenticator email is used as the sender.
 * @return A string indicating the result of the email-sending operation, typically a success message or email ID.
 */
suspend inline fun Application.sendEmail(
    subject: String,
    htmlMsg: String,
    emailTo: String?,
    emailFrom: String? = null,
): String = withContext(Dispatchers.IO) {
    val hostName = environment.config.tryGetString("HtmlEmail.hostName")
    val port = environment.config.tryGetString("HtmlEmail.port")?.toInt() ?: 587
    val authenticatorEmail = environment.config.tryGetString("HtmlEmail.authenticator.email")
    val authenticatorPassword = environment.config.tryGetString("HtmlEmail.authenticator.password")
    HtmlEmail().apply {
        this.hostName = hostName
        setSmtpPort(port)
        setAuthenticator(DefaultAuthenticator(authenticatorEmail, authenticatorPassword))
        isSSLOnConnect = true
        setFrom(emailFrom ?: authenticatorEmail)
        emailFrom?.let {
            setCc(setOf(InternetAddress(it)))
        }
        this.subject = subject
        setHtmlMsg(htmlMsg)
        addTo(emailTo ?: authenticatorEmail)
    }.send()
}