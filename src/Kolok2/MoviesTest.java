package Kolok2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Movie {
    //неговиот наслов и листа од рејтинзи (цели броеви од 1 до 10)
    private String title;
    private List<Integer> ratings;
    double avgRating;

    public Movie(String title, int[] ratings) {
        this.title = title;
        this.ratings = IntStream.of(ratings)
                .boxed()
                .collect(Collectors.toList());
        avgRating = this.ratings.stream().mapToDouble(Integer::doubleValue).average().orElse(0);
    }

    public String getTitle() {
        return title;
    }

    public List<Integer> getRatings() {
        return ratings;
    }

    public double getAvgRating() {
        return avgRating;
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f) of %d ratings", title, avgRating, ratings.size());
    }
}

class CoefComparator implements Comparator<Movie> {

    int maxRatings;

    public CoefComparator(int maxRatings) {
        this.maxRatings = maxRatings;
    }

    @Override
    public int compare(Movie o1, Movie o2) {
        int ar = Double.compare(o1.avgRating * o1.getRatings().size() / maxRatings,
                o2.avgRating * o2.getRatings().size() / maxRatings);
        if(ar == 0) {
            return o1.getTitle().compareTo(o2.getTitle());
        }
        return ar;
    }
}

class MoviesList {

    List<Movie> movies;

    public MoviesList() {
        movies = new ArrayList<>();
    }

    public void addMovie(String title, int[] ratings) {
        movies.add(new Movie(title, ratings));
    }

    public List<Movie> top10ByAvgRating() {
        return movies.stream().sorted(Comparator.comparing(Movie::getAvgRating).reversed()).limit(10).collect(Collectors.toList());
    }

    public List<Movie> top10ByRatingCoef() {
        int maxRatings = movies.stream().map(m -> m.getRatings().size()).reduce(0,Math::max);
        return movies.stream().sorted(new CoefComparator(maxRatings)).limit(10).collect(Collectors.toList());
    }
}

public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}
