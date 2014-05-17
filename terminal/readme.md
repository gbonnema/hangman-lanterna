	Copyright 2014 Guus Bonnema, Dieren, The Netherlands.

    This file is part of hangman-lanterna.

    hangman-lanterna is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

	hangman-lanterna is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with hangman-lanterna.  If not, see <http://www.gnu.org/licenses/>.
    
    INTRODUCTION
    ============
    This project is an example project for using Lanterna. It implements
    the hangman game is an extremely simple form to show some of what 
    Lanterna can do with screens.
    
    I am convinced, if people find it useful, they will also come up
    with ways to improve what this shows about Lanterna.
    
    For instance, I included the Observer/Observable pattern, to show
    how it works together (and because it was practical to do so), but 
    I did not include any form of multithreading, audio or such niceties.
    
    I know that Lanterna has some privisions for multithreading (it does some
    locking), I just don't know how to exploit it.
    
    THE GAME
    ========
    The game is extremely simple. You press HOME to get a new word. You press 
    PGUP or PGDN to browse the documentation. You press ESC to quit.
    
    While guessing the application also shows which word is the one you should 
    guess (yes, this is cheating, but the app is not about the game). The words 
    are all Dutch, so most ppl will no recognize them. Using the presented solution
    you can follow what happens when it fails a character of finds a character.
    
    THE MECHANISM
    =============
    The application consists of one main screen, that just uses screen. It implements
    an interface called TextDraw, just to provide some utilities to the other
    panels.
    
    The main screen creates all the panels, creates a hangmangame and connects the
    panels to the game as observer. Each panel retrieves its own information
    from the game.
    
    Most panels have a box around them, some with title.
    
    