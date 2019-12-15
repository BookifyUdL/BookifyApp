package com.example.readify;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.example.readify.Models.Book;
import com.example.readify.Models.Emoji;
import com.example.readify.Models.Genre;
import com.example.readify.Models.Item;
import com.example.readify.Models.Review;
import com.example.readify.Models.Shop;
import com.example.readify.Models.User;
import com.example.readify.Models.Achievement;

import java.util.ArrayList;

public class MockupsValues {

    private static ArrayList<Shop> SHOPS;
    private static ArrayList<Item> ITEMS;

    private static ArrayList<Genre> GENRE;
    private static ArrayList<Genre> PROFILE_GENRE_PERSONALIZED;
  
    private static ArrayList<Book> ALL_BOOKS_FOR_TUTORIAL;
    private static ArrayList<Book> LAST_ADDED_BOOKS;
    private static ArrayList<Book> SAME_AUTHOR_BOOKS;
    private static ArrayList<Book> SAME_GENDER_BOOKS;
    private static ArrayList<Book> PENDING_LIST_BOOKS;
    private static ArrayList<Book> READING_LIST_BOOKS;
    private static ArrayList<Book> PROFILE_PERSONALIZED_LIST_BOOKS;
    private static ArrayList<Book> TOP_RATED_BOOKS;

    private static ArrayList<Achievement> ACHIEVEMENTS;
    private static ArrayList<Achievement> PROFILE_ACHIEVEMENTS_PERSONALIZED;
  
    private static ArrayList<Book> PENDING_BOOKS_WITH_DISCOVER_BUTTON;
    private static ArrayList<Review> REVIEWS;
    private static ArrayList<Review> SUB_REVIEWS;
    private static ArrayList<Emoji> EMOJIS;
    private static ArrayList<User> USERS;
    public  static User user = new User();
    private static Context context;
    private static boolean isUserInDatabase = false;


    //private static User userProfile = new User("Connor MacArthur", true, getPersonalizedGenres(), getLastAddedBooksWithReads());

    public static User getUserProfile() { return user; }

    public static void setContext(Context con){
        context = con;
    }

    public static User getUser(){
        return user;
    }
  

    //public void setUser(User user) { this.user = user; }

    private static ArrayList<Shop> getShops(){
        if(SHOPS == null || SHOPS.isEmpty()){
            SHOPS = new ArrayList<>();
            SHOPS.add(new Shop(Shop.CASA_DEL_LIBRO_ID , "casadellibro", "Casa del Libro"));
            SHOPS.add(new Shop(Shop.CORTE_INGLES_ID,"elcorteingles", "El Corte Inglés"));
            SHOPS.add(new Shop(Shop.FNAC_ID,"fnac", "Fnac"));
        }
        return SHOPS;
    }

    public  static Shop getCorrespondiShop(Shop shop){
        for (Shop aux: getShops()){
            if(aux.getId().equals(shop.getId()))
                return aux;
        }
        return getShops().get(0);
    }

    public static void setTopRatedBooks(ArrayList<Book> topRatedBooks){
        TOP_RATED_BOOKS = topRatedBooks;
    }

    public static ArrayList<Book> getTopRatedBooks(){
        if(TOP_RATED_BOOKS == null)
            TOP_RATED_BOOKS = new ArrayList<>();
        return TOP_RATED_BOOKS;
    }

    public static void setIsUserInDatabase(boolean isOrNot){
        isUserInDatabase = isOrNot;
    }

    public static boolean getIsUserInDatabase(){
        return isUserInDatabase;
    }

    public static ArrayList<Item> getItems(){
        ArrayList<Shop> shops = getShops();
        if(ITEMS == null || ITEMS.isEmpty()){
            ITEMS = new ArrayList<>();
            ITEMS.add(new Item(10.5, shops.get(0)));
            ITEMS.add(new Item(7.5, shops.get(1)));
            ITEMS.add(new Item(8, shops.get(2)));

        }
        return ITEMS;
    }

    public static ArrayList<User> getUsers(){
        if(USERS == null || USERS.isEmpty()){
            USERS = new ArrayList<>();
            USERS.add(new User("Núria Gonzalez", "nuria@gmail.com" ,"user3"));
            USERS.add(new User("Alicia Carrasco", "carrasco@hotmail.es","user3"));
            USERS.add(new User("Joan Pérez", "perez2004@outlook.com", "user1"));
            USERS.add(new User("Arnau Roca", "arnau23@gmail.com","user2"));
        }
        return USERS;
    }

