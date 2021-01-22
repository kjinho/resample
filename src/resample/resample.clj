(ns resample.resample
  (:require [pdfboxing.info :as info]
            [resample.merge :as merge]
            [clojure.tools.cli :refer [parse-opts]])
  (:import (org.apache.pdfbox.rendering PDFRenderer ImageType)
           (org.apache.pdfbox.pdmodel PDDocument)
           (java.io File)
           javax.imageio.ImageIO))

(defn page-count
  "Returns the page count of the PDF provided"
  [pdf-doc]
  (info/page-number pdf-doc))

(defn explode-pdf
  [filename ^Integer resolution ^ImageType color-scheme]
  (with-open [document (PDDocument/load (new File filename))]
    (let [pdf-renderer (new PDFRenderer document)]
      (doseq [i (range (.getNumberOfPages document))
              :let [bim (.renderImageWithDPI pdf-renderer
                                             i
                                             resolution
                                             color-scheme)]]
        (ImageIO/write bim "PNG" (new File (str filename "-" i ".png")))))))

(defn resample
  [filename-in filename-out dpi color-scheme]
  (let [intermediate-files (map (fn [i] (str filename-in "-" i ".png"))
                                (range (page-count filename-in)))]
    (explode-pdf filename-in dpi color-scheme)
    (merge/merge-images-from-path intermediate-files filename-out)
    (doseq [f intermediate-files]
      (clojure.java.io/delete-file f))))