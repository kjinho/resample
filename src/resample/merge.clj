(ns resample.merge
  (:import (org.apache.pdfbox.pdmodel PDDocument
                                      PDPage
                                      PDPageContentStream)
           (org.apache.pdfbox.pdmodel.common PDRectangle)
           (org.apache.pdfbox.pdmodel.graphics.image PDImageXObject)))

(defn- add-image-to-page
  "Adds image as a page to the document object"
  [doc ^PDImageXObject image]
  (let [page-size PDRectangle/LETTER
        original-width (.getWidth image)
        original-height (.getHeight image)
        page-width (.getWidth page-size)
        page-height (.getHeight page-size)
        ratio (min (/ page-width original-width) (/ page-height original-height))
        scaled-width (* original-width ratio)
        scaled-height (* original-height ratio)
        x (/ (- page-width scaled-width) 2)
        y (/ (- page-height scaled-height) 2)
        page (PDPage. page-size)]
    (.addPage doc page)
    (with-open [stream (PDPageContentStream. ^PDDocument doc ^PDPage page)]
      (.drawImage stream image x y scaled-width scaled-height))))

(defn merge-images-from-path
  "Merges images provided as a vector of string paths"
  [images output-doc]
  (with-open [doc (PDDocument.)]
    (run! #(add-image-to-page doc (PDImageXObject/createFromFile % doc))
          images)
    (.save doc ^String output-doc)))