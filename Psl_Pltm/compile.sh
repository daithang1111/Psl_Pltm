mkdir bin
javac -d bin -sourcepath src -cp .:lib/bsh.jar:lib/javaml-0.1.7.jar:lib/jgrapht-0.6.0.jar:lib/jwnl-1.3.jar:lib/mallet-deps.jar:lib/mtj-0.9.9.jar:lib/grmm-deps.jar:lib/jdom-1.0.jar:lib/junit-4.5.jar:lib/kd.jar:lib/mallet.jar:lib/openjgraph.jar:lib/trove-2.0.2.jar src/edu/umd/umiacs/clip/tn/preprocess/*.java src/edu/umd/umiacs/clip/tn/pltm/*.java src/edu/umd/umiacs/clip/tn/main/*.java src/edu/umd/umiacs/clip/tn/data/*.java src/edu/umd/umiacs/clip/tn/crawler/*.java src/edu/umd/umiacs/clip/tn/blocking/*.java src/edu/umd/umiacs/clip/tn/baseline/*.java src/edu/umd/umiacs/clip/tn/analyze/*.java
mkdir jars
cd bin
jar -cvf psl_pltm.jar edu
mv psl_pltm.jar ../jars/

