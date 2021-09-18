# JetBrowser (JavaFX)
A desktop app for browsing parish book scans distributed in ZIP files.

## Building (Windows)
For other platforms please check https://docs.gluonhq.com/#_platforms

### Prerequisites
1. Install Visual Studio 2019 with [mandatory](https://docs.gluonhq.com/#platforms_windows) components
2. Download and unpack [the latest](https://github.com/gluonhq/graal/releases/latest) version of the Gluon built 
   version of GraalVM 21
3. Set `GRAALVM_HOME` to GraalVM installation folder, e.g. `C:\graalvm-svm-windows-gluon-21.2.0-dev` 

### Building native image
1. Open `x64 Native Tools Command Prompt for VS 2019` console app
2. Change the current directory to the project folder (e.g. `cd C:\jet-browser-javafx`)
3. Execute the maven goal `mvn gluonfx:build`
4. Verify the output stored in `target\client\x86_64-windows\jet-browser.exe`

### Changing icon and exe file metadata
1. Open `jet-browser.exe` in the Resource Hacker
2. Press `Ctrl+M` to Add binary resource, navigate to the `\icon.ico` and then click Add Resource
3. Expand the Version Info node
4. In the script on the right update FileDescription and ProductName values together with all version instances
5. Press `F5` to compile the script
6. Press `Ctrl+S` to save your changes

## Creating a Windows installer
1. Copy and rename `target\client\x86_64-windows\jet-browser.exe` to `installer\JetBrowser.exe`
2. Open NSIS app, in the Compiler section choose Compile NSI script and then load `installer\installer.nsi` script

## Running (on Windows)
1. Download an [installer](https://drifted.in/other/jetbrowser/installer.exe) (19 MB)
2. Install the image browser
3. Download a sample scanned parish book: [150-02792.zip](http://88.146.158.154:8083/150-02792.zip) (110 MB)
4. Locate downloaded file in the File Explorer, right click the zip file and choose the `Open with JetBrowser` menu item

## Running (on Windows, without installer)

### From console
- Type `JetBrowser.exe 150-02792.zip`

### From File Explorer (Windows)
- Add a new `Open with JetBrowser` context menu item for any ZIP file by altering Windows registry:
  1. Open `regedit.exe`
  2. In the `HKEY_CLASSES_ROOT\CompressedFolder\Shell` path create a new `JetBrowser` key 
  3. Select the `(Default)` key and set `Open with JetBrowser` as a value
  4. Optionally add a new `Icon` key and specify the path as a value
  5. Add a new `command` subkey and specify the JetBrowser executable path followed by `"%1"` as a value, 
     for example `C:\jet-browser-javafx\JetBrowser.exe "%1"`
- in the File Explorer right click the zip file and choose the `Open with JetBrowser` menu item

## Usage
- Use `<-`/`->` arrows keys to move to the previous/next image
- Use `Shift`/`Ctrl` modifiers together with arrow keys to increase the step to 5/20  
- Use `Home`/`End` keys to move to the first/last image
- Use `H`/`W` keys to scale the image to fit the height/width of the window
- Use `R` key to reset the size to the original image size
- Use `C` key to copy the current image filename to the clipboard