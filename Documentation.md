##Documentation for Stress Test Program

Program is written and tested on Apple M1 chip. 
The M1 is an ARM64 processor. 
"ARM64" represents the AArch64 state of the ARMv8-A architecture.
Basically ARM64 is shorthand for ARMv8-A 64 bits.

###Not all cores are equal

M1 features 4 big CPU cores and 4 smaller CPU cores, or known as the ARM.big.LITTLE technology. 
As the name suggest, there are 2 types of processors in the processing architecture. 

"Little" processor are designed for maximum power efficiency while "big" processors are designed 
to maximise performance.

This allows the big.LITTLE architecture to alternate processes based on their processing intensity.

Go to Activity Monitor -> Windows -> View CPU History will show that we have 8 cores:
    
    - core 1: Efficiency
    - core 2: Efficiency   
    - core 3: Efficiency
    - core 4: Efficiency
    - core 5: Performance
    - core 6: Performance
    - core 7: Performance
    - core 8: Performance

#### "yes" command
On Linux based OS system such as MacOS, the "yes" command outputs "y" continously until it's 
aborted.It output a STRING in a constant stream, if STRING isn't specified, it will output 'y' 
continously.

Synxtax

    yes [STRING]
    yes hello    - output 'hello' continously
    yes > /dev/null &   - start a yes process

    killall yes   - kill all yes command

yes will stream continously for a single core. To test 8 core, type
    
    yes hello 

8 times.

#### Multithreading
M1 doesn't support multithreading. Its thread to core ratio is 1:1.

The M1 has 8 instruction decoders while every Intel processor maxes out at 4. M1 uses a Reduced 
Instruction Set Computer (RISC) ISA, a fixed length instruction that each perform one operation.

This enables many more instructions in flight than on Intel. The M1 can process twice as many instructions per clock cycle than an x86 processor can.





