# Cayman Configurator

The Cayman Configurator is a Java Swing GUI application mimics the car configurator from the [Porsche official website](https://cc.porsche.com/icc_pcna/ccCall.do?rt=1508789638&screen=1280x720&userID=USM&lang=us&PARAM=parameter_internet_us&ORDERTYPE=982130&MODELYEAR=2018&hookURL=http%3a%2f%2fwww.porsche.com%2fusa%2fmodelstart%2f), allowing a user to customize a 2018 Porsche 718 Cayman S to their desired specification with real-time model-photo and price updates. In addition to configuration, the user has the option to view a summary of their specification and save their configuration to their home directory in CSV and/or PDF format.

## Motivation & Purpose

The Cayman Configurator is inspired by my dual passion for behind-the-scenes application logic and in-your-face interface design. In my free time, I enjoy configuring cars on manufacturers' websites, so I figured it would be an interesting challenge/display of skill to be able to "recreate" a manufacturer's car configurator using a Java Swing GUI.

## Getting Started

These instructions will assist you in downloading and launching the Cayman Configurator program on your computer

### Prerequisites:

 - [Java Runtime Environment (JRE)](https://java.com/en/)
 - Ability to unzip compressed folders

### Installation:

 1. Navigate to the [dist](https://github.com/jack-loss/CaymanConfigurator/tree/master/dist) folder
 2. Click the file named [CaymanConfigurator.zip](https://github.com/jack-loss/CaymanConfigurator/blob/master/dist/CaymanConfigurator.zip) and select "Download"
 3. Locate the CaymanConfigurator.zip file in your Downloads folder
 4. Drag the CaymanConfigurator.zip file to your Desktop
 5. Right-click the CaymanConfigurator.zip file and select "Extract All..."
 6. Leave the default settings in the prompt, make sure that "Show extracted files when complete" is checked, and click "Extract"
 7. Open the newly-created folder called "CaymanConfigurator"
 8. Make sure that the "lib" folder is in the "CaymanConfigurator" folder, along with the CaymanConfigurator.jar file
 9. Double-click the "CaymanConfigurator.jar" file
 10. The Cayman Configurator application is launched!

## Using the Application

 1. Note the layout of the interface upon first starting the application:
    - Model photo on the left
    - Configuration options on the right
    - Price breakdown on the bottom left under the model photo
    - "Show Overview" button on the bottom right under the configuration options
 2. Select your desired exterior color by selecting one of the five colored squares under the "Exerior Color" heading. Each option has ToolTip text that shows the option's name and price; activate by hovering your cursor over the corresponding icon for a few seconds. The exterior color currently selected is indicated by an inscribed red circle with white check mark. Note that the current price is updated in real-time in the price-breakdown section under the model photo
 3. Repeat the above process for the wheel style and interior color
 4. Select your desired transmission by clicking on one of the two radio buttons under the "Transmission" heading
 5. Select any number or combination of your desired additional upgrades by clicking the checkboxes under the "Additional Upgrades" heading
 6. View your current specification from different angles by clicking the gray dots below the model image. The model images shown are updated in real-time to reflect your changes to the model specification
 7. When satisfied with your specification, click the "Show Overview" button to be presented with a new window summarizing your model's configuration
 8. To save your configuration to the current user's home directory, click "Save as PDF" to save a graphical summary and/or "Save as CSV" to save a numeric/verbal summary
 9. To modify your configuration further, click "Cancel" to return to the original configuration window
 10. When finished with the application, click the X in the top-right corner of the application's window

## Built with

- [Java SE 8 (Swing)](http://www.oracle.com/technetwork/java/javase/overview/index.html) 
- [NetBeans IDE 8.2](https://netbeans.org/downloads/index.html)
- [iText PDF Library 5.5](https://itextpdf.com/?utm_expid=.9SegJ4FbQTqSTM0qCvzm8w.0&utm_referrer=https%3A%2F%2Fwww.google.com%2F)

## Authors

 - [Jack Loss](https://www.linkedin.com/in/jack-loss-99997a124/)
 
## Further Reading

Discussion of the program's [architecture](https://github.com/jack-loss/CaymanConfigurator/tree/master/discussion) and [initial design](https://github.com/jack-loss/CaymanConfigurator/tree/master/discussion/Initial%20Design)