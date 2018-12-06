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

Le fichier sortie.asm est le fichier généré par le programme, il se trouvera avant la racine du dossier git : "../sortie.asm". Ce fichier peut être exécuté par un émulateur.

## Exemples

Si nous saisissons le code suivant :
```
let a = input;
let b = input;
while(0<b)
do(let aux = (a mod b); let a = b; let b = aux);
output a
.
```
Le fichier de sortie sera :
```
DATA SEGMENT
	a DD
	b DD
	aux DD
DATA ENDS
CODE SEGMENT
	in eax
	push eax
	pop eax
	mov a, eax
	push eax
	pop eax
	in eax
	push eax
	pop eax
	mov b, eax
	push eax
	pop eax
debut_while_1:
	mov eax, 0
	push eax
	mov eax, b
	push eax
	pop eax
	pop ebx
	sub eax, ebx
	jle faux_gt_1
	mov eax, 1
	jmp sortie_gt_1
faux_gt_1:
	mov eax, 0
sortie_gt_1:
	jz sortie_while_1
	mov eax, a
	push eax
	mov eax, b
	push eax
	pop ebx
	pop eax
	mov ecx, eax
	div ecx, ebx
	mul ecx, ebx
	sub eax, ecx
	push eax
	pop eax
	mov aux, eax
	push eax
	pop eax
	mov eax, b
	push eax
	pop eax
	mov a, eax
	push eax
	pop eax
	mov eax, aux
	push eax
	pop eax
	mov b, eax
	push eax
	jmp debut_while_1
sortie_while_1:
	pop eax
	mov eax, a
	push eax
	out eax
CODE ENDS
```

Pour le code suivant:
```
let a = 12;
let b = 5;
let c = a + b;
output c
```
Le fichier suivant est créé:
```
DATA SEGMENT
	a DD
	b DD
	c DD
DATA ENDS
CODE SEGMENT
	mov eax, 12
	push eax
	pop eax
	mov a, eax
	push eax
	pop eax
	mov eax, 5
	push eax
	pop eax
	mov b, eax
	push eax
	pop eax
	mov eax, a
	push eax
	mov eax, b
	push eax
	pop ebx
	pop eax
	add eax, ebx
	push eax
	pop eax
	mov c, eax
	push eax
	pop eax
	mov eax, c
	push eax
	out eax
CODE ENDS
```

Une partie du programme est donc fonctionnelle.

Malheureusement, je n'ai pas complètement réussi à créer la partie si l'opérateur saisi est du type
```
IF condition THEN expression1 ELSE expression2
```
