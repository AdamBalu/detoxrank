# Detox Rank

Gamifikovaná aplikácia slúžiaca na pomoc so zbavením závislosti na dopamíne procesom nazývaným dopamínová detoxikácia.

Gamified app made to help you overcome your dopamine addiction. Start dopamine detoxing now!

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
