# TTTK1143 Program Design and Problem Solving - Tutorial 4 (Interface and UI)

Each folder is self-contained: the original console program, a Swing GUI version, and a small shared UI helper.

| Folder | Console | GUI window | GUI page |
|---|---|---|---|
| Q1_DataSet | DataSetApp | DataSetGUI | DataSetPanel, DataTab |
| Q2_EdibleApp | EdibleApp | EdibleGUI | EdiblePanel |
| Q3_MobileDevice | MobileApp | MobileGUI | MobilePanel |
| Q4_Price | PriceApp | PriceGUI | PricePanel |

## Code structure (same in every folder)
- `*App.java` - original console program and the model classes (interfaces, inheritance). Not changed.
- `UiKit.java` - shared colors, fonts, buttons, cards and form helpers, so all four windows look the same.
- `*GUI.java` - `*GUI` (the window) hosts a `*Panel` (the page). Each button calls a small method such as `onAdd()` or `onDemo()`.

The GUI only calls the model classes; it never changes their logic.

## How to run (example: Q1)
```
cd Q1_DataSet
javac *.java
java DataSetGUI      # GUI
java DataSetApp      # console
```
Requires JDK 8 or newer.
