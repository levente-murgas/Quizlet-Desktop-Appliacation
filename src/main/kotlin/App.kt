class App {

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

    fun runApp(){
        var exit = false

        while(!exit) {
            logger.logOutput("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):")
            when(logger.logInput()){
                "add" -> addCard()
                "remove" -> removeCard()
                "import" -> importCards()
                "export" -> exportCards()
                "ask" -> askCard()
                "exit" -> {
                    exit = true
                    logger.logOutput("Bye bye!")
                    exportCards(exiting = true)
                }
                "log" -> logger.saveLog()
                "hardest card" -> hardestCardFunc()
                "reset stats" -> resetStats()
                else -> {
                    logger.logOutput("Not an action name!")
                }
            }
        }
    }
}