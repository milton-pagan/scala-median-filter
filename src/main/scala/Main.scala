import filters.medianfilter.RegMedian
import filters.medianfilter.ParallelMedian
import akka.actor.ActorSystem
import akka.actor.Props
import servers.Server
import akka.actor.PoisonPill
import scala.concurrent.Await

object Main extends App {
  val system = ActorSystem("ImageProcessingSystem")

  println("Please enter image path: ")

  val path = scala.io.StdIn.readLine()

  val regMedian = system.actorOf(
    Props(new Server(new RegMedian(), path)),
    name = "regMedian"
  )
  val parMedian = system.actorOf(
    Props(new Server(new ParallelMedian(), path)),
    name = "parMedian"
  )
  regMedian ! "process"
  parMedian ! "process"

  regMedian ! PoisonPill
  parMedian ! PoisonPill

  system.terminate()
}
