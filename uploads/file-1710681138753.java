import java.util.*;

class Movie {
    private String title;
    private String genre;
    private int duration;
    private double rating;
    private String language;

    public Movie(String title, String genre, int duration, double rating, String language) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
        this.language = language;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getDuration() {
        return duration;
    }

    public double getRating() {
        return rating;
    }

    public String getLanguage() {
        return language;
    }

    @Override
    public String toString() {
        return title + " - Genre: " + genre + ", Duration: " + duration + " mins, Rating: " + rating + ", Language: " + language;
    }
}

class Theater {
    private String name;
    private Map<Movie, List<String>> movieShowtimes;

    public Theater(String name) {
        this.name = name;
        this.movieShowtimes = new HashMap<>();
    }

    public void addShowtime(Movie movie, String showtime) {
        movieShowtimes.computeIfAbsent(movie, k -> new ArrayList<>()).add(showtime);
    }

    public Map<Movie, List<String>> getMovieShowtimes() {
        return movieShowtimes;
    }

    @Override
    public String toString() {
        return name;
    }
}

class BookingSystem {
    private List<Theater> theaters;

    public BookingSystem() {
        theaters = new ArrayList<>();
    }

    public void addTheater(Theater theater) {
        theaters.add(theater);
    }

    public List<Theater> getTheaters() {
        return theaters;
    }

    public void displayTheaters() {
        for (int i = 0; i < theaters.size(); i++) {
            System.out.println((i + 1) + ". " + theaters.get(i));
        }
    }

    public void displayMoviesInTheater(int theaterIndex) {
        Theater theater = theaters.get(theaterIndex);
        System.out.println("Movies playing at " + theater + ":");
        Map<Movie, List<String>> movieShowtimes = theater.getMovieShowtimes();
        for (Movie movie : movieShowtimes.keySet()) {
            System.out.println(movie);
        }
    }

    public void displayShowtimesForMovie(int theaterIndex, Movie movie) {
        Theater theater = theaters.get(theaterIndex);
        System.out.println("Showtimes for " + movie.getTitle() + " at " + theater + ":");
        List<String> showtimes = theater.getMovieShowtimes().get(movie);
        for (int i = 0; i < showtimes.size(); i++) {
            System.out.println((i + 1) + ". " + showtimes.get(i));
        }
    }

    public void bookTicket(int theaterIndex, Movie movie, String showtime) {
        // Here you would implement the ticket booking logic.
        System.out.println("Ticket booked for " + movie.getTitle() + " at " + showtime + " in " + theaters.get(theaterIndex));
    }
}

public class Main {
    public static void main(String[] args) {
        BookingSystem bookingSystem = new BookingSystem();

        // Adding sample theaters and movies
        Theater theater1 = new Theater("Cineplex");
        Theater theater2 = new Theater("AMC");

        Movie movie1 = new Movie("Inception", "Sci-Fi", 148, 8.8, "English");
        Movie movie2 = new Movie("The Dark Knight", "Action", 152, 9.0, "English");
        Movie movie3 = new Movie("Parasite", "Thriller", 132, 8.6, "Korean");
        Movie movie4 = new Movie("Spirited Away", "Animation", 125, 8.6, "Japanese");
        theater1.addShowtime(movie1, "10:00 AM");
        theater1.addShowtime(movie2, "1:00 PM");
        theater2.addShowtime(movie3, "3:00 PM");
        theater2.addShowtime(movie4, "6:00 PM");

        bookingSystem.addTheater(theater1);
        bookingSystem.addTheater(theater2);

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            System.out.println("1. Display Theaters");
            System.out.println("2. Choose Theater and Movie");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Available Theaters:");
                    bookingSystem.displayTheaters();
                    break;
                case 2:
                    System.out.println("Choose a theater:");
                    bookingSystem.displayTheaters();
                    int theaterIndex = scanner.nextInt() - 1;
                    bookingSystem.displayMoviesInTheater(theaterIndex);
                    System.out.print("Enter the index of the movie you want to book: ");
                    int movieIndex = scanner.nextInt() - 1;
                    scanner.nextLine(); // Consume newline
                    Movie selectedMovie = null;
                    for (Movie movie : bookingSystem.getTheaters().get(theaterIndex).getMovieShowtimes().keySet()) {
                        if (movieIndex == 0) {
                            selectedMovie = movie;
                            break;
                        }
                        movieIndex--;
                    }
                    if (selectedMovie == null) {
                        System.out.println("Invalid movie selection.");
                        break;
                    }
                    bookingSystem.displayShowtimesForMovie(theaterIndex, selectedMovie);
                    System.out.print("Enter the index of the showtime you want to book: ");
                    int showtimeIndex = scanner.nextInt() - 1;
                    if (showtimeIndex < 0 || showtimeIndex >= bookingSystem.getTheaters().get(theaterIndex).getMovieShowtimes().get(selectedMovie).size()) {
                        System.out.println("Invalid showtime selection.");
                        break;
                    }
                    String selectedShowtime = bookingSystem.getTheaters().get(theaterIndex).getMovieShowtimes().get(selectedMovie).get(showtimeIndex);
                    bookingSystem.bookTicket(theaterIndex, selectedMovie, selectedShowtime);
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
}
