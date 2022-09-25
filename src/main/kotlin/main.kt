import java.io.*
import java.util.*

class NFA(
    n: Int,
    m: Int,
    val initialStates: List<Int>,
    val acceptedStates: Set<Int>
) {
    val sigma: Array<Array<MutableList<Int>>> =
        Array(n) { Array(m) { arrayListOf() } }

    fun addTransition(from: Int, symbol: Int, to: Int) = sigma[from][symbol].add(to)
}

class Node {
    val to = mutableMapOf<Int, Node>()
    var stateNumber: Int? = null
    var acceptedState = false
}

fun buildDFA(nfa: NFA, curState: Set<Int>): Node {
    val maps = mutableMapOf<Int, MutableSet<Int>>()
    for (state in curState) {
        for ((char, to) in nfa.sigma[state]) {
            if (char !in maps) {
                maps[char] = mutableSetOf(to)
            } else {
                maps[char]!!.add(to)
            }
        }
    }
    val curNode = Node()
    for (state in curState) {
        if (state in nfa.acceptedStates) {
            curNode.acceptedState = true
        }
    }
    for ((char, to) in maps) {
        curNode.to[char] = buildDFA(nfa, to)
    }
    return curNode
}

class DFA {
    data class Edge(val from : Int, val symbol : Int, val to: Int)

    var initialState = 0
    val acceptedStates = mutableSetOf<Int>()
    val sigma = mutableListOf<Edge>()

    fun addTransition(from: Int, symbol: Int, to: Int) {
        sigma.add(Edge(from, symbol, to))
    }
}

fun NFA_TO_DFA(nfa: NFA): DFA {
    val dfa = DFA()
    val result = buildDFA(nfa, nfa.initialStates.toSet())
    val q: Queue<Node> = LinkedList()
    q.add(result)
    var last = 0
    while (q.isNotEmpty()) {
        val curNode = q.element()
        q.poll()
        if (curNode.stateNumber == null) {
            curNode.stateNumber = last++
        }
        if (curNode.acceptedState) {
            dfa.acceptedStates.add(curNode.stateNumber!!)
        }
        curNode.to.forEach { (c, node) ->
            if (node.stateNumber == null) {
                node.stateNumber = last++
            }
            dfa.addTransition(curNode.stateNumber!!, c, node.stateNumber!!)
            q.add(node)
        }
    }
    return dfa
}

fun main(args: Array<String>) {
    val file = BufferedReader(FileReader("input.txt"))
    val n = file.readLine()!!.toInt()
    val m = file.readLine()!!.toInt()

    val q0 = file.readLine()!!.split(" ").map { it.toInt() }
    val F = file.readLine()!!.split(" ").map { it.toInt() }.toHashSet()

    val automata = NFA(n, m, q0, F)

    val lines = file.readLines()
    for (i in 0 until lines.size - 1) {
        val (from, symbol, to) = lines[i].split(" ").map { it.toInt() }
        automata.addTransition(from, symbol, to)
    }
    val input = lines.last()

    val dfa = NFA_TO_DFA(automata)
    val output = BufferedWriter(FileWriter("output.txt"))

    dfa.sigma.forEach {
        output.write("${1} ${2} ${3}".format(it.from, it.symbol, it.to))
        output.newLine()
    }
}