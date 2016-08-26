package forms

import play.api.data.Form
import play.api.data.Forms._

class ChangePasswordForm {

  import CreateAccountForm._

  val form = Form(
    tuple(
      "oldPassword" -> nonEmptyText,
      "newPassword" -> nonEmptyText
        .verifying(passwordLengthConstraint)
        .verifying(passwordCharactersConstaint)
        .verifying(passwordTopologyConstraint),
      "newPasswordConfirm" -> nonEmptyText
    )
  )
}
