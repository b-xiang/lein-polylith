(ns leiningen.polylith.cmd.gen-doc
  (:require [leiningen.polylith.cmd.deps :as cdeps]
            [leiningen.polylith.cmd.shared :as shared]
            [clojure.set :as set]
            [selmer.parser :as selmer]
            [leiningen.polylith.file :as file]))

(defn dependencies [ws-path top-dir system-or-environment]
  (let [used-entities (shared/used-entities ws-path top-dir system-or-environment)
        used-components (set/intersection used-entities (shared/all-components ws-path))
        used-bases (set/intersection used-entities (shared/all-bases ws-path))]
    (cdeps/component-dependencies ws-path top-dir used-components used-bases)))

(defn entity-type [entity all-bases]
  (if (contains? all-bases entity)
    "base"
    "component"))

(defn dependency-tree [entity deps all-bases]
  {:entity entity
   :type (entity-type entity all-bases)
   :children (mapv #(dependency-tree % deps all-bases) (deps entity))})

(defn count-cols [{:keys [_ _ children]}]
  (cond
    (empty? children) 1
    :else (apply + (map count-cols children))))

(defn count-columns [tree]
  (let [sections (count-cols tree)]
    (if (zero? sections)
      0
      (dec (* 2 sections)))))

(defn calc-result [{:keys [entity type children] :as tree} y maxy result]
  (assoc! result y (conj (get result y) {:entity entity
                                         :type type
                                         :columns (count-columns tree)}))
  (if (empty? children)
    (doseq [yy (range (inc y) (inc maxy))]
      (assoc! result yy (conj (get result yy) {:type "empty"})))
    (doseq [child children]
      (calc-result child (inc y) maxy result))))

(defn max-deps [{:keys [_ _ children]} depth]
  (if (empty? children)
    depth
    (apply max (map #(max-deps % (inc depth)) children))))

(defn calc-table [tree]
  (let [maxy (max-deps tree 0)
        result (transient (vec (repeat maxy [])))
        _ (calc-result tree 0 maxy result)
        table (reverse (persistent! result))]
    (map #(interpose {:type "spc"} %) table)))

(defn calculate-table [ws-path top-dir entity]
  (let [deps (dependencies ws-path top-dir entity)
        all-bases (shared/all-bases ws-path)
        base (first (set/intersection (set (keys deps)) all-bases))
        tree (dependency-tree base deps all-bases)]
    (calc-table tree)))

(defn execute [ws-path top-dir output-dir template-dir [template-file]]
  (let [template-filename (or template-file "workspace.html")
        _ (selmer/set-resource-path! template-dir)
        table {:table (calculate-table ws-path top-dir "development")
               :name "Kalle"}
        content (selmer/render-file template-filename table)
        path (str output-dir "/development.html")]
    (file/create-file-with-content path content)))
