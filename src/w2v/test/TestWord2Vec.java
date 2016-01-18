package w2v.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import w2v.nlp.util.Tokenizer;
import w2v.org.nlp.vec.VectorModel;
import w2v.org.nlp.vec.Word2Vec;

/**
 * @author siegfang
 */
public class TestWord2Vec {

    public static void readByJava(String textFilePath, String modelFilePath){

        Word2Vec wv = new Word2Vec.Factory()
                .setMethod(Word2Vec.Method.Skip_Gram)
                .setNumOfThread(1).build();

        try  {
        	BufferedReader br =
                    new BufferedReader(new FileReader(textFilePath));
            int lineCount = 0;
            for (String line = br.readLine(); line != null;
                    line = br.readLine()){
                wv.readTokens(new Tokenizer(line, " "));
//                System.out.println(line);
                lineCount ++;

            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        wv.training();
        wv.saveModel(new File(modelFilePath));
    }

    public static void testVector(String modelFilePath){

        VectorModel vm = VectorModel.loadFromFile(modelFilePath);
        Set<VectorModel.WordScore> result1 = Collections.emptySet();

        result1 = vm.similar("亲");
        for (VectorModel.WordScore we : result1){
            System.out.println(we.name + " :\t" + we.score);
        }
    }

    public static void main(String[] args){

        String textFilePath = "D:/data/corpus.dat";
        String modelFilePath = "D:/data/corpus.nn";
        readByJava(textFilePath, modelFilePath);
        testVector(modelFilePath);
    }

}
