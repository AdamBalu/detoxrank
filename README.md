# Dokumentácia k aplikácii Detox Rank

Gamifikovaná aplikácia slúžiaca na pomoc so zbavením závislosti na dopamíne procesom nazývaným dopamínová detoxikácia.

Obsahuje niekoľko hlavných sekcií. Prvou je hodnotiaci systém na udržanie užívateľovej motivácie.
Ďalšiu sekciu tvoria úlohy, ktoré slúžia na vytváranie zdravých zvykov. Nasledujúca sekcia je venovaná teórii, ktorá slúži na
vzdelávanie používateľa, aby lepšie pochopil princípy detoxikácie a vytvoril si vnútornú motiváciu. Dôležitou sekciou je aj časovač,
ktorý slúži na meranie času detoxikácie. Za rôzne aktivity naprieč aplikáciou je možné dostávať rôzne achievementy a skúsenostné body.


## Odporúčaný postup v aplikácii (.apk):

1. Naštudovanie sekcie s teóriou
2. Nastavenie obtiažnosti časovača
3. Spustenie časovača
4. Plnenie denných -> týždenných -> mesačných úloh
5. Plnenie achievementov a vylepšovanie osobného hodnotenia - ranku


## Spustenie bez .apk súboru

0. Odporúčaný software na spustenie aplikácie je Android Studio.
   Preto je postup spustenia aplikácie popísaný výhradne pomocou tohto softwaru.
1. Otvoriť priečinok `main_app` priamo z Android Studia (File -> Open.. -> vybrať main_app)
2. Stiahnuť Gradle JDK verziu 18 a použiť ju pre tento projekt (pozor, musí byť výhradne verzia 18!).
3. Ak je to potrebné, v súbore build.gradle stlačiť synchronizáciu gradle pluginu,
4. Ak nie je nastavená konfigurácia, vytvoriť novú (vpravo hore, add configuration -> Android App)
   a vyplniť potrebné parametre, ako je napríklad SDK.
5. Nakonfigurovať mobilné zariadenie, aby sa na ňom dala spustiť aplikácia, viac na https://developer.android.com/studio/run/device
   Prípadne vytvoriť virtuálne zariadenie pomocou vbudovaného emulátora.
6. Build -> Make Project
7. Run

## Implementácia
#### Hlavné použité knižnice:
```
Hilt, Dagger, MaterialDesign3, Navigation, Material Icons, Room
```

### Štruktúra programu
#### Funkcie s príponou `-HomeScreen`
Rozdeľujú obsah podľa toho, či je aplikácia v rozlíšení obrazovky, ktorá vyžaduje Navigation Drawer.
#### Funkcie s príponou `-Content`
Definujú hlavný obsah sekcie so všetkými Composable elementami
#### Funkcie s príponou `-Large`
Definujú rozloženia elementov, ktoré sa zobrazia iba na veľkých obrazovkách, ako je napríklad tablet alebo laptop.

#### Objekt `Service`
Rieši obsluhu časovača a funkcionalitú spojenú s narábaním s časom.

#### Objekty `LocalDataProvider`
Slúžia na lokálne poskytnutie elementov tvoriacich databázu. Po resete schémy databázy je týmito poskytovateľmi znovu naplnená.

#### Objekt `DetoxRankViewModelProvider`
Objekt návrhového vzoru továreň, ktorý poskytuje View Modely jednotlivým obrazovkám.

#### Objekty `DAO`
Pristupujú k jednotlivým entitám v databáze a robia operácie nad týmito entitami. Každá entita má vlastný Dao (data access object).

#### Repozitáre
Využívajú funkcie DAO objektov na prístup ku dátam. Každý repozitár pracuje nad jednou entitou.