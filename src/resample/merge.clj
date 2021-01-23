;; adapted from pdfboxing
(ns resample.merge
  (:require [resample.logging])
  (:import (org.apache.pdfbox.pdmodel PDDocument
                                      PDPage
                                      PDPageContentStream)
           (org.apache.pdfbox.pdmodel.common PDRectangle)
           (org.apache.pdfbox.pdmodel.graphics.image PDImageXObject)))

(defn- add-image-to-page
  "Adds image as a page to the document object"
  [doc ^PDImageXObject image logging]
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
      (.drawImage stream image x y scaled-width scaled-height)
      (resample.logging/log
        (-> (assoc logging
              :message (str "addition of " (.toString ^PDImageXObject image) " complete"))
            (update :current inc))))))

(defn merge-images-from-path
  "Merges images provided as a vector of string paths"
  [images output-doc logging]
  (with-open [doc (PDDocument.)]
    (run! #(add-image-to-page
             doc
             (PDImageXObject/createFromFile (second %) doc)
             (assoc logging
               :current (first %)))
          (map vector (range (:current logging) (:total logging)) images))
    (.save doc ^String output-doc)))