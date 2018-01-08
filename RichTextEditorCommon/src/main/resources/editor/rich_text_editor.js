
var editor = {

    _textField: document.getElementById('editor'),

    _htmlSetByApplication: null,

    _currentSelection: {
        "startContainer": 0,
        "startOffset": 0,
        "endContainer": 0,
        "endOffset": 0
    },


    init: function() {
        document.addEventListener("selectionchange", function() {
            editor._backupRange();
            editor._handleTextEntered(); // in newly selected area different commands may be activated / deactivated
        });

        this._textField.addEventListener("keydown", function(e) {
            var BACKSPACE = 8;
            if(e.which == BACKSPACE) {
                if(editor._textField.innerText.length == 1) { // prevent that first paragraph gets deleted
                    e.preventDefault();

                    return false;
                }
            }
        });

        this._textField.addEventListener("paste", function(e) { editor._waitTillPastedDataInserted(e); });

        this._ensureEditorInsertsParagraphWhenPressingEnter();
        this._updateEditorState();
    },

    _ensureEditorInsertsParagraphWhenPressingEnter: function() {
        // see https://stackoverflow.com/a/36373967
        this._executeCommand("DefaultParagraphSeparator", "p");

        this._textField.innerHTML = ""; // clear previous content

        var newElement = document.createElement("p");
        newElement.innerHTML = "&#8203";
        this._textField.appendChild(newElement);

        var selection=document.getSelection();
        var range=document.createRange();
        range.setStart(newElement.firstChild, 1);
        selection.removeAllRanges();
        selection.addRange(range);
    },


    _handleTextEntered: function() {
        if(this._getHtml() == "<p><br></p>") { // SwiftKey, when deleting all entered text, inserts a pure "<br>" therefore check for <p>​&#8203</p> doesn't work anymore
            this._ensureEditorInsertsParagraphWhenPressingEnter();
        }

        this._updateEditorState();
    },

    _waitTillPastedDataInserted: function(event) {
        var previousHtml = this._getHtml();

        setTimeout(function () { // on paste event inserted text is not inserted yet -> wait for till text has been inserted
            editor._waitTillPastedTextInserted(previousHtml, 10); // max 10 tries, after that we give up to prevent endless loops
        }, 100);
    },

    _waitTillPastedTextInserted: function(previousHtml, iteration) {
        var hasBeenInserted = this._getHtml() != previousHtml;

        if(hasBeenInserted || ! iteration) {
            this._updateEditorState();
        }
        else {
            setTimeout(function () { // wait for till pasted data has been inserted
                editor._waitTillPastedTextInserted(pastedText, iteration - 1);
            }, 100);
        }
    },


    _getHtml: function() {
        return this._textField.innerHTML;
    },

    getEncodedHtml: function() {
        return encodeURI(this._getHtml());
    },

    setHtml: function(html, baseUrl) {
        if(baseUrl) {
            this._setBaseUrl(baseUrl);
        }

        if(html.length != 0) {
            this._textField.innerHTML = decodeURIComponent(html.replace(/\+/g, '%20'));
        }
        else {
            this._ensureEditorInsertsParagraphWhenPressingEnter();
        }

        this.didHtmlChange = false;
        this._htmlSetByApplication = this._textField.innerHTML;
    },

    _setBaseUrl: function(baseUrl) {
        var baseElements = document.head.getElementsByTagName('base');
        var baseElement = null;
        if(baseElements.length > 0) {
            baseElement = baseElements[0];
        }
        else {
            var baseElement = document.createElement('base');
            document.head.appendChild(baseElement); // don't know why but append() is not available
        }

        baseElement.setAttribute('href', baseUrl);
        baseElement.setAttribute('target', '_blank');
    },
    
    
    /*      Text Commands        */

    undo: function() {
        this._executeCommand('undo', null);
    },
    
    redo: function() {
        this._executeCommand('redo', null);
    },
    
    setBold: function() {
        this._executeCommand('bold', null);
    },
    
    setItalic: function() {
        this._executeCommand('italic', null);
    },

    setUnderline: function() {
        this._executeCommand('underline', null);
    },
    
    setSubscript: function() {
        this._executeCommand('subscript', null);
    },
    
    setSuperscript: function() {
        this._executeCommand('superscript', null);
    },
    
    setStrikeThrough: function() {
        this._executeCommand('strikeThrough', null);
    },

    setTextColor: function(color) {
        this._executeStyleCommand('foreColor', color);
    },

    setTextBackgroundColor: function(color) {
        if(color == 'rgba(0, 0, 0, 0)') { // resetting backColor does not work with any color value (whether #00000000 nor rgba(0, 0, 0, 0)), we have to pass 'inherit'. Thanks to https://stackoverflow.com/a/7071465 for pointing this out to me
            this._executeStyleCommand('backColor', 'inherit');
        }
        else {
            this._executeStyleCommand('backColor', color);
        }
    },

    setFontName: function(fontName) {
        this._executeCommand("fontName", fontName);
    },

    setFontSize: function(fontSize) {
        this._executeCommand("fontSize", fontSize);
    },

    setHeading: function(heading) {
        this._executeCommand('formatBlock', '<h'+heading+'>');
    },

    setFormattingToParagraph: function() {
        this._executeCommand('formatBlock', '<p>');
    },

    setPreformat: function() {
        this._executeCommand('formatBlock', '<pre>');
    },

    setBlockQuote: function() {
        this._executeCommand('formatBlock', '<blockquote>');
    },

    removeFormat: function() {
        this._executeCommand('removeFormat', null);
    },
    
    setJustifyLeft: function() {
        this._executeCommand('justifyLeft', null);
    },
    
    setJustifyCenter: function() {
        this._executeCommand('justifyCenter', null);
    },
    
    setJustifyRight: function() {
        this._executeCommand('justifyRight', null);
    },

    setJustifyFull: function() {
        this._executeCommand('justifyFull', null);
    },

    setIndent: function() {
        this._executeCommand('indent', null);
    },

    setOutdent: function() {
        this._executeCommand('outdent', null);
    },

    insertBulletList: function() {
        this._executeCommand('insertUnorderedList', null);
    },

    insertNumberedList: function() {
        this._executeCommand('insertOrderedList', null);
    },


    /*      Insert elements             */

    insertLink: function(url, title) {
        this._restoreRange();
        var sel = document.getSelection();

        if (sel.toString().length == 0) {
            this.insertHtml("<a href='"+url+"'>"+title+"</a>");
        }
        else if (sel.rangeCount) {
           var el = document.createElement("a");
           el.setAttribute("href", url);
           el.setAttribute("title", title);

           var range = sel.getRangeAt(0).cloneRange();
           range.surroundContents(el);
           sel.removeAllRanges();
           sel.addRange(range);

           this._updateEditorState();
       }
    },

    insertImage: function(url, alt) {
        var html = '<img src="' + url + '" alt="' + alt + '" />';
        this.insertHtml(html);
    },

    insertCheckbox: function(text) {
        var html = '<input type="checkbox" name="'+ text +'" value="'+ text +'"/> &nbsp;';
        this.insertHtml(html);
    },

    insertHtml: function(html) {
        this._backupRange();
        this._restoreRange();

        document.execCommand('insertHTML', false, html);

        this._updateEditorState();
    },
    
    
    /*      Editor default settings     */
    
    setBaseTextColor: function(color) {
        this._textField.style.color  = color;
    },

    setBaseFontFamily: function(fontFamily) {
        this._textField.style.fontFamily = fontFamily;
    },
    
    setBaseFontSize: function(size) {
        this._textField.style.fontSize = size;
    },
    
    setPadding: function(left, top, right, bottom) {
      this._textField.style.paddingLeft = left;
      this._textField.style.paddingTop = top;
      this._textField.style.paddingRight = right;
      this._textField.style.paddingBottom = bottom;
    },

    // TODO: is this one ever user?
    setBackgroundColor: function(color) {
        document.body.style.backgroundColor = color;
    },
    
    setBackgroundImage: function(image) {
        this._textField.style.backgroundImage = image;
    },
    
    setWidth: function(size) {
        this._textField.style.minWidth = size;
    },
    
    setHeight: function(size) {
        this._textField.style.height = size;
    },
    
    setTextAlign: function(align) {
        this._textField.style.textAlign = align;
    },
    
    setVerticalAlign: function(align) {
        this._textField.style.verticalAlign = align;
    },
    
    setPlaceholder: function(placeholder) {
        this._textField.setAttribute("placeholder", placeholder);
    },
    
    setInputEnabled: function(inputEnabled) {
        this._textField.contentEditable = String(inputEnabled);
    },

    focus: function() {
        var range = document.createRange();
        range.selectNodeContents(this._textField);
        range.collapse(false);
        var selection = window.getSelection();
        selection.removeAllRanges();
        selection.addRange(range);
        this._textField.focus();
    },

    blurFocus: function() {
        this._textField.blur();
    },


    _executeStyleCommand: function(command, parameter) {
        this._backupRange();
        this._restoreRange();

        this._executeCommand("styleWithCSS", null, true);
        this._executeCommand(command, parameter);
        this._executeCommand("styleWithCSS", null, false);
    },
    
    _executeCommand: function(command, parameter) {
        document.execCommand(command, false, parameter);

        this._updateEditorState();
    },


    _updateEditorState: function() {
        var html = this._getHtml();
        var didHtmlChange = this._htmlSetByApplication != null && this._htmlSetByApplication != html;

        if(typeof javafx !== 'undefined') { // in JavaFX changing window.location.href doesn't work -> JavaFX determines editor state manually
            javafx.updateEditorState(didHtmlChange)
        }
        else {
            var commandStates = this._determineCommandStates();

            var editorState = {
                'didHtmlChange': didHtmlChange,
                'html': html,
                'commandStates': commandStates
            };

            window.location.href = "editor-state-changed-callback://" + encodeURI(JSON.stringify(editorState));
        }
    },

    _determineCommandStates: function() {
        var commandStates = {};

        this._determineStateForCommand('undo', commandStates);
        this._determineStateForCommand('redo', commandStates);

        this._determineStateForCommand('bold', commandStates);
        this._determineStateForCommand('italic', commandStates);
        this._determineStateForCommand('underline', commandStates);
        this._determineStateForCommand('subscript', commandStates);
        this._determineStateForCommand('superscript', commandStates);
        this._determineStateForCommand('strikeThrough', commandStates);

        this._determineStateForCommand('foreColor', commandStates);
        this._determineStateForCommand('backColor', commandStates);

        this._determineStateForCommand('fontName', commandStates);
        this._determineStateForCommand('fontSize', commandStates);

        this._determineStateForCommand('formatBlock', commandStates);
        this._determineStateForCommand('removeFormat', commandStates);

        this._determineStateForCommand('justifyLeft', commandStates);
        this._determineStateForCommand('justifyCenter', commandStates);
        this._determineStateForCommand('justifyRight', commandStates);
        this._determineStateForCommand('justifyFull', commandStates);

        this._determineStateForCommand('indent', commandStates);
        this._determineStateForCommand('outdent', commandStates);

        this._determineStateForCommand('insertUnorderedList', commandStates);
        this._determineStateForCommand('insertOrderedList', commandStates);
        this._determineStateForCommand('insertHorizontalRule', commandStates);
        this._determineStateForCommand('insertHTML', commandStates);

        return commandStates;
    },

    _determineStateForCommand: function(command, commandStates) {
        commandStates[command.toUpperCase()] = {
            'executable': document.queryCommandEnabled(command),
            'value': document.queryCommandValue(command)
        }
    },


    _backupRange: function(){
        var selection = window.getSelection();
        if(selection.rangeCount > 0) {
          var range = selection.getRangeAt(0);

          this._currentSelection = {
              "startContainer": range.startContainer,
              "startOffset": range.startOffset,
              "endContainer": range.endContainer,
              "endOffset": range.endOffset
          };
        }
    },

    _restoreRange: function(){
        var selection = window.getSelection();
        selection.removeAllRanges();

        var range = document.createRange();
        range.setStart(this._currentSelection.startContainer, this._currentSelection.startOffset);
        range.setEnd(this._currentSelection.endContainer, this._currentSelection.endOffset);

        selection.addRange(range);
    },

}


editor.init();