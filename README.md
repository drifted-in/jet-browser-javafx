# JetBrowser (JavaFX)
A desktop app for browsing parish book scans distributed in ZIP files.

## Running (on Windows)
1. Download an [installer](https://drifted.in/other/jetbrowser/installer.exe) (26 MB)
2. Install the image browser
3. Download a sample scanned parish book: [150-02792.zip](http://88.146.158.154:8083/150-02792.zip) (110 MB)
4. Locate downloaded file in the File Explorer, right-click the zip file and choose the `Open with JetBrowser` menu item

## Usage
- Use `<-`/`->` arrows keys to move to the previous/next image
- Use `Shift`/`Ctrl` modifiers together with arrow keys to increase the step to 5/20
- Use `Home`/`End` keys to move to the first/last image
- Use `H`/`W` keys to scale the image to fit the height / width of the window
- Use `Ctrl +` / `Ctrl -` to increase / decrease the image size
- Use `R` key to reset the size to the original image size
- Use `C` key to copy the current image filename to the clipboard

## Building as the Windows app
This app doesn't require JDK on target machine because custom JDK is already bundled.
1. Create a custom JDK runtime: `mvn clean javajx:jlink`
2. Create a bundle (JDK16+): `jpackage --name jet-browser --icon icon.ico --module in.drifted.tools.jetbrowser/in.drifted.tools.jetbrowser.App --runtime-image target/jet-browser --type app-image`
3. Create an installer using NSIS: In the Compiler section choose Compile NSI script and then load `installer\installer.nsi` script

## Building as the Windows native app (experimental)
This app doesn't require JDK on target machine as all Java code is compiled into native code.
It requires more complex setup. Additionally, the final app can't be run on all machines because of failures when loading some system libraries.
For other platforms please check https://docs.gluonhq.com/#_platforms

### Prerequisites
1. Install Visual Studio 2019 with [mandatory](https://docs.gluonhq.com/#platforms_windows) components
2. Download and unpack [the latest](https://github.com/gluonhq/graal/releases/latest) version of the Gluon built 
   version of GraalVM 21
3. Set `GRAALVM_HOME` to GraalVM installation folder, e.g. `C:\graalvm-svm-windows-gluon-21.2.0-dev` 

### Building native image
1. Disable all Java references (by adding a path prefix) from the global `PATH` variable
2. Open `x64 Native Tools Command Prompt for VS 2019` console app
3. Clear the `PATH` variable and optionally set the path to Maven bin folder `SET PATH=C:\maven\bin`
4. Set the MS specific vars `C:\Program Files (x86)\Microsoft Visual Studio\2019\Community\VC\Auxiliary\Build\vcvars64.bat`
5. Set the proper `JAVA_HOME` variable `SET JAVA_HOME=%GRAALVM_HOME%`
6. Change the current directory to the project folder (e.g. `cd C:\jet-browser-javafx`)
7. Execute the maven goal `mvn gluonfx:build`
8. Verify the output stored in `target\client\x86_64-windows\jet-browser.exe`

### Changing icon and exe file metadata
1. Open `jet-browser.exe` in the Resource Hacker
2. Press `Ctrl+M` to Add binary resource, navigate to the `\icon.ico` and then click Add Resource
3. Expand the Version Info node
4. In the script on the right update FileDescription and ProductName values together with all version instances
5. Press `F5` to compile the script
6. Press `Ctrl+S` to save your changes

### Creating an installer
1. Copy and rename `target\client\x86_64-windows\jet-browser.exe` to `installer\JetBrowser.exe`
2. Open NSIS app, in the Compiler section choose Compile NSI script and then load `installer\installer-native.nsi` script
