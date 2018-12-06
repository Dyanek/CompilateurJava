# TPs 2, 3 & 4 Compilation : Génération d'arbres abstraits

## TP 2

L'objectif du TP 2 était de créer un fichier CUP permettant de construire un arbre à partir d'une saisie utilisateur. Le code devait être écrit à l'aide d'un fichier existant fournir par notre professeur.

Une classe Java appelée "Arbre" a été créée, elle est constituée de quatre attributs :
```
private final String valeur;
private final Arbre gauche;
private final Arbre droite;
private final Type type;
```
* valeur est la valeur de l'arbre, cela peut être un entier ou un signe : if, <, while, <=, *, +, ...
* type est issu d'une classe enum, il peut être un Entier (nombre), un Identifiant (let) ou un Operateur (opérateur par défaut)
* gauche est la partie gauche de l'arbre
* droite est la partie droite de l'arbre

Par exemple, l'arbre suivant :
```
valeur = "*";
gauche.valeur = "5";
droite.valeur = "7;
```
correspond à une multiplication.

## TPs 3 & 4

Pour ces TPs, nous avions comme objectif de créer un fichier .asm à partir des arbres construits au TP précédent, ce fichier peut ensuit être lu par une machine à registres.

Le code à génerer se fait dans une méthode appelée genere(Writer writer, int whileNumber, int ifNumber, int gtNumber, int gteNumber) de la classe Arbre.

Le paramètre writer permet d'écrire les arbres générés directement dans un fichier .asm. Chaque arbre fonctionne de manière récurrente jusqu'à arriver au bout d'une branche (gauche = null && droite = null).

Les autres paramètres servent à garder le compte du nombre d'opérations utilisant certains opérateur. Ces opérations sont :
* while
* if
* <
* <=
