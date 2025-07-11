
import java.util.ArrayList;
import java.util.List;

// Product del Factory
interface User {
    void getUser();
    void getTipo();
    String getUsername();
}

// Classe concreta che implementa il product
class PremiumUser implements User {
    private String username;                                         // identificatore per utente premium

    public PremiumUser(String username) {                            // Costruttore 
        this.username = username;
    }
    
    // Getter 
    @Override
    public void getUser() {
        System.out.println("Utente Premium: " + username);
    }

    @Override
    public void getTipo() {
        System.out.println("Tipo: Premium");
    }

    @Override
    public String getUsername() {
        return username;
    }

    // toString
    @Override
    public String toString() {
        return "PremiumUser(" + username + ")";
    }
}

// Classe concreta che implementa il product
class NormalUser implements User {
    private String username;                                           // Identificatore utente normale

    public NormalUser(String username) {
        this.username = username;
    }

    // Getter
    @Override
    public void getUser() {
        System.out.println("Utente Normale: " + username);
    }

    @Override
    public void getTipo() {
        System.out.println("Tipo: Normale");
    }

    @Override
    public String getUsername() {
        return username;
    }

    // toString
    @Override
    public String toString() {
        return "NormalUser(" + username + ")";
    }
}

// Fabbrica di utenti
interface UserFactory {
    User creaUser(String type, String username);  // Metodo Factory in base al tipo crea l oggetto
}

// Qui andiamo a controllare effettivamente se un utente deve essere creato come premium o come normale
class ConcreteUser implements UserFactory {

    @Override
    public User creaUser(String tipo, String username) {
        if (tipo.equalsIgnoreCase("premium")) {    // Se scriviamo premium 
            return new PremiumUser(username);                    // Crea ogetto premium e lo restituisce
        } else if (tipo.equalsIgnoreCase("normale")) { // Se si scrive normale
            return new NormalUser(username);                        // Crea e restituisce l oggetto normale
        } else {
            System.out.println("Tipo utente non riconosciuto. Creazione di un utente normale di default.");   // Altrimenti tipo non riconoscito e ne ritorniamo uno normale
            return new NormalUser(username);
        }
    }
}

// Abbiamo creato una classe di autenticazione di accesso utilizzando il pattern singleton
class AutenticatoreSingleton {
    private static AutenticatoreSingleton istanza;     // Unica instanza possibile
    private User currentUser;                          // Oggetto di tipo user da autenticare
    private UserFactory userFactory;                   // Oggetto dell interfaccia UserFactory per creare l utente in base al tipo

    private AutenticatoreSingleton() {                 // Costruttore privato per limitare la creazione
        this.userFactory = new ConcreteUser();
    }

    public static AutenticatoreSingleton getIstanza() {// Metodo public per ritornare solo una instanza
        if (istanza == null) {
            istanza = new AutenticatoreSingleton();
        }
        return istanza;
    }

    // Metodo per il login  si verificano le credenziali e si crea l oggetto normale in maniera hardcoding
    public User login(String username, String password) {                                  //  Da aggiungere il tipo per confrontarlo nel metodo creaUser(incompleto)
        System.out.println("Login effettuato per: " + username);
        this.currentUser = userFactory.creaUser("normale", username); 
        System.out.print("Utente loggato: ");
        currentUser.getUser();
        return currentUser;             // Restituisce l utente creato
    }


    // Metodo per la registrazione dove passiamo anche il tipo
    public User registra(String username, String password, String userType) {
        System.out.println("Tentativo di registrazione per: " + username + " come " + userType + "...");
        User newUser = userFactory.creaUser(userType, username);        // Metodo factory per la creazione specifica
        System.out.print("Nuovo utente creato: ");
        newUser.getUser();
        newUser.getTipo();
        System.out.println("Registrazione completata per " + username);
        return newUser;                                                 // Ritorna l utente creato
    }

    public User getCurrentUser() {                                      // Ritorna un oggetto di tipo User
        return currentUser;
    }

