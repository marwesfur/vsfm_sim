package vsfm.mainapplication

import notmvc.Action

case class LoginAction(room: String) extends Action
object LogoutAction extends Action
object LoadProjectsAction extends Action
case class OpenProjectAction(id: Int) extends Action
object CloseProjectAction extends Action

object ShowHelpAction extends Action