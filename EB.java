import java.security.DrbgParameters.Reseed;

/**
 * @author Camille Protat 
 * 		Ce programme permet de construire des expressions
 *         booléennes sous forme d'arbres binaires
 *
 *         L'expression booléenne ne nécessite que 2 variables Le Noeud qui est
 *         la racine de l'arbre et l'entier k qui est le nombre de variables de
 *         l'expression
 */

public class EB {

	private Noeud racine;
	private int k;

	// Constructeur pour une expression booléenne vide
	public EB() {
		racine = new Noeud();
		k = 0;
	}

	// Constructeur avec un caractère qui sera utiisé par la méthode singleton
	public EB(char c) {
		racine = new Noeud(c);
		k = 0;
	}

	// accesseurs
	/**
	 * Renvoie le noeud racine de l'EB
	 * @return Noeud racine
	 */
	public Noeud getRacine() {
		return racine;
	}

	/**
	 * Renvoie le nombre de variable k de l'EB
	 * @return int k
	 */
	public int nbVar() {
		return k;
	}

	/**
	 * Crée un nouvel EB contenant un seul noeud 
	 * @param c le caractère contenu du nouveau singleton
	 * @return EB
	 */
	public EB singleton(char c) {
		EB n = new EB(c);
		n.k++;
		return n;
	}

	/**
	 * Crée un nouvel EB avec le caractère de disjonction (OU) en racine
	 * On met à gauche l'EB appelant et à droite l'EB en paramètre
	 * @param d : EB en parametre, sera a droite de la disjonction
	 * @return E
	 */
	public EB disjonction(EB d) {
		EB myNewEB = new EB('∨');
		myNewEB.k = this.nbVar() + d.nbVar();
		myNewEB.racine.setGauche(this.getRacine());
		myNewEB.racine.setDroite(d.getRacine());
		return myNewEB;
	}

	/**
	 * Crée un nouvel EB avec le caractère de conjonction (ET) en racine
	 * On met à gauche l'EB appelant et à droite l'EB en paramètre
	 * @param d : EB en parametre, sera a droite de la conjonction
	 * @return EB
	 */
	public EB conjonction(EB d) {
		EB myNewEB = new EB('∧');
		myNewEB.k = this.nbVar() + d.nbVar();
		myNewEB.racine.setGauche(this.getRacine());
		myNewEB.racine.setDroite(d.getRacine());
		return myNewEB;
	}

	/**
	 * Crée un nouvel EB avec le caractère de negation en racine
	 * On met l'EB appelant à droite pour faciliter l'écriture
	 * @return EB
	 */
	public EB negation() {
		EB n = new EB('¬');
		n.k = this.k;
		n.racine.setDroite(this.racine);
		return n;
	}

	//Ecrit l'EB en notation infixée (avec quelques parenthèses en trop cependant)
	public String toString() {
		return ecrire(racine);
	}

	/**
	 * Est appelée récursivement après son premier appel dans toString().
	 * La méthode traverse tout l'arbre et affiche les caractère contenus dans les noeuds
	 * @param courant : le noeud que l'on étudie actuellement
	 * @return String
	 */
	private String ecrire(Noeud courant) {
		String s = "";
		if (courant.getGauche() != null)
			s += "(" + ecrire(courant.getGauche());
		s += courant;
		if (courant.getDroite() != null)
			s += ecrire(courant.getDroite()) + ")";
		return s;
	}

	/**
	 * vérifie qu'un EB est vide, vrai si vide et faux sinon
	 * @return boolean
	 */
	public boolean estVide() {
		if (racine.estNull() && k == 0)
			return true;
		return false;
	}

