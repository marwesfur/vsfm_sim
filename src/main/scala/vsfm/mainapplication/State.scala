package vsfm.mainapplication

import vsfm.types.{Location, Project}

case class State(location: Option[Location], projects: Option[Seq[Project]], openedProject: Option[Project])
