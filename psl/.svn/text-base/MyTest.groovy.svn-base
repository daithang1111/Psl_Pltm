package edu.umd.cs.example;

import edu.umd.cs.psl.config.*
import edu.umd.cs.psl.database.RDBMS.DatabaseDriver
import edu.umd.cs.psl.groovy.*
import edu.umd.cs.psl.model.function.AttributeSimilarityFunction
import edu.umd.cs.psl.ui.functions.textsimilarity.*

//
PSLModel m = new PSLModel(this);

//
m.add predicate: "name" , obj: Entity, string : Text
m.add predicate: "hasTopicVector", snr: Entity, snt : Entity
//m.add predicate: "hasCategory" , snr: Entity, snt : Entity
m.add predicate: "hasEditor" , snr: Entity, snt : Entity
m.add predicate: "hasBot" , snr: Entity, snt: Entity
m.add predicate: "hasLink" , snr: Entity, snt: Entity
m.add predicate: "hasLanguage" , snr: Entity, snt : Entity
m.add predicate: "hasWikiLink" , snr: Entity, snt : Entity
m.add predicate: "inSameBlock", snr: Entity, snt: Entity
//
m.add predicate: "isComparable", snr: Entity, metar: Entity, open: true
//m.add predicate: "isSame", snr: Entity, metar: Entity, open: true

//
m.add rule : (inSameBlock(A,B)& hasLink(A,X) & hasLink (B,X)  &hasLanguage(A,X1) & hasLanguage(B,X2) &(X1^X2)& (A ^ B)  ) >> isComparable(A,B),  weight : 20.0
//m.add rule : ( hasCategory(A,X) & hasCategory(B,X)  &hasLanguage(A,X1) & hasLanguage(B,X2) &(X1^X2)& (A ^ B)  ) >> isComparable(A,B),  weight : 1.0
m.add rule : (inSameBlock(A,B)& hasBot(A,X) & hasBot(B,X) &hasLanguage(A,X1) & hasLanguage(B,X2) &(X1^X2)&(A^B) ) >> isComparable(A,B),  weight : 20.0
m.add rule : (inSameBlock(A,B)& hasEditor(A,X) & hasEditor(B,X) &hasLanguage(A,X1) & hasLanguage(B,X2) &(X1^X2)&(A^B) ) >> isComparable(A,B),  weight : 20.0

m.add function: "similarVector" , name1: Text, name2: Text, implementation: new TopicVectorSimilarity_Blocking()

m.add rule : (inSameBlock(A,B) &(A^B)) >>inSameBlock(B,A), weight: 1000
m.add rule : (inSameBlock(A,B)&(A^B)) >>isComparable(A,B),constraint:true, weight: 1000
m.add rule : (inSameBlock(A,B)&hasTopicVector(A,X) & hasTopicVector(B,Y) & name(X,XX) &name(Y,YY) & similarVector(XX,YY) &hasLanguage(A,X1) & hasLanguage(B,X2) &(X1^X2)& (A^B) ) >> isComparable(A,B), weight : 50.0
m.add rule : (hasWikiLink(A,B)&(A^B)) >>hasWikiLink(B,A), weight : 1000
m.add rule : (inSameBlock(A,B)& isComparable(C,D)&hasWikiLink(A,C)&hasWikiLink(B,D) &(A^B) &(C^D) &hasLanguage(A,X1) & hasLanguage(B,X2) &(X1^X2) &(A^B)  ) >> isComparable(A,B),  weight : 20.0

m.add PredicateConstraint.PartialFunctional , on : isComparable
m.add PredicateConstraint.PartialInverseFunctional , on : isComparable
m.add Prior.Simple, on : isComparable, weight: 1E-6

//
println m;

//
DataStore data = new RelationalDataStore(m)
data.setup db : DatabaseDriver.H2

//
def insert = data.getInserter(name)
def dir ="";//"data/wikidata/";// 'data'+java.io.File.separator+'wikidata'+java.io.File.separator;
insert.loadFromFile(dir+"name");

insert = data.getInserter(hasLanguage)
insert.loadFromFile(dir+"hasLanguage");

insert = data.getInserter(inSameBlock)
insert.loadFromFile(dir+"inSameBlock");

//insert = data.getInserter(hasCategory)
//insert.loadFromFile(dir+"hasCategory");
insert = data.getInserter(hasWikiLink)
insert.loadFromFile(dir+"hasWikiLink");

insert = data.getInserter(hasEditor)
insert.loadFromFile(dir+"hasEditor");

insert = data.getInserter(hasBot)
insert.loadFromFile(dir+"hasBot");

insert = data.getInserter(hasLink)
insert.loadFromFile(dir+"hasLink");

insert = data.getInserter(hasTopicVector)
insert.loadFromFile(dir+"hasTopicVector");


/* After having loaded the data, we are ready to run some inference and see what kind of
 * alignment our model produces. Note that for now, we are using the predefined weights.
 */
def result = m.mapInference(data.getDatabase())

