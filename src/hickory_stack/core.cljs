(ns hickory-stack.core
  (:require [clojure.browser.repl :as repl]
            [hickory.core :as h])
  )

(enable-console-print!)

(println (map h/as-hiccup (h/parse-fragment "<div>test</div>"))) 
(println (map h/as-hiccup (h/parse-fragment "<div style='color:red'>test</div>")))
