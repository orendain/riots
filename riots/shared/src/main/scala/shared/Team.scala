package shared

object Team {

  val teams = Map[Int, Team](
    (1 -> Team("The Janitors")),
    (2 -> Team("The Chess Club")),
    (3 -> Team("The Dropouts")),
    (4 -> Team("The Suits"))
  )

  //def apply(id: TeamID) = teams(id.id)
}

case class Team(name: String)
