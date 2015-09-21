package vsfm.mainapplication

import util._
import vsfm.uitools.ConsoleUi

class MainApplication(name: String) {

  var behavior: Behavior = DefaultBehavior
  var state: State = State(None, None, None)
  val consoleUi = new ConsoleUi(name, handleCommand)

  def handleCommand(command: String) = {
    Commands.toAction
      .andThen(action => (action, state))
      .andThenPartial(showHelp.orElse(behavior.apply.andThen(reflectStateAndBehavior)))
      .orElse(beep)(command)
  }

  def showHelp: PartialFunction[(Action, State), Unit] = {
    case (ShowHelpAction, _) => consoleUi.appendStatus(Commands.all.mkString("\n"))
  }

  def beep: PartialFunction[String, Unit] = {
    case _ => consoleUi.appendStatus("beep")
  }

  def reflectStateAndBehavior(stateAndBehavior: (State, Behavior)): Unit = {
    val (newState, newBehavior) = stateAndBehavior
    state = newState
    behavior = newBehavior
    consoleUi.appendStatus(state.toString)
  }
}