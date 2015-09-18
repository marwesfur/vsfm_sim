package vsfm.mobileapplication

import vsfm.types.Project

case class State(synchronizedProject: Option[Project], rememberedProjects: Seq[Project], editingProject: Option[Project])

object State {
  def apply(): State =
    apply(None, Seq(), None)
}
