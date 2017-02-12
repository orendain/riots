package shared

sealed trait Job

object Job {

  case object Gather extends Job
  case object Sabotage extends Job

  val jobs = Map[Int, Job](
    (1 -> Gather),
    (2 -> Sabotage)
  )

  //def apply(id: Int): Job = jobs(id)
}
