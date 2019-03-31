# RichTextEditor
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.dankito.richtexteditor/richtexteditor-common/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.dankito.richtexteditor/richtexteditor-common)

WYSIWYG editor for Android and JavaFX with a rich set of supported formatting options.

Based on [https://github.com/wasabeef/richeditor-android](url), but with more options like setting the font and text foreground and background color.
And it also has ready to use UI elements implemented for these.


## Features

- Bold, Italic, Underline, Strike through, Subscript, Superscript
- Heading 1 - 6, Text body, Preformatted, Block quote
- Font (reads all system fonts)
- Font Size
- Text Color
- Text Background Color
- Highlight text
- Justify Left, Center, Right, Blockquote
- Indent, Outdent
- Undo, Redo
- Unordered List (Bullets)
- Ordered List (Numbers)
- Insert local or remote Image
- Insert Link
- Insert Checkbox
- Search
- For all those exist predefined commands. You can use one of the standard toolbars ([GroupedCommandsEditorToolbar](RichTextEditorAndroid/src/main/kotlin/net/dankito/richtexteditor/android/toolbar/GroupedCommandsEditorToolbar.kt) or [AllCommandsEditorToolbar](RichTextEditorAndroid/src/main/kotlin/net/dankito/richtexteditor/android/toolbar/AllCommandsEditorToolbar.kt)) or create your own within minutes, see [Create custom editor toolbar](#customtoolbar).
- All commands have a Material Design icon. But of course you can give all of them your custom icon.
- Themeable


## Demo application

You can download it from [PlayStore](https://play.google.com/store/apps/details?id=net.dankito.richtexteditor.android), can use the [precompiled .apk](res/PlayStore/releases/2000100_v2.0.1_DemoApp-release.apk) or compile the DemoApp project yourself.


## Setup

Gradle:
```
dependencies {
  implementation 'net.dankito.richtexteditor:richtexteditor-android:2.0.8'
}
```

Maven:
```
<dependency>
   <groupId>net.dankito.richtexteditor</groupId>
   <artifactId>richtexteditor-android</artifactId>
   <version>2.0.8</version>
</dependency>
```


## Usage

Layout xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <net.dankito.richtexteditor.android.RichTextEditor
        android:id="@+id/editor"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="10"
    />

    <net.dankito.richtexteditor.android.toolbar.GroupedCommandsEditorToolbar
        android:id="@+id/editorToolbar"
        android:layout_width="match_parent"
        android:layout_height="36dp"
        android:background="@color/colorPrimary"
    />

</LinearLayout>
```

Activity

```java
public class MainActivity extends AppCompatActivity {

    private RichTextEditor editor;

    private GroupedCommandsEditorToolbar bottomGroupedCommandsToolbar;

    private IPermissionsService permissionsService = new PermissionsService(this);


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_main);

        editor = (RichTextEditor) findViewById(R.id.editor);

        // this is needed if you like to insert images so that the user gets asked for permission to access external storage if needed
        // see also onRequestPermissionsResult() below
        editor.setPermissionsService(permissionsService);

        bottomGroupedCommandsToolbar = (GroupedCommandsEditorToolbar) findViewById(R.id.bottomGroupedCommandsToolbar);
        bottomGroupedCommandsToolbar.setEditor(editor);

        // you can adjust predefined toolbars by removing single commands
//        bottomGroupedCommandsToolbar.removeCommandFromGroupedCommandsView(CommandName.TOGGLE_GROUPED_TEXT_STYLES_COMMANDS_VIEW, CommandName.BOLD);
//        bottomGroupedCommandsToolbar.removeSearchView();


        editor.setEditorFontSize(20);
        editor.setPadding((4 * (int) getResources().getDisplayMetrics().density));

        // some properties you also can set on editor
//        editor.setEditorBackgroundColor(Color.YELLOW)
//        editor.setEditorFontColor(Color.MAGENTA)
//        editor.setEditorFontFamily("cursive")

        // show keyboard right at start up
//        editor.focusEditorAndShowKeyboardDelayed()

        // only needed if you allow to automatically download remote images
        editor.setDownloadImageConfig(new DownloadImageConfig(DownloadImageUiSetting.AllowSelectDownloadFolderInCode,
            new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "downloaded_images")));
    }


    // Important: Overwrite onBackPressed and pass it to toolbar.There's no other way that it can get informed of back button presses.
    @Override
    public void onBackPressed() {
        if(bottomGroupedCommandsToolbar.handlesBackButtonPress() == false) {
            super.onBackPressed();
        }
    }

    // only needed if you like to insert images from local device so the user gets asked for permission to access external storage if needed

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsService.onRequestPermissionsResult(requestCode, permissions, grantResults);

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    // then when you want to do something with edited html
    private void save() {
        editor.getCurrentHtmlAsync(new GetCurrentHtmlCallback() {

            @Override
            public void htmlRetrieved(@NotNull String html) {
                saveHtml(html);
            }
        });
    }

    private void saveHtml(String html) {
        // ...
    }

}
```

For an example see [MainActivity](DemoApp/src/main/kotlin/net/dankito/richtexteditor/android/demo/MainActivity.kt) in DemoApp project (it's written in Kotlin).


<h2 id="customtoolbar">Create custom editor toolbar</h2>

As an example how to define your own editor toolbar you can see [GroupedCommandsEditorToolbar](RichTextEditorAndroid/src/main/kotlin/net/dankito/richtexteditor/android/toolbar/GroupedCommandsEditorToolbar.kt) or [AllCommandsEditorToolbar](RichTextEditorAndroid/src/main/kotlin/net/dankito/richtexteditor/android/toolbar/AllCommandsEditorToolbar.kt).

Let's say you like to have on your toolbar the grouped style command, the undo command and a search bar:

```java
import android.content.Context;
import android.util.AttributeSet;

import net.dankito.richtexteditor.android.command.ToggleGroupedTextStylesCommand;
import net.dankito.richtexteditor.android.command.UndoCommand;

public class ExampleToolbar extends EditorToolbar {

    public ExampleToolbar(Context context) {
        super(context);

        initToolbar();
    }

    public ExampleToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);

        initToolbar();
    }

    public ExampleToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initToolbar();
    }

    public ExampleToolbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        initToolbar();
    }


    private void initToolbar() {
        addCommand(new ToggleGroupedTextStylesCommand());

        addCommand(new UndoCommand());

        addSearchView();
    }

}
```

See package [net.dankito.richtexteditor.android.command](RichTextEditorAndroid/src/main/kotlin/net/dankito/richtexteditor/android/command) for all predefined commands.


## Themes

To theme the editor add a custom themes.css to assets folder and define styles with data-theme attribute set to theme_name, e. g.

```css
html[data-theme='green'] {
    color: #00FF00;
}
```

and then call RichTextEditor.setTheme('<theme_name>'), in this case with <theme_name>="green" as defined above:

```java
RichTextEditor editor = ...;
editor.setTheme("green")
```

As an example see [theme.css](RichTextEditorCommon/src/main/resources/editor/themes.css) in RichTextEditorCommon which defines a dark theme.

There's also some magic for setting editor to dark theme:

If you use `net.dankito.richtexteditor.android.R.style.RichTextEditorAppThemeDark` as your activity theme or derive your activity theme from `net.dankito.richtexteditor.android.R.style.RichTextEditorAppThemeDark` without overwriting `themeName` attribute, then editor's theme automatically gets set to dark theme.

# License

    Copyright 2017 dankito

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
