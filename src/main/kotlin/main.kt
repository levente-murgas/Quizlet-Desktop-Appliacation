import java.io.File
import kotlin.random.Random

val logger = Logger()


fun main(args: Array<String>) {
    val app = App()
    app.initApp(args)
    app.runApp()
}