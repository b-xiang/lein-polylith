(ns leiningen.polylith.cmd.help.sync-deps)

(defn help []
  (println "  These steps are performed:")
  (println)
  (println "  Firstly, it adds missing libraries to the development environment.")
  (println "  The way it does that is to first check which components and bases")
  (println "  are part of the development environment. Then it goes through")
  (println "  those components and bases and collects a list of all their dependencies")
  (println "  from each project.clj file. That list is compared with the dependencies")
  (println "  in environments/development/project.clj and missing libraries are updated.")
  (println)
  (println "  Secondly it makes sure that the library versions for all components")
  (println "  and bases are in sync with the library versions in")
  (println "  environment/development/project.clj.")
  (println)
  (println "  Finally it makes sure that each system has a library list that reflects")
  (println "  the sum of all libraries of its components and bases.")
  (println)
  (println "  examples:")
  (println "    lein polylith sync-deps"))
