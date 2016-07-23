package forms

import models.User
import play.api.data.Form
import play.api.data.Forms._

class CreateAccountForm {
  val form = Form(
    mapping(
      "userId" -> nonEmptyText,
      "password" -> nonEmptyText
    )(User.apply)(User.unapply)
  )
}
