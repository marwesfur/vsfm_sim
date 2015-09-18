package vsfm

import vsfm.events._
import vsfm.types.Location

object Dispatcher {

  var listeners = Map[Location, Seq[(Event) => Unit]]()
  var openedProjects = Map[Location, Option[ProjectOpened]]()

  def dispatch(event: Event) = {
    val openedProject = event match {
      case o: ProjectOpened => Some(o)
      case _ => None
    }
    openedProjects = openedProjects + (event.location -> openedProject)
    listeners.get(event.location).foreach { _.foreach(_(event)) }
  }


  def subscribe(location: Location, listener: (Event) => Unit) = {
    listeners = listeners + (location -> (listeners.getOrElse(location, Seq()) :+ listener))
    openedProjects.get(location).foreach { _.foreach(listener(_)) }
  }

  def unsubscribe(location: Location, listener: (Event) => Unit) =
    listeners = listeners + (location -> (listeners.getOrElse(location, Seq()).filterNot(_ == listener)))
}
