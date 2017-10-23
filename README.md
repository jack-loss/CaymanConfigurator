# Cayman Configurator

The Cayman Configurator application mimics the car configurator from the [Porsche official website](https://cc.porsche.com/icc_pcna/ccCall.do?rt=1508789638&screen=1280x720&userID=USM&lang=us&PARAM=parameter_internet_us&ORDERTYPE=982130&MODELYEAR=2018&hookURL=http%3a%2f%2fwww.porsche.com%2fusa%2fmodelstart%2f), allowing a user to customize a 2018 Porsche 718 Cayman S to their desired specification with real-time model-photo and price updates. In addition to configuration, the user has the option to view a summary of their specification and save their configuration to their home directory in CSV and/or PDF format.

## Getting Started

These instructions will assist you in downloading and launching the Cayman Configurator program on your computer.

### Prerequisites:

 - Java Runtime Environment (JRE) -> Download [here](https://java.com/en/)
 - Ability to unzip compressed folders

### Installation:

 - Navigate to the [dist](https://github.com/jack-loss/CaymanConfigurator/tree/master/dist) folder
 - Right-click the file named [CaymanConfigurator.ZIP](https://github.com/jack-loss/CaymanConfigurator/blob/master/dist/CaymanConfigurator.zip) and select "Download"
 - Locate the CaymanConfigurator.ZIP file in your Downloads folder
 - Drag the CaymanConfigurator.ZIP file to your Desktop
 - Right-click the CaymanConfigurator.ZIP file and select "Extract All..."
 - Leave the default settings in the prompt, make sure that "Show extracted files when complete" is checked, and click "Extract"
 - Open the newly-created folder called "CaymanConfigurator"
 - Make sure that the "lib" folder is in the "CaymanConfigurator" folder, along with the CaymanConfigurator.JAR file
 - Double-click the "CaymanConfigurator.JAR" file
 - The Cayman Configurator application is launched!

## Using the Application



-Note the layout of the interface upon first starting the application:

   -model photos on the left

   -configuration options on the right

   -price breakdown on the bottom left under the model photos

   -show overview button on the bottom right under the configuration options

-Select your desired exterior color by selecting one of the five colored squares. The exterior color currently selected is indicated by an inscribed red circle and white check mark

-repeat the above process for the wheel style, interior color, transmission, and options

-view your current specification from different by clicking the gray dots below the model images. The model images shown are updated in real-time to reflect your changes to the model specification

-when satisfied with your specification, click the "Show Overview" button to be presented with a new window summarizing your model's configuration

-to save your configuration to the current user's home directory, click "Save as PDF" to save a graphical summary and/or "Save as CSV" to save a numeric/verbal summary

-to modify your configuration further, click cancel to return to the original configuration window

-when finished with the application, click the X in the top-right corner of the application's window



Built With

-Java SE 8 (Swing GUI)

-NetBeans IDE

-iText PDF Library



Authors

-Jack Loss