    public static ArrayList<Emoji> getEmojis(){
        if(EMOJIS == null || EMOJIS.isEmpty()){
            EMOJIS = new ArrayList<>();
            EMOJIS.add(new Emoji(context.getResources().getString(R.string.angry_emoji), "angry"));
            EMOJIS.add(new Emoji(context.getResources().getString(R.string.scared_emoji), "scare"));
            EMOJIS.add(new Emoji(context.getResources().getString(R.string.sad_emoji), "unhappy"));
            EMOJIS.add(new Emoji(context.getResources().getString(R.string.confused_emoji), "confused"));
            EMOJIS.add(new Emoji(context.getResources().getString(R.string.bored_emoji), "bored"));
            EMOJIS.add(new Emoji(context.getResources().getString(R.string.shocked_emoji), "surprised"));
            EMOJIS.add(new Emoji(context.getResources().getString(R.string.happy_emoji), "happy"));
            EMOJIS.add(new Emoji(context.getResources().getString(R.string.excited_emoji), "excited"));
        }
        return EMOJIS;
    }

    public static ArrayList<Genre> getGenres() {
        /*if (GENRE == null || GENRE.isEmpty()) {
            GENRE = new ArrayList<>();
            GENRE.add(new Genre( context.getResources().getString(R.string.biography_gender), "genre1"));
            GENRE.add(new Genre( context.getResources().getString(R.string.internet_gender), "genre2"));
            GENRE.add(new Genre( context.getResources().getString(R.string.crime_gender), "genre3"));
            GENRE.add(new Genre( context.getResources().getString(R.string.education_gender), "genre4"));
            GENRE.add(new Genre( context.getResources().getString(R.string.literature_gender), "genre5"));
            GENRE.add(new Genre( context.getResources().getString(R.string.health_gender), "genre6"));
            GENRE.add(new Genre( context.getResources().getString(R.string.romance_gender), "genre7"));
            GENRE.add(new Genre( context.getResources().getString(R.string.fantasy_gender), "genre8"));
            GENRE.add(new Genre( context.getResources().getString(R.string.adventure_gender), "genre9"));
        }*/
        if(GENRE == null)
            GENRE = new ArrayList<>();
        return GENRE;
    }

    public static void setGenres(ArrayList<Genre> genres){
        GENRE = genres;
    }


    public static void addReview(Review review){
        if(REVIEWS == null || REVIEWS.isEmpty())
            REVIEWS = new ArrayList<>();
        REVIEWS.add(review);
    }

    public static void setReview(Review review, int position){
        if(REVIEWS == null || REVIEWS.isEmpty())
            REVIEWS = new ArrayList<>();
        REVIEWS.set(position, review);
    }

    public static void deleteReview(Review review){
        if(REVIEWS == null || REVIEWS.isEmpty())
            REVIEWS = new ArrayList<>();
        REVIEWS.remove(review);
    }

    public static ArrayList<Review> getReviews(){
        if(REVIEWS == null || REVIEWS.isEmpty()){
            REVIEWS = new ArrayList<>();
            REVIEWS.add(new Review(new User("Joaquim Sabina", "user1"), "Un libro intrigante y entretenido."));
            REVIEWS.add(new Review(new User("Mateo Jordi", "user2"), "Cuando leí este libro por primera vez era demasiado pequeña para entender su verdadero significado. Un par de años después, cuando lo releí, descubrí lo bien escrito que estaba, lo realista que era. Todos los adolescentes (o al menos, la inmensa mayoría) hemos sido como Holden Caulfield alguna vez. Sorprendente y brillante, un clásico que debe leerse como mínimo una vez en la vida."));
            REVIEWS.add(new Review(new User("Maite La Cruz", "user3"), "\n" +
                    "Un libro para leer y disfrutar, histórico, esta vez deja de lado libros fantásticos, el autor que escribía en el semanal de los Domingos que entraba con en el Norte de Castilla, Arturo Pérez-Reverte, dejando de lado \"El capitán Alatriste\" y su personaje de Ficción. Se habla del Cid, un personaje de Castilla muy temido por los árabes y también apreciado, despreciado por Doña Urraca y Alfonso, y acusado por la muerte de su hermano Sancho estará siempre en el exilio, sirviendo a Alfonso y no sabiendo o sí el engaño. Una novela que sale en la prensa , y revistas de moda como Marie Claire, que gusta leer, para ver diversas versiones de la historia del CID, que por algunos historiadores, se piensa que es un personaje legendario, y nunca existió."));
            REVIEWS.add(new Review(new User("Joaquim Sabina", "user1"), "Un libro intrigante y entretenido.", getSubReviews()));
            REVIEWS.add(new Review(new User("Joaquim Sabina", "user1"), "Un libro intrigante y entretenido.", getSubReviews()));
            REVIEWS.add(new Review(new User("Joaquim Sabina", "user1"), "Un libro intrigante y entretenido.", getSubReviews()));

        }
        return REVIEWS;
    }

