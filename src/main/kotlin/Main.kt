/*! 1. Gedanke: Ich möchte ein einziges Register erschaffen, wo sämtliche kontobezogenen Daten pro Kunde einsehbar sind. Hierzu möchte ich eine MutableMap verwenden.
Dazu darf ich nicht vergessen, dass folgende Daten Strings sind: Kundenname, Account Name, Account Passwort, Überweisungsempfänger
während folgende Daten eigentlich Doubles sein sollten: initialer Kontostand, aktueller Kontostand, Ein- oder Auszahlungsbetrag, Überweisungsbetrag.
Weitere interessante Boolean Daten wären, ob ein Sparkonto das einzige Konto des Kunden/der Kundin ist oder ob er/sie noch weitere Konten bei der Bank hat und ob der Kunde gerade eingeloggt ist oder nicht.
Wir haben also verschiedene Datentypen, die wir als Value in der MutableMap abspeichern wollen, allerdings möchte ich ungern Unit als Datentypen hier verwenden, weshalb ich überlege, ob ich die Daten alle in Strings umwandle und sie dann erst in einer MutableList speichere.
Reihenfolge für die Value wäre möglicherweise so:
mutableListOf("Account Name", "Account Passwort", "Initialer Kontostand", "Aktueller Kontostand", "Eingeloggt?", "Ein- oder Auszahlungsbetrag (unterschieden mit +/-)", "Überweisung: Überweisungsempfänger und Betrag") -> Dann kann ich über den Index auf die ersten 5 Daten sicher zugreifen, während ich die anderen Daten über den String-Anfang filtere und dabei herausfinden kann, ob eine Ein- oder Auszahlung oder eine Überweisung stattgefunden hat.
 */
//! Variablen, die in allen Funktionen benötigt werden
// Register der Bankangestellten.
val bankEmployees : MutableMap<String, MutableList<String>> = mutableMapOf(
		"Hannah Arendt" to mutableListOf("Passwort123456789", "logged i"),
		"Bernd Depp" to mutableListOf("1234567890", "logged out"),
                                                                          )

// Register über alle Konten, wo abgeprüft wird, was für Bankkontotypen pro Kund*in geführt werden.
val registerAllAccounts : MutableMap<String, MutableList<String>> = mutableMapOf()

// Register über die Sparkonten.
val registerSavingsAccount : MutableMap<String, MutableList<String>> = mutableMapOf()

// Auflistung aller Daten innerhalb eines Sparkontos.
val customerDataSavings : MutableList<String> = mutableListOf()

// Register über die Tagesgeldkonten.
val registerDepositAccount : MutableMap<String, MutableList<String>> = mutableMapOf()

// Auflistung aller Daten innerhalb eines Sparkontos.
val customerDataDeposit : MutableList<String> = mutableListOf()

//! Hilfsfunktionen

// Funktion, die kontrolliert, ob bereits in einem Register für alle Kunden der Name des Kunden/der Kundin auftaucht und dann gegebenenfalls die Liste von den bestehenden Kunden mit dem Namen ausgibt.
fun filterCustomers(name : String, register : MutableMap<String, MutableList<String>>) : List<String>
{
	return try
	{
		register.filterKeys { it.startsWith(name) }.keys.toList()
	}
	catch (e : Exception)
	{
		val message : List<String> = listOf("There is no $name in the Register $register.")
		message
	}
}

// Hilfsfunktion, die überprüft, ob es einen Kunden gibt oder nicht.
fun isCustomer(name : String, register : MutableMap<String, MutableList<String>> = registerAllAccounts) : Boolean
{
	val registerCustomerKeys = filterCustomers(name, register)
	return registerCustomerKeys.isNotEmpty()
}

// Hilfsfunktion, die überprüft, ob es mehr Kunden mit dem gleichen Namen in dem geprüften Register gibt.
fun areMoreCustomers(name : String, register : MutableMap<String, MutableList<String>>) : Boolean
{
	val registerCustomerKeys = filterCustomers(name, register)
	return registerCustomerKeys.size > 1
}

// Hilfsfunktion, die überprüft, ob es den exakten Kunden im überprüften Register gibt.
fun isExactCustomer(name : String, register : MutableMap<String, MutableList<String>>) : Boolean
{
	val registerCustomerKeys = filterCustomers(name, register)
	return registerCustomerKeys.contains(name)
}

