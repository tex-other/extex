; author: Sebastian Waschik
; created: 2004-07-26
; RCS-ID: $Id$
(jde-project-file-version "1.0")
(jde-set-variables
 '(jde-project-name "ExTeX")

; allow paths to be relative to project file
 '(jde-resolve-relative-paths-p t)
 '(jde-sourcepath 
   (quote 
    ("./src/java"
     "./src/test")))
 '(jde-global-classpath
   (quote 
    ("./target/classes"
    "./lib"
    "./lib.develop")))

 ;; ant
 '(jde-ant-use-global-classpath t)
 '(jde-ant-working-directory "./")

 ;; checkstyle
 '(jde-checkstyle-classpath
   (quote
    ("./lib.develop/checkstyle-all-3.4.jar")))
 '(jde-checkstyle-style "./.checkstyle.cfg")

 ;; compile
 '(jde-build-function (quote (jde-ant-build)))
 '(jde-compile-option-deprecation t)
 '(jde-compile-option-directory "./target/classes")
 '(jde-compile-option-source (quote ("1.4")))
 '(jde-compile-option-target (quote ("1.4")))
 '(jde-built-class-path
   (quote
    ("./target/classes")))

 ;; debug
 '(jde-db-read-app-args t)

 ;; run
 '(jde-run-application-class "de.dante.extex.ExTeX")
 '(jde-run-working-directory ".")
 '(jde-run-read-app-args t)

 ;; style
 '(jde-import-auto-sort t)
 '(jde-gen-k&r t)

 ;; xrefdb
 '(jde-xref-store-prefixes (quote ("de.dante")))
)
