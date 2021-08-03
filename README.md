# Maths

An SC implementation of the MakeNoise Maths EuroRack module. Creates a logarithmic to exponential curve function that can be triggered or can oscillate. The function starts at 0, goes to 1, and returns to 0 on each loop or trigger.

There are two versions provided. For a more efficient algorithm, use the Faust implementation - Maths. The SC version is Maths2. Both are completely interoperable and should give mostly identical output. (Right now the EOC and EOR triggers are not exactly the same - Maths outputs single sample triggers. Maths2 correctly outputs PWM oscillators.)

To install, download the source code and place it in the Extensions folder or install the quark with Quarks.install("Maths").

The Faust Ugen is provided as a compiled macos scx file. The source code is provided and can be compiled for SC, PD, Max, or other environments using the FaustIDE online compiler.
