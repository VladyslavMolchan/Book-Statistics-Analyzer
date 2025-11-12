#  Book Statistics Analyzer

### Опис
Програма аналізує набір JSON-файлів з інформацією про книги, підраховує статистику за вибраним атрибутом (`author`, `yearPublished`, або `genres`) і записує результат у XML-файл.

---

### Спочатку збираємо проєкт
mvn clean package

### Статистика за авторами
java -jar target/JavaCore-1.0-SNAPSHOT.jar books author

### Статистика за роком публікації
java -jar target/JavaCore-1.0-SNAPSHOT.jar books yearPublished

### Статистика за жанрами
java -jar target/JavaCore-1.0-SNAPSHOT.jar books genres

---

###  Основні сутності

| Клас | Призначення |
|------|--------------|
| **Book** | Представляє одну книгу (title, author, yearPublished, genres). Має immutable поля. |
| **BookParser** | Зчитує JSON-файли у список об’єктів `Book`. |
| **StatisticsCalculator** | Розраховує кількість книг за певним атрибутом. |
| **XmlStatisticsWriter** | Записує результат у XML-файл. |
| **App** | Основний клас: керує виконанням, порівнює продуктивність при різній кількості потоків. |

---

###  Формат вхідних даних
JSON-файли мають містити масив об’єктів:

```json
[
  {
    "title": "The Midnight Library",
    "author": "Matt Haig",
    "yearPublished": 2020,
    "genres": ["Fiction", "Philosophical"]
  }
]
```
---

### Час виконання потоків

### Статистика за жанрами
java -jar target/JavaCore-1.0-SNAPSHOT.jar books genres  
Threads: 1, Time: 196ms  
Threads: 2, Time: 2ms  
Threads: 4, Time: 3ms  
Threads: 8, Time: 1ms  
Statistics written to statistics_by_genres.xml

### Статистика за авторами
java -jar target/JavaCore-1.0-SNAPSHOT.jar books author  
Threads: 1, Time: 188ms  
Threads: 2, Time: 2ms  
Threads: 4, Time: 2ms  
Threads: 8, Time: 2ms  
Statistics written to statistics_by_author.xml

### Статистика за роком публікації
java -jar target/JavaCore-1.0-SNAPSHOT.jar books yearPublished  
Threads: 1, Time: 190ms  
Threads: 2, Time: 2ms  
Threads: 4, Time: 2ms  
Threads: 8, Time: 1ms  
Statistics written to statistics_by_yearPublished.xml

