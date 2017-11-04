
var editor = {

    textField: document.getElementById('editor'),

    htmlSetByApplication: null,

    informOfEachTextChange: true,

    currentSelection: {
        "startContainer": 0,
        "startOffset": 0,
        "endContainer": 0,
        "endOffset": 0
    },


    init: function() {
        document.addEventListener("selectionchange", function() { editor._backupRange(); });

        this.textField.addEventListener("click", function() { editor._updateEditorState });

        this.textField.addEventListener("keydown", function(e) {
            var BACKSPACE = 8;
            if(e.which == BACKSPACE) {
                if(editor.textField.innerText.length == 1) { // prevent that first paragraph gets deleted
                    e.preventDefault();

                    return false;
                }
            }
        });
        this.textField.addEventListener("keyup", function(e) {
            editor._handleTextEntered();
        });

        this._ensureEditorInsertsParagraphWhenPressingEnter();
        this._updateEditorState();
    },

    _ensureEditorInsertsParagraphWhenPressingEnter: function() {
        // see https://stackoverflow.com/a/36373967
        this._executeCommand("DefaultParagraphSeparator", "p");

        var newElement = document.createElement("p");
        newElement.innerHTML = "&#8203";
        this.textField.appendChild(newElement);

        var selection=document.getSelection();
        var range=document.createRange();
        range.setStart(newElement.firstChild, 1);
        selection.removeAllRanges();
        selection.addRange(range);
    },


    _handleTextEntered: function() {
        this._updateEditorState();

        if(this.informOfEachTextChange) {
            // wait some time after _updateEditorState() has changed window.location.href before _informApplicationTextChanged() also manipulates it
            setTimeout(editor._textChanged, 100);
        }
    },

    _textChanged: function() {
        if(this.informOfEachTextChange) {
            this._informApplicationTextChanged()
        }
    },

    _informApplicationTextChanged: function() {
        window.location.href = "text-changed-callback://" + editor.getEncodedHtml();
    },


    getHtml: function() {
        return this.textField.innerHTML;
    },

    getEncodedHtml: function() {
        return encodeURI(this.getHtml());
    },

    setHtml: function(html) {
        this.textField.innerHTML = decodeURIComponent(html.replace(/\+/g, '%20'));

        this.didHtmlChange = false;
        this.htmlSetByApplication = this.textField.innerHTML;
    },

    setInformOfEachTextChange: function(informOfEachTextChange) {
        this.informOfEachTextChange = informOfEachTextChange
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

           this._textChanged();
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

        this._textChanged();
    },
    
    
    /*      Editor default settings     */
    
    setBaseTextColor: function(color) {
        this.textField.style.color  = color;
    },

    setBaseFontFamily: function(fontFamily) {
        this.textField.style.fontFamily = fontFamily;
    },
    
    setBaseFontSize: function(size) {
        this.textField.style.fontSize = size;
    },
    
    setPadding: function(left, top, right, bottom) {
      this.textField.style.paddingLeft = left;
      this.textField.style.paddingTop = top;
      this.textField.style.paddingRight = right;
      this.textField.style.paddingBottom = bottom;
    },

    // TODO: is this one ever user?
    setBackgroundColor: function(color) {
        document.body.style.backgroundColor = color;
    },
    
    setBackgroundImage: function(image) {
        this.textField.style.backgroundImage = image;
    },
    
    setWidth: function(size) {
        this.textField.style.minWidth = size;
    },
    
    setHeight: function(size) {
        this.textField.style.height = size;
    },
    
    setTextAlign: function(align) {
        this.textField.style.textAlign = align;
    },
    
    setVerticalAlign: function(align) {
        this.textField.style.verticalAlign = align;
    },
    
    setPlaceholder: function(placeholder) {
        this.textField.setAttribute("placeholder", placeholder);
    },
    
    setInputEnabled: function(inputEnabled) {
        this.textField.contentEditable = String(inputEnabled);
    },

    focus: function() {
        var range = document.createRange();
        range.selectNodeContents(this.textField);
        range.collapse(false);
        var selection = window.getSelection();
        selection.removeAllRanges();
        selection.addRange(range);
        this.textField.focus();
    },

    blurFocus: function() {
        this.textField.blur();
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


        var didHtmlChange = this.htmlSetByApplication != null && this.htmlSetByApplication != this.getHtml();

        var editorState = {
            'didHtmlChange': didHtmlChange,
            'commandStates': commandStates
        };

        window.location.href = "editor-state-changed-callback://" + encodeURI(JSON.stringify(editorState));
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

          this.currentSelection = {
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
        range.setStart(this.currentSelection.startContainer, this.currentSelection.startOffset);
        range.setEnd(this.currentSelection.endContainer, this.currentSelection.endOffset);

        selection.addRange(range);
    },

}


editor.init();