package model.event

/**
 * @param teams First team is the attacking team, second is the defending team.
 */
/*class SimpleAttack(teams: Seq[TeamID]) extends Event(teams) {


  var attack, defense = 0
  var attackers, defenders = Map[Int, Int]()

  def start() = {

    val msg = "You are ATTACKING team " + teams(2).name + "! Click to help out!"
    val msg2 = "You are UNDER ATTACK by team " + teams(1).name + "! Click to defend!"

    val json = CommandFormatter.build(TeamAttackCmd("atk", "Attack!", msg))
    val json2 = CommandFormatter.build(TeamAttackCmd("dfn", "Defend!", msg2))

    UserStore.players filter { p =>
      UserDatabase(p._2.uid).team == teams(1)
    } foreach (p => p._2.out ! json)

    UserStore.players filter { p =>
      UserDatabase(p._2.uid).team == teams(2)
    } foreach (p => p._2.out ! json2)

  }

  def end() = {
    ChatManager.sendNotification(
      if (attack > defense) {
        "Attack succeeded"
      } else {
        "Attack falied"
      }
    )
  }

  def interact(uid: Int, cmd: EventCommand) = {
    val t = involvedWith(uid).get
    if (t == teams(1)) {
      cmd match {
        case Click1 => playerAttack(uid)
        case PowerPoint => extraAttack(uid)
        case _ =>
      }
    }
    else if (t == teams(2)) {
      cmd match {
        case Click1 => playerDefense(uid)
        case PowerPoint => extraDefense(uid)
        case _ =>
      }
    }
  }

  private def playerAttack(uid: Int) {
    if (!attackers.contains(uid)) {
      attackers += (uid -> 0)
      attack += 1
    }
  }

  private def playerDefense(uid: Int) {
    if (!defenders.contains(uid)) {
      defenders += (uid -> 0)
      defense += 1
    }
  }

  private def extraAttack(uid: Int) {
    playerAttack(uid)
    if (UserDatabase(uid).powerpoints > 0) {
      UserDatabase(uid).powerpoints -= 1
      attackers += (uid -> (attackers(uid)+1))
      attack += 1
    }
  }

  private def extraDefense(uid: Int) {
    playerDefense(uid)
    if (UserDatabase(uid).powerpoints > 0) {
      UserDatabase(uid).powerpoints -= 1
      defenders += (uid -> (defenders(uid)+1))
      defense += 1
    }
  }]


}
*/
