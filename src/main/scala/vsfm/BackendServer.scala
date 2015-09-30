package vsfm

import vsfm.types.Project

object BackendServer {

  private var projects = Seq(Project(1, "Auto"), Project(2, "Fahrrad"), Project(3, "Bobby-Car"))

  def allProjects = projects

  def projectById(id: Int) = allProjects.find(_.id == id)

  def saveProject(project: Project) = {
    projects = projects.updated(projects.indexWhere(_.id == project.id), project)
  }
}
