package vsfm.mobileapplication

import notmvc.{Loop, Action}
import util._
import util.ui.ConsoleUi
import vsfm.events._

class MobileApplication(name: String) {

  val consoleUi = new ConsoleUi(name, handleCommand)
  val proximitySensor = new ProximitySensor(name, handleGoingToDesk, handleLeavingDesk)
  val loop = new Loop(MobileState(None, None, Seq(), None), new UnsynchronizedBehavior(handleDispatchedEvent), renderState)

  def handleCommand(command: String) = {
    Commands.toAction
      .andThenPartial(showHelp.orElse(loop.handleAction))
      .orElse(beep)(command)
  }

  def handleGoingToDesk(room: String): Unit = {
    loop.handleAction(StartSyncAction(room))
  }

  def handleLeavingDesk(): Unit = {
    loop.handleAction(StopSyncAction)
  }

  def handleDispatchedEvent(event: Event): Unit =
    event match {
      case ProjectOpened(id, _) => loop.handleAction(SyncedProjectOpenedAction(id))
      case ProjectClosed(_) => loop.handleAction(SyncedProjectClosedAction)
      case _ =>
    }

  def showHelp: PartialFunction[Action, Unit] = {
    case ShowHelpAction => consoleUi.appendStatus(Commands.all.mkString("\n"))
  }

  def beep: PartialFunction[String, Unit] = {
    case _ => consoleUi.appendStatus("beep")
  }

  def renderState(state: MobileState): Unit =
    consoleUi
      .appendStatus("Ort: " + state.location.map(_.name).getOrElse("nicht synchronisiert"))
      .appendStatus("Synchronisiertes Projekt: " + state.synchronizedProject)
      .appendStatus("Vorgemerkte Projekte: " + state.rememberedProjects)
      .appendStatus("Projekt in Bearbeitung: " + state.editingProject)
      .appendStatus("-----")
}
