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

## Actiuni si interogari disponibile

Sistemul permite urmatoarele actiuni si interogari:

### Autentificare
- **Register** - creare cont nou cu username si master password
- **Login** - autentificare cu username si master password
- **Logout** - iesire din cont

### Gestionarea credentialelor
- **Adaugare credential website** - stocare URL, username, password
- **Adaugare credential card** - stocare date card (numar, CVV, data expirare, titular)
- **Adaugare nota securizata** - stocare text protejat
- **Listare credentiale** - afisare toate credentialele utilizatorului
- **Cautare credentiale** - cautare dupa cuvinte cheie
- **Filtrare pe categorii** - afisare credentiale dintr-o categorie specifica
- **Sortare credentiale** - ordonare dupa nume, data crearii, etc.
- **Modificare credentiale** - actualizare date existente
- **Stergere credentiale** - stergere din baza de date

### Gestionarea categoriilor
- **Listare categorii** - afisare categorii disponibile
- **Adaugare categorii** - creare categorie noua
- **Atribuire la categorii** - asociere credentiale cu categorii

### Securitate si rapoarte
- **Generare parola** - generare parola aleatoare sigura
- **Raport de securitate** - statistici si avertismente de securitate
- **Export rezumat** - export datelor pentru backup
- **Audit** - inregistrare CSV a tuturor actiunilor din sistem

## Tipurile de obiecte din sistem

Sistemul gestioneaza urmatoarele tipuri de obiecte:

### UserAccount - Cont utilizator
- username/email
- master password (hash)
- data creare

### Vault - Seif virtual
- container pentru credentiale ale unui utilizator
- asociat cu UserAccount
- contine credentiale si categorii

### Credential - Credențial (clasa de baza)
- **id** - identificator unic
- **nume/titlu** - denumirea credentialului
- **data creare** - cand a fost adaugat
- **data modificare** - ultima actualizare
- **categorie** - categoria asociata

**Tipuri speciale (mostenesc din Credential):**

#### WebsiteCredential - Credential website
- **URL** - adresa site-ului
- **username** - utilizatorul pe site
- **password** - parola pentru acces

#### CardCredential - Credential card
- **numar card** - cardului bancar/de credit
- **titular** - numele titularului
- **data expirarii** - valabilitatea cardului
- **CVV** - codul de securitate

#### NoteCredential - Nota securizata
- **continut text** - informatii confidentiale in format text

### Category - Categorie
- **id** - identificator unic
- **nume** - denumirea categoriei
- **descriere** - detalii despre categorie

### AuditEntry - Intrare in audit
- **utilizator** - cine a efectuat actiunea
- **actiune** - ce s-a facut (de ex. "LOGIN", "ADD_CREDENTIAL")
- **data si ora** - cand s-a intamplat
- **detalii** - informatii suplimentare despre actiune

### SecurityReport - Raport de securitate
- **numar total credentiale** - statistica generala
- **credentiale pe tip** - distributie: website, card, note
- **statistici parola** - lungime medie, caractere speciale, etc.
- **avertismente** - parola slaba, date expirate, etc.

## Fisiere generate

La rulare se creeaza:

- `data/password_manager.db` pentru baza de date SQLite
- `audit.csv` pentru istoricul actiunilor

**Tipuri speciale (mostenesc din Credential):**

#### WebsiteCredential - Credențial website
- **URL** - adresa site-ului
- **username** - utilizatorul pe site
- **password** - parola pentru acces

#### CardCredential - Credențial card
- **număr card** - cardului bancar/de credit
- **titular** - numele titularului
- **data expirării** - valabilitatea cardului
- **CVV** - codul de securitate

#### NoteCredential - Nota securizată
- **conținut text** - informații confidențiale în format text

### Category - Categorie
- **id** - identificator unic
- **nume** - denumirea categoriei
- **descriere** - detailii despre categorie

### AuditEntry - Intrare în audit
- **utilizator** - cine a efectuat acțiunea
- **acțiune** - ce s-a făcut (de ex. "LOGIN", "ADD_CREDENTIAL")
- **dată și ora** - când s-a întâmplat
- **detalii** - informații suplimentare despre acțiune

### SecurityReport - Raport de securitate
- **număr total credențiale** - statistică generală
- **credențiale pe tip** - distribuție: website, card, note
- **statistici parolă** - lungime medie, caractere speciale, etc.
- **avertismente** - parolă slabă, date expirate, etc.

## Fisiere generate

La rulare se creeaza:

- `data/password_manager.db` pentru baza de date SQLite
- `audit.csv` pentru istoricul actiunilor
