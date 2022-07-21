package eu.davidknotek.pomotodo.util

fun setPomodoroString(pomoCount: Int, pomoFinish: Int): String {
    var pomoString = ""

    for (i in 1..pomoFinish) {
        pomoString += "✅"
    }

    for (i in 1..pomoCount - pomoFinish) {
        pomoString += "\uD83D\uDFE9"
    }

    return pomoString
}