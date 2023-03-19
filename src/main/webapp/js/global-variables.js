let selectedField = [];
const boardColor = "#a8a098";
const scale = 50;
const hexagonHeight = scale * Math.cos(Math.PI / 6) * 2;
const hexagonWidth = 1.5 * scale;
const hexagons = {};
const direction = {
    x:0,
    y:0
}
const fieldDefinition = {
    a: 5,
    b: 6,
    c: 7,
    d: 8,
    e: 9,
    f: 8,
    g: 7,
    h: 6,
    i: 5,
};

let gameState = {
    white: ["i5", "i6", "i7", "i8", "i9", "h4", "h5", "h6", "h7", "h8", "h9", "g5", "g6", "g7"],
    black: ["a1", "a2", "a3", "a4", "a5", "b1", "b2", "b3", "b4", "b5", "b6", "c3", "c4", "c5"],
};


