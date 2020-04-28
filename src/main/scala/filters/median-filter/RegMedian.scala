package filters.medianfilter

import java.awt.image.BufferedImage
import scala.util.Sorting
import filters.Filter
import java.awt.Color

class RegMedian extends Filter {

  override def filterImage(img: BufferedImage): BufferedImage =
    bufferedImageWithPixels(
      img,
      (x: Int, y: Int) => {
        val matrixRed = new Array[Int](9)
        val matrixGreen = new Array[Int](9)
        val matrixBlue = new Array[Int](9)

        var i = 0
        var RGB = 0
        (x until (x + 2) % img.getWidth()).foreach(xn => {
          (y until (y + 2) % img.getHeight()).foreach(yn => {
            RGB = img.getRGB(xn, yn)
            matrixRed(i) = (RGB >> 16) & 0x000000FF
            matrixGreen(i) = (RGB >> 8) & 0x000000FF
            matrixBlue(i) = (RGB) & 0x000000FF

            i += 1
          })
        })

        Sorting.quickSort(matrixRed)
        Sorting.quickSort(matrixGreen)
        Sorting.quickSort(matrixBlue)

        new Color(matrixRed(5), matrixGreen(5), matrixBlue(5)).getRGB()
      }
    )

}
