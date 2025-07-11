
    

import java.util.ArrayList;
import java.util.List;

public class TestPattern {
    
}

interface User
{
    void getUser();
    void getTipo();

}
//factory
interface UserFactory
{
    User creaUser(String type);
}
//concret user premium
class PremiumUser implements User
{
    private String username;
    PremiumUser(String username)
    {
        this.username=username;
    }
 
@Override
public void getUser() {
    System.out.println("Utente Premium");

}

@Override
public void getTipo() {
    System.out.println("Tipo: Premium");
    
}

@Override
public String toString() {
    return username;
}



}
//concreta classe normal user
class NormalUser implements User
{
private String username;
NormalUser(String username)
{
    this.username=username;
}

    @Override
    public void getUser() {
        System.out.println("Utente Normale");
  
    }

    @Override
    public void getTipo() {
        System.out.println("Tipo:nomale ");
        
    }

    @Override
    public String toString() {
        return username;
    }


}
//classe concreta Factory
class ConcreteUserFactory implements UserFactory {
    @Override
    public User creaUser(String tipo) {
        String defaultUsername = "utenteSconosciuto"; // O un nome utente dinamico dal contesto

        if (tipo.equalsIgnoreCase("premium")) {
            return new PremiumUser(defaultUsername);
        } else if (tipo.equalsIgnoreCase("normale")) {
            return new NormalUser(defaultUsername);
        } else {
            System.out.println("Tipo utente non riconosciuto. Creazione di un utente normale di default.");
            return new NormalUser(defaultUsername);
        }
    }
}


//singleton
class AutenticatoreSingleton
{
    
  
    
private static AutenticatoreSingleton istanza;
    private User currentUser; // Aggiunto per tenere traccia dell'utente loggato
    private UserFactory userFactory; // Aggiunto per usare la factory

    // Costruttore privato
    private AutenticatoreSingleton() {
        this.userFactory = new ConcreteUserFactory(); // Inizializza la factory qui
    }

    // Metodo per ottenere l'unica istanza
    public static AutenticatoreSingleton getIstanza() {
        if (istanza == null) {
            istanza = new AutenticatoreSingleton();
        }
        return istanza;
    }

    // Metodo login (base)
    public void login(String username, String password) {
        // In un'applicazione reale, qui ci sarebbe una verifica con un DB o un servizio esterno
        System.out.println("Login effettuato per: " + username);
        // Per semplicità, ipotizziamo un login di successo e creiamo un utente normale
        this.currentUser = userFactory.creaUser("normale"); 
        System.out.print("Utente loggato: ");
        currentUser.getUser();
    }

    // Metodo registra (utilizza la Factory)
    public void registra(String username, String password, String userType) { // Aggiunto userType
        System.out.println("Tentativo di registrazione per: " + username + " come " + userType + "...");
        User newUser = userFactory.creaUser(userType);
        System.out.print("Nuovo utente creato: ");
        newUser.getUser();
        newUser.getTipo();
        // In un'applicazione reale, qui l'utente verrebbe salvato in un DB
        System.out.println("Registrazione completata per " + username);
    }

    // Getter per l'utente corrente
    public User getCurrentUser() {
        return currentUser;
    }
    // Logout
    public void logout() {
        if (currentUser != null) {
            System.out.println("Logout di " + currentUser.getClass().getSimpleName() + " effettuato.");
            this.currentUser = null;
        } else {
            System.out.println("Nessun utente loggato.");
        }
    }
}



//decorator
interface Comment
{
String getText();

}

class SocialComment implements Comment { // Rinominata da BaseComment a SocialComment per chiarezza nel dominio
    private String text;
    private User author;
    private String postId; // A quale post appartiene il commento

    public SocialComment(String text, User author, String postId) {
        this.text = text;
        this.author = author;
        this.postId = postId;
    }

    @Override
    public String getText() {
        return text;
    }

    public User getauthor() {
         return author; 
        }
    public String getPostId() {
         return postId; 
        }

    @Override
    public String toString() {
        return "Commento [Autore: " + author.getClass().getSimpleName() + ", Testo: '" + getText() + "']";
    }
}

 abstract class CommentDecorator implements Comment
 {
    protected Comment decoratedComment; // Protetti per accesso dai sottoclassi

    public CommentDecorator(Comment comment) {
        this.decoratedComment = comment;
    }

    // Il decoratore delega il metodo di base al componente incapsulato
    @Override
    public String getText() {
        return decoratedComment.getText();
    }


 }

//classe concreta decoratore
 class DecoratoreConcreto extends CommentDecorator
 {
    // costruttore
    public DecoratoreConcreto (Comment comment) {
        super(comment);
    }
    @Override
    public String getText() {
        // Aggiunge la funzionalità di "modifica" al testo esistente
        return super.getText() + " (Modificato)";
    }
 }
 class Post {
    private String contenuto;
    private List<Comment> commenti = new ArrayList<>();
    private String foto=null;

    public Post(String contenuto) {
        this.contenuto = contenuto;
    }

    public void aggiungiCommento(Comment commento) {
        commenti.add(commento);
        System.out.println("Commento aggiunto: " + commento.getText());
    }

    public void rimuoviCommento(int indice) {
        if (indice >= 0 && indice < commenti.size()) {
            commenti.remove(indice);
            System.out.println("Commento rimosso.");
        } else {
            System.out.println("Indice non valido.");
        }
    }

    public void mostraCommenti() {
        System.out.println("Commenti sul post:");
        for (Comment c : commenti) {
            System.out.println("- " + c.getText());
        }
    }

    public String getContenuto() {
        return contenuto;
    }
    public void setFoto(String foto) {
        this.foto = foto;
    }
    public String getFoto() {
        return foto;
    }
}
// Decoratore per aggiungere emoticon
class EmoticonDecorator extends CommentDecorator {
    private String emoticon;

    public EmoticonDecorator(Comment comment, String emoticon) {
        super(comment);
        this.emoticon = emoticon;
    }

    @Override
    public String getText() {
        return decoratedComment.getText() + " " + emoticon;
    }
}


