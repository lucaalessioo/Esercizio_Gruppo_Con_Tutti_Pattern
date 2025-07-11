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
 
@Override
public void getUser() {
    System.out.println("Utente Premium");

}

@Override
public void getTipo() {
    System.out.println("Tipo: Premium");
    
}

}
//concreta classe normal user
class NormalUser implements User
{

    @Override
    public void getUser() {
        System.out.println("Utente Normale");
  
    }

    @Override
    public void getTipo() {
        System.out.println("Tipo:nomale ");
        
    }


}
//classe concreta Factory
class ConcreteUser implements UserFactory {
    @Override
    public User creaUser(String tipo) {
        if (tipo.equalsIgnoreCase("premium")) {
            return new PremiumUser();
        } else if (tipo.equalsIgnoreCase("normale")) { // Aggiunta la condizione per "normale"
            return new NormalUser();
        } else {
            System.out.println("Tipo utente non riconosciuto. Creazione di un utente normale di default.");
            return new NormalUser(); // Gestione di tipi non validi
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
        this.userFactory = new ConcreteUser(); // Inizializza la factory qui
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

class BaseComment implements Comment
{
private String text;
public BaseComment(String text)
{
    this.text=text;
}
@Override
    public String getText() {
        return text;
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



