package vsfm.mainapplication

import vsfm.events.{ProjectOpened, ProjectClosed}
import vsfm.{Dispatcher, BackendServer}
import vsfm.types.Location

trait Behavior {
  def apply: PartialFunction[(Action, State), (State, Behavior)]
}

object DefaultBehavior extends Behavior {

  override def apply: PartialFunction[(Action, State), (State, Behavior)] = {
    case (LoginAction(room), state) =>
      (state.copy(location = Some(Location(room))), LoggedInBehavior)
  }
}

object LoggedInBehavior extends Behavior {

  override def apply: PartialFunction[(Action, State), (State, Behavior)] = {
    case (LogoutAction, state) =>
      (state.copy(location = None, projects = None, openedProject = None), DefaultBehavior)

    case (LoadProjectsAction, state) =>
      (state.copy(projects = Some(BackendServer.allProjects)), this)

    case (OpenProjectAction(id), state@State(Some(location), Some(projects), _)) if projects.exists(_.id == id) =>
      val project = BackendServer.projectById(id).get // project is guaranteed to exist at this point
      Dispatcher.dispatch(ProjectOpened(project.id, location))
      (state.copy(openedProject = Some(project)), this)

    case (CloseProjectAction, state@State(Some(location), _, Some(_))) =>
      Dispatcher.dispatch(ProjectClosed(location))
      (state.copy(openedProject = None), this)
  }
}