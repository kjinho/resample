(ns resample.core
  (:require [resample.resample :refer [resample]]
            clojure.tools.cli
            [clojure.java.io :as io])
  (:import org.apache.pdfbox.rendering.ImageType)
  (:gen-class))

(def jar-name "resample-0.1.1-standalone.jar")

(def color-schemes
  {"BW"    ImageType/BINARY
   "GRAY"  ImageType/GRAY
   "RGB"   ImageType/RGB
   "ALPHA" ImageType/ARGB})

(def cli-options
  ;; An option with a required argument
  [["-i" "--input INPUT_FILE" "input file name"
    :validate [#(.exists (io/file %)) "input file must exist"]]
   ["-o" "--output OUTPUT_FILE" "output file name"
    :validate [#(not (.exists (io/file %))) "output file must not exist"]]
   ["-d" "--dpi NUMBER" "DPI"
    :default 200
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
   ["-v" nil "verbosity level (display progress)"
    :id :verbosity
    :default 0
    :update-fn inc]
   ["-c" "--color TYPE" "the color type [BW|GRAY|RGB|ALPHA]"
    :default "BW"
    :validate [#(not (nil? (get color-schemes %)))
               "Must be one of BW, GRAY, RGB, or ALPHA"]]
   ["-h" "--help"]])

(defn print-help
  [opts]
  (println
    (str
      "resample: create a new PDF by resampling the input PDF\n"
      "USAGE: java -jar " jar-name " --input [INPUT_FILE] --output [OUTPUT_FILE] [options]\n"
      (-> opts :summary))))

(defn -main [& args]
  (let [opts (clojure.tools.cli/parse-opts args cli-options)]
    (cond
      (-> opts :options :help) (print-help opts)
      (-> opts :errors) (do
                          (println "Command-line error:")
                          (dorun (map println (-> opts :errors))))
      (and (-> opts :options :input)
           (-> opts :options :output))
      (resample (-> opts :options :input)
                (-> opts :options :output)
                (-> opts :options :dpi)
                (-> opts :options :color color-schemes)
                {:level (-> opts :options :verbosity)})
      :else (print-help opts))))