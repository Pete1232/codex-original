package forms

import models.User
import play.api.data.Form
import play.api.data.Forms._
import services.LoginService

class UserForm(loginService: LoginService){
  val userForm = Form(
    mapping(
      "userId" -> nonEmptyText,
      "password" -> nonEmptyText
    )(User.apply)(User.unapply)
        .verifying(
          "login.validation.credentials",
          user => loginService.validateUser(user)
        )
  )
}
