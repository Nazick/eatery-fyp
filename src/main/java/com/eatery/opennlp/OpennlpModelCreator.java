package com.eatery.opennlp;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.featuregen.*;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.Collections;

/**
 * Created by nazick on 7/23/15.
 */
public class OpennlpModelCreator {

    public static void main(String[] args) {
        OpennlpModelCreator opennlpModelCreator = new OpennlpModelCreator();
        String trainFilePath, modelFilePath;

        for(int i = 1 ;i<=15;i++){
            trainFilePath = "src/main/resources/opennlp/evaluation/reviews_"+i+"00.train";
            modelFilePath = "src/main/resources/opennlp/evaluation/models/reviews_"+i+"00.bin";

            System.out.println("Creating model for" + trainFilePath);
            opennlpModelCreator.createModel(trainFilePath,modelFilePath);
        }
//
//        trainFilePath = "src/main/resources/opennlp/evaluation/reviews_1300.train";
//        modelFilePath = "src/main/resources/opennlp/evaluation/models/reviews_1300.bin";
//
//        System.out.println("Creating model for" + trainFilePath);
//        opennlpModelCreator.createModel(trainFilePath,modelFilePath);

    }

    public void createModel(String trainFilePath, String modelFilePath){
        Charset charset = Charset.forName("UTF-8");

        ObjectStream<NameSample> sampleStream = null;
        try {
            ObjectStream<String> lineStream =
                    new PlainTextByLineStream(new FileInputStream(trainFilePath), charset);
            sampleStream = new NameSampleDataStream(lineStream);
        }catch(Exception ex){}

        TokenNameFinderModel model = null;
        AdaptiveFeatureGenerator adaptiveFeatureGenerator = new CachedFeatureGenerator(
                new AdaptiveFeatureGenerator[]{
                        new WindowFeatureGenerator(new TokenFeatureGenerator(), 2, 2),
                        new WindowFeatureGenerator(new TokenClassFeatureGenerator(true), 2, 2),
                        new OutcomePriorFeatureGenerator(),
                        new PreviousMapFeatureGenerator(),
                        new BigramNameFeatureGenerator(),
                        new SentenceFeatureGenerator(true, false)
                });

        try {
            model = NameFinderME.train("en", "Restaurant", sampleStream, TrainingParameters.defaultParams(),
                    adaptiveFeatureGenerator, Collections.<String, Object>emptyMap());
        }catch(Exception ex){

        }finally {
            try {
                sampleStream.close();
            }catch(Exception ex){}
        }

        BufferedOutputStream modelOut = null;
        try {
            try {
                modelOut = new BufferedOutputStream(new FileOutputStream(modelFilePath));
                model.serialize(modelOut);
            }catch (Exception ex){}
        } finally {
            if (modelOut != null)
                try {
                    modelOut.close();
                }catch(Exception ex){}
        }
    }
}