// Hilfsfunktion, die den richtigen Kunden findet, wenn mehrere Kunden den gleichen Namen haben.
fun whichCustomer(name : String, register : MutableMap<String, MutableList<String>>) : String
{
	val registerCustomerKeys = filterCustomers(name, register)
	println(
			"""
			We have several customers with the name $name.
			Which one are you?
			$registerCustomerKeys
		""".trimMargin()
	       )
	return readln()
}

// Hilfsfunktion, die Beträge auf 2 Stellen hinter dem Komma/Punkt rundet.
fun roundBalance(balance : Double) : Double
{
	val roundedBalance : String = "%.2f".format(balance)
	return roundedBalance.replace(',', '.').toDouble()
}

// Hilfsfunktion, die den neuen Kontostand berechnet.
fun calNewBalance(
		name : String,
		register : MutableMap<String, MutableList<String>>,
		money : Double,
		isAdd : Boolean,
                 ) : String
{
	var currentBalance : String = register[name]?.get(3) !!
	// Überprüft, ob Geld hinzugefügt werden soll
	if (isAdd)
	{
		
		var newBalance : Double = currentBalance.toDouble() + money
		newBalance = roundBalance(newBalance)
		currentBalance = newBalance.toString()
		return currentBalance
	}
	// Überprüft, ob Geld abgezogen werden soll
	else
	{
		var newBalance : Double = currentBalance.toDouble() - money
		newBalance = roundBalance(newBalance)
		currentBalance = newBalance.toString()
		return currentBalance
	}
}

// Hilfsfunktion, die im Boolean herausgibt, ob der exakte Name bereits im Sparkontenregister existiert.
fun isSavings(name : String, register : MutableMap<String, MutableList<String>> = registerSavingsAccount) : Boolean
{
	return name in register
}

// Hilfsfunktion, die im Boolean herausgibt, ob der exakte Name bereits im Tageskontenregister existiert.
fun isDeposit(name : String, register : MutableMap<String, MutableList<String>> = registerDepositAccount) : Boolean
{
	return name in register
}

// Hilfsfunktion, die im Boolean herausgibt, ob der exakte Name wirklich zu einem Bankmitarbeiter gehört.
fun isEmployee(name : String, register : MutableMap<String, MutableList<String>> = bankEmployees) : Boolean
{
	return name in register
}

// Hilfsfunktion, die im Boolean herausgibt, ob Bankangestellter eingeloggt ist oder nicht.
fun isLoggedIn(name : String, register : MutableMap<String, MutableList<String>> = bankEmployees) : Boolean
{
	val logInState : String = register[name]?.get(1) !!
	return logInState == "eingeloggt"
}

// Hilfsfunktion, die kontrolliert, in welchem Register der Name des Kunden/der Kundin existiert.
fun filterAccountTypes(name : String, register : MutableMap<String, String>)
{
	println(
			"""Es existieren folgende Arten von Konten unter dem Namen $name:
		${register[name]}""".trimMargin()
	       )
}

// Hilfsfunktion, die überprüft, ob man genug Geld auf seinem Konto hat.
fun isEnoughMoneySavings(name : String, money : Double, register : MutableMap<String, MutableList<String>>) : Boolean
{
	val currentBalance : String = register[name]?.get(3) !!
	return currentBalance.toDouble() >= money
}

//! Hauptfunktionen
// Funktion, die Mitarbeiter einloggt.
fun loginEmployee(name : String, password : String, register : MutableMap<String, MutableList<String>>) : String
{
	val employeeDataPassword : String = register[name]?.get(0) !!
	var logInState : String = register[name]?.get(1) !!
	println("Please type the name of the employee to login.")
	val nameEntered : String = readln()
	if (isEmployee(nameEntered) && ! isLoggedIn(nameEntered))
	{
		println("Please enter your password.")
		val passwordEntered : String = readln()
		return if (passwordEntered == employeeDataPassword)
		{
			println("Now you're logged in.")
			logInState = "logged in"
			logInState
		}
		else
		{
			println("You're password was wrong")
			logInState
		}
	}
	else if (isEmployee(nameEntered) && isLoggedIn(nameEntered))
	{
		println("You are already logged in.")
		return logInState
	}
	else
	{
		println("No employee with the name $nameEntered works here.")
		return "error"
	}
}

//

