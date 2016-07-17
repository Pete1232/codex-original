package forms

import play.api.data.Form
import play.api.data.Forms._

case class User(userId: String, password: String) {
  def validate(password: String) = {
    password.length > 5
  }
}

object User {
  val userForm = Form(
    mapping(
      "userId" -> nonEmptyText,
      "password" -> nonEmptyText
    )(User.apply)(User.unapply)
        .verifying(
          "Password must be longer than 5 characters",
          user => user.validate(user.password)
        )
  )
}
