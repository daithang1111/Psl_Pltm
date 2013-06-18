*************RUN****************************
go to Test directory

#to crawl new data
+ rm data/*
+ crawl_wiki.sh Machine_learning 2 (2 - level is snowball depth)
#to test the project
#need to find the CORPUS_SIZE (english_content_sorted file lines), and put it
# in Psl_PltmPreferences.properties
+ sh run.sh 20 20 20 20 20 20 1 50 0.5 4 0 1   //this generates all dirs and outputs
#get precision/recall
+ sh analyze.sh 20 10 1 20 20 20 1 // 

***************properties********************
DATA_DIR : dir to store all crawled data/preprocessed data
MAX_PER_PAGE: max articles to crawl from 1 article
DOC_LENGTH_THRESHOLD: only article with html text length bigger than this value is crawled
...

*********************************************
#if error happens (mostly due to eclipse prebuilt classes incompatibility), 
#recreate the psl_pltm.jar file from all classes in Psl_Pltm project as follow:
+ cd Psl_Pltm
+ sh compile.sh : this will compile the whole project and create a psl_pltm.jar in jars directory
#runPSL.jar is a little bit tricky to generate, use put psl/MyTest.groovy in
psl-example project downloaded from https://github.com/linqs/psl/wiki/Installing-examples (need to follow instructions on this page to run MyTest.groovy) 
