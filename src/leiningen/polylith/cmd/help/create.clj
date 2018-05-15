(ns leiningen.polylith.cmd.help.create)

(defn help []
  (println "  Create a component:")
  (println)
  (println "  lein polylith create c[omponent] NAME [INTERFACE]")
  (println "    NAME = Component name")
  (println "    INTERFACE = Interface name. Same as component name if omitted.")
  (println "  --------------------------------------------------------")
  (println "  Create a system:")
  (println)
  (println "  lein polylith create s[ystem] NAME [BASE]")
  (println "    NAME = System name")
  (println "    BASE = Base name. Same as system name if omitted.")
  (println "  --------------------------------------------------------")
  (println "  Create a workspace:")
  (println)
  (println "  lein polylith create w[orkspace] WS NS")
  (println "    WS = Workspace name")
  (println "    NS = Namespace name or '-' to omit it.")
  (println "         It's recommended and good practice to give a namespace.")
  (println "  example:")
  (println "    lein polylith create c mycomponent")
  (println "    lein polylith create c mycomponent myinterface")
  (println "    lein polylith create component mycomponent")
  (println "    lein polylith create component mycomponent myinterface")
  (println "    lein polylith create s mysystem")
  (println "    lein polylith create s mysystem mybase")
  (println "    lein polylith create system mysystem")
  (println "    lein polylith create system mysystem mybase")
  (println "    lein polylith create w myworkspace -")
  (println "    lein polylith create w myworkspace com.my.company")
  (println "    lein polylith create workspace myworkspace com.my.company"))
