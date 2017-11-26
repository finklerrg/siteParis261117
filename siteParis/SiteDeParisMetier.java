

package siteParis;


import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author Bernard Prou et Julien Mallet
 * <br><br>
 * La classe qui contient toutes les méthodes "Métier" de la gestion du site de paris.
 * <br><br>
 * Dans toutes les méthodes :
 * <ul>
 * <li>un paramètre de type <code>String</code> est invalide si il n'est pas instancié.</li>
 *  <li>pour la validité d'un password de gestionnaire et d'un password de joueur :
 * <ul>
 * <li>       lettres et chiffres sont les seuls caractères autorisés </li>
 * <li>       il doit avoir une longueur d'au moins 8 caractères </li>
 * </ul></li>
 * <li>pour la validité d'un pseudo de joueur  :
 * <ul>
 * <li>        lettres et chiffres sont les seuls caractères autorisés  </li>
 * <li>       il doit avoir une longueur d'au moins 4 caractères</li>
 * </ul></li>
 * <li>pour la validité d'un prénom de joueur et d'un nom de joueur :
 *  <ul>
 *  <li>       lettres et tiret sont les seuls caractères autorisés  </li>
 *  <li>      il  doit avoir une longueur d'au moins 1 caractère </li>
 * </ul></li>
 * <li>pour la validité d'une compétition  :
 *  <ul>
 *  <li>       lettres, chiffres, point, trait d'union et souligné sont les seuls caractères autorisés </li>
 *  <li>      elle  doit avoir une longueur d'au moins 4 caractères</li>
 * </ul></li>
 * <li>pour la validité d'un compétiteur  :
 *  <ul>
 *  <li>       lettres, chiffres, trait d'union et souligné sont les seuls caractères autorisés </li>
 *  <li>      il doit avoir une longueur d'au moins 4 caractères.</li>
 * </ul></li></ul>
 */

public class SiteDeParisMetier {


	private Gestionnaire leGestionnaire;
	private LinkedList<Joueur> joueurs;
	private LinkedList<Competition> competitions;


	public SiteDeParisMetier(String passwordGestionnaire) throws MetierException {
		leGestionnaire= new Gestionnaire(passwordGestionnaire);
		joueurs=new LinkedList<Joueur> ();
		competitions=new LinkedList<Competition> ();

	}





	// Les méthodes du gestionnaire (avec mot de passe gestionnaire)



	/**
	 * inscrire un joueur.
	 *
	 * @param nom   le nom du joueur
	 * @param prenom   le prénom du joueur
	 * @param pseudo   le pseudo du joueur
	 * @param passwordGestionnaire  le password du gestionnaire du site
	 *
	 * @throws MetierException   levée
	 * si le <code>passwordGestionnaire</code> proposé est invalide,
	 * si le <code>passwordGestionnaire</code> est incorrect.
	 * @throws JoueurExistantException   levée si un joueur existe avec les mêmes noms et prénoms ou le même pseudo.
	 * @throws JoueurException levée si <code>nom</code>, <code>prenom</code>, <code>pseudo</code> sont invalides.
	 *
	 * @return le mot de passe (déterminé par le site) du nouveau joueur inscrit.
	 */
	public String inscrireJoueur(String nom, String prenom, String pseudo, String passwordGestionnaire) throws MetierException, JoueurExistantException, JoueurException {

		validitePasswordGestionnaire(passwordGestionnaire);
		if(!leGestionnaire.getPasswordGestionnaire().equals(passwordGestionnaire)) throw new MetierException();


		Joueur j=new Joueur(nom,prenom, pseudo,passwordGestionnaire);



		for(int i=0;i<joueurs.size();i=i+1){

			if(joueurs.get(i).getNom().equals(j.getNom()) && joueurs.get(i).getPrenom().equals(j.getPrenom()) ) throw new JoueurExistantException();
			if(joueurs.get(i).getPseudo().equals(j.getPseudo()) )  throw new JoueurExistantException();

		}


		joueurs.add(j);

		Random random = new Random();
		int[] arraypass = new int[8];
		String[] arraypassstr = new String[8];
		String password="";
		for (int i=0; i<8; i++){
			arraypass[i]=random.nextInt(9);
			arraypassstr[i]=Integer.toString(arraypass[i]);
			password=password+arraypassstr[i];

		}
		//System.out.println(password);


		j.setPasswordJouer(password);

		return password;

	}

