package forms

import models.User
import play.api.data.Form
import play.api.data.Forms._
import services.TopologyParser

class CreateAccountForm {
  val form = Form(
    mapping(
      "userId" -> nonEmptyText,
      "password" -> nonEmptyText
    )(User.apply)(User.unapply)
      .verifying(
        User.passwordCheckConstraint
      )
      .verifying(
        TopologyParser.passwordCheckConstraint
      )
  )
}
