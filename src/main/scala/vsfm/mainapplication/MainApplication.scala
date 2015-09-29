package vsfm.mainapplication

import notmvc._
import util._
import util.ui.ConsoleUi

class Loop(initialBehavior: MainBehavior, initialState: MainState, renderState: MainState => Unit) {

  var behavior: MainBehavior = initialBehavior
  var state: MainState = initialState

  def handleAction = new PartialFunction[Action, Unit] {
    override def isDefinedAt(action: Action): Boolean =
      behavior.apply.isDefinedAt(action, state)

    override def apply(action: Action): Unit =
      behavior.apply.andThen(reflectStateAndBehavior)(action, state)
  }

  def reflectStateAndBehavior(stateAndBehavior: (MainState, MainBehavior)) = {
    val (newState, newBehavior) = stateAndBehavior
    state = newState
    behavior = newBehavior
    renderState(state)
  }
}

class MainApplication(name: String) {

  val consoleUi = new ConsoleUi(name, handleCommand)
  val loop = new Loop(DefaultBehavior, MainState(None, None, None), renderState)

  def handleCommand(command: String) = {
    Commands.toAction
      .andThenPartial(loop.handleAction)
      .orElse(beep)(command)
  }

  def showHelp: PartialFunction[(Action, MainState), Unit] = {
    case (ShowHelpAction, _) => consoleUi.appendStatus(Commands.all.mkString("\n"))
  }

  def beep: PartialFunction[String, Unit] = {
    case _ => consoleUi.appendStatus("beep")
  }

  def renderState(state: MainState): Unit =
    consoleUi.appendStatus(state.toString)
}