package vsfm.mainapplication

import notmvc._
import util._
import util.ui.ConsoleUi

class MainApplication(name: String) {

  var behavior: MainBehavior = DefaultBehavior
  var state: MainState = MainState(None, None, None)
  val consoleUi = new ConsoleUi(name, handleCommand)

  def handleCommand(command: String) = {
    Commands.toAction
      .andThen(action => (action, state))
      .andThenPartial(showHelp.orElse(behavior.apply.andThen(reflectStateAndBehavior)))
      .orElse(beep)(command)
  }

  def showHelp: PartialFunction[(Action, MainState), Unit] = {
    case (ShowHelpAction, _) => consoleUi.appendStatus(Commands.all.mkString("\n"))
  }

  def beep: PartialFunction[String, Unit] = {
    case _ => consoleUi.appendStatus("beep")
  }

  def reflectStateAndBehavior(stateAndBehavior: (MainState, MainBehavior)): Unit = {
    val (newState, newBehavior) = stateAndBehavior
    state = newState
    behavior = newBehavior
    consoleUi.appendStatus(state.toString)
  }
}