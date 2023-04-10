/*! 1. Gedanke: Ich möchte ein einziges Register erschaffen, wo sämtliche kontobezogenen Daten pro Kunde einsehbar sind. Hierzu möchte ich eine MutableMap verwenden.
Dazu darf ich nicht vergessen, dass folgende Daten Strings sind: Kundenname, Account Name, Account Passwort, Überweisungsempfänger
während folgende Daten eigentlich Doubles sein sollten: initialer Kontostand, aktueller Kontostand, Ein- oder Auszahlungsbetrag, Überweisungsbetrag.
Weitere interessante Boolean Daten wären, ob ein Sparkonto das einzige Konto des Kunden/der Kundin ist oder ob er/sie noch weitere Konten bei der Bank hat und ob der Kunde gerade eingeloggt ist oder nicht.
Wir haben also verschiedene Datentypen, die wir als Value in der MutableMap abspeichern wollen, allerdings möchte ich ungern Unit als Datentypen hier verwenden, weshalb ich überlege, ob ich die Daten alle in Strings umwandle und sie dann erst in einer MutableList speichere.
Reihenfolge für die Values wäre möglicherweise so:
mutableListOf("Account Name", "Account Passwort", "Initialer Kontostand", "Aktueller Kontostand", "Eingeloggt?", "Ein- oder Auszahlungsbetrag (unterschieden mit +/-)", "Überweisung: Überweisungsempfänger und Betrag") -> Dann kann ich über den Index auf die ersten 5 Daten sicher zugreifen, während ich die anderen Daten über den String-Anfang filtere und dabei herausfinden kann, ob eine Ein- oder Auszahlung oder eine Überweisung stattgefunden hat.
 */
//! Variablen, die in allen Funktionen benötigt werden
// Register der Bankangestellten.
val bankEmployees : MutableMap<String, MutableList<String>> = mutableMapOf(
		"Hannah Arendt" to mutableListOf("Passwort123456789", "logged in"),
		"Bernd Depp" to mutableListOf("1234567890", "logged out"),
                                                                          )

// Register über alle Konten, wo abgeprüft wird, was für Bankkontotypen pro Kund*in geführt werden.
val registerAllAccounts : MutableMap<String, MutableList<String>> = mutableMapOf()

// Register über die Sparkonten.
val registerSavingAccounts : MutableMap<String, MutableList<String>> = mutableMapOf()

// Register über die Tagesgeldkonten.
val registerDepositAccounts : MutableMap<String, MutableList<String>> = mutableMapOf()

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
	var currentBalance : String = register[name] !![3]
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
fun isSavings(name : String, register : MutableMap<String, MutableList<String>> = registerSavingAccounts) : Boolean
{
	return name in register
}

// Hilfsfunktion, die im Boolean herausgibt, ob der exakte Name bereits im Tageskontenregister existiert.
fun isDeposit(name : String, register : MutableMap<String, MutableList<String>> = registerDepositAccounts) : Boolean
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
	val logInState : String = register[name] !![1]
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
fun isEnoughMoneySavings(
		name : String,
		money : Double,
		register : MutableMap<String, MutableList<String>> = registerSavingAccounts,
                        ) : Boolean
{
	val currentBalance : Double = register[name] !![3].toDouble()
	return currentBalance >= money
}

