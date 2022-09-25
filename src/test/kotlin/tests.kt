import org.junit.jupiter.api.Test

class AutomataTests {
    @Test
    fun `DFA same as NFA`() {
        val n = 3
        val m = 2

        val q0 = listOf(0)
        val F = listOf(2).toSet()

        val automata = NFA(n, m, q0, F)

        automata.addTransition(0, 0, 1)
        automata.addTransition(1, 0, 2)

        val dfa = NFA_TO_DFA(automata)
        assert(
            dfa.sigma == listOf(
                DFA.Edge(0, 0, 1),
                DFA.Edge(1, 0, 2)
            )
        )
    }

    @Test
    fun `DFA not Same`() {
        val n = 3
        val m = 2

        val q0 = listOf(0)
        val F = listOf(2).toSet()

        val automata = NFA(n, m, q0, F)

        automata.addTransition(0, 0, 1)
        automata.addTransition(1, 1, 2)
        automata.addTransition(1, 0, 2)

        val dfa = NFA_TO_DFA(automata)
        assert(
            dfa.sigma == listOf(
                DFA.Edge(0, 0, 1),
                DFA.Edge(1, 0, 2),
                DFA.Edge(1, 1, 3)
            )
        )
    }
}