# gremlins-mik
 Two-dimensional graphical sprite game project for **INFO1113** (Introduction to Object-Oriented Programming) 2022 Semester 2 Assignment 1.
- Utilises Java Processing Library for graphical processing
- Gradle implementation as dependency manager

---
## Game Setup and Configuration
### Powerups

Implementation of powerups are determined by level design.
One may wish to implement this feature by editing a level
layout ``.txt`` file and placing a ``P`` character in the
desired tile location.
- All powerup effects have a duration of 10 seconds.

### Extensions

The ``Iceball`` projectile extension is integrated into the core
functionality and experience of the gameplay, and thus requires
no further configuration in ``.txt`` layout or ``.json`` config
files.
- Iceball projectiles have the same cooldowns as fireballs.
- Their freeze effect is always 10 seconds.

### Game Levels

The ``config.json`` file in the game root directory handles all
level hierarchies in the ``"Levels"`` JSON array, and both the 
player and enemy firing cooldown in the ``"wizard_cooldown"`` 
and ``"enemy_cooldown"`` keys respectively.

---
## Utilisation
  - JSON Processing
  - Object-Oriented Design
  - Testcase usage with JUnit, AssertJ and JaCoCo
  - Javadoc implementation
  
## Author
Mikael Sebastian Indrawan

## Plagiarism notice
In the event of assignment reuse, do not plagiarise. Univserity of Sydney takes plagiarism **very seriously**.
