(ns leiningen.polylith.cmd.success-test
  (:require [clojure.test :refer :all]
            [leiningen.polylith.cmd.shared :as shared]
            [leiningen.polylith.cmd.test-helper :as helper]
            [leiningen.polylith.file :as file]))

(use-fixtures :each helper/test-setup-and-tear-down)

(defn fake-fn [& args]
  args)

(deftest polylith-success--local--update-time
  (with-redefs [file/current-path                (fn [] @helper/root-dir)
                leiningen.polylith.cmd.shared/sh fake-fn]
    (let [ws-dir  (str @helper/root-dir "/ws1")
          project (helper/settings ws-dir "my.company")
          _       (helper/execute-polylith nil "create" "w" "ws1" "my.company" "-git")
          _       (helper/execute-polylith project "create" "c" "comp1")
          _       (helper/execute-polylith project "create" "s" "system1" "base1")
          output  (with-out-str (helper/execute-polylith project "success"))]

      (is (= "set :last-success in .polylith/time.edn\n" output))
      (is (< 0 (-> (helper/content ws-dir ".polylith/time.edn")
                   first :last-success))))))

(deftest polylith-success--local-custom-bookmark--update-time
  (with-redefs [file/current-path                (fn [] @helper/root-dir)
                leiningen.polylith.cmd.shared/sh fake-fn]
    (let [ws-dir  (str @helper/root-dir "/ws1")
          project (helper/settings ws-dir "my.company")
          _       (helper/execute-polylith nil "create" "w" "ws1" "my.company" "-git")
          _       (helper/execute-polylith project "create" "c" "comp1")
          _       (helper/execute-polylith project "create" "s" "system1" "base1")
          output  (with-out-str (helper/execute-polylith project "success" "test"))]

      (is (= "set :test in .polylith/time.edn\n" output))
      (is (< 0 (-> (helper/content ws-dir ".polylith/time.edn")
                   first :test))))))

(deftest polylith-success--ci--update-git
  (try
    (with-redefs [file/current-path (fn [] @helper/root-dir)]
      (System/setProperty "CI" "CIRCLE")
      (let [ws-dir  (str @helper/root-dir "/ws1")
            project (helper/settings ws-dir "my.company")
            _       (helper/execute-polylith nil "create" "w" "ws1" "my.company" "-git")
            _       (helper/execute-polylith project "create" "c" "comp1")
            _       (helper/execute-polylith project "create" "s" "system1" "base1")
            _       (shared/sh "git" "init" :dir ws-dir)
            _       (shared/sh "git" "add" "." :dir ws-dir)
            _       (shared/sh "git" "commit" "-m" "Initial Commit" :dir ws-dir)
            output  (with-out-str (helper/execute-polylith project "success"))
            _       (System/clearProperty "CI")]

        (is (= "set :last-success in .polylith/git.edn\n" output))
        (is (not (nil? (-> (helper/content ws-dir ".polylith/git.edn")
                           first :last-success))))))
    (catch Exception _
      (System/clearProperty "CI"))))

(deftest polylith-success--ci-local-custom-bookmark--update-git
  (try
    (with-redefs [file/current-path (fn [] @helper/root-dir)]
      (System/setProperty "CI" "CIRCLE")
      (let [ws-dir  (str @helper/root-dir "/ws1")
            project (helper/settings ws-dir "my.company")
            _       (helper/execute-polylith nil "create" "w" "ws1" "my.company" "-git")
            _       (helper/execute-polylith project "create" "c" "comp1")
            _       (helper/execute-polylith project "create" "s" "system1" "base1")
            _       (shared/sh "git" "init" :dir ws-dir)
            _       (shared/sh "git" "add" "." :dir ws-dir)
            _       (shared/sh "git" "commit" "-m" "Initial Commit" :dir ws-dir)
            output  (with-out-str (helper/execute-polylith project "success" "test"))
            _       (System/clearProperty "CI")]

        (is (= "\nset :test in .polylith/git.edn\n" output))
        (is (not (nil? (-> (helper/content ws-dir ".polylith/git.edn")
                           first :test))))))
    (catch Exception _
      (System/clearProperty "CI"))))
