fun main() {
    println("Welcome to Man og Language")
    while (true){
        val line = readLine()

        val scanner = Scanner(line)
        val tokens = scanner.scanTokens()

        for(i in tokens)
            println(i)
    }
}
