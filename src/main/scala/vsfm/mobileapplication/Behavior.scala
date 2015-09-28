package vsfm.mobileapplication

import vsfm.events.Event
import vsfm.types.Location
import vsfm.{Dispatcher, BackendServer}

trait Behavior {
  def apply: PartialFunction[(Action, State), (State, Behavior)]
}

case class UnsynchronizedBehavior(listener: Event => Unit) extends Behavior {

  override def apply: PartialFunction[(Action, State), (State, Behavior)] = {
    case (StartEditAction(id), state@State(_, _, rememberedProjects, _)) if rememberedProjects.exists(_.id == id) =>
      (state.copy(editingProject = BackendServer.projectById(id)), this)

    case (CommitEditAction, state@State(_, _, rememberedProjects, Some(project))) =>
      (state.copy(editingProject = None, rememberedProjects = rememberedProjects.filterNot(_.id == project.id)), this)

    case (AbortEditAction, state@State(_, _, _, Some(project))) =>
      (state.copy(editingProject = None), this)

    case (UnrememberProjectAction(id), state@State(_, _, rememberedProjects, None)) if rememberedProjects.exists(_.id == id) =>
      (state.copy(rememberedProjects = rememberedProjects.filterNot(_.id == id)), this)
    case (UnrememberProjectAction(id), state@State(_, _, rememberedProjects, Some(editingProject))) if rememberedProjects.exists(_.id == id) && editingProject.id != id =>
      (state.copy(rememberedProjects = rememberedProjects.filterNot(_.id == id)), this)

    case (StartSyncAction(room), state@State(None, _, _, _)) =>
      val openedProject = Dispatcher.subscribe(Location(room), listener)
      val syncedProject = openedProject.flatMap(p => BackendServer.projectById(p.projectId))
      (state.copy(location = Some(Location(room)), editingProject = None, synchronizedProject = syncedProject), SynchronizedBehavior(listener))
  }
}

case class SynchronizedBehavior(listener: Event => Unit) extends Behavior {

  override def apply: PartialFunction[(Action, State), (State, Behavior)] = {
    case (SyncedProjectOpenedAction(id), state) =>
      (state.copy(synchronizedProject = BackendServer.projectById(id)), this)

    case (SyncedProjectClosedAction, state) =>
      (state.copy(synchronizedProject = None), this)

    case (RememberSyncedProjectAction, state@State(_, Some(syncedProject), rememberedProjects, _)) =>
      (state.copy(rememberedProjects = rememberedProjects :+ syncedProject), this)

    case (UnrememberProjectAction(id), state@State(_, _, rememberedProjects, _)) if rememberedProjects.exists(_.id == id) =>
      (state.copy(rememberedProjects = rememberedProjects.filterNot(_.id == id)), this)

    case (StopSyncAction, state@State(Some(location), _, _, _)) =>
      Dispatcher.unsubscribe(location, listener)
      (state.copy(location = None, synchronizedProject = None), UnsynchronizedBehavior(listener))
  }
}