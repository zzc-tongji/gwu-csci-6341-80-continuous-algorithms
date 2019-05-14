# Team Project Selection: Markov Chains

### Description

Markov chains are a fairly common, and relatively simple, way to statistically model random processes. They have been used in many different domains. Markov Chains are   conceptually quite intuitive, and are very accessible in that they can be implemented without the use of any advanced statistical or mathematical concepts. They are a great way to start learning about probabilistic modeling and data science techniques.

### Implementation

A Markov chain is a probabilistic automaton. The probability distribution of state transitions is typically represented as the Markov chain’s transition matrix. If the Markov chain has N possible states, the matrix will be an N x N matrix, such that entry (I, J) is the probability of transitioning from state I to state J. Additionally, the transition matrix must be a stochastic matrix, a matrix whose entries in each row must add up to exactly 1. This  makes complete sense, since each row represents its own probability distribution.

### Exercises

Use Java to imitate each state of the event and store the state in a specific matrix. Next, predict the future with given states and possibilities.