	/**
	 * supprimer un joueur.
	 *
	 * @param nom   le nom du joueur
	 * @param prenom   le prénom du joueur
	 * @param pseudo   le pseudo du joueur
	 * @param passwordGestionnaire  le password du gestionnaire du site
	 *
	 * @throws MetierException
	 * si le <code>passwordGestionnaire</code>  est invalide,
	 * si le <code>passwordGestionnaire</code> est incorrect.
	 * @throws JoueurInexistantException   levée si il n'y a pas de joueur  avec le même <code>nom</code>, <code>prenom</code>  et <code>pseudo</code>.
	 * @throws JoueurException levée
	 * si le joueur a des paris en cours,
	 * si <code>nom</code>, <code>prenom</code>, <code>pseudo</code> sont invalides.
	 *
	 * @return le nombre de jetons à rembourser  au joueur qui vient d'être désinscrit.
	 *
	 */
	public long desinscrireJoueur(String nom, String prenom, String pseudo, String passwordGestionnaire) throws MetierException, JoueurInexistantException, JoueurException {



		validitePasswordGestionnaire(passwordGestionnaire);
		if(!leGestionnaire.getPasswordGestionnaire().equals(passwordGestionnaire)) throw new MetierException();

		validiteNomJoueur(nom);
		validitePrenomJoueur(prenom);
		validitePseudoJoueur(pseudo);

		int var;
		int c=0;

		int i1=0;
		int i2=0;
		int joueurTrue=0;

		for(i1=0;i1<competitions.size();i1++){
			int taille=competitions.get(i1).getParis().size();
			for(i2=0;i2<taille;i2++){
				if(pseudo.equals(competitions.get(i1).getParis().get(i2).getJoueurReference().getPseudo())){

						joueurTrue=1;
						break;
				}

			}
		}

		if(joueurTrue==1) throw new JoueurException();

		for(int i=0;i<joueurs.size();i=i+1){

			if(joueurs.get(i).getPseudo().equals(pseudo) && joueurs.get(i).getNom().equals(nom) && joueurs.get(i).getPrenom().equals(prenom)){

				c=1;
				var=i;
				joueurs.remove(var);
				break;
			}

		}



		if(c==0) throw new JoueurInexistantException();





		return 0;
	}



	/**
	 * ajouter une compétition.
	 *
	 * @param competition le nom de la compétition
	 * @param dateCloture   la date à partir de laquelle il ne sera plus possible de miser
	 * @param competiteurs   les noms des différents compétiteurs de la compétition
	 * @param passwordGestionnaire  le password du gestionnaire du site
	 *
	 * @throws MetierException levée si le tableau des
	 * compétiteurs n'est pas instancié, si le
	 * <code>passwordGestionnaire</code> est invalide, si le
	 * <code>passwordGestionnaire</code> est incorrect.
	 * @throws CompetitionExistanteException levée si une compétition existe avec le même nom.
	 * @throws CompetitionException levée si le nom de la
	 * compétition ou des compétiteurs sont invalides, si il y a
	 * moins de 2 compétiteurs, si un des competiteurs n'est pas instancié,
	 * si deux compétiteurs ont le même nom, si la date de clôture
	 * n'est pas instanciée ou est dépassée.
	 */
	public void ajouterCompetition(String competition, DateFrancaise dateCloture, String[] competiteurs, String passwordGestionnaire) throws MetierException, CompetitionExistanteException, CompetitionException  {



		validitePasswordGestionnaire(passwordGestionnaire);
		if(!leGestionnaire.getPasswordGestionnaire().equals(passwordGestionnaire)) throw new MetierException();


		if (competiteurs==null) throw new MetierException();


		int v = competiteurs.length;
		if (v==1)  throw new CompetitionException();



		Competition comp =new Competition(competition,dateCloture,competiteurs, v);


		for(int i=0;i<competitions.size();i=i+1){
			if(competitions.get(i).getCompetition().equals(competition)) throw new CompetitionExistanteException();

		}






		competitions.add(comp);





	}