// This prints out the results for our inference predicate
//System.out.println("Without learning, below is prob\n");
result.printAtoms(isComparable)
/*
 System.out.println("Start learning ...\n");
 insert = data.getInserter(isComparable,2)
 insert.loadFromFileWithTruth(dir+"isComparable.train","\t");
 ConfigManager cm = ConfigManager.getManager();
 ConfigBundle exampleBundle = cm.getBundle("example");
 WeightLearningConfiguration config = new WeightLearningConfiguration();
 config.setLearningType(WeightLearningConfiguration.Type.LBFGSB);
 config.setInitialParameter(1.0);
 //m.learn data, evidence : 1, infered: 2, close : isSame, configuration: config, config: exampleBundle
 m.learn data, evidence : 1, infered: 2, close : isComparable, configuration: config, config: exampleBundle
 //Let's have a look at the newly learned weights.
 println m
 insert = data.getInserter(name,3)
 insert.loadFromFile(dir+"name");
 insert = data.getInserter(hasLanguage,3)
 insert.loadFromFile(dir+"hasLanguage");
 insert = data.getInserter(hasCategory,3)
 insert.loadFromFile(dir+"hasCategory");
 insert = data.getInserter(hasEditor,3)
 insert.loadFromFile(dir+"hasEditor");
 insert = data.getInserter(hasBot,3)
 insert.loadFromFile(dir+"hasBot");
 insert = data.getInserter(hasLink,3)
 insert.loadFromFile(dir+"hasLink");
 insert = data.getInserter(hasTopicVector,3)
 insert.loadFromFile(dir+"hasTopicVector");
 result = m.mapInference(data.getDatabase(parts: 3));
 //print the inferred output here
 System.out.println("|OUTPUT|");
 result.printAtoms(isComparable);
 */

class TopicVectorSimilarity_Blocking implements AttributeSimilarityFunction {

	@Override
	public double similarity(String a, String b) {

		if(!a.contains("|")||!b.contains("|")){
			return 0.0;
		}
		
		//sparse vector
		String[] vector1 =a.split("\\|");
		String[] vector2 =b.split("\\|");
		/*
		int count=0;
		for(int i=0;i<vector1.length;i++){
			int index1 =Integer.parseInt(vector1[i].split(":")[0]);
			for(int j=0;j<vector2.length;j++){
				int index2 =Integer.parseInt(vector2[j].split(":")[0]);
				if(index2==index1){
					count++;
					break;
				}
			}
		}
		

		double jaccard =(double)count/(double)(vector1.length+vector2.length);
		if(jaccard>0.2) return 1.0;
		else return 0.0;
		*/
		
		double tmp=0.0, square1=0.0, square2=0.0;
		for(int i=0;i<vector1.length;i++){
			tmp=Double.parseDouble(vector1[i].split(":")[1]);
			square1+=tmp*tmp;
		}
		square1=Math.sqrt(square1);
		tmp=0.0
		for(int i=0;i<vector2.length;i++){
			tmp=Double.parseDouble(vector2[i].split(":")[1]);
			square2+=tmp*tmp;
		}
		square2=Math.sqrt(square2);

		
		double diff=0.0,totalDif=0.0;
		for(int i=0;i<vector1.length;i++){
			int index1 =Integer.parseInt(vector1[i].split(":")[0]);
			for(int j=0;j<vector2.length;j++){
				int index2 =Integer.parseInt(vector2[j].split(":")[0]);
				if(index2==index1){
					diff =Double.parseDouble(vector1[i].split(":")[1])*Double.parseDouble(vector2[j].split(":")[1]);
					totalDif +=diff;
					break;
				}
			}
		}


		double something =totalDif/(square1*square2);
		if(something>0.8) return something;
		else return 0.0;
		
		/*
		 String[] vector1 =a.split("\\|");
		 String[] vector2 =b.split("\\|");
		 int size =Math.min(vector1.length,vector2.length);
		 double [] vec1 =new double[size];
		 double [] vec2 =new double[size];
		 double squareDis=0.0;
		 double square1 =0.0;
		 double square2 =0.0;
		 for(int i=0;i<size;i++){
		 vec1[i] =Double.parseDouble(vector1[i].split(":")[1]);
		 square1+=vec1[i]*vec1[i];
		 vec2[i] =Double.parseDouble(vector2[i].split(":")[1]);
		 square2 +=vec2[i]*vec2[i];
		 }
		 square1 =Math.sqrt(square1);
		 square2 =Math.sqrt(square2);
		 //cosin metrix
		 //		double total=0.0;
		 //		for(int i=0;i<size;i++){
		 //			double x1 =vec1[i];
		 //			double x2 =vec2[i];
		 //			total+=x1*x2;
		 //		}
		 //		double similarMS =total/(square1*square2);
		 //		if(similarMS>0.9){
		 //			return 1.0;
		 //		}else{
		 //			return 0.0;
		 //		}
		 for(int i=0;i<size;i++){
		 double x1 =vec1[i]/square1;
		 double x2 =vec2[i]/square2;
		 squareDis +=(x1-x2)*(x1-x2);
		 }
		 //		System.out.println(squareDis);
		 //				return 1.0-Math.sqrt(squareDis);
		 if(1.0-Math.sqrt(squareDis)>0.8){
		 return 1.0-Math.sqrt(squareDis);//ThangNguyen improved
		 }else{
		 return 0.0;
		 }
		 */

	}
}