    // Metodo per uscire 
    public void logout() {
        if (currentUser != null) {
            System.out.println("Logout di " + currentUser.getUsername() + " effettuato.");
            this.currentUser = null;
        } else {
            System.out.println("Nessun utente loggato.");
        }
    }
}

// Inizio Facade ------------------------------------------------------------------------------------------------------------------------------------------------------

// Rappresenta il post sul social
class Post {
    // Caratteristiche del post
    private String id;
    private User author;
    private String content;

    // Costruttore
    public Post(String id, User author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
    }

    // Getter
    public String getId() {
         return id; 
        }
    public User getAuthor() {
         return author;
         }
    public String getContent() { 
        return content; 
    }

    // toString
    @Override
    public String toString() {
        return "Post [ID: " + id + ", Autore: " + author.getUsername() + ", Contenuto: '" + content + "']";
    }
}

// Rappresenta il commento del post
interface Comment {

    // Metodi per le info sul post
    String getText();
    User getAuthor();
    String getPostId();
}

// Rappresenta il commento effetuato dall utente
class SocialComment implements Comment {

    // Variabili d istanza
    private String text;
    private User author;
    private String postId;

    public SocialComment(String text, User author, String postId) {     // Creazione del commento con testo autore del commento e l id del post per riferimento
        this.text = text;
        this.author = author;
        this.postId = postId;
    }

    @Override
    public String getText() {
         return text; 
        }
    @Override
    public User getAuthor() {
         return author; 
        }
    @Override
    public String getPostId() {
         return postId; 
        }

    @Override
    public String toString() {
        return "Commento [Autore: " + author.getUsername() + ", Testo: '" + getText() + "']";
    }
}

// Qui inizia la parte sul decorator per modificare un commento
abstract class CommentDecorator implements Comment {
    protected Comment decoratedComment;                         // Variabile per modificare il commento

    public CommentDecorator(Comment comment) {                  // Costruttore del decoratore con all interno l istanza del commento da modificare
        this.decoratedComment = comment;
    }

    // Override dei metodi cambiando il testo l autore e l id del post che si vuole commentare (Applicativo del decorator)
    @Override
    public String getText() {
         return decoratedComment.getText(); 
        }
    @Override
    public User getAuthor() {
         return decoratedComment.getAuthor(); 
        }
    @Override
    public String getPostId() {
         return decoratedComment.getPostId(); 
        }
}

// Classe concreta del decoratore
class DecoratoreConcreto extends CommentDecorator {

    public DecoratoreConcreto(Comment comment) {            // Costruttore con all interno il commento che richiama il costruttore della classe astratta sopra
        super(comment);
    }

    // toString
    @Override
    public String getText() {
        return super.getText() + " ( Commento Modificato)";  // Abbiamo decorato il testo del commento
    }
}

// Possibile implementazione controllo commento (Parolacce)

/*class AdminApprovedCommentDecorator extends CommentDecorator {
    public AdminApprovedCommentDecorator(Comment comment) {
        super(comment);
    }

    @Override
    public String getText() {
        return "[APPROVATO ADMIN] " + super.getText();
    }
}
    */

// Classe concreta del facade 
class SocialMediaFacade {
    AutenticatoreSingleton authManager;      // Singleton 
    private List<Post> posts;                // Lista di post           

    // Costruttore dove andiamo ad prendere l instanza dell auteticatore, instanziamo l array list dei post 
    public SocialMediaFacade() {
        this.authManager = AutenticatoreSingleton.getIstanza();
        this.posts = new ArrayList<>();
        
    }

    // Questo metodo serve per registrare un utente
    public User registerUser(String username, String password, String userType) {
        System.out.println("\nRichiesta Registrazione Utente");
        return authManager.registra(username, password, userType);
    }

    // Metodo per il login
    public User loginUser(String username, String password) {
        System.out.println("\nRichiesta Login Utente");
        return authManager.login(username, password);
    }

