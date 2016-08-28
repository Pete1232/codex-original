package services

import com.google.inject.Inject
import play.api.libs.mailer._

class PasswordResetService  @Inject()(mailerClient: MailerClient){
  def sendEmail(username: String, address: String): String = {
    val email = Email(
      "Your password reset request",
      "Codex <t2sting2@email.com>",
      Seq(s"$username <$address>"),
      bodyHtml = Some(s"""<html><body><p>Hello, World!</p></body></html>""")
    )
    mailerClient.send(email)
  }
}
