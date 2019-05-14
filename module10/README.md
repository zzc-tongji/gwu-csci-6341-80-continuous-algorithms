# Module 10

### 02

> Why is this true? What is the minimum (unconstrained) value of $f(x,y)=3x+4y$?

For a "linear objective, unconstrained variables" problem, the minimum value must be $+\infty$ and the maximum valus must be $-\infty$.

The minimum (unconstrained) value of $f(x,y)=3x+4y$ is $-\infty$ (not existent in general).

### 03

> Go back to your calc book and find an example of a "hard to differentiate" function.

$$
\begin{align*}
f(x)&=\ln(x)+\cos(x)\\
\end{align*}
$$

### 04

> What's an example of a function that's continuous but not differentiable? Consider the (weird) function $f(x)$ where $f(x)=1$ if $x$ is rational and $f(x)=0$ otherwise. Is this continuous? Differentiable?

 One example:
$$
\begin{align*}
g(x)&=\lvert x\rvert\\
\end{align*}
$$
It is continuous but not differentiable when $x=0$.

Function $f(x)$ (Dirichlet function) is netither continuous nor differentiable.

### 05

> Consider the function:
> $$
> \begin{align*}
> f(x)&=\frac{x}{\mu_{1}-\lambda x}+\frac{1-x}{\mu_{2}-\lambda(1-x)}\\
> \end{align*}
> $$
> Compute the derivative $f′(x)$. Can you solve $f′(x)=0$?

Get derivative $f′(x)$:
$$
\begin{align*}
f(x)&=\frac{x}{\mu_{1}-\lambda x}+\frac{1-x}{\mu_{2}-\lambda(1-x)}\\
f'(x)&=\frac{\mu_{1}}{x^{2}}-\frac{\mu_{2}}{(1-x)^{2}}
\end{align*}
$$
Calculate $f′(x)=0$:
$$
\begin{align*}
&f'(x)=0\Longrightarrow\frac{\mu_{1}}{x^{2}}=\frac{\mu_{2}}{(1-x)^{2}}\\
&\Longrightarrow(1-x)^{2}\mu_{1}=\mu_{2}x^{2}&(x\neq0\;,\;x\neq1)\\
&\Longrightarrow(\mu_{2}-\mu_{1})x^{2}+2\mu_{1}x-\mu_{1}=0&(x\neq0\;,\;x\neq1)\\
\end{align*}
$$
So:
$$
\begin{align*}
x=\left\{
\begin{array}{**lr**}
\dfrac{1}{2}&(\mu_{1}=\mu_{2})\\
\dfrac{-\mu_{1}\pm \sqrt{\mu_{1}^{2}-\mu_{1}(\mu_{2}-\mu_{1})}}{\mu_{2}-\mu_{1}}&(\mu_{1}\neq \mu_{2}\;,\;\mu_{1}^{2}-\mu_{1}(\mu_{2}-\mu_{1})\ge0\;,\;x\neq0\;,\;x\neq1)\\
\end{array}
\right.
\end{align*}
$$

### 06

> Download and execute [BracketSearch.java](https://www2.seas.gwu.edu/~simhaweb/contalg/modules/module10/examples/BracketSearch.java).
>
> - What is the running time in terms of $M$ and $N$?
> - If we keep $MN$ constant (e.g., $MN=24$), what values of $M$ and $N$ produce best results?

See file `BracketSearch.java`.

Result:

```
a=4.560280445054108 b=4.950464868160342 bestf=2.502058677967597
```

The running times is $MN$.

$M=6\;,\;N=4$ will get the best result:

```
a=4.629629629629631 b=4.753086419753088 bestf=2.500347523243408
```

### 07

> Draw an example of a function for which bracket-search fails miserably, that is, the true minimum is much lower than what's found by bracket search even for large $M$ and $N$.

$$
\begin{align*}
f(x)=x\sin(100x);
\end{align*}
$$

### 08

> What is the number of function evaluations in terms of $M$ and $N$ for the bracket-search algorithm?

The time complexity is $O(MN)$.

### 09

> What is the number of function evaluations in terms of $M$ and $N$ for the 2D bracket-search algorithm? How does this generalize to $n$ dimensions?

The time complexity of 2D bracket-search is $O(M^{2}N)$.

The time complexity of n-D bracket-search is $O(M^{n}N)$.

### 10

> Add code to [MultiBracketSearch.java](https://www2.seas.gwu.edu/~simhaweb/contalg/modules/module10/examples/MultiBracketSearch.java) to find the minimum of $f(x_{1},x_{2})=(x_{1}-4.71)^{2} + (x_{2}-3.2)^{2} + 2(x_{1}-4.71)^{2}(x_{2}-3.2)^{2}$.

See file `MultiBracketSearch.java`.

Result:

```
Bracketing search: x1=4.691358024691359 x2=3.2098765432098757 numFuncEvals=138
```

### 11

> Modify [BracketSearch2.java](https://www2.seas.gwu.edu/~simhaweb/contalg/modules/module10/examples/BracketSearch2.java) to use the proportional-difference stopping condition.

See file `BracketSearch2.java`.

### 16

> Download and execute [GradientDemo.java](https://www2.seas.gwu.edu/~simhaweb/contalg/modules/module10/examples/GradientDemo.java).
>
> - How many iterations does it take to get close to the optimum?
> - What is the effect of using a small $\alpha$ (e.g, $\alpha=0.001$)?
> - In the method `nextStep()`, print out the current value of $x$, and the value of $xf′(x)$ before the update.
> - Set $\alpha=1$. Explain what you observe.
> - What happens when $\alpha=10$?

**1**

about 200 times

**2**

more iteration times needed

**3**

See file `GradientDemo.java`.

**4**

It doesn't work.

**5**

It doesn't work.

### 17

> Download [GradientDemo2.java](https://www2.seas.gwu.edu/~simhaweb/contalg/modules/module10/examples/GradientDemo2.java) and examine the function being optimized.
>
> - Fill in the code for computing the derivative.
> - Try an initial value of $x$ at $1.8$. Does it converge?
> - Next, try an initial value of $x$ at $5.8$. What is the gradient at the point of convergence?

See file `GradientDemo2.java`.

Both $x=1.8$ and $x=5.8$ converge and their gradient are $0$.