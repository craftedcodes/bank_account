/*! 1. Gedanke: Ich möchte ein einziges Register erschaffen, wo sämtliche kontobezogenen Daten pro Kunde einsehbar sind. Hierzu möchte ich eine MutableMap verwenden.
Dazu darf ich nicht vergessen, dass folgende Daten Strings sind: Kundenname, Account Name, Account Passwort, Überweisungsempfänger
während folgende Daten eigentlich Doubles sein sollten: initialer Kontostand, aktueller Kontostand, Ein- oder Auszahlungsbetrag, Überweisungsbetrag.
Weitere interessante Boolean Daten wären, ob ein Sparkonto das einzige Konto des Kunden/der Kundin ist oder ob er/sie noch weitere Konten bei der Bank hat und ob der Kunde gerade eingeloggt ist oder nicht.
Wir haben also verschiedene Datentypen, die wir als Value in der MutableMap abspeichern wollen, allerdings möchte ich ungern Unit als Datentypen hier verwenden, weshalb ich überlege, ob ich die Daten alle in Strings umwandle und sie dann erst in einer MutableList speichere.
 */

//! Variablen
val registerSavingsAccount : MutableMap<String, MutableList<String>> = mutableMapOf()

//! Hilfsfunktionen

//! Hauptfunktionen
fun main()
{
}
