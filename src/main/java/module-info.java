module in.drifted.tools.jetbrowser {

    requires javafx.controls;
    // jdk.zipfs module is not auto-detected by default so adding it explicitly here
    requires jdk.zipfs;

    exports in.drifted.tools.jetbrowser;
}