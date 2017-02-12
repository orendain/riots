package service

/**
  * TODO: Retrieve nextGuestID via cross-instance messaging system.
  */
object GuestIDSupervisor {

  var guestID = 0

  def nextGuestID() = {
    guestID -= 1
    guestID
  }

  def isGuestID(uid: Int) = uid < 0
}
