package vsfm.mainapplication

import util._
import vsfm.uitools.ConsoleUi

class MainApplication(name: String) {

  var behavior: Behavior = DefaultBehavior
  var state: State = State(None, None, None)
  val consoleUi = new ConsoleUi(name, handleCommand)

  def handleCommand(command: String) = {
    val applyBehavior = BindPartial.snd[Action, State](state)
      .andThenPartial(behavior.apply)
      .andThen(reflectStateAndBehavior)

    Commands.toAction
      .andThenPartial(showHelp.orElse(applyBehavior))
      .orElse(beep)(command)
  }

  def showHelp: PartialFunction[Action, Unit] = {
    case ShowHelpAction => consoleUi.appendStatus(Commands.all.mkString("\n"))
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