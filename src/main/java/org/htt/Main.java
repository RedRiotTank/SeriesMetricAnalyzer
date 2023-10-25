package org.htt;

import console.ConsoleProcessor;
import csvmetricprocessor.CsvMetricProcessor;
import customanalyzers.SynomAnalyzer;
import files.FileProcessor;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.util.Objects;

import static console.ConsoleProcessor.*;
import static java.lang.System.exit;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            parameterError();
            printHelpMessage();
            return;
        }

        String lastArg = args[args.length - 1];
        FileProcessor.folderInitializer(lastArg);
        CsvMetricProcessor.loadEpisodeDataModels(FileProcessor.getCsvFiles());

        boolean
                averageVotes = false,
                averageMetric = false,
                averageCharacterNumber = false;

        SynomAnalyzer synomAnalyzer = null;

        for(int i=0; i< args.length-1; i++){
            String arg = args[i];

            if(Objects.equals(arg, "-analyzer")){
                if(!args[i+1].endsWith(".chs")){
                    synonymFormatError();
                    printHelpMessage();
                    exit(1);
                } else {
                    i++;
                    synomAnalyzer = new SynomAnalyzer(FileProcessor.generateSynomMap(args[i]));
                    System.out.println(synomAnalyzer.namesMap);
                }
            }

            switch (arg) {
                case "-V": // average votes
                        averageVotes = true;
                        break;
                case "-R":  // average metric
                        averageMetric = true;
                        break;
                case "-C": // average character number
                        averageCharacterNumber = true;
                        break;
                case "-analyzer":
                    String text = "Mariom fat tony D'Amico";
                    TokenStream tokenStream = synomAnalyzer.tokenStream("field", text);

                    // Obtener el atributo CharTermAttribute para acceder a los tokens
                    CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

                    // Procesar y mostrar los tokens resultantes
                    tokenStream.reset();
                    while (tokenStream.incrementToken()) {
                        System.out.println(charTermAttribute.toString());
                    }
                    break;
            }
            CsvMetricProcessor.executeOptions(averageVotes,averageMetric, averageCharacterNumber);
        }

        if(averageVotes) ConsoleProcessor.printAverageVoteNumber(CsvMetricProcessor.getAverageVotes());

        if(averageMetric) ConsoleProcessor.PrintAverageSeriesRating(CsvMetricProcessor.getAverageRating());

        if(averageCharacterNumber) ConsoleProcessor.printAverageCharacterNumber(CsvMetricProcessor.getAverageCharacterNumber());

    }
}