	/**
	 * solder une compétition vainqueur (compétition avec vainqueur).
	 *
	 * Chaque joueur ayant misé sur cette compétition
	 * en choisissant ce compétiteur est crédité d'un nombre de
	 * jetons égal à :
	 *
	 * (le montant de sa mise * la somme des
	 * jetons misés pour cette compétition) / la somme des jetons
	 * misés sur ce compétiteur.
	 *
	 * Si aucun joueur n'a trouvé le
	 * bon compétiteur, des jetons sont crédités aux joueurs ayant
	 * misé sur cette compétition (conformément au montant de
	 * leurs mises). La compétition est "supprimée" si il ne reste
	 * plus de mises suite à ce solde.
	 *
	 * @param competition   le nom de la compétition
	 * @param vainqueur   le nom du vainqueur de la compétition
	 * @param passwordGestionnaire  le password du gestionnaire du site
	 *
	 * @throws MetierException   levée
	 * si le <code>passwordGestionnaire</code>  est invalide,
	 * si le <code>passwordGestionnaire</code> est incorrect.
	 * @throws CompetitionInexistanteException   levée si il n'existe pas de compétition de même nom.
	 * @throws CompetitionException levée
	 * si le nom de la compétition ou du vainqueur est invalide,
	 * si il n'existe pas de compétiteur du nom du vainqueur dans la compétition,
	 * si la date de clôture de la compétition est dans le futur.
	 *
	 */
	public void solderVainqueur(String competition, String vainqueur, String passwordGestionnaire) throws MetierException,  CompetitionInexistanteException, CompetitionException, JoueurException, JoueurInexistantException  {

		validitePasswordGestionnaire(passwordGestionnaire);
		if(!leGestionnaire.getPasswordGestionnaire().equals(passwordGestionnaire)) throw new MetierException();


		int c1 =0;
		int var1=0; //var1 donne la position de l'objet competition de la linkedList competition

		for(int i=0;i<competitions.size();i=i+1){


			if(competitions.get(i).getCompetition().equals(competition)){


				c1 =1;
				var1=i;
				break;

			}

		}


		if(c1 ==0) throw new CompetitionInexistanteException();//si la competition n'existe pas

		if(competitions.get(var1).getDateCloture().estDansLePasse()==false) throw new CompetitionException();//dateDeCloture incorrecte

		int taille = competitions.get(var1).getParis().size(); //nombre de paris pour verifier

		//verifier valeur total des paris faites pour la competition
		long total=0;
		for(int i1 = 0; i1< taille; i1++){
			total=total+competitions.get(var1).getParis().get(i1).getMiseEnJetons();//somme de tout l'argent
		}

		int taille2 = competitions.get(var1).getCompetiteurs().size();//nombre de competiteurs dans la competition
		int c4 =0;

		for(int i2 = 0; i2< taille2; i2=i2+1){

			if(competitions.get(var1).getCompetiteurs().get(i2).getNom().equals(vainqueur)){
				c4 =1;
				break;
			}
		}


		if(c4 ==0) throw new CompetitionException(); //esse if ve se na linked list de competidores existe esse time

		long valeurcorrecte =0; //sauver la valeur correcte
		int c3=0;

		//boucle pour calculer l'argent du vainqueur
		for(int i3 = 0; i3< taille; i3=i3+1){
			if(competitions.get(var1).getParis().get(i3).getCompetiteurReference().getNom().equals(vainqueur)){//ve se a aposta esta certa
				valeurcorrecte = valeurcorrecte +competitions.get(var1).getParis().get(i3).getMiseEnJetons();
				c3=1;
			}
		}
		//if (c3==0) throw new CompetitionException();

		if(valeurcorrecte ==0){
			for(int i = 0; i< taille; i++){

				String nom=competitions.get(var1).getParis().get(i).getJoueurReference().getNom();
				String prenom=competitions.get(var1).getParis().get(i).getJoueurReference().getPrenom();
				String pseudo=competitions.get(var1).getParis().get(i).getJoueurReference().getPseudo();
				long MiseEnJeton=competitions.get(var1).getParis().get(i).getMiseEnJetons();



				crediterJoueur(nom,prenom,pseudo,MiseEnJeton,passwordGestionnaire) ;

			}



		}



		//crediter tous à être credité
		float crediter=0;
		float MiseEnJeton;
		for(int i = 0; i< taille; i++){
			if(competitions.get(var1).getParis().get(i).getCompetiteurReference().getNom().equals(vainqueur)){//ve se a aposta esta certa
				//debiter les autres

				String nom=competitions.get(var1).getParis().get(i).getJoueurReference().getNom();
				String prenom=competitions.get(var1).getParis().get(i).getJoueurReference().getPrenom();
				String pseudo=competitions.get(var1).getParis().get(i).getJoueurReference().getPseudo();
				MiseEnJeton=competitions.get(var1).getParis().get(i).getMiseEnJetons();


				crediter=(MiseEnJeton/ valeurcorrecte)*total;

				long crediter1=(long) crediter;


				crediterJoueur(nom,prenom,pseudo,crediter1,passwordGestionnaire) ;


			}

		}

		competitions.remove(var1);

	}



