if [ $# -ne 7 ]; then 
	echo "Need 7 parameters: docsize, trainsize, iteration, init vectorsize, increment, end vectorsize, loop"
	exit 0
fi
l=1
while [ $l -le $7 ]
do	
	echo "calculate precision, recall"
	T=$4
	while [ $T -le $5 ]
	do
		outname="precision_recall"_$1_$2_$3
		java -cp jars/psl_pltm.jar edu.umd.umiacs.clip.tn.analyze.analyzeAllData $1 $2 $l $T $3 $outname			
		T=$(( $T + $6 ))
	done

	(( l++ ))
done
