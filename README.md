# Amazon-AI-Game
Creating a game client to play the Amazons game with AI

# Algorithms we'll be using:
opening book(starter algorithm for possible moves)
1. Build Tree
2. Searching Tree 
Alpha-Beta Pruning: Minimax algorithm(find heuristic)
4. Sorting the Moves 
Static Board scores/ Killer moves 
5. Evaluation of positions  
Territory Evaluation
  Step 1: Mark all squares which can be reached directly by the original amazons of each side
by Y which denotes the number of moves necessary to reach this square, Y is initialised
to 1. A square that is marked for both sides will be denoted neutral.
  Step 2: For each unmarked square, search if there are marked squares to be reached in one
move. If so mark this square by the marker found and add one to it (Y+1). Again if
markers for both sides are found the square under consideration will be denoted
neutral. 
  Step 3: Use all newly-marked squares found in step 2 except the neutral ones and mark all the
squares they can reach in the same way as described in step 1. Keep repeating step 2
and 3 until either of them cannot put any more markers on the board. 



# References:
1. https://project.dke.maastrichtuniversity.nl/games/files/msc/Hensgens_thesis.pdf
2. https://github.com/rmcqueen/Game-of-the-Amazons-AI