	/**
	 * créditer le compte en jetons d'un joueur du site de paris.
	 *
	 * @param nom   le nom du joueur
	 * @param prenom   le prénom du joueur
	 * @param pseudo   le pseudo du joueur
	 * @param sommeEnJetons   la somme en jetons à créditer
	 * @param passwordGestionnaire  le password du gestionnaire du site
	 *
	 * @throws MetierException   levée
	 * si le <code>passwordGestionnaire</code>  est invalide,
	 * si le <code>passwordGestionnaire</code> est incorrect,
	 * si la somme en jetons est négative.
	 * @throws JoueurException levée
	 * si <code>nom</code>, <code>prenom</code>,  <code>pseudo</code> sont invalides.
	 * @throws JoueurInexistantException   levée si il n'y a pas de joueur  avec les mêmes nom,  prénom et pseudo.
	 */
	public void crediterJoueur(String nom, String prenom, String pseudo, long sommeEnJetons, String passwordGestionnaire) throws MetierException, JoueurException, JoueurInexistantException {

		validitePasswordGestionnaire(passwordGestionnaire);
		if(!leGestionnaire.getPasswordGestionnaire().equals(passwordGestionnaire)) throw new MetierException();

		validiteNomJoueur(nom);
		validitePrenomJoueur(prenom);
		validitePseudoJoueur(pseudo);

		if(sommeEnJetons<0) throw new MetierException();

		int c1=0;
		int var1=0;
		for(int i=0;i<joueurs.size();i=i+1){

			if(joueurs.get(i).getPseudo().equals(pseudo) && joueurs.get(i).getNom().equals(nom) && joueurs.get(i).getPrenom().equals(prenom)){

				c1=1;
				var1=i;

			}

		}

		if(c1==0) throw new JoueurInexistantException();


		joueurs.get(var1).setSommeEnJetons(joueurs.get(var1).getSommeEnJetons()+sommeEnJetons);





	}


