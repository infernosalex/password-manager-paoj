# Password Manager PAO

Aplicatie Java in consola pentru gestionarea unor intrari de tip website, card si nota securizata.

## Cerinte

- Java 17 sau mai nou
- Maven instalat, daca vrei sa rulezi cu `mvn`

## Rulare cu Maven

Daca ai Maven instalat:

```bash
mvn compile
mvn exec:java
```

## Rulare fara Maven

Daca nu ai Maven, poti rula direct cu `javac` si un driver SQLite JDBC:

```bash
curl -L -o sqlite-jdbc.jar https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.50.3.0/sqlite-jdbc-3.50.3.0.jar
mkdir -p target/classes
javac -cp sqlite-jdbc.jar -d target/classes $(find src/main/java -name '*.java')
java -cp target/classes:sqlite-jdbc.jar ro.unibuc.pao.Main
```

## Ce face aplicatia

Aplicatia permite:

- creare cont cu master password
- autentificare
- adaugare credentiale pentru website
- adaugare intrari de tip card
- adaugare note securizate
- listare, cautare, filtrare, sortare
- update si delete
- raport simplu de securitate
- audit CSV pentru actiunile din meniu
- persistenta in SQLite

## Cum o folosesti

La pornire apare meniul principal. Un flux normal este:

1. alegi `1` pentru `Register`
2. introduci `username` si `master password`
3. alegi `2` pentru `Login`
4. dupa autentificare alegi una dintre optiunile `3`, `4` sau `5` pentru a adauga intrari
5. poti folosi `6` pentru listare, `7` pentru cautare, `8` pentru filtrare
6. poti folosi `9` pentru update si `10` pentru delete
7. poti vedea raportul cu `12`
8. iesi din aplicatie cu `0`

## Fisiere generate

La rulare se creeaza:

- `data/password_manager.db` pentru baza de date SQLite
- `audit.csv` pentru istoricul actiunilor
