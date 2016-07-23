package forms

import models.User
import play.api.data.Form
import play.api.data.Forms._
import services.LoginService

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class UserForm(loginService: LoginService){
  val userForm = Form(
    mapping(
      "userId" -> nonEmptyText,
      "password" -> nonEmptyText
    )(User.apply)(User.unapply)
        .verifying(
          "login.validation.credentials",
          // TODO revisit this later - should remove all validation from the form model
          user => Await.result(loginService.validateUser(user),Duration.Inf)
        )
  )
}
