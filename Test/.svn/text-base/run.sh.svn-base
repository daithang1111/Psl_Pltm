if [ $# -ne 12 ]; then 
	echo "Need 12 arguments: start datasize, increment, end datasize, start vectorsize, increment, end vectorsize, iteration, pltm iteration, threshold, blockingsize, what blocking, loop "
	exit 0
fi
N=$1
K1=2
while [ $N -le $2 ]
do
	K=$(($N/$K1))
	l=1
	while [ $l -le ${12} ]
	do	
		echo "generate data for $N, $K, $l"
		sh sample_data.sh $N $K $l	
		olddir="TEST"_$(($N))_$(($K))_$(($l))
		if [ ${11} -eq 1 ]; then
			cd $olddir
			java -cp ../jars/psl_pltm.jar edu.umd.umiacs.clip.tn.baseline.BaselineSixWords fully_aligned_en fully_aligned_de baseline.out
			java -cp ../jars/psl_pltm.jar edu.umd.umiacs.clip.tn.blocking.BaselineBlocking baseline.out $N ${10} inSameBlock
			cd ..
		fi
		T=$4
		while [ $T -le $5 ]
		do
			echo "create dir to store infor $N, $K, $l, $T"
			newdir="TEST"_$(($N))_$(($K))_$(($l))_$(($T))
			mkdir $newdir
		
			echo "copy all files from old dir to new dir"
			cp $olddir/* $newdir
			cd $newdir
			mkdir stoplists
			cp ../stoplists/* stoplists
			cp ../jars/runPSL.jar .

			c=1
			while [ $c -le $7 ]
			do
				echo "RUN --- $c"
        			mkdir tmp_$c
				echo "train PLTM on aligned articles with $T topics and $8 iterations"
				java -cp ../jars/psl_pltm.jar edu.umd.umiacs.clip.tn.pltm.PltmTrain aligned_en aligned_de $T $8 aligned_en aligned_de
				echo "generate PSL data with"
				java -cp ../jars/psl_pltm.jar edu.umd.umiacs.clip.tn.data.generatePSLData fully_aligned_en fully_aligned_de ../data/doc_doc_en ../data/doc_doc_de ../data/doc_user_en ../data/doc_user_de ../data/doc_bot_en ../data/doc_bot_de ../data/doc_link_en ../data/doc_link_de aligned_en.dvs aligned_de.dvs
				if [ ${11} -eq 0 ]; then
					echo "generate vector blocking..."
					java -cp ../jars/psl_pltm.jar edu.umd.umiacs.clip.tn.blocking.KDTreeBlocking $N $T ${10} hasTopicVector vectorNames inSameBlock
				fi
				echo "run PSL on PSL data to generate isComparable.txt file"
				java -jar -Xmx1200m runPSL.jar >isComparable.txt
				echo "convert isComparable.txt content to aligned_pair of document titles"
				java -cp ../jars/psl_pltm.jar edu.umd.umiacs.clip.tn.preprocess.CleanPSLOutput isComparable.txt name $9 aligned_pair
				echo "generate new training data from aligned_pair"
				java -cp ../jars/psl_pltm.jar edu.umd.umiacs.clip.tn.data.generateTrainDataFromFile aligned_en aligned_de aligned_pair new_aligned_en new_aligned_de
				mv aligned_en aligned_de aligned_en.dvs aligned_de.dvs isComparable.txt name hasLanguage hasEditor hasBot hasLink hasTopicVector hasWikiLink aligned_pair tmp_$c
				if [ ${11} -eq 0 ]; then
					mv inSameBlock tmp_$c
				fi
				mv new_aligned_en aligned_en
				mv new_aligned_de aligned_de
	
				(( c++))
			done
			cd ..			


			T=$(( $T + $6 ))
		done
	
		(( l++ ))
	done

	N=$(( $N + $3))
done
