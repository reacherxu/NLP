package sentiment;
import java.io.IOException;

public class LibSVMTest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//Test for svm_train and svm_predict
		//svm_train: 
		//	  param: String[], parse result of command line parameter of svm-train
		//    return: String, the directory of modelFile
		//svm_predect:
		//	  param: String[], parse result of command line parameter of svm-predict, including the modelfile
		//    return: Double, the accuracy of SVM classification
		String[] trainArgs = {"data/svmTrain"};//directory of training file
		String modelFile = svm_train.main(trainArgs);
		String[] testArgs = {"data/svmTest", modelFile, "data/svmRes"};//directory of test file, model file, result file
		Double accuracy = svm_predict.main(testArgs);
		System.out.println("SVM Classification is done! The accuracy is " + accuracy);
		
		//Test for cross validation
		//String[] crossValidationTrainArgs = {"-v", "10", "UCI-breast-cancer-tra"};// 10 fold cross validation
		//modelFile = svm_train.main(crossValidationTrainArgs);
		//System.out.print("Cross validation is done! The modelFile is " + modelFile);
		
		
		/*double g = (double)1/324;
		String[] path = {"-g",g+"","-c","7.24", "data\\train2.txt"};
        String modelFile = svm_train.main(path);
        String[] testArgs = {"data\\test2.txt", modelFile, "d:/max.txt"};//directory of test file, model file, result file  
        svm_predict.main(testArgs);*/
	}

}
