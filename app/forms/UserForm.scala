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
    )(User.apply)
    // $COVERAGE-OFF$
    (User.unapply)
    // $COVERAGE-ON$
  )
}
