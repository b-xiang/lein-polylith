(ns leiningen.polylith.cmd.help.changes)

(defn help []
  (println "  Show what has been changed since a specific point in time.")
  (println)
  (println "  lein polylith changes ENTITY [ARG]")
  (println "    ENTITY = i[nterface] -> Show changed interfaces")
  (println "             c[omponent] -> Show changed components")
  (println "             b[ase]      -> Show changed bases")
  (println "             s[ystem]    -> Show changed systems")
  (println)
  (println "    ARG = (omitted) -> Since last successful build, stored in bookmark")
  (println "                       :last-successful-build in WS-ROOT/.polylith/time.edn. or")
  (println "                       :last-successful-build in WS-ROOT/.polylith/git.edn if")
  (println "                       you have CI variable set to something on the machine.")
  (println "          timestamp -> Since the given timestamp (milliseconds since 1970).")
  (println "          git-hash  -> Since the given Git hash if the CI variable is set.")
  (println "          bookmark  -> Since the timestamp for the given bookmark in")
  (println "                       WS-ROOT/.polylith/time.edn or since the Git hash")
  (println "                       for the given bookmark in WS-ROOT/.polylith/git.edn")
  (println "                       if CI variable set.")
  (println)
  (println "  example:")
  (println "    lein polylith changes i")
  (println "    lein polylith changes c")
  (println "    lein polylith changes component")
  (println "    lein polylith changes b")
  (println "    lein polylith changes s 1523649477000")
  (println "    lein polylith changes s 7d7fd132412aad0f8d3019edfccd1e9d92a5a8ae")
  (println "    lein polylith changes s mybookmark"))
