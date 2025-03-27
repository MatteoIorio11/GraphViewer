# Graph Viewer
## How to run the application
If you want to run the current appplication, all you need to do is to run the following command:
```bash
./gradlew runGui
```
This gradle task will run the application and you will be able to see the main window of it. Once the GUI has loaded you can start interacting with it.

## How to use the GUI
The GUI is divided in different groups of components. The main groups are the following:
1. **Text Area**: Inside the text area you will be able to define the graph that you want to visualize, in order to generate the graph all you need to do
is define all the edges using this format: 'a->b', where 'a' is the source node and 'b' the destination node. Then if you want to visualize the graph
all you need to do is to press 'Enter' or click on the button 'Render Graph'. Every edge that is defined must be separated by a new line, so every time you define a new edge, hit
enter and define the new edge, like so:
```
a->b
b->c
b->d
```
Every other format will be invalid and not accepted.
2. **Vertexs Panel**: Once you have defined your graph and you have rendered it, you will be able to see a new component on the right side of the screen,
inside this panel, you will see a list of buttons where each button represents one of the graph's vertex. If you click on one of the buttons you will be
able to deactivate and activate the clicked *vertex*.
3. **Image Label**: After describing the structure of the code, the graph will be rendered on this component as an image, in this way you will be able to see It.
I have also defined a *zoom* in and zoom out button that will allow you to see the graph in more detail or in a more general way.
