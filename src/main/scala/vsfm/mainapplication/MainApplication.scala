package vsfm.mainapplication

import vsfm.events._
import vsfm.types._
import vsfm.{BackendServer, Dispatcher}

class MainApplication(name: String) {

  private val ui = new Ui("Hauptanwendung " + name, this)
  private var state: State = State(None, None, None)

  object WithState {
    def unapply[T](value: T): Option[(T, State)] =
      Some(value, state)
  }

  def loginNew: PartialFunction[String, Unit] = {
    case WithState(room, State(None, _, _)) => updateState(state.copy(location = Some(Location(room))))
  }

  def login(room: String) =
    state match {
      case State(None, _, _) => updateState(state.copy(location = Some(Location(room))))
    }

  def logout(): Unit =
    state match {
      case State(Some(_), _, _) => updateState(state.copy(location = None))
    }

  def loadProjects() =
    state match {
      case State(Some(_), _, _) => updateState(state.copy(projects = Some(BackendServer.allProjects), openedProject = None))
    }

  def openProject(id: Int) =
    state match {
      case State(Some(_), Some(projects), _) if projects.exists(_.id == id) =>
        BackendServer.projectById(id).foreach { project =>
          updateState(state.copy(openedProject = Some(project)))
          Dispatcher.dispatch(ProjectOpened(id, state.location.get))
        }
    }

  def closeProject() =
    state match {
      case State(Some(_), _, Some(_)) =>
        updateState(state.copy(openedProject = None))
        Dispatcher.dispatch(ProjectClosed(state.location.get))
    }

  private def updateState(newState: State) = {
    state = newState
    ui.showState(state)
  }
}