// Funktion, die Mitarbeiter ausloggt.
fun logOutEmployee(name : String, password : String, register : MutableMap<String, MutableList<String>>) : String
{
	val employeeDataPassword : String = register[name]?.get(0) !!
	var logInState : String = register[name]?.get(1) !!
	println("Please type the name of the employee, you want to log out.")
	val nameEntered : String = readln()
	return if (isEmployee(nameEntered) && isLoggedIn(nameEntered))
	{
		println("Now you're logged out.")
		logInState = "logged out"
		logInState
	}
	else if (isEmployee(nameEntered) && ! isLoggedIn(nameEntered))
	{
		println("You are already logged out.")
		logInState
	}
	else
	{
		println("No employee with the name $nameEntered works here.")
		"error"
	}
}

// Diese Funktion fügt einen neuen Kunden dem Gesamtregister hinzu.
fun addNewCustomer(nameEmployee : String) : MutableMap<String, MutableList<String>> {
	if (isLoggedIn(nameEmployee))
	{
		println(
				"""Hi, I am $nameEmployee and I am responsible to register new customers.
		To create an account for you, please tell me your full name first.
	""".trimMargin()
		       )
		var nameCustomer : String = readln()
		return if (isCustomer(nameCustomer)) {
			val listOfKeysSize = filterCustomers(nameCustomer, registerAllAccounts).size
			nameCustomer = "$nameCustomer $listOfKeysSize"
			println("""Because we already have customers with your name, I register your account with the name $nameCustomer.
				Later, I will register your type of account.
			""".trimMargin())
			registerAllAccounts[nameCustomer] = mutableListOf<String>()
			registerAllAccounts
		} else {
			registerAllAccounts[nameCustomer] = mutableListOf<String>()
			registerAllAccounts
			
		}
	}
	
	return registerAllAccounts
}

// Diese Funktion erstellt für einen Bestandskunden ein Sparkonto, wenn er noch keines hat.
fun addNewSavingsAccount(nameEmployee : String) : MutableMap<String, MutableList<String>>
{
	if (isLoggedIn(nameEmployee))
	{
		println(
				"To create a saving account for you, please tell me the name first which is used to register your account."
		       )
		val nameCustomer : String = readln()
		if (isExactCustomer(nameCustomer, registerAllAccounts) && ! isSavings(nameCustomer))
		{
			println("What do you want to use as your username for your e-banking account?")
			val userName : String = readln()
			println("The username you chose was $userName.")
			println("What do you want to use as your password for your e-banking account?")
			val password : String = readln()
			println("How much money do you want to save initially? Just write the amount like this: 123.93")
			val initialBalance : String = readln()
			println("You want to save $initialBalance initially.")
			return try
			{
				registerSavingsAccount[nameCustomer] = mutableListOf(userName, password, initialBalance, initialBalance)
				registerSavingsAccount
			}
			catch (e : Exception)
			{
				println("An error occurred. Please try again.")
				registerSavingsAccount
			}
		} else if (isExactCustomer(nameCustomer, registerAllAccounts) && isSavings(nameCustomer)) {
			println("You already have a saving account.")
			return registerSavingsAccount
		} else {
			println("You are no customer yet. Please become a customer first.")
			return registerSavingsAccount
		}
	}
	else
	{
		println(
				"""I am sorry, currently is no service man for the saving accounts available.
			Please come again later.
		""".trimMargin()
		       )
		return registerSavingsAccount
	}
}

// Funktion, die Bankkonten zum Gesamtregister hinzufügt.
fun addAccountTypes(name : String, register : MutableMap<String, MutableList<String>> = registerAllAccounts) : MutableMap<String, MutableList<String>> {
	val accountTypes = registerAllAccounts[name]
	return if (isCustomer(name) && ! accountTypes!!.contains("Deposit Account") && isDeposit(name) && ! accountTypes.contains("Saving Account") && isSavings(name)) {
		accountTypes.add("Deposit Account")
		accountTypes.add("Saving Account")
		registerAllAccounts
	} else if (isCustomer(name) && ! accountTypes!!.contains("Deposit Account") && isDeposit(name)) {
		accountTypes.add("Deposit Account")
		registerAllAccounts
	} else if (isCustomer(name) && ! accountTypes!!.contains("Saving Account") && isSavings(name)) {
		accountTypes.add("Saving Account")
		registerAllAccounts
	} else {
		println("All account types already registered")
		registerAllAccounts
	}
}

fun main()
{
}
