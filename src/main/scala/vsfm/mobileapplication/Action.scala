package vsfm.mobileapplication

import notmvc.Action

case class StartEditAction(id: Int) extends Action
object CommitEditAction extends Action
object AbortEditAction extends Action
case class UnrememberProjectAction(id: Int) extends Action
case class StartSyncAction(room: String) extends Action
object StopSyncAction extends Action
case class SyncedProjectOpenedAction(id: Int) extends Action
object SyncedProjectClosedAction extends Action
object RememberSyncedProjectAction extends Action
object ShowHelpAction extends Action