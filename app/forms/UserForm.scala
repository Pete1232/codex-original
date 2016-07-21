package forms

import models.User
import play.api.data.Form
import play.api.data.Forms._
import services.MockLoginService

object UserForm {
  val userForm = Form(
    mapping(
      "userId" -> nonEmptyText,
      "password" -> nonEmptyText
    )(User.apply)(User.unapply)
        .verifying(
          "login.validation.length",
          user => user.validate(user.password)
        )
        .verifying(
          "login.validation.userId",
          user => MockLoginService.isKnown(user)
        )
        .verifying(
          "login.validation.credentials",
          user => MockLoginService.validateUser(user)
        )
  )
}
