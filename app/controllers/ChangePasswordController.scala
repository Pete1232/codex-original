package controllers

import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.inject.Inject

import connectors.UserDatabaseConnector
import forms.ChangePasswordForm
import models.User
import play.Logger
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
    Ok(views.html.change_password(changePasswordForm)).withHeaders()
  }

  def changePasswordPost = Action.async { implicit request =>
    changePasswordForm.bindFromRequest.fold(
      formWithErrors => {
        Logger.debug("Error creating form")
        formWithErrors.errors.foreach(error => Logger.info(s"Validation error on field ${error.messages}"))
        Future.successful(BadRequest(views.html.change_password(formWithErrors)))
      },
      passwordData => {
        Logger.debug("Validating user credentials")
        val userName: Option[String] = request.session.get("userId")
        Logger.debug("User" + userName.get)
        //        TODO Refactor - Should throw system error on no userId (should have been authenticated twice by this point!)
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
