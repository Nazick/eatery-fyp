package com.eatery.engine.opennlp;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.featuregen.AdaptiveFeatureGenerator;

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
        Charset charset = Charset.forName("UTF-8");

        ObjectStream<NameSample> sampleStream = null;
        try {
            ObjectStream<String> lineStream =
                    new PlainTextByLineStream(new FileInputStream("src/main/resources/opennlp/eval/Test4.train"), charset);
            sampleStream = new NameSampleDataStream(lineStream);
        }catch(Exception ex){}

        TokenNameFinderModel model = null;
        AdaptiveFeatureGenerator adaptiveFeatureGenerator = null;

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
            String modelFile = "src/main/resources/opennlp/eval/Test4.bin";
            try {
                modelOut = new BufferedOutputStream(new FileOutputStream(modelFile));
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
