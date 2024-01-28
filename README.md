# Detox Rank

Gamifikovaná aplikácia slúžiaca na pomoc so zbavením závislosti na dopamíne procesom nazývaným dopamínová detoxikácia.

Obsahuje niekoľko hlavných sekcií. Prvou je hodnotiaci systém na udržanie užívateľovej motivácie.
Ďalšiu sekciu tvoria úlohy, ktoré slúžia na vytváranie zdravých zvykov. Nasledujúca sekcia je venovaná teórii, ktorá slúži na
vzdelávanie používateľa, aby lepšie pochopil princípy detoxikácie a vytvoril si vnútornú motiváciu. Dôležitou sekciou je aj časovač,
ktorý slúži na meranie času detoxikácie. Za rôzne aktivity naprieč aplikáciou je možné dostávať rôzne achievementy a skúsenostné body.


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