//! Hauptfunktionen
// Funktion, die Mitarbeiter einloggt.
fun loginEmployee(register : MutableMap<String, MutableList<String>> = bankEmployees) : String
{
	println("Please type the name of the employee to login.")
	val nameEntered : String = readln()
	val employeeDataPassword : String = register[nameEntered]?.get(0) !!
	var logInState : String = register[nameEntered]?.get(1) !!
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
fun addNewCustomer(nameEmployee : String) : MutableMap<String, MutableList<String>>
{
	if (isEmployee(nameEmployee) && isLoggedIn(nameEmployee))
	{
		println(
				"""Hi, I am $nameEmployee and I am responsible to register new customers.
		To create an account for you, please tell me your full name first.
	""".trimMargin()
		       )
		var nameCustomer : String = readln()
		return if (isCustomer(nameCustomer))
		{
			val listOfKeysSize = filterCustomers(nameCustomer, registerAllAccounts).size
			nameCustomer = "$nameCustomer $listOfKeysSize"
			println(
					"""Because we already have customers with your name, I register your account with the name $nameCustomer.
				Later, I will register your type of account.
			""".trimMargin()
			       )
			registerAllAccounts[nameCustomer] = mutableListOf<String>()
			registerAllAccounts
		}
		else
		{
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
				registerSavingAccounts[nameCustomer] = mutableListOf(userName, password, initialBalance, initialBalance)
				registerSavingAccounts
			}
			catch (e : Exception)
			{
				println("An error occurred. Please try again.")
				registerSavingAccounts
			}
		}
		else if (isExactCustomer(nameCustomer, registerAllAccounts) && isSavings(nameCustomer))
		{
			println("You already have a saving account.")
			return registerSavingAccounts
		}
		else
		{
			println("You are no customer yet. Please become a customer first.")
			return registerSavingAccounts
		}
	}
	else
	{
		println(
				"""I am sorry, currently is no service man for the saving accounts available.
			Please come again later.
		""".trimMargin()
		       )
		return registerSavingAccounts
	}
}

// Funktion, die Bankkonten zum Gesamtregister hinzufügt.
fun addAccountTypes(
		name : String,
		register : MutableMap<String, MutableList<String>> = registerAllAccounts,
                   ) : MutableMap<String, MutableList<String>>
{
	val accountTypes = register[name]
	return if (isCustomer(name) && ! accountTypes !!.contains("Deposit Account") && isDeposit(name) && ! accountTypes.contains(
					"Saving Account"
	                                                                                                                          ) && isSavings(
					name
	                                                                                                                                        )
	)
	{
		accountTypes.add("Deposit Account")
		accountTypes.add("Saving Account")
		registerAllAccounts
	}
	else if (isCustomer(name) && ! accountTypes !!.contains("Deposit Account") && isDeposit(name))
	{
		accountTypes.add("Deposit Account")
		registerAllAccounts
	}
	else if (isCustomer(name) && ! accountTypes !!.contains("Saving Account") && isSavings(name))
	{
		accountTypes.add("Saving Account")
		registerAllAccounts
	}
	else
	{
		println("All account types already registered")
		registerAllAccounts
	}
}

// Funktion, die Bankkonten zum Gesamtregister hinzufügt.
fun removeAccountTypes(
		name : String,
		register : MutableMap<String, MutableList<String>> = registerAllAccounts,
                      ) : MutableMap<String, MutableList<String>>
{
	val accountTypes : MutableList<String>? = register[name]
	val isNoSaving : Boolean = ! registerSavingAccounts.contains(name)
	val isNoDeposit : Boolean = ! registerDepositAccounts.contains(name)
	val registerValue : String = register.values.toString()
	val containsSaving : Boolean = registerValue.contains("Saving Account")
	val containsDeposit : Boolean = registerValue.contains("Deposit Account")
	return if (isCustomer(name) && isNoSaving && isNoDeposit && containsSaving && containsDeposit)
	{
		try
		{
			accountTypes !!.remove("Saving Account")
			accountTypes.remove("Deposit Account")
			return registerAllAccounts
		}
		catch (e : Exception)
		{
			println("Error occurred")
			return registerAllAccounts
		}
	}
	else if (isCustomer(name) && ! isNoSaving && containsSaving)
	{
		try
		{
			accountTypes !!.remove("Deposit Account")
			return registerAllAccounts
		}
		catch (e : Exception)
		{
			println("Error occurred")
			return registerAllAccounts
		}
	}
	else if (isCustomer(name) && ! isNoDeposit && containsDeposit)
	{
		try
		{
			accountTypes !!.remove("Saving Account")
			return registerAllAccounts
		}
		catch (e : Exception)
		{
			println("Error occurred")
			return registerAllAccounts
		}
	}
	else
	{
		println("All account types already registered")
		registerAllAccounts
	}
}

