
import java.util.ArrayList;
import java.util.List;

interface User {
    void getUser();
    void getTipo();
    String getUsername();
}

class PremiumUser implements User {
    private String username;

    public PremiumUser(String username) {
        this.username = username;
    }

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

    @Override
    public String toString() {
        return "PremiumUser(" + username + ")";
    }
}

class NormalUser implements User {
    private String username;

    public NormalUser(String username) {
        this.username = username;
    }

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

    @Override
    public String toString() {
        return "NormalUser(" + username + ")";
    }
}

interface UserFactory {
    User creaUser(String type, String username);
}

class ConcreteUser implements UserFactory {
    @Override
    public User creaUser(String tipo, String username) {
        if (tipo.equalsIgnoreCase("premium")) {
            return new PremiumUser(username);
        } else if (tipo.equalsIgnoreCase("normale")) {
            return new NormalUser(username);
        } else {
            System.out.println("Tipo utente non riconosciuto. Creazione di un utente normale di default.");
            return new NormalUser(username);
        }
    }
}

class AutenticatoreSingleton {
    private static AutenticatoreSingleton istanza;
    private User currentUser;
    private UserFactory userFactory;

    private AutenticatoreSingleton() {
        this.userFactory = new ConcreteUser();
    }

    public static AutenticatoreSingleton getIstanza() {
        if (istanza == null) {
            istanza = new AutenticatoreSingleton();
        }
        return istanza;
    }

    public User login(String username, String password) {
        System.out.println("Login effettuato per: " + username);
        this.currentUser = userFactory.creaUser("normale", username); 
        System.out.print("Utente loggato: ");
        currentUser.getUser();
        return currentUser;
    }

    public User registra(String username, String password, String userType) {
        System.out.println("Tentativo di registrazione per: " + username + " come " + userType + "...");
        User newUser = userFactory.creaUser(userType, username);
        System.out.print("Nuovo utente creato: ");
        newUser.getUser();
        newUser.getTipo();
        System.out.println("Registrazione completata per " + username);
        return newUser;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        if (currentUser != null) {
            System.out.println("Logout di " + currentUser.getUsername() + " effettuato.");
            this.currentUser = null;
        } else {
            System.out.println("Nessun utente loggato.");
        }
    }
}

class Post {
    private String id;
    private User author;
    private String content;

    public Post(String id, User author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
    }

    public String getId() { return id; }
    public User getAuthor() { return author; }
    public String getContent() { return content; }

    @Override
    public String toString() {
        return "Post [ID: " + id + ", Autore: " + author.getUsername() + ", Contenuto: '" + content + "']";
    }
}

interface Comment {
    String getText();
    User getAuthor();
    String getPostId();
}

class SocialComment implements Comment {
    private String text;
    private User author;
    private String postId;

    public SocialComment(String text, User author, String postId) {
        this.text = text;
        this.author = author;
        this.postId = postId;
    }

    @Override
    public String getText() { return text; }
    @Override
    public User getAuthor() { return author; }
    @Override
    public String getPostId() { return postId; }

    @Override
    public String toString() {
        return "Commento [Autore: " + author.getUsername() + ", Testo: '" + getText() + "']";
    }
}

abstract class CommentDecorator implements Comment {
    protected Comment decoratedComment;

    public CommentDecorator(Comment comment) {
        this.decoratedComment = comment;
    }

    @Override
    public String getText() { return decoratedComment.getText(); }
    @Override
    public User getAuthor() { return decoratedComment.getAuthor(); }
    @Override
    public String getPostId() { return decoratedComment.getPostId(); }
}

class DecoratoreConcreto extends CommentDecorator {
    public DecoratoreConcreto(Comment comment) {
        super(comment);
    }

    @Override
    public String getText() {
        return super.getText() + " (Modificato)";
    }
}

class AdminApprovedCommentDecorator extends CommentDecorator {
    public AdminApprovedCommentDecorator(Comment comment) {
        super(comment);
    }

    @Override
    public String getText() {
        return "[APPROVATO ADMIN] " + super.getText();
    }
}

class SocialMediaFacade {
    AutenticatoreSingleton authManager;
    private List<Post> posts;

    public SocialMediaFacade() {
        this.authManager = AutenticatoreSingleton.getIstanza();
        this.posts = new ArrayList<>();
        System.out.println("  [Facade]: SocialMediaFacade inizializzato.");
    }

    public User registerUser(String username, String password, String userType) {
        System.out.println("\n[Facade]: Richiesta Registrazione Utente");
        return authManager.registra(username, password, userType);
    }

    public User loginUser(String username, String password) {
        System.out.println("\n[Facade]: Richiesta Login Utente");
        return authManager.login(username, password);
    }

    public Post createNewPost(User author, String content) {
        System.out.println("\n[Facade]: Richiesta Creazione Post");
        String postId = "post_" + (posts.size() + 1);
        Post newPost = new Post(postId, author, content);
        posts.add(newPost);
        System.out.println("  [Facade]: Post creato da " + author.getUsername() + ": " + newPost.getContent());
        return newPost;
    }

    public void deleteExistingPost(String postId) {
        System.out.println("\n[Facade]: Richiesta Eliminazione Post");
        Post postToRemove = null;
        for (Post p : posts) {
            if (p.getId().equals(postId)) {
                postToRemove = p;
                break;
            }
        }
        if (postToRemove != null) {
            posts.remove(postToRemove);
            System.out.println("  [Facade]: Post con ID " + postId + " eliminato.");
        } else {
            System.out.println("  [Facade]: Post con ID " + postId + " non trovato per l'eliminazione.");
        }
    }

    public Comment addCommentToPost(String postId, User author, String text) {
        System.out.println("\n[Facade]: Richiesta Aggiunta Commento");
        Comment newComment = new SocialComment(text, author, postId);
        System.out.println("  [Facade]: Commento aggiunto al post " + postId + " da " + author.getUsername() + ": '" + newComment.getText() + "'");
        return newComment;
    }

    public Comment modifyComment(Comment originalComment) {
        System.out.println("\n[Facade]: Richiesta Modifica Commento");
        Comment modifiedComment = new DecoratoreConcreto(originalComment);
        System.out.println("  [Facade]: Commento modificato: '" + originalComment.getText() + "'  '" + modifiedComment.getText() + "'");
        return modifiedComment;
    }
    
    public void viewAllPosts() {
        System.out.println("\n[Facade]: Visualizzazione Tutti i Post");
        if (posts.isEmpty()) {
            System.out.println("  Nessun post disponibile al momento.");
        } else {
            System.out.println("  Post disponibili:");
            for (Post p : posts) {
                System.out.println("    - " + p.toString());
            }
        }
    }

    public void performFullSocialWorkflow(String username, String password, String userType, String postContent, String commentText) {
        System.out.println("\nFacade: Avvio Workflow Social Completo (Registrazione, Login, Post, Commento, Modifica)");
        
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
            
            authManager.logout(); // Correzione: Chiamiamo logout direttamente su authManager
        } else {
            System.out.println(" non completato a causa del fallimento del login.");
        }
        System.out.println("Facade: Fine Workflow Social Completo");
    }
}

public class Application {
    public static void main(String[] args) {
        SocialMediaFacade socialFacade = new SocialMediaFacade();

        socialFacade.performFullSocialWorkflow( "MarioRossi", "password123", "PREMIUM",  "Questo è il mio primo post sul nuovo social!", "Ottimo post Mario! Benvenuto!");

        System.out.println("\n[Dimostrazione di operazioni singole tramite Facade]");
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