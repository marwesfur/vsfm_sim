package vsfm.mainapplication

object Commands {
  val login = """login at (.+)""".r
  val logout = """logout""".r
  val loadProjects = """load projects""".r
  val openProject = """open project (\d+)""".r
  val closeProject = """close project""".r
  val showHelp = """show help""".r
  val all = Seq(login, logout, loadProjects, openProject, closeProject, showHelp)

  def toAction: PartialFunction[String, Action] = {
    case login(room) => LoginAction(room)
    case logout() => LogoutAction
    case loadProjects() => LoadProjectsAction
    case openProject(id) => OpenProjectAction(id.toInt)
    case closeProject() => CloseProjectAction
    case showHelp() => ShowHelpAction
  }
}

