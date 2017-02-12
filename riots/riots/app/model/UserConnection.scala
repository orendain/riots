package model

import akka.actor.ActorRef

case class UserConnection (
  uid: Int,
  outActr: ActorRef
)
