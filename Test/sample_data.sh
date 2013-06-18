echo "create TEST_$1_$2_$3"
mkdir TEST_$1_$2_$3
cp Psl_PltmPreferences.properties TEST_$1_$2_$3
cd TEST_$1_$2_$3
echo "extract first $1 articles from english and german as the fully sample"

java -cp ../jars/psl_pltm.jar edu.umd.umiacs.clip.tn.data.generateSampleData ../data/english_content_sorted ../data/german_content_sorted $1 fully_aligned_en fully_aligned_de
echo "generate training article pairs with article range from 0 to $2"
java -cp ../jars/psl_pltm.jar edu.umd.umiacs.clip.tn.data.generateTrainData  fully_aligned_en fully_aligned_de 0 $2 aligned_en aligned_de 0 false

			

echo "DONE!!!!!!!!!!!!!!!!!!!"
	


