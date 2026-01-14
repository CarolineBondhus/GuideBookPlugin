package net.tfminecraft.guides.enums;

public enum CharWidth {
    SPACE(' ', 4),

    EXCLAMATION('!', 2),
    QUOTE('"', 5),
    HASH('#', 6),
    DOLLAR('$', 6),
    PERCENT('%', 6),
    AMPERSAND('&', 6),
    APOSTROPHE('\'', 3),
    LEFT_PAREN('(', 5),
    RIGHT_PAREN(')', 5),
    ASTERISK('*', 5),
    PLUS('+', 6),
    COMMA(',', 2),
    MINUS('-', 6),
    PERIOD('.', 2),
    BULLET('•', 2),
    SLASH('/', 6),
    

    ZERO('0', 6),
    ONE('1', 6),
    TWO('2', 6),
    THREE('3', 6),
    FOUR('4', 6),
    FIVE('5', 6),
    SIX('6', 6),
    SEVEN('7', 6),
    EIGHT('8', 6),
    NINE('9', 6),

    COLON(':', 2),
    SEMICOLON(';', 2),
    LESS('<', 5),
    EQUAL('=', 6),
    GREATER('>', 5),
    QUESTION('?', 6),
    AT('@', 7),

    A('A', 6),
    B('B', 6),
    C('C', 6),
    D('D', 6),
    E('E', 6),
    F('F', 6),
    G('G', 6),
    H('H', 6),
    I('I', 4),
    J('J', 6),
    K('K', 6),
    L('L', 6),
    M('M', 6),
    N('N', 6),
    O('O', 6),
    P('P', 6),
    Q('Q', 6),
    R('R', 6),
    S('S', 6),
    T('T', 6),
    U('U', 6),
    V('V', 6),
    W('W', 6),
    X('X', 6),
    Y('Y', 6),
    Z('Z', 6),

    LEFT_BRACKET('[', 4),
    RIGHTBRACKET(']', 4),
    CARET('^', 6),
    UNDERSCORE('_', 6),
    GRAVE('`', 3),

    a('a', 6),
    b('b', 6),
    c('c', 6),
    d('d', 6),
    e('e', 6),
    f('f', 5),
    g('g', 6),
    h('h', 6),
    i('i', 2),
    j('j', 6),
    k('k', 5),
    l('l', 3),
    m('m', 6),
    n('n', 6),
    o('o', 6),
    p('p', 6),
    q('q', 6),
    r('r', 6),
    s('s', 6),
    t('t', 4),
    u('u', 6),
    v('v', 6),
    w('w', 6),
    x('x', 6),
    y('y', 6),
    z('z', 6),

    LEFT_BRACE('{', 5),
    PIPE('|', 2),
    RIGHT_BRACE('}', 5),
    TILDE('~', 7),

    UNKNOWN('?', 6);

    public final char character;
    public final int width;

    CharWidth(char character, int width) {
        this.character = character;
        this.width = width;
    }

    public static int getWidth(char c, boolean bold) {
        for (CharWidth v : values()) {
            if (v.character == c) {
                return v.width + (bold ? 1 : 0);
            }
        }
        return UNKNOWN.width + (bold ? 1 : 0);
    }
}
