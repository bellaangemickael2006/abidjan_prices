# Abidjan Prices — Application de comparaison des prix agricoles

Prototype minimal pour collecte, comparaison et visualisation des prix agricoles à Abidjan.

Run (dev):

1. En développement rapide sans PostgreSQL : lancer l'application avec le profil `dev` (H2 en mémoire, Flyway désactivé) :

```powershell
# si vous avez Maven
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# ou (Windows) si vous avez le Maven Wrapper
#.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

2. Pour utiliser PostgreSQL (production ou persistance réelle) :

 - Créer la base `abidjan_prices` et définir les variables d'environnement ou mettre à jour `src/main/resources/application.yml` :

```powershell
# exemple pour créer la DB si psql est disponible
psql -U postgres -c "CREATE DATABASE abidjan_prices;"
```

 - Exemples d'env vars (ou configurez dans `application.yml`):
	 - `SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/abidjan_prices`
	 - `SPRING_DATASOURCE_USERNAME=postgres`
	 - `SPRING_DATASOURCE_PASSWORD=postgres`

3. Lancer en mode production (avec Flyway activé) :

```powershell
mvn spring-boot:run
# ou
.\mvnw.cmd spring-boot:run
```

Frontend: pages statiques dans `src/main/resources/static`.
