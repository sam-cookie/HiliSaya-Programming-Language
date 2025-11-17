package Evaluator
import parser.*
import scanner.*


// need to have class
// printing should not be inside the main function
fun main() {
    val evaluator = Evaluator()
    print("Hilisaya Programming Language")
    print("Type 'humana' to exit.\n") 
    while (true) {
        print("> ")
        val line = readLine() ?: break
        if (line.trim().isEmpty()) continue
        if (line.trim().lowercase() == "humana") break

        val scanner = Scanner(line)
        val tokens = scanner.scanTokens()

        val parser = Parser(tokens)
        val expression = parser.parse()

        val result = evaluator.evaluate(expression)

        if (result != null) println(result)
    }
}
