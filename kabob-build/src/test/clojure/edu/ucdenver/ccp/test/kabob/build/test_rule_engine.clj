(ns edu.ucdenver.ccp.test.kabob.build.test-rule-engine
  (use clojure.test
       clojure.pprint

       edu.ucdenver.ccp.utils
       edu.ucdenver.ccp.kr.rule
       ;;edu.ucdenver.ccp.kr.forward-rule
       edu.ucdenver.ccp.kabob.rule

       edu.ucdenver.ccp.kabob.namespace)
  (require kabob
           clojure.string))

;;; --------------------------------------------------------
;;; constansts
;;; --------------------------------------------------------

;;(defonce ^:dynamic *rcon* nil)

(def rule-test-path "edu/ucdenver/ccp/kabob/build/rules/bio_to_ice/")

;;; --------------------------------------------------------
;;; functions
;;; --------------------------------------------------------

;;; --------------------------------------------------------
;;; manual / live tests
;;; --------------------------------------------------------

;;; --------------------------------------------------------
;;; automated tests
;;; --------------------------------------------------------

;;this is to correct for some combination of dependencies in the poms
;;  generating a state of being that prevents the classpath from being
;;  seen by the clojure code.
(deftest verify-classpath-readable
  ;;(pprint (classpath-seq))
  (is (< 5 (count (classpath-seq)))))

(deftest verify-specific-rule-file-readable
  (is (< 1 
         (count 
          (edu.ucdenver.ccp.kabob.rule/kabob-load-rules-from-classpath 
           rule-test-path)))))



;;; --------------------------------------------------------
;;; END
;;; --------------------------------------------------------
