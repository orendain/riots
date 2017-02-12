package service


object UserRegistrar {
/*
  // TODO: better nextUid ... connect to DB if necessary
  var nextUid = 1;
  def nextID = {
    nextUid += 1
    PlayerID(nextUid)
  }

  // TODO: futurize?
  def registerPlayer(name: String, password: String, teamID: Int) = {
    Logger.debug("UserRegistrar: 1")
    isNameTaken(name) match {
      case false => {
        Logger.debug("UserRegistrar: 2")
        val sb = new PlayerStatBlock
        sb.stats.name = name
        sb.stats.id = nextID
        sb.password = password
        sb.stats.teamID = TeamID(teamID)
        sb.enabled :+ ItemDirectory.playerCooldownItem
        sb.enabled :+ ItemDirectory.playerUpgradeItem
        sb.enabled :+ ItemDirectory.playerShopItem
        EntityDatabase.add(sb.stats.id, sb)

        Logger.debug("UserRegistrar: Registering (" + sb.stats.name + ", " + password + ", " + sb.stats.id.uid + ")")
        Some(sb.stats.id.uid)
      }
      case _ => {
        Logger.debug("UserRegistrar: 3")
        None
      }
    }
  }

  def loginPlayer(name: String, password: String) = {
    EntityDatabase.findPlayerByName(name) match {
      case Some(sb) if sb.password == password => Some(sb.stats.id)
      case _ => None
    }
  }
*/
  def isNameTaken(name: String) = {
  false
    //(EntityDatabase.stats.values count { sb => sb.stats.name.toLowerCase == name.toLowerCase }) > 0
  }
}



/**
 * TODO: Save data to Redis instance (hashes / json) and have another service running that
 * syncs a mongo DB  with the redis instance for backup and persistance.
 * Redis pub/sub to let MongoDB know about new registered user to save.
 */

/**
 * Each instance should sub to redis. Subbing will allow us to send group messages,
 * even when user from another node.
 */
