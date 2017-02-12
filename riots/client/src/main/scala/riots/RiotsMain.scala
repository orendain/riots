package riots

import scala.scalajs.js
import scala.scalajs.js.JSON
//import scalatags.Text.all._
import org.scalajs.{jquery, dom}
import org.scalajs.dom.{CloseEvent, ErrorEvent, Event, MessageEvent}
import org.scalajs.jquery.{JQuery, jQuery}
import shared._
import upickle.default._

import scala.scalajs.js.Dynamic.{literal => lit}
import scalatags.JsDom.all._

@js.native
trait JQueryColorbox extends JQuery {
  def colorbox(): this.type = ???
}
object JQueryColorbox {
  implicit def jq2colorbox(jq: JQuery): JQueryColorbox =
    jq.asInstanceOf[JQueryColorbox]
}


@js.native
trait JQueryBootstrap extends JQuery {
  def modal(): this.type = ???
}
object JQueryBootstrap {
  implicit def jq2colorbox(jq: JQuery): JQueryBootstrap =
    jq.asInstanceOf[JQueryBootstrap]
}


@js.native
trait JQueryBootstrapProgressBar extends JQuery {
  def progressbar(): this.type = ???
}
object JQueryBootstrapProgressBar {
  implicit def jq2bsprogressbar(jq: JQuery): JQueryBootstrapProgressBar =
    jq.asInstanceOf[JQueryBootstrapProgressBar]
}

object RiotsMain extends js.JSApp {

  var ws: dom.WebSocket = _
  var chatPanel: JQuery = _
  var chatMessages: JQuery = _
  var playerStatsPanel: JQuery = _
  var teamStatsPanel: JQuery = _

  /**
    *
    */
  def main() {
    onDocumentReady()
  }

  def onDocumentReady() {
    jQuery(dom.document).ready { () => {
      initWS()
      initPanels()

      registerHandlers()
    }}
  }

  def registerHandlers() {
    //jQuery("#header a[href='register']").asInstanceOf[JQueryColorbox].colorbox()
    jQuery("#header a").asInstanceOf[JQueryColorbox].colorbox()
  }

/*
  // Contact
  def initContact() {
    jQuery.ajax(lit(
      url = "/contact",
      success = { (data: js.Any, textStatus: String, jqXHR: jquery.JQueryXHR) =>
        //Console.println(s"data=$data,text=$textStatus,jqXHR=$jqXHR")
        jQuery("#contact-form-wrapper").html(data.toString)
        registerContactHandlers()
      },
      error = { (jqXHR: jquery.JQueryXHR, textStatus: String, errorThrow: String) =>
        //Console.println(s"jqXHR=$jqXHR,text=$textStatus,err=$errorThrow")
      }
    ).asInstanceOf[jquery.JQueryAjaxSettings])

  }

  def registerContactHandlers() {
    jQuery("#contact-submit").click(submitContact _)
  }
*/


  /**
    *
    */
  def initWS() {
    ws = new dom.WebSocket("ws://localhost:9000/ws")

    ws.onopen = (e: Event) => Console.println("Websocket open")
    ws.onclose = (e: CloseEvent) => Console.println("WebSocket closed" + e.reason + " " + e.code)
    ws.onerror = (e: ErrorEvent) => Console.println("Websocket error: " + e.message)
    ws.onmessage = (e: MessageEvent) => processWSMsg(e)
  }


  /**
    *
    */
  def initPanels() {
    chatPanel = jQuery("#chat-panel")
    chatPanel.find("#chat-send").click(processChatCommand _)
    chatMessages = chatPanel.find("#chat-msgs")

    playerStatsPanel = jQuery("#player-stats-panel")
    teamStatsPanel = jQuery("#team-stats-panel")

    Console.println("Panels initialized.")
  }

  /**
    *
    */
  def processWSMsg(msg: MessageEvent) {
    val obj = JSON.parse(msg.data.toString)
    val cmd = read[Command](obj.cmd.toString)
    processCommand(cmd)
  }

  def processChatCommand() {
    val msg = jQuery("#chat-input").value().toString.trim
    val firstPart = msg.split(' ').head

    firstPart match {
      case "/help" => printChatHelp()
      case "/t" => ws.send(write(TeamChatInCmd(msg.substring(3).trim)))
      case _ => ws.send(write(PlayerChatInCmd(msg)))
    }
  }

  def printChatHelp() {
    addSystemMessage("The Riots - Chat Commands")
    addSystemMessage("[text] - Send a message to all players")
    addSystemMessage("/t [text] - Send a message to your team")
    addSystemMessage("/help - Display this help text")
  }

