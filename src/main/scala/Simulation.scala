import vsfm.mainapplication.MainApplication
import vsfm.mobileapplication.MobileApplication
import vsfm.types.Location

object Simulation {
  def main(args: Array[String]) {
    val meetingRoomA = Location("A")
    val meetingRoomB = Location("B")

    val appInA = new MainApplication("App in A")
    val appInB = new MainApplication("App in B")

    val firstTablet = new MobileApplication("First Tablet")
    val sndTablet = new MobileApplication("Second Tablet")
  }
}