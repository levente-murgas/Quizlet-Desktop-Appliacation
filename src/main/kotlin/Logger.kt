import java.io.File

class Logger(private val log: MutableList<String> = mutableListOf()){

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

}