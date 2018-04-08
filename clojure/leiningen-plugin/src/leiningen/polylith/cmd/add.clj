(ns leiningen.polylith.cmd.add
  (:require [leiningen.polylith.utils :as utils]
            [leiningen.polylith.cmd.info :as info]
            [leiningen.polylith.file :as file]
            [leiningen.polylith.cmd.create.shared :as shared]
            [clojure.string :as str]))

(defn validate [ws-path component system]
  (let [components (info/all-components ws-path)
        systems (info/all-systems ws-path)]
    (cond
      (utils/is-empty-str? component) [false "Missing component name"]
      (utils/is-empty-str? system) [false "Missing system name"]
      (not (contains? components component)) [false (str "Component '" component "' not found")]
      (not (contains? systems system)) [false (str "System '" system "' not found")]
      :else [true])))

(defn add-component-to-system [ws-path top-dir component system]
  (let [component-dir (shared/full-name top-dir "/" component)
        system-dir (shared/full-name top-dir "/" system)
        ;; todo: refactor these two lines (and all other occurances)
        levels (+ 2 (count (str/split system-dir #"/")))
        component-relative-path (str (str/join (repeat levels "../")) "components/" component "/src/" component-dir)
        systems-component-src (str ws-path "/systems/" system "/src/" component-dir)]

    (file/create-symlink-if-not-exists systems-component-src component-relative-path)))

(defn execute [ws-path top-dir [component system]]
  (let [[ok? message] (validate ws-path component system)]
    (if ok?
      (add-component-to-system ws-path top-dir component system)
      (println message))))
