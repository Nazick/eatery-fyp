package com.eatery.engine.opennlp;

import opennlp.model.TrainUtil;
import opennlp.tools.namefind.*;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.eval.FMeasure;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Created by nazick on 11/15/15.
 */
public class OpenNLPModelAnalyzer {

    public FMeasure evaluate(String modelFilePath, String testFilePath){
        Charset charset = Charset.forName("UTF-8");
        InputStream eateryModel = null;
        FMeasure result = null;

        try {
            eateryModel = new FileInputStream(modelFilePath);
            TokenNameFinderModel model = new TokenNameFinderModel(eateryModel);

            TokenNameFinderEvaluator evaluator = new TokenNameFinderEvaluator(new NameFinderME(model));

            ObjectStream<NameSample> lineStream = new NameSampleDataStream(new PlainTextByLineStream(new FileInputStream(testFilePath), charset));

            evaluator.evaluate(lineStream);

            result = evaluator.getFMeasure();

            System.out.println(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (eateryModel != null) {
                try {
                    eateryModel.close();
                } catch (IOException e) {
                }
            }
            return result;
        }

    }

    public FMeasure crossValidation(String filePath){

        Charset charset = Charset.forName("UTF-8");
        ObjectStream<NameSample> sampleStream = null;
        FMeasure result = null;

        try {
            ObjectStream<String> lineStream =
                    new PlainTextByLineStream(new FileInputStream(filePath), charset);
            sampleStream = new NameSampleDataStream(lineStream);
            TrainingParameters params = TrainingParameters.defaultParams();
            params.put(TrainUtil.CUTOFF_PARAM, "100");
            params.put(TrainUtil.ITERATIONS_PARAM, "5");
            TokenNameFinderCrossValidator evaluator = new TokenNameFinderCrossValidator("en", 100, 5);
            evaluator.evaluate(sampleStream, 10);

            result = evaluator.getFMeasure();

            System.out.println(result.toString());
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            return result;
        }
    }

    public static void main(String args[]){
        OpenNLPModelAnalyzer openNLPModelAnalyzer = new OpenNLPModelAnalyzer();
        //openNLPModelAnalyzer.evaluate();
        String modelFilePath;
        String testFilePath = "src/main/resources/opennlp/evaluation/Test1.test";

        for(int i = 1 ;i<10;i++){

            modelFilePath = "src/main/resources/opennlp/evaluation/models/reviews_"+i+"00.bin";

            System.out.println("############ Evaluation for "+i+"00 reviews #############");
            System.out.println();
            openNLPModelAnalyzer.evaluate(modelFilePath,testFilePath);
            System.out.println();
        }

//        modelFilePath = "src/main/resources/opennlp/evaluation/models/reviews_1000.bin";
//        openNLPModelAnalyzer.evaluate(modelFilePath,testFilePath);

    }


}
