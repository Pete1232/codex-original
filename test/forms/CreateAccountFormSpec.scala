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
    accountForm.bind(Map("userId" -> "user", "password" -> "p2Ssword"))
      .hasErrors mustBe false
  }
  it must "not validate an empty userId field" in {
    val error = accountForm.bind(Map("userId" -> "", "password" -> ""))
      .errors
      .head
    error.key mustBe "userId"
    error.message mustBe "error.required"
  }
  it must "not validate an empty password field" in {
    val error = accountForm.bind(Map("userId" -> "user", "password" -> ""))
      .errors
      .head
    error.key mustBe "password"
    error.message mustBe "error.required"
  }
  it must "not validate a password that is too short" in {
    testForAnError("user", "pass", "login.validation.length")
  }
  it must "requre a valid password topology" in {
    val error = accountForm.bind(Map("userId" -> "user", "password" -> "password"))
      .errors
      .head
    error.message mustBe "login.validation.topology"
  }
}
