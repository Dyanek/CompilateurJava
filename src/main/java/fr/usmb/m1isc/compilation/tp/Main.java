package fr.usmb.m1isc.compilation.tp;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java_cup.runtime.Symbol;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        LexicalAnalyzer yy;

        if (args.length > 0)
            yy = new LexicalAnalyzer(new FileReader(args[0]));
        else
            yy = new LexicalAnalyzer(new InputStreamReader(System.in));

        @SuppressWarnings("deprecation")
        parser p = new parser(yy);
        Symbol s = p.parse();

        Arbre arbre = (Arbre) s.value;

        arbre.afficher();

        ArrayList<String> listeIdentifiants = arbre.getIdentifiants();

        Set<String> hs = new HashSet<>();
        hs.addAll(listeIdentifiants);
        listeIdentifiants.clear();
        listeIdentifiants.addAll(hs);

        String filename = "../sortie.asm";

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8")))
        {
            writer.write("DATA SEGMENT\n");

            if (!listeIdentifiants.isEmpty())
            {
                for (String identifiant : listeIdentifiants)
                    writer.write("\t" + identifiant + " DD\n");
            }

            writer.write("DATA ENDS\n");

            writer.write("CODE SEGMENT\n");

            arbre.genere(writer, 0, 0, 0, 0);

            writer.write("CODE ENDS\n");
        }
    }
}
