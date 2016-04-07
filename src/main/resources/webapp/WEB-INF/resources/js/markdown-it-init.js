/**
 * @author jayfella
 */

var markdownConfig = {

    html: false,                    // Enable HTML tags in source
    xhtmlOut: false,                // Use '/' to close single tags (<br />)
    breaks: true,                   // Convert '\n' in paragraphs into <br>
    langPrefix: 'hljs language-',   // CSS language prefix for fenced blocks
    linkify: true,                  // autoconvert URL-like texts to links
    typographer: true,              // Enable smartypants and other sweet transforms

    highlight: function (str, lang) {

        if (lang && hljs.getLanguage(lang)) {
            try {
                return hljs.highlight(lang, str).value;
            } catch (__) {
            }
        }

        try {
            return hljs.highlightAuto(str).value;
        } catch (__) {
        }

        return ''; // use external default escaping
    }
};

var markdown = window.markdownit(markdownConfig)
    .use(window.markdownitAbbr)
    .use(window.markdownitDeflist)
    // .use(window.markdownitEmoji)
    .use(window.markdownitFootnote)
    .use(window.markdownitIns)
    .use(window.markdownitMark)
    .use(window.markdownitSub)
    .use(window.markdownitSup);

// Beautify output of parser for html content
markdown.renderer.rules.table_open = function ()
{
    return '<table class="table table-striped">\n';
};

// Replace emoji codes with images
markdown.renderer.rules.emoji = function(token, idx)
{
    return window.twemoji.parse(token[idx].to);
};

