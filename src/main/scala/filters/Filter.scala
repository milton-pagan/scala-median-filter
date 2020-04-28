package filters

import java.awt.image.BufferedImage

trait Filter {
  def filterImage(img: BufferedImage): BufferedImage

  protected def bufferedImageWithPixels(
      img: BufferedImage,
      fx: (Int, Int) => Int
  ): BufferedImage = {
    val w = img.getWidth
    val h = img.getHeight
    val out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)

    (0 until w).foreach(x => {
      (0 until h).foreach(y => {
        out.setRGB(x, y, fx(x, y))
      })
    })

    out
  }
}
