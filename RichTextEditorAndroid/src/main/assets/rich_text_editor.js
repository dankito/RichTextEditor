
var editor = {

    textField: document.getElementById('editor'),


    init: function() {
        this.textField.addEventListener("input", this.textChangedCallback)

        this.ensureEditorInsertsParagraphWhenPressingEnter()
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
    
    
    /*      Text Editor Commands        */

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
    
    insertBulletList: function() {
        this._executeCommand('insertUnorderedList', null);
    },
    
    insertNumberedList: function() {
        this._executeCommand('insertOrderedList', null);
    },
    
    setIndent: function() {
        this._executeCommand('indent', null);
    },
    
    setOutdent: function() {
        this._executeCommand('outdent', null);
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
    
    setBlockquote: function() {
        this._executeCommand('formatBlock', '<blockquote>');
    },
    
    _executeCommand: function(command, parameter) {
        document.execCommand(command, false, parameter);
    },

}


editor.init();