// Funktion zum Löschen von Kundensparkonten
fun deleteCustomerSaving(nameEmployee : String) : MutableMap<String, MutableList<String>>
{
	if (isEmployee(nameEmployee) && isLoggedIn(nameEmployee))
	{
		println(
				"""Hello, my name is $nameEmployee.
			Which saving account do you wish to delete?
		""".trimMargin()
		       )
		val nameAccount : String = readln()
		if (isCustomer(nameAccount) && ! areMoreCustomers(nameAccount, registerSavingAccounts))
		{
			if (isSavings(nameAccount))
			{
				registerSavingAccounts.remove(nameAccount)
				return registerSavingAccounts
			}
		}
		else if (isCustomer(nameAccount) && areMoreCustomers(nameAccount, registerSavingAccounts))
		{
			val nameAccountNew = whichCustomer(nameAccount, registerSavingAccounts)
			if (isSavings(nameAccountNew))
			{
				registerSavingAccounts.remove(nameAccountNew)
				return registerSavingAccounts
			}
		}
		else
		{
			println("You are no customer yet.")
			return registerSavingAccounts
		}
	}
	else if (isEmployee(nameEmployee) && ! isLoggedIn(nameEmployee, bankEmployees))
	{
		loginEmployee()
		println(
				"""Hello, my name is $nameEmployee.
			Which saving account do you wish to delete?
		""".trimMargin()
		       )
		val nameAccount : String = readln()
		if (isCustomer(nameAccount) && ! areMoreCustomers(nameAccount, registerSavingAccounts))
		{
			if (isSavings(nameAccount))
			{
				registerSavingAccounts.remove(nameAccount)
				return registerSavingAccounts
			}
		}
		else if (isCustomer(nameAccount) && areMoreCustomers(nameAccount, registerSavingAccounts))
		{
			val nameAccountNew = whichCustomer(nameAccount, registerSavingAccounts)
			return if (isSavings(nameAccountNew))
			{
				registerSavingAccounts.remove(nameAccountNew)
				registerSavingAccounts
			}
			else
			{
				println("You are no customer yet.")
				registerSavingAccounts
			}
		}
	}
	else
	{
		println("No employee active.")
		return registerSavingAccounts
	}
	return registerSavingAccounts
}

// Funktion zum Löschen von Kundenkonten
fun deleteCustomer(nameEmployee : String) : MutableMap<String, MutableList<String>>
{
	if (isEmployee(nameEmployee) && isLoggedIn(nameEmployee))
	{
		println(
				"""Hello, my name is $nameEmployee.
			Which saving account do you wish to delete?
		""".trimMargin()
		       )
		val nameAccount : String = readln()
		if (isCustomer(nameAccount) && ! areMoreCustomers(nameAccount, registerAllAccounts))
		{
			registerAllAccounts.remove(nameAccount)
			return registerAllAccounts
		}
		else if (isCustomer(nameAccount) && areMoreCustomers(nameAccount, registerAllAccounts))
		{
			val nameAccountNew = whichCustomer(nameAccount, registerAllAccounts)
			if (isSavings(nameAccountNew))
			{
				registerSavingAccounts.remove(nameAccountNew)
				return registerAllAccounts
			}
		}
		else
		{
			println("You are no customer yet.")
			return registerSavingAccounts
		}
	}
	else if (isEmployee(nameEmployee) && ! isLoggedIn(nameEmployee, bankEmployees))
	{
		loginEmployee()
		println(
				"""Hello, my name is $nameEmployee.
			Which saving account do you wish to delete?
		""".trimMargin()
		       )
		val nameAccount : String = readln()
		if (isCustomer(nameAccount) && ! areMoreCustomers(nameAccount, registerSavingAccounts))
		{
			if (isSavings(nameAccount))
			{
				registerSavingAccounts.remove(nameAccount)
				return registerSavingAccounts
			}
		}
		else if (isCustomer(nameAccount) && areMoreCustomers(nameAccount, registerSavingAccounts))
		{
			val nameAccountNew = whichCustomer(nameAccount, registerAllAccounts)
			return if (isSavings(nameAccountNew))
			{
				registerSavingAccounts.remove(nameAccountNew)
				registerSavingAccounts
			}
			else
			{
				println("You are no customer yet.")
				registerSavingAccounts
			}
		}
	}
	else
	{
		println("No employee active.")
		return registerSavingAccounts
	}
	return registerSavingAccounts
}

