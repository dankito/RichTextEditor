# RichTextEditor
WYSIWYG editor for Android with a rich set of supported formatting options.

Based on [https://github.com/wasabeef/richeditor-android](url), but with more options like setting the font and text foreground and background color.
And it also has ready to use UI elements implemented for these.


## Features

- Font (reads all system fonts)
- Font Size
- Text Color
- Text Background Color
- Heading 1 - 6, Text body, Preformatted, Block quote
- Bold, Italic, Underline, Strike through, Subscript, Superscript
- Justify Left, Center, Right, Blockquote
- Indent, Outdent
- Undo, Redo
- Insert Image
- Insert Link
- Insert Checkbox
- Unordered List (Bullets)
- Ordered List (Numbers)
- For all those it has predefined commands. You can use the [standard toolbar](https://github.com/dankito/RichTextEditor/blob/master/RichTextEditorAndroid/src/main/kotlin/net/dankito/richtexteditor/android/toolbar/AllCommandsEditorToolbar.kt) or create your own within minutes.
- All commands have a Material Design icon. But of course you can give all of them your custom icon.



## Setup

Gradle:
```
dependencies {
  compile 'net.dankito.richtexteditor:richtexteditor-android:1.0'
}
```

Maven:
```
<dependency>
   <groupId>net.dankito.richtexteditor</groupId>
   <artifactId>richtexteditor-android</artifactId>
   <version>1.0</version>
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

    <net.dankito.richtexteditor.android.toolbar.AllCommandsEditorToolbar
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

    private AllCommandsEditorToolbar editorToolbar;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editor = (RichTextEditor) findViewById(R.id.editor);

        editorToolbar = (AllCommandsEditorToolbar) findViewById(R.id.editorToolbar);
        editorToolbar.setEditor(editor);

        editor.setEditorFontSize(20);
        editor.setPadding((int) (4 * getResources().getDisplayMetrics().density));

        // some properties you also can set on editor
        // editor.setEditorBackgroundColor(Color.YELLOW);
        // editor.setEditorFontColor(Color.MAGENTA);
        // editor.setEditorFontFamily("cursive");

        // show keyboard right at start up
        editor.focusEditorAndShowKeyboardDelayed();
    }


    @Override
    public void onBackPressed() {
        if(editorToolbar.handlesBackButtonPress() == false) {
            super.onBackPressed();
        }
    }

}
```

For an example see [MainActivity](https://github.com/dankito/RichTextEditor/blob/master/DemoApp/src/main/kotlin/net/dankito/richtexteditor/android/demo/MainActivity.kt) in DemoApp project (it's written in Kotlin).

[MainActivity](../../blob/master/DemoApp/src/main/kotlin/net/dankito/richtexteditor/android/demo/MainActivity.kt)

[MainActivity](DemoApp/src/main/kotlin/net/dankito/richtexteditor/android/demo/MainActivity.kt)



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