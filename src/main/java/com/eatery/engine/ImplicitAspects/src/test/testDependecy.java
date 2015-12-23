/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.trees.tregex.ParseException;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import edu.stanford.nlp.util.CoreMap;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ruba
 */
public class testDependecy {
    
    public static void main(String args[]){
    Properties props = new Properties();
    props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
    TregexPattern npPattern = null;
        try {
            npPattern = TregexPattern.compile("@NP");
        } catch (ParseException ex) {
            Logger.getLogger(TestVersion2.class.getName()).log(Level.SEVERE, null, ex);
        }
     
    Annotation document = new Annotation("Bill is big and honest");
    pipeline.annotate(document);
    List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
    
    for (CoreMap sentence : sentences) {

        Tree sentenceTree = sentence.get(TreeAnnotation.class);
        TregexMatcher matcher = npPattern.matcher(sentenceTree);

        while (matcher.find()) {
            //this tree should contain "The fitness room" 
            Tree nounPhraseTree = matcher.getMatch();
            //Question : how do I find that "dirty" has a relationship to the nounPhraseTree


        }

        // Output dependency tree
        TreebankLanguagePack tlp = new PennTreebankLanguagePack();
        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
        GrammaticalStructure gs = gsf.newGrammaticalStructure(sentenceTree);
        Collection<TypedDependency> tdl = gs.typedDependenciesCollapsed();
        System.out.println(tdl);
        System.out.println("typedDependencies: "+tdl.iterator().next().gov().nodeString()); 
        System.out.println(tdl.iterator().next().reln().getShortName());
    }
    
    }
}