// Funktion über Geld Einzahlungen.
fun addMoneySavings(
		register : MutableMap<String, MutableList<String>> = registerSavingAccounts,
		isAdd : Boolean = true,
                   ) : MutableMap<String, MutableList<String>>
{
	println("Under which name is your account registered?")
	val name : String = readln()
	println(
			"""How much money do you want to add to your saving account?
		Please write it in the Format: 12345.12""".trimMargin()
	       )
	val money : Double = readln().toDouble()
	return try
	{
		val newBalance = calNewBalance(name, register, money, isAdd)
		registerSavingAccounts[name] !!.add("+ $newBalance")
		println("$newBalance € were added to the account of $name")
		registerSavingAccounts
	}
	catch (e : Exception)
	{
		println("An error occurred.")
		registerSavingAccounts
	}
}

// Funktion über Geld Auszahlungen.
fun substractMoneySavings(
		register : MutableMap<String, MutableList<String>> = registerSavingAccounts,
		isAdd : Boolean = false,
                         ) : MutableMap<String, MutableList<String>>
{
	println("Under which name is your account registered?")
	val name : String = readln()
	println(
			"""How much money do you want to take out from your saving account?
		Please write it in the Format: 12345.12""".trimMargin()
	       )
	val money : Double = readln().toDouble()
	return try
	{
		val newBalance = calNewBalance(name, register, money, isAdd)
		registerSavingAccounts[name] !!.add("- $newBalance")
		println("$newBalance € were taken from the account of $name")
		registerSavingAccounts
	}
	catch (e : Exception)
	{
		println("An error occurred.")
		registerSavingAccounts
	}
}