  /**
    *
    */
  def processCommand(cmd: Command) {
    Console.println("processCommand: " + cmd.toString)
    cmd match {
      case PlayerChatOutCmd(msg, snd, cls) => addPlayerChatMessage(msg, snd, cls)
      case TeamChatOutCmd(msg, snd, cls) => addTeamChatMessage(msg, snd, cls)
      case NotificationChatOutCmd(msg, cls) => addNotificationMessage(msg, cls)

      case PlayerStatsUpdateOutCmd(stats) => updatePlayerStats(stats)
      case TeamStatsUpdateOutCmd(teamID, stats) => updateTeamStats(teamID, stats)

      case ItemUpdateOutCmd(itemUpdateType) => updateItem(itemUpdateType)

      case MultipleCmds(ups) => ups foreach { processCommand(_) }

      case ItemUseSuccessOutCmd(_) =>
      case ItemUseFailOutCmd(_) =>

      case EventStartOutCmd(eventID: Int, event: EventType) =>
        displayEventStart(eventID, event)

      case _ =>
    }
  }

  /*
  <div class="modal fade" tabindex="-1" role="dialog">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">Modal title</h4>
      </div>
      <div class="modal-body">
        <p>One fine body&hellip;</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary">Save changes</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
   */

  def displayEventStart(eventID: Int, event: EventType) = {

    val m = div(id := "event_modal_1", cls := "modal fade", role := "dialog", tabindex := "-1")(
      div(cls := "modal-dialog")(
        div(cls := "modal-content")(
          div(cls := "modal-header")(
            "WAR!"
          ),
          div(cls := "modal-body")(
            event.message
          ),
          div(cls := "modal-footer")(
            event match {
              case EventClick1OptionOutCmd(_, click1) =>
                button(cls := "click1")(
                  click1
                )
              case EventClick2OutCmd(_, click1, click2) =>
                button(cls := "click1")(
                  click1
                )
            }
          )
        )
      )
    ).render

    val body = jQuery("body")
    body.append(m)

    jQuery("#event_modal_1").asInstanceOf[JQueryBootstrap].modal()


    val item = jQuery("#event_modal_1 .click1")
    def what = { x: Unit =>
      Console.println("what.")
      ws.send(write(EventActionInCmd(eventID, Click1(0))))
    }
    def what2 = {
      Console.println("what2.")
      ws.send(write(EventActionInCmd(eventID, Click1(0))))
    }
    item.click(what2 _)
    //item.click(TESTCLICK1 _)
    //item.click{
      //ws.send(write(EventActionInCmd(eventID, Click1(0))))
      //Int => Unit = { onEventClick(eventID) }
      //onEventClick2
      //eventID2: Unit => ws.send(write(EventActionInCmd(eventID, Click1(0))))

      //Console.println("SUP")
      //what
      //onEventClick2 _
    //}
  }

  def TESTCLICK1() {
    ws.send(write(EventActionInCmd(1002, Click1(0))))
  }

  def onEventClick(eventID: Int) {
    ws.send(write(EventActionInCmd(eventID, Click1(0))))
  }

  def onEventClick2 = { eventID: Int => ws.send(write(EventActionInCmd(eventID, Click1(0)))) }

  val playerItemPanelSelector = ".player-items"
  val teamItemPanelSelector = ".team-items"

  def updateItem(itemUpdateType: ItemUpdateType) {

    val cooldownItemsPanel = jQuery("#cooldown-items")
    val upgradeItemsPanel = jQuery("#upgrade-items")
    val shopItemsPanel = jQuery("#shop-items")

    val (panelType, itemBuilder) = itemUpdateType.itemUpdate match {
      case update: CooldownItemUpdate =>
        (cooldownItemsPanel, itemUpdateToCooldownItemPanel(update) _)
      case update: UpgradeItemUpdate =>
        (upgradeItemsPanel, itemUpdateToUpgradeItemPanel(update) _)
      case update: ShopItemUpdate =>
        (shopItemsPanel, itemUpdateToShopItemPanel(update) _)
    }

    val (panelSelector, itemClass) = itemUpdateType match {
      case IndividualItemUpdateOutCmd(update) =>
        (playerItemPanelSelector, "player")
      case TeamItemUpdateOutCmd(update) =>
        (teamItemPanelSelector, "team")
    }

    val panel = itemBuilder(itemClass)
    panelType.find(panelSelector).append(panel)

    val item = jQuery(s"#item_${itemUpdateType.itemUpdate.id}")
    //item.click(processCooldownClick _)
    item.click(processClick2(itemClass) _)
  }

  def processClick(event: jquery.JQueryEventObject) = {
    val elem = jQuery(event.delegateTarget)
    val id = elem.data("id").toString.toInt
    ws.send(write(TeamItemInCmd(id)))
  }

  def processClick2(str: String)(event: jquery.JQueryEventObject) = {
    val elem = jQuery(event.delegateTarget)
    val id = elem.data("id").toString.toInt

    val inCmd = str match {
      case "player" => ws.send(write(IndividualItemInCmd(id)))
      case "team" => ws.send(write(TeamItemInCmd(id)))
    }
  }

