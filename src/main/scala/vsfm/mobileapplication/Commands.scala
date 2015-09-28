package vsfm.mobileapplication

object Commands {
  val remember = """remember""".r
  val startEdit = """edit (\d+)""".r
  val abortEdit = """abort""".r
  val commitEdit = """save""".r
  val unremember = """unremember (\d+)""".r

  val showHelp = """show help""".r
  val all = Seq(remember, startEdit, commitEdit, unremember, showHelp)

  def toAction: PartialFunction[String, Action] = {
    case remember() => RememberSyncedProjectAction
    case startEdit(id) => StartEditAction(id.toInt)
    case commitEdit() => CommitEditAction
    case abortEdit() => AbortEditAction
    case unremember(id) => UnrememberProjectAction(id.toInt)
    case showHelp() => ShowHelpAction
  }
}