// Funktion für Überweisungen.
fun transferSaving() : MutableMap<String, MutableList<String>>
{
	println("What is your name?")
	val nameSender : String = readln()
	if (isSavings(nameSender) && ! areMoreCustomers(nameSender, registerSavingAccounts))
	{
		println("Who do you want to send money?")
		val nameReceiver : String = readln()
		if (isSavings(nameReceiver) && ! areMoreCustomers(nameReceiver, registerSavingAccounts))
		{
			println(
					"""How much money do you want to send $nameReceiver?
				Please write in the format: 123.54
			""".trimMargin()
			       )
			val money : Double = readln().toDouble()
			return if (isEnoughMoneySavings(nameSender, money))
			{
				try
				{
					calNewBalance(nameSender, registerSavingAccounts, money, false)
					registerSavingAccounts[nameSender] !!.add("Überweisung an: $nameReceiver, Geldbetrag: $money €")
					calNewBalance(nameReceiver, registerSavingAccounts, money, true)
					registerSavingAccounts[nameSender] !!.add("Überweisung von: $nameSender, Geldbetrag: $money €")
					registerSavingAccounts
				}
				catch (e : Exception)
				{
					println("An error occurred.")
					registerSavingAccounts
				}
			}
			else
			{
				println("You don't have enough money to send.")
				registerSavingAccounts
			}
		}
		else if (isSavings(nameReceiver) && areMoreCustomers(nameReceiver, registerSavingAccounts))
		{
			val nameReceiverNew : String = whichCustomer(nameReceiver, registerSavingAccounts)
			println(
					"""How much money do you want to send $nameReceiverNew?
				Please write in the format: 123.54
			""".trimMargin()
			       )
			val money : Double = readln().toDouble()
			return if (isEnoughMoneySavings(nameSender, money))
			{
				try
				{
					calNewBalance(nameSender, registerSavingAccounts, money, false)
					registerSavingAccounts[nameSender] !!.add("Überweisung an: $nameReceiverNew, Geldbetrag: $money €")
					calNewBalance(nameReceiverNew, registerSavingAccounts, money, true)
					registerSavingAccounts[nameSender] !!.add("Überweisung von: $nameSender, Geldbetrag: $money €")
					registerSavingAccounts
				}
				catch (e : Exception)
				{
					println("An error occurred.")
					registerSavingAccounts
				}
			}
			else
			{
				println("You don't have enough money to send.")
				registerSavingAccounts
			}
		}
		else
		{
			println("$nameReceiver seems to not be our customer yet")
			return registerSavingAccounts
		}
	}
	else if (isSavings(nameSender) && areMoreCustomers(nameSender, registerSavingAccounts))
	{
		val nameSenderNew = whichCustomer(nameSender, registerSavingAccounts)
		
		println("Who do you want to send money?")
		val nameReceiver : String = readln()
		if (isSavings(nameReceiver) && ! areMoreCustomers(nameReceiver, registerSavingAccounts))
		{
			println(
					"""How much money do you want to send $nameReceiver?
				Please write in the format: 123.54
			""".trimMargin()
			       )
			val money : Double = readln().toDouble()
			return if (isEnoughMoneySavings(nameSenderNew, money))
			{
				try
				{
					calNewBalance(nameSenderNew, registerSavingAccounts, money, false)
					registerSavingAccounts[nameSenderNew] !!.add("Überweisung an: $nameReceiver, Geldbetrag: $money €")
					calNewBalance(nameReceiver, registerSavingAccounts, money, true)
					registerSavingAccounts[nameSenderNew] !!.add("Überweisung von: $nameSender, Geldbetrag: $money €")
					registerSavingAccounts
				}
				catch (e : Exception)
				{
					println("An error occurred.")
					registerSavingAccounts
				}
			}
			else
			{
				println("You don't have enough money to send.")
				registerSavingAccounts
			}
		}
		else if (isSavings(nameReceiver) && areMoreCustomers(nameReceiver, registerSavingAccounts))
		{
			val nameReceiverNew : String = whichCustomer(nameReceiver, registerSavingAccounts)
			println(
					"""How much money do you want to send $nameReceiverNew?
				Please write in the format: 123.54
			""".trimMargin()
			       )
			val money : Double = readln().toDouble()
			return if (isEnoughMoneySavings(nameSender, money))
			{
				try
				{
					calNewBalance(nameSenderNew, registerSavingAccounts, money, false)
					registerSavingAccounts[nameSenderNew] !!.add("Überweisung an: $nameReceiverNew, Geldbetrag: $money €")
					calNewBalance(nameReceiverNew, registerSavingAccounts, money, true)
					registerSavingAccounts[nameSenderNew] !!.add("Überweisung von: $nameSenderNew, Geldbetrag: $money €")
					registerSavingAccounts
				}
				catch (e : Exception)
				{
					println("An error occurred.")
					registerSavingAccounts
				}
			}
			else
			{
				println("You don't have enough money to send.")
				registerSavingAccounts
			}
		}
		else
		{
			println("$nameReceiver seems to not be our customer yet")
			return registerSavingAccounts
		}
		
	}
	else
	{
		println("Seems like you don't have an account here yet.")
		return registerSavingAccounts
	}
}

fun main()
{
	loginEmployee()
	println(" ")
	addNewCustomer("Hannah Arendt")
	println(registerAllAccounts)
	println(" ")
	addNewSavingsAccount("Hannah Arendt")
	println(registerSavingAccounts)
	addAccountTypes("Hannah Arendt")
	println(registerSavingAccounts)
	println(" ")
	addMoneySavings()
	println(registerSavingAccounts)
	println(" ")
	substractMoneySavings()
	println(registerSavingAccounts)
	println(" ")
}
