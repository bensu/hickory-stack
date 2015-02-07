(ns hickory-stack.core
  (:require [clojure.string :as s]
            [clojure.walk :as w]
            [reagent.core :as reagent :refer [atom]]
            [hickory.core :as h]))

(enable-console-print!)

(def good-style "color:red;background:black; font-style: normal    ;font-size : 20px")

(defn string->tokens
  "Takes a string with syles and parses it into properties and value tokens"
  [style]
  {:pre [(string? style)]
   :post [(even? (count %))]}
  (->> (s/split style #";")
       (mapcat #(s/split % #":"))
       (map s/trim)))
(defn tokens->map
  "Takes a seq of tokens with the properties (even) and their values (odd)"
  [tokens]
  {:pre [(even? (count tokens))]
   :post [(map? %)]}
  (zipmap (keep-indexed #(if (even? %1) %2) tokens)
          (keep-indexed #(if (odd? %1) %2) tokens)))

(defn style->map
  "Takes an inline style attribute stirng and converts it to a React Style map"
  [style]
  (tokens->map (string->tokens style)))

(defn hiccup->sablono [coll]
  (w/postwalk
   (fn [x]
     (if (map? x)
       (do (println x) (update-in x [:style] style->map))
       x))
   coll))

(def html-fragment
    (str "<div style='" good-style "'>test</div>"))

(defn some-view []
  [:div (hiccup->sablono
         (first (map h/as-hiccup (h/parse-fragment html-fragment))))])

(reagent/render-component [some-view]
                          (. js/document (getElementById "app")))

(comment  ;; Testing
  
  (println "Test for style-map")
  (println (style->map good-style))

  (println "Test for parse-string")
  (println (string->tokens good-style))

  (println "Test for walk")
  (println (hiccup->sablono
            (first (map h/as-hiccup (h/parse-fragment html-fragment))))))





