package forms

import models.User
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}
import services.TopologyParser

class CreateAccountForm {
  import CreateAccountForm._
  val form = Form(
    mapping(
      "userId" -> nonEmptyText,
      "password" -> nonEmptyText
        .verifying(passwordLengthConstraint)
        .verifying(passwordTopologyConstraint)
    )(User.apply)
      // $COVERAGE-OFF$
      (User.unapply)
      // $COVERAGE-ON$
  )
}

object CreateAccountForm {
  def passwordLengthConstraint: Constraint[String] = stringConstraintBuilder(validatePassword, "login.validation.length")
  def passwordTopologyConstraint: Constraint[String] = stringConstraintBuilder(TopologyParser.validateTopology, "login.validation.topology")

  private def stringConstraintBuilder(validationCheck: String => Boolean, messageKey: String): Constraint[String] = Constraint({
    inputString =>
      val errors = validationCheck(inputString) match {
        case false => Seq(ValidationError(messageKey))
        case _ => Nil
      }
      if (errors.isEmpty) {
        Valid
      } else {
        Invalid(errors)
      }
  })
  private def validatePassword(password: String) = {
    password.length > 5
  }
}