    public static ArrayList<Review> getSubReviews(){
        if(SUB_REVIEWS == null || SUB_REVIEWS.isEmpty()){
            SUB_REVIEWS = new ArrayList<>();
            SUB_REVIEWS.add(new Review(new User("Joaquim Sabina", "user1"), "Un libro intrigante y entretenido."));
            SUB_REVIEWS.add(new Review(new User("Mateo Jordi", "user2"), "Cuando leí este libro por primera vez era demasiado pequeña para entender su verdadero significado. Un par de años después, cuando lo releí, descubrí lo bien escrito que estaba, lo realista que era. Todos los adolescentes (o al menos, la inmensa mayoría) hemos sido como Holden Caulfield alguna vez. Sorprendente y brillante, un clásico que debe leerse como mínimo una vez en la vida."));
            SUB_REVIEWS.add(new Review(new User("Maite La Cruz", "user3"), "Un libro intrigante y entretenido."));
            SUB_REVIEWS.add(new Review(new User("Joaquim Sabina", "user1"), "Un libro intrigante y entretenido."));
        }
        return SUB_REVIEWS;
    }

    public static Genre getGenreFromGenresList(int i){
        return getGenres().get(i);
    }
  
    public static void addPendingBook(Book book){
        book.setRead(false);
        if(PENDING_LIST_BOOKS == null)
            PENDING_LIST_BOOKS = new ArrayList<>();
        PENDING_LIST_BOOKS.add(book);
        if(!user.getLibrary().contains(book))
            user.addBookToLibrary(book);
    }

    public static void removePendingListBook(Book book){
        if(PENDING_LIST_BOOKS == null)
            PENDING_LIST_BOOKS = new ArrayList<>();
        PENDING_LIST_BOOKS.remove(book);
    }

    public static void removeReadingListBook(Book book){
        if(READING_LIST_BOOKS == null)
            READING_LIST_BOOKS = new ArrayList<>();
        READING_LIST_BOOKS.remove(book);
    }

    public static  void addReadingBook(Book book){
        if(READING_LIST_BOOKS== null)
            READING_LIST_BOOKS = new ArrayList<>();
        READING_LIST_BOOKS.add(book);
        if(!user.getLibrary().contains(book))
            user.addBookToLibrary(book);
    }

    public static void setPendingListBooks(ArrayList<Book> books){
        PENDING_LIST_BOOKS = books;
    }

    public static ArrayList<Book> getPendingListBooks(){
        if(PENDING_LIST_BOOKS == null){
            PENDING_LIST_BOOKS = new ArrayList<>();
        }
        return PENDING_LIST_BOOKS;
    }

    public static ArrayList<Book> getReandingListBooks(){
        if(READING_LIST_BOOKS == null){
            READING_LIST_BOOKS = new ArrayList<>();
        }
        return READING_LIST_BOOKS;
    }

    public static ArrayList<Book> getAllBooksForTutorial(){
        /*if(ALL_BOOKS_FOR_TUTORIAL == null || ALL_BOOKS_FOR_TUTORIAL.isEmpty() || ALL_BOOKS_FOR_TUTORIAL.size() != 15){
            ALL_BOOKS_FOR_TUTORIAL = new ArrayList<>();
            ALL_BOOKS_FOR_TUTORIAL.addAll(getLastAddedBooks());
            ALL_BOOKS_FOR_TUTORIAL.addAll(getSameAuthorBooks());
            ALL_BOOKS_FOR_TUTORIAL.addAll(getSameGenderBooks());
        }*/
        if(ALL_BOOKS_FOR_TUTORIAL == null)
            ALL_BOOKS_FOR_TUTORIAL = new ArrayList<>();
        return ALL_BOOKS_FOR_TUTORIAL;
    }

