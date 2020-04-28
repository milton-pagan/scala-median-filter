package filters.medianfilter

import filters.Filter
import java.awt.image.BufferedImage
import scala.util.Sorting
import java.awt.Color
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.blocking
import scala.util.Failure
import scala.util.Success
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class ParallelMedian extends Filter {

  override def filterImage(img: BufferedImage): BufferedImage =
    bufferedImageWithPixels(
      img,
      (xStart: Int, yStart: Int, xEnd: Int, yEnd: Int, out: BufferedImage) => {
        var processWindow = (x: Int, y: Int) => {
          val matrixRed = new Array[Int](9)
          val matrixGreen = new Array[Int](9)
          val matrixBlue = new Array[Int](9)

          val RGB = new Array[Int](9)
          var j = 0

          (x until x+2).foreach(xn => {
            (y until y+2).foreach(yn => {
              RGB(j) = img.getRGB(xn, yn)
              j += 1
            })
          })

          
          for (i <- 0 until 9) {
            matrixRed(i) = (RGB(i) >> 16) & 0x000000FF
            matrixGreen(i) = (RGB(i) >> 8) & 0x000000FF
            matrixBlue(i) = (RGB(i)) & 0x000000FF
          }

          Sorting.quickSort(matrixRed)
          Sorting.quickSort(matrixGreen)
          Sorting.quickSort(matrixBlue)

          new Color(matrixRed(5), matrixGreen(5), matrixBlue(5)).getRGB()

        }

        (xStart until xEnd).foreach(x => {
          (yStart until yEnd).foreach(y => {
            out.setRGB(x, y, processWindow(x, y))
          })
        })
      }
    )

  def bufferedImageWithPixels(
      img: BufferedImage,
      filterChunk: (Int, Int, Int, Int, BufferedImage) => Unit
  ): BufferedImage = {
    val w = img.getWidth
    val h = img.getHeight
    var futures = new Array[Future[Unit]](4)
    val out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)

    futures(0) = Future {
      filterChunk(1, 1, w / 2, h / 2, out)
    }

    futures(1) = Future {
      filterChunk(w / 2, 1, w - 1, h / 2, out)
    }

    futures(2) = Future {
      filterChunk(1, h / 2, w / 2, h - 1, out)
    }

    futures(3) = Future {
      filterChunk(w / 2, h / 2, w - 1, h - 1, out)
    }
    (0 until 4).foreach(i => {
      Await.result(futures(i), Duration.Inf)
    })
    out
  }

}
