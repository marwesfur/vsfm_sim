package vsfm.mainapplication

import vsfm.uitools._

class Ui(name: String, app: MainApplication) {

  private val consoleUi = new ConsoleUi(name, onCommand)

  object Commands {
    val login = """login at (.+)""".r
    val logout = """logout""".r
    val loadProjects = """load projects""".r
    val openProject = """open project (\d+)""".r
    val closeProject = """close project""".r
    val showHelp = """help""".r
    val all = Seq(login, logout, loadProjects, openProject, closeProject, showHelp)
  }

  def onCommand: PartialFunction[String, Unit] = {
    case Commands.login(room) => app.loginNew.lift(room)
    case Commands.logout() => app.logout()
    case Commands.loadProjects() => app.loadProjects()
    case Commands.openProject(id) => app.openProject(id.toInt)
    case Commands.closeProject() => app.closeProject()
    case Commands.showHelp() => consoleUi.appendStatus(Commands.all.mkString("\n"))
  }

  def showState(state: State) =
    consoleUi
      .appendStatus("Login: " + state.location)
      .appendStatus("Project list: " + state.projects)
      .appendStatus("Project details: " + state.openedProject)
}
