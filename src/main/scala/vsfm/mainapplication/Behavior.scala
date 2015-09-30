package vsfm.mainapplication

import notmvc._
import vsfm.events.{ProjectOpened, ProjectClosed}
import vsfm.{Dispatcher, BackendServer}
import vsfm.types.Location

object DefaultBehavior extends MainBehavior {

  override def apply: PartialFunction[(Action, MainState), (MainState, MainBehavior)] = {
    case (LoginAction(room), state) =>
      (state.copy(location = Some(Location(room))), LoggedInBehavior)
  }
}

object LoggedInBehavior extends MainBehavior {

  override def apply: PartialFunction[(Action, MainState), (MainState, MainBehavior)] = {
    case (LogoutAction, state) =>
      if (state.openedProject.isDefined)
        Dispatcher.dispatch(ProjectClosed(state.location.get))
      (state.copy(location = None, projects = None, openedProject = None), DefaultBehavior)

    case (LoadProjectsAction, state) =>
      (state.copy(projects = Some(BackendServer.allProjects)), this)

    case (OpenProjectAction(id), state@MainState(Some(location), Some(projects), _)) if projects.exists(_.id == id) =>
      val project = BackendServer.projectById(id).get // project is guaranteed to exist at this point
      Dispatcher.dispatch(ProjectOpened(project.id, location))
      (state.copy(openedProject = Some(project)), this)

    case (CloseProjectAction, state@MainState(Some(location), _, Some(_))) =>
      Dispatcher.dispatch(ProjectClosed(location))
      (state.copy(openedProject = None), this)
  }
}