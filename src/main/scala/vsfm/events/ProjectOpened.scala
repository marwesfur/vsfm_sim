package vsfm.events

import vsfm.types.Location

case class ProjectOpened(projectId: Int, location: Location) extends Event
