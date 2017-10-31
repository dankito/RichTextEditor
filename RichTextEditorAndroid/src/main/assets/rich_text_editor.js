
var editor = {

    textField: document.getElementById('editor'),


    init: function() {
        this.textField.addEventListener("input", this.textChangedCallback)
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