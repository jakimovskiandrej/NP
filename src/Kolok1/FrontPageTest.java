package Kolok1;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

class CategoryNotFoundException extends Exception {
    public CategoryNotFoundException(String message) {
        super(message);
    }
}

abstract class NewsItem {

    private final String title;
    private final Date date;
    private final Category category;

    public NewsItem(String title, Date date, Category category) {
        this.title = title;
        this.date = date;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public Category getCategory() {
        return category;
    }

    public abstract String getTeaser();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewsItem newsItem = (NewsItem) o;
        return Objects.equals(title, newsItem.title) && Objects.equals(date, newsItem.date) && Objects.equals(category, newsItem.category);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(title);
        result = 31 * result + Objects.hashCode(date);
        result = 31 * result + Objects.hashCode(category);
        return result;
    }
}

class Category {

    private final String categoryName;

    public Category(String categoryMame) {
        this.categoryName = categoryMame;
    }

    public String getCategoryName() {
        return categoryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;
        return Objects.equals(categoryName, category.categoryName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(categoryName);
    }

}

class TextNewsItem extends NewsItem {

    private final String text;

    public TextNewsItem(String title, Date date, Category category, String text) {
        super(title, date, category);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String getTeaser() {
        long minutes = Duration.between(getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime(), LocalDateTime.now()).toMinutes();
        String content = text.length() > 80 ? text.substring(0, 80) : text;
        return getTitle() + "\n" + minutes + "\n" + content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TextNewsItem that = (TextNewsItem) o;
        return Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(text);
        return result;
    }
}

class MediaNewsItem extends NewsItem {

    private final String url;
    private final int views;

    public MediaNewsItem(String title, Date date, Category category, String url, int views) {
        super(title, date, category);
        this.url = url;
        this.views = views;
    }

    public String getUrl() {
        return url;
    }

    public int getViews() {
        return views;
    }

    @Override
    public String getTeaser() {
        long minutes = Duration.between(getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime(), LocalDateTime.now()).toMinutes();
        return getTitle() + "\n" + minutes + "\n" + url + "\n" + views;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MediaNewsItem that = (MediaNewsItem) o;
        return views == that.views && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(url);
        result = 31 * result + views;
        return result;
    }
}

class FrontPage {

    private final List<NewsItem> newsItems;
    private final Category[] categories;

    public FrontPage(Category[] categories) {
        newsItems = new ArrayList<>();
        this.categories = categories;
    }

    public void addNewsItem(NewsItem newsItem) {
        newsItems.add(newsItem);
    }

    public List<NewsItem> listByCategory(Category category) {
        List<NewsItem> result = new ArrayList<>();
        for (NewsItem item : newsItems) {
            if(item.getCategory().equals(category)) {
                result.add(item);
            }
        }
        return result;
    }

    public List<NewsItem> listByCategoryName(String categoryName) throws CategoryNotFoundException {
        List<NewsItem> result = new ArrayList<>();
        Category target = null;

        for (Category c : categories) {
            if(categoryName.equals(c.getCategoryName())) {
                target = c;
                break;
            }
        }

        if(target == null) {
            throw new CategoryNotFoundException("Category " + categoryName + " was not found");
        }

        for (NewsItem item : newsItems) {
            if(target.equals(item.getCategory())) {
                result.add(item);
            }
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (NewsItem item : newsItems) {
            sb.append(item.getTeaser()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FrontPage frontPage = (FrontPage) o;
        return Objects.equals(newsItems, frontPage.newsItems) && Arrays.equals(categories, frontPage.categories);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(newsItems);
        result = 31 * result + Arrays.hashCode(categories);
        return result;
    }
}

public class FrontPageTest {
    public static void main(String[] args) {
        // Reading
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] parts = line.split(" ");
        Category[] categories = new Category[parts.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = new Category(parts[i]);
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        FrontPage frontPage = new FrontPage(categories);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            cal = Calendar.getInstance();
            int min = scanner.nextInt();
            cal.add(Calendar.MINUTE, -min);
            Date date = cal.getTime();
            scanner.nextLine();
            String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
            frontPage.addNewsItem(tni);
        }

        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -min);
            scanner.nextLine();
            Date date = cal.getTime();
            String url = scanner.nextLine();
            int views = scanner.nextInt();
            scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
            frontPage.addNewsItem(mni);
        }
        // Execution
        String category = scanner.nextLine();
        System.out.println(frontPage);
        for(Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
            System.out.println(frontPage.listByCategoryName(category).size());
        } catch(CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}


// Vasiot kod ovde