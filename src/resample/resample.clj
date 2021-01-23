(ns resample.resample
  (:require [resample.merge :as merge]
            [clojure.tools.cli :refer [parse-opts]]
            resample.logging)
  (:import (org.apache.pdfbox.rendering PDFRenderer ImageType)
           (org.apache.pdfbox.pdmodel PDDocument)
           (java.io File)
           javax.imageio.ImageIO
           (java.nio.file Files Paths)
           (java.nio.file.attribute FileAttribute)))

(defn create-temp-files
  "Returns a collection of Files to temporary files that will be deleted upon close"
  [n extension]
  (let [temp-files
        (->> (for [_i (range n)]
               (->
                 (Files/createTempFile
                   (Paths/get "." (into-array String [""]))
                   "resample-"
                   extension
                   (into-array FileAttribute []))
                 (.toUri)
                 (File.)))
             (into []))]
    (dorun (map #(.deleteOnExit %) temp-files))
    temp-files))

(defn explode-pdf
  "Returns a collection of the exploded image files"
  [filename ^Integer resolution ^ImageType color-scheme logging]
  (with-open [document (PDDocument/load (new File filename))]
    (let [pdf-renderer (new PDFRenderer document)
          len (.getNumberOfPages document)
          temp-files (create-temp-files len ".png")]
      (doseq [i (range len)
              :let [bim (.renderImageWithDPI pdf-renderer
                                             i
                                             resolution
                                             color-scheme)]]
        (ImageIO/write bim "PNG" ^File (nth temp-files i))
        (resample.logging/log
          (assoc logging
            :message (str "resampling of `" (.getName ^File (nth temp-files i)) "` complete")
            :current (inc i))))
      temp-files)))

(defn resample
  [filename-in filename-out dpi color-scheme logging]
  (let [len (with-open [document (PDDocument/load (new File filename-in))]
              (.getNumberOfPages document))
        new-log (assoc logging :total (* 2 len))]
    (as-> (explode-pdf filename-in dpi color-scheme new-log) x
          (mapv #(.getPath %) x)
          (merge/merge-images-from-path x filename-out (assoc new-log
                                                         :current len))))
  (resample.logging/log {:level    (:level logging)
                          :message "Resampling and compilation complete; cleaning up now"}))