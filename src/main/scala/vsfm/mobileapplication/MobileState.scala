package vsfm.mobileapplication

import notmvc.State
import vsfm.types.{Location, Project}

case class MobileState(location: Option[Location], synchronizedProject: Option[Project], rememberedProjects: Seq[Project], editingProject: Option[Project]) extends State