package com.example.readify;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.readify.Models.Author;
import com.example.readify.Models.Book;
import com.example.readify.Models.Genre;
import com.example.readify.Models.Review;
import com.example.readify.Models.ServerCallback;
import com.example.readify.Models.ServerCallbackForAuthors;
import com.example.readify.Models.ServerCallbackForBooks;
import com.example.readify.Models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ApiConnector extends AsyncTask<String, Integer, String> {

    private static String ALL_GENRES = "genres";
    private static String ALL_GENRE = "genre";
    private static String ALL_BOOKS = "books";
    private static String ALL_BOOK = "book";
    private static String ALL_ITEMS = "items";
    private static String ALL_USERS = "users";
    private static String ALL_UPDATE = "/update";
    private static String ALL_TOP_RATED = "/toprated";
    private static String ALL_AUTHOR = "/author";
    private static String ALL_COMMENTS = "comments";

    private static SharedPreferences preferences;

    private static String SLASH = "/";

    private static String urlv = "http://10.0.2.2:3000/";

    public static void setPreferences(SharedPreferences pref) {
        preferences = pref;
    }

    public static void addUserToDatabase(Context context, final ServerCallback callback, User user) {
        JSONObject userJSON = User.toJSON(user);
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, urlv + ALL_USERS, userJSON, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println(response.toString());
                            try {
                                String id = response.getString("_id");
                                MockupsValues.user.setUid(id);
                                preferences.edit().putString("com.example.readify.uid", id).apply();
                                MockupsValues.user.saveToFirebase();
                            } catch (JSONException ex) {
                                System.out.println(ex);
                            }
                            callback.onSuccess(response);
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("Error");
                        }
                    });
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(jsonObjectRequest);
            queue.start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void getUser(Context context, final ServerCallback callback) {
        String id = SLASH + preferences.getString("com.example.readify._id", "empt");
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, urlv + ALL_USERS + id, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println(response.toString());
                            MockupsValues.user = new User(response);
                            callback.onSuccess(new JSONObject());
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("Error");
                        }
                    });
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(jsonObjectRequest);
            queue.start();
        } catch (Exception e) {
            System.out.println("GET catch error, user.");
            System.out.println(e);
        }
    }

    public static void getInfoClientUser(Context context, final ServerCallback callback) {
        String id = SLASH + preferences.getString("com.example.readify._id", "empt");
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, urlv + ALL_USERS + id, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println(response.toString());
                            MockupsValues.user.getInfoFromJSON(response);
                            callback.onSuccess(new JSONObject());
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("Error");
                        }
                    });
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(jsonObjectRequest);
            queue.start();
        } catch (Exception e) {
            System.out.println("GET catch error, user.");
            System.out.println(e);
        }
    }

    public static void postComment(Context context, Review comment, final ServerCallback callback) {
        try {
            JSONObject object = comment.toJsonObject();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, urlv + ALL_COMMENTS, object, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            callback.onSuccess(response);
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                            System.out.println("Error");

                        }
                    });

            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            System.out.println("Post user error.");
        }
    }

    public static void updateBook(Context context, Book book, final ServerCallback callback) {
        try {
            //RequestFuture<JSONObject> jsonObjectRequestFuture = RequestFuture.newFuture();
            JSONObject object = book.toJsonObject();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.PATCH, urlv + ALL_BOOKS + ALL_UPDATE + SLASH + book.getId(), object, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            callback.onSuccess(response);
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                            System.out.println("Error");

                        }
                    });

            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public static void updateUser(Context context, final ServerCallback callback, User user) {
        String id = SLASH + preferences.getString("com.example.readify._id", "empt");
        try {
            JSONObject jsonObject = User.toJSON(user);
            JsonObjectRequest jsonArrayRequest = new JsonObjectRequest
                    (Request.Method.PATCH, urlv + ALL_USERS + ALL_UPDATE + id, jsonObject, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println(response.toString());
                            callback.onSuccess(new JSONObject());
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("Error");
                        }
                    });
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(jsonArrayRequest);
            queue.start();
        } catch (Exception e) {
            System.out.println("PATCH catch error, user.");
            System.out.println(e);
        }
    }

    public static void getBooksByAuthor(Context context, final ArrayList<Book> books, String authorId, final String bookId, final ServerCallback callback) {
        try {
            String url = urlv + ALL_BOOKS + ALL_AUTHOR + SLASH + authorId;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            String aux = response.toString();
                            try {
                                JSONArray jsonArray = response.getJSONArray("book");
                                ArrayList<Book> authorBooks = Book.bookListFromJson(jsonArray);
                                for (Book b : authorBooks) {
                                    //if(b.getId() != bookId)
                                    books.add(b);

                                }
                                callback.onSuccess(response);
                            } catch (org.json.JSONException e) {

                                System.out.println("Error");

                            }
                            //textView.setText("Response: " + response.toString());
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                            System.out.println("Error");

                        }
                    });

            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(jsonObjectRequest);
            //queue.start();
            //Wait_until_Downloaded();
            //jsonObjectRequestFuture.get(30, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void getAllBooks(Context context, final ServerCallback callback) {
        //final ArrayList<Book> books = new ArrayList<>();
        try {
            //RequestFuture<JSONObject> jsonObjectRequestFuture = RequestFuture.newFuture();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, urlv + ALL_BOOKS, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            String aux = response.toString();
                            try {
                                //String aux2 = response.get("genres");
                                //String aux2 = response.get("genres").toString();

                                JSONArray jsonarray = new JSONArray(response.get("books").toString());
                                ArrayList<Book> books = parseJsonArrayToBookList(jsonarray);

                                //MockupsValues.setGenres(genres);
                                MockupsValues.setAllBooksForTutorial(books);

                                callback.onSuccess(response);
                                //aux2 = "";
                            } catch (org.json.JSONException e) {

                                System.out.println("Error");

                            }
                            //textView.setText("Response: " + response.toString());
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                            System.out.println("Error");

                        }
                    });

            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(jsonObjectRequest);
            //queue.start();
            //Wait_until_Downloaded();
            //jsonObjectRequestFuture.get(30, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.out.println(e);
        }
        //return genres;
    }

    public static void getAllAuthors(Context context, final ServerCallback callback) {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, urlv + "authors", null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            String aux = response.toString();
                            try {

                                JSONArray jsonarray = new JSONArray(response.get("authors").toString());
                                ArrayList<Author> authors = parseJsonArrayToAuthorList(jsonarray);

                                MockupsValues.setAuthors(authors);

                                callback.onSuccess(response);
                            } catch (org.json.JSONException e) {
                                System.out.println("Error");
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("Error");

                        }
                    });

            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void getAllUsers(final Context context, final ServerCallback callback) {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, urlv + ALL_USERS, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonarray = new JSONArray(response.get("users").toString());
                                ArrayList<User> users = parseJsonArrayToUserList(jsonarray, context);

                                MockupsValues.setAllUsers(users);

                                callback.onSuccess(response);
                            } catch (org.json.JSONException e) {
                                System.out.println("Error");
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                            System.out.println("Error");
                        }
                    });

            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static ArrayList<Author> parseJsonArrayToAuthorList(JSONArray jsonarray) {
        final ArrayList<Author> authors = new ArrayList<>();
        for (int i = 0; i < jsonarray.length(); i++) {
            try {
                JSONObject author = jsonarray.getJSONObject(i);
                Author auxAuthor = new Author(author.getString("_id"),
                        author.getString("name"));
                //Book auxBook = new Book(book);
                authors.add(auxAuthor);
            } catch (Exception e) {
                System.out.println("Error parsion book ");
            }
        }
        return authors;
    }

    public static ArrayList<Book> parseJsonArrayToBookList(JSONArray jsonarray) {
        final ArrayList<Book> books = new ArrayList<>();
        for (int i = 0; i < jsonarray.length(); i++) {
            try {
                JSONObject book = jsonarray.getJSONObject(i);
                Author author = new Author(book.getJSONObject("author"));
                Book auxBook = new Book(book.getString("_id"),
                        book.getString("title"), author, book.getString("cover_image"), book.getBoolean("is_new"));
                //Book auxBook = new Book(book);
                books.add(auxBook);
            } catch (Exception e) {
                System.out.println("Error parsion book ");
            }
        }
        return books;
    }

    public static ArrayList<Book> parseJsonArrayToBookListFromUsers(JSONArray jsonarray, Context context) {
        final ArrayList<Book> books = new ArrayList<>();
        for (int i = 0; i < jsonarray.length(); i++) {
            try {
                final JSONObject book = jsonarray.getJSONObject(i);
                ApiConnector.getAuthorById(context, book.getString("author"), new ServerCallbackForAuthors() {
                    @Override
                    public void onSuccess(ArrayList<ArrayList<Author>> authors) {

                    }

                    @Override
                    public void onSuccess(Author author) {
                        Book auxBook = null;
                        try {
                            auxBook = new Book(book.getString("_id"),
                                    book.getString("title"), author, book.getString("cover_image"), book.getBoolean("is_new"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Book auxBook = new Book(book);
                        books.add(auxBook);
                    }
                });

            } catch (Exception e) {
                System.out.println("Error parsion book ");
            }
        }
        return books;
    }

    public static ArrayList<User> parseJsonArrayToUserList(JSONArray jsonarray, Context context) {
        final ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i < jsonarray.length(); i++) {
            try {
                JSONObject user = jsonarray.getJSONObject(i);
                //Author author = new Author(book.getJSONObject("author"));
                User auxUser = new User(user.getString("_id"),
                        user.getString("firebaseId"),
                        user.getString("name"),
                        user.getString("userPicture"),
                        user.getBoolean("premium"),
                        user.getString("email"),
                        parseJsonArrayToGenreList(user.getJSONArray("genres")),
                        parseJsonArrayToBookListFromUsers(user.getJSONArray("library"), context),
                        parseJsonArrayToBookListFromUsers(user.getJSONArray("reading_book"), context),
                        parseJsonArrayToBookListFromUsers(user.getJSONArray("interested_book"), context),
                        parseJsonArrayToBookListFromUsers(user.getJSONArray("read_book"), context),
                        MockupsValues.getAchievements());
                users.add(auxUser);
            } catch (Exception e) {
                System.out.println("Error parse user");
            }
        }
        return users;
    }

    public static ArrayList<Genre> parseJsonArrayToGenreList(JSONArray jsonArray) {
        ArrayList<Genre> genres = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject genre = jsonArray.getJSONObject(i);
                Genre auxGenre = new Genre(genre.getString("_id"),
                        genre.getString("name"), genre.getString("picture"));
                genres.add(auxGenre);
            } catch (Exception e) {
                System.out.println("Error parse genre");
            }
        }
        return genres;
    }

    public static void getGenreById(final Context context, final int index, final ArrayList<String> genresId, final ArrayList<ArrayList<Book>> booksByGenre, final ServerCallbackForBooks callback) {

        if (index >= genresId.size()) {
            callback.onSuccess(booksByGenre);
        } else {
            String url = urlv + ALL_BOOKS + ALL_TOP_RATED + SLASH + genresId.get(index);
            try {
                //final boolean responseReceived = false;
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                //String aux = response.toString();
                                try {
                                    JSONArray jsonarray = new JSONArray(response.get("book").toString());
                                    ArrayList<Book> books = parseJsonArrayToBookList(jsonarray);
                                    booksByGenre.add(books);
                                    getGenreById(context, index + 1, genresId, booksByGenre, new ServerCallbackForBooks() {
                                        @Override
                                        public void onSuccess(ArrayList<ArrayList<Book>> books) {
                                            callback.onSuccess(booksByGenre);
                                        }

                                        @Override
                                        public void onSuccess(Book book) {
                                        }
                                    });
                                    //callback.onSuccess(response);
                                    //responseReceived = true;
                                    //requests--;
                                } catch (org.json.JSONException e) {
                                    System.out.println("Error");
                                }
                                //textView.setText("Response: " + response.toString());
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error
                                System.out.println("Error");
                                booksByGenre.add(new ArrayList<Book>());

                            }
                        });
                RequestQueue queue = Volley.newRequestQueue(context);
                queue.add(jsonObjectRequest);
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        //queue.add(jsonObjectRequest);
    }

    public static void getShopItemsByBookId(Context context, String bookId, final ServerCallback callback) {
        try {
            String url = urlv + ALL_ITEMS + SLASH + ALL_BOOK + SLASH + bookId;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            callback.onSuccess(response);
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                            System.out.println("Error");
                            //booksByGenre.add(new ArrayList<Book>());

                        }
                    });
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(jsonObjectRequest);

        } catch (Exception e) {
            System.out.println("Error in api call. Get item shops by book id");
        }
    }

    public static void getBooksByGenre(Context context, String genreId, final ServerCallback callback) {
        try {
            String url = urlv + ALL_BOOKS + SLASH + ALL_GENRE + SLASH + genreId;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            callback.onSuccess(response);
                            //String aux = response.toString();
                            /*try{
                                //JSONArray jsonarray = new JSONArray(response.get("book").toString());
                                //ArrayList<Book> books = parseJsonArrayToBookList(jsonarray);
                                callback.onSuccess(response);
                            } catch (org.json.JSONException e) {
                                System.out.println("Error");
                            }*/
                            //textView.setText("Response: " + response.toString());
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                            System.out.println("Error");
                            //booksByGenre.add(new ArrayList<Book>());

                        }
                    });
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public static void getTopRatedBooksForGenre(final Context context, ArrayList<Genre> genres, final ServerCallbackForBooks callback) {
        final ArrayList<ArrayList<Book>> booksByGenre = new ArrayList<ArrayList<Book>>();
        ArrayList<String> genresId = new ArrayList<>();
        for (Genre g : genres) {
            //String url = urlv + ALL_BOOKS + ALL_TOP_RATED + SLASH + g.getId();
            //urls.put(url);
            genresId.add(g.getId());
        }
        getGenreById(context, 0, genresId, booksByGenre, new ServerCallbackForBooks() {
            @Override
            public void onSuccess(ArrayList<ArrayList<Book>> books) {
                callback.onSuccess(books);
            }

            @Override
            public void onSuccess(Book book) {
            }
        });
    }

    public static void getBookById(final Context context, String bookId, final ServerCallbackForBooks callback) {
        String url = urlv + ALL_BOOKS + SLASH + bookId;
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println(response.toString());
                            try {
                                JSONObject aux = response.getJSONObject("book");
                                Book book = new Book(aux, context);
                                callback.onSuccess(book);
                            } catch (Exception e) {
                                System.out.println("Error parsing book from jsonobject");
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                            System.out.println("Error");
                        }
                    });
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(jsonObjectRequest);
            //queue.start();
        } catch (Exception e) {
            System.out.println("GET catch error, user.");
            System.out.println(e);
        }
    }

    public static void getAuthorById(final Context context, String authorId, final ServerCallbackForAuthors callback) {
        String url = urlv + "authors" + SLASH + authorId;
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println(response.toString());
                            try {
                                JSONObject aux = response.getJSONObject("author");
                                Author author = new Author(aux);
                                callback.onSuccess(author);
                            } catch (Exception e) {
                                System.out.println("Error parsing book from jsonobject");
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("Error");
                        }
                    });
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            System.out.println("GET catch error, user.");
            System.out.println(e);
        }
    }

    public static void getTopRatedBooks(Context context, final ServerCallback callback) {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, urlv + ALL_BOOKS + ALL_TOP_RATED, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonarray = new JSONArray(response.get("book").toString());
                                ArrayList<Book> books = parseJsonArrayToBookList(jsonarray);
                                MockupsValues.setTopRatedBooks(books);
                                callback.onSuccess(response);
                            } catch (org.json.JSONException e) {
                                System.out.println("Error");
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("Error");
                        }
                    });
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void getGenres(Context context, final ServerCallback callback) {
        final ArrayList<Genre> genres = new ArrayList<>();
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, urlv + ALL_GENRES, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                JSONArray jsonarray = new JSONArray(response.get("genres").toString());
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    genres.add(new Genre(jsonarray.getJSONObject(i)));
                                }
                                MockupsValues.setGenres(genres);
                                callback.onSuccess(response);
                            } catch (org.json.JSONException e) {
                                System.out.println("Error");
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("Error");
                        }
                    });
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    protected String doInBackground(String... urlString) {
        ArrayList<Genre> genres = new ArrayList<>();
        try {
            URL url = new URL(urlv + ALL_GENRES);
            StringBuilder buffer = new StringBuilder();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String content = "", line;
            while ((line = rd.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0)
                return null;

            JSONObject jsonObj = new JSONObject(buffer.toString());
            try {
                //String aux2 = response.get("genres");
                JSONArray jsonarray = new JSONArray(jsonObj.get("genres"));
                for (int i = 0; i < jsonarray.length(); i++) {
                    genres.add(new Genre(jsonarray.getJSONObject(i)));
                }
            } catch (org.json.JSONException e) {

            }
            MockupsValues.setGenres(genres);
            return content;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
