jNtccFiles
==========
The jNtcc virtual machine is a Java tool for the Ntcc [1] calculus, which allows to observe the execution of modeled systems through the trace evolution.
For its construction more than five thousand code lines excluding Parser and Lex were written. It consists of a parametric design that allows exchanging engine constraints, an flexible design, to add new operators without altering the functionality of the virtual machine.
It has a high-level language with a semantics that includes all operators of Ntcc[1] calculus, including its variants Bang and Star. Also it does not use any random or probabilistic approach to the operator Choice, because selection is performed based on the non-determinism of Java[2] threads.
