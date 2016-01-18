package sentiment;
import java.io.IOException;


public class LibSVM {

	public static void main(String[] args) throws IOException {

	    String []arg ={ "trainfile\\train.txt" //存放SVM训练模型用的数据的路径
};  //存放SVM通过训练数据训/ //练出来的模型的路径

	    String []parg={"trainfile\\scale.txt",   //这个是存放测试数据
	    		       "trainfile\\model_r.txt",  //调用的是训练以后的模型
	    		        "trainfile\\out_r.txt"};  //生成的结果的文件的路径
	    System.out.println("........SVM运行开始..........");  
	    //创建一个训练对象
	    svm_train t = new svm_train();  
	    //创建一个预测或者分类的对象
	    svm_predict p= new svm_predict();  
	    t.main(arg);   //调用
	    p.main(parg);  //调用
		
	}

}
