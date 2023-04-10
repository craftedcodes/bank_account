/*! 1. Gedanke: Ich möchte ein einziges Register erschaffen, wo sämtliche kontobezogenen Daten pro Kunde einsehbar sind. Hierzu möchte ich eine MutableMap verwenden.
Dazu darf ich nicht vergessen, dass folgende Daten Strings sind: Kundenname, Account Name, Account Passwort, Überweisungsempfänger
während folgende Daten eigentlich Doubles sein sollten: initialer Kontostand, aktueller Kontostand, Ein- oder Auszahlungsbetrag, Überweisungsbetrag.
Weitere interessante Boolean Daten wären, ob ein Sparkonto das einzige Konto des Kunden/der Kundin ist oder ob er/sie noch weitere Konten bei der Bank hat und ob der Kunde gerade eingeloggt ist oder nicht.
Wir haben also verschiedene Datentypen, die wir als Value in der MutableMap abspeichern wollen, allerdings möchte ich ungern Unit als Datentypen hier verwenden, weshalb ich überlege, ob ich die Daten alle in Strings umwandle und sie dann erst in einer MutableList speichere.
Reihenfolge für die Value wäre möglicherweise so:
mutableListOf("Account Name", "Account Passwort", "Initialer Kontostand", "Aktueller Kontostand", "Eingeloggt?", "Ein- oder Auszahlungsbetrag (unterschieden mit +/-)", "Überweisung: Überweisungsempfänger und Betrag") -> Dann kann ich über den Index auf die ersten 5 Daten sicher zugreifen, während ich die anderen Daten über den String-Anfang filtere und dabei herausfinden kann, ob eine Ein- oder Auszahlung oder eine Überweisung stattgefunden hat.
 */

//! Variablen, die in allen Funktionen benötigt werden
// Register über alle Konten, wo abgeprüft wird, was für Bankkontotypen pro Kund*in geführt werden
val registerAllAccounts : MutableMap<String, MutableList<Boolean>> = mutableMapOf()

// Register über die Sparkonten
val registerSavingsAccount : MutableMap<String, MutableList<String>> = mutableMapOf()

// Auflistung aller Daten innerhalb eines Sparkontos
val userDataSavings : MutableList<String> = mutableListOf()

//! Hilfsfunktionen

// Funktion, die kontrolliert, ob bereits im Register für alle Kunden der Name des Kunden/der Kundin auftaucht und gegebenenfalls was für ein Konto


//! Hauptfunktionen

fun main()
{
}
