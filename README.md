# CS1632-Deliverable6
## Red-Yellow-Green Report  
   | **Subsystems**       | Status  | Description |
   |----------------------|:-------------|-------------|
   | REPL                 | GREEN        | The REPL evaluator works as expected and passes all Unit Tests. I have no reasonable concerns.                            |
   | FILE                 | GREEN        | The FILE evaluator works as expected and has passed all tests (unit, system, and manual).  I have no reasonable concerns. |
   | ErrorReporting       | YELLOW       | The error reporting system could use some refactoring work, as it is somewhat clunky and might have issues with future maintainability.  This is not a major concern at this time.
   | FileConcatenator     | YELLOW       | File Concatenation functions properly.  I have slight concerns about the performance, as it is very heavy on memory.  This leaves an implicit maximum size on files to be read in.  The maximum is incredibly large, so it is not a large concern at this time.  
   | ExpressionEvaluation | GREEN        | Expression Evaluation functions as expected with proper handling of errors and exceptions regarding stack operations.  A look at performance optimization would be nice, but is certainly not required.  

## Testing Strategies
 1. **What kinds of tests did you write? Why?**  
    First and foremost, I wrote a lot of Unit Tests.  There are many potential failure points throughout this system, and I wanted to be sure that each subsystem (Statement, Expression, FileConcatenator) was able to handle the errors I would expect from those sections.  This however, was not enough by itself.  Those individual subsystems may be able to handle the errors themselves, but assuring that they would report them properly was a separate issue that I would say falls in the realm of Integration Testing.  

    I did both manual and automatic Integration Testing for this project.  The manual integration testing was important because I needed to see firsthand that the code was working in unison as I thought it would.  Since, I began the project by creating the REPL mode evaluator and then added the FILE mode evaluator, this was also a prime opportunity to allow my Unit Tests to handle most of the automated process.  By adding a few asserts to my existing unit tests, I could verify that new additions did not cause regression failures throughout the system. By and large the Integration Testing I performed was really just running the older versions of the Unit Tests and Manual Tests, with a few updates for the specific FILE architecture.  

    Because the REPL mode is fairly UI heavy, I decided that at least some manual tests would be required for that section.  Most of my manual tests are based on the sample output provided by the instructor, but some of them were developed by me to check for more specific edge cases.  I wanted to be sure I attempted to "cover all the bases" in terms of testing this program.  

    Because there is a performance requirement as well, I used the VisualVM profiler to evaluate the performance of my program over large files.  It was nearly impossible to look for hotspots in the REPL mode, so creating large files was the best possible alternative to this.  While running VisualVM, I noticed the hotspots were my `FileConcatenator.addFile()` and `Expression.evaluate()` methods.  Given that these two were the most "algorithmically" intensive methods, this was not a surprise to me.  

    In reference to combinatorial testing, I used the REPL to play with different combinations of factors such as a LET and large number, LET and empty stack, large number and empty stack, Print and large number, etc.  I believe these kinds of combinations were important to test, mostly because a majority of the variation of future inputs would depend on the interactions between these factors.  

 2. **What percentage of time/effort was allocated to each kind of test?**  
    Throughout the project, I did my best to follow the Pyramid Pattern as we discussed in class.  A majority of my time was spent developing Unit Tests. To quantify it, I would say about 75% of my time testing was specifically on developing and updating the Unit Tests.  After that, I spend a good amount of time (~15%) on System and Integration testing of the REPL and FILE modes, and the various "subsystems" (classes) that I used to build the project.  The remaining 10% was spent on manually testing the outputs for different combinations of inputs in both the REPL and FILE modes.  I did not want to spend too much time making manual tests, since there is an infinite amount of combinations I could end up testing; however, I did realize that these manual tests would be very important in evaluating the behavior of files especially, since they are difficult to tests with Unit Tests.  

 3. **What recommendations would you give to increase quality?**  
    I have several ideas for increasing the quality of my project:
    * I would refactor some of the code to make it more modular and less repetitive.  In development, I ended up accidentally reusing similar bits of code throughout the system, and I think it would be very beneficial if I were to move that into methods.  This would increase traceability of errors, readability of the code, and most importantly, testability of the system.  

    * In order to increase performance (specifically for large files), I would look in to optimizing the `FileConcatenator.addFile()` method.  This could involve a full overhaul of the existing code to make use of a buffer instead of a single ArrayList of Strings to increase the speed of operation.  This could also involve using threading and concurrency to speed the action of concatenating the files.  In the short time I had to work on that specific part of the project, I was not able to play around with the many various ways of efficiently completing that task.  I would be very interested to see if a buffer or threading would increase the speed of that operation.

    * The performance of `Expression.evaluate()` is also very important to hitting the performance target.  Although a target has not been explicitly set for this project, it makes sense to examine the performance of the most used function in the system.  One way to speed this up would be to implement some sort of cache, which could save recently used/computed values and use them if necessary. Since the math the system performs is not very intense, I would be curious to see if such an implementation would have a noticeable effect on performance for large programs.

    * I would love to add more test cases to Test Plan.  The more tests I have, the more likely I am to catch one of the smaller errors that might be hiding in the "tall grass" of this specific system.  This would involve coming up with more equivalence classes and determining the best course of action for implementing a test for them, and whether an automated test would be possible.  

 4. **What parts of the code need to be cleaned up?**  
    * I am sure that I could refactor some of `RPN.java`'s code to be in better form.  I could reduce the amount of repetition that is present between the implementations of REPL and FILE mode.  This would surely simplify debugging and error diagnosis in the future.  

    * I feel as though the error handling mechanisms of my project are somewhat clunky.  I feel as though I handled it well to make the code testable and maintainable, but there are definitely improvements I could make to the organizational structure of errors in the system.  One such improvement would be to standardize the numbers used as error codes in some sort of enum-esque structure.  

    * Relating to error reporting, I think some of my `try-catch` blocks could probably use a little bit of work.  I am not really used to using them in my code, so I tried to work them in as much as possible to avoid ending up with a default-printed stack trace.  With some time and research, I could probably refactor my exception handling to be "prettier".

    * With a little more time, it is likely that I could find a better way to organize my `Statement.java` class and keep it testable.  

    * I didn't have time to set new configurations for a Linter, but I'm sure my code could use some static analysis other than `Findbugs`.  I did my best to stay consistent in my style, but I am nearly positive that there is some inconsistency throughout the whole proejct.  

 5. **What kinds of things did you discover in exploratory testing?**  
    After my initial implementation, I performed a lot of exploratory testing through the use of the REPL.  This allowed me to play around with combinations of values and variables much quicker than if I was repeatedly writing new unit tests over and over again for different values.  Interestingly, I did find a few different errors through my REPL exploration:
     * At one point, simply entering `PRINT` would cause the entire program to crash with an `EmptyStackException` because I did not check the remainder of the line after the print statement before sending it to be evaluated.  

     * I discovered that I was not treating empty lines as nothing, but instead continued to count them as if they were being operated on.  This led to incorrect error reporting, as the line number would be wrong.

     * I set the line increment to over 1 Million per line and incremented many times.  This is how I discovered that my programs would then be limited to the maximum value of a Java int.  I decided that a `long` would be sufficient for arbitrarily-sized files.  

    I used a similar technique when dealing with the FILE mode of the system, which also led me to some particular defects:  

    * I was not properly handling empty lines in files.  I discovered this using a file with two commands (the second being an invalid command) that were separated by several empty spaces.  The resulting error displayed a much higher count than the anticipated 2.  

    * I discovered that my program would not properly halt when I encountered an error in a line when evaluating multiple files.  

    In summary, through exploratory testing I discovered a few issues that were not necessarily detectable simply through my automated tests.  This was especially important for the file operations, which are TUFs.
 6. **Manual Test Plan**  
    I did not develop a specific Manual Test Plan for this assignment; however, I did recognize the importance of manual testing here and followed some guidelines as I tested.  Firstly, I built some test files that allowed me to standardize my manual tests.  I would run the file with a specific output in mind and then compare it to the observed result.  This allowed me to change the contents/operations of the file on the fly and see how different types of operations would interact with each other.  A few of these files are on GitHub in the `testfiles` directory.  

    The manual tests I did run were particularly important for system-level testing, as I combined different operations in a pairwise fashion to gather information about how the language components of `RPN++` would work when pieced together.  To begin my manual testing, I copied the tests as shown on the assignment website sample output.  I compared my output to the sample and adjusted until the results matched up.  At this point I began to branch out and try different combinations of the tests than were present on the site.  

    I used the same procedure for the REPL mode of the system, which allowed me to evaluate the observed output vs the expected output and make changes very quickly.  This rapid turnaround on command-response was the perfect grounds for variables that were not easily tested in an automated fashion.  Both the REPL and the FileIO operations are fairly test-unfriendly, so being able to manually test these aspects of the program was very important.
