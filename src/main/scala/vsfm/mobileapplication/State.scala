package vsfm.mobileapplication

import vsfm.types.{Location, Project}

case class State(location: Option[Location], synchronizedProject: Option[Project], rememberedProjects: Seq[Project], editingProject: Option[Project])