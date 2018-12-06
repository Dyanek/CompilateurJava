package fr.usmb.m1isc.compilation.tp;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

public class Arbre
{
    private final String valeur;
    private final Arbre gauche;
    private final Arbre droite;
    private final Type type;

    public Arbre(String valeur, Arbre gauche, Arbre droite, Type type)
    {
        this.valeur = valeur;
        this.gauche = gauche;
        this.droite = droite;
        this.type = type;
    }

    public Arbre(String valeur, Arbre gauche, Arbre droite)
    {
        this.valeur = valeur;
        this.gauche = gauche;
        this.droite = droite;
        this.type = Type.Operateur;
    }

    public void afficher()
    {
        System.out.println(this.valeur + " " + this.type);

        if (this.gauche != null)
            this.gauche.afficher();

        if (this.droite != null)
            this.droite.afficher();
    }

    public void genere(Writer writer, int whileNumber, int ifNumber, int gtNumber, int gteNumber) throws IOException
    {
        switch (valeur)
        {
            case ";":
                if (this.gauche != null)
                {
                    this.gauche.genere(writer, whileNumber, ifNumber, gtNumber, gteNumber);
                    writer.write("\tpop eax\n");
                }

                if (this.droite != null)
                    this.droite.genere(writer, whileNumber, ifNumber, gtNumber, gteNumber);

                break;

            case "let":
                this.droite.genere(writer, whileNumber, ifNumber, gtNumber, gteNumber);

                writer.write("\tpop eax\n");
                writer.write("\tmov " + this.gauche.valeur + ", eax\n");
                writer.write("\tpush eax\n");

                break;

            case "+":
                this.gauche.genere(writer, whileNumber, ifNumber, gtNumber, gteNumber);
                this.droite.genere(writer, whileNumber, ifNumber, gtNumber, gteNumber);

                writer.write("\tpop ebx\n");
                writer.write("\tpop eax\n");
                writer.write("\tadd eax, ebx\n");
                writer.write("\tpush eax\n");

                break;

            case "-":
                this.gauche.genere(writer, whileNumber, ifNumber, gtNumber, gteNumber);
                this.droite.genere(writer, whileNumber, ifNumber, gtNumber, gteNumber);

                writer.write("\tpop ebx\n");
                writer.write("\tpop eax\n");
                writer.write("\tsub ebx, eax\n");
                writer.write("\tpush eax\n");

                break;

            case "*":
                this.gauche.genere(writer, whileNumber, ifNumber, gtNumber, gteNumber);
                this.droite.genere(writer, whileNumber, ifNumber, gtNumber, gteNumber);

                writer.write("\tpop ebx\n");
                writer.write("\tpop eax\n");
                writer.write("\tmul eax, ebx\n");
                writer.write("\tpush eax\n");

                break;

            case "/":
                this.gauche.genere(writer, whileNumber, ifNumber, gtNumber, gteNumber);
                this.droite.genere(writer, whileNumber, ifNumber, gtNumber, gteNumber);

                writer.write("\tpop ebx\n");
                writer.write("\tpop eax\n");
                writer.write("\tdiv eax, ebx\n");
                writer.write("\tpush eax\n");

                break;

            case "%":
                this.gauche.genere(writer, whileNumber, ifNumber, gtNumber, gteNumber);
                this.droite.genere(writer, whileNumber, ifNumber, gtNumber, gteNumber);

                writer.write("\tpop ebx\n");
                writer.write("\tpop eax\n");
                writer.write("\tmov ecx, eax\n");
                writer.write("\tdiv ecx, ebx\n");
                writer.write("\tmul ecx, ebx\n");
                writer.write("\tsub eax, ecx\n");
                writer.write("\tpush eax\n");

                break;

            case "input":
                writer.write("\tin eax\n");
                writer.write("\tpush eax\n");

                break;

            case "output":
                this.gauche.genere(writer, whileNumber, ifNumber, gtNumber, gteNumber);

                writer.write("\tout eax\n");

                break;

            case "while":
                whileNumber++;

                writer.write("debut_while_" + whileNumber + ":\n");
                //gauche = condition, droite = bloc à effectuer si cond = true

                this.gauche.genere(writer, whileNumber, ifNumber, gtNumber, gteNumber);

                writer.write("faux_gt_" + whileNumber + ":\n");
                writer.write("\tmov eax, 0\n");

                writer.write("sortie_gt_" + whileNumber + ":\n");
                writer.write("\tjz sortie_while_" + whileNumber + "\n");

                this.droite.genere(writer, whileNumber, ifNumber, gtNumber, gteNumber);

                writer.write("\tjmp debut_while_" + whileNumber + "\n");

                writer.write("sortie_while_" + whileNumber + ":\n");

                break;
                
            case "if":
                ifNumber++;
                
                writer.write("debut_if_" + ifNumber + ":\n");
                //gauche = condition, droite = bloc à effectuer si cond = true

                this.gauche.genere(writer, whileNumber, ifNumber, gtNumber, gteNumber);

                writer.write("faux_gt_" + ifNumber + ":\n");
                this.droite.droite.genere(writer, whileNumber, ifNumber, gtNumber, gteNumber);

                writer.write("sortie_gt_" + ifNumber + ":\n");
                writer.write("\tjz sortie_if_" + ifNumber + "\n");

                this.droite.gauche.genere(writer, whileNumber, ifNumber, gtNumber, gteNumber);

                writer.write("sortie_if_" + ifNumber + ":\n");
                
                break;

            case "<":
                gtNumber++;

                this.gauche.genere(writer, whileNumber, ifNumber, gtNumber, gteNumber);
                this.droite.genere(writer, whileNumber, ifNumber, gtNumber, gteNumber);

                writer.write("\tpop eax\n");
                writer.write("\tpop ebx\n");
                writer.write("\tsub eax, ebx\n");
                writer.write("\tjle faux_gt_" + gtNumber + "\n");
                writer.write("\tmov eax, 1\n");
                writer.write("\tjmp sortie_gt_" + gtNumber + "\n");
                break;
                
            case "<=":
                gteNumber++;

                this.gauche.genere(writer, whileNumber, ifNumber, gtNumber, gteNumber);
                this.droite.genere(writer, whileNumber, ifNumber, gtNumber, gteNumber);

                writer.write("\tpop eax\n");
                writer.write("\tpop ebx\n");
                writer.write("\tsub eax, ebx\n");
                writer.write("\tjl faux_gt_" + gteNumber + "\n");
                writer.write("\tmov eax, 1\n");
                writer.write("\tjmp sortie_gt_" + gteNumber + "\n");
                
                break;

            default:
                writer.write("\tmov eax, " + this.valeur + "\n");
                writer.write("\tpush eax\n");
                break;
        }
    }

    public ArrayList<String> getIdentifiants()
    {
        ArrayList<String> listeIdentifiants = new ArrayList<>();

        if (this.type == Type.Identifiant)
            listeIdentifiants.add(this.valeur);

        if (this.gauche == null && this.droite != null)
            listeIdentifiants.addAll(this.droite.getIdentifiants());
        else if (this.gauche != null && this.droite == null)
            listeIdentifiants.addAll(this.gauche.getIdentifiants());
        else if (this.gauche != null && this.droite != null)
        {
            listeIdentifiants.addAll(this.gauche.getIdentifiants());
            listeIdentifiants.addAll(this.droite.getIdentifiants());
        }

        return listeIdentifiants;
    }

    public void test()
    {
        System.out.println(this.valeur);
        System.out.println(this.gauche.valeur);
        System.out.println(this.droite.valeur);
    }
}