    public static void setAllBooksForTutorial(ArrayList<Book> booksForTutorial){
        ALL_BOOKS_FOR_TUTORIAL = booksForTutorial;
    }


    public static ArrayList<Book> getLastAddedBooks(){
        if(LAST_ADDED_BOOKS == null || LAST_ADDED_BOOKS.isEmpty()){
            LAST_ADDED_BOOKS = new ArrayList<>();
            if(ALL_BOOKS_FOR_TUTORIAL != null){
                for (Book book : ALL_BOOKS_FOR_TUTORIAL){
                    if(book.isNew())
                        LAST_ADDED_BOOKS.add(book);
                }
            }
            /*LAST_ADDED_BOOKS.add(new Book("Algún día hoy", "Ángela Becerra", "lib1", 2015, 253,getGenreFromGenresList(6)));
            LAST_ADDED_BOOKS.add(new Book("La cocinera de Gastamar", "Fernadndo J.Múñez", "lib2", 2001, 200,getGenreFromGenresList(3)));
            LAST_ADDED_BOOKS.add(new Book("El rey recibe", "Eduardo Mendoza", "lib3", 2017, 353,getGenreFromGenresList(4)));
            LAST_ADDED_BOOKS.add(new Book("100 recetas de oro", "Carlos Arguiñano", "lib4", 2019, 153,getGenreFromGenresList(5)));
            LAST_ADDED_BOOKS.add(new Book("Patria", "Fernando Aramburu", "lib5", 2018, 99,getGenreFromGenresList(7)));*/
        }
        return LAST_ADDED_BOOKS;
    }

    public static ArrayList<Book> getLastAddedBooksDiscover(){
        if(PENDING_BOOKS_WITH_DISCOVER_BUTTON == null || PENDING_BOOKS_WITH_DISCOVER_BUTTON.isEmpty()){
            PENDING_BOOKS_WITH_DISCOVER_BUTTON = new ArrayList<>();
            PENDING_BOOKS_WITH_DISCOVER_BUTTON.addAll(getLastAddedBooks());
            PENDING_BOOKS_WITH_DISCOVER_BUTTON.add(new Book());
      }
        return LAST_ADDED_BOOKS;
    }

    public static ArrayList<Book> getSameAuthorBooks(){
        if(SAME_AUTHOR_BOOKS == null || SAME_AUTHOR_BOOKS.isEmpty()){
            SAME_AUTHOR_BOOKS = new ArrayList<>();
            SAME_AUTHOR_BOOKS.add(new Book("El penúltimo sueño", "Ángela Becerra", "auth1", 2011, 289,getGenreFromGenresList(6)));
            SAME_AUTHOR_BOOKS.add(new Book("Lo que le falta al tiempo", "Ángela Becerra", "auth2", 2015, 129,getGenreFromGenresList(4)));
            SAME_AUTHOR_BOOKS.add(new Book("Memorias de un singerguenza de siete suelas", "Ángela Becerra", "auth3", 2005, 189,getGenreFromGenresList(6)));
            SAME_AUTHOR_BOOKS.add(new Book("Ella, que todo lo tuvo", "Ángela Becerra", "auth4", 2019, 444,getGenreFromGenresList(4)));
            SAME_AUTHOR_BOOKS.add(new Book("De los amores negados", "Ángela Becerra", "auth5", 2007, 299,getGenreFromGenresList(4)));
        }
        return SAME_AUTHOR_BOOKS;
    }

    public static ArrayList<Book> getSameGenderBooks(){
        if(SAME_GENDER_BOOKS == null || SAME_GENDER_BOOKS.isEmpty()){
            SAME_GENDER_BOOKS = new ArrayList<>();
            SAME_GENDER_BOOKS.add(new Book("Largo pétalo de mar", "Isabel Allende", "oth1", 2005, 189,getGenreFromGenresList(6)));
            SAME_GENDER_BOOKS.add(new Book("Reina Roja", "Juan Gómez-Jurado", "oth2", 2015, 461,getGenreFromGenresList(6)));
            SAME_GENDER_BOOKS.add(new Book("Silencio", "Pablo Poveda", "oth3", 2016, 501,getGenreFromGenresList(6)));
            SAME_GENDER_BOOKS.add(new Book("Eres parte de mí", "Saint Rose Sophie", "oth4", 2018, 235,getGenreFromGenresList(6)));
            SAME_GENDER_BOOKS.add(new Book("7 Libros para Eva", "Roberto Martínez Guzmán", "oth5", 2011, 223,getGenreFromGenresList(6)));

        }
        return SAME_GENDER_BOOKS;
    }

