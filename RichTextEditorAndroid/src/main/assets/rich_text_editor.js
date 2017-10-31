
var editor = {

    textField: document.getElementById('editor'),


    init: function() {
        this.textField.addEventListener("input", this.textChangedCallback)

        this.ensureEditorInsertsParagraphWhenPressingEnter()
    },

    ensureEditorInsertsParagraphWhenPressingEnter: function() {
        // see https://stackoverflow.com/a/36373967
        document.execCommand("DefaultParagraphSeparator", false, "p");

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
    }

}


editor.init();