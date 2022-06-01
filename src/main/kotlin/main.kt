import java.io.File
import kotlin.random.Random

val flashcards = mutableMapOf<String,String>()
val cardsAndErrors = mutableMapOf<Pair<String,String>,Int>()
val log = mutableListOf<String>()
var importFileName = ""
var exportFileName = ""

fun <K, V> getKey(map: Map<K, V>, target: V): K? {
    for ((key, value) in map)
    {
        if (target == value) {
            return key
        }
    }
    return null
}

fun addNCard(index: Int){
    logOutput("Card #$index:")
    var newTerm = logInput()
    if(flashcards.containsKey(newTerm)) {
        var unique = false
        while (!unique) {
            logOutput("The term \"$newTerm\" already exists. Try again:")
            newTerm = logInput()
            if(!flashcards.containsKey(newTerm))
                unique = true
        }
    }
    logOutput("The definition for card #$index:")
    var newDefinition = logInput()
    if(flashcards.containsValue(newDefinition)){
        var unique = false
        while (!unique) {
            logOutput("The definition \"$newDefinition\" already exists. Try again:")
            newDefinition = logInput()
            if(!flashcards.containsValue(newDefinition))
                unique = true
        }
    }
    flashcards[newTerm] = newDefinition
}

fun addCard(){
    logOutput("Card :")
    var newTerm = logInput()
    if(flashcards.containsKey(newTerm)) {
        var unique = false
        while (!unique) {
            logOutput("The card \"$newTerm\" already exists.")
            return
            /*
            newTerm = logInput()
            if(!flashcards.containsKey(newTerm))
                unique = true
             */
        }
    }
    logOutput("The definition of the card:")
    var newDefinition = logInput()
    if(flashcards.containsValue(newDefinition)){
        var unique = false
        while (!unique) {
            logOutput("The definition \"$newDefinition\" already exists.")
            return
            /*
            newDefinition = logInput()
            if(!flashcards.containsValue(newDefinition))
                unique = true
             */
        }
    }

    flashcards[newTerm] = newDefinition
    cardsAndErrors[Pair(newTerm, flashcards[newTerm]!!)] = 0
    logOutput("The pair (\"$newTerm\":\"$newDefinition\") has been added.")
}

fun removeCard() {
    logOutput("Which card?")
    val term = logInput()
    if(flashcards.containsKey(term)){
        cardsAndErrors.remove(Pair(term, flashcards[term]))
        flashcards -= term
        logOutput("The card has been removed.")
    }
    else{
        logOutput("Can't remove \"$term\": there is no such card.")
    }
}

fun importCards(){
    var fileName = importFileName
    if(importFileName.isEmpty()) {
        logOutput("File name:")
        fileName = logInput()
    }
    val file = File(fileName)
    if(file.exists()){
        val lines = file.readLines()
        for(line in lines){
            var (originalCard,errors) = line.split("=") //(term, definition)=1
            var filteredCard = originalCard.filter { c -> c != '(' && c != ')' && c != ','} //term definition
            var (term, definition) = filteredCard.split(' ')
            if(flashcards.containsKey(term)){
                cardsAndErrors.remove(Pair(term, flashcards[term]))
                cardsAndErrors[Pair(term,definition)] = errors.toInt()
                flashcards.replace(term,definition)
            }
            else{
                flashcards[term] = definition
                cardsAndErrors[Pair(term, flashcards[term]!!)] = errors.toInt()
            }
        }
        logOutput("${lines.size} cards have been loaded.")

    } else logOutput("File not found.")

}

fun exportCards(exiting: Boolean = false){
    var fileName = exportFileName
    if(exportFileName.isEmpty() && !exiting) {
        logOutput("File name:")
        fileName = logInput()
    }
    val file = File(fileName)
    //val cards = flashcards.entries.joinToString(separator = "\n")
    val cardsAndErrors = cardsAndErrors.entries.joinToString(separator = "\n")
    if(file.exists() || fileName.isNotEmpty()) {
        file.writeText(cardsAndErrors)
        logOutput("${flashcards.size} cards have been saved.")
    }
}

fun askCard(){
    logOutput("How many times to ask?")
    val numOfCards = logInput().toInt()
    for(i in 1..numOfCards){
        val random = Random.nextInt(0, flashcards.size)
        var index = 0
        for (entry in flashcards) {
            if (index == random){
                logOutput("Print the definition of \"${entry.key}\":")
                var answer = logInput()
                if(answer == entry.value){
                    logOutput("Correct!")
                }
                else{
                    logOutput("Wrong. The right answer is \"${entry.value}\", " +
                            "but your definition is correct for \"${getKey(flashcards,answer)}\".")
                    val newErrorCount = cardsAndErrors[Pair(entry.key,entry.value)]?.plus(1)
                    if (newErrorCount != null) {
                        cardsAndErrors[Pair(entry.key,entry.value)] = newErrorCount.toInt()
                    }
                }
                break
            }
            index++
        }
    }
}

fun logInput(): String{
    val string = readln()
    log.add(string)
    return string
}

fun logOutput(str: String){
    log.add(str)
    println(str)
}

fun saveLog(){
    logOutput("File name:")
    val fileName = logInput()
    val file = File(fileName)
    val exportableLog = log.joinToString(separator = "\n")
    file.writeText(exportableLog)
    logOutput("The log has been saved.")
}

fun hardestCardFunc(){
    var highest = 0
    for (entry in cardsAndErrors) {
        if (entry.value > highest) {
            highest = entry.value
        }
    }
    if (highest == 0) {
        logOutput("There are no cards with errors.")
        return
    }
    else{
        val hardestCards = mutableListOf<String>()
        for (entry in cardsAndErrors) {
            if (entry.value == highest) {
                hardestCards.add("\"${entry.key.first}\"")
            }
        }
        if (hardestCards.size > 1){
            logOutput("The hardest cards are ${hardestCards.joinToString(separator = ", ")}. " +
                    "You have $highest errors answering them.")
        }
        else{
            logOutput("The hardest card is ${hardestCards.joinToString()}. You have $highest errors answering it.")
        }
    }
}

fun resetStats(){
    for (entry in cardsAndErrors){
        entry.setValue(0)
    }
    logOutput("Card statistics have been reset.")
}

fun initApp(args: Array<String>){
    if (args.isNotEmpty()) {
        if(args.size == 2) { //only import or export
            when(args[0]) {
                "-import" -> {
                    importFileName = args[1]
                    importCards()
                }
                "-export" -> exportFileName = args[1]
            }
        }
        else if(args.size == 4) { //import AND export
            when(args[0]) {
                "-import" -> {
                    importFileName = args[1]
                    importCards()
                }
                "-export" -> exportFileName = args[1]
            }
            when(args[2]) {
                "-import" -> {
                    importFileName = args[3]
                    importCards()
                }
                "-export" -> exportFileName = args[3]
            }
        }
    }
}

fun main(args: Array<String>) {
    initApp(args)

    var exit = false

    while(!exit) {
        logOutput("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):")
        when(logInput()){
            "add" -> addCard()
            "remove" -> removeCard()
            "import" -> importCards()
            "export" -> exportCards()
            "ask" -> askCard()
            "exit" -> {
                exit = true
                logOutput("Bye bye!")
                exportCards(exiting = true)
            }
            "log" -> saveLog()
            "hardest card" -> hardestCardFunc()
            "reset stats" -> resetStats()
            else -> {
                logOutput("Not an action name!")
            }
        }
    }
}