package notmvc

trait Behavior[TState <: State] {
  def apply: PartialFunction[(Action, TState), (TState, Behavior[TState])]
}