	/**
	 * débiter le compte en jetons d'un joueur du site de paris.
	 *
	 * @param nom   le nom du joueur
	 * @param prenom   le prénom du joueur
	 * @param pseudo   le pseudo du joueur
	 * @param sommeEnJetons   la somme en jetons à débiter
	 * @param passwordGestionnaire  le password du gestionnaire du site
	 *
	 * @throws MetierException   levée
	 * si le <code>passwordGestionnaire</code>  est invalide,
	 * si le <code>passwordGestionnaire</code> est incorrect,
	 * si la somme en jetons est négative.
	 * @throws JoueurException levée
	 * si <code>nom</code>, <code>prenom</code>,  <code>pseudo</code> sont invalides
	 * si le compte en jetons du joueur est insuffisant (il deviendrait négatif).
	 * @throws JoueurInexistantException   levée si il n'y a pas de joueur  avec les mêmes nom,  prénom et pseudo.
	 *
	 */

	public void debiterJoueur(String nom, String prenom, String pseudo, long sommeEnJetons, String passwordGestionnaire) throws  MetierException, JoueurInexistantException, JoueurException {

		validitePasswordGestionnaire(passwordGestionnaire);
		if(!leGestionnaire.getPasswordGestionnaire().equals(passwordGestionnaire)) throw new MetierException();

		validiteNomJoueur(nom);
		validitePrenomJoueur(prenom);
		validitePseudoJoueur(pseudo);

		if(sommeEnJetons<0) throw new MetierException();

		int c2 =0;
		int var2=0;
		for(int i=0;i<joueurs.size();i=i+1){

			if(joueurs.get(i).getPseudo().equals(pseudo) && joueurs.get(i).getNom().equals(nom) && joueurs.get(i).getPrenom().equals(prenom)){

				c2 =1;
				var2=i;

			}

		}

		if(c2 ==0) throw new JoueurInexistantException();




		if (joueurs.get(var2).getSommeEnJetons()<sommeEnJetons) throw new JoueurException();



		joueurs.get(var2).setSommeEnJetons(joueurs.get(var2).getSommeEnJetons()-sommeEnJetons);


	}



	/**
	 * consulter les  joueurs.
	 *
	 * @param passwordGestionnaire  le password du gestionnaire du site de paris

	 * @throws MetierException   levée
	 * si le <code>passwordGestionnaire</code>  est invalide,
	 * si le <code>passwordGestionnaire</code> est incorrect.
	 *
	 * @return une liste de liste dont les éléments (de type <code>String</code>) représentent un joueur avec dans l'ordre :
	 *  <ul>
	 *  <li>       le nom du joueur  </li>
	 *  <li>       le prénom du joueur </li>
	 *  <li>       le pseudo du joueur  </li>
	 *  <li>       son compte en jetons restant disponibles </li>
	 *  <li>       le total de jetons engagés dans ses mises en cours. </li>
	 *  </ul>
	 */
	public LinkedList <LinkedList <String>> consulterJoueurs(String passwordGestionnaire) throws MetierException {

		//valider  password gestionnaire
		validitePasswordGestionnaire(passwordGestionnaire);
		if(!leGestionnaire.getPasswordGestionnaire().equals(passwordGestionnaire)) throw new MetierException();

		LinkedList<LinkedList<String>> linkJoueur; //declaration
		linkJoueur=new LinkedList<LinkedList<String>>(); //initialisation

		LinkedList<String>[] attributs;
		int tailleLinked =joueurs.size();
		attributs =new LinkedList[tailleLinked];

		int somme =0;

		for(int i1 = 0; i1< tailleLinked; i1++){

			attributs[i1]=new LinkedList<String>();
			attributs[i1].add(joueurs.get(i1).getNom());
			attributs[i1].add(joueurs.get(i1).getPrenom());
			attributs[i1].add(joueurs.get(i1).getPseudo());
			attributs[i1].add(String.valueOf(joueurs.get(i1).getSommeEnJetons()));



			//cherche dans tous les competitions
			int taille2 =competitions.size();
			for(int i2 = 0; i2< taille2; i2++){


				//tous les paris d'une competition
				int taille3 =competitions.get(i2).getParis().size();


				for(int i3 = 0; i3< taille3; i3++){

					if(competitions.get(i2).getParis().get(i3).getJoueurReference().getPseudo().equals(joueurs.get(i1).getPseudo())){

						somme = somme +(int)competitions.get(i2).getParis().get(i3).getMiseEnJetons();

					}

				}

			}

			attributs[i1].add(String.valueOf(somme));
			linkJoueur.add(attributs[i1]);
			somme =0;

		}



		return linkJoueur;
	}








