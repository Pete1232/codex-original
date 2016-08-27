package controllers

import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.inject.Inject

import connectors.UserDatabaseConnector
import forms.ChangePasswordForm
import models.User
import play.Logger
import play.api.data.FormError
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}
import services.GenericPasswordChangeService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ChangePasswordController @Inject()(passwordChangeService: GenericPasswordChangeService, userDatabaseConnector: UserDatabaseConnector)
                                        (implicit webJarAssets: WebJarAssets, val messagesApi: MessagesApi)
  extends Controller with I18nSupport {

  val changePasswordForm = new ChangePasswordForm().form

  def changePassword = Action { implicit request =>
    request2session.get("userId") match {
      case Some(userId) => Ok(views.html.change_password(changePasswordForm)).withHeaders()
      case _ => Redirect(routes.LoginController.login(Some("/change-password")))
    }
  }

  def changePasswordPost = Action.async { implicit request =>
    changePasswordForm.bindFromRequest.fold(
      formWithErrors => {
        Logger.debug("Error creating form")
        formWithErrors.errors.foreach(error => Logger.info(s"Validation error on field ${error.messages}"))
        Future.successful(BadRequest(views.html.change_password(formWithErrors)))
      },
      passwordData => {
        if (passwordData._2 != passwordData._3) {
          val form = changePasswordForm.bindFromRequest.copy(errors = Seq(FormError("newPasswordConfirm", "login.validation.passwordMismatch")))
          Future.successful(BadRequest(views.html.change_password(form)))
        }
        else {
          val userName: Option[String] = request.session.get("userId")
          if(userName.isDefined) {
            Logger.debug("Validating user credentials")
            userDatabaseConnector.validatePasswordForUser(User(userName.get, passwordData._1))
              .collect {
                case valid => {
                  val salt = generateSalt
                  passwordChangeService.changePassword(
                    userName.get,
                    hashPassword(passwordData._2, salt),
                    salt
                  )
                  Redirect("/")
                }
              }
          }
          else {
            Future.successful(Redirect(routes.LoginController.login(Some("/change-password"))))
          }
        }
      }
    )
  }

  //  TODO refactor services and connectors - quick fix to finish change password impl
  def hashPassword(password: String, salt: Array[Byte], iterations: Int = 4096, keyLength: Int = 256): Array[Byte] = {
    Logger.debug("Hashing password")
    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
      .generateSecret(
        new PBEKeySpec(password.toCharArray, salt, iterations, keyLength)
      ).getEncoded
  }

  def generateSalt: Array[Byte] = {
    Logger.debug("Generating new salt")
    val byteArray: Array[Byte] = new Array(8)
    new SecureRandom()
      .nextBytes(byteArray)
    byteArray
  }

}
