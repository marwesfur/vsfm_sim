import vsfm.mainapplication.MainApplication
import vsfm.mobileapplication.MobileApplication

object Simulation {
  def main(args: Array[String]) {
    val firstMainApp = new MainApplication("First Main App")
    val sndMainApp = new MainApplication("Second Main App")

    val firstTablet = new MobileApplication("First Tablet")
    val sndTablet = new MobileApplication("Second Tablet")
  }
}