	// Les méthodes avec mot de passe utilisateur



	/**
	 * miserVainqueur  (parier sur une compétition, en désignant un vainqueur).
	 * Le compte du joueur est débité du nombre de jetons misés.
	 *
	 * @param pseudo   le pseudo du joueur
	 * @param passwordJoueur  le password du joueur
	 * @param miseEnJetons   la somme en jetons à miser
	 * @param competition   le nom de la compétition  relative au pari effectué
	 * @param vainqueurEnvisage   le nom du compétiteur  sur lequel le joueur mise comme étant le  vainqueur de la compétition
	 *
	 * @throws MetierException levée si la somme en jetons est négative.
	 * @throws JoueurInexistantException levée si il n'y a pas de
	 * joueur avec les mêmes pseudos et password.
	 * @throws CompetitionInexistanteException   levée si il n'existe pas de compétition de même nom.
	 * @throws CompetitionException levée
	 * si <code>competition</code> ou <code>vainqueurEnvisage</code> sont invalides,
	 * si il n'existe pas un compétiteur de  nom <code>vainqueurEnvisage</code> dans la compétition,
	 * si la compétition n'est plus ouverte (la date de clôture est dans le passé).
	 * @throws JoueurException   levée
	 * si <code>pseudo</code> ou <code>password</code> sont invalides,
	 * si le <code>compteEnJetons</code> du joueur est insuffisant (il deviendrait négatif).
	 */
	public void miserVainqueur(String pseudo, String passwordJoueur, long miseEnJetons, String competition, String vainqueurEnvisage) throws MetierException, JoueurInexistantException, CompetitionInexistanteException, CompetitionException, JoueurException  {


		if (pseudo.equals("")) throw new JoueurException();
		//if (passwordJoueur.equals("")) throw new JoueurException();
		validitePasswordJoueur(passwordJoueur);

		if (miseEnJetons<=0) throw new MetierException();

		int c1=0;
		int var1=0; //var1 donne la position de l'objet joueur de la linkedList joueurs
		for(int i=0;i<joueurs.size();i=i+1){

			if(joueurs.get(i).getPseudo().equals(pseudo)){

				c1=1;
				var1=i;

				//////////////////////////////////////
				//String nom=joueurs.get(i).getNom();
				//String prenom=joueurs.get(i).getPrenom();
				//////////////////////////////////////
			}

		}

		if (c1==0) throw new JoueurInexistantException();






		int c3=0;
		int var3=0; //var3 donne la position de l'objet joueur de la linkedList competition

		for(int i=0;i<competitions.size();i=i+1){

			if(competitions.get(i).getCompetition().equals(competition)){

				c3=1;
				var3=i;

			}

		}

		if(c3==0) throw new CompetitionInexistanteException();

		if (competitions.get(var3).getDateCloture().estDansLePasse()==true) throw new CompetitionException();


		int c2=0;
		int var2=0; ////var3 donne la position de l'objet joueur de la linkedList competiteurs sauvegardé dans competiition
		for(int i=0;i<competitions.get(var3).getCompetiteurs().size();i=i+1){

			if(competitions.get(var3).getCompetiteurs().get(i).getNom().equals(vainqueurEnvisage)){

				c2=1;
				var2=i;

			}

		}

		if(c2==0) throw new CompetitionException();



		//recevoir des arguments, créer un objet pari et sauvegarder dans la linkedList de l'objet competition correcte

		//avant de commencer, il faut savoir qui est l'objet joueur, donc il est cherché dans la linkedList



		//Ajouter pari à linkedList de paris

		if (joueurs.get(var1).getSommeEnJetons()<miseEnJetons) throw new JoueurException();

		Pari pari= new Pari(miseEnJetons,joueurs.get(var1) ,competitions.get(var3).getCompetiteurs().get(var2) );


		competitions.get(var3).getParis().add(pari);

		joueurs.get(var1).setSommeEnJetons(joueurs.get(var1).getSommeEnJetons()-miseEnJetons);

		//System.out.println(" "+joueurs.get(var1).getPseudo()+" apostou "+ miseEnJetons+" na competição" +competitions.get(var3).getCompetition());


	}

