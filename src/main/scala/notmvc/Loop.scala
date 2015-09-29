package notmvc

class Loop[TState <: State](initialState: TState, initialBehavior: Behavior[TState], renderState: TState => Unit) {

  var behavior: Behavior[TState] = initialBehavior
  var state: TState = initialState

  renderState(state)

  def handleAction = new PartialFunction[Action, Unit] {
    override def isDefinedAt(action: Action): Boolean =
      behavior.apply.isDefinedAt(action, state)

    override def apply(action: Action): Unit =
      behavior.apply.andThen(reflectStateAndBehavior)(action, state)
  }

  def reflectStateAndBehavior(stateAndBehavior: (TState, Behavior[TState])): Unit = {
    val (newState, newBehavior) = stateAndBehavior
    state = newState
    behavior = newBehavior
    renderState(state)
  }
}
