package vsfm.mainapplication

import notmvc.State
import vsfm.types.{Location, Project}

case class MainState(location: Option[Location], projects: Option[Seq[Project]], openedProject: Option[Project]) extends State
