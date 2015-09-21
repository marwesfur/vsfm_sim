package object util {

  implicit class ComposePartial[A, B](pf: PartialFunction[A, B]) {
    def andThenPartial[C](that: PartialFunction[B, C]): PartialFunction[A, C] =
      Function.unlift(pf.lift(_) flatMap that.lift)
  }
}
