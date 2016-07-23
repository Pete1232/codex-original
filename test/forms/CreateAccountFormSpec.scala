package forms

import config.UnitSpec

class CreateAccountFormSpec extends UnitSpec{
  val accountForm = new CreateAccountForm().form

  def testForAnError(userId: String, password: String, errorId: String) = {
    accountForm
      .bind(Map("userId" -> userId, "password" -> password))
      .errors
      .head
      .message mustBe errorId
  }

  "accountForm" must "validate a good userId/password combination" in {
    accountForm.bind(Map("userId" -> "user", "password" -> "password"))
      .hasErrors mustBe false
  }
}
