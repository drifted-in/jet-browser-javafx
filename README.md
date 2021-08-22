# JetBrowser (JavaFX)
A desktop app for browsing parish book scans distributed in ZIP files.

## Building (Windows)
For other platforms please check https://docs.gluonhq.com/#_platforms

### Prerequisites
1. Install Visual Studio 2019 with [mandatory](https://docs.gluonhq.com/#platforms_windows) components
2. Download and unpack [the latest](https://github.com/gluonhq/graal/releases/latest) version of the Gluon built version of GraalVM 21
3. Set `GRAALVM_HOME` to GraalVM installation folder, e.g. `C:\graalvm-svm-windows-gluon-21.2.0-dev` 

### Building native image
1. Open `x64 Native Tools Command Prompt for VS 2019` console app
2. Change the current directory to the project folder (e.g. `cd C:\jet-browser-javafx`)
3. Execute the maven goal `mvn client:build`
4. Verify the output stored in `target\client\x86_64-windows\jet-browser.exe`

## Running
The example of scanned parish book: [150-02792.zip](http://88.146.158.154:8083/150-02792.zip) (110 MB)

### From console
- Type `jet-browser.exe 150-02792.zip`

### From File Explorer
- Add a new `Open with JetBrowser` context menu item for any ZIP file by altering Windows registry:
  1. Open `regedit.exe`
  2. In the `HKEY_CLASSES_ROOT\CompressedFolder\Shell` path create a new `JetBrowser` key 
  3. Select the `(Default)` key and set `Open with JetBrowser` as a value
  4. Optionally add a new `Icon` key and specify the path as a value
  5. Add a new `command` subkey and specify the JetBrowser executable path followed by `"%1"` as a value, 
     for example `C:\jet-browser-javafx\target\client\x86_64-windows\jet-browser.exe "%1"`
- in the File Explorer right click the zip file and choose the `Open with JetBrowser` menu item

## Usage
- Use `<-`/`->` arrows keys to move to the previous/next image
- Use `Shift`/`Ctrl` modifiers together with arrow keys to increase the step to 5/20  
- Use `Home`/`End` keys to move to the first/last image
- Use `H`/`W` keys to scale the image to fit the height/width of the window
- Use `R` key to reset the size to the original image size
- Use `C` key to copy the current image filename to the clipboard