# Machine Vision application using Union-Find Data structure and Connected Component Labeling algorithm (algorithms-assign1)
Student ID: 20061112
Assignment 1 for Algorithm module

Features:
- This app locates and highlights objects on an image. It can also set those objects to random colors. Each pixel (or point) of an image is first converted to grayscale and determined if it belongs to the "object level" or "background level" based on a predefined threshold. Then, all pixels are labelled so that groups of linked/connected pixels can be identified, these groups are ultimately objects on the image. 
- Automatic shutdown if processing of an image takes too long
- Used JavaFX to develop GUI
- Test-Driven Development was used


Demo Run:

![alt tag](https://www.dropbox.com/s/3l5lm5894uokcgt/connected%20component.png?raw=1)

The three image windows are respectively the original image, the image with detected objects highlighted, and the image with its objects set to random colors. Notice that there are only 8 crosses but 9 objects were detected, this is because 4-connectivity scheme was implemented as opposed to to 8-connectivity scheme ie. Diagonal Pixels are considerred not connected). If we take a closer look on one unusual cross, we can see there is a pixel completely "diagonal" to the cross object next to it:

![alt tag](https://www.dropbox.com/s/7fdlzgry1cnpaap/connected%20component%202.png?raw=1)

How to use:
- Java 8 and e(fx)eclipse are required to run the program
- Run from Main class in the "application" package

Original Assignment requirements (all satisfied):
- Read in an image file
- Count the number of objects in the image
- Locate and highlight objects in the image
- Provide full JUnit test coverage

Link to e(fx)clipse:
http://www.eclipse.org/efxclipse/install.html

Link to the algorithm:
https://en.wikipedia.org/wiki/Connected-component_labeling
