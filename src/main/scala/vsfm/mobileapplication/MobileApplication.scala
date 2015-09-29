package vsfm.mobileapplication

import notmvc.Action
import util._
import util.ui.ConsoleUi
import vsfm.events._

class MobileApplication(name: String) {

  var state: MobileState = MobileState(None, None, Seq(), None)
  var behavior: MobileBehavior = new UnsynchronizedBehavior(handleDispatchedEvent)
  val consoleUi = new ConsoleUi(name, handleCommand)
  val proximitySensor = new ProximitySensor(name, handleGoingToDesk, handleLeavingDesk)

  def handleCommand(command: String) = {
    Commands.toAction
      .andThen(action => (action, state))
      .andThenPartial(showHelp.orElse(behavior.apply.andThen(reflectStateAndBehavior)))
      .orElse(beep)(command)
  }

  def handleGoingToDesk(room: String): Unit = {
    behavior.apply.andThen(reflectStateAndBehavior)(StartSyncAction(room), state)
  }

  def handleLeavingDesk(): Unit = {
    behavior.apply.andThen(reflectStateAndBehavior)(StopSyncAction, state)
  }

  def handleDispatchedEvent(event: Event) =
    event match {
      case ProjectOpened(id, _) => behavior.apply.andThen(reflectStateAndBehavior)(SyncedProjectOpenedAction(id), state)
      case ProjectClosed(_) => behavior.apply.andThen(reflectStateAndBehavior)(SyncedProjectClosedAction, state)
      case _ =>
    }

  def showHelp: PartialFunction[(Action, MobileState), Unit] = {
    case (ShowHelpAction, _) => consoleUi.appendStatus(Commands.all.mkString("\n"))
  }

  def beep: PartialFunction[String, Unit] = {
    case _ => consoleUi.appendStatus("beep")
  }

  def reflectStateAndBehavior(stateAndBehavior: (MobileState, MobileBehavior)): Unit = {
    val (newState, newBehavior) = stateAndBehavior
    state = newState
    behavior = newBehavior
    consoleUi.appendStatus(state.toString)
  }
}