    public static ArrayList<Genre> getPersonalizedGenres() {
        if (PROFILE_GENRE_PERSONALIZED == null || PROFILE_GENRE_PERSONALIZED.isEmpty()) {
            PROFILE_GENRE_PERSONALIZED = new ArrayList<>();
            PROFILE_GENRE_PERSONALIZED.add(new Genre("Crime & thrillers", "genre3"));
            PROFILE_GENRE_PERSONALIZED.add(new Genre("Fiction & Literature", "genre5"));
            PROFILE_GENRE_PERSONALIZED.add(new Genre("Health & well-being", "genre6"));
            PROFILE_GENRE_PERSONALIZED.add(new Genre("Sci-fi & Fantasy", "genre8"));
            PROFILE_GENRE_PERSONALIZED.add(new Genre("Travel & Adventure", "genre9"));
        }
        return PROFILE_GENRE_PERSONALIZED;
    }

    public static ArrayList<Book> getLastAddedBooksWithReads(){
        if(PROFILE_PERSONALIZED_LIST_BOOKS == null || PROFILE_PERSONALIZED_LIST_BOOKS.isEmpty()){
            Book book = new Book();
            PROFILE_PERSONALIZED_LIST_BOOKS = new ArrayList<>();

            book = new Book("Algún día hoy", "Ángela Becerra", "lib1");
            book.setRead(true);
            PROFILE_PERSONALIZED_LIST_BOOKS.add(book);

            PROFILE_PERSONALIZED_LIST_BOOKS.add(new Book("La cocinera de Gastamar", "Fernadndo J.Múñez", "lib2"));
            PROFILE_PERSONALIZED_LIST_BOOKS.add(new Book("El rey recibe", "Eduardo Mendoza", "lib3"));

            book = new Book("100 recetas de oro", "Carlos Arguiñano", "lib4");
            book.setRead(true);
            PROFILE_PERSONALIZED_LIST_BOOKS.add(book);

            book = new Book("Patria", "Fernando Aramburu", "lib5");
            book.setRead(true);
            PROFILE_PERSONALIZED_LIST_BOOKS.add(book);
        }
        return PROFILE_PERSONALIZED_LIST_BOOKS;
    }

    public static ArrayList<Achievement> getAchievements(){
        if (ACHIEVEMENTS == null || ACHIEVEMENTS.isEmpty()){
            ACHIEVEMENTS = new ArrayList<>();
            ACHIEVEMENTS.add(new Achievement(0,"Bronze reader", "Read 1 books of any genre", "achiev1_bronze", 0, 1));
            ACHIEVEMENTS.add(new Achievement(1,"Silver reader", "Read 10 books of any genre", "achiev1_plata", 0, 10));
            ACHIEVEMENTS.add(new Achievement(2,"Gold reader", "Read 20 books of any genre", "achiev1", 0, 20));
            ACHIEVEMENTS.add(new Achievement(3,"Diamond reader", "Read 100 books of any genre", "achiev1_diamond", 0, 100));
            ACHIEVEMENTS.add(new Achievement(4,"Golden user", "Upgrade your account to premium", "premium_image_icon", 0, 1));
        }
        return ACHIEVEMENTS;
    }

    public static ArrayList<Achievement> getAchievementsPersonalized(){
        if (PROFILE_ACHIEVEMENTS_PERSONALIZED == null || PROFILE_ACHIEVEMENTS_PERSONALIZED.isEmpty()){
            PROFILE_ACHIEVEMENTS_PERSONALIZED = new ArrayList<>();
            Achievement achievement = new Achievement(3,"Reading teacher", "Read 20 books of any genre", "achiev1", 0, 20);
            achievement.incrementValue(20);
            PROFILE_ACHIEVEMENTS_PERSONALIZED.add(achievement);

            achievement = new Achievement(4,"Golden user", "Upgrade your account to premium", "premium_image_icon", 0, 1);
            achievement.incrementValue(1);
            PROFILE_ACHIEVEMENTS_PERSONALIZED.add(achievement);
        }
        return PROFILE_ACHIEVEMENTS_PERSONALIZED;
    }
}
