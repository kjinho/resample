(defproject resample "0.1.0-SNAPSHOT"
  :description "tool to resample PDF"
  :url "http://github.com/kjinho/resample"
  :license {:name "Apache License, Version 2.0"
            :url  "https://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [pdfboxing/pdfboxing "0.1.15.1-SNAPSHOT"]
                 [org.apache.pdfbox/pdfbox "2.0.21"]
                 [org.apache.pdfbox/pdfbox-tools "2.0.21"]
                 [com.github.jai-imageio/jai-imageio-core "1.4.0"]
                 [com.github.jai-imageio/jai-imageio-jpeg2000 "1.4.0"]
                 [org.apache.pdfbox/jbig2-imageio "3.0.3"]
                 [org.clojure/tools.cli "1.0.194"]]
  :main ^:skip-aot resample.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot      :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
