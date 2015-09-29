package vsfm.mobileapplication

import notmvc.Action
import vsfm.events.Event
import vsfm.types.Location
import vsfm.{Dispatcher, BackendServer}

case class UnsynchronizedBehavior(listener: Event => Unit) extends MobileBehavior {

  override def apply: PartialFunction[(Action, MobileState), (MobileState, MobileBehavior)] = {
    case (StartEditAction(id), state@MobileState(_, _, rememberedProjects, _)) if rememberedProjects.exists(_.id == id) =>
      (state.copy(editingProject = BackendServer.projectById(id)), this)

    case (CommitEditAction, state@MobileState(_, _, rememberedProjects, Some(project))) =>
      (state.copy(editingProject = None, rememberedProjects = rememberedProjects.filterNot(_.id == project.id)), this)

    case (AbortEditAction, state@MobileState(_, _, _, Some(project))) =>
      (state.copy(editingProject = None), this)

    case (UnrememberProjectAction(id), state@MobileState(_, _, rememberedProjects, None)) if rememberedProjects.exists(_.id == id) =>
      (state.copy(rememberedProjects = rememberedProjects.filterNot(_.id == id)), this)
    case (UnrememberProjectAction(id), state@MobileState(_, _, rememberedProjects, Some(editingProject))) if rememberedProjects.exists(_.id == id) && editingProject.id != id =>
      (state.copy(rememberedProjects = rememberedProjects.filterNot(_.id == id)), this)

    case (StartSyncAction(room), state@MobileState(None, _, _, _)) =>
      val openedProject = Dispatcher.subscribe(Location(room), listener)
      val syncedProject = openedProject.flatMap(p => BackendServer.projectById(p.projectId))
      (state.copy(location = Some(Location(room)), editingProject = None, synchronizedProject = syncedProject), SynchronizedBehavior(listener))
  }
}

case class SynchronizedBehavior(listener: Event => Unit) extends MobileBehavior {

  override def apply: PartialFunction[(Action, MobileState), (MobileState, MobileBehavior)] = {
    case (SyncedProjectOpenedAction(id), state) =>
      (state.copy(synchronizedProject = BackendServer.projectById(id)), this)

    case (SyncedProjectClosedAction, state) =>
      (state.copy(synchronizedProject = None), this)

    case (RememberSyncedProjectAction, state@MobileState(_, Some(syncedProject), rememberedProjects, _)) =>
      (state.copy(rememberedProjects = rememberedProjects :+ syncedProject), this)

    case (UnrememberProjectAction(id), state@MobileState(_, _, rememberedProjects, _)) if rememberedProjects.exists(_.id == id) =>
      (state.copy(rememberedProjects = rememberedProjects.filterNot(_.id == id)), this)

    case (StopSyncAction, state@MobileState(Some(location), _, _, _)) =>
      Dispatcher.unsubscribe(location, listener)
      (state.copy(location = None, synchronizedProject = None), UnsynchronizedBehavior(listener))
  }
}