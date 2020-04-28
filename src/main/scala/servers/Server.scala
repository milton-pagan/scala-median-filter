package servers

import akka.actor.{Actor, ActorRef, ActorSystem, PoisonPill, Props}
import filters.Filter
import javax.imageio.ImageIO
import java.io.File
import filters.medianfilter.RegMedian

case object String

class Server(filter: Filter, path: String) extends Actor {

  def receive = {
    case "process" => {
      val t1 = System.nanoTime()
      var img = ImageIO.read(new File(path))
      if (filter.isInstanceOf[RegMedian]) {
        ImageIO.write(
          filter.filterImage(img),
          "jpg",
          new File("out_reg_median.jpg")
        )
        val t2 = System.nanoTime()
        printf("Regular Median done in %fs\n", (t2 - t1) / 1e9)
      } else {
        ImageIO.write(
          filter.filterImage(img),
          "jpg",
          new File("out_par_median.jpg")
        )
        val t2 = System.nanoTime()
        printf("Parallel Median done in %fs\n", (t2 - t1) / 1e9)
      }
    }
    case _ => println("Unknown command.")
  }

}
