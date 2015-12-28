package com.eatery.engine.sentimentAnalysis;

import com.eatery.engine.opennlp.OpennlpTagger;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.AnnotatedTree;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.CoreMap;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * Created by nazick on 9/3/15.
 */
public class SentimentAnalyzer {


    public SentimentAnalyzer() {
        props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        pipeline = new StanfordCoreNLP(props);
    }

    StanfordCoreNLP pipeline;
    Properties props;

    public String findSentimentState(String line) {

        int longest = 0;
        int mainSentiment = 0;
        String sentiment = null;
        Annotation annotation = pipeline.process(line);
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            sentiment = sentence.get(SentimentCoreAnnotations.ClassName.class);

            Tree tree = sentence
                    .get(AnnotatedTree.class);
            int sentimentScore = RNNCoreAnnotations.getPredictedClass(tree);
            String partText = sentence.toString();
            if (partText.length() > longest) {
                mainSentiment = sentimentScore;
                longest = partText.length();
            }

            System.out.println(sentiment + "\t" + mainSentiment + "\t" + sentence);
            mainSentiment = 0;
            longest = 0;
        }
        return sentiment;
    }

    public Integer findSentimentScore(String line) {

        int mainSentiment = 0;
        if (line != null && line.length() > 0) {
            int longest = 0;
            Annotation annotation = pipeline.process(line);
            for (CoreMap sentence : annotation
                    .get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sentence
                        .get(AnnotatedTree.class);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                String partText = sentence.toString();
                if (partText.length() > longest) {
                    mainSentiment = sentiment;
                    longest = partText.length();
                }
            }
        }
        System.out.println(mainSentiment + " - " + line);
        return mainSentiment;
    }

    public static void main(String[] args) {
        SentimentAnalyzer esa = new SentimentAnalyzer();
        try {
            esa.findSentimentScore("in the top five of");
        }catch (Exception e){
            e.printStackTrace();
        }
        //esa.tdl();
       // esa.findSentimentState("think the decor was great and");

    }

    public void tdl() {
        LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz","-maxLength", "80", "-retainTmpSubcategories");
        TreebankLanguagePack tlp = new PennTreebankLanguagePack();

        OpennlpTagger nerTagger = new OpennlpTagger();

        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
        String[] sent = nerTagger.tokenizeSentence("The restaurant has a nice setting and pleasant decor, but the food is seriously lacking");
        Tree parse = lp.apply(Sentence.toWordList(sent));
        GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
        Collection<TypedDependency> tdls = gs.typedDependenciesCollapsedTree();
        System.out.println(tdls);
    }

}
