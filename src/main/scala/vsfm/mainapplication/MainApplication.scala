package vsfm.mainapplication

import notmvc._
import util._
import util.ui.ConsoleUi

class MainApplication(name: String) {

  val consoleUi = new ConsoleUi(name, handleCommand)
  val loop = new Loop(MainState(None, None, None), DefaultBehavior, renderState)

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