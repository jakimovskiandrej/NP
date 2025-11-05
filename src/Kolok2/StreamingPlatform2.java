package Kolok2;

import java.util.*;
import java.io.*;
import java.util.stream.*;

class Movie {

    String id;
    String name;

    public Movie(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;
        return Objects.equals(id, movie.id) && Objects.equals(name, movie.name);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(name);
        return result;
    }
}

class User {

    String id;
    String username;

    public User(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(username);
        return result;
    }
}

class Rating {

    String userId;
    String movieId;
    int rating;

    public Rating(String userId, String movieId, int rating) {
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rating rating1 = (Rating) o;
        return rating == rating1.rating && Objects.equals(userId, rating1.userId) && Objects.equals(movieId, rating1.movieId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(userId);
        result = 31 * result + Objects.hashCode(movieId);
        result = 31 * result + rating;
        return result;
    }
}

class StreamingPlatform {

    Map<String,Movie> movies;
    Map<String,User> users;
    Map<String,Map<String,Integer>> ratings;

    public StreamingPlatform() {
        movies = new HashMap<>();
        users = new HashMap<>();
        ratings = new HashMap<>();
    }

    void addMovie (String id, String name) {
        movies.put(id, new Movie(id, name));
    }

    void addUser (String id, String username) {
        users.put(id, new User(id, username));
        ratings.putIfAbsent(id, new HashMap<>());
    }

    void addRating (String userId, String movieId, int rating) {
        if(!users.containsKey(userId) || movies.containsKey(movieId) || rating < 1 || rating > 10) {
            return;
        }
        ratings.get(userId).put(movieId,rating);
    }

    void topNMovies (int n) {
        Map<String,Double> result = new HashMap<>();
        for (String s : movies.keySet()) {
            int total = 0;
            int counter = 0;
            for (Map<String, Integer> value : ratings.values()) {
                if(value.containsKey(s)) {
                    total += value.get(s);
                    counter++;
                }
            }
            if(counter > 0) {
                result.put(s, (double) total / counter);
            }
        }
        result.entrySet().stream().sorted((a,b) -> Double.compare(b.getValue(),a.getValue()))
                .limit(n)
                .forEach(s -> System.out.println(movies.get(s.getKey()) + " Rating: " + String.format(" %.2f ", s.getValue())));
    }

    void favouriteMoviesForUsers(List<String> userIds) {

    }

    void similarUsers(String userId) {

    }
}

public class StreamingPlatform2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        StreamingPlatform sp = new StreamingPlatform();

        while (sc.hasNextLine()){
            String line = sc.nextLine();
            String [] parts = line.split("\\s+");

            if (parts[0].equals("addMovie")) {
                String id = parts[1];
                String name = Arrays.stream(parts).skip(2).collect(Collectors.joining(" "));
                sp.addMovie(id ,name);
            } else if (parts[0].equals("addUser")){
                String id = parts[1];
                String name = parts[2];
                sp.addUser(id ,name);
            } else if (parts[0].equals("addRating")){
                //String userId, String movieId, int rating
                String userId = parts[1];
                String movieId = parts[2];
                int rating = Integer.parseInt(parts[3]);
                sp.addRating(userId, movieId, rating);
            } else if (parts[0].equals("topNMovies")){
                int n = Integer.parseInt(parts[1]);
                System.out.println("TOP " + n + " MOVIES:");
                sp.topNMovies(n);
            } else if (parts[0].equals("favouriteMoviesForUsers")) {
                List<String> users = Arrays.stream(parts).skip(1).collect(Collectors.toList());
                System.out.println("FAVOURITE MOVIES FOR USERS WITH IDS: " + users.stream().collect(Collectors.joining(", ")));
                sp.favouriteMoviesForUsers(users);
            } else if (parts[0].equals("similarUsers")) {
                String userId = parts[1];
                System.out.println("SIMILAR USERS TO USER WITH ID: " + userId);
                sp.similarUsers(userId);
            }
        }
    }
}
