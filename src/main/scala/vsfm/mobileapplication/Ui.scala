package vsfm.mobileapplication

import vsfm.types.Project
import vsfm.uitools._

class Ui(name: String, app: MobileApplication) {

  private val consoleUi = new ConsoleUi("Tablet " + name, onCommand)

  object Commands {
    val goToDesk = """go to desk at (.+)""".r
    val leaveDesk = """leave desk at (.+)""".r
    val rememberCurrentProject = """remember current project""".r
    val showRememberedProjects = """show remembered projects""".r
    val editRememberedProject = """edit remembered project (.+)""".r
    val removeRememberedProject = """remove remembered project (.+)""".r
    val backToSynchronizedProject = """back to snychronized project""".r
    val showHelp = """help""".r
    val all = Seq(goToDesk, leaveDesk, rememberCurrentProject, showRememberedProjects, editRememberedProject, removeRememberedProject, backToSynchronizedProject, showHelp)
  }

  def onCommand: PartialFunction[String, Unit] = {
    case Commands.goToDesk(room) => app.goToDesk(room)
    case Commands.leaveDesk(room) => app.leaveDesk(room)
    case Commands.showHelp() => consoleUi.appendStatus(Commands.all.mkString("\n"))
  }

  def showProjectDetails(project: Project) =
    consoleUi.appendStatus("showing details: " + project.toString)
}
