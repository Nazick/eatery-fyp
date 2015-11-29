package com.eatery.engine.opennlp;

import opennlp.tools.namefind.*;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.eval.FMeasure;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Created by nazick on 11/15/15.
 */
public class OpenNLPModelAnalyzer {

    public void evaluator(){

        InputStream eateryModel = null;
        try {
            eateryModel = new FileInputStream("src/main/resources/opennlp/eval/Test4.bin");
            TokenNameFinderModel model = new TokenNameFinderModel(eateryModel);

            TokenNameFinderEvaluator evaluator = new TokenNameFinderEvaluator(new NameFinderME(model));
            Charset charset = Charset.forName("UTF-8");
            ObjectStream<NameSample> lineStream = new NameSampleDataStream(new PlainTextByLineStream(new FileInputStream("src/main/resources/opennlp/Test.test"), charset));

            evaluator.evaluate(lineStream);

            FMeasure result = evaluator.getFMeasure();

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
        }

    }

    public static void main(String args[]){
        OpenNLPModelAnalyzer openNLPModelAnalyzer = new OpenNLPModelAnalyzer();
        openNLPModelAnalyzer.evaluator();
    }


}
