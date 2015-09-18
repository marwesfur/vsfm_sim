package object util {

  implicit class ComposePartial[A, B](pf: PartialFunction[A, B]) {
    def andThenPartial[C](that: PartialFunction[B, C]): PartialFunction[A, C] =
      Function.unlift(pf.lift(_) flatMap that.lift)
  }

  object BindPartial {
    def snd[A, B](b: B): PartialFunction[A, (A, B)] = {
      case a => (a, b)
    }
  }
}
