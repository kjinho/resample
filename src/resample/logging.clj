(ns resample.logging)

(defn percentage-string
  "returns an integer that encapsulates the whole percentage"
  [num den]
  (quot (* 100 num) den))

(defn stringify
  [int]
  (format "%3d%%" int))

(defn log
  "logs the current progress"
  [progress]
  (case (:level progress)
    1 (case (:total progress)
        nil (println (:message progress))
        0 (println (:message progress))
        (-> (format "[%s] %s"
                    (stringify (percentage-string (:current progress)
                                                  (:total progress)))
                    (:message progress))
            (println)))
    nil))