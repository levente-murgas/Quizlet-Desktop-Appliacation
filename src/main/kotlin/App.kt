class App {
    private var deck = Deck();
    private var importFileName = ""
    private var exportFileName = ""

    fun initApp(args: Array<String>){

        if (args.isNotEmpty()) {
            if(args.size == 2) { //only import or export
                when(args[0]) {
                    "-import" -> {
                        importFileName = args[1]
                        deck.importCards(importFileName)
                    }
                    "-export" -> exportFileName = args[1]
                }
            }
            else if(args.size == 4) { //import AND export
                when(args[0]) {
                    "-import" -> {
                        importFileName = args[1]
                        deck.importCards(importFileName)
                    }
                    "-export" -> exportFileName = args[1]
                }
                when(args[2]) {
                    "-import" -> {
                        importFileName = args[3]
                        deck.importCards(importFileName)
                    }
                    "-export" -> exportFileName = args[3]
                }
            }
        }
    }

    fun runApp(){
        var exit = false

        while(!exit) {
            logger.logOutput("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):")
            when(logger.logInput()){
                "add" -> deck.addCard()
                "remove" -> deck.removeCard()
                "import" -> deck.importCards(importFileName)
                "export" -> deck.exportCards(exiting = false,exportFileName)
                "ask" -> deck.askCard()
                "exit" -> {
                    exit = true
                    logger.logOutput("Bye bye!")
                    deck.exportCards(exiting = true,exportFileName)
                }
                "log" -> logger.saveLog()
                "hardest card" -> deck.hardestCardFunc()
                "reset stats" -> deck.resetStats()
                else -> {
                    logger.logOutput("Not an action name!")
                }
            }
        }
    }
}