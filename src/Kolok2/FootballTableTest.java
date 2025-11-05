package Kolok2;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

class Game {

    String homeTeam;
    String awayTeam;
    int homeGoals;
    int awayGoals;

    public Game(String homeTeam, String awayTeam, int homeGoals, int awayGoals) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public int getHomeGoals() {
        return homeGoals;
    }

    public void setHomeGoals(int homeGoals) {
        this.homeGoals = homeGoals;
    }

    public int getAwayGoals() {
        return awayGoals;
    }

    public void setAwayGoals(int awayGoals) {
        this.awayGoals = awayGoals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;
        return homeGoals == game.homeGoals && awayGoals == game.awayGoals && Objects.equals(homeTeam, game.homeTeam) && Objects.equals(awayTeam, game.awayTeam);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(homeTeam);
        result = 31 * result + Objects.hashCode(awayTeam);
        result = 31 * result + homeGoals;
        result = 31 * result + awayGoals;
        return result;
    }
}

class TeamStats {
    int played;
    int won;
    int lost;
    int drawn;
    int points;
    int goalDifference;

    public TeamStats() {
        this.played = 0;
        this.won = 0;
        this.lost = 0;
        this.drawn = 0;
        this.points = 0;
        this.goalDifference = 0;
    }

    public void addGameResult(int homeGoals,int awayGoals) {
        played++;
        goalDifference += homeGoals - awayGoals;
        if(homeGoals > awayGoals) {
            won++;
            points+=3;
        } else if(homeGoals < awayGoals) {
            lost++;
        } else {
            drawn++;
            points+=1;
        }
    }
}

class FootballTable {

    Map<String,TeamStats> games;

    public FootballTable() {
        games = new HashMap<>();
    }

    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals) {
        games.putIfAbsent(homeTeam, new TeamStats());
        games.putIfAbsent(awayTeam, new TeamStats());
        games.get(homeTeam).addGameResult(homeGoals,awayGoals);
        games.get(awayTeam).addGameResult(awayGoals,homeGoals);
    }

    public void printTable() {
        List<Map.Entry<String,TeamStats>> sorted = games.entrySet().stream().sorted((e1,e2) -> {
            int pointsCompare = Integer.compare(e2.getValue().points, e1.getValue().points);
            if(pointsCompare != 0) {
                return pointsCompare;
            }
            int goalDiffCompare = Integer.compare(e2.getValue().goalDifference, e1.getValue().goalDifference);
            if(goalDiffCompare != 0) {
                return goalDiffCompare;
            }
            return e1.getKey().compareTo(e2.getKey());
        }).collect(Collectors.toList());

        int rank = 1;
        for (Map.Entry<String, TeamStats> s : sorted) {
            String team = s.getKey();
            TeamStats stats = s.getValue();
            System.out.printf("%2d. %-15s%5d%5d%5d%5d%5d\n",rank++,team,stats.played,stats.won,stats.drawn,stats.lost,stats.points);
        }
    }
}

public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}
