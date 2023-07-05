public class Noeud {
    /**
     * @author Camille Protat
     * 
    **/
    private char contenu;
    private Noeud gauche;
    private Noeud droite;

    public Noeud(){
        contenu=' ';
        gauche=droite=null;
    }
    
    public Noeud(char e){
        contenu = e;
        gauche = droite = null;
    }

    public char getContenu() {
        return contenu;
    }
    public void setContenu(char contenu) {
        this.contenu = contenu;
    }

    public void setDroite(Noeud droite) {
        this.droite = droite;
    }
    public void setGauche(Noeud gauche) {
        this.gauche = gauche;
    }

    public Noeud getDroite() {
        return droite;
    }
    public Noeud getGauche() {
        return gauche;
    }

    public String toString(){
        String s = String.valueOf(contenu);
        return s;
    }
    public boolean estFeuille(){
        if (gauche == null && droite == null) return true;
        return false;
    }

    public boolean estNeg(){
        if(contenu == '¬') return true;
        return false;
    }

    public boolean estOu(){
        if(contenu == '∨') return true;
        return false;
    }
    public boolean estEt(){
        if(contenu == '∧') return true;
        return false;
    }

    public boolean estNull(){
        if(contenu == ' ') return true;
        return false;
    }


}
