;; This script will execute the de_ice rules on the specified KB. 
;; Generated triples are output to files in the specified output directory.
;; This script is designed to be run by the clojure:run goal of the maven clojure plugin.

(ns edu.ucdenver.ccp.kabob.build.run_rules)
(use 'edu.ucdenver.ccp.kr.kb
  'edu.ucdenver.ccp.kr.rdf
  'edu.ucdenver.ccp.kr.sparql
  'edu.ucdenver.ccp.kr.sesame.kb
  'edu.ucdenver.ccp.kabob.namespace
  'edu.ucdenver.ccp.kabob.rule
  'edu.ucdenver.ccp.kr.rule
  'edu.ucdenver.ccp.kr.forward-rule)
(require 'edu.ucdenver.ccp.kr.sesame.writer-kb
         'edu.ucdenver.ccp.kabob.build.kb-connection
         'kabob)

(import '(java.io InputStreamReader)
        '(edu.ucdenver.ccp.common.file CharacterEncoding)
        '(java.io File))

;; The file path to the BigData journal file containing the KB
(def kb-journal-file (nth *command-line-args* 0))
;; The file path to the BigData properties file for the KB
(def kb-properties-file (nth *command-line-args* 1))
;; The name of the store, e.g. kaboblt
(def kb-name (nth *command-line-args* 2)) 
;; The output directory where generated triple files will be placed. 
;; IMPORTANT: The output directory path must end with a forward slash.
(def output-directory (nth *command-line-args* 3)) 
;; Names of the rule sets to use (must point to files available on the 
;; classpath; NOTE that it should not start with a forward slash)
;; e.g. edu/ucdenver/ccp/kabob/build/id_typing/entrez_gene_id_typing_rules.clj
(def rule-set-names (drop 4 *command-line-args*)) 

(prn "Serializing rule output for: " rule-set-names)
(prn (str "Clojure version: " *clojure-version*))
(prn (str "BigData journal file: " kb-journal-file))
(prn (str "BigData properties file: " kb-properties-file))
(prn (str "KB name: " kb-name))
(prn (str "Output directory: " output-directory))

(def jFile (File. kb-journal-file))
(if-not (.exists jFile) (throw (RuntimeException. (str "FILE DOES NOT EXIST!!!!!!!!" kb-journal-file))))

;; Extract rules from files on the classpath
;; The rule-set-names variable holds the names of rule files available 
;; in src/main/resources. Below we map over each rule set name, openning an 
;; InputStreamReader to read the rule set file, and pass the open reader to 
;; edu.ucdenver.ccp.kr.rule/load-rules-from-file
(def rules (doall
             (mapcat 
               (fn [rule-set-name] 
                 (edu.ucdenver.ccp.kabob.rule/kabob-load-rules-from-classpath rule-set-name)) 
               rule-set-names)))


(prn rules)

;; Init source KB connection
(def source-kb (edu.ucdenver.ccp.kabob.build.kb-connection/builder-kb {
                                                                       :properties kb-properties-file
                                                                       :journal kb-journal-file
                                                                       :store-name kb-name}))
;;(register-namespaces source-kb)
;;(set!-kb source-kb)
(set!-kb (register-namespaces 
           source-kb
           edu.ucdenver.ccp.kabob.namespace/*namespaces*))

;; Map over each rule, sending the generated triples to a mock KB which is 
;; actaully an output writer
(binding [edu.ucdenver.ccp.kr.sparql/*force-prefixes* [["franzOption_queryEngine" "franz:sparql-1.1"]]
          ;edu.ucdenver.ccp.kr.sparql/*select-limit* 10
          ]
  (dorun
    ;;switching this map to pmap should do these in somewhat parallel
    ;;  however it may overload the kb?
    (map (fn [rule]
           (def start-time (System/currentTimeMillis))
           (prn (str "Processing rule: " (:name rule)))
           ;;the second argument to connection forces a new connection
           (let [source-connection (connection source-kb false)
                 target-connection
                 (connection (register-namespaces
                               (edu.ucdenver.ccp.kr.sesame.writer-kb/new-sesame-writer-kb
                                 (str output-directory (str (:name rule) ".nt")))) true)]
             (run-forward-rule source-connection
                               target-connection
                               rule)
             (close target-connection)
             ;;(close source-connection)
             )
           (prn 
             (str "Rule processing time for " (:name rule) " = " 
                  (/ (- (System/currentTimeMillis) start-time) 1000.0) "s")))
         rules)))

;; Close KB?