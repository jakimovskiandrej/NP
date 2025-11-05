package Kolok1;

import java.time.LocalDate;
import java.util.*;

class NonExistingItemException extends Exception {

    private final int id;

    public NonExistingItemException(int id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return String.format("Item with id %d doesn't exist", id);
    }
}

abstract class Archive {
    protected int id;
    protected LocalDate dateArchived;

    public Archive(int id) {
        this.id = id;
        this.dateArchived = LocalDate.now();
    }

    public int getId() {
        return id;
    }

    public void setDateArchived(LocalDate dateArchived) {
        this.dateArchived = dateArchived;
    }

    public abstract String tryToOpen(LocalDate l);

}

class ArchiveStore {

    private final List<Archive> archives;
    private final StringBuilder log;

    public ArchiveStore() {
        archives = new ArrayList<>();
        log = new StringBuilder();
    }

    public void archiveItem(Archive item, LocalDate date) {
        archives.add(item);
        item.setDateArchived(date);
        log.append("Item ").append(item.getId()).append(" archived at ").append(date).append("\n");
    }

    public void openItem(int id, LocalDate date) throws NonExistingItemException {
        Optional<Archive> item = archives.stream().filter(a -> a.getId() == id).findAny();
        if(item.isEmpty()) {
            throw new NonExistingItemException(id);
        } else {
            log.append(item.get().tryToOpen(date)).append('\n');
        }
    }

    public String getLog() {
        return log.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArchiveStore that = (ArchiveStore) o;
        return Objects.equals(archives, that.archives) && Objects.equals(log, that.log);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(archives);
        result = 31 * result + Objects.hashCode(log);
        return result;
    }
}

class LockedArchive extends Archive {

    private final LocalDate dateToOpen;

    public LockedArchive(int id, LocalDate dateToOpen) {
        super(id);
        this.dateToOpen = dateToOpen;
    }

    @Override
    public String tryToOpen(LocalDate l) {
        if(l.isBefore(dateToOpen)) {
            return String.format("Item %d cannot be opened before %s", id, dateToOpen);
        } else {
            return String.format("Item %d opened at %s", id,l);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LockedArchive that = (LockedArchive) o;
        return Objects.equals(dateToOpen, that.dateToOpen);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(dateToOpen);
    }
}

class SpecialArchive extends Archive {

    private final int maxOpen;
    private int timesOpen;

    public SpecialArchive(int id, int maxOpen) {
        super(id);
        this.maxOpen = maxOpen;
        timesOpen = 0;
    }

    @Override
    public String tryToOpen(LocalDate l) {
        if(timesOpen == maxOpen) {
            return String.format("Item %d cannot be opened more than %d times", id, maxOpen);
        } else {
            timesOpen++;
            return String.format("Item %d opened at %s", id, l);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpecialArchive that = (SpecialArchive) o;
        return maxOpen == that.maxOpen && timesOpen == that.timesOpen;
    }

    @Override
    public int hashCode() {
        int result = maxOpen;
        result = 31 * result + timesOpen;
        return result;
    }
}

public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        LocalDate date = LocalDate.of(2013, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();

            LocalDate dateToOpen = date.atStartOfDay().plusSeconds(days * 24 * 60 * 60).toLocalDate();
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while(scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch(NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}