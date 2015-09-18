package vsfm.mobileapplication

import vsfm.events._
import vsfm.types._
import vsfm.{BackendServer, Dispatcher}

class MobileApplication(name: String) {

  private val ui = new Ui("Tablet " + name, this)
  private var state = State()

  def handleDispatchedEvent(event: Event) =
   event match {
     case ProjectOpened(id, _) => mainApplicationShowingProject(id)
     case _ =>
   }

  def mainApplicationShowingProject(id: Int) =
    BackendServer.projectById(id)
      .foreach { project =>
        state = state.copy(synchronizedProject = Some(project))
        ui.showProjectDetails(project)
      }



  def goToDesk(room: String) =
    ProximitySensor.enterPersonalZone(Location(room))

  def leaveDesk(room: String) =
    ProximitySensor.leavePersonalZone(Location(room))



  object ProximitySensor {
    val listener = handleDispatchedEvent _

    def enterPersonalZone(location: Location) =
      Dispatcher.subscribe(location, listener)

    def leavePersonalZone(location: Location) =
      Dispatcher.unsubscribe(location, listener)
  }
}
