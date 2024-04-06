# CS205 Game Application
Singapore... but it's the year 2100.

This is a simple game application developed in Android Studio using Java. The game involves a player and an enemy, with the player's objective being to shoot the enemy while avoiding collision.  
# Features
Player and Enemy: The game involves a player and an enemy. The player can shoot bullets at the enemy and the enemy moves around the screen.
Joystick Control: The player's movement is controlled using a joystick.
Score: The score is incremented each time a bullet hits the enemy.
Collision Detection: The game checks for collisions between the player and the enemy, as well as between bullets and the enemy.
Game Over: The game ends when the player's health reaches zero or the enemy's health reaches zero.
Music: Background music is played during the game.

# Code Structure
The main classes in the application are:  
**MainActivity**: This is the main activity of the application. It handles the game logic, including player movement, shooting bullets, collision detection, and game over scenarios.
**GameView**: This is a custom view that handles the game's graphics and animations.
**Player**: This class represents the player. It handles shooting bullets and updating the player's health.
**Enemy**: This class represents the enemy. It handles the enemy's movement.
**Bullet**: This class represents a bullet. It handles updating the bullet's position and checking for collisions with the enemy.
**JoystickView**: This is a custom view that represents the joystick. It handles the joystick's movement.
**MusicPlayer**: This class handles playing the background music.

# Setup
1. Clone the repository.
2. Open the project in Android Studio.
3. Run the application on an emulator or a physical device.

# Requirements
Android Studio
Java
Gradle
Contributing

# Team 
- Justin Goh 
- Chan Jess Myn 
- Leong Zhe Cheng
- Jeremy John
- Trong Hai Dang 


## Grading Essential Features
- An application contains at least 1 activity drawn using 2D graphics.
  - **At least 1 activity drawn using 2D graphics**: The `GameView` class is responsible for drawing the game's 2D graphics. It uses the `Canvas` class to draw the player, enemy, and bullets on the screen.
- An application works in “real-time”, and as such it includes 3 types of dynamic elements,progressing without human interaction:
  - **Real-time elements updated synchronously in each frame and animated**
    - **Real-time elements updated synchronously in each frame and animated fluently at a constant rate despite potentially variable time deltas between frames**: The runnable in the `MainActivity` class is responsible for updating the game state in real-time. It updates the enemy's position, checks for collisions, and redraws the `GameView` at a constant rate (every 100 milliseconds).
  - **Interval elements updated synchronously at predetermined interval steps with each update spanning a fixed time delta**
    - **Interval elements updated synchronously at predetermined interval steps with each update spanning a fixed time delta**: The `bulletCollisionRunnable` and `reloadRunnable` in the `MainActivity` class are examples of interval elements. They are updated at fixed intervals (every 100 milliseconds) to check for bullet collisions and update the reload progress bar, respectively.
  - **Asynchronous elements updated in threads, regardless of a simulated time delta**
    - **Asynchronous elements updated in threads, regardless of a simulated time delta**: The new `Thread(bulletCollisionRunnable).start();` line in the `MainActivity` class creates a new worker thread that checks for bullet collisions asynchronously.
- An application is “interactive”, and as such it includes behaviours triggered by user events (e.g. click, touch, key press, voice, captured image, etc).
  - **Behaviours triggered by user events (e.g. click, touch, key press, voice, captured image, etc)**: The `JoystickView.JoystickListener` and `shootButton.setOnClickListener` in the `MainActivity` class are responsible for handling user interactions. They update the player's position and shoot bullets based on the user's input.
- An application performs parallel operations by creating at least one worker thread.
  - **Parallel operations by creating at least one worker thread**: The new `Thread(bulletCollisionRunnable).start();` line in the `MainActivity` class creates a new worker thread that checks for bullet collisions. This operation runs in parallel with the main thread.
- An application ensures that threads synchronise updates to a common state with built-in synchronisation primitives, e.g. mutexes
  - **Threads synchronise updates to a common state with built-in synchronisation primitives, e.g. mutexes**: The `Handler` class is used to post `Runnable` objects to the main thread's message queue. This ensures that updates to the game state (such as the player's position, the enemy's position, and the bullet list) are synchronised and occur on the main thread, preventing race conditions.
## Extra Features
  - An application integrates mobile features (e.g. vibrations, accelerometer, notifications, notification lights, GPS, flashlight, camera, audio, video).
    - `mediaPlayer = MediaPlayer.create(this, R.raw.game_music);`
    - The application uses the `Vibrator` service to create vibrations on hit.
  - An application uses a thread pool for performing tasks triggered by human interactions.
    - In `MainActivity.java` file, a `ScheduledExecutorService` with a fixed thread pool of size 5 has been created.
  - An application can preserve state of its activities (e.g. in SQLite) when it gets terminated. This can be done automatically or upon a user request.
    - Score is saved onto SQLite when game ends before moving on to the next page.
  - An application uses a producer-consumer pattern for performing asynchronous tasks.
    - Player Health taking damage.
  - An application supports OS-related customization of activities, or of the application as a whole(e.g. hide the OS status bar, keep the screen always on, show on the lock-screen).
    - On `Activity_main`, the OS Status bar is hidden, and the screen is kept always on.
  - An application utilizes advanced built-in abstractions meant to facilitate multithreading(e.g. Looper class, message queues, asynchronous tasks, futures, background services).
    - Use of the `ScheduledExecutorService` class, which is a higher-level replacement for working with threads directly.
  - An application supports advanced 2D graphics features (e.g. transparency, gradients, shaders, complex animations).
    - `Enemy.java`
      - **Gradients**: The `Paint` object used to draw the Enemy has a `LinearGradient` shader set, which creates a gradient effect.
      - **Shaders**: As mentioned above, a `LinearGradient` shader is used, which is a type of shader.