  def processCooldownClick(event: jquery.JQueryEventObject) {
    val elem = jQuery(event.delegateTarget)
    val cooldown = elem.data("cooldown").toString()
    val bar = elem.find(".progress .progress-bar")
    bar.css("-webkit-transition", s"width ${cooldown}ms ease-in-out")
    bar.css("-moz-transition", s"width ${cooldown}ms ease-in-out")
    bar.css("-ms-transition", s"width ${cooldown}ms ease-in-out")
    bar.css("-o-transition", s"width ${cooldown}ms ease-in-out")
    bar.css("transition", s"width ${cooldown}ms ease-in-out")

    bar.asInstanceOf[JQueryBootstrapProgressBar].progressbar()
  }

  def onProgressBarFill() = {
    //
  }

  def clearProgressBar() = {
    //
  }

  def itemUpdateToCooldownItemPanel(itemData: CooldownItemUpdate)(itType: String) = {
    val b = button(cls:="item cooldown col-xs-6", id:="item_"+itemData.id, data.id:=itemData.id, data.cooldown := itemData.cooldown, data.itemType := itType)(
      span(cls:="name")(itemData.name),
      span(cls:="dscription")(itemData.description),
      div(cls:="progress")(div(cls:="progress-bar", role:="progessbar", data.transitiongoal:="100"))
    )

    Console.println(s"Building $b")
    b.render
  }

  def itemUpdateToUpgradeItemPanel(itemData: UpgradeItemUpdate)(itemType: String) = {
    button(cls:="item upgrade col-xs-6", id:="item_"+itemData.id, data.id:=itemData.id, data.itemType := itemType)(
      span(cls:="name")(itemData.name),
      span(cls:="dscription")(itemData.description),
      span(cls:="cost")(itemData.cost)
    ).render
  }

  def itemUpdateToShopItemPanel(itemData: ShopItemUpdate)(itemType: String) = {
    button(cls:="item shop col-xs-6", id:="item_"+itemData.id, data.id:=itemData.id, data.itemType := itemType)(
      span(cls:="name")(itemData.name),
      span(cls:="dscription")(itemData.description),
      span(cls:="cost")(itemData.cost)
    ).render
  }

  def cooldownTransitionStyle(cooldown: Long) =
    s"-webkit-transition:width ${cooldown}ms ease-in-out;-moz-transition:width ${cooldown}ms ease-in-out;-ms-transition:width ${cooldown}ms ease-in-out;-o-transition:width ${cooldown}ms ease-in-out;transition:width ${cooldown}ms ease-in-out"

  def updatePlayerStats(stats: PlayerStatsUpdate) {
    playerStatsPanel.find(".resources .value").text(stats.resources.toString)
    playerStatsPanel.find(".power .value").text(stats.power.toString)

    playerStatsPanel.find(".job .value").text(stats.job.toString)
    playerStatsPanel.find(".playTime .value").text(stats.playTime.toString)

    playerStatsPanel.find(".strength .value").text(stats.strength.toString)
    playerStatsPanel.find(".speed .value").text(stats.speed.toString)
    playerStatsPanel.find(".patience .value").text(stats.patience.toString)
    playerStatsPanel.find(".luck .value").text(stats.luck.toString)

    playerStatsPanel.find(".discount .value").text(stats.discount.toString)
  }

  def updateTeamStats(teamID: TeamID, stats: TeamStatsUpdate) {
    val panel = teamStatsPanel.find(".teamstats.t" + teamID.id)
    panel.find(".resources .value").text(stats.resources.toString)
    panel.find(".power .value").text(stats.power.toString)
    panel.find(".target .value").text(stats.targetID.toString)

    panel.find(".strength .value").text(stats.strength.toString)
    panel.find(".speed .value").text(stats.speed.toString)

    panel.find(".discount .value").text(stats.discount.toString)
    panel.find(".activePlayers .value").text(stats.activePlayers.toString)
  }

  /* Chat Messages */
  def addPlayerChatMessage(msg: String, name: String, clss: String) {
    addChatMessage(name, msg, clss + " player")
  }

  def addTeamChatMessage(msg: String, name: String, clss: String) {
    addChatMessage(name, msg, clss + " team")
  }

  def addNotificationMessage(msg: String, clss: String) {
    addSystemMessage(msg, clss + " notification")
  }

  def addSystemMessage(msg: String, clss: String = "") {
    addChatMessage("System", msg, clss)
  }

  def addChatMessage(name: String, msg: String, clss: String) {
    chatMessages.append(
      div(cls:=clss)(
        span(cls:="name")(s"[$name]:"),
        span(cls:="msg")(s"$msg")
      ).render
    )
  }
}