	/**
	 * Evaluation de l'EB grace au vecteur passé en paramètre à l'execution du programme. 
	 * on y appelle la fonction récursive eval()
	 * @param v : Vecteur dont les valeurs sont soit "V" soit "F", sera converti en vecteur de boolean dans la méthode
	 * @return boolean
	 */
	public boolean evaluer(String[] v) {
		if (estVide()) {
			System.out.println("L'expression est vide");
			return false;
		}
		boolean[] vect = new boolean[k];
		for (int i = 0; i < k; i++) {
			if (v[i].equals("V"))
				vect[i] = true;
			else
				vect[i] = false;
		}
		return eval(vect, racine);
	}

	/**
	 * Fonction recursive d'évaluation.
	 * @param v : vecteur booléen
	 * @param courant : le noeud qu'on est entrain d'étudier
	 * @return boolean
	 */
	private boolean eval(boolean[] v, Noeud courant) {
		//si le noeud est une negation, on retourne l'inverse de l'évaluation du noeud de droite
		if (courant.estNeg()) {
			return !eval(v, courant.getDroite());
		}
		
		//si le noeud est une conjonction, retourne l'evaluation du noeud de droite ET celle du noeud de gauche
		if (courant.estEt()) {
			return (eval(v, courant.getGauche()) && eval(v, courant.getDroite()));
		}
		
		//si le noeud est une disjonction, retourne l'evaluation du noeud de droite OU celle du noeud de gauche
		if (courant.estOu()) {
			return (eval(v, courant.getGauche()) || eval(v, courant.getDroite()));
		}
		
		//si le noeud est une feuille 
		if (courant.estFeuille()) {
			char n = 'a';
			//on doit vérifier si c'est une variable valide
			//dans la table ascii les lettres entre a et z sont entre 97 et 122
			while (courant.getContenu() != n) {
				//on cherche la lettre et si on ne la trouve pas alors on renvoie faux
				n++;
				if (n > 122) {
					System.out.println("La feuille n'a pas une lettre valide !");
					return false;
				}
			}
			//quand la lettre est trouver on renvoie le booleen correspondant du vecteur
			//sa position est n-97 car si 'a' = 97 alors on aura la position 0 du vecteur et ainsi de suite. 
			return v[n - 97];
		}
		return false;
	}

	public static void main(String[] args) {
		//IL NE FAUT PAS OUBLIER DE DONNER LE VECTEUR SOUS LA FORME : {"V","F","F","V","F"} !
		//Il reste une erreur dans le compte du nombre de variables...
		/*
		EB a = new EB().singleton('a');// a
		EB b = new EB().singleton('b');// b
		EB c = new EB().singleton('c');// c
		EB d = new EB().singleton('d');// d

		EB EB1 = c.disjonction(a);// a ou c
		EB notA = a.negation();
		EB EB2 = notA.conjonction(EB1); // not a et (a ou c)
		EB1 = EB2.negation(); // not (not a et (a ou c))

		EB EB3 = b.conjonction(d);// d et b
		EB3 = EB3.negation();// not (d et b)

		EB myEB = EB1.disjonction(EB3); // (not (not a et (a ou c))) OU (not (d et b))
		System.out.println(myEB + " le nombre de variables est de : " + myEB.nbVar());
		System.out.println(myEB.evaluer(args));

		EB notC = c.negation(); // not c
		EB EB4 = (a.negation()).disjonction(b.conjonction(c)); // not a ou (b et c)
		EB myEB2 = EB4.conjonction(notC);// (not a ou (b et c)) et not c

		System.out.println(myEB2 + " le nombre de variables est de : " + myEB2.nbVar());
		System.out.println(myEB2.evaluer(args));
		*/
		EB monA = new EB().singleton('a');
		EB monB = new EB().singleton('b');
		EB monC = new EB().singleton('c');
		EB nonC = monC.negation();
		EB nonA = monA.negation();

		EB aConjNonC = monA.conjonction(nonC);
		EB nonAdisjB = nonA.disjonction(monB);
		EB result = aConjNonC.disjonction(nonAdisjB);

		System.out.println(result + " le nombre de variables est de : " + result.nbVar());
		System.out.println(result.evaluer(args));
	}
}