	public LinkedList <LinkedList <String>> consulterCompetitions(){


		LinkedList<LinkedList<String>> competitions2;

		competitions2=new LinkedList<LinkedList<String>> ();


		LinkedList<String>[] info1;
		info1 = new LinkedList[competitions.size()];






		for (int i=0;i<competitions.size();i++){

			info1[i]=new LinkedList<String>();
			info1[i].add(competitions.get(i).getCompetition());
			info1[i].add(competitions.get(i).getDateCloture().toString());
			competitions2.add(info1[i]);
		}

		return competitions2;
	}








	public LinkedList <String> consulterCompetiteurs(String competition) throws CompetitionException, CompetitionInexistanteException{


		LinkedList<String> competConsult1;
		competConsult1=new LinkedList<String>();

		validiteCompetition(competition);
		int verif =0;
		int var=0;
		int taille =competitions.size();
		for(int i = 0; i< taille; i++){

			if(competitions.get(i).getCompetition().equals(competition)){
				verif =1;
				var=i;
			}

		}

		if(verif ==0) throw new CompetitionInexistanteException();


		int taille2 =competitions.get(var).getCompetiteurs().size();
		for(int i1 = 0; i1< taille2; i1++){

			competConsult1.add(competitions.get(var).getCompetiteurs().get(i1).getNom().toString());


		}


		return competConsult1;
	}


	protected void validitePasswordGestionnaire(String passwordGestionnaire) throws MetierException {
		if (passwordGestionnaire==null) throw new MetierException();
		if (!passwordGestionnaire.matches("[0-9A-Za-z]{8,}")) throw new MetierException();
	}

	protected void validitePasswordJoueur (String passwordJoueur) throws JoueurException {
		int var = -1;
		if (passwordJoueur.equals("")) throw new JoueurException();
		if (passwordJoueur==null ) throw new JoueurException();
		if (!passwordJoueur.matches("[0-9A-Za-z]{8,}")) throw new JoueurException();
		for(int i=0;i<joueurs.size();i=i+1){

			if(joueurs.get(i).getPasswordJouer().equals(passwordJoueur)){

				var=i;

			}

		}
		if(var==-1) throw new JoueurException();
	}






	protected void validiteCompetition(String competition )throws CompetitionException{

		if (competition==null) throw new CompetitionException();
		if (!competition.matches("[0-9A-Za-z]{4,}")) throw new CompetitionException();


	}
	public void validiteNomJoueur(String nom) throws JoueurException {
		if (nom==null) throw new JoueurException();
		if (!nom.matches("[-A-Za-z]{1,}")) throw new JoueurException();
	}
	public void validitePrenomJoueur(String prenom) throws JoueurException {
		if (prenom==null) throw new JoueurException();
		if (!prenom.matches("[-A-Za-z]{1,}")) throw new JoueurException();
	}
	public void validitePseudoJoueur(String Pseudo) throws JoueurException {
		if (Pseudo==null) throw new JoueurException();
		if (!Pseudo.matches("[0-9A-Za-z]{4,}")) throw new JoueurException();
	}


}


