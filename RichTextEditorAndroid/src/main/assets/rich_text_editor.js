
var editor = {

    textField: document.getElementById('editor'),

    currentSelection: {
        "startContainer": 0,
        "startOffset": 0,
        "endContainer": 0,
        "endOffset": 0
    },


    init: function() {
        this.textField.addEventListener("input", this.textChangedCallback);

        document.addEventListener("selectionchange", function() { editor._backupRange(); });

        this.textField.addEventListener("click", this.updateCommandStates);
        this.textField.addEventListener("keyup", function(e) {
            var KEY_LEFT = 37, KEY_RIGHT = 39;
            if (e.which == KEY_LEFT || e.which == KEY_RIGHT) {
                editor.updateCommandStates(e);
            }
        });

        this.ensureEditorInsertsParagraphWhenPressingEnter();
    },

    ensureEditorInsertsParagraphWhenPressingEnter: function() {
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


    textChangedCallback: function() {
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
    
    setSubscript: function() {
        this._executeCommand('subscript', null);
    },
    
    setSuperscript: function() {
        this._executeCommand('superscript', null);
    },
    
    setStrikeThrough: function() {
        this._executeCommand('strikeThrough', null);
    },
    
    setUnderline: function() {
        this._executeCommand('underline', null);
    },

    setTextColor: function(color) {
        this._executeStyleCommand('foreColor', color);
    },

    setTextBackgroundColor: function(color) {
        this._executeStyleCommand('hiliteColor', color);
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
       }

       this.textChangedCallback();
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
    },


    updateCommandStates: function(e) {
        var items = [];
        if (document.queryCommandState('undo')) {
            items.push('undo');
        }
        if (document.queryCommandState('redo')) {
            items.push('redo');
        }
        if (document.queryCommandState('bold')) {
            items.push('bold');
        }
        if (document.queryCommandState('italic')) {
            items.push('italic');
        }
        if (document.queryCommandState('underline')) {
            items.push('underline');
        }
        if (document.queryCommandState('subscript')) {
            items.push('subscript');
        }
        if (document.queryCommandState('superscript')) {
            items.push('superscript');
        }
        if (document.queryCommandState('strikeThrough')) {
            items.push('strikeThrough');
        }
        if (document.queryCommandState('indent')) {
            items.push('indent');
        }
        if (document.queryCommandState('indent')) {
            items.push('indent');
        }
        if (document.queryCommandState('outdent')) {
            items.push('outdent');
        }
        if (document.queryCommandState('justifyCenter')) {
            items.push('justifyCenter');
        }
        if (document.queryCommandState('justifyFull')) {
            items.push('justifyFull');
        }
        if (document.queryCommandState('justifyLeft')) {
            items.push('justifyLeft');
        }
        if (document.queryCommandState('justifyRight')) {
            items.push('justifyRight');
        }
        if (document.queryCommandState('insertOrderedList')) {
            items.push('orderedList');
        }
        if (document.queryCommandState('insertUnorderedList')) {
            items.push('unorderedList');
        }
        if (document.queryCommandState('insertHorizontalRule')) {
            items.push('horizontalRule');
        }
        var formatBlock = document.queryCommandValue('formatBlock');
        if (formatBlock.length > 0) {
            items.push(formatBlock);
        }

        window.location.href = "command-states-changed-callback://" + encodeURI(items.join(','));
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