    // Metodo per creare un nuovo post
    public Post createNewPost(User author, String content) {
        System.out.println("\nRichiesta Creazione Post");
        String postId = "post_" + (posts.size() + 1);      // Prendo il nuovo id del post aggiunto
        Post newPost = new Post(postId, author, content);  // Crea l oggetto post con i vari attributi
        posts.add(newPost);   // Aggiunge il post alla lista
        System.out.println(" Post creato da " + author.getUsername() + ": " + newPost.getContent());
        return newPost;  // Ritorna il post creato
    }

    // Metodo per eliminare in base all id 
    public void deleteExistingPost(String postId) {
        System.out.println("\nRichiesta Eliminazione Post");
        Post postToRemove = null;
        for (Post p : posts) {                             // Se nella lista dei post troviamo l id inserito nel metodo lo eliminiamo
            if (p.getId().equals(postId)) {
                postToRemove = p;
                break;
            }
        }

        // Se il post è stato trovato lo elimiamo altrimenti stampiamo non trovato
        if (postToRemove != null) {
            posts.remove(postToRemove);
            System.out.println("Post con ID " + postId + " eliminato.");
        } else {
            System.out.println("Post con ID " + postId + " non trovato per l'eliminazione.");
        }
    }

    // Metodo per aggiungere un commento al post e lo ritorna indietro
    public Comment addCommentToPost(String postId, User author, String text) {
        System.out.println("\nRichiesta Aggiunta Commento");
        Comment newComment = new SocialComment(text, author, postId);
        System.out.println(" Commento aggiunto al post " + postId + " da " + author.getUsername() + ": '" + newComment.getText() + "'");
        return newComment;
    }

    // Metodo per modificare un commento nel post
    public Comment modifyComment(Comment originalComment) {
        System.out.println("\nRichiesta Modifica Commento");
        Comment modifiedComment = new DecoratoreConcreto(originalComment);
        System.out.println("Commento modificato: '" + originalComment.getText() + "'  '" + modifiedComment.getText() + "'");
        return modifiedComment;
    }
     
    // Metodo per visualizzare tutti i post presenti
    public void viewAllPosts() {
        System.out.println("\nVisualizzazione Tutti i Post");
        if (posts.isEmpty()) {
            System.out.println("Nessun post disponibile al momento.");
        } else {
            System.out.println("Post disponibili:");
            for (Post p : posts) {
                System.out.println("    - " + p.toString());
            }
        }
    }

    // Applicativo del Facade Tramite questo metodo andiamo ad attivare tutti i metodi creati per la gestione del social
    public void performFullSocialWorkflow(String username, String password, String userType, String postContent, String commentText) {
        System.out.println("\nAvvio Workflow Social Completo (Registrazione, Login, Post, Commento, Modifica)");
        
        User newUser = registerUser(username, password, userType);
        User loggedInUser = loginUser(username, password);

        if (loggedInUser != null) {
            Post myPost = createNewPost(loggedInUser, postContent);

            if (myPost != null) {
                Comment originalComment = addCommentToPost(myPost.getId(), loggedInUser, commentText);
                Comment decoratedComment = modifyComment(originalComment);
                System.out.println("  [Facade]: Commento finale (decorato): " + decoratedComment.getText());
            }
            
            viewAllPosts();
            
            authManager.logout(); 
        } else {
            System.out.println(" non completato a causa del fallimento del login.");
        }
        System.out.println("Facade: Fine Workflow Social Completo");
    }
}

// Main veloce veloce 
public class Application {
    public static void main(String[] args) {
        SocialMediaFacade socialFacade = new SocialMediaFacade();

        socialFacade.performFullSocialWorkflow( "MarioRossi", "password123", "PREMIUM",  "Questo è il mio primo post sul nuovo social!", "Ottimo post Mario! Benvenuto!");

        
        User user1 = socialFacade.loginUser("MarioRossi", "password123");
        if (user1 != null) {
            socialFacade.createNewPost(user1, "Un secondo post interessante!");
            
            socialFacade.viewAllPosts();

            Comment testComment = socialFacade.addCommentToPost("post_1", user1, "Commento da modificare.");
            socialFacade.modifyComment(testComment);
            
            socialFacade.authManager.logout(); 
        }
    }
}