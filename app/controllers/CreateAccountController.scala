package controllers

import com.google.inject.Inject
import forms.CreateAccountForm
import play.Logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}

class CreateAccountController @Inject()(implicit webJarAssets: WebJarAssets, val messagesApi: MessagesApi)
  extends Controller with I18nSupport{
  val accountForm = new CreateAccountForm().form
  val create = Action {
    Ok(views.html.create(accountForm))
  }

  val createPost = Action { implicit request =>
    accountForm.bindFromRequest.fold(
      formWithErrors => {
        formWithErrors.errors.foreach(error => Logger.info(s"Validation error on field ${error.messages}"))
        BadRequest(views.html.create(formWithErrors))
      },
      userData => {
        Redirect(routes.HomeController.home)
          .withSession("userId" -> userData.userId)
      }
    )
  }
}
